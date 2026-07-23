# CLAUDE.md

Operational guide for working in this repository. Every statement here is derived from
`README.md`, `pom.xml`, `.github/workflows/main_CI.yml`, the test sources under `src/test/java`,
the Gherkin features under `src/test/resources/features`, and `runSuite.sh`.
If any statement below stops matching those files, trust the files and update this document.

## Purpose and architecture

Cyanfy is a portfolio BDD web-automation framework (Java 17, Selenium WebDriver 4.29.0, Cucumber
JVM 7.21.1, JUnit Jupiter/Platform 5.11.4 / 1.11.4, Maven Wrapper 3.9.9) that exercises the public
site `https://www.automationexercise.com/` — a real external application, not a mock.

```text
src/test/java
|-- data/       Environment-backed test configuration (DataGiver reads CYANFY_TEST_* env vars)
|-- hooks/      Hooks: BeforeAll/AfterAll evidence cleanup, Before/After driver lifecycle + evidence
|-- models/     Value objects (Credential, FakeAccount)
|-- pages/      Page Objects extending BasePage; own locators and UI interactions
|-- steps/      Cucumber step definitions; express scenario intent, delegate to pages/CommonFlows
`-- utilities/  DriverManager, WebDriverProvider (ThreadLocal<WebDriver>), FileManager (evidence),
                Logs, BasePage (waits), CommonFlows (shared navigation flows)

src/test/resources
|-- features/   5 feature files: Login, SignUp, Contact, ItemReview, Shopping
|-- samples/    Contact-form upload fixture
`-- junit-platform.properties  Wires cucumber.glue=hooks,steps and report plugins
```

`main.RunTests` is a `@Suite` with `@IncludeEngines("cucumber")` that bridges JUnit Platform to
Cucumber; it carries no logic itself.

## Authoritative Maven commands

Compile test sources only (fast gate, mirrors CI `compile` job):

```powershell
.\mvnw.cmd --batch-mode --no-transfer-progress test-compile -DskipTests
```
```sh
./mvnw --batch-mode --no-transfer-progress test-compile -DskipTests
```

Run the `@smoke` scenario (headless Chrome, no credentials required — mirrors CI `web-smoke` job):

```powershell
.\mvnw.cmd --batch-mode --no-transfer-progress clean test `
  -Dtest=RunTests `
  '-Dcucumber.filter.tags=@smoke' `
  -Dbrowser=chrome `
  -DexecutionMode=local `
  -Dheadless
```
```sh
./mvnw --batch-mode --no-transfer-progress clean test \
  -Dtest=RunTests \
  "-Dcucumber.filter.tags=@smoke" \
  -Dbrowser=chrome \
  -DexecutionMode=local \
  -Dheadless
```

Run the `@regression` suite (requires credentials; not run in CI — see below):

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
```sh
export CYANFY_TEST_EMAIL="your-test-account@example.com"
export CYANFY_TEST_PASSWORD="your-test-password"
sh runSuite.sh
```

`runSuite.sh` (Unix only) hardcodes `@regression`, `browser=chrome`, `executionMode=local`,
`headless`, and `set -eu`; it does not pass `-Dtest=RunTests`. There is no Windows-native
equivalent script — use the PowerShell command above.

## `@smoke` vs `@regression`

- Only `Login.feature`'s first scenario ("verify the login page") is tagged `@smoke` (it also
  carries `@regression`). It requires no credentials and no authenticated state.
- Every other scenario across all 5 feature files is `@regression` only, including the second
  Login scenario (actual sign-in), and all of SignUp, Contact, ItemReview, and Shopping.
- `@regression` scenarios mutate the live external site: they can create accounts, submit contact
  messages and reviews, and modify the configured account's cart.
- CI (`.github/workflows/main_CI.yml`) only ever runs `@smoke`. Do not claim, imply, or configure
  a full `@regression` run inside GitHub Actions — no isolated CI test account exists yet (see
  Roadmap in README).

## `CYANFY_TEST_EMAIL` / `CYANFY_TEST_PASSWORD`

- Read exclusively via `data.DataGiver.getValidCredentials()`, which calls
  `System.getenv(...)` for both variables and throws `IllegalStateException` if either is null or
  blank. There is no fallback, default, or config-file source.
- Required only for `@regression` scenarios that log in (Login's second scenario, Shopping,
  ItemReview, Contact if it needs a session). `@smoke` never touches these variables.
- Never hardcode real credentials in code, feature files, commits, or shell history. Use a
  disposable test account. Never print these values in logs or command output.

## Browser / execution system properties

| Property | Values | Default | Source |
| --- | --- | --- | --- |
| `browser` | `chrome`, `edge`, `firefox`, `safari` | `chrome` | `DriverManager.buildDriver()` |
| `executionMode` | `local`, `remote` | `local` | `DriverManager.buildDriver()` |
| `remoteUrl` | Selenium Grid endpoint | `http://localhost:4444/wd/hub` | `DriverManager.buildRemoteDriver()` |
| `headless` | presence-only flag (`-Dheadless`) | disabled | `DriverManager.buildDriver()` |

