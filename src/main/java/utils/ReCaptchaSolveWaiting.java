package utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.concurrent.atomic.AtomicBoolean;
@Deprecated
public class ReCaptchaSolveWaiting {

    // Method to wait for a specific response and handle incorrect messages
    public static void waitForReCaptchaResponse(Page page, String targetUrl, int expectedStatus, long timeout) {
        AtomicBoolean responseReceived = new AtomicBoolean(false);
        AtomicBoolean incorrectMessageDisplayed = new AtomicBoolean(false);

        // Listen for network responses
        page.onResponse(response -> {
            if (response.url().contains(targetUrl)) {
                System.out.println("Received response from: " + response.url());
                if (response.status() == expectedStatus) {
                    responseReceived.set(true);  // Mark as received if status matches
                    System.out.println("Status: " + response.status() + " - Desired response received.");
                }
            }
        });

        Locator incorrectMessage = page.locator("#rc-imageselect > div.rc-imageselect-payload > div.rc-imageselect-incorrect-response");
        if (incorrectMessage.isVisible()) {
            incorrectMessageDisplayed.set(true);
            System.out.println("Incorrect response message displayed. Waiting for another response.");
        }

        // Wait for the specific API request with expected status or timeout
        long startTime = System.currentTimeMillis();
        while (!responseReceived.get() && (System.currentTimeMillis() - startTime) < timeout) {
            if (incorrectMessageDisplayed.get()) {
                System.out.println("Detected incorrect message. Waiting before retrying.");
                page.waitForTimeout(4000);  // Wait briefly before checking again
                incorrectMessageDisplayed.set(false);  // Reset flag after handling
            } else {
                page.waitForTimeout(500);  // Short delay to avoid tight loop
            }
        }

        if (!responseReceived.get()) {
            System.out.println("Timeout exceeded without receiving the expected response.");
        } else {
            System.out.println("Continuing with the test as the desired response was received.");
        }
    }
}

