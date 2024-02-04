alter table sys_config
alter column config_value type text using config_value::text;

alter table sys_menu add component_name varchar(255);
comment on column sys_menu.component_name is '组件名称';

UPDATE sys_menu SET component_name = 'User' WHERE menu_id = 100;
UPDATE sys_menu SET component_name = 'TenantPackage' WHERE menu_id = 122;
UPDATE sys_menu SET component_name = 'Tenant' WHERE menu_id = 121;
UPDATE sys_menu SET component_name = 'SysConfig' WHERE menu_id = 106;
UPDATE sys_menu SET component_name = 'Role' WHERE menu_id = 101;
UPDATE sys_menu SET component_name = 'Powerjob' WHERE menu_id = 120;
UPDATE sys_menu SET component_name = 'Post' WHERE menu_id = 104;
UPDATE sys_menu SET component_name = 'OssRule' WHERE menu_id = 1521;
UPDATE sys_menu SET component_name = 'OssConfig' WHERE menu_id = 1500;
UPDATE sys_menu SET component_name = 'OssCategory' WHERE menu_id = 1531;
UPDATE sys_menu SET component_name = 'Oss' WHERE menu_id = 118;
UPDATE sys_menu SET component_name = 'Operlog' WHERE menu_id = 500;
UPDATE sys_menu SET component_name = 'Online' WHERE menu_id = 109;
UPDATE sys_menu SET component_name = 'Notice' WHERE menu_id = 107;
UPDATE sys_menu SET component_name = 'MessageTemplate' WHERE menu_id = 1820;
UPDATE sys_menu SET component_name = 'MessageLog' WHERE menu_id = 1830;
UPDATE sys_menu SET component_name = 'MessageKey' WHERE menu_id = 1810;
UPDATE sys_menu SET component_name = 'MessageConfig' WHERE menu_id = 1802;
UPDATE sys_menu SET component_name = 'Menus' WHERE menu_id = 102;
UPDATE sys_menu SET component_name = 'Logininfor' WHERE menu_id = 501;
UPDATE sys_menu SET component_name = 'Gen' WHERE menu_id = 115;
UPDATE sys_menu SET component_name = 'Dict' WHERE menu_id = 105;
UPDATE sys_menu SET component_name = 'Dept' WHERE menu_id = 103;
UPDATE sys_menu SET component_name = 'Client' WHERE menu_id = 123;
UPDATE sys_menu SET component_name = 'Cache' WHERE menu_id = 113;
UPDATE sys_menu SET component_name = 'Build' WHERE menu_id = 114;
UPDATE sys_menu SET component_name = 'App' WHERE menu_id = 1701;
UPDATE sys_menu SET component_name = 'Admin' WHERE menu_id = 117;
