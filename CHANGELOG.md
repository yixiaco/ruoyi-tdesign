## 🌈 1.0.9 `2023-10-07`
> 本次更新需要执行`update_1.0.8-1.0.9.sql`脚本<br>
> 包含可能有破坏性的脚本且更新时需要删除所有缓存
### 🚀 New Features
- 新增角色、菜单、部门、岗位、字典类型、字典数据、通知公告、登录日志、OSS配置管理 详情
- 富文本组件新增setup参数
- 增加基础分页查询对象

### 🔥 Performance
- 文件上传直接上传不需要中转
- dict-tag组件默认转为string进行匹配
- 优化弹窗表单验证效果
- 编辑弹窗关闭时销毁，防止校验规则复用
- 表单不再提供触发类型
- 优化页面显示效果；代码生成导入时防止重复点击
- 代码生成到地址时，不过滤前端文件
- 升级tinymce版本
- 优化代码生成编辑页效果
- 动态租户注解增加读取租户是否是必须的
- 优化类型提示

### 🐞 Bug Fixes
- 查询语法应该兼容通用sql
- 修复类型错误提示
- 面包屑组件缓存问题
- 修复oss分类查询报错问题
- 修复路由重定向超过3层时地址错误
- 解决eslint解析tsx错误问题
- 获取用户对象返回null而不是直接抛出异常
- 修复面包屑点击无跳转、动态路由显示路径问题
- 修复部门树过滤失效问题
- 修复消息模板的标题字段没有变量响应提示
- 修复模板问题
- 修复个人中心打开报错问题
- 发送MQ消息时,TenantMQMessage对象未序列化报错
- 修复区域选择滚动时选中项被取消的问题
- 代码生成模板格式错乱问题

### 🏡 Chore
- 调整代码生成模板
- 隐藏多余的系统参数配置
- 删除多余的缓存查询
- 同步ruoyi-vue-plus代码（包含可能有破坏性的脚本且更新时需要删除所有缓存）
- 同步tdesign starter
- 缓存前缀默认使用应用名称
- 个人中心图片上传更换编辑icon、旋转更改为向左

### 🔨 Dependency Upgrades
#### 🔨 java pom
- Upgrade to spring-boot-dependencies-3.1.3
- Upgrade to spring-boot-configuration-processor-3.1.3
- Upgrade to spring-boot-admin-starter-server-3.1.5
- Upgrade to spring-boot-admin-starter-client-3.1.5
- Upgrade to redisson-spring-boot-starter-3.23.4
- Upgrade to aws-java-sdk-s3-1.12.540
#### 🔨 node package
- Upgrade to @tinymce/tinymce-vue@^5.1.1,
- Upgrade to axios@^1.5.0,
- Upgrade to tdesign-vue-next@1.5.6,
- Upgrade to tinymce@^6.7.0,
- Upgrade to vue-cropper@^1.1.1,
- Upgrade to @types/crypto-js@^4.1.2,
- Upgrade to @types/js-cookie@^3.0.4,
- Upgrade to @types/lodash@^4.14.198,
- Upgrade to @types/qs@^6.9.8,
- Upgrade to @typescript-eslint/eslint-plugin@^6.7.0,
- Upgrade to @typescript-eslint/parser@^6.7.0,
- Upgrade to @vitejs/plugin-vue@^4.3.4,
- Upgrade to @vue/eslint-config-typescript@^12.0.0,
- Upgrade to eslint@^8.49.0,
- Upgrade to lint-staged@^14.0.1,
- Upgrade to typescript@~5.2.2,
- Upgrade to unplugin-vue-components@^0.25.2,
- Upgrade to vue-tsc@^1.8.11 


