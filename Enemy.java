import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

import java.io.Serializable;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;

public class Enemy extends GameObject implements Serializable {
	private Shape path;
	double moveDuration;
	public Enemy(Circle shape,Shape path, double x, double y, double moveDuration) {
		super(shape, x, y);
		this.path = path;
		this.moveDuration = moveDuration;
		shape.setStroke(Color.BLACK);
		shape.setStrokeType(StrokeType.INSIDE);
		shape.setStrokeWidth(5);
		// TODO Auto-generated constructor stub
	}
	public void Move() {
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
	public double getMoveDuration() {
		return moveDuration;
	}
	public void setMoveDuration(double moveDuration) {
		this.moveDuration = moveDuration;
	}

	
}
