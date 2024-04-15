-- ----------------------------
-- Table structure for pj_app_info
-- ----------------------------
DROP TABLE "pj_app_info";
CREATE TABLE "pj_app_info" (
  "id" NUMBER(20) NOT NULL,
  "app_name" NVARCHAR2(255),
  "current_server" NVARCHAR2(255),
  "gmt_create" DATE,
  "gmt_modified" DATE,
  "password" NVARCHAR2(255)
);

-- ----------------------------
-- Records of "pj_app_info"
-- ----------------------------
INSERT INTO "pj_app_info" VALUES ('1', 'ruoyi-worker', '127.0.0.1:10010', NULL, NULL, '123456');

-- ----------------------------
-- Table structure for pj_container_info
-- ----------------------------
DROP TABLE "pj_container_info";
CREATE TABLE "pj_container_info" (
  "id" NUMBER(20) NOT NULL,
  "app_id" NUMBER(20),
  "container_name" NVARCHAR2(255),
  "gmt_create" DATE,
  "gmt_modified" DATE,
  "last_deploy_time" DATE,
  "source_info" NVARCHAR2(255),
  "source_type" NUMBER(11),
  "status" NUMBER(11),
  "version" NVARCHAR2(255)
)
;

-- ----------------------------
-- Table structure for pj_instance_info
-- ----------------------------
DROP TABLE "pj_instance_info";
CREATE TABLE "pj_instance_info" (
  "id" NUMBER(20) NOT NULL,
  "actual_trigger_time" NUMBER(20),
  "app_id" NUMBER(20),
  "expected_trigger_time" NUMBER(20),
  "finished_time" NUMBER(20),
  "gmt_create" DATE,
  "gmt_modified" DATE,
  "instance_id" NUMBER(20),
  "instance_params" NCLOB,
  "job_id" NUMBER(20),
  "job_params" NCLOB,
  "last_report_time" NUMBER(20),
  "result" NCLOB,
  "running_times" NUMBER(20),
  "status" NUMBER(11),
  "task_tracker_address" NVARCHAR2(255),
  "type" NUMBER(11),
  "wf_instance_id" NUMBER(20)
)
;

-- ----------------------------
-- Table structure for pj_job_info
-- ----------------------------
DROP TABLE "pj_job_info";
CREATE TABLE "pj_job_info" (
  "id" NUMBER(20) NOT NULL,
  "advanced_runtime_config" NVARCHAR2(255),
  "alarm_config" NVARCHAR2(255),
  "app_id" NUMBER(20),
  "concurrency" NUMBER(11),
  "designated_workers" NVARCHAR2(255),
  "dispatch_strategy" NUMBER(11),
  "dispatch_strategy_config" NVARCHAR2(255),
  "execute_type" NUMBER(11),
  "extra" NVARCHAR2(255),
  "gmt_create" DATE,
  "gmt_modified" DATE,
  "instance_retry_num" NUMBER(11),
  "instance_time_limit" NUMBER(20),
  "job_description" NVARCHAR2(255),
  "job_name" NVARCHAR2(255),
  "job_params" NCLOB,
  "lifecycle" NVARCHAR2(255),
  "log_config" NVARCHAR2(255),
  "max_instance_num" NUMBER(11),
  "max_worker_count" NUMBER(11),
  "min_cpu_cores" NUMBER NOT NULL,
  "min_disk_space" NUMBER NOT NULL,
  "min_memory_space" NUMBER NOT NULL,
  "next_trigger_time" NUMBER(20),
  "notify_user_ids" NVARCHAR2(255),
  "processor_info" NVARCHAR2(255),
  "processor_type" NUMBER(11),
  "status" NUMBER(11),
  "tag" NVARCHAR2(255),
  "task_retry_num" NUMBER(11),
  "time_expression" NVARCHAR2(255),
  "time_expression_type" NUMBER(11)
)
;

