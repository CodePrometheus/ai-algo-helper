> [!NOTE]
> **Fork notice:** AI Algo Helper is an independent fork of [shuzijun/leetcode-editor](https://github.com/shuzijun/leetcode-editor), based on upstream version **8.16.0**. We thank the original author and contributors for their work. This fork preserves the upstream attribution and continues to use the [Apache License 2.0](LICENSE).

# AI Algo Helper

[简体中文](README_ZH.md)

AI Algo Helper is an IntelliJ IDEA plugin for algorithm practice.

> AI Algo Helper is an independent open-source project and is not affiliated with or endorsed by LeetCode.

## Highlights

- Complete the problem-solving workflow inside IntelliJ IDEA: browse, search, code, run, submit, and review submissions.
- Learn systematically with 12 curated topic collections organized as collapsible chapter trees, including solved and attempted status markers.
- Read problem descriptions, examples, and available solutions in an IDE-native interface that follows the active UI theme.
- Generate local solution files from configurable templates.
- Sign in through the built-in web dialog without giving the plugin your LeetCode password.
- Practice without analytics, telemetry, or persistent device tracking.

## Current release

The current version is `1.0.2`. See the [changelog](CHANGELOG.md) for the complete release notes.

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
- The plugin does not collect or store a LeetCode username or password.
- Web sign-in session cookies are stored through JetBrains PasswordSafe.
- HTTPS uses normal certificate and hostname verification.

## Attribution and license

The upstream project history, copyright notices, and attribution are retained as stated in the fork notice above.

This project is distributed under the [Apache License 2.0](LICENSE). See [NOTICE](NOTICE) for attribution and modification notices.
