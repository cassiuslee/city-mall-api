-- V1__init_schema.sql

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================================================
-- 商品分类表
-- =========================================================
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`
(
    `f_id`                  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
    `pid`                   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '父分类ID',
    `category_name`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
    `category_full_name`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         DEFAULT NULL COMMENT '分类全名',
    `category_code`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         DEFAULT NULL COMMENT '分类编码',
    `category_icon`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         DEFAULT NULL COMMENT '分类图标',
    `category_picture`      json                                                                  DEFAULT NULL COMMENT '分类图片',
    `category_level`        tinyint unsigned                                                      DEFAULT NULL COMMENT '层级：1一级 2二级 3三级',
    `sort_order`            int                                                          NOT NULL DEFAULT 0 COMMENT '排序值',
    `c_status`              varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '1' COMMENT '状态',
    `is_show`               varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '1' COMMENT '是否展示',
    `is_index`              varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '0' COMMENT '首页推荐',
    `remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         DEFAULT NULL COMMENT '描述',

    `f_tenant_id`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '租户id',
    `f_delete_mark`         int                                                          NOT NULL DEFAULT 0 COMMENT '删除标志',
    `f_delete_time`         datetime                                                              DEFAULT NULL COMMENT '删除时间',
    `f_delete_user_id`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '删除用户',
    `f_version`             int                                                          NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `f_flow_id`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '流程id',
    `f_flow_task_id`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '流程任务主键',
    `f_flow_state`          int                                                                   DEFAULT NULL COMMENT '流程任务状态',
    `f_creator_user_id`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '创建用户',
    `f_last_modify_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '修改用户',
    `f_creator_time`        datetime                                                              DEFAULT NULL COMMENT '创建时间',
    `f_last_modify_time`    datetime                                                              DEFAULT NULL COMMENT '修改时间',

    PRIMARY KEY (`f_id`),
    UNIQUE KEY `uk_category_code` (`category_code`),
    KEY `idx_category_pid` (`pid`),
    KEY `idx_category_level` (`category_level`),
    KEY `idx_category_status` (`c_status`),
    KEY `idx_category_delete_mark` (`f_delete_mark`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='商品分类表';


-- =========================================================
-- 轮播广告表
-- =========================================================
DROP TABLE IF EXISTS `cms_banner`;
CREATE TABLE `cms_banner`
(
    `f_id`                  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '主键',
    `banner_name`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '轮播图名称',
    `banner_desc`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '轮播图描述',
    `banner_url`            varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '轮播图地址',
    `jump_url`              varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '跳转地址',
    `distribution_site`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '投放位置',
    `sort_order`            int                                                           NOT NULL DEFAULT 0 COMMENT '排序值',
    `is_show`               varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NOT NULL DEFAULT '1' COMMENT '是否显示',
    `start_time`            datetime                                                               DEFAULT NULL COMMENT '开始时间',
    `end_time`              datetime                                                               DEFAULT NULL COMMENT '结束时间',
    `remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '备注',

    `f_tenant_id`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '租户id',
    `f_delete_mark`         int                                                           NOT NULL DEFAULT 0 COMMENT '删除标志',
    `f_delete_time`         datetime                                                               DEFAULT NULL COMMENT '删除时间',
    `f_delete_user_id`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '删除用户',
    `f_version`             int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `f_flow_id`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '流程id',
    `f_flow_task_id`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '流程任务主键',
    `f_flow_state`          int                                                                    DEFAULT NULL COMMENT '流程任务状态',
    `f_creator_user_id`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '创建用户',
    `f_last_modify_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '修改用户',
    `f_creator_time`        datetime                                                               DEFAULT NULL COMMENT '创建时间',
    `f_last_modify_time`    datetime                                                               DEFAULT NULL COMMENT '修改时间',

    PRIMARY KEY (`f_id`),
    KEY `idx_cms_banner_site` (`distribution_site`),
    KEY `idx_cms_banner_show` (`is_show`),
    KEY `idx_cms_banner_time` (`start_time`, `end_time`),
    KEY `idx_cms_banner_delete_mark` (`f_delete_mark`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='轮播广告表';


-- =========================================================
-- 公司主体管理表
-- =========================================================
DROP TABLE IF EXISTS `company_manage`;
CREATE TABLE `company_manage`
(
    `f_id`                  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '主键',
    `company_name`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公司名称',
    `company_short_name`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '公司简称',
    `company_code`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '公司编码',
    `credit_code`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '统一社会信用代码',
    `company_logo`          varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '公司logo',
    `company_intro`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '公司介绍',
    `contact_name`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '联系人',
    `contact_mobile`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '联系电话',
    `address`               varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '地址',
    `c_status`              varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NOT NULL DEFAULT '1' COMMENT '状态',
    `sort_order`            int                                                           NOT NULL DEFAULT 0 COMMENT '排序值',
    `remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '备注',

    `f_tenant_id`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '租户id',
    `f_delete_mark`         int                                                           NOT NULL DEFAULT 0 COMMENT '删除标志',
    `f_delete_time`         datetime                                                               DEFAULT NULL COMMENT '删除时间',
    `f_delete_user_id`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '删除用户',
    `f_version`             int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `f_flow_id`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '流程id',
    `f_flow_task_id`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '流程任务主键',
    `f_flow_state`          int                                                                    DEFAULT NULL COMMENT '流程任务状态',
    `f_creator_user_id`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '创建用户',
    `f_last_modify_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '修改用户',
    `f_creator_time`        datetime                                                               DEFAULT NULL COMMENT '创建时间',
    `f_last_modify_time`    datetime                                                               DEFAULT NULL COMMENT '修改时间',

    PRIMARY KEY (`f_id`),
    UNIQUE KEY `uk_company_manage_name` (`company_name`),
    UNIQUE KEY `uk_company_manage_code` (`company_code`),
    KEY `idx_company_manage_status` (`c_status`),
    KEY `idx_company_manage_delete_mark` (`f_delete_mark`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='公司主体管理表';


-- =========================================================
-- 企业标准表
-- =========================================================
DROP TABLE IF EXISTS `enterprise_standard`;
CREATE TABLE `enterprise_standard`
(
    `f_id`                  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '主键',
    `enterprise_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '企业名称',
    `enterprise_code`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '企业编码',
    `enterprise_logo`       varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '企业logo',
    `enterprise_intro`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '企业介绍',
    `c_status`              varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NOT NULL DEFAULT '1' COMMENT '状态',
    `sort_order`            int                                                           NOT NULL DEFAULT 0 COMMENT '排序值',
    `remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '备注',

    `f_tenant_id`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '租户id',
    `f_delete_mark`         int                                                           NOT NULL DEFAULT 0 COMMENT '删除标志',
    `f_delete_time`         datetime                                                               DEFAULT NULL COMMENT '删除时间',
    `f_delete_user_id`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '删除用户',
    `f_version`             int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `f_flow_id`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '流程id',
    `f_flow_task_id`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '流程任务主键',
    `f_flow_state`          int                                                                    DEFAULT NULL COMMENT '流程任务状态',
    `f_creator_user_id`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '创建用户',
    `f_last_modify_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '修改用户',
    `f_creator_time`        datetime                                                               DEFAULT NULL COMMENT '创建时间',
    `f_last_modify_time`    datetime                                                               DEFAULT NULL COMMENT '修改时间',

    PRIMARY KEY (`f_id`),
    UNIQUE KEY `uk_enterprise_standard_name` (`enterprise_name`),
    UNIQUE KEY `uk_enterprise_standard_code` (`enterprise_code`),
    KEY `idx_enterprise_standard_status` (`c_status`),
    KEY `idx_enterprise_standard_delete_mark` (`f_delete_mark`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='企业标准表';


-- =========================================================
-- 商品品牌表
-- =========================================================
DROP TABLE IF EXISTS `goods_brand`;
CREATE TABLE `goods_brand`
(
    `f_id`                  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '主键',
    `brand_name`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '品牌名称',
    `brand_code`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '品牌编码',
    `brand_logo`            varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '品牌logo',
    `brand_desc`            varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '品牌描述',
    `is_show`               varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NOT NULL DEFAULT '1' COMMENT '是否展示',
    `sort_order`            int                                                           NOT NULL DEFAULT 0 COMMENT '排序值',
    `remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '备注',

    `f_tenant_id`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '租户id',
    `f_delete_mark`         int                                                           NOT NULL DEFAULT 0 COMMENT '删除标志',
    `f_delete_time`         datetime                                                               DEFAULT NULL COMMENT '删除时间',
    `f_delete_user_id`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '删除用户',
    `f_version`             int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `f_flow_id`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '流程id',
    `f_flow_task_id`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '流程任务主键',
    `f_flow_state`          int                                                                    DEFAULT NULL COMMENT '流程任务状态',
    `f_creator_user_id`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '创建用户',
    `f_last_modify_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '修改用户',
    `f_creator_time`        datetime                                                               DEFAULT NULL COMMENT '创建时间',
    `f_last_modify_time`    datetime                                                               DEFAULT NULL COMMENT '修改时间',

    PRIMARY KEY (`f_id`),
    UNIQUE KEY `uk_goods_brand_name` (`brand_name`),
    UNIQUE KEY `uk_goods_brand_code` (`brand_code`),
    KEY `idx_goods_brand_show` (`is_show`),
    KEY `idx_goods_brand_delete_mark` (`f_delete_mark`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='商品品牌表';


-- =========================================================
-- 商品详情表
-- =========================================================
DROP TABLE IF EXISTS `goods_details`;
CREATE TABLE `goods_details`
(
    `f_id`                  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
    `spu_id`                varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品SPU主键',
    `goods_desc`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '商品描述',
    `test_report_files`     json                                                                  DEFAULT NULL COMMENT '检测报告附件',
    `goods_content`         longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '商品详情内容',
    `remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         DEFAULT NULL COMMENT '备注',

    `f_tenant_id`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '租户id',
    `f_delete_mark`         int                                                          NOT NULL DEFAULT 0 COMMENT '删除标志',
    `f_delete_time`         datetime                                                              DEFAULT NULL COMMENT '删除时间',
    `f_delete_user_id`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '删除用户',
    `f_version`             int                                                          NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `f_flow_id`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '流程id',
    `f_flow_task_id`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '流程任务主键',
    `f_flow_state`          int                                                                   DEFAULT NULL COMMENT '流程任务状态',
    `f_creator_user_id`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '创建用户',
    `f_last_modify_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '修改用户',
    `f_creator_time`        datetime                                                              DEFAULT NULL COMMENT '创建时间',
    `f_last_modify_time`    datetime                                                              DEFAULT NULL COMMENT '修改时间',

    PRIMARY KEY (`f_id`),
    UNIQUE KEY `uk_goods_details_spu_id` (`spu_id`),
    KEY `idx_goods_details_delete_mark` (`f_delete_mark`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='商品详情表';


-- =========================================================
-- 商品包装表
-- =========================================================
DROP TABLE IF EXISTS `goods_package`;
CREATE TABLE `goods_package`
(
    `f_id`                  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '主键',
    `package_name`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '包装名称',
    `package_code`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '包装编码',
    `package_desc`          varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '包装描述',
    `sort_order`            int                                                           NOT NULL DEFAULT 0 COMMENT '排序值',
    `remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '备注',

    `f_tenant_id`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '租户id',
    `f_delete_mark`         int                                                           NOT NULL DEFAULT 0 COMMENT '删除标志',
    `f_delete_time`         datetime                                                               DEFAULT NULL COMMENT '删除时间',
    `f_delete_user_id`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '删除用户',
    `f_version`             int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `f_flow_id`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '流程id',
    `f_flow_task_id`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '流程任务主键',
    `f_flow_state`          int                                                                    DEFAULT NULL COMMENT '流程任务状态',
    `f_creator_user_id`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '创建用户',
    `f_last_modify_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '修改用户',
    `f_creator_time`        datetime                                                               DEFAULT NULL COMMENT '创建时间',
    `f_last_modify_time`    datetime                                                               DEFAULT NULL COMMENT '修改时间',

    PRIMARY KEY (`f_id`),
    UNIQUE KEY `uk_goods_package_name` (`package_name`),
    UNIQUE KEY `uk_goods_package_code` (`package_code`),
    KEY `idx_goods_package_delete_mark` (`f_delete_mark`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='商品包装表';


-- =========================================================
-- 商品SKU表
-- =========================================================
DROP TABLE IF EXISTS `goods_sku`;
CREATE TABLE `goods_sku`
(
    `f_id`                  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '主键',
    `spu_id`                varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '商品SPU主键',
    `sku_code`              varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT 'SKU编码',
    `sku_name`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'SKU名称',
    `goods_spec_id`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '规格主键',
    `goods_package_id`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '包装主键',
    `sku_image`             varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT 'SKU图片',
    `sale_price`            decimal(10, 2)                                                         DEFAULT NULL COMMENT '销售价',
    `market_price`          decimal(10, 2)                                                         DEFAULT NULL COMMENT '市场价',
    `cost_price`            decimal(10, 2)                                                         DEFAULT NULL COMMENT '成本价',
    `stock_num`             int                                                           NOT NULL DEFAULT 0 COMMENT '库存',
    `lock_stock_num`        int                                                           NOT NULL DEFAULT 0 COMMENT '锁定库存',
    `sku_status`            varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NOT NULL DEFAULT '1' COMMENT 'SKU状态',
    `not_show_types`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '不可展示端',
    `sort_order`            int                                                           NOT NULL DEFAULT 0 COMMENT '排序值',
    `remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '备注',

    `f_tenant_id`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '租户id',
    `f_delete_mark`         int                                                           NOT NULL DEFAULT 0 COMMENT '删除标志',
    `f_delete_time`         datetime                                                               DEFAULT NULL COMMENT '删除时间',
    `f_delete_user_id`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '删除用户',
    `f_version`             int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `f_flow_id`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '流程id',
    `f_flow_task_id`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '流程任务主键',
    `f_flow_state`          int                                                                    DEFAULT NULL COMMENT '流程任务状态',
    `f_creator_user_id`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '创建用户',
    `f_last_modify_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '修改用户',
    `f_creator_time`        datetime                                                               DEFAULT NULL COMMENT '创建时间',
    `f_last_modify_time`    datetime                                                               DEFAULT NULL COMMENT '修改时间',

    PRIMARY KEY (`f_id`),
    UNIQUE KEY `uk_goods_sku_code` (`sku_code`),
    KEY `idx_goods_sku_spu_id` (`spu_id`),
    KEY `idx_goods_sku_spec_id` (`goods_spec_id`),
    KEY `idx_goods_sku_package_id` (`goods_package_id`),
    KEY `idx_goods_sku_status` (`sku_status`),
    KEY `idx_goods_sku_delete_mark` (`f_delete_mark`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='商品SKU表';


-- =========================================================
-- 商品SKU属性表
-- =========================================================
DROP TABLE IF EXISTS `goods_sku_attr`;
CREATE TABLE `goods_sku_attr`
(
    `f_id`                  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '主键',
    `goods_sku_id`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT 'SKU主键',
    `attr_name`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '属性名称',
    `attr_value`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '属性值',
    `sort_order`            int                                                           NOT NULL DEFAULT 0 COMMENT '排序值',
    `remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '备注',

    `f_tenant_id`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '租户id',
    `f_delete_mark`         int                                                           NOT NULL DEFAULT 0 COMMENT '删除标志',
    `f_delete_time`         datetime                                                               DEFAULT NULL COMMENT '删除时间',
    `f_delete_user_id`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '删除用户',
    `f_version`             int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `f_flow_id`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '流程id',
    `f_flow_task_id`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '流程任务主键',
    `f_flow_state`          int                                                                    DEFAULT NULL COMMENT '流程任务状态',
    `f_creator_user_id`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '创建用户',
    `f_last_modify_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '修改用户',
    `f_creator_time`        datetime                                                               DEFAULT NULL COMMENT '创建时间',
    `f_last_modify_time`    datetime                                                               DEFAULT NULL COMMENT '修改时间',

    PRIMARY KEY (`f_id`),
    KEY `idx_goods_sku_attr_sku_id` (`goods_sku_id`),
    KEY `idx_goods_sku_attr_delete_mark` (`f_delete_mark`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='商品SKU属性表';


-- =========================================================
-- 商品与分类关系表
-- =========================================================
DROP TABLE IF EXISTS `goods_category_relation`;
CREATE TABLE `goods_category_relation`
(
    `f_id`                  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
    `spu_id`                varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品SPU主键',
    `category_id`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类主键',
    `sort_order`            int                                                          NOT NULL DEFAULT 0 COMMENT '排序值',
    `remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         DEFAULT NULL COMMENT '备注',

    `f_tenant_id`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '租户id',
    `f_delete_mark`         int                                                          NOT NULL DEFAULT 0 COMMENT '删除标志',
    `f_delete_time`         datetime                                                              DEFAULT NULL COMMENT '删除时间',
    `f_delete_user_id`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '删除用户',
    `f_version`             int                                                          NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `f_flow_id`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '流程id',
    `f_flow_task_id`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '流程任务主键',
    `f_flow_state`          int                                                                   DEFAULT NULL COMMENT '流程任务状态',
    `f_creator_user_id`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '创建用户',
    `f_last_modify_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '修改用户',
    `f_creator_time`        datetime                                                              DEFAULT NULL COMMENT '创建时间',
    `f_last_modify_time`    datetime                                                              DEFAULT NULL COMMENT '修改时间',

    PRIMARY KEY (`f_id`),
    UNIQUE KEY `uk_goods_category_relation` (`spu_id`, `category_id`),
    KEY `idx_goods_category_relation_spu_id` (`spu_id`),
    KEY `idx_goods_category_relation_category_id` (`category_id`),
    KEY `idx_goods_category_relation_delete_mark` (`f_delete_mark`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='商品与分类关系表';


-- =========================================================
-- 商品规格关系表
-- =========================================================
DROP TABLE IF EXISTS `goods_spec_relation`;
CREATE TABLE `goods_spec_relation`
(
    `f_id`                  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '主键',
    `spu_id`                varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '商品SPU主键',
    `spec_name`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '规格名称',
    `spec_value`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '规格值',
    `goods_package_id`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '包装主键',
    `sort_order`            int                                                           NOT NULL DEFAULT 0 COMMENT '排序值',
    `remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '备注',

    `f_tenant_id`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '租户id',
    `f_delete_mark`         int                                                           NOT NULL DEFAULT 0 COMMENT '删除标志',
    `f_delete_time`         datetime                                                               DEFAULT NULL COMMENT '删除时间',
    `f_delete_user_id`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '删除用户',
    `f_version`             int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `f_flow_id`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '流程id',
    `f_flow_task_id`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '流程任务主键',
    `f_flow_state`          int                                                                    DEFAULT NULL COMMENT '流程任务状态',
    `f_creator_user_id`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '创建用户',
    `f_last_modify_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '修改用户',
    `f_creator_time`        datetime                                                               DEFAULT NULL COMMENT '创建时间',
    `f_last_modify_time`    datetime                                                               DEFAULT NULL COMMENT '修改时间',

    PRIMARY KEY (`f_id`),
    KEY `idx_goods_spec_relation_spu_id` (`spu_id`),
    KEY `idx_goods_spec_relation_package_id` (`goods_package_id`),
    KEY `idx_goods_spec_relation_delete_mark` (`f_delete_mark`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='商品规格关系表';

SET FOREIGN_KEY_CHECKS = 1;