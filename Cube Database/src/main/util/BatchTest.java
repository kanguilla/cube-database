package main.util;

import main.core.Database;
import main.core.ViewController;

public class BatchTest {

	public static void main(String[] args) {
		Database db = new Database(new ViewController());
		BatchUtility.readBatch("batch.cb", db);
	}

}
