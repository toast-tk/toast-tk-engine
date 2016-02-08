package com.synaptix.toast.adapter.web.component;

import org.junit.Assert;
import org.junit.Test;

import com.synaptix.toast.adapter.web.component.DefaultWebPage;
import com.synaptix.toast.core.adapter.AutoWebType;
import com.synaptix.toast.core.runtime.IWebElementDescriptor.LocationMethod;

public class WebDescriptorTest {
	
	@Test
	public void buildDefaultTestPageMethodTest(){
		DefaultWebPage page = new DefaultWebPage();
		DefaultWebElement descriptor = page.buildWebElementDescriptor("pageName", "input", null, ".data", 0);
		Assert.assertNull(descriptor.getReferenceName());
		Assert.assertEquals(LocationMethod.CSS, descriptor.getMethod());
	}
	
	@Test
	public void buildDefaultTestPageReferenceTest(){
		DefaultWebPage page = new DefaultWebPage();
		DefaultWebElement descriptor = page.buildWebElementDescriptor("pageName", "component:Job", "CSS", ".data", 2);
		Assert.assertEquals("Job", descriptor.getReferenceName());
		Assert.assertEquals(LocationMethod.CSS, descriptor.getMethod());
		Assert.assertEquals(AutoWebType.component, descriptor.getType());
	}

}
