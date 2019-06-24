import javafx.scene.shape.Shape;

public abstract class GameObject {
	protected Shape shape;
	protected Double iniX;
	protected Double iniY;
	public GameObject(Shape shape, Double x, Double y) {
		this.shape = shape;
		this.iniX = x;
		this.iniY = y;
		shape.relocate(x, y);
	}
	
}
