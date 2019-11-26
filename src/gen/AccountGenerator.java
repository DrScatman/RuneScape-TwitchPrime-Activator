package gen;

import com.twocaptcha.api.ProxyType;
import com.twocaptcha.api.TwoCaptchaService;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;


public class AccountGenerator {

    private static final String logDirectory = System.getProperty("user.home") + "\\Desktop\\primedAccounts.txt";
    private static final String CREATION_URL = "https://secure.runescape.com/m=account-creation/g=oldscape/create_account";
    private static final String GECKO_EXE = System.getProperty("user.home") + "\\IdeaProjects\\RuneScape-Account-Generator-2.0\\geckodriver.exe";
    private static final String TWITCH_URL = "https://www.twitch.tv/";
    private String proxyIp;
    private String proxyPort;
    private String proxyUser;
    private String proxyPass;
    private static final String API_KEY = "4935affd16c15fb4100e8813cdccfab6";
    private static final String GOOGLE_KEY = "6Lcsv3oUAAAAAGFhlKrkRb029OHio098bbeyi_Hv";
    private static final String PAGE_URL = CREATION_URL;
    private FirefoxDriver driver;
    private BrowserUtilities utilities;
    private int IMPLICIT_WAIT_SECONDS = 60;
    private StringBuffer verificationErrors = new StringBuffer();
    private String currentCreationUrl;
    private static final String[] EMAIL_DOMAINS = new String[]{"grr.la", "pokemail.net", "spam4.me"};
    private static String errorMessage = "A problem was encountered. Details are in the log.";
    private final String EMAIL_WEBSITE = build(104, 116, 116, 112, 115, 58, 47, 47, 119, 119, 119, 46, 103, 117, 101,
            114, 114, 105, 108, 108, 97, 109, 97, 105, 108, 46, 99, 111, 109, 47, 105, 110, 98, 111, 120, 47);
    boolean redoLastAccount = false;
    //private Logger log;

    public static void main(String[] args) throws Exception {
        new AccountGenerator().performTwitchTask("Tripler8347", "Q748Fsug0", "m.illeja.115+.QK4K.9.TFQ9M.414VX@gmail.com", "qazzaq");

        /*if (args.length == 5) {
            new AccountGenerator().performTask(args[0], args[1],
                    Integer.parseInt(args[2]), Integer.parseInt(args[3]), args[4]);
        } else if (args.length == 9) {
            new AccountGenerator().performTask(args[0], args[1], Integer.parseInt(args[2]),
                    Integer.parseInt(args[3]), args[4], args[5], args[6], args[7], args[8]);
        } else {
            System.out.println("NO ARGS: Writing To Error File");
            new AccountGenerator().performTask(BrowserUtilities.getRandomString(), BrowserUtilities.getRandomString(),
                    0, 0, "C:\\Users\\bllit\\OneDrive\\Desktop\\RSPeerErrors.txt");
        }*/
    }

    private void performTwitchTask(String twitchUser, String twitchPassword, String osrsEmail, String osrsPassword) throws Exception {
        try {
            setUp();

            int accountsManaged = 0;

            logIntoTwitch(TWITCH_URL, twitchUser, twitchPassword);

            claimTwitchAward();

            logIntoRunescape(osrsEmail, osrsPassword, true);

            writeAccount(new String[]{osrsEmail, osrsPassword});

            tearDown();
        } catch (Exception e) {
            e.printStackTrace();
            driver.quit();
            JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
            errorMessage = "A problem was encountered. Details are in the log.";
        }
    }

