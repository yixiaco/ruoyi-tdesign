-- 新增字典回显风格
ALTER TABLE sys_dict_data
    ADD COLUMN tag_style varchar(50) NULL COMMENT '回显风格' AFTER list_class;
UPDATE sys_dict_data SET list_class='text' WHERE list_class = 'default';
UPDATE sys_dict_data SET list_class='primary', tag_style='light-outline' WHERE dict_type = 'sys_grant_type';
