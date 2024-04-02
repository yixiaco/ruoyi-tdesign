insert into sys_dict_data values(61, '000000', 14, '七牛云短信', 'QI_NIU', 'sys_message_supplier_type', null, 'primary', null, 'N', 103, 1, now(), 1, now(), null);

alter table sys_menu add hidden_expression varchar(255);
alter table sys_menu add shop_expression varchar(255);
comment on column sys_menu.hidden_expression is '隐藏表达式';
comment on column sys_menu.shop_expression is '停用表达式';

UPDATE sys_menu SET shop_expression = 'getProperty(''spring.profiles.active'') != ''dev''' WHERE menu_id = 3;
UPDATE sys_menu SET shop_expression = 'getProperty(''spring.profiles.active'') != ''dev''' WHERE menu_id = 4;
UPDATE sys_menu SET shop_expression = '!getProperty(''tenant.enable'')' WHERE menu_id = 6;
UPDATE sys_menu SET shop_expression = '!getProperty(''spring.boot.admin.client.enabled'')' WHERE menu_id = 117;
UPDATE sys_menu SET shop_expression = '!getProperty(''powerjob.worker.enabled'')' WHERE menu_id = 120;
