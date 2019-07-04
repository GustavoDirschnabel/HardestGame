import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.io.Serializable;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;

public class Enemy extends GameObject implements Serializable {
	private Shape path;
	int moveDuration;
	public Enemy(Circle shape,Shape path, double x, double y, int moveDuration) {
		super(shape, x, y);
		this.path = path;
		this.moveDuration = moveDuration;
		// TODO Auto-generated constructor stub
	}
	public void Move() {
		System.out.println(path.toString());
		PathTransition mover = new PathTransition(Duration.seconds(moveDuration),path,this.shape);
		mover.setCycleCount(PathTransition.INDEFINITE);
		mover.setInterpolator(Interpolator.LINEAR);
		mover.play();
	}
	public Shape getPath() {
		return path;
	}
	public void setPath(Shape path) {
		this.path = path;
		this.Move();
	}
	public int getMoveDuration() {
		return moveDuration;
	}
	public void setMoveDuration(int moveDuration) {
		this.moveDuration = moveDuration;
	}

	
}
