package com.bookit.pages;

import com.bookit.utilities.BrowserUtilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage{

    @FindBy(name="email")
    private WebElement email;

    @FindBy(name="password")
    private WebElement password;

    public void login(String email,String password){
        BrowserUtilities.waitForPageToLoad(10);
        this.email.sendKeys(email);
        this.password.sendKeys(password, Keys.ENTER);

    }
}