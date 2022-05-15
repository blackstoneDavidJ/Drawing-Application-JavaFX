package application;
	
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application 
{
	public void start(Stage primaryStage) throws IOException 
	{	
		Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
		primaryStage.setTitle("Drawing Application");
		primaryStage.setScene(new Scene(root, 1280, 720));
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
}

//DatePicker datepicker = new DatePicker();
		//Button button = new Button("Read Date");
		/*button.setOnAction(action -> {
			LocalDate val = datepicker.getValue();
			System.out.println(val.toString());
		});
		*/
		//HBox hbox = new HBox(datepicker);
		
		
		//color picker
		//Scene scene = new Scene(hbox, 300,240);
		/*ColorPicker cp = new ColorPicker();
		Color value = cp.getValue();
		HBox vb = new HBox(cp);
		Scene scene = new Scene(vb, 960,600);
		primaryStage.setScene(scene);
		primaryStage.show();
		System.out.println(value.toString());*/
		
		/*Slider slider = new Slider(0,100,0);
		slider.setMajorTickUnit(10.0);
		slider.setMinorTickCount(4);
		slider.setSnapToTicks(true);
		slider.setShowTickMarks(true);
		slider.setShowTickLabels(true);
		VBox vb = new VBox(slider);
		Scene scene = new Scene(vb,960,600);*/
		
		/*ToolBar toolbar = new ToolBar();
		for(int i = 1; i < 20; i++)
		{
			toolbar.getItems().add(new Button("Button-"+i));
		}

		toolbar.setOrientation(Orientation.VERTICAL);
		VBox vb = new VBox(toolbar);
		Scene scene = new Scene(vb, 960,600);*/
		
		/*ProgressBar progressBar = new ProgressBar(0);
		progressBar.setProgress(0.5);
		VBox vb = new VBox(progressBar);
		Scene scene = new Scene(vb, 960, 600);
		*/
		
		/*FileChooser fc = new FileChooser();
		fc.getExtensionFilters().addAll(
			     new FileChooser.ExtensionFilter("Text Files", "*.txt")
			);
		Button btn = new Button("Select a File");
		btn.setOnAction(e -> {
			File selectedFile = fc.showOpenDialog(primaryStage);
		});
		
		VBox vb = new VBox(btn);
		Scene scene = new Scene(vb,960,600);
		*/
