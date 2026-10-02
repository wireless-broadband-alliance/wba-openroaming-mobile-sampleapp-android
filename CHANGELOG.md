# Release V1.6.0

### Architectural & SDK Integration
- **SDK Decoupling & Integration**: Re-architected the app to consume the standalone `or-sdk` library instead of containing embedded OpenRoaming logic.
- **Dynamic Environment Configuration**: Integrated CI-level variable injection for sensitive assets (such as `google-services.json`) and API environment parameters.

### User Interface & Experience
- **OpenRoaming Workflow Integration**: Connected UI screens directly to the new `or-sdk` singleton services (`SDK.getUser()`, `SDK.getConfig()`, `SDK.getTurnstile()`).
- **Authentication & Challenge Flow**: Added UI support for 2FA (TOTP) step-up prompts, Cloudflare Turnstile bot verification WebViews, and SAML SSO browser redirection.
- **Passpoint Installation Feedback**: Integrated user feedback indicators and snackbars for Wi-Fi Passpoint profile provisioning and device compatibility warnings.

### Security & CI/CD
- **Automated Security Pipelines**: Configured GitLab CI stages for Software Bill of Materials (SBOM) generation using Syft and cdxgen.
- **Repository Documentation**: Added detailed developer onboarding, setup guides, and architectural overview documentation.

---

# Release V1.5.1

### Security & Dependency Hardening
- **Vulnerability Patching**: Forced explicit resolution strategies and upgraded versions for `netty`, `google-protobuf`, and `bouncycastle` dependencies to mitigate critical CVEs.
- **SAML SSO Authentication**: Added web-based SAML single sign-on authentication flow and resolved connection authorization token bugs.

### Stability & Error Handling
- **Crash Analytics**: Integrated Firebase Crashlytics to enable real-time error tracking and crash reporting in production.
- **WebView & UI Resilience**: Enhanced exception handling for in-app WebViews and extended text limits on user notification components (Snackbars).

---

# Release V1.3.0

### UI/UX & Design Overhaul
- **Screen Architecture Refactoring**: Refactored primary onboarding flows from legacy Fragments to dedicated Activities, improving screen lifecycle stability and navigation reliability.
- **Branding & Visual Assets**: Redesigned main UI theme, updated application icons, header graphics, color palette, and Google Play Store feature graphic assets.
- **Gradle & Permissions**: Configured explicit runtime and system permissions in Gradle build manifests.

---

# Release V1.1.0

### Initial Application Base
- **Core Application Setup**: Initialized baseline Android application structure, project dependencies, and onboarding placeholders.
- **Authentication UI**: Built core authentication interfaces including credential login, user registration, and 2FA onboarding guide screens.
- **API Integration & Persistence**: Integrated baseline API endpoints for profile management, app configuration fetching, and local user session persistence.
- **CI/CD Foundation**: Established initial GitLab CI build script pipeline.