-- ----------------------------
-- Records of "PJ_JOB_INFO"
-- ----------------------------
INSERT INTO "pj_job_info" VALUES ('1', NULL,'{"alertThreshold":0,"silenceWindowLen":0,"statisticWindowLen":0}', '1', '5', NULL, '2', NULL, '1', NULL, NULL, NULL, '1', '0', NULL, '单机处理器执行测试', NULL, '{}', '{"type":1}', '0', '0', '0.0000000000000000', '0.0000000000000000', '0.0000000000000000', NULL, NULL, 'org.dromara.job.processors.StandaloneProcessorDemo', '1', '2', NULL, '1', '30000', '3');
INSERT INTO "pj_job_info" VALUES ('2', NULL,'{"alertThreshold":0,"silenceWindowLen":0,"statisticWindowLen":0}', '1', '5', NULL, '1', NULL, '2', NULL, NULL, NULL, '0', '0', NULL, '广播处理器测试', NULL, '{}', '{"type":1}', '0', '0', '0.0000000000000000', '0.0000000000000000', '0.0000000000000000', NULL, NULL, 'org.dromara.job.processors.BroadcastProcessorDemo', '1', '2', NULL, '1', '30000', '3');
INSERT INTO "pj_job_info" VALUES ('3', NULL,'{"alertThreshold":0,"silenceWindowLen":0,"statisticWindowLen":0}', '1', '5', NULL, '1', NULL, '4', NULL, NULL, NULL, '0', '0', NULL, 'Map处理器测试', NULL, '{}', '{"type":1}', '0', '0', '0.0000000000000000', '0.0000000000000000', '0.0000000000000000', NULL, NULL, 'org.dromara.job.processors.MapProcessorDemo', '1', '2', NULL, '1', '1000', '3');
INSERT INTO "pj_job_info" VALUES ('4', NULL,'{"alertThreshold":0,"silenceWindowLen":0,"statisticWindowLen":0}', '1', '5', NULL, '1', NULL, '3', NULL, NULL, NULL, '0', '0', NULL, 'MapReduce处理器测试', NULL, '{}', '{"type":1}', '0', '0', '0.0000000000000000', '0.0000000000000000', '0.0000000000000000', NULL, NULL, 'org.dromara.job.processors.MapReduceProcessorDemo', '1', '2', NULL, '1', '1000', '3');
INSERT INTO "pj_job_info" VALUES ('11', NULL,'{\"alertThreshold\":0,\"silenceWindowLen\":0,\"statisticWindowLen\":0}', '1','5', NULL, '2', NULL, '1', NULL, NULL, NULL, '0', '0', '每小时执行任务', '每小时执行', NULL, '{}', '{\"type\":1}', 1, 0, 0, 0, 0, NULL, NULL, 'org.dromara.job.handle.EveryHourProcessor', 1, 1, NULL, 1, '0 0 * * * ?', 2);
INSERT INTO "pj_job_info" VALUES ('12', NULL,'{\"alertThreshold\":0,\"silenceWindowLen\":0,\"statisticWindowLen\":0}', '1','5', NULL, '2', NULL, '1', NULL, NULL, NULL, '0', '0', '每天2点执行任务', '每天执行', NULL, '{}', '{\"type\":1}', 1, 0, 0, 0, 0, NULL, NULL, 'org.dromara.job.handle.EveryDayProcessor', 1, 1, NULL, 1, '0 0 2 * * ?', 2);
INSERT INTO "pj_job_info" VALUES ('13', NULL,'{\"alertThreshold\":0,\"silenceWindowLen\":0,\"statisticWindowLen\":0}', '1','5', NULL, '2', NULL, '1', NULL, NULL, NULL, '0', '0', '每分钟执行任务', '每分钟执行', NULL, '{}', '{\"type\":1}', 1, 0, 0, 0, 0, NULL, NULL, 'org.dromara.job.handle.EveryMinuteProcessor', 1, 1, NULL, 1, '0 * * * * ?', 2);
INSERT INTO "pj_job_info" VALUES ('14', NULL,'{\"alertThreshold\":0,\"silenceWindowLen\":0,\"statisticWindowLen\":0}', '1','5', NULL, '2', NULL, '1', NULL, NULL, NULL, '0', '0', '每月1号执行任务', '每月执行', NULL, '{}', '{\"type\":1}', 1, 0, 0, 0, 0, NULL, NULL, 'org.dromara.job.handle.EveryMonthProcessor', 1, 1, NULL, 1, '0 0 0 1 * ?', 2);
INSERT INTO "pj_job_info" VALUES ('15', NULL,'{\"alertThreshold\":0,\"silenceWindowLen\":0,\"statisticWindowLen\":0}', '1','5', NULL, '2', NULL, '1', NULL, NULL, NULL, '0', '0', '每年1月1日执行任务', '每年执行', NULL, '{}', '{\"type\":1}', 1, 0, 0, 0, 0, NULL, NULL, 'org.dromara.job.handle.EveryYearProcessor', 1, 1, NULL, 1, '0 0 0 1 1 ? *', 2);

