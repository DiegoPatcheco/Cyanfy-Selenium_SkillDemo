# Cyanfy Selenium BDD Automation

[![CI](https://github.com/DiegoPatcheco/Cyanfy-Selenium_SkillDemo/actions/workflows/main_CI.yml/badge.svg)](https://github.com/DiegoPatcheco/Cyanfy-Selenium_SkillDemo/actions/workflows/main_CI.yml)

Cyanfy is a portfolio project that demonstrates maintainable web BDD automation with Java,
Selenium WebDriver, Cucumber, Gherkin, JUnit Platform, and Maven. The suite exercises the public
[Automation Exercise](https://www.automationexercise.com/) application through independent browser
sessions and publishes diagnostic evidence when a scenario fails.

## Implemented capabilities

| Capability | Current implementation |
| --- | --- |
| BDD coverage | 5 feature files and 14 scenarios/examples for login, sign-up, contact, product review, and shopping |
| Browser automation | Local Chrome, Edge, Firefox, and Safari selection through Selenium WebDriver |
| Remote execution | Selenium Grid-compatible `RemoteWebDriver` mode with configurable URL |
| Synchronization | Explicit waits for page readiness, element presence, visibility, and dynamic cart updates |
| Isolation | A new WebDriver session and cookie cleanup for every scenario |
| Failure evidence | PNG screenshot and HTML page source persisted under `target/evidence` and embedded in Cucumber output |
| Reporting | Cucumber JSON/HTML, Masterthought HTML, and Surefire JUnit XML |
| Build gate | Test failures propagate as a non-zero Maven result; failures are not ignored or parsed from report text |
| CI | Fast test-source compilation followed by a real Chrome headless `@smoke` scenario on pushes and pull requests |

## Technology

- Java 17
- Selenium WebDriver 4.29.0
- Cucumber JVM 7.21.1
- JUnit Jupiter and JUnit Platform 5.11.4 / 1.11.4
- Maven Wrapper 3.9.9
- Amazon Corretto 17 in GitHub Actions

Versions are controlled in [`pom.xml`](pom.xml). Dependency upgrades are intentionally handled as
reviewed changes rather than silently introduced alongside framework work.

## Project structure

```text
src/test/java
|-- data/       Environment-backed test configuration
|-- hooks/      Scenario setup, evidence capture, and teardown
|-- models/     Test-domain value objects and generated account data
|-- pages/      Page Objects and reusable UI components
|-- steps/      Cucumber step definitions
`-- utilities/  Driver lifecycle, waits, common flows, logging, and files

src/test/resources
|-- features/   Gherkin specifications
|-- samples/    Contact-form upload fixture
`-- junit-platform.properties
```

`RunTests` connects the JUnit Platform Suite engine to Cucumber. Page Objects own locators and UI
interactions, step definitions express scenario intent, and hooks manage one driver per scenario.

## Prerequisites

- JDK 17 available on `PATH`
- Git
- Chrome for the default local and CI-equivalent smoke command
- Network access to `https://www.automationexercise.com/`

A separate Maven installation or manually downloaded browser driver is not required. The repository
includes Maven Wrapper, and Selenium Manager resolves supported Chrome, Edge, and Firefox drivers.

## Clone and compile

```sh
git clone https://github.com/DiegoPatcheco/Cyanfy-Selenium_SkillDemo.git
cd Cyanfy-Selenium_SkillDemo
```

Linux/macOS:

```sh
chmod +x mvnw
./mvnw --batch-mode --no-transfer-progress test-compile -DskipTests
```

Windows PowerShell:

```powershell
.\mvnw.cmd --batch-mode --no-transfer-progress test-compile -DskipTests
```

## Run the smoke test

The `@smoke` scenario only verifies the public login screen and does not require credentials.

Linux/macOS:

```sh
./mvnw --batch-mode --no-transfer-progress clean test \
  -Dtest=RunTests \
  "-Dcucumber.filter.tags=@smoke" \
  -Dbrowser=chrome \
  -DexecutionMode=local \
  -Dheadless
```

Windows PowerShell:

```powershell
.\mvnw.cmd --batch-mode --no-transfer-progress clean test `
  -Dtest=RunTests `
  '-Dcucumber.filter.tags=@smoke' `
  -Dbrowser=chrome `
  -DexecutionMode=local `
  -Dheadless
```

## Run the regression suite

Authenticated scenarios read their test account exclusively from environment variables. Use a
dedicated disposable account; never commit real credentials.

Linux/macOS:

```sh
export CYANFY_TEST_EMAIL="your-test-account@example.com"
export CYANFY_TEST_PASSWORD="your-test-password"
sh runSuite.sh
```

Windows PowerShell:

```powershell
$env:CYANFY_TEST_EMAIL = "your-test-account@example.com"
$env:CYANFY_TEST_PASSWORD = "your-test-password"

.\mvnw.cmd --batch-mode --no-transfer-progress clean test `
  -Dtest=RunTests `
  '-Dcucumber.filter.tags=@regression' `
  -Dbrowser=chrome `
  -DexecutionMode=local `
  -Dheadless
```

The regression suite changes data in the external application: it can create accounts, submit
contact messages and reviews, and modify the configured account's cart.

## Browser and execution configuration

| Property | Values | Default |
| --- | --- | --- |
| `browser` | `chrome`, `edge`, `firefox`, `safari` | `chrome` |
| `executionMode` | `local`, `remote` | `local` |
| `remoteUrl` | Selenium Grid endpoint | `http://localhost:4444/wd/hub` |
| `headless` | Enable by including `-Dheadless` | Disabled |

Example remote smoke execution:

```sh
./mvnw clean test -Dtest=RunTests \
  "-Dcucumber.filter.tags=@smoke" \
  -Dbrowser=chrome \
  -DexecutionMode=remote \
  -DremoteUrl=http://localhost:4444/wd/hub \
  -Dheadless
```

Safari execution requires macOS and is intentionally rejected when `-Dheadless` is supplied.

## Reports and failure evidence

| Output | Path |
| --- | --- |
| Cucumber JSON | `target/cucumber/report.json` |
| Cucumber HTML | `target/cucumber-reports/cucumber.html` |
| Masterthought HTML | `target/cucumber-html-reports/overview-features.html` |
| JUnit XML and Surefire output | `target/surefire-reports/` |
| Failure screenshots | `target/evidence/screenshots/` |
| Failure page sources | `target/evidence/page-source/` |

The native Cucumber outputs and attached evidence are written during scenario execution. Maven
still returns a failure when a scenario fails. GitHub Actions uploads available report and evidence
paths with `if: always()`, so diagnostics are retained without converting a failed build into a pass.

## Continuous integration

The [`CI` workflow](.github/workflows/main_CI.yml) runs on pushes and pull requests targeting
`main`, and it can also be started manually.

1. `Compile test automation` provides a quick Java/test-source gate.
2. `Run Chrome headless smoke` opens the real website and executes the `@smoke` scenario.
3. Reports and failure evidence are retained as a GitHub Actions artifact for 14 days.

The authenticated `@regression` suite is not currently executed in GitHub Actions because a safe,
isolated CI account has not been configured.

## Known limitations

- Test availability and results depend on the public Automation Exercise environment and network.
- Authenticated scenarios share the account supplied by the operator and are not designed for
  parallel execution.
- Sign-up scenarios create persistent external data and currently do not delete the account.
- Chrome DevTools Protocol features are not used, but newer local Chrome releases may log a CDP
  compatibility warning until Selenium is upgraded through a reviewed dependency change.
- Cross-browser implementations compile, but the current CI gate validates Chrome only.

## Roadmap

- Add an isolated CI test account and run authenticated regression safely.
- Add a reviewed Chrome/Firefox CI matrix.
- Make generated test data reproducible and clean up external accounts after sign-up scenarios.
- Introduce parallel execution only after removing shared external state.
- Add static analysis and dependency/security scanning as independent CI gates.
- Align Selenium with current browser CDP support through an explicitly approved version update.

## Author

Developed by [Diego Patcheco](https://github.com/DiegoPatcheco).
