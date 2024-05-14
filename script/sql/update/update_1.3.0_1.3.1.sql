-- 字典不再支持多租户操作，转为通用表。删除其他租户的字典与数据 (若想继续保持字典多租户，请忽略此提交更新)
DELETE FROM sys_dict_type WHERE tenant_id != '000000';
DELETE FROM sys_dict_data WHERE tenant_id != '000000';

-- 修改应用表名为租户应用管理表
ALTER TABLE sys_app RENAME TO sys_tenant_app;
ALTER TABLE sys_tenant_app COMMENT = '租户应用管理表';

UPDATE sys_menu SET menu_name = '租户应用管理', path = 'tenantApp', component = 'system/tenantApp/index', component_name = 'TenantApp', perms = 'system:tenantApp:list', remark = '租户应用管理菜单' WHERE menu_id = 1701;
UPDATE sys_menu SET menu_name = '租户应用管理查询', perms = 'system:tenantApp:query' WHERE menu_id = 1702;
UPDATE sys_menu SET menu_name = '租户应用管理新增', perms = 'system:tenantApp:add' WHERE menu_id = 1703;
UPDATE sys_menu SET menu_name = '租户应用管理修改', perms = 'system:tenantApp:edit' WHERE menu_id = 1704;
UPDATE sys_menu SET menu_name = '租户应用管理删除', perms = 'system:tenantApp:remove' WHERE menu_id = 1705;
UPDATE sys_menu SET menu_name = '租户应用管理导出', perms = 'system:tenantApp:export' WHERE menu_id = 1706;
