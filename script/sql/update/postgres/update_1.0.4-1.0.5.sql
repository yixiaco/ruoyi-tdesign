-- powerjob server控制台 update by 2023-7-11
delete from sys_menu where menu_id = 120;
insert into sys_menu values('120',  '任务调度中心',  '2',   '5',  'powerjob',           'monitor/powerjob/index',        '', 0, 1, 'C', '1', '1', 'monitor:powerjob:list',         'video',           103, 1, now(), null, null, 'PowerJob控制台菜单');
