insert into sys_dict_data values(61, '000000', 14, '七牛云短信', 'QI_NIU', 'sys_message_supplier_type', null, 'primary', null, 'N', 103, 1, sysdate(), 1, sysdate(), null);

alter table sys_menu
add column hidden_expression varchar(255) null comment '隐藏表达式' after icon,
add column shop_expression varchar(255) null comment '停用表达式' after hidden_expression;

UPDATE sys_menu SET shop_expression = 'getProperty(\'spring.profiles.active\') != \'dev\'' WHERE menu_id = 3;
UPDATE sys_menu SET shop_expression = 'getProperty(\'spring.profiles.active\') != \'dev\'' WHERE menu_id = 4;
UPDATE sys_menu SET shop_expression = '!getProperty(\'tenant.enable\')' WHERE menu_id = 6;
UPDATE sys_menu SET shop_expression = '!getProperty(\'spring.boot.admin.client.enabled\')' WHERE menu_id = 117;
UPDATE sys_menu SET shop_expression = '!getProperty(\'powerjob.worker.enabled\')' WHERE menu_id = 120;
