import argparse
import re
from dataclasses import dataclass, field
from pathlib import Path
from typing import Dict, List, Optional, Set, Tuple


@dataclass
class Column:
    name: str
    col_type: str
    comment: str = ''


@dataclass
class ForeignKey:
    child_table: str
    child_column: str
    parent_table: str
    parent_column: str
    constraint_name: Optional[str] = None


@dataclass
class Table:
    name: str
    columns: List[Column] = field(default_factory=list)
    primary_keys: List[str] = field(default_factory=list)
    foreign_keys: List[ForeignKey] = field(default_factory=list)

    def column_names(self) -> Set[str]:
        return {c.name for c in self.columns}


CREATE_TABLE_RE = re.compile(
    r"CREATE\s+TABLE\s+`(?P<name>[^`]+)`\s*\((?P<body>.*?)\)\s*ENGINE\s*=.*?;",
    re.IGNORECASE | re.DOTALL,
)


def _split_sql_body_lines(body: str) -> List[str]:
    lines = []
    for raw in body.splitlines():
        line = raw.strip()
        if not line:
            continue
        if line.endswith(','):
            line = line[:-1].rstrip()
        lines.append(line)
    return lines


STOP_WORDS = {
    'not',
    'null',
    'default',
    'comment',
    'primary',
    'unique',
    'key',
    'constraint',
    'references',
    'auto_increment',
    'collate',
}


NON_COLUMN_PREFIXES = {
    'primary',
    'unique',
    'key',
    'constraint',
    'foreign',
}


def _parse_column_type(after_name: str) -> str:
    tokens = after_name.strip().split()
    acc: List[str] = []
    for t in tokens:
        if t.lower() in STOP_WORDS:
            break
        acc.append(t)
    return ' '.join(acc) if acc else ''


def _parse_column_comment(rest: str) -> str:
    m = re.search(r"\bCOMMENT\s+'(?P<c>(?:\\'|[^'])*)'", rest, re.IGNORECASE)
    if not m:
        return ''
    return m.group('c').replace("\\'", "'").strip()


def _has_cjk(s: str) -> bool:
    return any('\u4e00' <= ch <= '\u9fff' for ch in s)


def _normalize_cn_comment(raw: str) -> str:
    c = (raw or '').strip()
    if not c:
        return '(无备注)'
    if _has_cjk(c):
        return c
    return f'备注: {c}'


def _parse_columns_from_line(line: str) -> Optional[Column]:
    first_token = line.split(None, 1)[0].strip('`').lower() if line.split(None, 1) else ''
    if first_token in NON_COLUMN_PREFIXES:
        return None
    m = re.match(r"^(?:`(?P<quoted_name>[^`]+)`|(?P<bare_name>[A-Za-z_][A-Za-z0-9_]*))\s+(?P<rest>.+)$", line)
    if not m:
        return None
    name = m.group('quoted_name') or m.group('bare_name')
    rest = m.group('rest')
    col_type = _parse_column_type(rest)
    if not col_type:
        return None
    comment = _normalize_cn_comment(_parse_column_comment(rest))
    return Column(name=name, col_type=col_type, comment=comment)


def _parse_primary_keys(line: str) -> List[str]:
    if not line.lower().startswith('primary key'):
        return []
    return re.findall(r"`([^`]+)`", line)


def _parse_foreign_key(table_name: str, line: str) -> Optional[ForeignKey]:
    if 'foreign key' not in line.lower() or 'references' not in line.lower():
        return None
    constraint_name = None
    cm = re.match(r"^CONSTRAINT\s+`(?P<cname>[^`]+)`\s+FOREIGN\s+KEY\s*\((?P<cols>[^)]+)\)\s+REFERENCES\s+`(?P<pt>[^`]+)`\s*\((?P<pcols>[^)]+)\)", line, re.IGNORECASE)
    if cm:
        constraint_name = cm.group('cname')
        child_cols = re.findall(r"`([^`]+)`", cm.group('cols'))
        parent_cols = re.findall(r"`([^`]+)`", cm.group('pcols'))
        if len(child_cols) == 1 and len(parent_cols) == 1:
            return ForeignKey(
                child_table=table_name,
                child_column=child_cols[0],
                parent_table=cm.group('pt'),
                parent_column=parent_cols[0],
                constraint_name=constraint_name,
            )
        return None

    fm = re.match(r"^FOREIGN\s+KEY\s*\((?P<cols>[^)]+)\)\s+REFERENCES\s+`(?P<pt>[^`]+)`\s*\((?P<pcols>[^)]+)\)", line, re.IGNORECASE)
    if not fm:
        return None
    child_cols = re.findall(r"`([^`]+)`", fm.group('cols'))
    parent_cols = re.findall(r"`([^`]+)`", fm.group('pcols'))
    if len(child_cols) != 1 or len(parent_cols) != 1:
        return None
    return ForeignKey(
        child_table=table_name,
        child_column=child_cols[0],
        parent_table=fm.group('pt'),
        parent_column=parent_cols[0],
        constraint_name=constraint_name,
    )


