import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class GUI extends Application{
	
	private Stage primaryStage;
	private Button start,levels,exit;
	private Scene menuScene,gameScene;
	private Group gameLayout;
	private GridPane menuLayout;
	private Player pl;
	private final double rectangleSpeedX = 150 ;
	private final double rectangleSpeedY = 150 ;// pixels per second
	private final DoubleProperty rectangleVelocityX = new SimpleDoubleProperty();
	private final DoubleProperty rectangleVelocityY = new SimpleDoubleProperty();
	private final LongProperty lastUpdateTime = new SimpleLongProperty();
	private final AnimationTimer rectangleAnimation = new AnimationTimer() {
	  @Override
	  public void handle(long timestamp) {
	    if (lastUpdateTime.get() > 0) {
	      final double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0 ;
	      final double deltaX = elapsedSeconds * rectangleVelocityX.get();
	      final double oldX = pl.getShape().getTranslateX();
	      final double newX = oldX + deltaX;
	      final double deltaY = elapsedSeconds * rectangleVelocityY.get();
	      final double oldY = pl.getShape().getTranslateY();
	      final double newY = oldY + deltaY;
	      pl.getShape().setTranslateX(newX);
	      pl.getShape().setTranslateY(newY);
	    }
	    lastUpdateTime.set(timestamp);
	  }
	};
	
	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		this.primaryStage = primaryStage;
		ButtonHandler btnHandler = new ButtonHandler();
		
		start = new Button();
		start.setText("Start");
		start.setOnAction(btnHandler);
		
		levels = new Button();
		levels.setText("Levels");
		
		exit = new Button();
		exit.setText("Exit");
		exit.setOnAction(btnHandler);
		
		
		menuLayout = new GridPane();
		menuLayout.setAlignment(Pos.CENTER_LEFT);
		menuLayout.setVgap(20);
		menuLayout.setHgap(20);
		menuLayout.setPadding(new Insets(25,25,25,25));
		
		menuLayout.add(start, 0, 0);
		menuLayout.add(levels, 0, 1);
		menuLayout.add(exit, 0, 2);
		
		menuScene = new Scene(menuLayout, 1366, 768);
		menuScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if(e.getCode() == KeyCode.ESCAPE) {
						primaryStage.close();
				}
			}
		});
		primaryStage.setScene(menuScene);
		
		gameLayout = new Group();
		gameScene = new Scene(gameLayout,1366,768);
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			  @Override
			  public void handle(KeyEvent event) {
				  if (event.getCode()==KeyCode.RIGHT) { // don't use toString here!!!
					  rectangleVelocityX.set(rectangleSpeedX);
				  } 
				  else if (event.getCode() == KeyCode.LEFT) {
					  rectangleVelocityX.set(-rectangleSpeedX);
				  } 
				  else if (event.getCode() == KeyCode.UP) {
				      rectangleVelocityY.set(-rectangleSpeedY);
				  }	
				  else if (event.getCode() == KeyCode.DOWN) {
				      rectangleVelocityY.set(rectangleSpeedY);
				   }
			  }
		});
		gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			  @Override
			  public void handle(KeyEvent event) {
				    if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT) {
				      rectangleVelocityX.set(0);
				    }
				    if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
					      rectangleVelocityY.set(0);
				    }
			  }
		});
		primaryStage.show();
		
		double[] wallPoints = {
								200,400,
								200,600,
								900,600,
								900,300,
								1100,300,
								1100,100,
								400,100,
								400,400,
								200,400
								};
		Polyline pol = new Polyline(wallPoints);
		pol.setStrokeWidth(15);
		
		Wall zaWall = new Wall(pol,200,100);
		gameLayout.getChildren().add(zaWall.getShape());
		
		Rectangle rekt = new Rectangle(50,50);
		rekt.setFill(Color.FIREBRICK);
		pl = new Player(rekt,300,500);
		gameLayout.getChildren().add(pl.getShape());
		rectangleAnimation.start();
	}
	private class ButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			if(arg0.getSource() == exit) {
			    Stage stage = (Stage) exit.getScene().getWindow();
			    stage.close();
			}
			else if (arg0.getSource() == start) {
				primaryStage.setScene(gameScene);
			}
			
		}
		
	}
}

/*public void move() {
		if(cima) {
			pl.shape.setTranslateY(pl.shape.getTranslateY() - velocidade);
			pl.shape.translateYProperty();
		}
		if(baixo) {
			pl.shape.setTranslateY(pl.shape.getTranslateY() + velocidade);
			pl.shape.translateYProperty();
		}
		if(esquerda) {
			pl.shape.setTranslateX(pl.shape.getTranslateX() - velocidade);
			pl.shape.translateXProperty();
		}
		if(direita) {
			pl.shape.setTranslateX(pl.shape.getTranslateX() + velocidade);
			pl.shape.translateXProperty();
		}
	}*/