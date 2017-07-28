package com.synaptix.toast.test.runtime;

import io.toast.tk.runtime.AbstractTestPlanRunner;
import io.toast.tk.runtime.ToastRuntimeException;
import org.junit.Test;

/**
 * To run this example, a mongoDB instance is required
 */
public class MongoTest extends AbstractTestPlanRunner {

	private static final String MONGO_DB_REPORT_STORAGE_HOST = "10.149.57.196";
	private static final String API_TOKEN = "-ISHO1VrNHwOjXPm8g2lqF8K4EZeFG82";
	private static final int MONGO_DB_REPORT_STORAGE_PORT = 27017;

	public MongoTest() {
		super(MONGO_DB_REPORT_STORAGE_HOST, MONGO_DB_REPORT_STORAGE_PORT, "play_db");
	}

	public static void main(String[] args) throws ToastRuntimeException {
		MongoTest projectRunner;
		projectRunner = new MongoTest();
		projectRunner.test("Ontime", "5979afe31f00001f001b9d22", true, API_TOKEN);
	}

	@Test
	public void runTest() throws ToastRuntimeException {
		MongoTest.main(null);
	}

	@Override
	public void tearDownEnvironment() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initEnvironment() {
		// TODO Auto-generated method stub
	}

	@Override
	public void beginTest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void endTest() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getReportsOutputPath() {
		// TODO Auto-generated method stub
		return null;
	}
}
