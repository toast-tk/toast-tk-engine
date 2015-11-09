package com.synaptix.toast.core.net.request;

public class RecordRequest implements IIdRequest {

	private boolean start;

	private boolean modeChange;

	private int mode;

	/**
	 * for serialization purpose only
	 */
	public RecordRequest() {
	}

	public RecordRequest(
		boolean start) {
		this.setStart(start);
	}

	public RecordRequest(
		int mode) {
		this.setStart(true);
		this.setModeChange(true);
		this.mode = mode;
	}

	private void setModeChange(
		boolean modeChange) {
		this.modeChange = modeChange;
	}

	@Override
	public String getId() {
		return null;
	}

	public boolean isStart() {
		return start;
	}

	private void setStart(
		boolean start) {
		this.start = start;
	}

	public boolean isModeChange() {
		return modeChange;
	}

	public int getMode() {
		return mode;
	}
}
