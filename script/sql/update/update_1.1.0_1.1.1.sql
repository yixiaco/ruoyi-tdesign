ALTER TABLE sys_config
MODIFY COLUMN config_value text NULL COMMENT '参数键值' AFTER config_key;
