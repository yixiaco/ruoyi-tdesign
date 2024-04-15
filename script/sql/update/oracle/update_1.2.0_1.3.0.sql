insert into sys_dict_data values(61, '000000', 14, '七牛云短信', 'QI_NIU', 'sys_message_supplier_type', null, 'primary', null, 'N', 103, 1, sysdate, 1, sysdate, null);

alter table sys_menu add hidden_expression varchar(255);
alter table sys_menu add shop_expression varchar(255);
comment on column sys_menu.hidden_expression is '隐藏表达式';
comment on column sys_menu.shop_expression is '停用表达式';

UPDATE sys_menu SET shop_expression = 'getProperty(''spring.profiles.active'') != ''dev''' WHERE menu_id = 3;
UPDATE sys_menu SET shop_expression = 'getProperty(''spring.profiles.active'') != ''dev''' WHERE menu_id = 4;
UPDATE sys_menu SET shop_expression = '!getProperty(''tenant.enable'')' WHERE menu_id = 6;
UPDATE sys_menu SET shop_expression = '!getProperty(''spring.boot.admin.client.enabled'')' WHERE menu_id = 117;
UPDATE sys_menu SET shop_expression = '!getProperty(''powerjob.worker.enabled'')' WHERE menu_id = 120;

alter table sys_oss_rule add rule_sort number(4) default 0 not null;
comment on column sys_oss_rule.rule_sort is '规则顺序';

alter table sys_logininfor add user_id number(20);
comment on column sys_logininfor.user_id is '用户id';

alter table sys_oper_log add user_id number(20);
comment on column sys_oper_log.user_id is '用户id';

-- 如果启用PowerJob则执行以下脚本
-- Upgrade PowerJob SQL FROM 4.3.7 to 4.3.8
-- ----------------------------
-- Table change for pj_job_info
-- ----------------------------
-- alter table "pj_job_info" add "dispatch_strategy_config" varchar(255);
-- alter table "pj_job_info" add "advanced_runtime_config" varchar(255);
