> [!NOTE]
> **Fork 声明：** AI Algo Helper 是 [shuzijun/leetcode-editor](https://github.com/shuzijun/leetcode-editor) 的独立 Fork，基于上游 **8.16.0**。感谢原作者及所有贡献者的工作。本项目保留上游署名，并继续遵循 [Apache License 2.0](LICENSE)。

# AI Algo Helper

[English](README.md)

AI Algo Helper 是一款用于算法练习的 IntelliJ IDEA 插件。

> AI Algo Helper 是独立的开源项目，与 LeetCode 无隶属关系，也未获得其官方背书。

## 核心亮点

- 在 IntelliJ IDEA 内完成浏览、搜索、编码、运行、提交和查看记录的完整刷题流程。
- 通过 12 个精选专题系统练习；题单按可折叠章节树组织，并显示已完成和尝试状态。
- 在跟随当前 IDE 主题的原生界面中查看题目描述、示例和可用题解。
- 通过可配置模板生成本地解题文件。
- 使用内置网页登录弹窗，无需向插件提供 LeetCode 密码。
- 不包含分析统计、遥测或持久化设备追踪。

## 当前版本

当前版本为 `1.0.2`，完整版本记录请查看 [CHANGELOG.md](CHANGELOG.md)。

## 兼容性

当前开发基线为：

- IntelliJ IDEA `2026.2`
- IntelliJ Platform build `262`
- JBR/JDK `25`

## 从源码构建

使用 JDK 25，然后运行：

```shell
./gradlew test
./gradlew buildPlugin
```

如需使用本机安装的 IntelliJ IDEA 构建：

```shell
./gradlew buildPlugin -PlocalIdePath="/path/to/IntelliJ IDEA"
```

插件安装包会生成在 `build/distributions/`。

## 隐私与安全

- AI Algo Helper 不包含分析统计或遥测。
- 不生成持久化的设备追踪标识。
- 只有在使用对应插件功能时，才会向 LeetCode 发起网络请求。
- 插件不收集或保存 LeetCode 用户名和密码。
- 网页登录产生的会话 Cookie 通过 JetBrains PasswordSafe 保存。
- HTTPS 使用正常的证书和主机名校验。

## 上游署名与许可证

本项目保留上游项目历史、版权声明和署名，具体来源见文首 Fork 声明。

本项目采用 [Apache License 2.0](LICENSE) 发布，署名与修改声明见 [NOTICE](NOTICE)。
