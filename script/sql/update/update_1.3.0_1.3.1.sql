-- 字典不再支持多租户操作，转为通用表。删除其他租户的字典与数据 (若想继续保持字典多租户，请忽略此提交更新)
DELETE FROM sys_dict_type WHERE tenant_id != '000000';
DELETE FROM sys_dict_data WHERE tenant_id != '000000';
