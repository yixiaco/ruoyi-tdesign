
-- ----------------------------
-- 消息配置表
-- ----------------------------
create table sys_message_config (
  message_config_id number(20)      not null,
  title             varchar2(255)  not null,
  message_type      varchar2(20)   not null,
  supplier_type     varchar2(20)   not null,
  config_json       nclob,
  status            number(4)       not null,
  remark            varchar2(500),
  create_dept       number(20),
  update_by         number(20),
  create_by         number(20),
  update_time       date,
  create_time       date
);

alter table sys_message_config add constraint pk_sys_message_config primary key (message_config_id);
create index idx_sys_message_type on sys_message_config (message_type, status);
comment on table sys_message_config                     is '消息配置表';
comment on column sys_message_config.message_config_id  is '消息设置id';
comment on column sys_message_config.title              is '标题';
comment on column sys_message_config.message_type       is '消息类型 SMS、MAIL';
comment on column sys_message_config.supplier_type      is '支持平台标识';
comment on column sys_message_config.config_json        is '配置json';
comment on column sys_message_config.status             is '状态（1正常 0停用）';
comment on column sys_message_config.remark             is '备注';
comment on column sys_message_config.create_dept        is '创建部门';
comment on column sys_message_config.update_by          is '更新者';
comment on column sys_message_config.create_by          is '创建者';
comment on column sys_message_config.update_time        is '更新时间';
comment on column sys_message_config.create_time        is '创建时间';

-- ----------------------------
-- 消息常量表
-- ----------------------------
create table sys_message_key (
  message_key_id    number(20)      not null,
  name              varchar2(255)  not null,
  message_key       varchar2(50)   not null,
  remark            varchar2(500),
  create_dept       number(20),
  update_by         number(20),
  create_by         number(20),
  update_time       date,
  create_time       date
);

alter table sys_message_key add constraint pk_sys_message_key primary key (message_key_id);
comment on table sys_message_key                    is '消息常量表';
comment on column sys_message_key.message_key_id    is '消息key主键';
comment on column sys_message_key.name              is '消息名称';
comment on column sys_message_key.message_key       is '消息key';
comment on column sys_message_key.remark            is '备注';
comment on column sys_message_key.create_dept       is '创建部门';
comment on column sys_message_key.update_by         is '更新者';
comment on column sys_message_key.create_by         is '创建者';
comment on column sys_message_key.update_time       is '更新时间';
comment on column sys_message_key.create_time       is '创建时间';

-- ----------------------------
-- 消息发送记录表
-- ----------------------------
create table sys_message_log (
  message_log_id        number(20)      not null,
  message_template_id   number(20),
  message_key           varchar2(50)   not null,
  message_template_name varchar2(255),
  message_type          varchar2(20)   not null,
  template_mode         varchar2(20),
  account               varchar2(255)  not null,
  title                 varchar2(255),
  template_id           varchar2(100),
  content               nclob,
  message_config_title  varchar2(255),
  supplier_type         varchar2(20),
  is_success            number(4),
  error_code            varchar2(255),
  error_message         varchar2(500),
  biz_id                varchar2(255),
  message               varchar2(255),
  log_time              date            not null
);

alter table sys_message_log add constraint pk_sys_message_log primary key (message_log_id);
create index idx_sys_message_template_id on sys_message_log (message_template_id);
comment on table sys_message_log                        is '消息发送记录表';
comment on column sys_message_log.message_log_id        is '消息发送记录id';
comment on column sys_message_log.message_template_id   is '消息模板id';
comment on column sys_message_log.message_key           is '消息key';
comment on column sys_message_log.message_template_name is '模板名称';
comment on column sys_message_log.message_type          is '消息类型 SMS、MAIL';
comment on column sys_message_log.template_mode         is '模板类型 模板id模式、内容模式';
comment on column sys_message_log.account               is '发送账号';
comment on column sys_message_log.title                 is '标题';
comment on column sys_message_log.template_id           is '模板ID';
comment on column sys_message_log.content               is '发送内容';
comment on column sys_message_log.message_config_title  is '消息配置标题';
comment on column sys_message_log.supplier_type         is '平台标识';
comment on column sys_message_log.is_success            is '是否成功';
comment on column sys_message_log.error_code            is '错误码';
comment on column sys_message_log.error_message         is '错误消息';
comment on column sys_message_log.biz_id                is '回执消息id';
comment on column sys_message_log.message               is '返回消息';
comment on column sys_message_log.log_time              is '记录时间';

-- ----------------------------
-- 消息模板表
-- ----------------------------
create table sys_message_template (
  message_template_id   number(20)      not null,
  template_name         varchar2(255)  not null,
  message_config_id     number(20)      not null,
  message_key_id        number(20)      not null,
  message_key           varchar2(50)   not null,
  message_type          varchar2(20)   not null,
  template_mode         varchar2(20)   not null,
  title                 varchar2(255),
  signature             varchar2(100),
  template_id           varchar2(100),
  content               nclob,
  vars_json             nclob,
  status                number(4)       not null,
  remark                varchar2(500),
  create_dept           number(20),
  update_by             number(20),
  create_by             number(20),
  update_time           date,
  create_time           date
);

