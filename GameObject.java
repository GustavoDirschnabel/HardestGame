import java.io.Serializable;

import javafx.scene.shape.Shape;

public abstract class GameObject implements Serializable {
	protected Shape shape;
	protected double iniX;
	protected double iniY;
	public GameObject(Shape shape, double x, double y) {
		this.shape = shape;
		this.iniX = x;
		this.iniY = y;
		shape.relocate(x, y);
	}
	public Shape getShape() {
		return shape;
	}
	public void setShape(Shape shape) {
		this.shape = shape;
	}
	public double getIniX() {
		return iniX;
	}
	public void setIniX(double iniX) {
		this.iniX = iniX;
	}
	public double getIniY() {
		return iniY;
	}
	public void setIniY(double iniY) {
		this.iniY = iniY;
	}
	
	
	
}
