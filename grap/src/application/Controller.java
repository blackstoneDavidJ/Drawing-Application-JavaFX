package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Controller implements Initializable
{
	
	@FXML
	private RadioButton rectangle;
	@FXML
	private RadioButton circle;
	@FXML
	private RadioButton triangle;
	@FXML
	private Button create;
	@FXML
	private Pane drawCanvas;
	@FXML
	private ColorPicker colorPicker;
	private ArrayList<Node> shapes = new ArrayList<>();
	private Color colorSelected;
	private Shapes shapeToDraw;
	private double mouseAnchorX;
	private double mouseAnchorY;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		System.out.println("Ready to Draw!");
		colorPicker.setValue(Color.BLACK);
		colorPicker.setOnAction(new EventHandler() 
		{
			@Override
			public void handle(Event arg0) 
			{
				colorSelected = colorPicker.getValue();			
			}		
		});
	}
	
	public void setShape()
	{
		if(rectangle.isSelected()) 
		{
			shapeToDraw = Shapes.RECTANGLE;
		}
		
		if(circle.isSelected()) 
		{
			shapeToDraw = Shapes.CIRCLE;
		}
		
		if(triangle.isSelected()) 
		{
			shapeToDraw = Shapes.TRIANGLE;
		}
		
	}

	public void createShape()
	{
		if(shapeToDraw == Shapes.CIRCLE)
		{
			Circle circle = new Circle(drawCanvas.getWidth()/2,drawCanvas.getHeight()/2, 100);
			circle.setFill(colorSelected);
			shapes.add(circle);
			drawCanvas.getChildren().add(circle);
			circle.setOnMousePressed(e -> 
			{
				if(e.isPrimaryButtonDown())
				{
					mouseAnchorX = e.getX();
					mouseAnchorY = e.getY();
				}
				
				if(e.isSecondaryButtonDown())
				{
					drawCanvas.getChildren().remove(circle);
				}
			});
			
			circle.setOnMouseDragged(e -> 
			{
				if(e.isPrimaryButtonDown())
				{
					circle.setLayoutX(e.getSceneX() - mouseAnchorX);
					circle.setLayoutY(e.getSceneY() - mouseAnchorY);
				}
			});
		}
		
		if(shapeToDraw == Shapes.RECTANGLE)
		{
			Rectangle rec = new Rectangle(drawCanvas.getWidth()/2, drawCanvas.getHeight()/2, 100,100);
			rec.setFill(colorSelected);
			drawCanvas.getChildren().add(rec);
			rec.setOnMousePressed(e -> 
			{
				if(e.isPrimaryButtonDown())
				{
					mouseAnchorX = e.getX();
					mouseAnchorY = e.getY();
				}
				
				if(e.isSecondaryButtonDown())
				{
					drawCanvas.getChildren().remove(rec);
				}
			});
			
			rec.setOnMouseDragged(e -> 
			{
				if(e.isPrimaryButtonDown())
				{
					rec.setLayoutX(e.getSceneX() - mouseAnchorX);
					rec.setLayoutY(e.getSceneY() - mouseAnchorY);
				}
			});
		}
		
		if(shapeToDraw == Shapes.TRIANGLE)
		{
			/*GraphicsContext gc = drawCanvas.getGraphicsContext2D();
			gc.setFill(colorSelected);
			gc.fillPolygon(new double[]{(drawCanvas.getWidth()/2)-75, (drawCanvas.getWidth()/2), (drawCanvas.getWidth()/2)+75},
					new double[]{(drawCanvas.getHeight()/2)+90, drawCanvas.getHeight()/2, (drawCanvas.getHeight()/2)+90}, 3);*/
		}
	}
	
	public enum Shapes
	{
		NONE,
		CIRCLE,
		RECTANGLE,
		TRIANGLE;
	}
}
