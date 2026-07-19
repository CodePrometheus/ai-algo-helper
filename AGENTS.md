# Repository Guidelines

## Project identity

- Product: **AI Algo Helper**
- Artifact: `ai-algo-helper`
- Java package: `com.codeprometheus.aialgohelper`
- Action prefix: `aiAlgoHelper`
- Current baseline: IntelliJ IDEA 2026.2 / build 262 / JBR 25

## Development rules

- Prefer small, reviewable changes that follow the existing architecture.
- Use JetBrains platform APIs where available.
- Do not add analytics, telemetry, device identifiers, insecure TLS, or unnecessary network calls.
- Store credentials with JetBrains PasswordSafe.
- Check dependency licenses before adding or upgrading libraries.
- Do not describe planned AI capabilities as implemented features.
- Keep upstream attribution, Git history, and Apache-2.0 licensing intact.
- Keep `README.md`, `README_ZH.md`, and `CHANGELOG.md` consistent with released behavior.
- Do not commit, push, publish, or create releases without explicit approval.

## Verification

Run the narrowest relevant tests first, then:

```shell
./gradlew test
./gradlew buildPlugin
```

Report what was verified and what was not.
