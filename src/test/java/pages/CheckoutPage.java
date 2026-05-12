package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

public class CheckoutPage extends BasePage {

    private final By pageBody = By.tagName("body");

    // Personal information
    private final By firstNameField =
            By.xpath("//input[@name='firstName' or contains(@placeholder, 'Nombre')]");

    private final By lastNameField =
            By.xpath("//input[@name='lastName' or contains(@placeholder, 'Apellido')]");

    private final By phoneField =
            By.xpath("//input[@name='phone' or contains(@placeholder, 'Celular')]");

    private final By documentTypeDropdown =
            By.xpath("//*[contains(normalize-space(), 'Tipo de documento')]/following::*[self::mat-select or @role='combobox' or @role='button' or contains(@class,'select')][1]");

    private final By dniOption =
            By.xpath("//*[normalize-space()='DNI']");

    private final By documentNumberField =
            By.xpath(
                    "//label[contains(normalize-space(), 'Número de documento')]/following::input[1] " +
                            "| //*[contains(normalize-space(), 'Número de documento')]/following::input[1]"
            );

    // Delivery
    private final By deliverySection =
            By.xpath("//*[contains(normalize-space(), 'Dirección de entrega')]");

    private final By activeDeliveryDepartmentDropdown =
            By.xpath(
                    "//*[contains(normalize-space(), 'Departamento')]" +
                            "/following::*[self::select or @role='button' or @role='combobox' or contains(@class,'select')][1]"
            );

    private final String ADDRESS_LABEL = "Dirección";

    private final String STREET_NUMBER_LABEL = "Número";

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void waitUntilCheckoutPageIsLoaded() {
        waitForAnyTextInBody(
                "Datos de cliente",
                "Nombre",
                "Apellido",
                "Correo electrónico",
                "Tipo de documento"
        );
    }

    public boolean isCheckoutPageLoaded() {
        waitUntilCheckoutPageIsLoaded();

        String bodyText = getText(pageBody);

        return bodyText.contains("Datos de cliente")
                || bodyText.contains("Nombre")
                || bodyText.contains("Apellido")
                || bodyText.contains("Correo electrónico")
                || bodyText.contains("Tipo de documento");
    }


    public void fillPersonalInformation(
            String firstName,
            String lastName,
            String phone,
            String documentNumber
    ) {
        type(firstNameField, firstName);
        dispatchAngularEvents(waitForVisibility(firstNameField));

        type(lastNameField, lastName);
        dispatchAngularEvents(waitForVisibility(lastNameField));

        type(phoneField, phone);
        dispatchAngularEvents(waitForVisibility(phoneField));

        selectDocumentTypeDni();

        enterDocumentNumber(documentNumber);
    }

    private void selectDocumentTypeDni() {
        System.out.println("STEP: Selecting document type DNI");

        click(documentTypeDropdown);

        click(dniOption);
    }

    private void enterDocumentNumber(String documentNumber) {
        System.out.println("STEP: Filling document number");

        WebElement element = waitForVisibility(documentNumberField);

        scrollToCenter(element);

        element.click();

        element.clear();

        element.sendKeys(documentNumber);

        dispatchAngularEvents(element);
    }

    public void continueFromPersonalInformation() {
        System.out.println("STEP: Continuing from personal information");

        clickContinueButtonAfterDocumentNumber();

        waitUntilDeliverySectionIsActuallyOpen();
    }

    private void clickContinueButtonAfterDocumentNumber() {
        WebElement documentInput = waitForVisibility(documentNumberField);

        WebElement continueButton =
                (WebElement) ((JavascriptExecutor) driver)
                        .executeScript(
                                "const input = arguments[0];" +
                                        "const inputTop = input.getBoundingClientRect().top;" +
                                        "const buttons = Array.from(document.querySelectorAll('button'));" +
                                        "const candidates = buttons.filter(btn => {" +
                                        "  const text = (btn.innerText || btn.textContent || '').trim();" +
                                        "  const rect = btn.getBoundingClientRect();" +
                                        "  const visible = rect.width > 0 && rect.height > 0;" +
                                        "  const enabled = !btn.disabled;" +
                                        "  return visible && enabled && text === 'Continuar' && rect.top > inputTop;" +
                                        "});" +
                                        "candidates.sort((a, b) => a.getBoundingClientRect().top - b.getBoundingClientRect().top);" +
                                        "return candidates[0] || null;",
                                documentInput
                        );

        if (continueButton == null) {
            throw new RuntimeException("Could not find enabled Continuar button after document number field.");
        }

        scrollToCenter(continueButton);

        try {
            continueButton.click();
        } catch (Exception e) {
            System.out.println("Standard click failed. Trying JavaScript click.");
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", continueButton);
        }
    }

