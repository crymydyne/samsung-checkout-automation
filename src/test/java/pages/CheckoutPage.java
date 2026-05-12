package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CheckoutPage extends BasePage {

    private final By pageBody = By.tagName("body");

    // Personal information section
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

    // Delivery section
    private final By deliverySection =
            By.xpath("//*[contains(normalize-space(), 'Dirección de entrega')]");

    private final By activeDeliveryDepartmentDropdown =
            By.xpath(
                    "//*[contains(normalize-space(), 'Departamento')]" +
                            "/following::*[self::select or @role='button' or @role='combobox' or contains(@class,'select')][1]"
            );

    private final String ADDRESS_LABEL = "Dirección";

    private final String STREET_NUMBER_LABEL = "Número";

    private final By firstShippingOption =
            By.xpath("(//*[contains(normalize-space(), 'Envío regular') or contains(normalize-space(), 'Agenda tu envío')])[1]");

    private final By continueToPaymentButton =
            By.xpath("//button[contains(normalize-space(), 'Continuar con los métodos de pago')]");

    // Payment section
    private final By paymentSection =
            By.xpath("//*[contains(normalize-space(), 'Pago')]");

    private final By creditCardSection =
            By.xpath("//*[contains(normalize-space(), 'Tarjeta de Crédito') or contains(normalize-space(), 'Tarjeta de Débito')]");

    private final By cardNumberField =
            By.xpath("//*[contains(normalize-space(), 'Número de tarjeta')]/following::input[1]");

    private final By cardholderNameField =
            By.xpath("//*[contains(normalize-space(), 'Nombre del titular')]/following::input[1]");

    private final By expirationDateField =
            By.xpath("//*[contains(normalize-space(), 'MM/AA')]/following::input[1]");

    private final By cvvField =
            By.xpath("//*[contains(normalize-space(), 'CVV')]/following::input[1]");

    private final By placeOrderButton =
            By.xpath("//button[contains(normalize-space(), 'Realizar pedido')]");

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

    public String getCheckoutPageText() {
        waitUntilCheckoutPageIsLoaded();
        return getText(pageBody);
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

        printPersonalInfoDebugInfo();

        clickContinueButtonAfterDocumentNumber();

        waitUntilDeliverySectionIsActuallyOpen();
    }

    private void clickContinueButtonAfterDocumentNumber() {
        WebElement documentInput =
                waitForVisibility(documentNumberField);

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
            printContinueButtons();
            throw new RuntimeException("Could not find enabled Continuar button after document number field.");
        }

        scrollToCenter(continueButton);

        try {
            continueButton.click();
        } catch (Exception e) {
            System.out.println("Standard click failed. Trying JavaScript click.");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", continueButton);
        }
    }

    private void waitUntilDeliverySectionIsActuallyOpen() {
        System.out.println("STEP: Waiting for delivery section to open");

        waitForClickability(activeDeliveryDepartmentDropdown);
    }

    private void typeInputByLabel(String labelText, String value) {

        WebElement input =
                waitUntilInputByLabelIsVisible(labelText);

        scrollToCenter(input);

        input.click();

        input.clear();

        input.sendKeys(value);

        dispatchAngularEvents(input);
    }

    private WebElement waitUntilInputByLabelIsVisible(String labelText) {

        return wait.until(driver -> {

            WebElement input =
                    (WebElement) ((JavascriptExecutor) driver)
                            .executeScript(
                                    "const label = arguments[0].toLowerCase();" +
                                            "const fields = Array.from(document.querySelectorAll('mat-form-field, .mat-mdc-form-field'));" +
                                            "const field = fields.find(f => {" +
                                            "  const text = (f.innerText || '').toLowerCase();" +
                                            "  const hasInput = !!f.querySelector('input');" +
                                            "  return hasInput && text.includes(label);" +
                                            "});" +
                                            "if (!field) return null;" +
                                            "const input = field.querySelector('input');" +
                                            "const rect = input.getBoundingClientRect();" +
                                            "if (rect.width === 0 || rect.height === 0) return null;" +
                                            "return input;",
                                    labelText
                            );

            return input;
        });
    }

    public void waitUntilDeliverySectionIsAvailable() {
        waitUntilDeliverySectionIsActuallyOpen();
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

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        scrollDownToBillingTermsArea();
    }

    public boolean isDeliveryModeSelected() {

        String bodyText = getText(pageBody);

        return bodyText.contains("Completa tus datos de facturación")
                || bodyText.contains("Continuar con los métodos de pago")
                || bodyText.contains("Autorizo el tratamiento")
                || bodyText.contains("Términos y Condiciones");
    }

    public void acceptRequiredTerms() {

        System.out.println("STEP: Accepting required terms");

        scrollDownToBillingTermsArea();

        clickCircleToLeftOfText("Declaro que he leído");
    }

    public void continueToPayment() {

        System.out.println("STEP: Continuing to payment");

        scrollDownUntilTextExists("Continuar con los métodos de pago");

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
                .executeScript(
                        "arguments[0].scrollIntoView({block: 'center'});" +
                                "arguments[0].click();",
                        button
                );

        waitUntilPaymentSectionIsVisible();
    }

    public void waitUntilPaymentSectionIsVisible() {

        waitForAnyTextInBody(
                "3. Pago",
                "Pago único",
                "Tarjeta de Crédito",
                "Tarjeta de Débito",
                "Banca por Internet",
                "Cuotéalo",
                "Pago Efectivo"
        );
    }

    public boolean isPaymentSectionVisible() {

        waitUntilPaymentSectionIsVisible();

        String bodyText =
                getText(pageBody);

        System.out.println("Payment section validation text:");
        System.out.println(bodyText);

        return bodyText.contains("Pago")
                || bodyText.contains("Tarjeta de Crédito")
                || bodyText.contains("Tarjeta de Débito")
                || bodyText.contains("Banca por Internet")
                || bodyText.contains("Cuotéalo")
                || bodyText.contains("Pago Efectivo");
    }

    public void selectCreditCardPayment() {

        System.out.println("STEP: Selecting credit/debit card payment");

        WebElement creditCardOption =
                wait.until(driver -> (WebElement) ((JavascriptExecutor) driver)
                        .executeScript(
                                "const elements = Array.from(document.querySelectorAll('div, button, mat-expansion-panel-header'));" +
                                        "return elements.find(el => {" +
                                        "  const text = (el.innerText || el.textContent || '').toLowerCase();" +
                                        "  const rect = el.getBoundingClientRect();" +
                                        "  return rect.width > 0 && rect.height > 0" +
                                        "    && text.includes('tarjeta de crédito');" +
                                        "}) || null;"
                        ));

        scrollToCenter(creditCardOption);

        try {
            creditCardOption.click();
        } catch (Exception e) {
            System.out.println("Standard credit card click failed. Trying JavaScript click.");

            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", creditCardOption);
        }

        waitUntilCreditCardFieldsAreVisible();
    }

    public void enterCardDetails(
            String cardNumber,
            String cardholderName,
            String expiration,
            String cvv
    ) {
        type(cardNumberField, cardNumber);

        type(cardholderNameField, cardholderName);

        type(expirationDateField, expiration);

        type(cvvField, cvv);
    }

    public void placeOrder() {
        System.out.println("STEP: Placing order");

        click(placeOrderButton);
    }

    public boolean isPaymentErrorDisplayed() {
        String bodyText = getText(pageBody);

        return bodyText.contains("no puede ser procesado")
                || bodyText.contains("no se pudo procesar")
                || bodyText.contains("rechazado")
                || bodyText.contains("error");
    }

    public void printPersonalInfoDebugInfo() {
        System.out.println("Personal info debug:");

        Object result =
                ((JavascriptExecutor) driver)
                        .executeScript(
                                "const inputs = Array.from(document.querySelectorAll('input'));" +
                                        "return inputs.map((input, index) => {" +
                                        " return index + " +
                                        " ' | placeholder=' + input.getAttribute('placeholder') +" +
                                        " ' | name=' + input.getAttribute('name') +" +
                                        " ' | value=' + input.value +" +
                                        " ' | valid=' + input.checkValidity();" +
                                        "}).join('\\n');"
                        );

        System.out.println(result);

        Object buttons =
                ((JavascriptExecutor) driver)
                        .executeScript(
                                "return Array.from(document.querySelectorAll('button')).map((btn, index) => {" +
                                        " const rect = btn.getBoundingClientRect();" +
                                        " return index + " +
                                        " ' | text=' + (btn.innerText || btn.textContent || '').trim() +" +
                                        " ' | disabled=' + btn.disabled +" +
                                        " ' | visible=' + (rect.width > 0 && rect.height > 0) +" +
                                        " ' | top=' + rect.top;" +
                                        "}).join('\\n');"
                        );

        System.out.println("Button debug:");
        System.out.println(buttons);
    }

    private void selectMatDropdownByLabel(String labelText, String optionText) {
        WebElement dropdown =
                waitUntilDropdownByLabelIsEnabled(labelText);

        scrollToCenter(dropdown);

        try {
            dropdown.click();
        } catch (Exception e) {
            System.out.println("Standard dropdown click failed. Trying JavaScript click.");
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", dropdown);
        }

        WebElement option =
                waitUntilMatOptionIsVisible(optionText);

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
        return wait.until(driver -> {
            WebElement dropdown =
                    (WebElement) ((JavascriptExecutor) driver)
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
                            );

            return dropdown;
        });
    }

    private WebElement waitUntilMatOptionIsVisible(String optionText) {
        return wait.until(driver -> {
            WebElement option =
                    (WebElement) ((JavascriptExecutor) driver)
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
                            );

            return option;
        });
    }

    private void waitForDropdownOverlayToClose() {
        wait.until(driver ->
                driver.findElements(
                        By.cssSelector(".cdk-overlay-pane mat-option")
                ).isEmpty()
        );
    }

    private void printContinueButtons() {
        System.out.println("Visible Continuar buttons:");

        Object result =
                ((JavascriptExecutor) driver)
                        .executeScript(
                                "return Array.from(document.querySelectorAll('button')).map((btn, index) => {" +
                                        " const rect = btn.getBoundingClientRect();" +
                                        " return index + ' | text=' + (btn.innerText || btn.textContent || '').trim()" +
                                        " + ' | disabled=' + btn.disabled" +
                                        " + ' | visible=' + (rect.width > 0 && rect.height > 0)" +
                                        " + ' | top=' + rect.top" +
                                        " + ' | class=' + btn.className;" +
                                        "}).join('\\n');"
                        );

        System.out.println(result);
    }

    private WebElement waitUntilElementContainingTextIsVisible(String... possibleTexts) {

        return wait.until(driver -> {

            WebElement element =
                    (WebElement) ((JavascriptExecutor) driver)
                            .executeScript(
                                    "const texts = Array.from(arguments).map(t => t.toLowerCase());" +
                                            "const elements = Array.from(document.querySelectorAll('button, div, span, label, mat-card, mat-radio-button'));" +
                                            "return elements.find(el => {" +
                                            "  const text = (el.innerText || el.textContent || '').trim().toLowerCase();" +
                                            "  const rect = el.getBoundingClientRect();" +
                                            "  const visible = rect.width > 0 && rect.height > 0;" +
                                            "  return visible && texts.some(t => text.includes(t));" +
                                            "}) || null;",
                                    (Object[]) possibleTexts
                            );

            return element;
        });
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

                                            // Prefer the smallest visible element containing the text,
                                            // not a large parent container.
                                            "candidates.sort((a, b) => {" +
                                            "  const ra = a.getBoundingClientRect();" +
                                            "  const rb = b.getBoundingClientRect();" +
                                            "  return (ra.width * ra.height) - (rb.width * rb.height);" +
                                            "});" +

                                            "const textElement = candidates[0];" +
                                            "textElement.scrollIntoView({block: 'center'});" +

                                            "const rect = textElement.getBoundingClientRect();" +

                                            // Click the circle immediately to the left of the text.
                                            "const x = rect.left - 18;" +
                                            "const y = rect.top + rect.height / 2;" +

                                            "const targetElement = document.elementFromPoint(x, y);" +
                                            "if (!targetElement) return false;" +

                                            // Fire a more realistic mouse sequence.
                                            "targetElement.dispatchEvent(new MouseEvent('mousedown', { bubbles: true, clientX: x, clientY: y }));" +
                                            "targetElement.dispatchEvent(new MouseEvent('mouseup', { bubbles: true, clientX: x, clientY: y }));" +
                                            "targetElement.dispatchEvent(new MouseEvent('click', { bubbles: true, clientX: x, clientY: y }));" +

                                            "return true;",
                                    visibleText
                            );

            return Boolean.TRUE.equals(clicked);
        });
    }

    private void scrollDownToBillingTermsArea() {

        System.out.println("STEP: Scrolling to billing terms area");

        for (int i = 0; i < 6; i++) {

            ((JavascriptExecutor) driver)
                    .executeScript("window.scrollBy(0, 350);");

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            String bodyText = getText(pageBody);

            if (bodyText.contains("Declaro que he leído")
                    || bodyText.contains("Autorizo el tratamiento")
                    || bodyText.contains("Continuar con los métodos de pago")) {
                return;
            }
        }
    }

    private void scrollDownUntilTextExists(String visibleText) {

        wait.until(driver -> {

            Object result =
                    ((JavascriptExecutor) driver)
                            .executeScript(
                                    "const target = arguments[0].toLowerCase();" +

                                            "for (let i = 0; i < 8; i++) {" +
                                            "  const elements = Array.from(document.querySelectorAll('label, div, span, p, button, mat-checkbox'));" +

                                            "  const found = elements.find(el => {" +
                                            "    const text = (el.innerText || el.textContent || '').toLowerCase();" +
                                            "    return text.includes(target);" +
                                            "  });" +

                                            "  if (found) {" +
                                            "    found.scrollIntoView({block: 'center'});" +
                                            "    window.scrollBy(0, 150);" +
                                            "    return true;" +
                                            "  }" +

                                            "  window.scrollBy(0, 350);" +
                                            "}" +

                                            "return false;",
                                    visibleText
                            );

            return Boolean.TRUE.equals(result);
        });
    }

    private void scrollToText(String visibleText) {

        wait.until(driver -> {

            Object result =
                    ((JavascriptExecutor) driver)
                            .executeScript(
                                    "const target = arguments[0].toLowerCase();" +
                                            "const elements = Array.from(document.querySelectorAll('div, span, p, label, button'));" +
                                            "const element = elements.find(el => {" +
                                            "  const text = (el.innerText || el.textContent || '').toLowerCase();" +
                                            "  return text.includes(target);" +
                                            "});" +
                                            "if (!element) return false;" +
                                            "element.scrollIntoView({block: 'center'});" +
                                            "window.scrollBy(0, 250);" +
                                            "return true;",
                                    visibleText
                            );

            return Boolean.TRUE.equals(result);
        });
    }

    private void waitUntilCreditCardFieldsAreVisible() {

        waitForAnyTextInBody(
                "Número de tarjeta",
                "Nombre del titular",
                "MM/AA",
                "CVV",
                "Realizar pedido"
        );
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