import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestClass {
    public static void main (String[] args){

        WebDriver driver =  initChromeDriver();
        driver.get("http://prestashop-automation.qatestlab.com.ua/admin147ajyvk0/");
        WebElement fieldlogin = driver.findElement(By.id("email"));
        fieldlogin.sendKeys("webinar.test@gmail.com");
        WebElement fieldpass = driver.findElement(By.id("passwd"));
        fieldpass.sendKeys("Xcg7299bnSmMuRLp9ITw");
        fieldpass.submit();
        try{
        Thread.sleep(5000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement fieldicon = driver.findElement(By.cssSelector("img[alt]"));
        fieldicon.click();
        try{
            Thread.sleep(2000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement fieldout = driver.findElement(By.id("header_logout"));
        fieldout.click();

/*
        WebDriver driver =  initChromeDriver();
        driver.get("http://prestashop-automation.qatestlab.com.ua/admin147ajyvk0/");
        WebElement fieldlogin = driver.findElement(By.id("email"));
        fieldlogin.sendKeys("webinar.test@gmail.com");
        WebElement fieldpass = driver.findElement(By.id("passwd"));
        fieldpass.sendKeys("Xcg7299bnSmMuRLp9ITw");
        fieldpass.submit();
        try{
            Thread.sleep(5000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement dashboard = driver.findElement(By.className("title"));
        dashboard.click();
        driver.navigate().refresh();
        System.out.println("Section name: " + driver.getTitle());
        try{
            Thread.sleep(3000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement orders = driver.findElement(By.linkText("Заказы"));
        orders.click();
        driver.navigate().refresh();
        System.out.println("Section name: " + driver.getTitle());
        try{
            Thread.sleep(3000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement catalog = driver.findElement(By.linkText("Каталог"));
        catalog.click();
        driver.navigate().refresh();
        System.out.println("Section name: " + driver.getTitle());
        try{
            Thread.sleep(3000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement clients = driver.findElement(By.linkText("Клиенты"));
        clients.click();
        driver.navigate().refresh();
        System.out.println("Section name: " + driver.getTitle());
        try{
            Thread.sleep(3000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement support = driver.findElement(By.linkText("Служба поддержки"));
        support.click();
        driver.navigate().refresh();
        System.out.println("Section name: " + driver.getTitle());
        try{
            Thread.sleep(3000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement statistic = driver.findElement(By.linkText("Статистика"));
        statistic.click();
        driver.navigate().refresh();
        System.out.println("Section name: " + driver.getTitle());
        try{
            Thread.sleep(3000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement modules = driver.findElement(By.linkText("Modules"));
        modules.click();
        driver.navigate().refresh();
        System.out.println("Section name: " + driver.getTitle());
        try{
            Thread.sleep(3000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement design = driver.findElement(By.linkText("Design"));
        design.click();
        driver.navigate().refresh();
        System.out.println("Section name: " + driver.getTitle());
        try{
            Thread.sleep(3000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement pay = driver.findElement(By.linkText("Способ оплаты"));
        pay.click();
        driver.navigate().refresh();
        System.out.println("Section name: " + driver.getTitle());
        try{
            Thread.sleep(3000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement international = driver.findElement(By.linkText("International"));
        international.click();
        driver.navigate().refresh();
        System.out.println("Section name: " + driver.getTitle());
        try{
            Thread.sleep(3000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement shop_parameters = driver.findElement(By.linkText("Shop Parameters"));
        shop_parameters.click();
        driver.navigate().refresh();
        System.out.println("Section name: " + driver.getTitle());
        try{
            Thread.sleep(3000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement configuration = driver.findElement(By.linkText("Конфигурация"));
        configuration.click();
        driver.navigate().refresh();
        System.out.println("Section name: " + driver.getTitle());*/
    }
    public static WebDriver initChromeDriver(){
        System.setProperty("webdriver.chrome.driver",TestClass.class.getResource("chromedriver.exe").getPath());
        return new ChromeDriver();
    }
}