alter table sys_message_template add constraint pk_sys_message_template primary key (message_template_id);
create index idx_sys_message_key_id on sys_message_template (message_key_id);
create index idx_sys_message_key on sys_message_template (message_key, message_type, status);
comment on table sys_message_template                       is '消息模板表';
comment on column sys_message_template.message_template_id  is '消息模板id';
comment on column sys_message_template.template_name        is '模板名称';
comment on column sys_message_template.message_config_id    is '消息配置id';
comment on column sys_message_template.message_key_id       is '消息key主键';
comment on column sys_message_template.message_key          is '消息key';
comment on column sys_message_template.message_type         is '消息类型 SMS、MAIL';
comment on column sys_message_template.template_mode        is '模板类型 模板id模式、内容模式';
comment on column sys_message_template.title                is '标题';
comment on column sys_message_template.signature            is '签名';
comment on column sys_message_template.template_id          is '模板id';
comment on column sys_message_template.content              is '内容';
comment on column sys_message_template.vars_json            is '输入变量';
comment on column sys_message_template.status               is '状态（1正常 0停用）';
comment on column sys_message_template.remark               is '备注';
comment on column sys_message_template.create_dept          is '创建部门';
comment on column sys_message_template.update_by            is '更新者';
comment on column sys_message_template.create_by            is '创建者';
comment on column sys_message_template.update_time          is '更新时间';
comment on column sys_message_template.create_time          is '创建时间';

