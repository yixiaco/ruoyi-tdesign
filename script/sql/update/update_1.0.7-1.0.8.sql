ALTER TABLE sys_oss
ADD COLUMN content_type varchar(255) NULL COMMENT '内容类型' AFTER size,
ADD COLUMN oss_category_id bigint NOT NULL DEFAULT 0 COMMENT '分类id' AFTER content_type,
ADD COLUMN user_type varchar(20) NOT NULL COMMENT '用户类型' AFTER oss_category_id,
ADD COLUMN is_lock tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否锁定状态' AFTER user_type;
ALTER TABLE sys_oss
ADD INDEX idx_user(create_by, user_type, create_time) COMMENT '用户索引',
ADD INDEX idx_oss_category_id(oss_category_id) COMMENT '分类索引';

-- ----------------------------
-- Table structure for sys_oss_category
-- ----------------------------
DROP TABLE IF EXISTS sys_oss_category;
CREATE TABLE sys_oss_category  (
  oss_category_id bigint(20) NOT NULL COMMENT 'oss分类id',
  category_name varchar(255) NOT NULL COMMENT '分类名称',
  parent_id bigint(20) NOT NULL COMMENT '父级分类id',
  category_path varchar(2000) NOT NULL COMMENT '分类路径',
  level int(11) NOT NULL COMMENT '层级',
  order_num int(11) NOT NULL COMMENT '显示顺序',
  user_type varchar(20) NOT NULL COMMENT '用户类型',
  create_by bigint(20) NOT NULL COMMENT '上传人',
  update_time datetime NULL DEFAULT NULL COMMENT '更新时间',
  create_time datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (oss_category_id) USING BTREE
) ENGINE = InnoDB COMMENT = 'OSS分类表';
