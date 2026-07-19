> [!NOTE]
> **Fork notice:** AI Algo Helper is an independent fork of [shuzijun/leetcode-editor](https://github.com/shuzijun/leetcode-editor), based on upstream version **8.16.0**. We thank the original author and contributors for their work. This fork preserves the upstream attribution and continues to use the [Apache License 2.0](LICENSE).

# AI Algo Helper

[简体中文](README_ZH.md)

AI Algo Helper is an IntelliJ IDEA plugin for algorithm practice.

> AI Algo Helper is an independent open-source project and is not affiliated with or endorsed by LeetCode.

## What it does

- Browse and search LeetCode problems inside IntelliJ IDEA.
- View problem descriptions, examples, and available solutions.
- Generate local solution files from configurable templates.
- Run and submit solutions without leaving the IDE.
- Review submissions and track practice progress.

## 8.17.0 — Fork baseline

Version `8.17.0` is the first AI Algo Helper release line after forking upstream `8.16.0`.

- Renamed the plugin, artifact, actions, settings, tool windows, and Java packages to **AI Algo Helper** and `com.codeprometheus.aialgohelper`.
- Upgraded the development stack to IntelliJ IDEA `2026.2`, IntelliJ Platform Gradle Plugin `2.18.1`, Gradle `9.5.0`, Kotlin `2.4.10`, and JBR/JDK `25`.
- Declared the modular JCEF dependency required by current IntelliJ IDEA versions.
- Removed the obsolete Apache Commons Collections usage that caused startup failures.
- Removed the inherited Sentry integration, Baidu analytics requests, generated device identifier, and custom updater tied to the upstream Marketplace listing.
- Removed the GPL-licensed `lc-sdk` dependency and its unsafe TLS implementation.
- Removed the unused GPL-licensed JLaTeXMath dependency.
- Rebuilt HTTP access on JetBrains platform APIs with standard TLS verification, IDE proxy/PAC support, cookies, and bounded redirects.
- Added local regression tests for GET requests, redirects with cookie/CSRF propagation, and POST bodies.

## Compatibility

The current development baseline targets:

- IntelliJ IDEA `2026.2`
- IntelliJ Platform build `262`
- JBR/JDK `25`

## Build from source

Use JDK 25, then run:

```shell
./gradlew test
./gradlew buildPlugin
```

To build against a locally installed IntelliJ IDEA:

```shell
./gradlew buildPlugin -PlocalIdePath="/path/to/IntelliJ IDEA"
```

The packaged plugin is written to `build/distributions/`.

## Privacy and security

- AI Algo Helper does not include analytics or telemetry.
- It does not generate a persistent device tracking identifier.
- Network requests to LeetCode are made only when the related plugin features are used.
- Account credentials are stored through the JetBrains PasswordSafe integration.
- HTTPS uses normal certificate and hostname verification.

## Attribution and license

The upstream project history, copyright notices, and attribution are retained as stated in the fork notice above.

This project is distributed under the [Apache License 2.0](LICENSE). See [NOTICE](NOTICE) for attribution and modification notices.
