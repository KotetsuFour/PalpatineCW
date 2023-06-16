package application;
	
import java.io.FileInputStream;

import javafx.application.Application;
import javafx.stage.Stage;
import manager.PCWManager;
import people.Person;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;


public class Main extends Application {
	
	public static Group ROOT;
	public static Scene SCENE;
	public static Stage STAGE;
	public static PCWManager manager;
	
	@Override
	public void start(Stage primaryStage) {
		ROOT = new Group();
		SCENE = new Scene(ROOT, 1200, 600);
		STAGE = primaryStage;
		STAGE.setScene(SCENE);
		STAGE.show();
		manager = new PCWManager();
//		STAGE.setFullScreen(true);
		politicianPage(manager.getPalpatine(), null);
	}
	
	public void politicianPage(Person p, Person back) {
		ROOT.getChildren().clear();
		Group leftPart = new Group();
		Group middlePart = new Group();
		Group rightPart = new Group();
		middlePart.setTranslateX(500);
		rightPart.setTranslateX(900);
		ROOT.getChildren().add(leftPart);
		ROOT.getChildren().add(middlePart);
		ROOT.getChildren().add(rightPart);
		try {
			Image im = new Image(new FileInputStream("portrait/" + p.imageName() + ".jpg"),
					300, 200, false, false);
			ImageView view = new ImageView(im);
//			Rectangle view = new Rectangle(300, 200);
			view.setTranslateY(100);
			rightPart.getChildren().add(view);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