-- 菜单
-- 消息管理
INSERT INTO sys_menu VALUES ('1801', '消息管理', '1', 11, 'messageManage', NULL, NULL, 0, 1, 'M', '1', '1', NULL, 'chat', 103, 1, sysdate, 1, sysdate, '');
-- 消息配置
INSERT INTO sys_menu VALUES ('1802', '消息配置',    '1801', 1, 'messageConfig', 'system/messageConfig/index', NULL, 0, 1, 'C', '1', '1', 'system:messageConfig:list', 'tools', 103, 1, sysdate, 1, sysdate, '消息配置菜单');
INSERT INTO sys_menu VALUES ('1803', '消息配置查询', '1802', 1, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:messageConfig:query', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu VALUES ('1804', '消息配置新增', '1802', 2, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:messageConfig:add', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu VALUES ('1805', '消息配置修改', '1802', 3, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:messageConfig:edit', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu VALUES ('1806', '消息配置删除', '1802', 4, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:messageConfig:remove', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu VALUES ('1807', '消息配置导出', '1802', 5, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:messageConfig:export', '#', 103, 1, sysdate, NULL, NULL, '');
-- 消息常量
INSERT INTO sys_menu VALUES ('1810', '消息常量',     '1801', 2, 'messageKey', 'system/messageKey/index', NULL, 0, 1, 'C', '1', '1', 'system:messageKey:list', 'root-list', 103, 1, sysdate, 1, sysdate, '消息常量菜单');
INSERT INTO sys_menu VALUES ('1811', '消息常量查询', '1810', 1, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:messageKey:query', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu VALUES ('1812', '消息常量新增', '1810', 2, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:messageKey:add', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu VALUES ('1813', '消息常量修改', '1810', 3, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:messageKey:edit', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu VALUES ('1814', '消息常量删除', '1810', 4, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:messageKey:remove', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu VALUES ('1815', '消息常量导出', '1810', 5, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:messageKey:export', '#', 103, 1, sysdate, NULL, NULL, '');
-- 消息模板
INSERT INTO sys_menu VALUES ('1820', '消息模板',    '1801', 3, 'messageTemplate', 'system/messageTemplate/index', NULL, 0, 1, 'C', '1', '1', 'system:messageTemplate:list', 'relativity', 103, 1, sysdate, 1, sysdate, '消息模板菜单');
INSERT INTO sys_menu VALUES ('1821', '消息模板查询', '1820', 1, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:messageTemplate:query', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu VALUES ('1822', '消息模板新增', '1820', 2, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:messageTemplate:add', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu VALUES ('1823', '消息模板修改', '1820', 3, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:messageTemplate:edit', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu VALUES ('1824', '消息模板删除', '1820', 4, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:messageTemplate:remove', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu VALUES ('1825', '消息模板导出', '1820', 5, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:messageTemplate:export', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu VALUES ('1826', '发送测试消息', '1820', 6, '', NULL, NULL, 0, 1, 'F', '1', '1', 'system:messageTemplate:test', '#', 103, 1, sysdate, 1, sysdate, '');
-- 消息发送记录
INSERT INTO sys_menu VALUES ('1830', '消息发送记录',    '1801', 4, 'messageLog', 'system/messageLog/index', NULL, 0, 1, 'C', '1', '1', 'system:messageLog:list', 'history', 103, 1, sysdate, 1, sysdate, '消息发送记录菜单');
INSERT INTO sys_menu VALUES ('1831', '消息发送记录查询', '1830', 1, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:messageLog:query', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu VALUES ('1832', '消息发送记录删除', '1830', 4, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:messageLog:remove', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu VALUES ('1833', '消息发送记录导出', '1830', 5, '#', '', NULL, 0, 1, 'F', '1', '1', 'system:messageLog:export', '#', 103, 1, sysdate, NULL, NULL, '');

-- 字典类型
insert into sys_dict_type values(12, '000000', '消息类型', 'sys_message_type', '1', 103, 1, sysdate, 1, sysdate, NULL);
insert into sys_dict_type values(13, '000000', '消息支持平台', 'sys_message_supplier_type', '1', 103, 1, sysdate, 1, sysdate, NULL);
insert into sys_dict_type values(14, '000000', '消息模板类型', 'sys_message_template_mode', '1', 103, 1, sysdate, 1, sysdate, NULL);

-- 字典数据
INSERT INTO sys_dict_data VALUES(34, '000000', 0, '短信', 'SMS', 'sys_message_type', NULL, 'primary', 'N', '1', 103, 1, sysdate, 1, sysdate, NULL);
INSERT INTO sys_dict_data VALUES(35, '000000', 1, '邮箱', 'MAIL', 'sys_message_type', NULL, 'primary', 'N', '1', 103, 1, sysdate, 1, sysdate, NULL);
INSERT INTO sys_dict_data VALUES(36, '000000', 1, '阿里云短信', 'ALIBABA', 'sys_message_supplier_type', NULL, 'primary', 'N', '1', 103, 1, sysdate, 1, sysdate, NULL);
INSERT INTO sys_dict_data VALUES(37, '000000', 2, '华为云短信', 'HUAWEI', 'sys_message_supplier_type', NULL, 'primary', 'N', '1', 103, 1, sysdate, 1, sysdate, NULL);
INSERT INTO sys_dict_data VALUES(38, '000000', 3, '腾讯云短信', 'TENCENT', 'sys_message_supplier_type', NULL, 'primary', 'N', '1', 103, 1, sysdate, 1, sysdate, NULL);
INSERT INTO sys_dict_data VALUES(39, '000000', 4, '云片短信', 'YUNPIAN', 'sys_message_supplier_type', NULL, 'primary', 'N', '1', 103, 1, sysdate, 1, sysdate, NULL);
INSERT INTO sys_dict_data VALUES(40, '000000', 5, '合一短信', 'UNI_SMS', 'sys_message_supplier_type', NULL, 'primary', 'N', '1', 103, 1, sysdate, 1, sysdate, NULL);
INSERT INTO sys_dict_data VALUES(41, '000000', 6, '京东云短信', 'JD_CLOUD', 'sys_message_supplier_type', NULL, 'primary', 'N', '1', 103, 1, sysdate, 1, sysdate, NULL);
INSERT INTO sys_dict_data VALUES(42, '000000', 7, '容联云短信', 'CLOOPEN', 'sys_message_supplier_type', NULL, 'primary', 'N', '1', 103, 1, sysdate, 1, sysdate, NULL);
INSERT INTO sys_dict_data VALUES(43, '000000', 8, '亿美软通短信', 'EMAY', 'sys_message_supplier_type', NULL, 'primary', 'N', '1', 103, 1, sysdate, 1, sysdate, NULL);
INSERT INTO sys_dict_data VALUES(44, '000000', 9, '天翼云短信', 'CTYUN', 'sys_message_supplier_type', NULL, 'primary', 'N', '1', 103, 1, sysdate, 1, sysdate, NULL);
INSERT INTO sys_dict_data VALUES(45, '000000', 0, '邮箱', 'MAIL', 'sys_message_supplier_type', NULL, 'primary', 'N', '1', 103, 1, sysdate, 1, sysdate, NULL);
INSERT INTO sys_dict_data VALUES(46, '000000', 10, '网易云短信', 'NETEASE', 'sys_message_supplier_type', NULL, 'primary', 'N', '1', 103, 1, sysdate, 1, sysdate, NULL);
INSERT INTO sys_dict_data VALUES(47, '000000', 0, '模板ID', 'TEMPLATE_ID', 'sys_message_template_mode', NULL, 'primary', 'N', '1', 103, 1, sysdate, 1, sysdate, NULL);
INSERT INTO sys_dict_data VALUES(48, '000000', 1, '模板内容', 'TEMPLATE_CONTENT', 'sys_message_template_mode', NULL, 'primary', 'N', '1', 103, 1, sysdate, 1, sysdate, NULL);
