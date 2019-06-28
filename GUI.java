import java.util.ArrayList;

import javafx.application.Application;
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
	
	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		this.primaryStage = primaryStage;
		ButtonHandler btnHandler = new ButtonHandler();
		KeyHandler kHandler = new KeyHandler();
		
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
		menuScene.setOnKeyPressed(kHandler);
		primaryStage.setScene(menuScene);
		
		gameLayout = new Group();
		gameScene = new Scene(gameLayout,1366,768);
		gameScene.setOnKeyPressed(kHandler);
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
		Player pl = new Player(rekt,300,500);
		gameLayout.getChildren().add(pl.getShape());
		ArrayList<Wall> wall0 = new ArrayList<Wall>();
		wall0.add(zaWall);
		
		ArrayList<Coin> coin0 = new ArrayList<Coin>();
		ArrayList<Enemy> enemy0 = new ArrayList<Enemy>();
		
		Level level0 = new Level(0,pl,enemy0,wall0,coin0);
		SerializedSave save = new SerializedSave();
		save.openFile();
		save.addLevel(level0);
		save.closeFile();
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
	
	private class KeyHandler implements EventHandler<KeyEvent>{

		@Override
		public void handle(KeyEvent arg0) {
			if(arg0.getCode() == KeyCode.ESCAPE) {
				if(primaryStage.getScene() == menuScene) {
					primaryStage.close();
				}
				else if(primaryStage.getScene() == gameScene) {
					primaryStage.setScene(menuScene);
				}
			}
			
		}
		
	}
	
	

}