    private void waitUntilDeliverySectionIsActuallyOpen() {
        System.out.println("STEP: Waiting for delivery section to open");

        waitForClickability(activeDeliveryDepartmentDropdown);
    }

    public boolean isAddressSectionEnabled() {
        waitUntilDeliverySectionIsActuallyOpen();

        return waitForVisibility(deliverySection).isDisplayed();
    }

    public void fillDeliveryAddress(String address, String number) {
        System.out.println("STEP: Selecting department");

        selectMatDropdownByLabel("Departamento", "LIMA");

        System.out.println("STEP: Selecting province");

        selectMatDropdownByLabel("Provincia", "LIMA");

        System.out.println("STEP: Selecting district");

        selectMatDropdownByLabel("Distrito", "BARRANCO");

        System.out.println("STEP: Filling address fields");

        typeInputByLabel(ADDRESS_LABEL, address);

        typeInputByLabel(STREET_NUMBER_LABEL, number);
    }

    private void selectMatDropdownByLabel(String labelText, String optionText) {
        WebElement dropdown = waitUntilDropdownByLabelIsEnabled(labelText);

        scrollToCenter(dropdown);

        try {
            dropdown.click();
        } catch (Exception e) {
            System.out.println("Standard dropdown click failed. Trying JavaScript click.");
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", dropdown);
        }

        WebElement option = waitUntilMatOptionIsVisible(optionText);

        try {
            option.click();
        } catch (Exception e) {
            System.out.println("Standard option click failed. Trying JavaScript click.");
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", option);
        }

        waitForDropdownOverlayToClose();
    }

    private WebElement waitUntilDropdownByLabelIsEnabled(String labelText) {
        return wait.until(driver -> (WebElement) ((JavascriptExecutor) driver)
                .executeScript(
                        "const label = arguments[0].toLowerCase();" +
                                "const fields = Array.from(document.querySelectorAll('mat-form-field, .mat-mdc-form-field'));" +
                                "const field = fields.find(f => (f.innerText || '').toLowerCase().includes(label));" +
                                "if (!field) return null;" +
                                "const select = field.querySelector('mat-select');" +
                                "if (!select) return null;" +
                                "const ariaDisabled = select.getAttribute('aria-disabled');" +
                                "const className = select.className || '';" +
                                "if (ariaDisabled === 'true' || className.includes('mat-mdc-select-disabled')) return null;" +
                                "return select;",
                        labelText
                ));
    }

    private WebElement waitUntilMatOptionIsVisible(String optionText) {
        return wait.until(driver -> (WebElement) ((JavascriptExecutor) driver)
                .executeScript(
                        "const optionText = arguments[0].toLowerCase();" +
                                "const options = Array.from(document.querySelectorAll('mat-option'));" +
                                "return options.find(option => {" +
                                "  const text = (option.innerText || option.textContent || '').trim().toLowerCase();" +
                                "  const rect = option.getBoundingClientRect();" +
                                "  const visible = rect.width > 0 && rect.height > 0;" +
                                "  return visible && text === optionText;" +
                                "}) || null;",
                        optionText
                ));
    }

    private void waitForDropdownOverlayToClose() {
        wait.until(driver ->
                driver.findElements(
                        By.cssSelector(".cdk-overlay-pane mat-option")
                ).isEmpty()
        );
    }

    private void typeInputByLabel(String labelText, String value) {
        WebElement input = waitUntilInputByLabelIsVisible(labelText);

        scrollToCenter(input);

        input.click();

        input.clear();

        input.sendKeys(value);

        dispatchAngularEvents(input);
    }

