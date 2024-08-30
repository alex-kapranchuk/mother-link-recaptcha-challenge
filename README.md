# Project mother-link-recaptcha-challenge
## Project Overview
This project automates tests for a web application using Java, Playwright, JUnit 5, Maven, Allure, Log4j, and Instancio. The tests include user registration verification and integration with ReCaptcha.

### Technology Stack
- Java - Programming language
- Playwright - Browser automation tool
- JUnit 5 - Testing framework
- Maven - Build automation tool
- Allure - Reporting tool
- Log4j - Logging
- Instancio - Test data generation

## Setup
### Test Preparation
#### RegisterTest e2e

1. Download the [Chrome extension](https://chromewebstore.google.com/detail/nocoding-data-scraper-eas/ojaffphbffmdaicdkahnmihipclmepok) to bypass ReCaptcha
2. Download the extension from [CRX Extractor](https://crxextractor.com/)
3. Extract the extension using [CRX Extractor](https://crxextractor.com/)
4. Copy the path to the extension folder EXTENSION_PATH
5. Set up the environment variable EXTENSION_PATH in your system:

```java
RegisterTestE2E class 

System.getenv("EXTENSION_PATH");
```
#### RegisterTest 
1. Log in to [Motherlink](https://mc-99999.motherlink.io/)
2. [Save the cookies](https://chromewebstore.google.com/detail/editthiscookie/fngmhnnpilhplaeedifhccceomclgfbg?hl=uk) as a JSON file with user login data.
3. Place the cookies file in 
```file
src/main/resources/cookies.json.
```

## Running Tests
1. Ensure all dependencies are installed:
```bash
mvn install
```
2. Run the tests:
```bash
mvn test
```
3. Generate Allure reports:
```bash
mvn allure:serve
```
### Logging Configuration
Logging is configured using Log4j. The configuration is stored in src/main/resources/log4j2.xml.

### Test Data Generation
Test data is generated using Instancio. Configuration and data generation are defined in the respective test classes.

### Documentation
Documentation for the tests is available in the Allure reports after running the tests.
