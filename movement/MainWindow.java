package movement;

import java.io.*;
import javax.imageio.ImageIO;
import javafx.geometry.*;
import javafx.application.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

/**
 * @version 1.6
 */

public class MainWindow extends Application {

	static ImageView picture;
	ToolsStage toolsStage;
	Pane toolsPane;
	GridTool grid = new GridTool();
	Layers layers = new Layers();
	Pane mainRoot;
	final FileChooser fileChooser = new FileChooser();

	public void start(Stage mainStage) {
		
		mainStage.setTitle("BIOpix");

		// set bounds
		mainStage.setX(Images.primaryScreenBounds.getMinX());
		mainStage.setY(Images.primaryScreenBounds.getMinY());
		mainStage.setWidth(Images.primaryScreenBounds.getWidth());
		mainStage.setHeight(Images.primaryScreenBounds.getHeight());

		// create loading image Scene
		FlowPane fileChooseRoot = new FlowPane();
		fileChooseRoot.setStyle("-fx-background: #101010;");
		fileChooseRoot.setAlignment(Pos.CENTER);
		
		fileChooseRoot.getChildren().add(Images.upload);
		Scene mainScene = new Scene(fileChooseRoot, Images.primaryScreenBounds.getWidth(),
				Images.primaryScreenBounds.getHeight());
		mainScene.setFill(Color.web("0x101010"));
		mainStage.setScene(mainScene);

		// create main Root
		mainRoot = new Pane();
		mainRoot.setLayoutX(0);
		mainRoot.setLayoutY(0);
		mainRoot.setStyle("-fx-background: #101010;");
		mainRoot.setMinWidth(Images.primaryScreenBounds.getWidth());
		mainRoot.setMinHeight(Images.primaryScreenBounds.getHeight());

		// Tools root set up
		toolsPane = new Pane();
		toolsPane.setMinWidth(Images.primaryScreenBounds.getWidth());
		toolsPane.setMinHeight(Images.primaryScreenBounds.getHeight());
		toolsPane.getChildren().add(grid.getCanvas());

		mainRoot.getChildren().add(toolsPane);
		toolsPane.setLayoutX(0 - toolsPane.getLayoutBounds().getMinX());
		toolsPane.setLayoutY(0 - toolsPane.getLayoutBounds().getMinY());

		mainStage.show();
		mainStage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});

		buildTools();

		// File Choose set up
		fileChooseRoot.setOnMouseClicked(e -> {
			configureFileChooser(fileChooser);
			File file = fileChooser.showOpenDialog(mainStage);
			if (file != null) {
				picture = new ImageView(new Image("file:" + file.getPath()));

				mainRoot.getChildren().add(picture);
				toolsPane.toFront();
				picture.setPreserveRatio(true);
				picture.setFitWidth(Images.primaryScreenBounds.getWidth());
				picture.setFitHeight(Images.primaryScreenBounds.getHeight());
				picture.autosize();

				picture.setLayoutX((mainRoot.getMinWidth() / 2) - (picture.getBoundsInParent().getWidth() / 2));
				picture.setLayoutY((mainRoot.getMinHeight() / 2) - (picture.getBoundsInParent().getHeight() / 2));
				mainScene.setRoot(mainRoot);
				toolsStage.getStage().show();
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void buildTools() {
		toolsStage = new ToolsStage(this);
		toolsStage.delBox.add("");
		toolsStage.delBox.removeLast();
	}

	private static void configureFileChooser(final FileChooser fileChooser) {
		fileChooser.setTitle("Upload an image");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG", "*.png"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"));
	}
	
	void savePNG(final FileChooser fileChooser, Node root)
	{
		 //Set extension filter
	    fileChooser.setTitle("Save Image As");

	    //Prompt user to select a file
	    File file = fileChooser.showSaveDialog(null);
	    
	    WritableImage image = 
	            new WritableImage((int)root.getScene().getWidth(), (int)root.getScene().getHeight());
	        root.snapshot(new SnapshotParameters(),image);
	    
	    if(file != null){
	    	try {
	            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
	        } catch (IOException ex) { ex.printStackTrace(); }
	    }
	}

}