## 🌈 1.0.8 `2023-08-28`
> 本次更新需要执行`update_1.0.7-1.0.8.sql`脚本<br>
> sys_oss如果存在数据，则应该设置user_type为对应的用户类型
### 🚀 New Features
- 新增文件分类功能
- 新增我的文件管理功能
- 新增鼠标选择区域组件
- 新增图片、文件上传组件适配我的文件选择功能
- 富文本组件上传支持我的文件选择功能
- 文件选择器支持accpet属性
- 使用import type导入类型，而不是直接引用
- 使用vue3.3新特性defineOptions定义组件名称
- 代码生成支持实时预览;新增编辑页同步表结构;新增生成方法（新增、编辑、修改、删除、导出、详情、查询）;新增生成对象（controller、Vue、Sql）；新增菜单icon选择
- 菜单列表新增显示状态
- RedisLockUtil新增加锁执行方法

### 🔥 Performance
- 升级适配tdesign-vue-next 1.5.2版本
- 优化用户管理部门树显示效果
- tdesign-vue-next恢复按需引入
- 代码生成查询对象优先使用bo而不是domain
- 代码生成优化、树结构类型补全
- 优化MapstructUtils类，使IDEA能够正常提示空指针异常
- 代码生成编辑默认上级菜单为根目录

### 🐞 Bug Fixes
- 修复测试菜单id重复问题及状态值问题
- 修复弹出层遮盖问题
- 修复文件大小为0不显示文件大小的问题
- 修复隐藏菜单没有效果
- tree结构导入文件修复
- 修复代码生成列表翻页未生效问题
- 修复同名组件不同路径的标签缓存问题
- 修复系统配置中数据库如果不存在数据时的报错问题
- 修复字典数据编辑按钮打开报错
- 修复了表格空数据字样不居中问题
- 修复vue文件生成多余的对象属性
- 修复ruoyi-ui全局函数未提示问题
- 用户管理过滤部门数据时，允许节点折叠

### 🏡 Chore
<details><summary>同步RuoYi-Vue-Plus项目</summary>

* fix:角色权限支持仅本人权限查看，解决无法查看自己创建的角色问题
* fix: 修复可能导致异常类无法反序列化问题
* docs: 错误的方法命名
* perf: 优化 CryptoFilter null判断工具
* update 优化 websocket 路径与 cloud 版本保持一致
* update 更新登录策略返回值
* update 修改验证码路径
* fix：修复客户端编辑时授权类型变更未保存的问题
* update 删除一些跟swagger有关的字眼 避免误解
* perf: 更新依赖
* style: 样式优化
* update 优化 过期的 Security 方法
* perf: 优化 excel 导出字典默认转为下拉框
* perf: 优化 excel 导出字典下拉选项下标错误问题
* perf: 优化 excel 导出问题
* fix 修复 三方绑定 实体类copy覆盖问题
</details> 

### 🔨 Dependency Upgrades
#### 🔨 java pom
- Upgrade to spring-boot-dependencies-3.1.2
- Upgrade to mybatis-spring-boot-starter 3.0.2
- Upgrade to springdoc-openapi 2.2.0
- Upgrade to easyexcel 3.3.2
- Upgrade to mybatis-plus 3.5.3.2
- Upgrade to pagehelper 1.4.7
- Upgrade to hutool 5.8.21
- Upgrade to okhttp 4.11.0
- Upgrade to spring-boot-admin 3.1.4
- Upgrade to redisson 3.23.2
- Upgrade to lock4j 2.2.5
- Upgrade to dynamic-datasource 4.1.3
- Upgrade to transmittable-thread-local 2.14.3
- Upgrade to lombok 1.18.28
- Upgrade to bcprov-jdk15to18 1.76
- Upgrade to aws-java-sdk-s3 1.12.528
- Upgrade to maven-surefire-plugin 3.1.2
#### 🔨 node package
- Upgrade to pinia@^2.1.6
- Upgrade to qrcode.vue@^3.4.1
- Upgrade to tdesign-icons-vue-next@0.2.2
- Upgrade to tdesign-vue-next@1.5.2
- Upgrade to tinymce@^6.6.1
- Upgrade to @commitlint/cli@^17.7.1
- Upgrade to @commitlint/config-conventional@^17.7.0
- Upgrade to @types/lodash@^4.14.197
- Upgrade to @typescript-eslint/eslint-plugin@^6.4.1
- Upgrade to @typescript-eslint/parser@^6.4.1
- Upgrade to @vitejs/plugin-vue@^4.3.3
- Upgrade to @vitejs/plugin-vue-jsx@^3.0.2
- Upgrade to eslint@^8.47.0
- Upgrade to eslint-config-prettier@^9.0.0
- Upgrade to eslint-plugin-import@^2.28.1
- Upgrade to eslint-plugin-vue@^9.17.0
- Upgrade to less@^4.2.0
- Upgrade to vite@^4.4.9
- Upgrade to vue-tsc@^1.8.8

