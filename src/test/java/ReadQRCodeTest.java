/*
 * Copyright 2018 Elias Nogueira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ReadQRCodeTest {


    private AppiumDriver<MobileElement> driver;
    private AppiumDriverLocalService service;

    @BeforeMethod
    public void setup() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.APP, new File("app/qrcode-app.apk").getAbsolutePath());
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");

        AppiumServiceBuilder builder  = new AppiumServiceBuilder().usingAnyFreePort();
        service =  AppiumDriverLocalService.buildService(builder);
        driver = new AndroidDriver<MobileElement>(builder, capabilities);

        service.start();
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
        service.stop();
    }

    /**
     * This test capture the screenshot and get the element that contains the QRCode
     * Based on the element points (width and height) the image os cropped
     * With cropped image we can decode the QRCode with zxing
     */
    @Test
    public void readQRCode() throws IOException, NotFoundException {
        MobileElement qrCodeElement = driver.findElement(By.id("com.eliasnogueira.qr_code:id/qrcode"));
        File screenshot = driver.getScreenshotAs(OutputType.FILE);

        String content = decodeQRCode(generateImage(qrCodeElement, screenshot));
        System.out.println("content = " + content);

    }

    /**
     * Return a cropped image based on an element (in this case the qrcode image) from the entire device screenshot
     * @param element elemement that will show in the screenshot
     * @param screenshot the entire device screenshot
     * @return a new image in BufferedImage object
     * @throws IOException if any problem in generate image occurs
     */
    private BufferedImage generateImage( MobileElement element, File screenshot) throws IOException {
        BufferedImage fullImage = ImageIO.read(screenshot);
        Point imageLocation = element.getLocation();

        int qrCodeImageWidth = element.getSize().getWidth();
        int qrCodeImageHeight = element.getSize().getHeight();

        int pointXPosition = imageLocation.getX();
        int pointYPosition = imageLocation.getY();

        BufferedImage qrCodeImage = fullImage.getSubimage(pointXPosition, pointYPosition, qrCodeImageWidth, qrCodeImageHeight);
        ImageIO.write(qrCodeImage, "png", screenshot);

        return qrCodeImage;
    }

    /**
     * Decode a QR Code image using zxing
     * @param qrCodeImage the qrcode image cropped from entire device screenshot
     * @return the content
     * @throws NotFoundException if the image was not found
     */
    private static String decodeQRCode(BufferedImage qrCodeImage) throws NotFoundException {
        LuminanceSource source = new BufferedImageLuminanceSource(qrCodeImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Result result = new MultiFormatReader().decode(bitmap);
        return result.getText();
    }

}