-- ----------------------------
-- Table structure for pj_oms_lock
-- ----------------------------
DROP TABLE "pj_oms_lock";
CREATE TABLE "pj_oms_lock" (
  "id" NUMBER(20) NOT NULL,
  "gmt_create" DATE,
  "gmt_modified" DATE,
  "lock_name" NVARCHAR2(255),
  "max_lock_time" NUMBER(20),
  "ownerip" NVARCHAR2(255)
)
;

-- ----------------------------
-- Table structure for pj_server_info
-- ----------------------------
DROP TABLE "pj_server_info";
CREATE TABLE "pj_server_info" (
  "id" NUMBER(20) NOT NULL,
  "gmt_create" DATE,
  "gmt_modified" DATE,
  "ip" NVARCHAR2(255)
)
;

-- ----------------------------
-- Table structure for pj_user_info
-- ----------------------------
DROP TABLE "pj_user_info";
CREATE TABLE "pj_user_info" (
  "id" NUMBER(20) NOT NULL,
  "email" NVARCHAR2(255),
  "extra" NVARCHAR2(255),
  "gmt_create" DATE,
  "gmt_modified" DATE,
  "password" NVARCHAR2(255),
  "phone" NVARCHAR2(255),
  "username" NVARCHAR2(255),
  "web_hook" NVARCHAR2(255)
)
;

-- ----------------------------
-- Table structure for pj_workflow_info
-- ----------------------------
DROP TABLE "pj_workflow_info";
CREATE TABLE "pj_workflow_info" (
  "id" NUMBER(20) NOT NULL,
  "app_id" NUMBER(20),
  "extra" NVARCHAR2(255),
  "gmt_create" DATE,
  "gmt_modified" DATE,
  "lifecycle" NVARCHAR2(255),
  "max_wf_instance_num" NUMBER(11),
  "next_trigger_time" NUMBER(20),
  "notify_user_ids" NVARCHAR2(255),
  "pedag" NCLOB,
  "status" NUMBER(11),
  "time_expression" NVARCHAR2(255),
  "time_expression_type" NUMBER(11),
  "wf_description" NVARCHAR2(255),
  "wf_name" NVARCHAR2(255)
)
;

-- ----------------------------
-- Table structure for pj_workflow_instance_info
-- ----------------------------
DROP TABLE "pj_workflow_instance_info";
CREATE TABLE "pj_workflow_instance_info" (
  "id" NUMBER(20) NOT NULL,
  "actual_trigger_time" NUMBER(20),
  "app_id" NUMBER(20),
  "dag" NCLOB,
  "expected_trigger_time" NUMBER(20),
  "finished_time" NUMBER(20),
  "gmt_create" DATE,
  "gmt_modified" DATE,
  "parent_wf_instance_id" NUMBER(20),
  "result" NCLOB,
  "status" NUMBER(11),
  "wf_context" NCLOB,
  "wf_init_params" NCLOB,
  "wf_instance_id" NUMBER(20),
  "workflow_id" NUMBER(20)
)
;

