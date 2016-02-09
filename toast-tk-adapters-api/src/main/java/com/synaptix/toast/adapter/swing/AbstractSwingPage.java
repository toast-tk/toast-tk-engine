package com.synaptix.toast.adapter.swing;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import org.apache.commons.beanutils.BeanUtils;

import com.synaptix.toast.adapter.swing.component.DefaultSwingElement;
import com.synaptix.toast.adapter.web.IWebComponentFactory;
import com.synaptix.toast.core.adapter.AutoSwingType;
import com.synaptix.toast.core.driver.IRemoteSwingAgentDriver;
import com.synaptix.toast.core.runtime.IFeedableSwingPage;
import com.synaptix.toast.core.runtime.ISwingAutoElement;
import com.synaptix.toast.core.runtime.ISwingElementDescriptor;

/**
 * Page fixture abstraction, initializes fixture elements's locators based on wiki definitions
 */
public abstract class AbstractSwingPage implements IFeedableSwingPage {

	public String beanClassName; // the bean class name

	Map<String, ISwingElementDescriptor> elements = new HashMap<String, ISwingElementDescriptor>();

	protected Map<String, ISwingAutoElement> autoElements = new HashMap<String, ISwingAutoElement>();
	
	private static ServiceLoader<ISwingComponentFactory> factoryLoader = ServiceLoader.load(ISwingComponentFactory.class);

	private String pageName;

	/**
	 * 
	 * @param elementDefinition
	 */
	@Override
	public void initElement(
		ISwingElementDescriptor e) {
		initElement(e.getName(), e.getType().name(), e.getLocator());
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
		String locator) {
		/**
		 * used to locate an element
		 */
		DefaultSwingElement defaultWebElement = new DefaultSwingElement(name, AutoSwingType.valueOf(type), locator);
		elements.put(name, defaultWebElement);
		try {
			ISwingComponentFactory factory = factoryLoader.iterator().next();
			if(factory == null){
				throw new IllegalAccessError("No Swing Component Factory declared !");
			}
			ISwingElementDescriptor iSwingComponent = elements.get(name);
			if(iSwingComponent != null) {
				ISwingAutoElement execAutoClass = factory.getElement(iSwingComponent);
				// for this abstract page, init fields (for java classes only
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

	private void initBeanFields(
		String name,
		ISwingAutoElement execAutoClass) {
		for(Field f : this.getClass().getFields()) {
			Class<?> automationClass = f.getType();
			if(ISwingAutoElement.class.isAssignableFrom(automationClass)) {
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
	public ISwingElementDescriptor getElement(
		String token) {
		return elements.get(token);
	}

	/**
	 * Convenient method to call an element based on the page enclosed fields' enum
	 */
	public ISwingAutoElement getAutoElement(
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

	public void setPageName(
		String pageName) {
		this.pageName = pageName;
	}

	public List<ISwingElementDescriptor> getLocationElements() {
		return new ArrayList<ISwingElementDescriptor>(elements.values());
	}

	/**
	 * set the driver that will be used by the automation elements
	 */
	@Override
	public void setDriver(
		IRemoteSwingAgentDriver sDvr) {
		for(ISwingAutoElement el : autoElements.values()) {
			el.setFrontEndDriver(sDvr);
		}
	}
}
