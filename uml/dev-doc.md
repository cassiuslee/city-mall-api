## 改表 checklist

1. 新建迁移 SQL，不改旧版本
2. 改 Entity
3. 改 DTO / VO
4. 改 Mapper / XML
5. 改 Service / Controller
6. 改 @Schema 文档注释
7. 本地启动验证
8. 接口联调验证

## 接口开发模板

### 第一步：先明确接口信息

#### 把这 5 个问题先写清楚：

- 接口名称是什么
- 请求方式是什么：GET / POST / PUT / DELETE
- 入参有哪些
- 返回什么结构
- 是单表还是多表

```
例如：

接口：分页查询用户及所属主体
方法：GET
路径：/customer/member-user/page-with-markets
入参：mobile、nickname、markName、current、size
返回：分页 + 用户 + 主体列表
类型：多表 join
```

### 第二步：先写 DTO / VO
```
不要一上来写 SQL。

因为：

DTO 决定前端怎么传
VO 决定前端怎么接

这个定好以后，后面的 mapper 和 service 才不会反复改。
```
### 第三步：再写 Mapper / XML

```
单表用 MP，复杂查询用 XML。

你的项目里后面大部分真正有价值的接口，都会涉及：

用户
关系表
主体
类型表

所以 join XML 基本跑不掉。
```

### 第四步：写 Service

```
Service 里做：

参数处理
查询调用
结果组装
异常处理

注意：
复杂聚合逻辑尽量不要直接写在 controller。
```

### 第五步：最后写 Controller

```
controller 只做三件事：

接参数
调 service
返回统一结果
```

## 接口开发清单
1. 明确接口用途
2. 确定请求方法和路径
3. 写 DTO
4. 写 VO
5. 写 Mapper 方法
6. 写 XML / LambdaQueryWrapper
7. 写 Service
8. 写 Controller
9. 加 @Operation / @Schema 注释
10. 本地测试
11. Knife4j 检查文档