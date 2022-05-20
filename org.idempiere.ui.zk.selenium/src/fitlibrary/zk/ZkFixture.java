/**
 * 
 */
package fitlibrary.zk;

import java.io.File;
import java.util.List;

import org.idempiere.ui.zk.selenium.Widget;
import org.idempiere.ui.zk.selenium.Zk;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;

import fitlibrary.annotation.SimpleAction;
import fitlibrary.spider.AbstractSpiderFixture;
import fitlibrary.spider.Finder;
import fitlibrary.spider.SpiderFixture;
import fitlibrary.spider.polling.PollForWithError;

/**
 * @author hengsin
 * 
 */
public class ZkFixture extends SpiderFixture {

	private Finder _finder;

	/**
	 * 
	 */
	public ZkFixture() {
		super();
		_finder = getFinder();
		setElementFinder(new ZkFinder());
	}

	// --------- CHECKBOX ---------
	@Override
	public boolean checkbox(String locator) {
		locator = locator + " ~ input";
		return super.checkbox(locator);
	}

	@Override
	public boolean withSelect(final String locator, final boolean select) {
		final Widget widget = new Widget(locator);
		final WebElement element = widget.$n(webDriver, "real");
		if (element.isSelected()) {
			if (!select) {
				element.click();
			}
		} else {
			if (select) {
				element.click();
			}
		}

		ensureMatchesNoException(new PollForWithError() {
			@Override
			public boolean matches() {
				//search again to avoid StaleElementReferenceException
				final WebElement e = widget.$n(webDriver, "real");
				return e.isSelected() == select;
			}
			@Override
			public String error() {
				return "Not selected correctly";
			}
		});
		return true;
	}

	// --------- ComboBox ---------
	@SimpleAction(wiki =  "|''<i>combobox</i>''|zk locator|''<i>selected value</i>''|", tooltip = "Return current selected value")
	public String comboboxSelectedValue(String locator) {
		Widget widget = new Widget(locator);
		return (String) widget.eval(webDriver, "getValue()");
	}

	@SimpleAction(wiki = "|''<i>combobox</i>''|xpath, id or other locator|''<i>select item</i>''|label of item|", tooltip = "Changes the selected item in the given comboBox.")
	public boolean comboboxSelectItem(String locator, String label) {
		Widget widget = new Widget(locator);
		widget.execute(webDriver, "open()");
		waitResponse();
		List<WebElement> list = webDriver.findElements(Zk.jq(locator + " @comboitem"));
		if (list != null && list.size() > 0) {
			for(WebElement element : list) {
				widget = new Widget("#"+element.getAttribute("id"));
				String elementLabel = (String) widget.eval(webDriver, "getLabel()");
				if (elementLabel.equals(label)) {
					element.click();
					waitResponse();
					String selected = comboboxSelectedValue(locator);
					return label.equals(selected);
				}
			}						
		}
		return false;		
	}

	@SimpleAction(wiki = "|''<i>combobox</i>''|xpath, id or other locator|''<i>select item at</i>''|index|", tooltip = "Changes the selected item to the nth one, in the given comboBox.")
	public boolean comboboxSelectItemAt(String locator, int index) {
		Widget widget = new Widget(locator);
		widget.execute(webDriver, "open()");
		List<WebElement> list = webDriver.findElements(Zk.jq(locator + " @Comboitem"));
		if (list != null && index < list.size()) {
			WebElement element = list.get(index);
			element.click();
			Widget item = new Widget("#"+element.getAttribute("id"));
			String label = (String) item.eval(webDriver, "getLabel()");
			return label.equals(comboboxSelectedValue(locator));
		}
		return false;
	}
	
	@SimpleAction(wiki= "|''<i>combobox</i>''|zk locator|''<i>set text</i>''|text|", tooltip = "Enter text into combobox and fire onChange event")
	public boolean comboboxSetText(String locator, String text) {
		Widget widget = new Widget(locator);
		widget.execute(webDriver, "setValue('"+text+"', true)");
		widget.execute(webDriver, "fireOnChange()");		
		WebElement element = widget.$n(webDriver, "real");
		element.click();
		waitResponse();
		
		return text.equals(comboboxSelectedValue(locator));		
	}
	
	// ---- Tabbox ----
	@SimpleAction(wiki = "|''<i>tabbox</i>''|xpath, id or other locator|''<i>select tab at</i>''|index|", tooltip = "Changes the selected tab to the nth one, in the given tabbox.")
	public void tabboxSelectTabAt(String locator, int index) {
		Widget widget = new Widget(locator);
		WebElement element = (WebElement) widget.eval(webDriver, "getTabs().getChildAt("+index+").$n('cnt');");
		element.click();
	}
	