    private void claimTwitchAward() throws InterruptedException {
        utilities.checkForElement(By.cssSelector("[data-a-target=\"top-nav-avatar\"]"),
                30, IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS);
        WebElement splashScreen = utilities.checkForElement(By.cssSelector("[data-test-selector=\"onboarding-modal-splash-screen__container\"]"),
                10, IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS);
        WebElement splashScreen2 = utilities.checkForElement(By.cssSelector("[data-test-selector=\"onboarding-modal-header__container\"]"),
                10, IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS);

        while ((splashScreen != null)
                || (splashScreen2 != null)) {
            Thread.sleep(5000);
            System.out.println("Awaiting manual splash-screen completion");
            splashScreen = utilities.checkForElement(By.cssSelector("[data-test-selector=\"onboarding-modal-splash-screen__container\"]"), 0,
                    IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS);
            splashScreen2 = utilities.checkForElement(By.cssSelector("[data-test-selector=\"onboarding-modal-header__container\"]"), 0,
                    IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS);
        }

        WebElement offers = utilities.checkForElement(By.cssSelector("[data-target=\"prime-offers-icon\"]"),
                30, IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS);

        if (offers != null) {
            offers.click();
            System.out.println("Clicked offers");
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Check for jQuery on the page, add it if need be
        js.executeScript("if (!window.jQuery) {"
                + "var jquery = document.createElement('script'); jquery.type = 'text/javascript';"
                + "jquery.src = 'https://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js';"
                + "document.getElementsByTagName('head')[0].appendChild(jquery);" + "}");
        Thread.sleep(5000);

        // Use jQuery to add jquery-growl to the page
        js.executeScript("$.getScript('https://the-internet.herokuapp.com/js/vendor/jquery.growl.js')");

        //js.executeScript("$.getScript('/Users/NaveenKhunteta/Documents/workspace/Test/src/testcases/jquery.growl.js')");

        // Use jQuery to add jquery-growl styles to the page
        js.executeScript("$('head').append('<link rel=\"stylesheet\" "
                + "href=\"https://the-internet.herokuapp.com/css/jquery.growl.css\" " + "type=\"text/css\" />');");
        Thread.sleep(5000);

        // jquery-growl w/ no frills
        js.executeScript("$.growl({ title: 'GET', message: '/' });");

        // jquery-growl w/ colorized output
        js.executeScript("$.growl.error({ title: 'ERROR', message: 'Some exception is coming' });");
        js.executeScript("$.growl.notice({ title: 'Notice', message: 'your notice message goes here' });");
        js.executeScript("$.growl.warning({ title: 'Warning!', message: 'your warning message goes here' });");

        js.executeScript("$(\":contains('RuneScape & Old School')\").parent(\".prime-offer-notification\").find(\".prime-claim-button\").find(\"button\").click();");

        WebElement here = utilities.checkForElement(By.cssSelector("[href=\"https://www.runescape.com/account/linked-accounts/twitch/redeem\"]"),
                20, IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS);
        try {
            here.click();
        } catch (Exception e) {
            utilities.openNewWindow("https://www.runescape.com/account/linked-accounts/twitch/redeem");
        }

            /*Thread.sleep(5000);
            js.executeScript("$(\":contains('Authorize')\").parent(\".authorize_actions\").find(\".js-authorize\").find(\"button\").submit();");
            System.out.println("Clicking Authorize");*/

        /*class="button button--large js-authorize"*/


        WebElement auth = utilities.checkForElement(By.cssSelector(".button .button--large .js-authorize"),
                20, IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS);
        if (auth != null) {
            auth.click();
            System.out.println("Clicked authorize");
        } else {
            System.out.println("Cant find authorize button");
        }

        WebElement redirect = utilities.checkForElement(By.cssSelector("[onclick=\"redirectPage()\"]"),
                15, IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS);
        if (redirect != null) {
            redirect.click();
        }

        WebElement yesLogin = utilities.checkForElement(By.cssSelector("[data-test=\"yes-log-in\"]"),
                10, IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS);

        String osrsLoginLink = null;

        if (yesLogin != null) {
            osrsLoginLink = yesLogin.getAttribute("href");
        }

        if (osrsLoginLink != null && !osrsLoginLink.isEmpty()) {
            do {
                driver.get(osrsLoginLink);
            } while (driver.getPageSource().contains("could not be loaded"));
        } else {
            WebElement linkButton = utilities.checkForElement(By.cssSelector("[class=\"a-social-button a-social-button--service-twitch\"]"),
                    15, IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS);
            if (linkButton != null) {
                utilities.openNewWindow(linkButton.getAttribute("href"));

                WebElement authOther = utilities.checkForElement(By.cssSelector("[class=\"button button--large js-authorize\"]"),
                        20, IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS);


                if (authOther != null) {
                    //js.executeScript("document.getElementsByClassName(\"button button--large js-authorize\")[0].click();");
                    authOther.click();
                    System.out.println("Clicked authorize2");

                    WebElement redirect2 = utilities.checkForElement(By.cssSelector("[onclick=\"redirectPage()\"]"),
                            10, IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS);
                    if (redirect != null) {
                        redirect2.click();
                    }
                } else {
                    System.out.println("Cant find authorize2 button");
                }
            }
        }

        yesLogin = utilities.checkForElement(By.cssSelector("[data-test=\"yes-log-in\"]"),
                15, IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS);

        if (yesLogin != null) {
            osrsLoginLink = yesLogin.getAttribute("href");
        }

        if (osrsLoginLink != null && !osrsLoginLink.isEmpty()) {
            do {
                driver.get(osrsLoginLink);
            } while (driver.getPageSource().contains("could not be loaded"));
        }
    }

    private boolean logIntoTwitch(String twitchUrl, String twitchUser, String twitchPassword) throws InterruptedException {
        do {
            driver.get(twitchUrl);
        } while (driver.getPageSource().contains("could not be loaded"));

        WebElement login = null;
        while (login == null || !login.isDisplayed()) {
            login = driver.findElement(By.cssSelector("[data-test-selector=\"anon-user-menu__login-button\"]"));
        }
        login.click();

        driver.findElement(By.id("login-username")).clear();
        driver.findElement(By.id("login-username")).sendKeys(twitchUser);
        driver.findElement(By.id("password-input")).clear();
        driver.findElement(By.id("password-input")).sendKeys(twitchPassword);

        WebElement login2 = utilities.checkForElement(By.cssSelector("[data-a-target=\"passport-login-button\"]"),
                0, IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS);
        if (login2 != null && login2.isDisplayed()) {
            login2.click();

            WebElement avatar = utilities.checkForElement(By.cssSelector("[data-a-target=\"top-nav-avatar\"]"),
                    0, IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS);
        } else {
            System.out.println("Cant find login2 button");
            return false;
        }
        return true;
    }

    /**
     * Creates and/or registers RuneScape accounts
     *
     * @param baseEmailName  - the email name without numbers attached to the end (excluding domain)
     * @param password       - the password of the created RuneScape accounts
     * @param firstExtension - the first number extension to attach to the end of baseEmailName (-1 for no extension)
     * @param lastExtension  - the last number extension to attach to the end of baseEmailName
     * @throws IllegalArgumentException when both <code>create</code> and <code>register</code> are false
     */
    private void performTask(String baseEmailName, String password,
                             int firstExtension, int lastExtension, String logDirectory) throws Exception {

        //log = new Logger(logDirectory);
        String emailDomain = EMAIL_DOMAINS[randInt(0, EMAIL_DOMAINS.length - 1)]; //build(103, 114, 114, 46, 108, 97);
        String baseEmail = baseEmailName + "@" + emailDomain;

        long timeStarted = -1;

        try {
            setUp();

            currentCreationUrl = CREATION_URL;
            int accountsManaged = 0;
            int accountsToMake = lastExtension - firstExtension + 1;

            String email = "";
            String previousEmail = "";

            for (; accountsManaged < accountsToMake; accountsManaged++) {
                previousEmail = email;

                String extension = (firstExtension + accountsManaged) == -1 ? "" : (accountsManaged + firstExtension)
                        + "";
                email = baseEmailName + extension + "@" + emailDomain;


                createEmailInbox(baseEmailName + extension, emailDomain);

                while (!createAccount(currentCreationUrl, email, password, true)) {
                    currentCreationUrl = getNewAccountCreationUrl(currentCreationUrl);

                    if (redoLastAccount) {
                        createAccount(currentCreationUrl, previousEmail, password, false);
                    }
                }

                String viaProxy = currentCreationUrl.equals(CREATION_URL) ? "" : " (via proxy server)";

                if (!utilities.waitUntilUrlContains(5, "account_created", "error").contains("error")) {
                    writeAccount(new String[]{email, password});
                }
                receiveRegistrationEmail();
                //registerEmail(baseEmailName + extension, emailDomain, password, create);
            }

            redoLastAccount = false;

            /*long timeStopped = System.nanoTime();
            String millisElapsed = toDurationString((timeStopped - timeStarted) / 1000000);
            //log.println();

            String mainTask = create ? " created" : " registered";
            String registeredString = create && register ? "and registered " : "";*/

            /*switch (accountsManaged) {
                case 0:
                    log.println("No accounts were" + mainTask + ".");
                    break;
                case 1:
                    log.println("1 account was" + mainTask + " " + registeredString + "in" + millisElapsed + ".");
                    break;
                default:
                    log.println(accountsManaged + " accounts were" + mainTask + " " + registeredString + "in"
                            + millisElapsed + ".");
                    break;
            }*/

            tearDown();
        } catch (Exception e) {
            /*String logMessage = e.toString() + ": " + e.getMessage();
            if (e.toString().toLowerCase().contains("sessionnotfound")) {
                logMessage = errorMessage;
            }*/
            /*log.println();
            log.println();
            log.println("ERROR encountered at" + toDurationString((System.nanoTime() - timeStarted) / 1000000) + ":");
            log.println(logMessage);*/
            driver.quit();
            JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
            errorMessage = "A problem was encountered. Details are in the log.";
        }
    }

    private void performTask(
            String baseEmailName, String password, int firstExtension, int lastExtension, String logDirectory,
            String proxyIp, String proxyPort, String proxyUser, String proxyPass) throws Exception {

        this.proxyIp = proxyIp;
        this.proxyPort = proxyPort;
        this.proxyUser = proxyUser;
        this.proxyPass = proxyPass;
        performTask(baseEmailName, password, firstExtension, lastExtension, logDirectory);
    }

    private boolean createAccount(String url, String email, String password, boolean quitOnTakenEmail) {
        do {
            driver.get(url);
        } while (driver.getPageSource().contains("could not be loaded"));

        String source = driver.getPageSource().toLowerCase();

        if (source.contains("unable to create") || source.contains("error=1")) {
            if (source.contains("error=1")) {
                redoLastAccount = true;
            }
            return false;
        }

        int day = randInt(1, 28);
        int month = randInt(1, 12);
        int year = randInt(1995, 2005);
        driver.findElement(By.id("create-email")).clear();
        driver.findElement(By.id("create-email")).sendKeys(email);
        driver.findElement(By.id("create-password")).clear();
        driver.findElement(By.id("create-password")).sendKeys(password);
        driver.findElement(By.name("day")).clear();
        driver.findElement(By.name("day")).sendKeys("" + (day < 10 ? "0" + day : day));
        driver.findElement(By.name("month")).clear();
        driver.findElement(By.name("month")).sendKeys("" + (month < 10 ? "0" + month : month));
        driver.findElement(By.name("year")).clear();
        driver.findElement(By.name("year")).sendKeys("" + year);
        WebElement gotIt = utilities.checkForElement(By.className("c-cookie-consent__options"), 0, IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS);
        if (gotIt != null && gotIt.isDisplayed()) {
            gotIt.click();
        }
        driver.findElement(By.id("create-submit")).click();

        String urlStateChange = utilities.waitUntilUrlContains(2, "account_created", "error");
        if (urlStateChange == null) {
            String captchaKey = solveCaptcha(3);
            ((JavascriptExecutor) driver).executeScript("document.getElementById(\"g-recaptcha-response\").innerHTML=\"" + captchaKey + "\";");
            ((JavascriptExecutor) driver).executeScript("onSubmit();");
        }

        WebElement error = utilities.checkForElement(By.className("error"), 0, IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS);

        if (error != null && quitOnTakenEmail) {
            errorMessage = "Account creation error: " + error;
            driver.quit();
        }

        if (url.toLowerCase().contains("hide")) {
            utilities.waitUntilUrlDoesntContain("=norefer");

            if (driver.getPageSource().contains("error=1")) {
                return false;
            } else if (driver.getPageSource().contains("could not be loaded")) {
                return false;
            }
        } else {
            if (utilities.waitUntilUrlContains("account_created", "error").contains("error")) {
                return false;
            }
        }

        return true;
    }

    private void writeAccount(String[] info) throws IOException {
        try (FileWriter fw = new FileWriter(logDirectory, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(info[0] + ":" + info[1]);

        } catch (IOException e) {
            throw new IOException("Can't Find/Read/Write to File");
        }
    }

    private String getNewAccountCreationUrl(String currentUrl) {
		/*String proxyUrl = "https://1.hidemyass.com/ip-1/encoded/czovL3NlY3VyZS5ydW5lc2NhcGUuY29tL209YWNjb3VudC1jcmVhdGlvbi9jcmVhdGVfYWNjb3VudA%3D%3D&f=norefer";
		//"https://proxy-sea.hidemyass-freeproxy.com/proxy/en-us/aHR0cHM6Ly9zZWN1cmUucnVuZXNjYXBlLmNvbS9tPWFjY291bnQtY3JlYXRpb24vZz1vbGRzY2FwZS9jcmVhdGVfYWNjb3VudA";
		boolean containsHide = currentUrl.toLowerCase().contains("hide");
		boolean containsRunescape = currentUrl.toLowerCase().contains("runescape");

		if (!(containsHide || containsRunescape)) {
			throw new IllegalArgumentException("Invalid URL for RuneScape account creation");
		}

		if (containsHide) {
			if (currentUrl.contains("ip-8")) {
				return CREATION_URL;
			} else {
				int ipIndex = currentUrl.indexOf("ip-") + 3;
				int newIp = Integer.parseInt(currentUrl.charAt(ipIndex) + "") + 1;
				return currentUrl.substring(0, ipIndex) + newIp + currentUrl.substring(ipIndex + 1);
			}
		} else {
			return proxyUrl;
		}*/
        return currentUrl;
    }

    private String getJobType(boolean create, boolean register) {
        if (create) {
            if (register) {
                return "create and register";
            } else {
                return "create";
            }
        } else {
            return "register";
        }
    }

    private String solveCaptcha(int retries) {
        System.out.println("Solving Captcha");

        /**
         * With proxy and user authentication
         */
        TwoCaptchaService service;
        if (proxyIp != null && !proxyIp.isEmpty()) {
            service = new TwoCaptchaService(API_KEY, GOOGLE_KEY, PAGE_URL, proxyIp, proxyPort, proxyUser, proxyPass, ProxyType.SOCKS5);
        } else {
            service = new TwoCaptchaService(API_KEY, GOOGLE_KEY, PAGE_URL);
        }

        try {
            String responseToken = service.solveCaptcha();
            System.out.println("The response token is: " + responseToken);
            return responseToken;
        } catch (InterruptedException e) {
            System.out.println("ERROR case 1");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("ERROR case 2");
            e.printStackTrace();
        }
        if (retries > 0) {
            solveCaptcha(retries - 1);
        }
        return null;
    }

    /**
     * Registers an RuneScape account via email confirmation.
     *
     * @param emailName
     *            - the RuneScape account's email address (excluding domain)
     * @param emailDomain
     *            - the domain for the above email name
     * @param password
     *            - the password of the RuneScape account
     * @param createOnFail
     *            - true if the specified account should be created in the case that it does not exist; else, false
     */
	/*private void registerEmail(String emailName, String emailDomain, String password, boolean createOnFail) {
		String loginEmail = emailName + "@" + emailDomain;
		String newEmailName = BrowserUtilities.getRandomString();
		String newEmailDomain = build(103, 114, 114, 46, 108, 97);
		String newEmail = newEmailName + "@" + newEmailDomain;

		createEmailInbox(newEmailName, newEmailDomain);

		*//*logIntoRunescape(loginEmail, password, createOnFail);

		String url = driver.getCurrentUrl();
		String sessionCode = url.substring(url.indexOf("c=") + 2, url.indexOf("/account"));

		changeRunescapeEmail(newEmail, sessionCode);*//*

		receiveRegistrationEmail();
	}*/

    /**
     * Creates an email inbox on a disposable email website.
     *
     * @param emailName   - the name of the inbox's email address (excluding domain)
     * @param emailDomain - the domain for the above email name
     */
    private void createEmailInbox(String emailName, String emailDomain) {
        if (driver.getWindowHandles().size() == 1) {
            utilities.openNewWindow(EMAIL_WEBSITE);
        } else {
            utilities.switchWindow();
            driver.get(EMAIL_WEBSITE);
        }

        new Select(driver.findElement(By.id("gm-host-select"))).selectByVisibleText(emailDomain);
        driver.findElement(By.id("inbox-id")).click();
        driver.findElement(By.cssSelector("#inbox-id input[type=\"text\"]")).click();
        driver.findElement(By.cssSelector("#inbox-id input[type=\"text\"]")).clear();
        driver.findElement(By.cssSelector("#inbox-id input[type=\"text\"]")).sendKeys(emailName);
        driver.findElement(By.xpath("//span[@id='inbox-id']/button")).click();

        utilities.switchWindow();
    }

    /**
     * Logs into the RuneScape website using an account's credentials.
     *
     * @param email        - the full email address of the RuneScape account
     * @param password     - the password of the RuneScape account
     * @param createOnFail - true if the specified account should be created in the case that it does not exist; else, false
     */
    private void logIntoRunescape(String email, String password, boolean createOnFail) throws InterruptedException {
        //driver.get("https://www.runescape.com/account_settings.ws");

        driver.findElement(By.id("login-username")).clear();
        driver.findElement(By.id("login-username")).sendKeys(email);
        driver.findElement(By.id("login-password")).clear();
        driver.findElement(By.id("login-password")).sendKeys(password);
        driver.findElement(By.id("du-login-submit")).click();

        String urlStateChange = utilities.waitUntilUrlContains(3, "c=");
        if (urlStateChange == null) {
            String captchaKey = solveCaptcha(3);
            ((JavascriptExecutor) driver).executeScript("document.getElementById(\"g-recaptcha-response\").innerHTML=\"" + captchaKey + "\";");
            ((JavascriptExecutor) driver).executeScript("onSubmit();");
        }

        WebElement error = utilities.checkForElement(By.className("errorMessage"), 0, IMPLICIT_WAIT_SECONDS,
                TimeUnit.SECONDS);

        if (error != null) {
            if (createOnFail) {
                createAccount(getNewAccountCreationUrl(currentCreationUrl), email, password, true);
                logIntoRunescape(email, password, false);
            } else {
                errorMessage = "Login error: " + error.getText();
                System.out.println("Login error: " + error.getText());
                driver.quit();
            }
        }

        utilities.waitUntilUrlContains("c=");
        Thread.sleep(85000);
    }

    /**
     * Changes a RuneScape account's default email address. Note that this does not change the login email address.
     *
     * @param newEmail    - the new, full email address of the RuneScape account
     * @param sessionCode - a code corresponding to the login session
     */
    /*private void changeRunescapeEmail(String newEmail, String sessionCode) {
        driver.get("https://secure.runescape.com/m=email-register/c=" + sessionCode
                + "/cancel_action.ws?type=address&IFrame=1");

        utilities.waitUntilUrlContains("mod");

        utilities.sendKeys(Keys.END);

        By boxClosed = By.xpath("//div[@id='email' and @class='RaggedBox TwoThirds RaggedBoxToggly RaggedBoxTogglyJs RaggedBoxClosed']");

        By raggedLocator = By.cssSelector("#email > div.RaggedBoxHeader > h3.RaggedBoxTitle.HoverText");

        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(raggedLocator));

        driver.findElement(raggedLocator).click();

        utilities.sendKeys(Keys.END);

        driver.switchTo().frame("email_registration");

        By field1Locator = By.id("na");
        By field2Locator = By.id("na2");
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(field1Locator));
        driver.findElement(field1Locator).clear();
        driver.findElement(field1Locator).sendKeys(newEmail);
        driver.findElement(field2Locator).clear();
        driver.findElement(field2Locator).sendKeys(newEmail);
        driver.findElementByCssSelector("#agree_privacy > span").click();
        driver.findElementByName("submit").click();

        if (utilities.checkForElement(boxClosed, 0, IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS) != null) {
            driver.findElement(raggedLocator).click();
            driver.findElementByName("submit").click();
        }

        driver.switchTo().defaultContent();
    }*/

    /**
     * Receives an email from RuneScape.com and uses the provided link to register the RuneScape account.
     */
    private void receiveRegistrationEmail() {
        utilities.switchWindow();

        By emailLocator = By.cssSelector("td.td3");
        try {
            new WebDriverWait(driver, 60).until(ExpectedConditions.textToBePresentInElementLocated(emailLocator,
                    "Thank you for registering"));
            driver.findElement(By.cssSelector("td.td3")).click();

            By linkLocator = By.cssSelector("a[href*='http://echo7.bluehornet.com']");
            new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(linkLocator));
            String validLink = "";
            List<WebElement> links = driver.findElements(linkLocator);
            for (WebElement link : links) {
                if (link.getText().contains("submit_code")) {
                    validLink = link.getText();
                    break;
                }
            }
            driver.get(validLink);
            utilities.waitUntilUrlContains("submit_code");
        } catch (TimeoutException e) {
            System.out.println("Registration email wait timed out".toUpperCase());
        }
    }

    /**
     * Sets up the FirefoxDriver and other essential objects.
     *
     * @throws Exception
     */
    private void setUp() throws Exception {
        System.setProperty("webdriver.load.strategy", "unstable");
        System.setProperty("webdriver.gecko.driver", GECKO_EXE);

        FirefoxProfile fp = new FirefoxProfile();
        FirefoxOptions fo = new FirefoxOptions();
        fp.setPreference("browser.privatebrowsing.autostart", true);
        fo.addPreference("browser.privatebrowsing.autostart", true);
        fp.setPreference("webdriver.load.strategy", "unstable");
        fo.addPreference("webdriver.load.strategy", "unstable");
        fo.setPageLoadStrategy(PageLoadStrategy.EAGER);
        fo.setHeadless(false);
        if (proxyIp != null && !proxyIp.isEmpty()) {
            Proxy proxy = new Proxy();
            proxy.setProxyType(Proxy.ProxyType.MANUAL);
            String pAddress = proxyIp + ":" + proxyPort;
            proxy.setHttpProxy(pAddress);
            proxy.setFtpProxy(pAddress);
            proxy.setSslProxy(pAddress);
            proxy.setSocksVersion(5);
            proxy.setSocksProxy(pAddress);
            proxy.setSocksUsername(proxyUser);
            proxy.setSocksPassword(proxyPass);
            /*fp.setPreference("network.proxy.socks", proxyIp);
            fp.setPreference("network.proxy.socks_port", proxyPort);*/
            fo.setProxy(proxy);
        }
        fo.setProfile(fp);

        driver = new FirefoxDriver(fo);
        utilities = new BrowserUtilities(driver);
        driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT_SECONDS, TimeUnit.SECONDS);


    }

