# Samsung Checkout Automation Framework

## Overview

This project is an automation framework created for the Samsung ecommerce checkout assignment.

The main objective is to automate the checkout flow in Samsung's staging ecommerce environment, validating that a customer can:

* Add a SKU to the cart
* Proceed through checkout as a Guest user
* Fill personal information
* Fill address information
* Select delivery mode
* Complete payment using test credit cards
* Validate successful order placement
* Capture the generated order number

---

# Technologies Used

| Technology         | Purpose                                |
| ------------------ | -------------------------------------- |
| Java               | Programming language                   |
| Selenium WebDriver | Browser automation                     |
| TestNG             | Test execution framework               |
| Maven              | Dependency management and build tool   |
| WebDriverManager   | Automatic browser driver management    |
| IntelliJ IDEA      | IDE used for development               |
| Git + GitHub       | Version control and repository hosting |

---

# Framework Architecture

The framework follows a Page Object Model (POM) structure.

## Project Structure

```text
src/test/java
│
├── base
│   ├── BaseTest.java
│   └── DriverFactory.java
│
├── config
│   └── ConfigReader.java
│
├── models
│   ├── Customer.java
│   ├── TestCardData.java
│   └── TestCustomerData.java
│
├── pages
│   ├── BasePage.java
│   ├── CartPage.java
│   ├── GuestPage.java
│   ├── CheckoutPage.java
│   ├── PaymentPage.java
│   └── ConfirmationPage.java
│
├── tests
│   ├── SmokeTest.java
│   └── CheckoutTest.java
│
└── utils
    ├── ScreenshotUtils.java
    ├── TestDataGenerator.java
    └── WaitUtils.java
```

---

# Implemented Features

## Framework Features

* Selenium WebDriver integration
* TestNG test execution
* Page Object Model architecture
* Centralized driver management
* Reusable BasePage methods
* Configurable environment URLs
* Screenshot capture utility
* Screenshot-on-failure support
* Dynamic test data generation
* Timestamped screenshots
* Reusable waits
* Structured test data models

---

# Test Flow

The current automation flow is structured as follows:

```text
Initialize session cookies
↓
Call add-to-cart endpoint
↓
Open cart page
↓
Proceed as guest user
↓
Fill checkout information
↓
Fill address information
↓
Select delivery mode
↓
Enter payment information
↓
Place order
↓
Validate confirmation page
```

---

# Environment Investigation

Before implementing automation, a manual exploratory investigation was performed on the staging environment.

## Investigated URLs

### Cookie Initialization

```text
https://stg2.shop.samsung.com/getcookie.html
```

Purpose:

* Initializes ecommerce session cookies
* Establishes storefront session

---

### Add-To-Cart Endpoint

```text
https://stg2.shop.samsung.com/pe/ng/p4v1/addToCart
```

Purpose:

* Adds SKU directly into the cart session
* Allows faster and more stable checkout preparation

Observed response:

```json
{
  "resultCode":"0000",
  "resultMessage":"SUCCESS"
}
```

---

### Cart Page

```text
https://stg2.shop.samsung.com/pe/cart
```

Purpose:

* Opens ecommerce cart
* Allows progression into checkout flow

---

# Environment Stability Findings

During exploratory investigation, the staging environment presented instability related to frontend/backend integration.

## Observed Issues

* Blank storefront rendering
* CORS policy failures
* 403 preflight request failures
* Backend API communication failures

## Technical Observation

Frontend API requests targeting:

```text
s2-smb-api-cdn.ecom-stg.samsung.com
```

were rejected due to CORS/access-control failures.

This prevented the Angular storefront from rendering correctly.

## Impact

The Selenium framework itself remains functional.

However, the unstable staging environment may temporarily prevent successful execution of the full checkout flow until backend services stabilize.

---

# Design Decisions

## Why Use The Add-To-Cart API?

Instead of automating product navigation manually, the provided add-to-cart endpoint is used to:

* Reduce test execution time
* Reduce UI flakiness
* Focus automation efforts on checkout behavior
* Improve stability

This approach mirrors common enterprise automation strategies.

---

## Why Use Page Object Model?

Page Object Model was selected to:

* Improve maintainability
* Centralize locators
* Reduce duplicated code
* Improve readability
* Support future scalability

---

## Why Generate Dynamic Emails?

The assignment explicitly mentioned support for parallel execution.

Dynamic email generation prevents:

* Duplicate-user conflicts
* Session collisions
* Data reuse problems

---

# How To Run The Project

## Prerequisites

Install:

* Java 17+ (or compatible installed version)
* Maven
* Google Chrome
* IntelliJ IDEA (recommended)

---

# Clone Repository

```bash
git clone <repository-url>
```

---

# Install Dependencies

```bash
mvn clean install
```

---

# Run Tests

## Run All Tests

```bash
mvn test
```

## Run Specific Test

```bash
mvn test -Dtest=SmokeTest
```

---

# Screenshots

Screenshots are automatically saved inside:

```text
/screenshots
```

Automatic screenshots are captured:

* During execution
* On failures
* During important flow transitions

---

# Current Limitations

The following items depend on staging environment availability:

* Final locator validation
* Checkout flow stabilization
* Payment iframe handling validation
* Final order placement validation
* Order number capture

Placeholder locators are currently used in some Page Objects until the storefront becomes stable again.

---

# Planned Improvements

Future improvements may include:

* Parallel execution support
* API validation layer
* Better reporting integration
* Negative checkout scenarios
* Data-driven testing
* Cross-browser execution
* Headless execution toggle
* CI/CD integration

---

# Assignment Requirements Coverage

| Requirement                    | Status                |
| ------------------------------ | --------------------- |
| Selenium automation framework  | Implemented           |
| Guest checkout flow structure  | Implemented           |
| Screenshot support             | Implemented           |
| Dynamic email generation       | Implemented           |
| Page Object Model              | Implemented           |
| Configuration management       | Implemented           |
| Checkout flow architecture     | Implemented           |
| Order confirmation validation  | Structured            |
| Negative scenario preparation  | Planned               |
| Parallel execution preparation | Partially implemented |

---

# Learning Outcomes

This project provided practical experience with:

* Selenium automation
* Framework architecture
* Page Object Model
* Maven dependency management
* Browser automation
* GitHub workflow
* Staging environment investigation
* Frontend/backend debugging
* QA automation best practices

---

# Final Notes

Even with staging instability, the project successfully established:

* A scalable automation architecture
* Reusable utilities and abstractions
* Structured checkout automation flow
* Environment investigation documentation
* Automation engineering foundations

The framework is prepared for rapid completion once the staging environment becomes stable again.
