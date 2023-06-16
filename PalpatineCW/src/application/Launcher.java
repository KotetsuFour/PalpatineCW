package application;

public class Launcher {

	public static void main(String[] args) {
		String[] input = new String[4];
		input[0] = "--module-path";
		input[1] = "\"C:/Users/Avery Hawkins/Documents/JavaFX/javafx-sdk-17.0.2/lib\"";
		input[2] = "--add-modules";
		input[3] = "javafx.controls,javafx.fxml";
		Main.main(input);
	}

}