    private WebElement waitUntilInputByLabelIsVisible(String labelText) {
        return wait.until(driver -> (WebElement) ((JavascriptExecutor) driver)
                .executeScript(
                        "const label = arguments[0].toLowerCase();" +
                                "const fields = Array.from(document.querySelectorAll('mat-form-field, .mat-mdc-form-field'));" +
                                "const field = fields.find(f => {" +
                                "  const text = (f.innerText || '').toLowerCase();" +
                                "  const hasInput = !!f.querySelector('input');" +
                                "  const rect = f.getBoundingClientRect();" +
                                "  return hasInput && text.includes(label) && rect.width > 0 && rect.height > 0;" +
                                "});" +
                                "if (!field) return null;" +
                                "const input = field.querySelector('input');" +
                                "const rect = input.getBoundingClientRect();" +
                                "if (rect.width === 0 || rect.height === 0) return null;" +
                                "return input;",
                        labelText
                ));
    }

    public void selectDeliveryMode() {
        System.out.println("STEP: Selecting delivery mode");

        WebElement shippingCard =
                wait.until(driver -> (WebElement) ((JavascriptExecutor) driver)
                        .executeScript(
                                "const cards = Array.from(document.querySelectorAll('div, label, mat-card'));" +
                                        "const candidates = cards.filter(el => {" +
                                        "  const text = (el.innerText || el.textContent || '').toLowerCase();" +
                                        "  const rect = el.getBoundingClientRect();" +
                                        "  return rect.width > 200 && rect.height > 40" +
                                        "    && text.includes('envío regular')" +
                                        "    && text.includes('49.00');" +
                                        "});" +
                                        "candidates.sort((a, b) => {" +
                                        "  const areaA = a.getBoundingClientRect().width * a.getBoundingClientRect().height;" +
                                        "  const areaB = b.getBoundingClientRect().width * b.getBoundingClientRect().height;" +
                                        "  return areaA - areaB;" +
                                        "});" +
                                        "return candidates[0] || null;"
                        ));

        ((JavascriptExecutor) driver)
                .executeScript(
                        "arguments[0].scrollIntoView({block: 'center'});" +
                                "arguments[0].click();",
                        shippingCard
                );

        waitShortly();

        scrollDownToBillingTermsArea();
    }

    public boolean isDeliveryModeSelected() {
        String bodyText = getText(pageBody);

        return bodyText.contains("Completa tus datos de facturación")
                || bodyText.contains("Continuar con los métodos de pago")
                || bodyText.contains("Declaro que he leído");
    }

    public void acceptRequiredTerms() {
        System.out.println("STEP: Accepting required terms");

        scrollDownToBillingTermsArea();

        clickCircleToLeftOfText("Declaro que he leído");
    }

    private void scrollDownToBillingTermsArea() {
        System.out.println("STEP: Scrolling to billing terms area");

        for (int i = 0; i < 6; i++) {
            ((JavascriptExecutor) driver)
                    .executeScript("window.scrollBy(0, 350);");

            waitShortly();

            String bodyText = getText(pageBody);

            if (bodyText.contains("Declaro que he leído")
                    || bodyText.contains("Continuar con los métodos de pago")) {
                return;
            }
        }
    }

