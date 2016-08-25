package io.toast.tk.core.agent.interpret;

public class WebEventRecord {

	private String value;
	private String componentName;
	private String component;
	private String eventType;
	private String target;
	private String parent;
	private String id;
	private int offsetX;
	private int offsetY;
	private String path;
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

	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (altKey ? 1231 : 1237);
		result = prime * result + button;
		result = prime * result + charCode;
		result = prime * result + ((component == null) ? 0 : component.hashCode());
		result = prime * result + ((componentName == null) ? 0 : componentName.hashCode());
		result = prime * result + (ctrlKey ? 1231 : 1237);
		result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + keyCode;
		result = prime * result + offsetX;
		result = prime * result + offsetY;
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + (shiftKey ? 1231 : 1237);
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebEventRecord other = (WebEventRecord) obj;
		if (altKey != other.altKey)
			return false;
		if (button != other.button)
			return false;
		if (charCode != other.charCode)
			return false;
		if (component == null) {
			if (other.component != null)
				return false;
		} else if (!component.equals(other.component))
			return false;
		if (componentName == null) {
			if (other.componentName != null)
				return false;
		} else if (!componentName.equals(other.componentName))
			return false;
		if (ctrlKey != other.ctrlKey)
			return false;
		if (eventType == null) {
			if (other.eventType != null)
				return false;
		} else if (!eventType.equals(other.eventType))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (keyCode != other.keyCode)
			return false;
		if (offsetX != other.offsetX)
			return false;
		if (offsetY != other.offsetY)
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (shiftKey != other.shiftKey)
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
}
