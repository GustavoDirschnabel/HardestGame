import java.util.ArrayList;

import javax.swing.JOptionPane;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class GUI extends Application {

	private Stage primaryStage;
	private Button start, levels, exit;
	private Button[] levelSelection;
	private Scene menuScene, gameScene, levelSelectionScene;
	private TextField deathCounter,levelCounter;
	private Group gameLayout;
	private GridPane menuLayout, levelSelectionLayout;
	private Enemy eminen;
	private Player pl;
	private Level nivelAtual;
	private ArrayList<CheckPoint> levelCheckpoints;
	private ArrayList<Coin> levelCoins;
	private ArrayList<Integer> savedCoins, unsavedCoins;
	private final double collisionTolerance = 0.50000000000001;
	private boolean canMoveLeft, canMoveRight, canMoveUp, canMoveDown, movingLeft, movingRight, movingUp, movingDown;
	private int currentLevel;
	private SerializedSave save;
	private ArrayList<Level> niveis;
	private ArrayList<GameObject> nodes;
	private final double rectangleSpeedX = 200;
	private final double rectangleSpeedY = 200;// pixels per second
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
			
			checkShapeIntersection(pl);
	
		}
	};

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Hardest Game");
		primaryStage.setResizable(false);
		ButtonHandler btnHandler = new ButtonHandler();
		MouseHandler mHandler = new MouseHandler();

		start = new Button();
		start.setPrefSize(150, 50);
		start.setText("START");
		start.setFont(Font.font("Impact", 36));
		start.setStyle("-fx-text-fill: black;");
		start.setBackground(null);
		start.setOnMouseEntered(mHandler);
		start.setOnMouseExited(mHandler);
		start.setOnAction(btnHandler);

		levels = new Button();
		levels.setPrefSize(150, 50);
		levels.setText("LEVELS");
		levels.setFont(Font.font("Impact", 36));
		levels.setStyle("-fx-text-fill: black;");
		levels.setBackground(null);
		levels.setOnMouseEntered(mHandler);
		levels.setOnMouseExited(mHandler);
		levels.setOnAction(btnHandler);

		exit = new Button();
		exit.setPrefSize(150, 50);
		exit.setText("EXIT");
		exit.setFont(Font.font("Impact", 36));
		exit.setStyle("-fx-text-fill: black;");
		exit.setBackground(null);
		exit.setOnMouseEntered(mHandler);
		exit.setOnMouseExited(mHandler);
		exit.setOnAction(btnHandler);

		menuLayout = new GridPane();
		menuLayout.setAlignment(Pos.CENTER);
		menuLayout.setVgap(20);
		menuLayout.setHgap(40);

		menuLayout.add(start, 0, 0);
		menuLayout.add(levels, 1, 0);
		menuLayout.add(exit, 2, 0);

		menuScene = new Scene(menuLayout, 1280, 720);
		menuScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ESCAPE) {
					primaryStage.close();
				}
			}
		});
		primaryStage.setScene(menuScene);

		levelSelectionLayout = new GridPane();
		levelSelectionLayout.setAlignment(Pos.CENTER);
		levelSelectionLayout.setVgap(20);
		levelSelectionLayout.setHgap(20);
		
		levelSelection = new Button[5];
		for(Integer i = 0; i < levelSelection.length; i++) {
			levelSelection[i] = new Button();
			levelSelection[i].setText(String.valueOf(i+1));
			levelSelection[i].setPrefSize(50, 50);
			levelSelection[i].setFont(Font.font("Impact", 36));
			levelSelection[i].setStyle("-fx-text-fill: black;");
			levelSelection[i].setBackground(null);
			levelSelection[i].setOnMouseEntered(mHandler);
			levelSelection[i].setOnMouseExited(mHandler);
			levelSelection[i].setOnAction(btnHandler);
		}
		levelSelectionLayout.add(levelSelection[0], 0, 0);
		levelSelectionLayout.add(levelSelection[1], 1, 0);
		levelSelectionLayout.add(levelSelection[2], 2, 0);
		levelSelectionLayout.add(levelSelection[3], 3, 0);
		levelSelectionLayout.add(levelSelection[4], 4, 0);
		levelSelectionLayout.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ESCAPE) {
					primaryStage.setScene(menuScene);
				}
			}
		});
		
		levelSelectionScene = new Scene(levelSelectionLayout,1280, 720);
		

		deathCounter = new TextField("MORTES:");
		deathCounter.setFont(Font.font("Impact", 36));
		deathCounter.setStyle("-fx-text-inner-color: black;");
		deathCounter.setEditable(false);
		deathCounter.setBackground(null);
		deathCounter.setLayoutX(1100);
		deathCounter.setLayoutY(-10);
		
		levelCounter = new TextField(currentLevel + 1 +"/"+5);
		levelCounter.setFont(Font.font("Impact", 36));
		levelCounter.setStyle("-fx-text-inner-color: black;");
		levelCounter.setEditable(false);
		levelCounter.setBackground(null);
		levelCounter.setLayoutX(600);
		levelCounter.setLayoutY(-10);
		
		gameLayout = new Group();
		gameScene = new Scene(gameLayout, 1280, 720, new Color(0.7,0.7,1,1));
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.RIGHT) {
					if (canMoveRight) {
						rectangleVelocityX.set(rectangleSpeedX);
						movingRight = true;
						movingLeft = false;
					}
				} if (event.getCode() == KeyCode.LEFT) {
					if (canMoveLeft) {
						rectangleVelocityX.set(-rectangleSpeedX);
						movingLeft = true;
						movingRight = false;
					}
				} if (event.getCode() == KeyCode.UP) {
					if (canMoveUp) {
						rectangleVelocityY.set(-rectangleSpeedY);
						movingUp = true;
						movingDown = false;
					}
				} if (event.getCode() == KeyCode.DOWN) {
					if (canMoveDown) {
						rectangleVelocityY.set(rectangleSpeedY);
						movingDown = true;
						movingUp = false;
					}
				} else if (event.getCode() == KeyCode.ESCAPE) {
					primaryStage.setScene(menuScene);
				}
			}
		});
		gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.RIGHT) {
					movingRight = false;
					if (!movingLeft)
						rectangleVelocityX.set(0);
					else if (canMoveLeft)
						rectangleVelocityX.set(-rectangleSpeedX);
				}
				if (event.getCode() == KeyCode.LEFT) {
					movingLeft = false;
					if (!movingRight)
						rectangleVelocityX.set(0);
					else if (canMoveRight)
						rectangleVelocityX.set(rectangleSpeedX);
				}
				if (event.getCode() == KeyCode.UP) {
					movingUp = false;
					if (!movingDown)
						rectangleVelocityY.set(0);
					else if (canMoveDown)
						rectangleVelocityY.set(rectangleSpeedY);
				}
				if (event.getCode() == KeyCode.DOWN) {
					movingDown = false;
					if (!movingUp)
						rectangleVelocityY.set(0);
					else if (canMoveUp)
						rectangleVelocityY.set(-rectangleSpeedY);
				}
			}
		});
		gameLayout.getChildren().add(deathCounter);
		gameLayout.getChildren().add(levelCounter);
		
		primaryStage.show();

		
		save = new SerializedSave();
		save.openFileInput();
		niveis = save.readLevels();
		currentLevel = 0;
		loadLevel(currentLevel);
		gameLayout.requestFocus();
		

	rectangleAnimation.start();
	}

	private void checkShapeIntersection(GameObject block) {
		for(GameObject static_bloc : nodes) {
			Shape intersect = Shape.intersect(block.getShape(), static_bloc.getShape());
			if (intersect.getBoundsInLocal().getWidth() != -1) {
				
				if (static_bloc.getClass().toString().equals("class Enemy")) {
					
					pl.setPoints(pl.getPoints() - unsavedCoins.size());
					
					for(int i = 0; i < levelCoins.size(); i ++) {
						if(unsavedCoins.contains(i)) {
							unsavedCoins.remove((Object) i);
							nodes.add(levelCoins.get(i));
							gameLayout.getChildren().add(levelCoins.get(i).getShape());
						}
					}
					
					for(int i = 0; i < levelCheckpoints.size(); i++) {
						if(!levelCheckpoints.get(i).isActivated()) {
							Rectangle checkShape = (Rectangle) levelCheckpoints.get(i - 1).getShape();
							Bounds checkBounds = checkShape.localToScene(checkShape.getBoundsInLocal());
							pl.getShape().relocate(checkBounds.getMinX() + (checkBounds.getMaxX() - checkBounds.getMinX())/2,
									checkBounds.getMinY() + (checkBounds.getMaxY() - checkBounds.getMinY())/2);
							pl.getShape().setTranslateX(-17.5);
							pl.getShape().setTranslateY(-17.5);
							break;
						}
					}
					pl.setDeaths(pl.getDeaths() + 1);
					deathCounter.setText("MORTES: "+pl.getDeaths());
					
				} else if (static_bloc.getClass().toString().equals("class Coin")) {
					pl.setPoints(pl.getPoints() + 1);
					for(int i = 0; i < levelCoins.size(); i++) {
						if(levelCoins.get(i).getIniX() == static_bloc.getIniX() && levelCoins.get(i).getIniY() == static_bloc.getIniY()) {
							nodes.remove(static_bloc);
							gameLayout.getChildren().remove(levelCoins.get(i).getShape());
							unsavedCoins.add(i);
							break;
						}
					}
					
				} else if (static_bloc.getClass().toString().equals("class Wall")) {
					Rectangle blockShape = (Rectangle) block.getShape();
					Vector2 blockCenter = new Vector2(
							blockShape.getLayoutX() + blockShape.getTranslateX() + blockShape.getWidth() / 2,
							blockShape.getLayoutY() + blockShape.getTranslateY() + blockShape.getHeight() / 2);
					
					canMoveLeft = true;
					canMoveRight = true;
					canMoveUp = true;
					canMoveDown = true;

					Bounds intersectBounds = intersect.localToScene(intersect.getBoundsInLocal());
					Vector2 intersectCenter = new Vector2(
							intersectBounds.getMinX() + (intersectBounds.getMaxX() - intersectBounds.getMinX()) / 2,
							intersectBounds.getMinY() + (intersectBounds.getMaxY() - intersectBounds.getMinY()) / 2);
					Vector2 centerDifference = new Vector2(intersectCenter.getPosX() - blockCenter.getPosX(),
							intersectCenter.getPosY() - blockCenter.getPosY());
					double sumOfAbsoluteDifferences = Math.abs(centerDifference.getPosX())
							+ Math.abs(centerDifference.getPosY());
					boolean stuck = false;

					if (sumOfAbsoluteDifferences <= 2.5) {
						rectangleVelocityX.set(0);
						rectangleVelocityY.set(0);
						Rectangle fixer = new Rectangle(intersectBounds.getMinX(), intersectBounds.getMinY(), 10, 10);
						Shape verticeFinder = Shape.intersect(fixer, static_bloc.getShape());
						if (verticeFinder.getBoundsInLocal().getWidth() == -1) {
							stuck = true;
							canMoveLeft = true;
							canMoveUp = true;
							canMoveRight = false;
							canMoveDown = false;
						} else {
							fixer.setTranslateX(intersectBounds.getMaxX() - intersectBounds.getMinX() - 1);
							verticeFinder = Shape.intersect(fixer, static_bloc.getShape());
							if (verticeFinder.getBoundsInLocal().getHeight() == -1
									&& verticeFinder.getBoundsInLocal().getWidth() == -1) {
								stuck = true;
								canMoveLeft = false;
								canMoveUp = true;
								canMoveRight = true;
								canMoveDown = false;
							} else {
								fixer.setTranslateY(intersectBounds.getMaxY() - intersectBounds.getMinY());
								verticeFinder = Shape.intersect(fixer, static_bloc.getShape());
								if (verticeFinder.getBoundsInLocal().getWidth() == -1) {
									stuck = true;
									canMoveLeft = false;
									canMoveUp = false;
									canMoveRight = true;
									canMoveDown = true;
								} else {
									stuck = true;
									canMoveLeft = true;
									canMoveUp = false;
									canMoveRight = false;
									canMoveDown = true;

								}
							}
						}
					}
					else if ( sumOfAbsoluteDifferences >= 25) {
						if(centerDifference.getPosX() > 0 && centerDifference.getPosY() > 0) {
							//quina à sudeste
							pl.getShape().setTranslateX(pl.getShape().getTranslateX() - 0.5);
							pl.getShape().setTranslateY(pl.getShape().getTranslateY() - 0.5);
						}
						else if(centerDifference.getPosX() < 0 && centerDifference.getPosY() > 0) {
							//quina à sudoeste
							pl.getShape().setTranslateX(pl.getShape().getTranslateX() + 0.5);
							pl.getShape().setTranslateY(pl.getShape().getTranslateY() - 0.5);
						}
						else if(centerDifference.getPosX() > 0 && centerDifference.getPosY() < 0) {
							//quina à nordeste
							pl.getShape().setTranslateX(pl.getShape().getTranslateX() - 0.5);
							pl.getShape().setTranslateY(pl.getShape().getTranslateY() + 0.5);
						}
						else {
							//quina à noroeste
							pl.getShape().setTranslateX(pl.getShape().getTranslateX() + 0.5);
							pl.getShape().setTranslateY(pl.getShape().getTranslateY() + 0.5);
						}
						stuck = true;
					}
					// Comandos para teste
					/*System.out.println("X: " + centerDifference.getPosX() + "\tY: " + centerDifference.getPosY());
					System.out.println("SAD: " + sumOfAbsoluteDifferences);
					System.out.println(Math.abs(centerDifference.getPosX()) / sumOfAbsoluteDifferences);
					System.out.println(Math.abs(centerDifference.getPosY()) / sumOfAbsoluteDifferences);*/
					//System.out.println("mRight: " + canMoveRight + "\tmLeft: " + canMoveLeft + "\tUp:" + canMoveUp + "\tDown: " + canMoveDown);
					
					if (Math.abs(centerDifference.getPosX()) / sumOfAbsoluteDifferences > collisionTolerance
							&& !stuck) {
						if (centerDifference.getPosX() < 0) {
							canMoveLeft = false;
						} else {
							canMoveRight = false;
						}
						rectangleVelocityX.set(0);
					}
					if (Math.abs(centerDifference.getPosY()) / sumOfAbsoluteDifferences > collisionTolerance
							&& !stuck) {
						if (centerDifference.getPosY() < 0) {
							canMoveUp = false;
						} else {
							canMoveDown = false;
						}
						rectangleVelocityY.set(0);
					}
					
				}
				else if (static_bloc.getClass().toString().equals("class CheckPoint")) {
					CheckPoint hitCheck = (CheckPoint) static_bloc;
					if (hitCheck.isFinishLine() && pl.getPoints() == nivelAtual.getCoins().size()) {
						if(currentLevel == niveis.size() - 1) {
							primaryStage.setScene(menuScene);
							currentLevel = 0;
							loadLevel(currentLevel);
						}
						else {
							currentLevel++;
							loadLevel(currentLevel);
						}
					}
					else if(!hitCheck.isActivated() && !hitCheck.isFinishLine()) {
						for(int i = 1; i < nivelAtual.getCheckPoints().size(); i++) {
							if(!levelCheckpoints.get(i).isActivated()) {
							   levelCheckpoints.get(i).setActivated(true);
							   break;
							}
						}
						for(int i = 0; i < unsavedCoins.size(); i++) {
							savedCoins.add(unsavedCoins.get(i));
						}
					}
					
				}
			} else if (static_bloc.getClass().toString().equals("class Wall")) {
				canMoveLeft = true;
				canMoveRight = true;
				canMoveUp = true;
				canMoveDown = true;
			}
		}
	}

	
	public void loadLevel(int levelNumber) {
		gameLayout.getChildren().remove(2, gameLayout.getChildren().size());
		for(int i = 0; i < niveis.size(); i++) {
			if(niveis.get(i).getLevelNumber() == levelNumber) {
				nivelAtual = niveis.get(i);
				break;
			}
		}
		nodes = new ArrayList<GameObject>();
		levelCheckpoints = nivelAtual.getCheckPoints();
		levelCoins = nivelAtual.getCoins();
		savedCoins = new ArrayList<Integer>();
		unsavedCoins = new ArrayList<Integer>();
		for (int i = 0; i < nivelAtual.getWalls().size(); i++) {
			nodes.add(nivelAtual.getWalls().get(i));
			Shape walle = nivelAtual.getWalls().get(i).getShape();
			walle.setFill(Color.WHITE);
			gameLayout.getChildren().add(walle);
			
		}
		for (int i = 0; i < nivelAtual.getCheckPoints().size(); i++) {
			nodes.add(levelCheckpoints.get(i));
			gameLayout.getChildren().add(levelCheckpoints.get(i).getShape());
		}
		for (int i = 0; i < nivelAtual.getEnemies().size(); i++) {
			eminen = nivelAtual.getEnemies().get(i);
			nodes.add(eminen);
			gameLayout.getChildren().add(eminen.getShape());
		}
		for (int i = 0; i < nivelAtual.getCoins().size(); i++) {
			nodes.add(levelCoins.get(i));
			gameLayout.getChildren().add(levelCoins.get(i).getShape());
		}
		
		pl = nivelAtual.getPlayer();
		pl.setPoints(0);
		rectangleVelocityX.set(0);
		rectangleVelocityY.set(0);
		gameLayout.getChildren().add(pl.getShape());
		
		levelCounter.setText(currentLevel + 1 +"/"+5);
		
	}

	
	private class MouseHandler implements EventHandler<MouseEvent>{
		@Override
		public void handle(MouseEvent e) {	
			if(e.getEventType() == MouseEvent.MOUSE_ENTERED ) {
				((Node) e.getSource()).setStyle("-fx-text-fill: blue;");
			}else if(e.getEventType() == MouseEvent.MOUSE_EXITED) {
				((Node) e.getSource()).setStyle("-fx-text-fill: black;");
			}
		}
	}

	private class ButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			if (arg0.getSource() == exit) {
				Stage stage = (Stage) exit.getScene().getWindow();
				stage.close();
			} else if (arg0.getSource() == start) {
				currentLevel = 0;
				loadLevel(currentLevel);
				primaryStage.setScene(gameScene);
			}
			else if (arg0.getSource() == levels) {
				primaryStage.setScene(levelSelectionScene);
			}
			else if (arg0.getSource() == levelSelection[0]) {
				currentLevel = 0;
				loadLevel(currentLevel);
				primaryStage.setScene(gameScene);
			}
			else if (arg0.getSource() == levelSelection[1]) {
				currentLevel = 1;
				loadLevel(currentLevel);
				primaryStage.setScene(gameScene);
			}
			else if (arg0.getSource() == levelSelection[2]) {
				currentLevel = 2;
				loadLevel(currentLevel);
				primaryStage.setScene(gameScene);
			}
			else if (arg0.getSource() == levelSelection[3]) {
				currentLevel = 3;
				loadLevel(currentLevel);
				primaryStage.setScene(gameScene);
			}
			else if (arg0.getSource() == levelSelection[4]) {
				currentLevel = 4;
				loadLevel(currentLevel);
				primaryStage.setScene(gameScene);
			}
		}

	}
}
