package io.toast.tk.core.agent.interpret;

public class WebEventRecord {

	private String value;
	private String componentName;
	private String component;
	private String eventType;
	private String target;
	private String parent;
	private String id;
	private int keyCode;
	private int charCode;
	private int button;
	private boolean altKey;
	private boolean ctrlKey;
	private boolean shiftKey;

	public WebEventRecord() {
		
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
	
	public String getEventType() {
		return eventType;
	}

	public void setEventType(final String type) {
		this.eventType = type;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(final String target) {
		this.target = target;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}
	
	public int getButton() {
		return button;
	}

	public void setButton(final int button) {
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

	public void setAltKey(final boolean altKey) {
		this.altKey = altKey;
	}

	public boolean isCtrlKey() {
		return ctrlKey;
	}

	public void setCtrlKey(final boolean ctrlKey) {
		this.ctrlKey = ctrlKey;
	}

	public boolean isShiftKey() {
		return shiftKey;
	}

	public void setShiftKey(final boolean shiftKey) {
		this.shiftKey = shiftKey;
	}
}
