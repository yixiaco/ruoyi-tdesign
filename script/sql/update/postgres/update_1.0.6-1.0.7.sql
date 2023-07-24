-- 消息模板发送记录耗时时间
ALTER TABLE sys_message_log ADD cost_time int8 default null;
COMMENT ON COLUMN sys_message_log.cost_time IS '消耗时间';
