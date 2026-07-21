<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# AI Algo Helper Changelog

## 1.0.0

### Added

- Added a dedicated Study Lists view with all 12 collapsible topic collections from [灵茶山艾府's scientific practice index](https://leetcode.cn/discuss/post/RvFUtj/), preserving author and source attribution.
- Added solved and attempted status markers to study-list problems by reusing the existing cached question status.
- Added PasswordSafe-backed storage for web sign-in session cookies.
- Added regression tests for login actions, study-list catalogs and parsing, GET requests, redirects with cookie/CSRF propagation, and POST bodies.

### Changed

- Rebranded the plugin, artifact, actions, settings, tool windows, and Java packages as AI Algo Helper and `com.codeprometheus.aialgohelper`.
- Upgraded the development stack to IntelliJ IDEA 2026.2, IntelliJ Platform Gradle Plugin 2.18.1, Gradle 9.5.0, Kotlin 2.4.10, and JBR/JDK 25.
- Added the required JCEF module dependency for modern IntelliJ IDEA versions.
- Replaced legacy account/password login with the built-in web sign-in dialog and PasswordSafe-backed session storage.
- Rebuilt HTTP access on JetBrains platform APIs with standard TLS verification, IDE proxy/PAC support, cookies, and bounded redirects.
- Made the JCEF problem preview follow the active IntelliJ UI theme and refresh when the theme changes.
- Updated CI and release workflows for the `main` branch and current toolchain.

### Fixed

- Removed obsolete Apache Commons Collections usage that caused startup failures.
- Fixed Action updates and background-context access for the IntelliJ IDEA 2026.2 threading model.
- Made Sign In open the web dialog directly instead of opening Settings first.
- Normalized supported inline Markdown math in imported study-list content.
- Fixed the packaged Marketplace description.

### Removed

- Removed inherited Sentry error reporting, Baidu analytics requests, and the generated device identifier.
- Removed the custom updater tied to the upstream Marketplace listing.
- Removed the inherited donation entry and personal-site links.
- Removed account/password fields and legacy direct-login code.
- Removed the GPL-licensed `lc-sdk` dependency and its unsafe TLS implementation.
- Removed the unused GPL-licensed JLaTeXMath dependency.

## 8.16.0

### Added

### Changed

### Deprecated

### Fixed
- fix [#760](https://github.com/shuzijun/leetcode-editor/issues/760)
### Removed


## 8.15.0

### Added

### Changed

### Deprecated

### Fixed
- fix [#756](https://github.com/shuzijun/leetcode-editor/issues/756)
### Removed



## 8.14.0

### Added

### Changed

### Deprecated

### Fixed
- fix [#726](https://github.com/shuzijun/leetcode-editor/issues/726)
-
### Removed


## 8.13.0

### Added

### Changed

### Deprecated

### Fixed
- fix [#719](https://github.com/shuzijun/leetcode-editor/issues/719)
- 
### Removed


## 8.12.0

### Added
- Code Type: Pandas & PostgreSQL enhancement [#709](https://github.com/shuzijun/leetcode-editor/issues/709)
- 希望点击搜索按钮时可以自动聚焦到搜索框 [#713](https://github.com/shuzijun/leetcode-editor/issues/713)
### Changed

### Deprecated

### Fixed
- fix [#708](https://github.com/shuzijun/leetcode-editor/issues/708)
- fix [#712](https://github.com/shuzijun/leetcode-editor/issues/712)
- 
### Removed


## 8.11.0

### Added

### Changed

### Deprecated

### Fixed
- fix [#701](https://github.com/shuzijun/leetcode-editor/issues/701)

### Removed



## 8.10.0

### Added

### Changed

### Deprecated

### Fixed
- fix [#697](https://github.com/shuzijun/leetcode-editor/issues/697)

### Removed


## 8.9.0

### Added

### Changed

### Deprecated

### Fixed
- fix [#682](https://github.com/shuzijun/leetcode-editor/issues/682)

### Removed


## 8.8.0

### Added

### Changed

### Deprecated

### Fixed
- fix [#636](https://github.com/shuzijun/leetcode-editor/issues/636)
- fix [#638](https://github.com/shuzijun/leetcode-editor/issues/638)

### Removed


## 8.7.0

### Added

### Changed

### Deprecated

### Fixed
- fix [#613](https://github.com/shuzijun/leetcode-editor/issues/613)
- fix [#625](https://github.com/shuzijun/leetcode-editor/issues/625)

### Removed

## 8.6.0

### Added

### Changed

### Deprecated

### Fixed
- fix [#601](https://github.com/shuzijun/leetcode-editor/issues/601)

### Removed

## 8.5.0

### Added

### Changed

### Deprecated

### Fixed
- fix [#598](https://github.com/shuzijun/leetcode-editor/issues/598)

### Removed

## 8.4.0

### Added

### Changed

### Deprecated

### Fixed
- fix [#566](https://github.com/shuzijun/leetcode-editor/issues/566)
- fix [#567](https://github.com/shuzijun/leetcode-editor/issues/567)

### Removed


## 8.3.0

### Added

### Changed

### Deprecated

### Fixed
- fix [#545](https://github.com/shuzijun/leetcode-editor/issues/545)
- fix [#538](https://github.com/shuzijun/leetcode-editor/issues/538)

### Removed


## 8.2.0

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
### Deprecated

### Fixed
- fix bugs

### Removed

## 0.0.0  

### Added   
    
### Changed    
- Snapshot

### Deprecated

### Removed

[badge:en-doc]: https://img.shields.io/badge/Docs-English%20Document-blue?logo=docs&style=flat-square
[badge:zh-doc]: https://img.shields.io/badge/Docs-中文文档-blue?logo=docs&style=flat-square
[gh:en-doc]: https://github.com/shuzijun/leetcode-editor/blob/master/README.md
[gh:zh-doc]: https://github.com/shuzijun/leetcode-editor/blob/master/README_ZH.md