## 🌈 1.0.7 `2023-07-30`
> 本次更新需要执行`update_1.0.6-1.0.7.sql`脚本
### 🚀 New Features
- 系统设置新增使用map直接修改参数；使用便捷方式修改系统参数，拆分租户参数与全局参数设置
- 优化消息模块抽取为新模块，新增耗时字段；从缓存中读取消息模板、消息配置
- 新增动态租户注解，与原有动态租户分开存储，优先级更高，不支持嵌套使用
- 提供租户消息的MQ基础消息对象，方便在使用消息队列时与动态租户注解组合
### 🔥 Performance
- 设置滚动条最小高度；富文本预览新增代码块最大高度

### 🐞 Bug Fixes
- 修复根路由为菜单类型时，不显示或显示错误问题
- 修复在不启用忽略租户方法中，不执行回调
- 修复后台权限匹配路径缺少`gen/`问题
- 修复依赖`ruoyi-common-amqp`报错问题
- 修复判断消息内容是否是HTML的正则不够准确
- 修复权限未匹配命中url时，请求资源被放行
- 修复消息发送时，值对象不是String类型时，不会替换变量引起的报错问题
- 修复在非web环境下构建PageQuery请求对象时报错
- 修复富文本弹出层z-index小于弹窗的问题
- 修复菜单搜索时，偶发性无法跳转问题
- 修复下载文件时，加载插件不关闭问题

### 🏡 Chore
- 更新gitee客户端id
- 优化字典类型链接显示效果
- 修改config遗留注释

### 🔨 Dependency Upgrades
- Upgrade to pinia-plugin-persistedstate@^3.2.0
- Upgrade to tdesign-vue-next@^1.4.1
- Upgrade to @commitlint/cli@^17.6.7
- Upgrade to @commitlint/config-conventional@^17.6.7
- Upgrade to @types/lodash@^4.14.196
- Upgrade to @typescript-eslint/eslint-plugin@^6.2.0
- Upgrade to @typescript-eslint/parser@^6.2.0
- Upgrade to eslint@^8.45.0
- Upgrade to stylelint@~15.10.2
- Upgrade to vite@^4.4.7
- Upgrade to vue-tsc@^1.8.6

## 🌈 1.0.6 `2023-07-20`
### 🚀 New Features
- 富文本编辑器使用TinyMCE替换CKEditor
- 升级tdesign-vue-next-starter版本为0.8.0
- 代码高亮使用Prismjs替代highlightjs，并支持显示行号
- 新增代码预览组件
- 新增富文本预览组件
- number-leading-zero deprecated
- 移除Stylelint-less依赖
- 加载中提示默认修改为30秒
- 绑定平台账号时检验是否已绑定其他用户
- 隐藏使用的clientId被误删、停用

### 🐞 Bug Fixes
- 修复根据用户查询菜单时的问题
- 校验客户端Key防止重复导致查询时因ClientId重复报错
- 修复prettier与eslint-plugin-prettier插件版本不兼容问题
- 登录超时时间设置问题
- 修复在多租户与不启用租户下，token到期时无法清理在线用户状态，设置动态租户在web环境未登录时不生效问题

