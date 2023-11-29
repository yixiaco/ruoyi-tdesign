-- 新增字典回显风格
alter table sys_dict_data add tag_style varchar(50);
comment on column sys_dict_data.tag_style is '回显风格';
UPDATE sys_dict_data SET list_class='text' WHERE list_class = 'default';
UPDATE sys_dict_data SET list_class='primary', tag_style='light-outline' WHERE dict_type = 'sys_grant_type';

insert into sys_dict_data values(58, '000000', 11, '助通短信', 'ZHUTONG', 'sys_message_supplier_type', null, 'primary', '', 'N', 103, 1, now(), 1, now(), null);

alter table sys_message_log drop column error_code;
alter table sys_message_log drop column error_message;
alter table sys_message_log drop column biz_id;
alter table sys_message_log drop column message;
alter table sys_message_log add response_body varchar(1000);
comment on column sys_message_log.response_body is '返回主体消息';

ALTER TABLE sys_logininfor ADD client_key varchar(32) default ''::varchar;
COMMENT ON COLUMN sys_logininfor.client_key IS '客户端';
ALTER TABLE sys_logininfor ADD device_type varchar(32) default ''::varchar;
COMMENT ON COLUMN sys_logininfor.device_type IS '设备类型';

UPDATE sys_dict_data SET list_class='primary' WHERE dict_type = 'sys_device_type';

-- ----------------------------
-- 租户套餐和菜单关联表
-- ----------------------------
CREATE TABLE sys_tenant_package_menu (
  package_id    int8 NOT NULL,
  menu_id       int8 NOT NULL
);
COMMENT ON COLUMN sys_tenant_package_menu.package_id    IS '租户套餐id';
COMMENT ON COLUMN sys_tenant_package_menu.menu_id       IS '菜单id';
COMMENT ON TABLE sys_tenant_package_menu                IS '租户套餐和菜单关联表';
ALTER TABLE sys_tenant_package_menu ADD PRIMARY KEY (package_id, menu_id);

insert into sys_tenant_package_menu
select t1.package_id, t2.menu_id
from sys_menu t2
         join sys_tenant_package t1 on CAST(t2.menu_id AS VARCHAR) = ANY(STRING_TO_ARRAY(REPLACE(t1.menu_ids, ', ', ','), ','));

alter table sys_tenant_package drop column menu_ids;
