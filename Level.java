import java.io.Serializable;
import java.util.ArrayList;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

public class Level implements Serializable {
	private int levelNumber;
	private int numberOfDeaths;
	private Vector2 posPlayer;
	private ArrayList<Vector2> posEnemies;
	private ArrayList<Integer> enemyPathType;
	private ArrayList<Double> enemyPathPoints;
	private ArrayList<Integer> firstIndexofEachEnemyPath;
	private ArrayList<Integer> enemyMoveDuration;
	private ArrayList<Vector2> posWalls;
	private ArrayList<Double> wallPoints;
	private ArrayList<Integer> firstIndexofEachWall;
	private ArrayList<Vector2> posCoins;
	private ArrayList<Vector2> posCheckpoints;
	private ArrayList<Vector2> checkPointBounds;
	
	public Level(int levelNumber, Player player, ArrayList<Enemy> enemies, ArrayList<Wall> wall,
			ArrayList<Coin> coins, ArrayList<CheckPoint> checkpoints) {
		this.levelNumber = levelNumber;
		this.numberOfDeaths = player.getDeaths();
		this.posPlayer = new Vector2(player.getIniX(), player.getIniY());
		this.posEnemies = new ArrayList<Vector2>();
		this.posWalls = new ArrayList<Vector2>();
		this.wallPoints = new ArrayList<Double>();
		this.firstIndexofEachWall = new ArrayList<Integer>();
		this.posCoins = new ArrayList<Vector2>();
		this.posCheckpoints = new ArrayList<Vector2>();
		this.checkPointBounds = new ArrayList<Vector2>();
		this.enemyPathPoints = new ArrayList<Double>();
		this.enemyPathType = new ArrayList<Integer>();
		this.firstIndexofEachEnemyPath = new ArrayList<Integer>();
		this.enemyMoveDuration = new ArrayList<Integer>();
		
		for(int i = 0; i < enemies.size(); i ++) {
			Vector2 pos = new Vector2(enemies.get(i).getIniX(), enemies.get(i).getIniY());
			this.posEnemies.add(i, pos);
			
			this.enemyMoveDuration.add(i, enemies.get(i).getMoveDuration());
			
			Shape enePath = enemies.get(i).getPath();
			int index = 0;
			if(i != 0) {
				Shape previousEnePath = enemies.get(i - 1).getPath();
				index += this.firstIndexofEachEnemyPath.get(i - 1);
				switch(previousEnePath.getClass().getName()) {
					case "javafx.scene.shape.Rectangle":
						index += 2;
						break;
					case "javafx.scene.shape.Circle":
						index += 1;
						break;
					case "javafx.scene.shape.Ellipse":
						index += 2;
						break;
					case "javafx.scene.shape.Polyline":
						Polyline previousEnePathAdapter = (Polyline) previousEnePath;
						index += previousEnePathAdapter.getPoints().size();
						break;
					default:
						System.err.println("Um Caminho de um Enemy não foi detectado corretamente!");
				}
			}
			
			this.firstIndexofEachEnemyPath.add(i,index);
			
			switch(enePath.getClass().getName()) {
				case "javafx.scene.shape.Rectangle":
					this.enemyPathType.add(i, ShapeTypes.RECTANGLE);
					Rectangle path = (Rectangle) enemies.get(i).getPath();
					this.enemyPathPoints.add(this.firstIndexofEachEnemyPath.get(i),path.getWidth());
					this.enemyPathPoints.add(this.firstIndexofEachEnemyPath.get(i) + 1,path.getHeight());
					break;
				case "javafx.scene.shape.Circle":
					this.enemyPathType.add(i, ShapeTypes.RECTANGLE);
					Circle path2 = (Circle) enemies.get(i).getPath();
					this.enemyPathPoints.add(this.firstIndexofEachEnemyPath.get(i),path2.getRadius());
					break;
				case "javafx.scene.shape.Ellipse":
					this.enemyPathType.add(i, ShapeTypes.ELLIPSE);
					Ellipse path3 = (Ellipse) enemies.get(i).getPath();
					this.enemyPathPoints.add(this.firstIndexofEachEnemyPath.get(i),path3.getRadiusX());
					this.enemyPathPoints.add(this.firstIndexofEachEnemyPath.get(i) + 1,path3.getRadiusY());
					break;
				case "javafx.scene.shape.Polyline":
					this.enemyPathType.add(i, ShapeTypes.POLYLINE);
					Polyline path4 = (Polyline) enemies.get(i).getPath();
					for(int n = 0; n < path4.getPoints().size(); n++) {
						this.enemyPathPoints.add(this.firstIndexofEachEnemyPath.get(i) + n, path4.getPoints().get(n));
					}
					break;
				default:
					System.err.println("Falha ao compactar Caminhos dos inimigos");
			}
		}
		
		for(int i = 0; i < wall.size(); i ++) {
			Polyline pol = (Polyline) wall.get(i).getShape();
			int index = 0;
			if(i != 0) {
				Polyline previousPol = (Polyline) wall.get(i - 1).getShape();
				index = firstIndexofEachWall.get(i-1) + previousPol.getPoints().size();
			}
			this.firstIndexofEachWall.add(i,index);
			for(int j = 0; j < pol.getPoints().size(); j++) {
				this.wallPoints.add(j + this.firstIndexofEachWall.get(i), pol.getPoints().get(j));
			}
			Vector2 pos = new Vector2(wall.get(i).getIniX(), wall.get(i).getIniY());
			posWalls.add(i, pos);
		}
		
		for(int i = 0; i < coins.size(); i ++) {
			Vector2 pos = new Vector2(coins.get(i).getIniX(), coins.get(i).getIniY());
			this.posCoins.add(i, pos);
		}
		
		for(int i = 0; i < checkpoints.size(); i++) {
			Vector2 pos = new Vector2 (checkpoints.get(i).getIniX(), checkpoints.get(i).getIniY());
			Shape checkShape = checkpoints.get(i).getShape();
			Bounds checkBounds = checkShape.localToScene(checkShape.getBoundsInLocal());
			Vector2 bounds = new Vector2(checkBounds.getMaxX() - checkBounds.getMinX(), checkBounds.getMaxY() - checkBounds.getMinY());
			this.posCheckpoints.add(i,pos);
			this.checkPointBounds.add(i,bounds);
		}
	}