### 🔨 Dependency Upgrades
- Upgrade to tdesign-icons-vue-next ^0.1.12
- Upgrade to tdesign-vue-next ^1.3.12
- Upgrade to @tinymce/tinymce-vue ^5.1.0
- Upgrade to prismjs ^1.29.0
- Upgrade to tinymce ^6.6.0
- Upgrade to @types/prismjs ^1.26.0
- Upgrade to eslint-plugin-prettier ^5.0.0
- Upgrade to vite-plugin-prismjs ^0.0.8
- Upgrade to vue-tsc ^1.8.5
- Upgrade to stylelint-config-standard ^34.0.0

## 🌈 1.0.5 `2023-07-14`
### 🚀 Features
- 使用PowerJob替换XXLJob
- 新增客户端管理
- 新增第三方登录
- 新增请求加密传输

### 🐞 Bug Fixes
- 修复代码生成中的问题
- 修复表格ui宽度不够时不能滚动问题
- 修复在多租户下，token到期时无法清理在线用户状态

### 🔨 Dependency Upgrades
- Upgrade to tdesign-vue-next ^1.3.11
- Upgrade to vue-router ~4.2.4
- Upgrade to @typescript-eslint/eslint-plugin ^6.0.0
- Upgrade to @typescript-eslint/parser ^6.0.0
- Upgrade to prettier ^3.0.0
- Upgrade to stylelint-less 1.0.8
- Upgrade to vite ^4.4.4
- Upgrade to vite-plugin-vue-devtools ^0.5.1
- Upgrade to Upgrade to easyexcel 3.3.1
- Upgrade to satoken 1.35.0.RC
- Upgrade to dynamic-ds 4.1.1

## 🌈 1.0.4 `2023-07-11`
### 🚀 Features
- fix: 修复菜单搜索浮层样式问题
- chore: release 0.7.7
- build(deps-dev): bump typescript from 4.9.5 to 5.1.6
- feat: 更新ruoyi-ui所有依赖
- refactor: 重构Axios工具，新增接口级防抖节流
- feat: 增加菜单搜索功能 (#10) 一夏* 2023/7/9 0009 22:24
- feat: 代码生成新增排序字段生成 (#8) 一夏* 2023/7/8 0008 15:29
- feat：更新依赖、highlightjs修改为本地组件 yixiaco 2023/7/6 0006 17:17
- feat：新增消息模板标题属性变量渲染 yixiaco 2023/7/3 0003 0:53
- feat 新增OSS配置create_bucket字段，在使用配置时使用创建桶;fix:修复新建配置时报错 yixiaco 2023/7/5 0005 0:43
- update 提供存在数据值时的sql更新脚本 yixiaco 2023/7/1 0001 9:28
- style: 修改面包屑分隔符 yixiaco 2023/7/8 0008 22:59
- perf: 优化代码生成编辑生成配置效果 yixiaco 2023/7/7 0007 11:03

### 🐞 Bug Fixes

- fix: 修复导入数据表列表时的偶发性错误 yixiaco 2023/7/7 0007 10:24
- fix: 修复字段排序不生效问题 yixiaco 2023/7/7 0007 10:05
- fix: 修复highlightjs打包后无高亮效果； yixiaco 2023/7/6 0006 17:17
- fix: redis读写锁释放读锁时，判断是否已经释放，避免锁重入时被提前释放 yixiaco 2023/7/6 0006 15:13
- fix: OSS文件管理列表分页问题修复 yixiaco 2023/7/5 0005 0:55
- fix: 刷新配置缓存时，未清理全局配置；变更OSS预览时失败修复； yixiaco 2023/7/4 0004 23:38
- fix: ckeditor列表样式问题 yixiaco 2023/7/3 0003 23:37
- fix: 修改消息模板内容太长时报错；修改提取变量正则不确认问题；修复变量属性无序问题，改为使用数组方式； yixiaco 2023/7/3 0003 0:53