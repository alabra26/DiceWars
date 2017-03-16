import java.util.Collection;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This represents the primary class for a game/animation
 * 
 * @author Jeremy Fox
 *
 */
public class DiceWars {
	private Stage myStage;
	private Timeline myAnimation;
	private String myLevel;
	private Scene myScene;
	private Group myRoot;
	private Player myPlayer;
	private Earth myEarth;
	private Score myScore;
	private Collection<GameObject> myBread = new CopyOnWriteArrayList<GameObject>();
	private Collection<GameObject> myBasicEnemies = new CopyOnWriteArrayList<GameObject>();
	private Collection<AdvancedEnemy> myAdvancedEnemies = new CopyOnWriteArrayList<AdvancedEnemy>();
	private Collection<GameObject> myProjectiles = new CopyOnWriteArrayList<GameObject>();
	private Random myRandom = new Random();
	private Timers t = new Timers();
	private BasicEnemy be = new BasicEnemy();
	private AdvancedEnemy ae = new AdvancedEnemy();
	private Bread b = new Bread();
	private Projectile p = new Projectile();

	private static final int TEXT_SIZE = 50;
	private static final int BASIC_ENEMY_SCORE = 20;
	private static final int ADVANCED_ENEMY_SCORE = 40;

	public BreadFirstSearch(String level,Stage stage,Timeline animation) {
		myStage = stage;
		myLevel = level;
		myAnimation = animation;
	}

	/**
	 * Create the game's scene
	 */
	public Scene init(Stage stage, int height, int width) {
		// start the scene graph
		myRoot = new Group();
		//create the player
		myPlayer = new Player(height, width);
		//set the score
		myScore = new Score();
		// add the player to the scene;
		myRoot.getChildren().add(myPlayer);
		myRoot.getChildren().add(myScore.getScore());
		myScene = new Scene(myRoot, width, height, Color.BLACK);
		myScene.setOnKeyPressed(e -> handleKeyInput(e));
		return myScene;

	}

	/**
	 * Create the game's frame
	 */
	public KeyFrame start(int frameRate) {
		return new KeyFrame(Duration.millis(1000 / frameRate), e -> updateSprites());

	}

	/**
	 * What to do each game frame
	 *
	 * Change the sprite properties each frame by a tiny amount to animate them
	 *
	 */
	private int myCounter = 0;

	private void updateSprites() {
		generateObjects();
		moveObjects();
		checkAllCollisions();
		killOffScreenObjects();
		cleanUpDeadSprites();
		myCounter++;
	}

	/**
	 * 
	 */
	public void checkAllCollisions() {
		for (GameObject current: myBasicEnemies) {
			checkCollide(myPlayer,current);
		}
		for (AdvancedEnemy current: myAdvancedEnemies) {
			checkCollide(myPlayer,current);
		}
		for (GameObject currentEnemy: myBasicEnemies) {
			for (GameObject currentProjectile: myProjectiles) {
				checkCollide(currentEnemy,currentProjectile);
			}
		}
		for (AdvancedEnemy currentEnemy: myAdvancedEnemies) {
			for (GameObject currentProjectile: myProjectiles) {
				checkCollide(currentEnemy,currentProjectile);
			}
		}
		for (GameObject currentBread: myBread) {
			checkCollide(currentBread,myPlayer);
		}
		if (myEarth!=null &&myPlayer.getBoundsInParent().intersects(myEarth.getBoundsInParent())) 
			setupButton("Congrats! You won!!","Click to go to Main Menu","This is the Victor's Screen");
	}

	/**
	 * 
	 */
	public void cleanUpDeadSprites() {
		for (GameObject current: myBasicEnemies)
			if (current.isDead()) {
				myRoot.getChildren().remove(current);
				myBasicEnemies.remove(current);
				if (!isOffScreen(current))
					increaseScore(current,BASIC_ENEMY_SCORE);
			}
		for (AdvancedEnemy current: myAdvancedEnemies)
			if (current.isDead()) {
				myRoot.getChildren().remove(current);
				myAdvancedEnemies.remove(current);
				increaseScore(current,ADVANCED_ENEMY_SCORE);
			}
		for (GameObject current: myProjectiles) 
			if (current.isDead()) {
				myRoot.getChildren().remove(current);
				myProjectiles.remove(current);
			}
		for (GameObject current: myBread) 
			if (current.isDead()) {
				myRoot.getChildren().remove(current);
				myBread.remove(current);
			}

		if (myPlayer.isDead()) {
			setupButton("Looks like you failed!","Click to go to Main Menu","You failed");
		}
	}

	/**
	 * @param current
	 */
	public void increaseScore(GameObject current, int score) {
		if (!isOffScreen(current))
			myScore.increaseScore(score);
	}


