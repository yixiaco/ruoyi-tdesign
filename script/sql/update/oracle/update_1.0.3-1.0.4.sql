-- OSS配置新增自动创建桶字段 update by 2023-07-05
ALTER TABLE sys_oss_config ADD (create_bucket number(1) default 0 not null);
COMMENT ON COLUMN sys_oss_config.create_bucket IS '创建桶（1=是,0=否）';

-- 代码生成新增排序字段 update by 2023-07-07
ALTER TABLE gen_table_column ADD (is_sort char(1));
COMMENT ON COLUMN gen_table_column.is_sort IS '是否排序字段（1是）';
-- 新增部门、菜单导出 update by 2023-07-07
insert into sys_menu values('1117', '菜单导出', '102', '5',  '', '', '', 0, 1, 'F', '1', '1', 'system:menu:export',         '#', 103, 1, sysdate, null, null, '');
insert into sys_menu values('1121', '部门导出', '103', '5',  '', '', '', 0, 1, 'F', '1', '1', 'system:dept:export',         '#', 103, 1, sysdate, null, null, '');
