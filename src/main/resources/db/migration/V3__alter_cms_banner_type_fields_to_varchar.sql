ALTER TABLE `cms_banner`
    MODIFY COLUMN `distribution_site` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '投放位置：1首页 2分类页',
    MODIFY COLUMN `c_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转类型',
    MODIFY COLUMN `is_show` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否展示';