	/**
	 * 
	 */
	public void setupButton(String textLabel, String buttonLabel, String sceneLabel) {
		myAnimation.pause();
		myRoot.getChildren().clear();
		Text myLabel = new Text(TEXT_SIZE,TEXT_SIZE,textLabel);
		myLabel.setFill(Color.WHITE);
		myLabel.setTranslateX(myScene.getWidth()/2);
		myLabel.setTranslateY(myScene.getHeight()/3);
		myRoot.getChildren().add(myLabel);
		Button btn = new Button();

		btn.setText(buttonLabel);
		btn.setTranslateX(myScene.getWidth()/2);
		btn.setTranslateY(myScene.getHeight()/2);
		btn.setOnAction(new EventHandler<ActionEvent>() {


			public void handle(ActionEvent event) {
				SplashPage mySplashPage = new SplashPage();
				myStage.setTitle(sceneLabel);
				mySplashPage.setup(myStage);
			}

		});
		myRoot.getChildren().add(btn);
	}

	/**
	 * 
	 */
	public void moveObjects() {
		moveUpOrDown(myBasicEnemies,"down");
		moveUpOrDown(myBread,"down");
		for (AdvancedEnemy current: myAdvancedEnemies) {
			current.setDestination(myPlayer.getLocation());
			ae.moveAdvancedEnemy(current);

		}
		moveUpOrDown(myProjectiles, "up");
		moveUpOrDown(myEarth,"down");
	}

	public void moveUpOrDown(Collection<GameObject> myList, String input) {
		if (input.equals("up"))
			for (GameObject current: myList)
				current.setCenterY(current.getCenterY() - current.getSpeed());
		else if (input.equals("down"))
			for (GameObject current: myList) 
				current.setCenterY(current.getCenterY() + current.getSpeed());
	}

	public void moveUpOrDown(GameObject myObject, String input) {
		if (myObject!=null && input.equals("up"))
			myObject.setCenterY(myObject.getCenterY() - myObject.getSpeed());
		else if (myObject!=null && input.equals("down"))
			myObject.setCenterY(myObject.getCenterY() + myObject.getSpeed());
	}


	/**
	 * 
	 */
	public void generateObjects() {
		if (t.isTimeForEarth(myCounter))
			generateEarth();
		if (myEarth==null) {
			if (t.isTimeForEnemy(myCounter)) 
				if (myLevel.equals("Level One")) 
					be.generate(myBasicEnemies, myRoot, myScene);

				else
					generateAdvancedEnemy();
			if (t.isTimeForBread(myCounter,myRandom))
				b.generate(myBread, myRoot, myScene);
		}
	}


	/**
	 * What to do each time a key is pressed
	 */
	private void handleKeyInput (KeyEvent e) {
		KeyCode keyCode = e.getCode();
		if (keyCode == KeyCode.RIGHT) {
			myPlayer.setCenterX( myPlayer.getCenterX() + myPlayer.getSpeed());
		}
		else if (keyCode == KeyCode.LEFT) {
			myPlayer.setCenterX( myPlayer.getCenterX() - myPlayer.getSpeed());
		}
		else if (keyCode == KeyCode.SPACE) {
			p.generate(myProjectiles, myRoot, myScene,myPlayer);
		}
		else if (keyCode == KeyCode.SHIFT) {
			myPlayer.toggleInvincibility();
		}
		else if (keyCode == KeyCode.TAB) {
			killAllEnemies();
		}

	}



	/**
	 * What to do each time shapes collide
	 */
	private void checkCollide (GameObject first, GameObject second) {
		if (first.getBoundsInParent().intersects(second.getBoundsInParent())) {
			if (first instanceof Bread) {
				triggerBreadEffect();
				first.kill();
			}
			else {
				if (!first.isInvincible())
					first.kill();
				if (!second.isInvincible())
					second.kill();
			}
		}

	}

	public void triggerBreadEffect() {
		killAllEnemies();
	}

	public void generateEarth() {
		myEarth = new Earth((int) myScene.getWidth(),(int) myScene.getHeight());
		myRoot.getChildren().add(myEarth);
	}


	public void generateAdvancedEnemy() {
		Point2D tempPoint = new Point2D((double)generateRandom((int) myScene.getWidth()),0);
		AdvancedEnemy tempEnemy = new AdvancedEnemy(tempPoint);
		myRoot.getChildren().add(tempEnemy);
		myAdvancedEnemies.add(tempEnemy);
	}

	public int generateRandom(int input) {
		return myRandom.nextInt(input) + 1;
	}

	public void killOffScreenObjects() {
		killOffScreenList(myBasicEnemies);
		killOffScreenList(myProjectiles);
		killOffScreenList(myBread);
		for (AdvancedEnemy current: myAdvancedEnemies)
			if (isOffScreen(current))
				current.kill();
	}

	/**
	 * 
	 */
	public void killOffScreenList(Collection<GameObject> myList) {
		for (GameObject current: myList)
			if (isOffScreen(current))
				current.kill();
	}

	public boolean isOffScreen(GameObject input) {
		return input.getCenterY() > myScene.getHeight() || input.getCenterY() < 0
				|| input.getCenterX() > myScene.getWidth() || input.getCenterX() < 0;
	}

	public void killAllEnemies() {
		killAllBasic(myBasicEnemies);
		killAllAdvanced(myAdvancedEnemies);
	}

	public void killAllBasic(Collection<GameObject> myCollection) {
		for (GameObject current:myCollection)
			current.kill();
	}

	public void killAllAdvanced(Collection<AdvancedEnemy> myCollection) {
		for (AdvancedEnemy current: myCollection) 
			current.kill();

	}

}