    private void clickCircleToLeftOfText(String visibleText) {
        wait.until(driver -> {
            Object clicked =
                    ((JavascriptExecutor) driver)
                            .executeScript(
                                    "const target = arguments[0].toLowerCase();" +
                                            "const elements = Array.from(document.querySelectorAll('label, span, div, p'));" +
                                            "const candidates = elements.filter(el => {" +
                                            "  const text = (el.innerText || el.textContent || '').trim().toLowerCase();" +
                                            "  const rect = el.getBoundingClientRect();" +
                                            "  return text.includes(target)" +
                                            "    && rect.width > 0" +
                                            "    && rect.height > 0" +
                                            "    && text.length < 220;" +
                                            "});" +
                                            "if (candidates.length === 0) return false;" +
                                            "candidates.sort((a, b) => {" +
                                            "  const ra = a.getBoundingClientRect();" +
                                            "  const rb = b.getBoundingClientRect();" +
                                            "  return (ra.width * ra.height) - (rb.width * rb.height);" +
                                            "});" +
                                            "const textElement = candidates[0];" +
                                            "textElement.scrollIntoView({block: 'center'});" +
                                            "const rect = textElement.getBoundingClientRect();" +
                                            "const x = rect.left - 18;" +
                                            "const y = rect.top + rect.height / 2;" +
                                            "const targetElement = document.elementFromPoint(x, y);" +
                                            "if (!targetElement) return false;" +
                                            "targetElement.dispatchEvent(new MouseEvent('mousedown', { bubbles: true, clientX: x, clientY: y }));" +
                                            "targetElement.dispatchEvent(new MouseEvent('mouseup', { bubbles: true, clientX: x, clientY: y }));" +
                                            "targetElement.dispatchEvent(new MouseEvent('click', { bubbles: true, clientX: x, clientY: y }));" +
                                            "return true;",
                                    visibleText
                            );

            return Boolean.TRUE.equals(clicked);
        });
    }

    public void continueToPayment() {
        System.out.println("STEP: Continuing to payment");

        scrollDownToBillingTermsArea();

        WebElement button =
                wait.until(driver -> (WebElement) ((JavascriptExecutor) driver)
                        .executeScript(
                                "const buttons = Array.from(document.querySelectorAll('button'));" +
                                        "return buttons.find(btn => {" +
                                        "  const text = (btn.innerText || btn.textContent || '').trim().toLowerCase();" +
                                        "  const rect = btn.getBoundingClientRect();" +
                                        "  return rect.width > 0 && rect.height > 0" +
                                        "    && !btn.disabled" +
                                        "    && text.includes('continuar con los métodos de pago');" +
                                        "}) || null;"
                        ));

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", button);

        waitUntilPaymentMethodsAreLoaded();
    }

    private void waitUntilPaymentMethodsAreLoaded() {
        System.out.println("STEP: Waiting for checkout payment methods to load");

        wait.until(driver -> {
            String bodyText =
                    driver.findElement(By.tagName("body"))
                            .getText();

            return bodyText.contains("Tarjeta de Crédito")
                    || bodyText.contains("Tarjeta de Credito");
        });

        waitShortly();
    }

    public boolean isPaymentSectionVisible() {
        waitUntilPaymentMethodsAreLoaded();

        String bodyText = getText(pageBody);

        return bodyText.contains("Tarjeta de Crédito")
                || bodyText.contains("Tarjeta de Credito");
    }

    public void selectCreditCardPayment() {
        System.out.println("STEP: Selecting credit/debit card payment");

        waitUntilPaymentMethodsAreLoaded();

        WebElement creditCardRow =
                wait.until(driver -> (WebElement) ((JavascriptExecutor) driver)
                        .executeScript(
                                "const elements = Array.from(document.querySelectorAll('div, button, mat-expansion-panel, mat-expansion-panel-header'));" +
                                        "const rows = elements.filter(el => {" +
                                        "  const text = (el.innerText || el.textContent || '').trim().toLowerCase();" +
                                        "  const rect = el.getBoundingClientRect();" +
                                        "  return rect.width > 400" +
                                        "    && rect.height > 50" +
                                        "    && rect.top >= 0" +
                                        "    && rect.bottom <= window.innerHeight" +
                                        "    && (text.includes('tarjeta de crédito') || text.includes('tarjeta de credito'))" +
                                        "    && text.includes('débito');" +
                                        "});" +
                                        "rows.sort((a, b) => {" +
                                        "  const ra = a.getBoundingClientRect();" +
                                        "  const rb = b.getBoundingClientRect();" +
                                        "  return (ra.width * ra.height) - (rb.width * rb.height);" +
                                        "});" +
                                        "return rows[0] || null;"
                        ));

        ((JavascriptExecutor) driver)
                .executeScript(
                        "arguments[0].scrollIntoView({block: 'center'});",
                        creditCardRow
                );

        waitShortly();

        ((JavascriptExecutor) driver)
                .executeScript(
                        "const el = arguments[0];" +
                                "const rect = el.getBoundingClientRect();" +
                                "const x = rect.right - 40;" +
                                "const y = rect.top + rect.height / 2;" +
                                "const target = document.elementFromPoint(x, y);" +
                                "target.click();",
                        creditCardRow
                );

        waitUntilCreditCardFieldsAreVisible();
    }

