package io.toast.tk.runtime.utils;

import java.util.ArrayList;
import java.util.List;

public class ResultObject {
	
	private int totalErrors = 0;
	private int totalTechnical = 0;
	private int totalSuccess = 0;
	private List<String> filesWithErrorsList = new ArrayList<>();

	public ResultObject(
			final int totalErrors,
			final int totalTechnical,
			final int totalSuccess,
			final List<String> filesWithErrorsList) {
		this.totalErrors = totalErrors;
		this.totalTechnical = totalTechnical;
		this.totalSuccess = totalSuccess;
		this.filesWithErrorsList = filesWithErrorsList;
	}

	public int getTotalErrors() {
		return this.totalErrors;
	}
	
	public int getTotalTechnical() {
		return this.totalTechnical;
	}

	public int getTotalSuccess() {
		return this.totalSuccess;
	}

	public List<String> getFilesWithErrorsList() {
		return this.filesWithErrorsList;
	}
}
