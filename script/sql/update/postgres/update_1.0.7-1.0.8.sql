-- 修改sys_oss表结构
alter table sys_oss add content_type varchar(255);
alter table sys_oss add oss_category_id int8 default 0 not null;
alter table sys_oss add user_type varchar(20) default ''::varchar not null;
alter table sys_oss add is_lock int2 default 0 not null;
comment on column sys_oss.content_type is '内容类型';
comment on column sys_oss.oss_category_id is '分类id';
comment on column sys_oss.user_type is '用户类型';
comment on column sys_oss.is_lock is '是否锁定状态';
create index idx_oss_category_id on sys_oss (oss_category_id);
create index idx_user on sys_oss (create_by, user_type, create_time);
comment on index idx_oss_category_id is '分类索引';
comment on index idx_user is '用户索引';

-- ----------------------------
-- OSS分类表
-- ----------------------------
drop table if exists sys_oss_category;
create table sys_oss_category
(
    oss_category_id int8          not null,
    category_name   varchar(255)  not null,
    parent_id       int8          not null,
    category_path   varchar(2000) not null,
    level           int4          not null,
    order_num       int4          not null,
    user_type       varchar(20)   not null,
    create_by       int8          not null,
    update_time     timestamp,
    create_time     timestamp     not null
);
comment on table sys_oss_category is 'OSS分类表';
comment on column sys_oss_category.oss_category_id is 'oss分类id';
comment on column sys_oss_category.category_name is '分类名称';
comment on column sys_oss_category.parent_id is '父级分类id';
comment on column sys_oss_category.category_path is '分类路径';
comment on column sys_oss_category.level is '层级';
comment on column sys_oss_category.order_num is '显示顺序';
comment on column sys_oss_category.user_type is '用户类型';
comment on column sys_oss_category.create_by is '上传人';
comment on column sys_oss_category.update_time is '更新时间';
comment on column sys_oss_category.create_time is '创建时间';

-- 系统菜单
insert into sys_menu values('1531', '我的文件', '1510', 4, 'ossCategory', 'system/ossCategory/index', NULL, 0, 1, 'C', '1', '1', 'system:ossCategory:list', 'folder-open', 103, 1, now(), 1, now(), '我的文件菜单');
insert into sys_menu values('1532', 'OSS分类查询', '1531', 1, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:ossCategory:query', '#', 103, 1, now(), NULL, NULL, '');
insert into sys_menu values('1533', 'OSS分类新增', '1531', 2, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:ossCategory:add', '#', 103, 1, now(), NULL, NULL, '');
insert into sys_menu values('1534', 'OSS分类修改', '1531', 3, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:ossCategory:edit', '#', 103, 1, now(), NULL, NULL, '');
insert into sys_menu values('1535', 'OSS分类删除', '1531', 4, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:ossCategory:remove', '#', 103, 1, now(), NULL, NULL, '');
insert into sys_menu values('1604', '文件修改', '118', 5, '', NULL, NULL, 0, 1, 'F', '1', '1', 'system:oss:edit', '#', 103, 1, now(), 1, now(), '');
update sys_menu set icon = 'tree-square-dot' where menu_id = 103;
update sys_menu set icon = 'book' where menu_id = 105;
update sys_menu set icon = 'backup' where menu_id = 118;