def parse_tables(sql_text: str) -> Dict[str, Table]:
    tables: Dict[str, Table] = {}
    for m in CREATE_TABLE_RE.finditer(sql_text):
        name = m.group('name')
        body = m.group('body')
        t = Table(name=name)

        for line in _split_sql_body_lines(body):
            col = _parse_columns_from_line(line)
            if col:
                t.columns.append(col)
                continue

            pk_cols = _parse_primary_keys(line)
            if pk_cols:
                t.primary_keys = pk_cols
                continue

            fk = _parse_foreign_key(name, line)
            if fk:
                t.foreign_keys.append(fk)
                continue

        tables[name] = t
    return tables


def _pick_inferred_parent_table(col_base: str, table_names: Set[str]) -> Optional[str]:
    if col_base in table_names:
        return col_base
    for suffix in ('_info', '_user'):
        cand = f'{col_base}{suffix}'
        if cand in table_names:
            return cand

    candidates = [
        t
        for t in table_names
        if t.endswith(col_base) or t == f'cms_{col_base}' or t == f'member_{col_base}'
    ]
    candidates = sorted(set(candidates))
    if len(candidates) == 1:
        return candidates[0]
    return None


def infer_relationships(tables: Dict[str, Table]) -> List[ForeignKey]:
    table_names = set(tables.keys())

    explicit: List[ForeignKey] = []
    explicit_child_cols: Set[Tuple[str, str]] = set()
    for t in tables.values():
        for fk in t.foreign_keys:
            explicit.append(fk)
            explicit_child_cols.add((fk.child_table, fk.child_column))

    inferred: List[ForeignKey] = []
    for t in tables.values():
        for c in t.columns:
            if not c.name.endswith('_id'):
                continue
            if (t.name, c.name) in explicit_child_cols:
                continue

            if c.name == 'parent_id' and t.name in table_names:
                inferred.append(
                    ForeignKey(
                        child_table=t.name,
                        child_column=c.name,
                        parent_table=t.name,
                        parent_column='f_id',
                        constraint_name=None,
                    ),
                )
                continue

            base = c.name[: -len('_id')]
            parent_table = _pick_inferred_parent_table(base, table_names)
            if not parent_table:
                continue
            parent_pk = 'f_id' if 'f_id' in tables[parent_table].column_names() else 'id'
            inferred.append(
                ForeignKey(
                    child_table=t.name,
                    child_column=c.name,
                    parent_table=parent_table,
                    parent_column=parent_pk,
                    constraint_name=None,
                ),
            )

    merged: Dict[Tuple[str, str, str, str], ForeignKey] = {}
    for fk in explicit + inferred:
        key = (fk.child_table, fk.child_column, fk.parent_table, fk.parent_column)
        if key not in merged:
            merged[key] = fk
        else:
            if merged[key].constraint_name is None and fk.constraint_name is not None:
                merged[key].constraint_name = fk.constraint_name
    return list(merged.values())


def _puml_escape(name: str) -> str:
    return name.replace('"', "'")


def _puml_escape_comment(comment: str) -> str:
    c = (comment or '').replace('\r', ' ').replace('\n', ' ').strip()
    return _puml_escape(c)


def to_plantuml(tables: Dict[str, Table], relationships: List[ForeignKey], title: str) -> str:
    lines: List[str] = []
    lines.append('@startuml')
    lines.append(f"' {title}")
    lines.append('hide circle')
    lines.append('skinparam linetype ortho')
    lines.append('')

    fk_by_table_col: Set[Tuple[str, str]] = {(fk.child_table, fk.child_column) for fk in relationships}

    for table_name in sorted(tables.keys()):
        t = tables[table_name]
        alias = table_name
        lines.append(f'entity "{_puml_escape(table_name)}" as {alias} {{')

        pk_set = set(t.primary_keys)
        for col in t.columns:
            prefix = ''
            if col.name in pk_set:
                prefix = '*'
            elif (t.name, col.name) in fk_by_table_col:
                prefix = '+'
            remark = _puml_escape_comment(col.comment)
            lines.append(f'  {prefix}{col.name} : {col.col_type} {remark}')
        lines.append('}')
        lines.append('')

    rel_lines: Set[str] = set()
    for fk in sorted(relationships, key=lambda x: (x.parent_table, x.child_table, x.child_column)):
        parent = fk.parent_table
        child = fk.child_table
        label = fk.child_column
        rel_lines.add(f'{parent} ||--o{{ {child} : {label}')
    for rl in sorted(rel_lines):
        lines.append(rl)
    lines.append('@enduml')
    lines.append('')
    return '\n'.join(lines)


def main() -> int:
    ap = argparse.ArgumentParser(description='Generate PlantUML ER diagram from MySQL CREATE TABLE DDL')
    ap.add_argument('sql_path', help='Input SQL file path')
    ap.add_argument('out_puml_path', help='Output .puml file path')
    args = ap.parse_args()

    sql_path = Path(args.sql_path)
    out_path = Path(args.out_puml_path)
    sql_text = sql_path.read_text(encoding='utf-8')

    tables = parse_tables(sql_text)
    relationships = infer_relationships(tables)

    title = f'Generated from {sql_path.as_posix()}'
    puml = to_plantuml(tables, relationships, title)

    out_path.parent.mkdir(parents=True, exist_ok=True)
    out_path.write_text(puml, encoding='utf-8')
    return 0


if __name__ == '__main__':
    raise SystemExit(main())
