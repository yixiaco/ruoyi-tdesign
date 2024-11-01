ALTER TABLE sys_dept ADD COLUMN dept_category varchar(100) default null::varchar;
COMMENT ON COLUMN sys_dept.dept_category IS '客户端';
ALTER TABLE sys_post ADD COLUMN dept_id int8 NOT NULL;
COMMENT ON COLUMN sys_post.dept_id IS '部门id';
ALTER TABLE sys_post ADD COLUMN post_category varchar(100) default null::varchar;
COMMENT ON COLUMN sys_post.post_category IS '岗位类别编码';
UPDATE sys_post SET dept_id = 100;
UPDATE sys_post SET dept_id = 103 where post_id = 1;
delete from sys_menu where menu_id in (120,114);
insert into sys_menu values('120',  '任务调度中心',  '2',    '6', 'snailjob',           'monitor/snailjob/index', null,        '', 0, 1, 'C', '1', '1', 'monitor:snailjob:list', 'video',           null, '!getProperty(''snail-job.enabled'')', 103, 1, now(), null, null, 'SnailJob控制台菜单');
UPDATE sys_menu SET menu_name = '文件管理' WHERE menu_id = 1510;
