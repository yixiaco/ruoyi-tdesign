insert into sys_menu values('11616', '工作流'  , '0',    '6', 'workflow',          '',                                 '', '0', '1', 'M', '1', '1', '',                       'tree-table', 103, 1, sysdate, NULL, NULL, '');
insert into sys_menu values('11617', '模型管理', '11616', '2', 'model',             'workflow/model/index',             '', '0', '1', 'C', '1', '1', 'workflow:model:list',    'tree-table', 103, 1, sysdate, NULL, NULL, '');
insert into sys_menu values('11618', '我的任务', '0', '7', 'task',              '',                                     '', '0', '1', 'M', '1', '1', '',                       'tree-table', 103, 1, sysdate, NULL, NULL, '');
insert into sys_menu values('11619', '我的待办', '11618', '2', 'taskWaiting',       'workflow/task/taskWaiting',        '', '0', '1', 'C', '1', '1', '',                       'tree-table', 103, 1, sysdate, NULL, NULL, '');
insert into sys_menu values('11632', '我的已办', '11618', '3', 'taskFinish',       'workflow/task/taskFinish',          '', '0', '1', 'C', '1', '1', '',                       'tree-table', 103, 1, sysdate, NULL, NULL, '');
insert into sys_menu values('11633', '我的抄送', '11618', '4', 'taskCopyList',       'workflow/task/taskCopyList',      '', '0', '1', 'C', '1', '1', '',                       'tree-table', 103, 1, sysdate, NULL, NULL, '');
insert into sys_menu values('11620', '流程定义', '11616', '3', 'processDefinition', 'workflow/processDefinition/index', '', '0', '1', 'C', '1', '1', '',                       'tree-table', 103, 1, sysdate, NULL, NULL, '');
insert into sys_menu values('11621', '流程实例', '11630', '1', 'processInstance',   'workflow/processInstance/index',   '', '0', '1', 'C', '1', '1', '',                       'tree-table', 103, 1, sysdate, NULL, NULL, '');
insert into sys_menu values('11622', '流程分类', '11616', '1', 'category',          'workflow/category/index',          '', '0', '1', 'C', '1', '1', 'workflow:category:list', 'tree-table', 103, 1, sysdate, NULL, NULL, '');
insert into sys_menu values('11629', '我发起的', '11618', '1', 'myDocument',        'workflow/task/myDocument',         '', '0', '1', 'C', '1', '1', '',                       'tree-table', 103, 1, sysdate, NULL, NULL, '');
insert into sys_menu values('11630', '流程监控', '11616', '4', 'monitor',           '',                                 '', '0', '1', 'M', '1', '1', '',                       'tree-table', 103, 1, sysdate, NULL, NULL, '');
insert into sys_menu values('11631', '待办任务', '11630', '2', 'allTaskWaiting',    'workflow/task/allTaskWaiting',     '', '0', '1', 'C', '1', '1', '',                       'tree-table', 103, 1, sysdate, NULL, NULL, '');


-- 流程分类管理相关按钮
insert into sys_menu values ('11623', '流程分类查询', '11622', '1', '#', '', '', 0, 1, 'F', 1, 1, 'workflow:category:query', '#', 103, 1, sysdate, null, null, '');
insert into sys_menu values ('11624', '流程分类新增', '11622', '2', '#', '', '', 0, 1, 'F', 1, 1, 'workflow:category:add',   '#', 103, 1, sysdate, null, null, '');
insert into sys_menu values ('11625', '流程分类修改', '11622', '3', '#', '', '', 0, 1, 'F', 1, 1, 'workflow:category:edit',  '#', 103, 1, sysdate, null, null, '');
insert into sys_menu values ('11626', '流程分类删除', '11622', '4', '#', '', '', 0, 1, 'F', 1, 1, 'workflow:category:remove','#', 103, 1, sysdate, null, null, '');
insert into sys_menu values ('11627', '流程分类导出', '11622', '5', '#', '', '', 0, 1, 'F', 1, 1, 'workflow:category:export','#', 103, 1, sysdate, null, null, '');

