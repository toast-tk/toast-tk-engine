package com.synaptix.toast.adapter.web.component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import com.synaptix.toast.adapter.web.IWebComponentFactory;
import com.synaptix.toast.automation.driver.web.SynchronizedDriver;
import com.synaptix.toast.core.adapter.AutoWebType;
import com.synaptix.toast.core.runtime.IFeedableWebPage;
import com.synaptix.toast.core.runtime.IWebAutoElement;
import com.synaptix.toast.core.runtime.IWebElementDescriptor;
import com.synaptix.toast.core.runtime.IWebElementDescriptor.LocationMethod;

/**
 * 
 * Page fixture abstraction
 * Initializes adapter elements's locators 
 * based on provided markdown definitions
 */
public abstract class AbstractWebPage implements IFeedableWebPage {

	public String beanClassName; // the bean class name

	Map<String, IWebElementDescriptor> elements = new HashMap<String, IWebElementDescriptor>();

	protected Map<String, IWebAutoElement<?>> autoElements = new HashMap<String, IWebAutoElement<?>>();

	private String pageName;
	
	public IWebElementDescriptor descriptor;

	private SynchronizedDriver driver;
	
	private static ServiceLoader<IWebComponentFactory> factoryLoader = ServiceLoader.load(IWebComponentFactory.class);
	
	/**
	 * 
	 * @param elementDefinition
	 */
	@Override
	public void initElement(
		IWebElementDescriptor e) {
		initElement(e.getName(), e.getType().name(), e.getMethod().name(), e.getLocator(), e.getPosition());
	}

	/**
	 * Done for each page element based on a feeder
	 * 
	 * @param name
	 * @param method
	 * @param locator
	 * @param position
	 */
	protected void initElement(
		String name,
		String type,
		String method,
		String locator,
		Integer position) {
		
		DefaultWebElement defaultWebElement = buildWebElementDescriptor(name,type, method, locator, position);
		elements.put(name, defaultWebElement);
		
		try {
			IWebElementDescriptor iWebElement = elements.get(name);
			IWebComponentFactory factory = factoryLoader.iterator().next();
			if(factory == null){
				throw new IllegalAccessError("No Web Element Factory declared !");
			}
			if(iWebElement != null) {
				IWebAutoElement<?> execAutoClass = factory.getElement(iWebElement);
				execAutoClass.setContainer(this);
				initBeanFields(name, execAutoClass);
				autoElements.put(name, execAutoClass);
			}
			else {
				// throw something
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	protected DefaultWebElement buildWebElementDescriptor(String name,
			String type, String method, String locator, Integer position) {
		String kind;
		String referenceName = null;
		
		if(isReferenceComponent(type)){
			kind = StringUtils.split(type, ":")[0];
			referenceName = StringUtils.split(type, ":")[1];
		}else{
			kind = type;
		}
		
		LocationMethod method2 = method == null ? LocationMethod.CSS : LocationMethod.valueOf(method);
		AutoWebType webType = AutoWebType.valueOf(kind);
		DefaultWebElement defaultWebElement = new DefaultWebElement(name, webType, locator,
			method2, position, referenceName);
		return defaultWebElement;
	}

	private boolean isReferenceComponent(String type) {
		return type.contains(":");
	}

	private void initBeanFields(
		String name,
		IWebAutoElement<?> execAutoClass) {
		for(Field f : this.getClass().getFields()) {
			Class<?> automationClass = f.getType();
			if(IWebAutoElement.class.isAssignableFrom(automationClass)) {
				if(f.getName().equals(name)) {
					try {
						BeanUtils.setProperty(this, name, execAutoClass);
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Convenient method to call an element based on the page enclosed fields' enum
	 */
	public IWebElementDescriptor getElement(String token) {
		return elements.get(token);
	}

	/**
	 * Convenient method to call an element based on the page enclosed fields' enum
	 */
	public IWebAutoElement<?> getAutoElement(
		String token) {
		return autoElements.get(token);
	}

	public String getBeanClassName() {
		return beanClassName;
	}

	public void setBeanClassName(
		String beanClassName) {
		this.beanClassName = beanClassName;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	@Override
	public List<IWebElementDescriptor> getChildren() {
		return new ArrayList<IWebElementDescriptor>(elements.values());
	}

	@Override
	public void setDescriptor(IWebElementDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	@Override
	public IWebElementDescriptor getDescriptor() {
		return this.descriptor;
	}
	
	@Override
	public void setContainer(IFeedableWebPage webPage){
		//NO OP
	}

	@Override
	public IFeedableWebPage getContainer(){
		return this;
	}

	@Override
	public void setDriver(SynchronizedDriver sDvr) {
		this.driver = sDvr;
		for(IWebAutoElement<?> el : autoElements.values()) {
			el.setDriver(sDvr);
		}		
	}
	
	@Override
	public Object getWebElement() {
		return driver.find(descriptor);
	}
	
	@Override
	public List getAllWebElements() {
		return driver.findAll(descriptor);
	}
}
