import java.util.ArrayList;

import javax.swing.JOptionPane;

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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class GUI extends Application {

	private Stage primaryStage;
	private Button start, levels, exit;
	private Scene menuScene, gameScene;
	private TextField testField;
	private Group gameLayout;
	private GridPane menuLayout;
	private Enemy eminen;
	private Polyline path;
	private Rectangle path2;
	private Player pl;
	private SerializedSave save;
	private ArrayList<Level> niveis;
	private ArrayList<GameObject> nodes;
	private final double rectangleSpeedX = 150;
	private final double rectangleSpeedY = 150;// pixels per second
	private final DoubleProperty rectangleVelocityX = new SimpleDoubleProperty();
	private final DoubleProperty rectangleVelocityY = new SimpleDoubleProperty();
	private final LongProperty lastUpdateTime = new SimpleLongProperty();
	private final AnimationTimer rectangleAnimation = new AnimationTimer() {
		@Override
		public void handle(long timestamp) {
			if (lastUpdateTime.get() > 0) {
				final double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0;
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
				
			//checkShapeIntersection(pl);
		}
	};

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		this.primaryStage = primaryStage;
		ButtonHandler btnHandler = new ButtonHandler();
		MouseHandler posTester = new MouseHandler();

		start = new Button();
		start.setText("Start");
		start.setOnAction(btnHandler);

		levels = new Button();
		levels.setText("Levels");

		exit = new Button();
		exit.setText("Exit");
		exit.setOnAction(btnHandler);
		
		testField = new TextField();
		testField.setEditable(false);

		menuLayout = new GridPane();
		menuLayout.setAlignment(Pos.CENTER_LEFT);
		menuLayout.setVgap(20);
		menuLayout.setHgap(20);
		menuLayout.setPadding(new Insets(25, 25, 25, 25));

		menuLayout.add(start, 0, 0);
		menuLayout.add(levels, 0, 1);
		menuLayout.add(exit, 0, 2);

		menuScene = new Scene(menuLayout, 1366, 768);
		menuScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ESCAPE) {
					primaryStage.close();
				}
			}
		});
		primaryStage.setScene(menuScene);

		gameLayout = new Group();
		gameScene = new Scene(gameLayout, 1366, 768, Color.DARKGRAY);
		gameScene.setOnMouseMoved(posTester);
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.RIGHT) { // don't use toString here!!!
					
					rectangleVelocityX.set(rectangleSpeedX);
				} else if (event.getCode() == KeyCode.LEFT) {
					
					rectangleVelocityX.set(-rectangleSpeedX);
				} else if (event.getCode() == KeyCode.UP) {
					
					rectangleVelocityY.set(-rectangleSpeedY);
				} else if (event.getCode() == KeyCode.DOWN) {
				
					rectangleVelocityY.set(rectangleSpeedY);
				} else if (event.getCode() == KeyCode.ENTER){
					eminen.Move(2);
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
		gameLayout.getChildren().add(testField);
		primaryStage.show();
		double[] wallPoints = { 50, 400, 50, 600, 1000, 600, 1000, 300, 1200, 300, 1200, 100, 250, 100, 250, 400, 50,
				400 };
		Polyline pol = new Polyline(wallPoints);
		pol.setStrokeWidth(1);
		pol.setFill(Color.WHITE);

		Wall zaWall = new Wall(pol, 100, 100);
		gameLayout.getChildren().add(zaWall.getShape());
		
		Circle circ = new Circle(15);
		circ.setFill(Color.BLUE);
		circ.setStroke(Color.BLACK);
		double[] pathPoints = {0,100,
				300,0, 0, 0		};
		path = new Polyline(pathPoints);
		path2 = new Rectangle(300,0);
		eminen = new Enemy(circ,path2,500,400);
		gameLayout.getChildren().add(eminen.getShape());
		gameLayout.requestFocus();
		
		/*
		Circle circ2 = new Circle(15);
		circ.setFill(Color.BLUE);
		circ.setStroke(Color.BLACK);
		Enemy eminen2 = new Enemy(circ2,500,400);
		gameLayout.getChildren().add(eminen2.getShape());
		
		Circle circ3 = new Circle(15);
		circ.setFill(Color.BLUE);
		circ.setStroke(Color.BLACK);
		Enemy eminen3 = new Enemy(circ3,500,400);
		gameLayout.getChildren().add(eminen3.getShape());
		
		Circle circ4 = new Circle(15);
		circ.setFill(Color.BLUE);
		circ.setStroke(Color.BLACK);
		Enemy eminen4 = new Enemy(circ4,500,400);
		gameLayout.getChildren().add(eminen4.getShape());*/

		Rectangle rekt = new Rectangle(30, 30);
		rekt.setStroke(Color.BLACK);
		rekt.setFill(Color.FIREBRICK);
		pl = new Player(rekt, 300, 500,0);
		
		Rectangle checkS = new Rectangle(200,200);
		checkS.setFill(Color.DARKSEAGREEN);
		checkS.setStroke(Color.DARKGREEN);
		checkS.setOpacity(0.5);
		Rectangle checkR = new Rectangle(200,200);
		checkR.setFill(Color.DARKSEAGREEN);
		checkR.setStroke(Color.DARKGREEN);
		checkR.setOpacity(0.5);
		CheckPoint check1 = new CheckPoint(checkS,100,400,false,true);
		CheckPoint check2 = new CheckPoint(checkR,1050,100,true,false);
		
		gameLayout.getChildren().add(check1.getShape());
		gameLayout.getChildren().add(check2.getShape());
		gameLayout.getChildren().add(pl.getShape());
		
		// consertar a condição de verificação do nivel
		/*
		save = new SerializedSave();
		save.openFileInput();
		niveis = save.readLevels();
		save.closeFile();
		nodes = new ArrayList<GameObject>();
		for (int i = 0; i < niveis.get(0).getEnemies().size(); i++) {
			nodes.add(niveis.get(0).getEnemies().get(i));
		}
		for (int i = 0; i < niveis.get(0).getWalls().size(); i++) {
			nodes.add(niveis.get(0).getWalls().get(i));
		}
		for (int i = 0; i < niveis.get(0).getCoins().size(); i++) {
			nodes.add(niveis.get(0).getCoins().get(i));
		}*/
		
		
		/*
		ArrayList<Wall> tet = new ArrayList<Wall>();
		tet.add(zaWall);
		ArrayList<Enemy> tut = new ArrayList<Enemy>();
		tut.add(eminen);
		Coin das = new Coin (new Circle(10,10,10),50,50);
		ArrayList<Coin> tat = new ArrayList<Coin>();
		tat.add(das);
		CheckPoint daimn = new CheckPoint(new Circle(10,10,10), 60, 60, false, false);
		ArrayList<CheckPoint> tit = new ArrayList<CheckPoint>();
		Level test = new Level(0,pl,tut,tet,tat, tit);
		save = new SerializedSave();
		save.openFileOutput();
		save.addLevel(test);
		save.closeFile();
		*/
		
		
		rectangleAnimation.start();
	}

	private void checkShapeIntersection(GameObject block) {
		for (GameObject static_bloc : nodes) {
			Shape intersect = Shape.intersect(block.getShape(), static_bloc.getShape());
			if (intersect.getBoundsInLocal().getWidth() != -1) {
				System.out.println(static_bloc.getClass().toString());
				if (static_bloc.getClass().toString().equals("class Enemy")) {
					pl.getShape().setTranslateX(0);
                    pl.getShape().setTranslateY(0);
					pl.setDeaths(pl.getDeaths()+1);
					System.out.println("morreu");
				}else if(static_bloc.getClass().toString() == "class Coin") {
					pl.setPoints(pl.getPoints()+1);
					gameLayout.getChildren().remove(static_bloc);
				}else if(static_bloc.getClass().toString().equals("class Wall")) {
					System.out.println("adsaodoadoij");
				}
			}
		}
	}
	
	private class MouseHandler implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent arg0) {
			testField.setText("X: " + arg0.getX() + "\tY: " + arg0.getY());
			
		}
		
	}

	private class ButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			if (arg0.getSource() == exit) {
				Stage stage = (Stage) exit.getScene().getWindow();
				stage.close();
			} else if (arg0.getSource() == start) {
				primaryStage.setScene(gameScene);
				
			}

		}

	}
}