	@SimpleAction(wiki = "|''<i>tabbox</i>''|xpath, id or other locator|''<i>select tab</i>''|label|", tooltip = "Changes the selected tab in the given tabbox.")
	public void tabboxSelectTab(String locator, String label) {
		Widget widget = new Widget(locator + " @tab[label=\""+label+"\"]");
		widget.$n(webDriver, "cnt").click();
	}
	
	@SimpleAction(wiki = "|''<i>selected tab</i>''|xpath, id or other locator|", tooltip = "Get the label of the selected tab in the given tabbox.")
	public String selectedTab(String locator) {
		Widget widget = new Widget(locator);
		return (String) widget.eval(webDriver, "getSelectedTab().getLabel()");
	}
	
	//--- Search (lookup) --
	@SimpleAction(wiki = "|''<i>lookup</i>''|xpath, id or other locator|''<i>search</i>''|value|", tooltip = "Search lookup with value.")
	public void lookupSearch(String locator, String value) {
		Widget widget = new Widget(locator + " @textbox");
		WebElement element = widget.findElement(webDriver);
		element.click();
		widget.execute(webDriver, "setValue(\""+value+"\")");
		widget.execute(webDriver, "fireOnChange()");
	}
	
	// ---- window ( tab ) ---
	@SimpleAction(wiki = "|''<i>open window</i>''|menu label|", tooltip = "Open window with label.")
	public void openWindow(String label) {		
		Widget widget = new Widget("$treeSearchCombo");
		String search = label.indexOf("&") > 0 ? label.substring(0, label.indexOf("&")) : label;
		WebElement element = widget.$n(webDriver, "real");
		element.clear();
		element.sendKeys(search);
		waitResponse();
		comboboxSelectItem("$treeSearchCombo", label);		
	}
	
	@SimpleAction(wiki = "|''<i>window</i>''|xpath, id or other locator|''<i>click process button</i>''|button id|", tooltip = "Click a window's process button.")
	public void windowClickProcessButton(String windowLocator, String btnId) {
		click(windowLocator + " $windowToolbar $BtnProcess");		
		waitResponse();
		click("@window[instanceName=\"processButtonPopup\"] $" + btnId);
	}

	@SimpleAction(wiki = "|''<i>window</i>''|xpath, id or other locator|''<i>click toolbar</i>''|value|", tooltip = "Click a window's toolbar button")
	public void windowClickToolbar(String windowLocator, String toolbarButtonId) {
		click(windowLocator + " $windowToolbar $" + toolbarButtonId);
	}

	@SimpleAction(wiki = "|''<i>window</i>''|xpath, id or other locator|''<i>click detail toolbar</i>''|value|", tooltip = "Click the detailpane's toolbar button")
	public void windowClickDetailToolbar(String windowLocator, String toolbarButtonId) {
		click(windowLocator + " $detailPane $" + toolbarButtonId + ":visible");
	}

	@SimpleAction(wiki = "|''<i>window message</i>''|xpath, id or other locator|", tooltip = "Current status message display for a window")
	public String windowMessage(String windowLocator) {
		return webDriver.findElement(Zk.jq(windowLocator +" $messages @label")).getText();
	}

	@SimpleAction(wiki = "|''<i>window</i>''|xpath, id or other locator|''<i>next record</i>''|value|", tooltip = "Navigate to next record.")
	public void windowNextRecord(String windowLocator) {
		click(windowLocator+" $breadCrumb $Next");
	}
	
	@SimpleAction(wiki = "|''<i>window</i>''|xpath, id or other locator|''<i>previous record</i>''|value|", tooltip = "Navigate to previous record.")
	public void windowPreviousRecord(String windowLocator) {
		click(windowLocator+" $breadCrumb $Previous");
	}
	
	// -------- Wait Ajax Response -----
	@SimpleAction(wiki = "|''<i>wait response</i>''|", tooltip = "Wait for ajax response with default timeout value.")
	public void waitResponse() {
		waitResponseWithTimeout(5000);
	}
	
	@SimpleAction(wiki = "|''<i>wait response with timeout</i>''|timeout|", tooltip = "Wait for ajax response with set timeout value.")
	public void waitResponseWithTimeout(int timeout) {
		long s = System.currentTimeMillis();
		int i = 0;
		int ms = 500;
		
		String script = "!!zAu.processing() || !!jq.timers.length";
		while (i < 2) { // make sure the command is triggered.
			while(Boolean.valueOf(getEval(script))) {
				if (System.currentTimeMillis() - s > timeout) {
					break;
				}
				i = 0;//reset
				sleep(ms);
			}
			i++;
			sleep(ms);
		}
	}
	
