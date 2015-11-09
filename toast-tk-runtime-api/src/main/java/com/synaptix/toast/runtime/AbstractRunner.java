package com.synaptix.toast.runtime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractRunner {

	private static final Logger LOG = LogManager.getLogger(AbstractRunner.class);
	
	public abstract void tearDownEnvironment();

	public abstract void beginTest();

	public abstract void endTest();

	public abstract void initEnvironment();
}
