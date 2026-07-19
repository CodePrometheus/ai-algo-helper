> [!NOTE]
> **Fork 声明：** AI Algo Helper 是 [shuzijun/leetcode-editor](https://github.com/shuzijun/leetcode-editor) 的独立 Fork，基于上游 **8.16.0**。感谢原作者及所有贡献者的工作。本项目保留上游署名，并继续遵循 [Apache License 2.0](LICENSE)。

# AI Algo Helper

[English](README.md)

AI Algo Helper 是一款用于算法练习的 IntelliJ IDEA 插件。

> AI Algo Helper 是独立的开源项目，与 LeetCode 无隶属关系，也未获得其官方背书。

## 现有功能

- 在 IntelliJ IDEA 内浏览和搜索 LeetCode 题目。
- 查看题目描述、示例和可用题解。
- 通过可配置模板生成本地解题文件。
- 不离开 IDE 即可运行和提交代码。
- 查看提交记录并跟踪练习进度。

## 8.17.0 — Fork 起点

`8.17.0` 是从上游 `8.16.0` Fork 后，AI Algo Helper 的首个版本线。

- 将插件、构建产物、Action、设置页、工具窗口和 Java 包统一更名为 **AI Algo Helper** 与 `com.codeprometheus.aialgohelper`。
- 开发栈升级到 IntelliJ IDEA `2026.2`、IntelliJ Platform Gradle Plugin `2.18.1`、Gradle `9.5.0`、Kotlin `2.4.10` 和 JBR/JDK `25`。
- 声明新版 IntelliJ IDEA 所需的模块化 JCEF 依赖。
- 移除会导致启动失败的旧版 Apache Commons Collections 用法。
- 移除继承自上游的 Sentry、百度统计、设备标识符，以及绑定上游 Marketplace 条目的自定义更新逻辑。
- 移除采用 GPL 许可证且包含不安全 TLS 实现的 `lc-sdk`。
- 移除未使用且采用 GPL 许可证的 JLaTeXMath 依赖。
- 基于 JetBrains 平台 API 重建 HTTP 访问，使用标准 TLS 校验，并支持 IDE 代理/PAC、Cookie 和有限次数重定向。
- 增加本地回归测试，覆盖 GET、携带 Cookie/CSRF 的重定向和 POST 请求体。

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
- 账号凭据通过 JetBrains PasswordSafe 保存。
- HTTPS 使用正常的证书和主机名校验。

## 上游署名与许可证

本项目保留上游项目历史、版权声明和署名，具体来源见文首 Fork 声明。

本项目采用 [Apache License 2.0](LICENSE) 发布，署名与修改声明见 [NOTICE](NOTICE)。
