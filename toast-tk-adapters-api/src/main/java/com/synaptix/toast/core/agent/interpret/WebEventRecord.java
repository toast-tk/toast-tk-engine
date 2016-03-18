package com.synaptix.toast.core.agent.interpret;


public class WebEventRecord {

	public String value;
	public String componentName;
	public String component;
	public String type;
	public String target;
	public String parent;
	public String id;
	public int keyCode;
	public int charCode;
	public int button;
	public boolean altKey;
	public boolean ctrlKey;
	public boolean shiftKey;
	
	public WebEventRecord(){
		
	}
	
	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}
	
	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public int getButton() {
		return button;
	}

	public void setButton(int button) {
		this.button = button;
	}
	
	public int getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}

	public int getCharCode() {
		return charCode;
	}

	public void setCharCode(int charCode) {
		this.charCode = charCode;
	}

	public boolean isAltKey() {
		return altKey;
	}

	public void setAltKey(boolean altKey) {
		this.altKey = altKey;
	}

	public boolean isCtrlKey() {
		return ctrlKey;
	}

	public void setCtrlKey(boolean ctrlKey) {
		this.ctrlKey = ctrlKey;
	}

	public boolean isShiftKey() {
		return shiftKey;
	}

	public void setShiftKey(boolean shiftKey) {
		this.shiftKey = shiftKey;
	}
	
}
