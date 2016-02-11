package com.synaptix.toast.core.agent.interpret;

public class WebEventRecord {
	
	//public with setter
	
	public String type;

	public String target;

	public String id;

	public int button;

	public boolean altKey;

	public boolean ctrlKey;

	public boolean shiftKey;

	public WebEventRecord() {
		
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
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