	public int getLevelNumber() {
		return levelNumber;
	}

	public void setLevelNumber(int levelNumber) {
		this.levelNumber = levelNumber;
	}
	

	public Player getPlayer() {
		Rectangle rect = new Rectangle(30,30);
		return new Player(rect,this.posPlayer.getPosX(), this.posPlayer.getPosY(),this.numberOfDeaths);
	}

	public void setPosPlayer(Vector2 posPlayer) {
		this.posPlayer = posPlayer;
	}

	public ArrayList<Vector2> getPosEnemies() {
		return posEnemies;
	}
	
	public ArrayList<Enemy> getEnemies(){
		Circle circ = new Circle(0,0,15);
		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		double x = 0;
		double y = 0;
		int moveDuration = 0;
		
		for(int i = 0; i < posEnemies.size(); i++) {
			x = posEnemies.get(i).getPosX();
		    y = posEnemies.get(i).getPosY();
		    moveDuration = enemyMoveDuration.get(i);
			circ.setCenterX(x);
			circ.setCenterY(y);
			circ.setFill(Color.DARKBLUE);
			circ.setStroke(Color.BLACK);
			
			Enemy enemy = new Enemy(circ,null,x,y,moveDuration);
			int dataPos = this.firstIndexofEachEnemyPath.get(i);
			switch(this.enemyPathType.get(i)) {
				case ShapeTypes.RECTANGLE:
					Rectangle path1 = new Rectangle(x,y,this.enemyPathPoints.get(dataPos), this.enemyPathPoints.get(dataPos + 1));
					enemy.setPath(path1);
					break;
				case ShapeTypes.CIRCLE:
					Circle path2 = new Circle(x,y,this.enemyPathPoints.get(dataPos));
					enemy.setPath(path2);
					break;
				case ShapeTypes.ELLIPSE:
					Ellipse path3 = new Ellipse(x,y,this.enemyPathPoints.get(dataPos),this.enemyPathPoints.get(dataPos + 1));
					enemy.setPath(path3);
					break;
				case ShapeTypes.POLYLINE:
					double[] path4Points;
					if(i == posEnemies.size() - 1)
						path4Points = new double[this.enemyPathPoints.size() - this.firstIndexofEachEnemyPath.get(i)];
					else
						path4Points = new double[this.firstIndexofEachEnemyPath.get(i+ 1) - this.firstIndexofEachEnemyPath.get(i)];
					
					for(int n = 0; n < path4Points.length; n++) {
						path4Points[n] = this.enemyPathPoints.get(firstIndexofEachEnemyPath.get(i) + n);
					}
					
					Polyline path4 = new Polyline(path4Points);
					enemy.setPath(path4);
					break;
				default:
					System.err.println("Não foi possível resgatar o caminho do inimigo");
					
			}
			
			enemies.add(enemy);
		}
		
		return enemies;
	}

