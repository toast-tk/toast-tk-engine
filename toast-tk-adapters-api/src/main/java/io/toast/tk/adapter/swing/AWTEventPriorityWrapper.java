package io.toast.tk.adapter.swing;

public class AWTEventPriorityWrapper {

	int priorityTaken = 0;

	public AWTEventPriorityWrapper(){
	}
	
	public void takePriority(){
		this.priorityTaken++;
	}
	public boolean isPriorityTaken(){
		return this.priorityTaken>0;
	}
	
}
