import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	public void start(Stage stage) {
		Board brd = new Board(45, 30);

		System.out.println(brd.countries[4]);
		System.out.println(brd.countries[4].neighbors);
		
		brd.display();

		stage.setTitle("Welcome to DiceWars");
		SplashPage mySplashPage = new SplashPage();
		mySplashPage.setup(stage);
	}

	public static void main(String[] args) {
		
		launch(args);

	}
}