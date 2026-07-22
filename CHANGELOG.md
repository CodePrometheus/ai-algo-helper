<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# AI Algo Helper Changelog

## [Unreleased]

## [1.0.1] - 2026-07-22

### Changed

- Replaced all IntelliJ Platform internal, override-only, and deprecated API usages with supported public APIs; the Marketplace compatibility verification is now fully clean.
- Determined the plugin version from the packaged descriptor instead of plugin-manager lookups.

## [1.0.0]

### Added

- Added a dedicated Study Lists view with all 12 topic collections from [灵茶山艾府's scientific practice index](https://leetcode.cn/discuss/post/RvFUtj/), organized as collapsible chapter trees with source attribution.
- Added solved and attempted status markers directly to study-list problems.
- Added built-in web sign-in for both leetcode.com and leetcode.cn, with session cookies stored through JetBrains PasswordSafe.

### Changed

- Reintroduced the plugin as AI Algo Helper, an independent fork focused on a modern IntelliJ IDEA algorithm-practice experience.
- Introduced a new `AIgo` visual identity with dedicated Marketplace and tool-window icons.
- Upgraded compatibility to IntelliJ IDEA 2026.2 and platform build 262.
- Made problem content follow the active IntelliJ IDEA light or dark theme.
- Updated the publisher display to Zixin Zhou · GitHub@CodePrometheus.

### Fixed

- Fixed startup and toolbar compatibility with the IntelliJ IDEA 2026.2 platform.
- Made Sign In open the web dialog directly and reliably capture `.com` or `.cn` sessions.
- Removed false page-load warnings caused by cancelled requests and embedded frames.
- Fixed inline mathematical notation in imported study-list content.

### Removed

- Removed inherited Sentry error reporting, Baidu analytics, and persistent device identification.
- Removed the legacy account/password login, upstream-specific updater, donation entry, and personal-site links.
- Removed obsolete dependencies and unsafe legacy networking behavior.

## [8.16.0]

### Fixed

- fix [#760](https://github.com/shuzijun/leetcode-editor/issues/760)

## [8.15.0]

### Fixed

- fix [#756](https://github.com/shuzijun/leetcode-editor/issues/756)

## [8.14.0]

### Fixed

- fix [#726](https://github.com/shuzijun/leetcode-editor/issues/726)
-

## [8.13.0]

### Fixed

- fix [#719](https://github.com/shuzijun/leetcode-editor/issues/719)
-

## [8.12.0]

### Added

- Code Type: Pandas & PostgreSQL enhancement [#709](https://github.com/shuzijun/leetcode-editor/issues/709)
- 希望点击搜索按钮时可以自动聚焦到搜索框 [#713](https://github.com/shuzijun/leetcode-editor/issues/713)

### Fixed

- fix [#708](https://github.com/shuzijun/leetcode-editor/issues/708)
- fix [#712](https://github.com/shuzijun/leetcode-editor/issues/712)
-

## [8.11.0]

### Fixed

- fix [#701](https://github.com/shuzijun/leetcode-editor/issues/701)

## [8.10.0]

### Fixed

- fix [#697](https://github.com/shuzijun/leetcode-editor/issues/697)

## [8.9.0]

### Fixed

- fix [#682](https://github.com/shuzijun/leetcode-editor/issues/682)

## [8.8.0]

### Fixed

- fix [#636](https://github.com/shuzijun/leetcode-editor/issues/636)
- fix [#638](https://github.com/shuzijun/leetcode-editor/issues/638)

## [8.7.0]

### Fixed

- fix [#613](https://github.com/shuzijun/leetcode-editor/issues/613)
- fix [#625](https://github.com/shuzijun/leetcode-editor/issues/625)

## [8.6.0]

### Fixed

- fix [#601](https://github.com/shuzijun/leetcode-editor/issues/601)

## [8.5.0]

### Fixed

- fix [#598](https://github.com/shuzijun/leetcode-editor/issues/598)

## [8.4.0]

### Fixed

- fix [#566](https://github.com/shuzijun/leetcode-editor/issues/566)
- fix [#567](https://github.com/shuzijun/leetcode-editor/issues/567)

## [8.3.0]

### Fixed

- fix [#545](https://github.com/shuzijun/leetcode-editor/issues/545)
- fix [#538](https://github.com/shuzijun/leetcode-editor/issues/538)

## [8.2.0]

### Added

- 增加了不同的窗口,包括*分页窗口*、*全部题目窗口*、*[CodeTop](https://codetop.cc/?utm_source=leetcode_editor)窗口*,可以在导航栏中通过按钮切换.
- Added different windows, including paging window, all problem window, [CodeTop](https://codetop.cc/?utm_source=leetcode_editor) window, which can be switched by buttons in the navigation bar.
- 增加数据统计信息存储,可配合[action](https://github.com/shuzijun/leetcode-editor/blob/master/action/README_ZH.md)生成勋章
- Increase the storage of data statistics, you can use [action](https://github.com/shuzijun/leetcode-editor/tree/master/action) to generate medals

### Changed

- 修改消息通知方式
- Modify the message notification method
- 更改窗口位置
- Change window position

### Fixed

- fix bugs

## [0.0.0]

### Changed

- Snapshot

[Unreleased]: https://github.com/CodePrometheus/ai-algo-helper/compare/v1.0.1...HEAD
[8.16.0]: https://github.com/CodePrometheus/ai-algo-helper/compare/v8.15.0...v8.16.0
[8.15.0]: https://github.com/CodePrometheus/ai-algo-helper/compare/v8.14.0...v8.15.0
[8.14.0]: https://github.com/CodePrometheus/ai-algo-helper/compare/v8.13.0...v8.14.0
[8.13.0]: https://github.com/CodePrometheus/ai-algo-helper/compare/v8.12.0...v8.13.0
[8.12.0]: https://github.com/CodePrometheus/ai-algo-helper/compare/v8.11.0...v8.12.0
[8.11.0]: https://github.com/CodePrometheus/ai-algo-helper/compare/v8.10.0...v8.11.0
[8.10.0]: https://github.com/CodePrometheus/ai-algo-helper/compare/v8.9.0...v8.10.0
[8.9.0]: https://github.com/CodePrometheus/ai-algo-helper/compare/v8.8.0...v8.9.0
[8.8.0]: https://github.com/CodePrometheus/ai-algo-helper/compare/v8.7.0...v8.8.0
[8.7.0]: https://github.com/CodePrometheus/ai-algo-helper/compare/v8.6.0...v8.7.0
[8.6.0]: https://github.com/CodePrometheus/ai-algo-helper/compare/v8.5.0...v8.6.0
[8.5.0]: https://github.com/CodePrometheus/ai-algo-helper/compare/v8.4.0...v8.5.0
[8.4.0]: https://github.com/CodePrometheus/ai-algo-helper/compare/v8.3.0...v8.4.0
[8.3.0]: https://github.com/CodePrometheus/ai-algo-helper/compare/v8.2.0...v8.3.0
[8.2.0]: https://github.com/CodePrometheus/ai-algo-helper/compare/v0.0.0...v8.2.0
[1.0.1]: https://github.com/CodePrometheus/ai-algo-helper/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/CodePrometheus/ai-algo-helper/compare/v8.16.0...v1.0.0
[0.0.0]: https://github.com/CodePrometheus/ai-algo-helper/commits/v0.0.0
[gh:zh-doc]: https://github.com/shuzijun/leetcode-editor/blob/master/README_ZH.md
[gh:en-doc]: https://github.com/shuzijun/leetcode-editor/blob/master/README.md
[badge:zh-doc]: https://img.shields.io/badge/Docs-中文文档-blue?logo=docs&style=flat-square
[badge:en-doc]: https://img.shields.io/badge/Docs-English%20Document-blue?logo=docs&style=flat-square