	@SimpleAction(wiki="|''<i>focus</i>''|xpath, id or other locator|", tooltip= "Set focus to a zk widget")
	public void focus(String locator) {
		Widget widget = new Widget(locator);
		widget.execute(webDriver, "focus_(100)");
	}
	
	@SimpleAction(wiki = "|''<i>with widget</i>''|zk locator|''<i>execute</i>''|command|", tooltip = "Execute zk widget command")
	public void withWidgetExecute(String locator, String command) {
		Widget widget = new Widget(locator);
		widget.execute(webDriver, command);
	}
	
	@SimpleAction(wiki = "|''<i>with widget</i>''|zk locator|''<i>eval</i>''|command|", tooltip = "Execute zk widget command and return the result")
	public Object withWidgetEval(String locator, String command) {
		Widget widget = new Widget(locator);
		return widget.eval(webDriver, command);
	}
	
	@SimpleAction(wiki = "|''<i>context click</i>''|zk locator|", tooltip = "Open context menu")
	public void contextClick(String locator) {
		Widget widget = new Widget(locator);
		WebElement element = widget.findElement(webDriver);
		Actions actions = new Actions(webDriver);
		actions.contextClick(element).build().perform();
	}
	
	@SimpleAction(wiki = "|''<i>file upload</i>''|xpath, id or other locator|''<i>file path</i>''|path of file|", tooltip = "Uploads file from the given path.")
	public boolean fileUploadFilePath(String locator, String filePath) throws Exception{
		if(filePath.equals("")){
			throw new Exception("file path is not specified.");
		}
		File file = new File(filePath);
		String ext = filePath.substring(filePath.trim().lastIndexOf("."), filePath.length()).toLowerCase();
		if(!file.exists() || !(ext.endsWith(".jpg") || ext.endsWith(".bmp") || ext.endsWith(".png") || ext.endsWith(".ico"))){
			throw new Exception("Upload an image of type jpg, bmp, png or ico.");
		}
		WebElement fileInput = webDriver.findElement(By.xpath(locator));
		fileInput.sendKeys(filePath);
		return true;
	}
	
	/**
     * Causes the currently executing thread to sleep for the specified number
     * of milliseconds, subject to the precision and accuracy of system timers
     * and schedulers. The thread does not lose ownership of any monitors.
     * @param millis the length of time to sleep in milliseconds.
     */
	@SimpleAction(wiki = "|''<i>sleep</i>''|millisecond|", tooltip = "sleep")
	public void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
	
	protected String getEval(String script) {
		return String.valueOf(executeJavaScript("return ("+ script+");"));
	}
	
	
	
	@Override
	public WebDriver webDriver() {
		if (webDriver == null) {
			String driver = getDynamicVariable(WEB_DRIVER_VARIABLE_NAME,"htmlunit").toString();
			
			if ("phantomjs".equals(driver)) {
				webDriver = phantomjsDriver();
				return webDriver;
			}
		}
		return super.webDriver();
	}
	
	public void screenShot() {
		if (webDriver instanceof TakesScreenshot)
		{
			String s = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.BASE64);
			show("<img src=\"data:image/png;base64,"+s+"\" />");
		}
		else
		{
			Augmenter augmenter = new Augmenter(); 
			TakesScreenshot ts = (TakesScreenshot) augmenter.augment(webDriver);
			String s = ts.getScreenshotAs(OutputType.BASE64);
			show("<img src=\"data:image/png;base64,"+s+"\" />");
		}
	}

	public void maximizeWindow() {
		webDriver.manage().window().maximize();
	}
	
	private WebDriver phantomjsDriver() {
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setJavascriptEnabled(true);
		caps.setCapability("takesScreenshot", true);
		PhantomJSDriver driver = new PhantomJSDriver(caps);
		//set default resolution
		driver.manage().window().setSize(new Dimension(1366, 768));
		return driver;
	}



	class ZkFinder implements Finder {

		@Override
		public WebElement findElement(String locator) {
			if (locator.startsWith("$") || locator.startsWith("@")) {
				return findElement(Zk.jq(locator));
			}
			return _finder.findElement(locator);
		}

		@Override
		public WebElement findElement(By by) {
			return _finder.findElement(by);
		}

		@Override
		public List<WebElement> findElements(String locator) {
			if (locator.startsWith("$") || locator.startsWith("@")) {
				return webDriver.findElements(Zk.jq(locator));
			}
			return _finder.findElements(locator);
		}

		@Override
		public WebElement findOption(String locator, String option,
				AbstractSpiderFixture abstractSpiderFixture) {
			return _finder.findOption(locator, option, abstractSpiderFixture);
		}

	}
}