    /**
     * Quits the driver.
     *
     * @throws Exception
     */
    private void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private static String toDurationString(long milliseconds) {
        return String
                .format(" %01d hours, %01d minutes, and %01d seconds",
                        TimeUnit.MILLISECONDS.toHours(milliseconds),
                        TimeUnit.MILLISECONDS.toMinutes(milliseconds)
                                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)),
                        TimeUnit.MILLISECONDS.toSeconds(milliseconds)
                                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)))
                .replace(" 1 hours", " 1 hour").replace(" 1 minutes", " 1 minute").replace(" 1 seconds", " 1 second");
    }

    private static String generateAge() {
        return Integer.toString(16 + (int) (Math.random() * ((24 - 16) + 1)));
    }

    private static String build(int... numbers) {
        String string = "";

        for (int number : numbers) {
            string += "" + (char) number;
        }

        return string;
    }

    /*private void logIntroSummary(String jobType, String baseEmail, String password, int firstExtension,
                                 int lastExtension) {
        log.println("RuneScape Account Generator log for " + Calendar.getInstance().getTime());
        log.println();

        int accountsToMake = lastExtension - firstExtension + 1;
        String accountPlural = accountsToMake == 1 ? "account" : "accounts";
        String emailBaseName = "\"" + baseEmail + "\"";
        String passwordString = "\"" + password + "\"";
        String emailPasswordString = " with the email base name " + emailBaseName + ", password " + passwordString
                + ", ";

        if (accountsToMake == 1 && firstExtension == -1) {
            log.println("Attempting to " + jobType + " 1 account" + emailPasswordString + "and no number extensions.");
        } else {
            log.println("Attempting to " + jobType + " " + accountsToMake + " " + accountPlural + emailPasswordString
                    + "and extensions " + (firstExtension == -1 ? "NONE" : firstExtension) + " to " + lastExtension
                    + ".");
        }

        log.println();
    }*/

    public static int randInt(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        java.util.Random rand = new java.util.Random();
        return rand.nextInt(max - min + 1) + min;
    }
}