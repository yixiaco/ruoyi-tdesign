insert into sys_menu values('11616', '工作流'  , '0',    '6', 'workflow',          '',null,                                 '', '0', '1', 'M', '1', '1', '',                       'tree-round-dot-vertical', null, null,103, 1, sysdate(), NULL, NULL, '');
insert into sys_menu values('11617', '模型管理', '11616', '2', 'model',             'workflow/model/index','WorkflowModel',             '', '0', '1', 'C', '1', '1', 'workflow:model:list',    'control-platform', null, null,103, 1, sysdate(), NULL, NULL, '');
insert into sys_menu values('11618', '我的任务', '0', '7', 'task',              '',null,                                 '', '0', '1', 'M', '1', '1', '',                       'task', null, null,103, 1, sysdate(), NULL, NULL, '');
insert into sys_menu values('11619', '我的待办', '11618', '2', 'taskWaiting',       'workflow/task/taskWaiting','WorkflowTaskWaiting',              '', '0', '1', 'C', '1', '1', '',                       'task-visible', null, null,103, 1, sysdate(), NULL, NULL, '');
insert into sys_menu values('11632', '我的已办', '11618', '3', 'taskFinish',       'workflow/task/taskFinish','WorkflowTaskFinish',              '', '0', '1', 'C', '1', '1', '',                       'task-checked', null, null,103, 1, sysdate(), NULL, NULL, '');
insert into sys_menu values('11633', '我的抄送', '11618', '4', 'taskCopyList',       'workflow/task/taskCopyList','WorkflowTaskCopyList',              '', '0', '1', 'C', '1', '1', '',                       'user-arrow-left', null, null,103, 1, sysdate(), NULL, NULL, '');
insert into sys_menu values('11620', '流程定义', '11616', '3', 'processDefinition', 'workflow/processDefinition/index','WorkflowProcessDefinition', '', '0', '1', 'C', '1', '1', '',                       'system-components', null, null,103, 1, sysdate(), NULL, NULL, '');
insert into sys_menu values('11621', '流程实例', '11630', '1', 'processInstance',   'workflow/processInstance/index','WorkflowProcessInstance',   '', '0', '1', 'C', '1', '1', '',                       'tree-round-dot', null, null,103, 1, sysdate(), NULL, NULL, '');
insert into sys_menu values('11622', '流程分类', '11616', '1', 'category',          'workflow/category/index','WorkflowCategory',          '', '0', '1', 'C', '1', '1', 'workflow:category:list', 'app', null, null,103, 1, sysdate(), NULL, NULL, '');
insert into sys_menu values('11629', '我发起的', '11618', '1', 'myDocument',        'workflow/task/myDocument','WorkflowMyDocument',         '', '0', '1', 'C', '1', '1', '',                       'send', null, null,103, 1, sysdate(), NULL, NULL, '');
insert into sys_menu values('11630', '流程监控', '11616', '4', 'monitor',           '',null,                                 '', '0', '1', 'M', '1', '1', '',                       'chart-analytics', null, null,103, 1, sysdate(), NULL, NULL, '');
insert into sys_menu values('11631', '待办任务', '11630', '2', 'allTaskWaiting',    'workflow/task/allTaskWaiting','WorkflowAllTaskWaiting',     '', '0', '1', 'C', '1', '1', '',                       'task-visible', null, null,103, 1, sysdate(), NULL, NULL, '');


-- 流程分类管理相关按钮
insert into sys_menu values ('11623', '流程分类查询', '11622', '1', '#', '', null,'', 0, 1, 'F', '1', '1', 'workflow:category:query', '#', null, null,103, 1, sysdate(), null, null, '');
insert into sys_menu values ('11624', '流程分类新增', '11622', '2', '#', '', null,'', 0, 1, 'F', '1', '1', 'workflow:category:add',   '#', null, null,103, 1, sysdate(), null, null, '');
insert into sys_menu values ('11625', '流程分类修改', '11622', '3', '#', '', null,'', 0, 1, 'F', '1', '1', 'workflow:category:edit',  '#', null, null,103, 1, sysdate(), null, null, '');
insert into sys_menu values ('11626', '流程分类删除', '11622', '4', '#', '', null,'', 0, 1, 'F', '1', '1', 'workflow:category:remove','#', null, null,103, 1, sysdate(), null, null, '');
insert into sys_menu values ('11627', '流程分类导出', '11622', '5', '#', '', null,'', 0, 1, 'F', '1', '1', 'workflow:category:export','#', null, null,103, 1, sysdate(), null, null, '');
-- 请假单信息
DROP TABLE if EXISTS test_leave;
create table test_leave
(
    id          bigint                       not null comment '主键',
    leave_type  varchar(255)                 not null comment '请假类型',
    start_date   datetime                    not null comment '开始时间',
    end_date     datetime                    not null comment '结束时间',
    leave_days  int(10)                      not null comment '请假天数',
    remark      varchar(255)                 null comment '请假原因',
    create_dept bigint                       null comment '创建部门',
    create_by   bigint                       null comment '创建者',
    create_time datetime                     null comment '创建时间',
    update_by   bigint                       null comment '更新者',
    update_time datetime                     null comment '更新时间',
    tenant_id   varchar(20) default '000000' null comment '租户编号',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB COMMENT = '请假申请表';

-- 流程分类信息表
DROP TABLE if EXISTS wf_category;
create table wf_category
(
    id            bigint                       not null comment '主键'
        primary key,
    category_name varchar(255)                 null comment '分类名称',
    category_code varchar(255)                 null comment '分类编码',
    parent_id     bigint                       null comment '父级id',
    sort_num      int(19)                      null comment '排序',
    tenant_id     varchar(20) default '000000' null comment '租户编号',
    create_dept   bigint                       null comment '创建部门',
    create_by     bigint                       null comment '创建者',
    create_time   datetime                     null comment '创建时间',
    update_by     bigint                       null comment '更新者',
    update_time   datetime                     null comment '更新时间',
    constraint uni_category_code
        unique (category_code)
) engine=innodb comment= '流程分类';
INSERT INTO wf_category values (1, 'OA', 'OA', 0, 0, '000000', 103, 1, sysdate(), 1, sysdate());


INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES (11638, '请假申请', 0, 8, 'leave', 'workflow/leave/index', 0, 1, 'C', '1', '1', 'demo:leave:list', 'assignment-user', 103, 1, sysdate(), NULL, NULL, '请假申请菜单');
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES (11639, '请假申请查询', 11638, 1, '#', '', 0, 1, 'F', '1', '1', 'demo:leave:query', '#', 103, 1, sysdate(), NULL, NULL, '');
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES (11640, '请假申请新增', 11638, 2, '#', '', 0, 1, 'F', '1', '1', 'demo:leave:add', '#', 103, 1, sysdate(), NULL, NULL, '');
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES (11641, '请假申请修改', 11638, 3, '#', '', 0, 1, 'F', '1', '1', 'demo:leave:edit', '#', 103, 1, sysdate(), NULL, NULL, '');
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES (11642, '请假申请删除', 11638, 4, '#', '', 0, 1, 'F', '1', '1', 'demo:leave:remove', '#', 103, 1, sysdate(), NULL, NULL, '');
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES (11643, '请假申请导出', 11638, 5, '#', '', 0, 1, 'F', '1', '1', 'demo:leave:export', '#', 103, 1, sysdate(), NULL, NULL, '');
