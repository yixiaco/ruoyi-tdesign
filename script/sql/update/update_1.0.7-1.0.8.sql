ALTER TABLE sys_oss
ADD COLUMN oss_category_id bigint NOT NULL DEFAULT 0 COMMENT '分类id' AFTER size,
ADD COLUMN user_type varchar(20) NOT NULL COMMENT '用户类型' AFTER oss_category_id,
ADD COLUMN is_lock tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否锁定状态' AFTER user_type,
ADD COLUMN is_list tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否显示在列表' AFTER is_lock;

-- ----------------------------
-- Table structure for sys_oss_category
-- ----------------------------
DROP TABLE IF EXISTS sys_oss_category;
CREATE TABLE sys_oss_category  (
  oss_category_id bigint(20) NOT NULL COMMENT 'oss分类id',
  category_name varchar(255) NOT NULL COMMENT '分类名称',
  parent_id bigint(20) NOT NULL COMMENT '父级分类id',
  order_num int(11) NOT NULL COMMENT '显示顺序',
  user_type varchar(20) NOT NULL COMMENT '用户类型',
  create_by bigint(20) NOT NULL COMMENT '上传人',
  update_time datetime NULL DEFAULT NULL COMMENT '更新时间',
  create_time datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (oss_category_id) USING BTREE
) ENGINE = InnoDB COMMENT = 'OSS分类表' ROW_FORMAT = Dynamic;
