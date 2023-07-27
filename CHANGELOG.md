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