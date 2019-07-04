import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.io.Serializable;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;

public class Enemy extends GameObject implements Serializable {
	private Shape path;
	public Enemy(Circle shape,Shape path, double x, double y) {
		super(shape, x, y);
		this.path = path;
		// TODO Auto-generated constructor stub
	}
	public void Move(double duracao) {
		PathTransition mover = new PathTransition(Duration.seconds(duracao),path,this.shape);
		mover.setCycleCount(PathTransition.INDEFINITE);
		mover.setInterpolator(Interpolator.LINEAR);
		mover.play();
	}
	public Shape getPath() {
		return path;
	}
	public void setPath(Shape path) {
		this.path = path;
	}
	
}