- Safari rejects `-Dheadless` with `IllegalArgumentException` (`validateSafariHeadless`) and
  requires macOS.
- Chrome/Edge headless uses `--headless=new --window-size=1920,1080`; Firefox headless uses
  `--headless --width=1920 --height=1080`.
- Selenium Manager resolves Chrome/Edge/Firefox drivers automatically; no manual driver download
  or separate Maven install is required (Maven Wrapper is committed).

## WebDriver lifecycle, waits, evidence, build gate

- One `WebDriver` per scenario via `ThreadLocal` in `WebDriverProvider`, created in
  `Hooks.before` (`DriverManager.buildDriver`) and destroyed in `Hooks.after`
  (`DriverManager.killDriver`, always runs in a `finally` block). Cookies are cleared after driver
  creation; the window is maximized only when not headless.
- `BasePage` is the only place that should perform explicit waits: `getWait()` builds a
  `WebDriverWait` with a 5-second default timeout (`DEFAULT_TIMEOUT`), overridable per Page Object
  via the constructor. Use `find`/`findAll`/`waitPage`; do not add `Thread.sleep` or implicit waits.
- On `FAILED` or `SKIPPED` scenario status, `Hooks.after` calls
  `FileManager.captureFailureEvidence`, which writes a PNG screenshot to
  `target/evidence/screenshots/` and an HTML page source (sanitized through Jsoup) to
  `target/evidence/page-source/`, and also attaches both to the Cucumber scenario. Evidence
  capture failures are caught and logged, never allowed to mask the original scenario failure.
  `Hooks.beforeAll` deletes `target/evidence/` at the start of a run.
- Build gate: test failures must propagate as a non-zero Maven exit code. Never parse report
  files to decide pass/fail, never suppress or swallow a failing scenario, and never mark a build
  green by skipping/ignoring failing tests. `maven-cucumber-reporting` runs with
  `checkBuildResult=true`, reinforcing this gate — do not set it to `false`.

## Report paths

| Output | Path |
| --- | --- |
| Cucumber JSON | `target/cucumber/report.json` |
| Cucumber HTML | `target/cucumber-reports/cucumber.html` |
| Masterthought HTML | `target/cucumber-html-reports/overview-features.html` |
| JUnit XML / Surefire | `target/surefire-reports/` |
| Failure screenshots | `target/evidence/screenshots/` |
| Failure page sources | `target/evidence/page-source/` |
| Log4j2 runtime logs | `target/logs/informative.log`, `target/logs/debug.log`, `target/logs/trace.log` |

## External-site side effects and isolation limits

- All scenarios run against the live `https://www.automationexercise.com/`; there is no local
  stub/mock, so results depend on that site's availability and content.
- `@regression` scenarios are not isolated: SignUp creates a persistent external account that is
  never deleted; Shopping/ItemReview/Contact reuse the single account from
  `CYANFY_TEST_EMAIL`/`CYANFY_TEST_PASSWORD` and mutate shared external state (cart contents,
  reviews, messages).