	public void setPosEnemies(ArrayList<Vector2> posEnemies) {
		this.posEnemies = posEnemies;
	}

	public ArrayList<Vector2> getPosWalls() {
		return posWalls;
	}
	
	public ArrayList<Wall> getWalls(){
		ArrayList<Wall> walls = new ArrayList<Wall>();
		ArrayList<Double> points;
		double[] pointAdapter;
		double x = 0;
		double y = 0;
		
		for(int i = 0; i < posWalls.size(); i++) {
			x = posWalls.get(i).getPosX();
		    y = posWalls.get(i).getPosY();
		    
		    points = new ArrayList<Double>();
		    if(i != posWalls.size() - 1) {
		    	for(int j = firstIndexofEachWall.get(i); j < firstIndexofEachWall.get(i+1); j++) {
		    		points.add(j,wallPoints.get(j));
		    	}
		    }
		    else {
		    	for(int j = firstIndexofEachWall.get(i); j < wallPoints.size(); j++) {
		    		points.add(j,wallPoints.get(j));
		    	}
		    }
		    
		    pointAdapter = new double[points.size()];
		    for(int j = 0; j < pointAdapter.length; j++) {
		    	pointAdapter[j] = points.get(j);
		    }
		    
		    Polyline pol = new Polyline(pointAdapter);
		    pol.setStrokeWidth(10);
		    walls.add(new Wall(pol,x,y));
		}
		
		return walls;
	}

	public void setPosWalls(ArrayList<Vector2> posWalls) {
		this.posWalls = posWalls;
	}

	public ArrayList<Double> getWallPoints() {
		return wallPoints;
	}

	public void setWallPoints(ArrayList<Double> wallPoints) {
		this.wallPoints = wallPoints;
	}

	public ArrayList<Integer> getFirstIndexofEachWall() {
		return firstIndexofEachWall;
	}

	public void setFirstIndexofEachWall(ArrayList<Integer> firstIndexofEachWall) {
		this.firstIndexofEachWall = firstIndexofEachWall;
	}

	public ArrayList<Vector2> getPosCoins() {
		return posCoins;
	}
	
	public ArrayList<Coin> getCoins() {
		ArrayList<Coin> coins = new ArrayList<Coin>();
		double x = 0;
		double y = 0;
		
		for(int i = 0; i < posCoins.size(); i++) {
			x = posCoins.get(i).getPosX();
		    y = posCoins.get(i).getPosY();
		    Circle circ = new Circle(x,y,15);
			circ.setFill(Color.GOLD);
			Coin coin = new Coin(circ,x,y);
			coins.add(coin);
		}
		
		return coins;
	}

	public void setPosCoins(ArrayList<Vector2> posCoins) {
		this.posCoins = posCoins;
	}

	public ArrayList<Vector2> getPosCheckpoints() {
		return posCheckpoints;
	}

	public void setPosCheckpoints(ArrayList<Vector2> posCheckpoints) {
		this.posCheckpoints = posCheckpoints;
	}

	public ArrayList<Vector2> getCheckPointBounds() {
		return checkPointBounds;
	}

	public void setCheckPointBounds(ArrayList<Vector2> checkPointBounds) {
		this.checkPointBounds = checkPointBounds;
	}
	
	public ArrayList<CheckPoint> getCheckPoints() {
		ArrayList<CheckPoint> checkpoints = new ArrayList<CheckPoint>();
		CheckPoint check;
		double x = 0;
		double y = 0;
		double w = 0;
		double h = 0;
		
		for (int i = 0; i < posCheckpoints.size(); i++) {
			x = posCheckpoints.get(i).getPosX();
			y = posCheckpoints.get(i).getPosY();
			w = checkPointBounds.get(i).getPosX();
			h = checkPointBounds.get(i).getPosY();
			
			Rectangle rect = new Rectangle(x,y,w,h);
			rect.setFill(Color.DARKSEAGREEN);
			rect.setStroke(Color.DARKGREEN);
			rect.setOpacity(0.5);
			
			if(i == 0) {
				check = new CheckPoint(rect,x,y,false,true);
			}
			else if(i == posCheckpoints.size() - 1) {
				check = new CheckPoint(rect,x,y,true,false);
			}
			else {
				check = new CheckPoint(rect,x,y,false,false);
			}
			Bounds rectBounds = rect.localToScene(rect.getBoundsInLocal());
			checkpoints.add(i,check);
		}
	//	System.out.println(checkpoints.get(0).getShape().getLayoutX() + ", " + checkpoints.get(1).getShape().getLayoutX());
		return checkpoints;
	}

	public Vector2 getPosPlayer() {
		return posPlayer;
	}	
	
}
