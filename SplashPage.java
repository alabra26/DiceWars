import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * This is the splash page screen, which doubles as a menu for when you win or lose
 * @author Jeremy
 *
 */

public class SplashPage {
	private static final int NUM_FRAMES_PER_SECOND = 60;
	private static final int STAGE_WIDTH = 500;
	private static final int STAGE_HEIGHT = 500;
	private static final int TRANSLATE_X = 70;
	private static final int TRANSLATE_Y_ONE = 150;
	private static final int TRANSLATE_Y_TWO = 250;
	private static final int TRANSLATE_TEXT_X = 100;
	private static final int TRANSLATE_TEXT_Y = 50;
	private static final int TEXT_SIZE = 50;
	private Stage myStage;
	private DiceWars myGame;
	public void setup(Stage stage) {
		Group root = new Group();
		myStage = stage;
		setupText(root);
		setupButton(stage,"Multiplayer","Level One",TRANSLATE_Y_ONE,root);
		setupButton(stage,"Singleplayer","Level Two",TRANSLATE_Y_TWO,root);
		Scene scene = new Scene(root, STAGE_WIDTH, STAGE_HEIGHT);
		stage.setTitle("DICEWARS");
		stage.setScene(scene);
		stage.show();
	}
	/**
	 * @return
	 */
	public void setupText(Group root) {
		Text myLabel = new Text(TEXT_SIZE,TEXT_SIZE,"Prepare to play DICEWARS");
		myLabel.setTranslateX(TRANSLATE_TEXT_X);
		myLabel.setTranslateY(TRANSLATE_TEXT_Y);
		root.getChildren().add(myLabel);
	}
	/**
	 * @param stage
	 * @return
	 */
	public void setupButton(Stage stage, String buttonText,String level,int yCoord, Group root) {
		Button btn = new Button();
		btn.setText(buttonText);
		btn.setTranslateX(TRANSLATE_X);
		btn.setTranslateY(yCoord);
		btn.setOnAction(new EventHandler<ActionEvent>() {


			public void handle(ActionEvent event) {
				loadBreadFirstSearch(stage,level);
			}
		});
		root.getChildren().add(btn);
	}
	public void loadBreadFirstSearch(Stage stage, String level) {
		stage.setTitle("Bread First Search");

		Timeline animation = new Timeline();
		//create the game
		myGame = new DiceWars(level,stage,animation);
		Scene scene = myGame.init(stage, STAGE_WIDTH, STAGE_HEIGHT);
		stage.setScene(scene);
		//stage.show();

		//setup the game's loop
		KeyFrame frame = myGame.start(NUM_FRAMES_PER_SECOND);
		animation.setCycleCount(Animation.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
	}
}
