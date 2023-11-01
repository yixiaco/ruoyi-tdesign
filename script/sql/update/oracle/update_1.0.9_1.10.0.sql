-- 新增字典回显风格
alter table sys_dict_data add tag_style varchar(50);
comment on column sys_dict_data.tag_style is '回显风格';
UPDATE sys_dict_data SET list_class='text' WHERE list_class = 'default';
UPDATE sys_dict_data SET list_class='primary', tag_style='light-outline' WHERE dict_type = 'sys_grant_type';
