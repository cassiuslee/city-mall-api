CREATE TABLE IF NOT EXISTS `member_identity`
(
    `f_id`                  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
    `identity_name`         varchar(255)                                                  DEFAULT NULL COMMENT '身份名称',
    `identity_code`         varchar(255)                                                  DEFAULT NULL COMMENT '身份编码',
    `identity_permissions`  json                                                          DEFAULT NULL COMMENT '身份权限',
    `identity_desc`         text                                                          DEFAULT NULL COMMENT '描述',
    `f_tenant_id`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '租户id',
    `f_delete_mark`         int                                                           DEFAULT 0 COMMENT '删除标志',
    `f_delete_time`         datetime                                                      DEFAULT NULL COMMENT '删除时间',
    `f_delete_user_id`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '删除用户',
    `f_version`             int                                                           DEFAULT 0 COMMENT '乐观锁',
    `f_flow_id`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '流程id',
    `f_flow_task_id`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '流程任务主键',
    `f_flow_state`          int                                                           DEFAULT NULL COMMENT '流程任务状态',
    `f_creator_user_id`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '创建用户',
    `f_last_modify_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '修改用户',
    `f_creator_time`        datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `f_last_modify_time`    datetime                                                      DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`f_id`) USING BTREE,
    KEY `idx_member_identity_code` (`identity_code`) USING BTREE,
    KEY `idx_member_identity_delete_mark` (`f_delete_mark`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '人员身份表';
