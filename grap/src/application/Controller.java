package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Controller implements Initializable
{
	@FXML
	private RadioButton rectangle, circle;
	@FXML
	private RadioButton drawPencil, eraserPencil, noTool;
	@FXML
	private RadioButton triangle;
	@FXML
	private Button create;
	@FXML
	private Pane drawCanvas;
	@FXML
	private ColorPicker colorPicker;
	@FXML
	private Label lengthLabel, heightLabel, CircumferenceLabel;
	@FXML
	private TextField length, height, circumference;
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
		colorSelected = colorPicker.getValue();
		drawCanvas.setLayoutX(drawCanvas.getWidth());
		drawCanvas.setLayoutY(drawCanvas.getHeight());
		colorPicker.setOnAction(new EventHandler() 
		{
			@Override
			public void handle(Event arg0) 
			{
				colorSelected = colorPicker.getValue();			
			}		
		});
	}
	
	public void pencilTool()
	{
		if(drawPencil.isSelected())
		{
			drawCanvas.setCursor(Cursor.CROSSHAIR);
			Canvas canvas = new Canvas(drawCanvas.getWidth(), drawCanvas.getHeight());
	        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
	        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, 
	                new EventHandler<MouseEvent>(){

	            @Override
	            public void handle(MouseEvent event) {
	            	graphicsContext.setFill(colorSelected);
	            	graphicsContext.setLineWidth(10);
	                graphicsContext.beginPath();
	                graphicsContext.moveTo(event.getX(), event.getY());
	                graphicsContext.stroke();
	            }
	        });

	        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
	                new EventHandler<MouseEvent>(){

	            @Override
	            public void handle(MouseEvent event) {
	                graphicsContext.lineTo(event.getX(), event.getY());
	                graphicsContext.stroke();
	            }
	        });

	        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, 
	                new EventHandler<MouseEvent>(){

	            @Override
	            public void handle(MouseEvent event) {

	            }
	        });
	        drawCanvas.getChildren().add(canvas);
		}
		
		if(eraserPencil.isSelected())
		{
			drawCanvas.setCursor(Cursor.CROSSHAIR);
		}
		
		if(noTool.isSelected())
		{
			drawCanvas.setCursor(Cursor.DEFAULT);
		}
	}
	
	public void setShape()
	{
		drawCanvas.setCursor(Cursor.DEFAULT);
		if(rectangle.isSelected()) 
		{
			shapeToDraw = Shapes.RECTANGLE;
			if(!length.isVisible())
			{
				length.setVisible(true);
				height.setVisible(true);
				lengthLabel.setVisible(true);
				heightLabel.setVisible(true);
			}
			CircumferenceLabel.setVisible(false);
			circumference.setVisible(false);
		}
		
		if(circle.isSelected()) 
		{
			shapeToDraw = Shapes.CIRCLE;
			if(!CircumferenceLabel.isVisible())
			{
				CircumferenceLabel.setVisible(true);
				circumference.setVisible(true);
			}
			length.setVisible(false);
			height.setVisible(false);
			lengthLabel.setVisible(false);
			heightLabel.setVisible(false);
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
			Circle circle = new Circle(drawCanvas.getWidth()/2,drawCanvas.getHeight()/2,
					Double.valueOf(circumference.getText().toString()));
			circle.setFill(colorSelected);
			shapes.add(circle);
			drawCanvas.getChildren().add(circle);
			circle.setOnMousePressed(e -> 
			{
				drawCanvas.setCursor(Cursor.OPEN_HAND);
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
					circle.setCenterX(e.getX());
					circle.setCenterY(e.getY());
					circle.setTranslateX(0);
					circle.setTranslateY(0);
				}
			});
		}
		
		if(shapeToDraw == Shapes.RECTANGLE)
		{
			Rectangle rec = new Rectangle(drawCanvas.getWidth()/2, drawCanvas.getHeight()/2, Double.valueOf(length.getText().toString()),
					Double.valueOf(height.getText().toString()));
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
			class Delta  { double x, y;}
			final Delta dragDelta = new Delta();

	        rec.setOnMousePressed(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent mouseEvent) {
	                // record a delta distance for the drag and drop operation.
	                dragDelta.x = rec.getTranslateX() - mouseEvent.getSceneX();
	                dragDelta.y = rec.getTranslateY() - mouseEvent.getSceneY();
	                rec.setCursor(Cursor.OPEN_HAND);
	            }
	        });
	        rec.setOnMouseReleased(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent mouseEvent) {
	            }
	        });
	        rec.setOnMouseDragged(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent mouseEvent) {

	                rec.setTranslateX(mouseEvent.getSceneX() + dragDelta.x);
	                rec.setTranslateY(mouseEvent.getSceneY() + dragDelta.y);
	                //checkBounds(rec);

	            }
	        });
		}
		
		if(shapeToDraw == Shapes.TRIANGLE)
		{
			
		}
	}
	
	public enum Shapes
	{
		NONE,
		CIRCLE,
		RECTANGLE,
		TRIANGLE;
	}
	
	public enum Tools
	{
		DRAWING,
		ERASER;
	}
}
