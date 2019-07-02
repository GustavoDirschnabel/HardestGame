import java.io.Serializable;

import javafx.scene.shape.Shape;

public class CheckPoint extends GameObject implements Serializable {
	private boolean finishLine;
	private boolean activated;
	
	public CheckPoint(Shape shape, double x, double y, boolean finishLine, boolean activated) {
		super(shape, x, y);
		this.finishLine = finishLine;
		this.activated = activated;
	}

	public boolean isFinishLine() {
		return finishLine;
	}

	public void setFinishLine(boolean finishLine) {
		this.finishLine = finishLine;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}
	
	
}