    private void waitUntilCreditCardFieldsAreVisible() {

        waitForAnyTextInBody(
                "Número de tarjeta",
                "Nombre del titular",
                "MM/AA",
                "CVV",
                "Realizar pedido"
        );

        System.out.println("STEP: Payment fields detected. Waiting for fields to become usable.");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void printVisiblePaymentInputs() {

        Object result =
                ((JavascriptExecutor) driver)
                        .executeScript(
                                "const inputs = Array.from(document.querySelectorAll('input'));" +
                                        "return inputs.map((input, index) => {" +
                                        "  const rect = input.getBoundingClientRect();" +
                                        "  const type = input.getAttribute('type');" +
                                        "  const placeholder = input.getAttribute('placeholder');" +
                                        "  const name = input.getAttribute('name');" +
                                        "  const value = input.value;" +
                                        "  const visible = rect.width > 40 && rect.height > 10;" +
                                        "  return index + ' | visible=' + visible" +
                                        "    + ' | type=' + type" +
                                        "    + ' | placeholder=' + placeholder" +
                                        "    + ' | name=' + name" +
                                        "    + ' | value=' + value" +
                                        "    + ' | top=' + rect.top" +
                                        "    + ' | left=' + rect.left" +
                                        "    + ' | width=' + rect.width;" +
                                        "}).join('\\n');"
                        );

        System.out.println("VISIBLE PAYMENT INPUT DEBUG:");
        System.out.println(result);
    }

    public void enterCardDetails(
            String cardNumber,
            String cardholderName,
            String expiration,
            String cvv
    ) {
        System.out.println("STEP: Entering card number");
        clickBelowPaymentLabelAndType(
                "Número de tarjeta",
                "cardNumber",
                0.70,
                cardNumber
        );

        waitShortly();

        System.out.println("STEP: Entering cardholder name");
        clickCardholderNameFieldAndType(cardholderName);

        waitShortly();

        System.out.println("STEP: Entering expiration date");
        clickRelativeToCardholderAndType(
                "expiration",
                -310,
                95,
                expiration
        );

        waitShortly();

        System.out.println("STEP: Entering CVV");
        clickRelativeToCardholderAndType(
                "cvv",
                0,
                95,
                cvv
        );

        waitShortly();
    }


    public void placeOrder() {

        System.out.println("STEP: Placing order");

        WebElement placeOrderButton =
                wait.until(driver -> (WebElement) ((JavascriptExecutor) driver)
                        .executeScript(
                                "const buttons = Array.from(document.querySelectorAll('button'));" +
                                        "const button = buttons.find(btn => {" +
                                        "  const text = (btn.innerText || btn.textContent || '').trim().toLowerCase();" +
                                        "  const rect = btn.getBoundingClientRect();" +
                                        "  return text.includes('realizar pedido')" +
                                        "    && rect.width > 0" +
                                        "    && rect.height > 0;" +
                                        "});" +
                                        "if (!button) return null;" +
                                        "button.scrollIntoView({block: 'center'});" +
                                        "return button;"
                        ));

        waitShortly();

        wait.until(driver -> {

            String disabled =
                    placeOrderButton.getAttribute("disabled");

            return disabled == null
                    && placeOrderButton.isDisplayed()
                    && placeOrderButton.isEnabled();
        });

        try {
            placeOrderButton.click();
        } catch (Exception e) {
            System.out.println("Standard place order click failed. Trying JavaScript click.");

            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", placeOrderButton);
        }
    }

    private void clickBelowPaymentLabelAndType(
            String labelText,
            String expectedActiveName,
            double xRatio,
            String value
    ) {

        System.out.println("STEP: Clicking textbox under label: " + labelText);

        WebElement label =
                wait.until(driver -> (WebElement) ((JavascriptExecutor) driver)
                        .executeScript(
                                "const target = arguments[0].toLowerCase();" +
                                        "const elements = Array.from(document.querySelectorAll('label, span, div, p'));" +
                                        "const candidates = elements.filter(el => {" +
                                        "  const text = (el.innerText || el.textContent || '').trim().toLowerCase();" +
                                        "  const rect = el.getBoundingClientRect();" +
                                        "  return text.includes(target)" +
                                        "    && rect.width > 0" +
                                        "    && rect.height > 0" +
                                        "    && text.length < 120;" +
                                        "});" +
                                        "if (candidates.length === 0) return null;" +
                                        "candidates.sort((a, b) => {" +
                                        "  const ra = a.getBoundingClientRect();" +
                                        "  const rb = b.getBoundingClientRect();" +
                                        "  return (ra.width * ra.height) - (rb.width * rb.height);" +
                                        "});" +
                                        "return candidates[0];",
                                labelText
                        ));

        scrollToCenter(label);

        int labelWidth =
                label.getSize().getWidth();

        int labelHeight =
                label.getSize().getHeight();

        int xOffset =
                -(labelWidth / 2) + (int) (labelWidth * xRatio);

        int yOffset =
                (labelHeight / 2) + 14;

        new Actions(driver)
                .moveToElement(label, xOffset, yOffset)
                .click()
                .perform();

        WebElement activeElement =
                driver.switchTo().activeElement();

        System.out.println(
                "Active element after clicking " + labelText
                        + ": name=" + activeElement.getAttribute("name")
        );

        wait.until(driver ->
                expectedActiveName.equals(
                        driver.switchTo().activeElement().getAttribute("name")
                )
        );

        new Actions(driver)
                .sendKeys(value)
                .perform();
    }

    private void clickCardholderNameFieldAndType(String cardholderName) {

        wait.until(driver -> {

            Object focused =
                    ((JavascriptExecutor) driver)
                            .executeScript(
                                    "const input = document.querySelector('input[name=\"input-checkout__cardholderName\"]');" +
                                            "if (!input) return false;" +
                                            "const rect = input.getBoundingClientRect();" +
                                            "if (rect.width === 0 || rect.height === 0) return false;" +
                                            "input.scrollIntoView({block: 'center'});" +
                                            "input.focus();" +
                                            "input.click();" +
                                            "return document.activeElement === input;"
                            );

            return Boolean.TRUE.equals(focused);
        });

        WebElement activeElement =
                driver.switchTo().activeElement();

        System.out.println(
                "Active element after clicking cardholder name: name="
                        + activeElement.getAttribute("name")
        );

        new Actions(driver)
                .keyDown(org.openqa.selenium.Keys.CONTROL)
                .sendKeys("a")
                .keyUp(org.openqa.selenium.Keys.CONTROL)
                .sendKeys(org.openqa.selenium.Keys.BACK_SPACE)
                .sendKeys(cardholderName)
                .perform();
    }

    private void clickRelativeToCardholderAndType(
            String fieldDescription,
            int xOffset,
            int yOffset,
            String value
    ) {
        System.out.println("STEP: Clicking relative position for " + fieldDescription);

        WebElement cardholderInput =
                wait.until(driver -> driver.findElement(
                        By.cssSelector("input[name='input-checkout__cardholderName']")
                ));

        scrollToCenter(cardholderInput);

        new Actions(driver)
                .moveToElement(cardholderInput, xOffset, yOffset)
                .click()
                .perform();

        WebElement activeElement =
                driver.switchTo().activeElement();

        System.out.println(
                "Active element after clicking " + fieldDescription
                        + ": name=" + activeElement.getAttribute("name")
        );

        new Actions(driver)
                .sendKeys(value)
                .perform();
    }

    private void waitShortly() {
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void scrollToCenter(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript(
                        "arguments[0].scrollIntoView({block: 'center'});",
                        element
                );
    }

    private void dispatchAngularEvents(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript(
                        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                                "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));" +
                                "arguments[0].dispatchEvent(new Event('blur', { bubbles: true }));",
                        element
                );
    }
}