- Do not assume parallel-safe execution of `@regression` scenarios — they share one external
  account. `@smoke` has no such constraint since it only views the public login page.

## Coding / Page Object / BDD conventions

- Page Objects extend `BasePage`, keep locators as `private final By` fields, and implement
  `waitPageLoad()` and `verifyPage()`. Interactions go through `find()`/`findAll()`, never raw
  `driver.findElement`.
- Step definitions live in `steps/`, stay declarative, and delegate real logic to Page Objects or
  `CommonFlows`; do not put Selenium calls or assertions directly in step definition bodies beyond
  wiring the Gherkin phrase to a Page Object/CommonFlows call.
- Assertions use JUnit Jupiter (`org.junit.jupiter.api.Assertions`), including
  `Assertions.assertAll` for multi-condition checks (see `LoginPage.verifyPage`).
- Logging goes through `utilities.Logs` (`debug`/`info`/`warning`/`error`), not
  `System.out`/`System.err`.
- Feature files: `Given`/`When`/`Then` phrasing as "The user ...", one `Background` per feature for
  shared preconditions, tag every scenario with `@regression` and add `@smoke` only to scenarios
  safe to run without credentials and without external mutation.
- Test data generation uses `com.github.javafaker` (see `models.FakeAccount`) for non-authenticated
  flows like SignUp; authenticated credentials always come from `DataGiver`, never generated.

## Validation requirements before considering work done

- Compile the test sources with the command above and confirm it succeeds.
- Run `git diff --check` to catch whitespace errors before finalizing any change.
- Run `git status --short` and confirm only the files you intentionally changed are listed.
- Prefer running the `@smoke` command locally when touching Page Objects, hooks, driver, or
  step-definition code that the smoke scenario exercises (Login page, driver lifecycle, evidence).

## Hard rules

- Never ignore, suppress, skip, or "fix" a failing test/build by weakening assertions, disabling
  the failing scenario/tag, or parsing report output to fake a pass. Fix the root cause or report
  the failure.
- Never change a dependency or plugin version in `pom.xml` (or `.mvn/wrapper/maven-wrapper.properties`)
  without explicit user authorization — this repo treats dependency upgrades as deliberate,
  reviewed changes, not incidental edits (see README "Technology" section).
- Never `git commit` or `git push` without explicit user approval for that specific action.
- Never claim or configure a full `@regression` run inside GitHub Actions/CI; only `@smoke` runs
  there today, because no isolated CI test account exists.

## Generated / IDE files

- `target/` is a Maven build output directory (compiled classes, reports, evidence) — never hand-edit
  or commit files under it; let `clean`/builds manage it. This includes the Log4j2 runtime logs at
  `target/logs/informative.log`, `target/logs/debug.log`, and `target/logs/trace.log` — they are not
  source files to edit by hand.
- `.gitignore` now ignores the entire `.idea/` directory, so newly generated IntelliJ metadata is
  untracked. Files already committed before this change (e.g. `.idea/vcs.xml`) remain tracked
  regardless — avoid unrelated edits to them, and never introduce editor-specific config changes
  as a side effect of an unrelated task.

## Implemented vs. roadmap — do not conflate

Implemented today (verified in code): local Chrome/Edge/Firefox/Safari execution, `RemoteWebDriver`
against a Grid URL, explicit waits via `BasePage`, per-scenario driver + cookie isolation, PNG +
HTML failure evidence, Cucumber JSON/HTML + Masterthought HTML + Surefire XML reports, a
non-zero-exit build gate, and a CI pipeline that compiles test sources then runs only the Chrome
headless `@smoke` scenario.

Not implemented — treat as future work, never as current behavior: an isolated CI account or CI
`@regression` execution, a Chrome/Firefox CI browser matrix, reproducible/cleaned-up SignUp test
data, parallel execution, static analysis or dependency/security scanning gates, and any Selenium
version bump for newer CDP support. Do not build features on the assumption that these already
exist.
