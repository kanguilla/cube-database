package main.core;

import javafx.scene.Parent;
import javafx.scene.Scene;

public abstract class DynamicScene extends Scene{
	public DynamicScene(Parent root) {
		super(root);
	}

	public abstract void update();
}