-- 请假单信息
create table TEST_LEAVE
(
    ID          NUMBER(20) not null
        constraint PK_TEST_LEAVE
        primary key,
    LEAVE_TYPE  VARCHAR2(255),
    START_DATE  DATE,
    END_DATE    DATE,
    LEAVE_DAYS  NUMBER(10),
    REMARK      VARCHAR2(255),
    CREATE_DEPT NUMBER(20),
    CREATE_BY   NUMBER(20),
    CREATE_TIME DATE,
    UPDATE_BY   NUMBER(20),
    UPDATE_TIME DATE,
    TENANT_ID   VARCHAR2(255) default '000000'
);

comment on table TEST_LEAVE is '请假申请表';
comment on column TEST_LEAVE.ID is '主键';
comment on column TEST_LEAVE.LEAVE_TYPE is '请假类型';
comment on column TEST_LEAVE.START_DATE is '开始时间';
comment on column TEST_LEAVE.END_DATE is '结束时间';
comment on column TEST_LEAVE.LEAVE_DAYS is '请假天数';
comment on column TEST_LEAVE.REMARK is '请假原因';
comment on column TEST_LEAVE.CREATE_DEPT is '创建部门';
comment on column TEST_LEAVE.CREATE_BY is '创建者';
comment on column TEST_LEAVE.CREATE_TIME is '创建时间';
comment on column TEST_LEAVE.UPDATE_BY is '更新者';
comment on column TEST_LEAVE.UPDATE_TIME is '更新时间';
comment on column TEST_LEAVE.TENANT_ID is '租户编号';

-- 流程分类信息表
create table WF_CATEGORY
(
    ID            NUMBER(20) not null
        constraint PK_WF_CATEGORY
        primary key,
    CATEGORY_NAME VARCHAR2(255),
    CATEGORY_CODE VARCHAR2(255)
        constraint UNI_CATEGORY_CODE
        unique,
    PARENT_ID     NUMBER(20),
    SORT_NUM      NUMBER(10),
    TENANT_ID     NUMBER(20),
    CREATE_DEPT   NUMBER(20),
    CREATE_BY     NUMBER(20),
    CREATE_TIME   DATE,
    UPDATE_BY     NUMBER(20),
    UPDATE_TIME   DATE
);

comment on table WF_CATEGORY is '流程分类';
comment on column WF_CATEGORY.ID is '主键';
comment on column WF_CATEGORY.CATEGORY_NAME is '分类名称';
comment on column WF_CATEGORY.CATEGORY_CODE is '分类编码';
comment on column WF_CATEGORY.PARENT_ID is '父级id';
comment on column WF_CATEGORY.SORT_NUM is '排序';
comment on column WF_CATEGORY.TENANT_ID is '租户编号';
comment on column WF_CATEGORY.CREATE_DEPT is '创建部门';
comment on column WF_CATEGORY.CREATE_BY is '创建者';
comment on column WF_CATEGORY.CREATE_TIME is '创建时间';
comment on column WF_CATEGORY.UPDATE_BY is '更新者';
comment on column WF_CATEGORY.UPDATE_TIME is '更新时间';

INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES (11638, '请假申请', 5, 1, 'leave', 'workflow/leave/index', 0, 1, 'C', 1, 1, 'demo:leave:list', '#', 103, 1, sysdate, NULL, NULL, '请假申请菜单');
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES (11639, '请假申请查询', 11638, 1, '#', '', 0, 1, 'F', 1, 1, 'demo:leave:query', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES (11640, '请假申请新增', 11638, 2, '#', '', 0, 1, 'F', 1, 1, 'demo:leave:add', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES (11641, '请假申请修改', 11638, 3, '#', '', 0, 1, 'F', 1, 1, 'demo:leave:edit', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES (11642, '请假申请删除', 11638, 4, '#', '', 0, 1, 'F', 1, 1, 'demo:leave:remove', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES (11643, '请假申请导出', 11638, 5, '#', '', 0, 1, 'F', 1, 1, 'demo:leave:export', '#', 103, 1, sysdate, NULL, NULL, '');
