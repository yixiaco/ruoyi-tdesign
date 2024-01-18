alter table sys_config
alter column config_value type text using config_value::text;