-- ----------------------------
-- Table structure for pj_workflow_node_info
-- ----------------------------
DROP TABLE "pj_workflow_node_info";
CREATE TABLE "pj_workflow_node_info" (
  "id" NUMBER(20) NOT NULL,
  "app_id" NUMBER(20) NOT NULL,
  "enable" VARCHAR2(1) NOT NULL,
  "extra" NCLOB,
  "gmt_create" DATE,
  "gmt_modified" DATE,
  "job_id" NUMBER(20),
  "node_name" NVARCHAR2(255),
  "node_params" NCLOB,
  "skip_when_failed" VARCHAR2(1) NOT NULL,
  "type" NUMBER(11),
  "workflow_id" NUMBER(20)
)
;

-- ----------------------------
-- Primary Key structure for table pj_app_info
-- ----------------------------
ALTER TABLE "pj_app_info" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pj_app_info
-- ----------------------------
CREATE UNIQUE INDEX "uidx01_app_info"
  ON "pj_app_info" ("app_name");

-- ----------------------------
-- Primary Key structure for table pj_container_info
-- ----------------------------
ALTER TABLE "pj_container_info" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pj_container_info
-- ----------------------------
CREATE INDEX "idx01_container_info"
  ON "pj_container_info" ("app_id");

-- ----------------------------
-- Primary Key structure for table pj_instance_info
-- ----------------------------
ALTER TABLE "pj_instance_info" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pj_instance_info
-- ----------------------------
CREATE INDEX "idx01_instance_info"
  ON "pj_instance_info" ("job_id", "status");
CREATE INDEX "idx02_instance_info"
  ON "pj_instance_info" ("app_id", "status");
CREATE INDEX "idx03_instance_info"
  ON "pj_instance_info" ("instance_id", "status");

-- ----------------------------
-- Primary Key structure for table pj_job_info
-- ----------------------------
ALTER TABLE "pj_job_info" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pj_job_info
-- ----------------------------
CREATE INDEX "idx01_job_info"
  ON "pj_job_info" ("app_id", "status", "time_expression_type", "next_trigger_time");

-- ----------------------------
-- Primary Key structure for table pj_oms_lock
-- ----------------------------
ALTER TABLE "pj_oms_lock" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pj_oms_lock
-- ----------------------------
CREATE UNIQUE INDEX "uidx01_oms_lock"
  ON "pj_oms_lock" ("lock_name");

-- ----------------------------
-- Primary Key structure for table pj_server_info
-- ----------------------------
ALTER TABLE "pj_server_info" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pj_server_info
-- ----------------------------
CREATE UNIQUE INDEX "uidx01_server_info"
  ON "pj_server_info" ("ip");
CREATE INDEX "idx01_server_info"
  ON "pj_server_info" ("gmt_modified");

-- ----------------------------
-- Primary Key structure for table pj_user_info
-- ----------------------------
ALTER TABLE "pj_user_info" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pj_user_info
-- ----------------------------
CREATE INDEX "uidx01_user_info"
  ON "pj_user_info" ("username");
CREATE INDEX "uidx02_user_info"
  ON "pj_user_info" ("email");

-- ----------------------------
-- Primary Key structure for table pj_workflow_info
-- ----------------------------
ALTER TABLE "pj_workflow_info" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pj_workflow_info
-- ----------------------------
CREATE INDEX "idx01_workflow_info"
  ON "pj_workflow_info" ("app_id", "status", "time_expression_type", "next_trigger_time");

-- ----------------------------
-- Primary Key structure for table pj_workflow_instance_info
-- ----------------------------
ALTER TABLE "pj_workflow_instance_info" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pj_workflow_instance_info
-- ----------------------------
CREATE UNIQUE INDEX "uidx01_wf_instance"
  ON "pj_workflow_instance_info" ("wf_instance_id");
CREATE INDEX "idx01_wf_instance"
  ON "pj_workflow_instance_info" ("workflow_id", "status", "app_id", "expected_trigger_time");

-- ----------------------------
-- Primary Key structure for table pj_workflow_node_info
-- ----------------------------
ALTER TABLE "pj_workflow_node_info" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pj_workflow_node_info
-- ----------------------------
CREATE INDEX "idx01_workflow_node_info"
  ON "pj_workflow_node_info" ("workflow_id", "gmt_create");
