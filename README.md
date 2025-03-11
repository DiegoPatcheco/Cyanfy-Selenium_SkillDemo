# Cyanfy-SkillDemo

## Overview
"Cyanfy-SkillDemo" is a project designed to demonstrate QA Automation expertise in Java, Selenium, BDD, Cucumber, Gherkin, CI/CD integration and agile testing. It enhances test readability and collaboration using Cucumber and Gherkin.

## Features
- Built using **Selenium WebDriver**, **Gherkin**, and **JUnit5** for test automation.
- Integrated with **Maven Wrapper** for simplified build and dependency management.
- CI/CD integration using **GitHub Actions** to ensure continuous testing and deployment.
- Generates detailed test reports automatically using **Cucumber Reports**.

## Prerequisites
Before setting up the project, ensure you have the following installed:
- [Amazon Corretto JDK 17.0.14](https://aws.amazon.com/corretto/)
- [Maven](https://maven.apache.org/install.html) (if not using Maven Wrapper)
- [Git](https://git-scm.com/downloads)
- Selenium-Java, Cucumber, Cucumber-reporting, and JUnit5 libraries (managed through Maven dependencies)

## Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/DiegoPatcheco/Cyanfy-SkillDemo.git
   ```
2. Navigate to the project directory:
   ```sh
   cd Cyanfy-SkillDemo
   ```
3. Install dependencies and build the project using Maven Wrapper:
   ```sh
   ./mvnw clean install
   ```

## Running Tests
To execute the automated tests, use the following command:
```sh
./mvnw clean test
```

You can also run a specific test suite using the `runSuite.sh` script:
```sh
./runSuite.sh
```

## Cucumber Reports
- The **Cucumber Report** is automatically generated after test execution.
- To view the report, run:
  ```sh
  ./openReport.sh
  ```

## CI/CD Integration
- The project is integrated with **GitHub Actions**, which automatically triggers the test suite on every push or pull request.
- Check the test results in the **Actions** tab on GitHub.
- **Cucumber Reports** are also generated and can be accessed after execution in the CI/CD pipeline.

## Contributing
Contributions are welcome! To contribute:
1. Fork the repository.
2. Create a new branch (`feature-branch`).
3. Commit your changes.
4. Push to the branch and submit a pull request.

## Author
Developed by [Diego Patcheco](https://github.com/DiegoPatcheco).
