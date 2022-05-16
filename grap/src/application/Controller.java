package application;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;

public class Controller implements Initializable
{
	@FXML
	private RadioButton rectangle, circle;
	@FXML
	private RadioButton drawPencil, eraserPencil, noTool;
	@FXML
	private RadioButton triangle;
	@FXML
	private Button create, clearScreen, saveButton, windowAdjust;
	@FXML
	private Pane drawCanvas, backgroundCanvas;
	@FXML
	private ColorPicker colorPicker;
	@FXML
	private Label lengthLabel, heightLabel, CircumferenceLabel;
	@FXML
	private TextField length, height, circumference, pencilThickness, windowLength, windowHeight;
	private ArrayList<Node> nodesList = new ArrayList<>();
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
		drawCanvas.setStyle("-fx-background-color: #FFFFFF");
		backgroundCanvas.setStyle("-fx-background-color: #808080");
		colorPicker.setOnAction(new EventHandler() 
		{
			@Override
			public void handle(Event arg0) 
			{
				colorSelected = colorPicker.getValue();					
			}		
		});
		
	}
	
	public void adjustWindow()
	{
		int windowL = Integer.valueOf(windowLength.getText().toString());
		int windowH = Integer.valueOf(windowHeight.getText().toString());
		
		drawCanvas.setLayoutX((backgroundCanvas.getWidth()/2)-(windowL/2));
		drawCanvas.setLayoutY((backgroundCanvas.getHeight()/2)-(windowH/2));
		drawCanvas.setPrefWidth(windowL);
		drawCanvas.setPrefHeight(windowH);
		drawCanvas.setMaxWidth(windowL);
		drawCanvas.setMaxHeight(windowH);
	}
	
	public void saveToImage()
	{
		FileChooser fileChooser = new FileChooser();

        //Set extension filter
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));

        //Prompt user to select a file
        File f = fileChooser.showSaveDialog(null);

        if(f != null){
            try {
                //Pad the capture area
                WritableImage writableImage = new WritableImage((int) drawCanvas.getWidth(), (int) drawCanvas.getHeight());
                drawCanvas.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                //Write the snapshot to the chosen file
                ImageIO.write(renderedImage, "png", f);
            } catch (IOException ex)
            { ex.printStackTrace(); }

        }
	}
	
	public void clearScreen()
	{
		for(Node node : nodesList)
		{
			drawCanvas.getChildren().remove(node);
		}
		nodesList.clear();
	}
	
	public void pencilTool()
	{
		Canvas canvas = new Canvas(drawCanvas.getWidth(), drawCanvas.getHeight());
		if(drawPencil.isSelected())
		{
			drawCanvas.setCursor(Cursor.CROSSHAIR);
			
	        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
			System.out.println("color: " +colorSelected.toString());   
	        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, 
	                new EventHandler<MouseEvent>(){
	        		
	            @Override
	            public void handle(MouseEvent event) {
	            	int size = Integer.valueOf(pencilThickness.getText().toString());
	            	graphicsContext.setLineWidth(size);
	                graphicsContext.beginPath();
	                graphicsContext.moveTo(event.getX(), event.getY());
	                graphicsContext.setStroke(colorSelected);
	                graphicsContext.setLineCap(StrokeLineCap.ROUND);
	            }
	        });

	        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
	                new EventHandler<MouseEvent>(){

	            @Override
	            public void handle(MouseEvent event) {
	            	
	            	//handle Pencil Collisions with border
	                graphicsContext.lineTo(event.getX(), event.getY());
	                if(event.getX()> canvas.getWidth())
	                {
	                	graphicsContext.lineTo(canvas.getWidth()-graphicsContext.getLineWidth()/2,
	                			event.getY());
	                }
	                
	                if(event.getY() > canvas.getHeight())
	                {
	                	graphicsContext.lineTo(event.getX(),
	                			canvas.getHeight()-graphicsContext.getLineWidth()/2);
	                }
	                if(event.getX() < 0)
	                {
	                	graphicsContext.lineTo(0+graphicsContext.getLineWidth()/2,
	                			event.getY());
	                }
	                if(event.getY() < 0)
	                {
	                	graphicsContext.lineTo(event.getX(),
	                			0+graphicsContext.getLineWidth()/2);
	                }
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
	        nodesList.add(canvas);
		}
		
		if(eraserPencil.isSelected())
		{
			drawCanvas.setCursor(Cursor.CROSSHAIR);
			
	        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
			System.out.println("color: " +colorSelected.toString());   
	        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, 
	                new EventHandler<MouseEvent>(){
	        		
	            @Override
	            public void handle(MouseEvent event) {
	            	graphicsContext.setLineWidth(Integer.valueOf(pencilThickness.getText().toString()));
	                graphicsContext.beginPath();
	                graphicsContext.moveTo(event.getX(), event.getY());
	                graphicsContext.setStroke(Color.WHITE);
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
	        nodesList.add(canvas);
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
			nodesList.add(circle);
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
					nodesList.remove(circle);
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
					
					//Circle collisions with screen
					if(circle.getCenterX()+circle.getRadius() > drawCanvas.getWidth())
					{
						circle.setCenterX(drawCanvas.getWidth()-circle.getRadius());
					}
					
					if(circle.getCenterX()-circle.getRadius() < 0)
					{
						circle.setCenterX(0+circle.getRadius());
					}
					
					if(circle.getCenterY()+circle.getRadius() > drawCanvas.getHeight())
					{
						circle.setCenterY(drawCanvas.getHeight()-circle.getRadius());
					}
					
					if(circle.getCenterY()-circle.getRadius() < 0)
					{
						circle.setCenterY(0+circle.getRadius());
					}
				}
			});
		}
		
		if(shapeToDraw == Shapes.RECTANGLE)
		{
			Rectangle rec = new Rectangle(drawCanvas.getWidth()/2, drawCanvas.getHeight()/2, Double.valueOf(length.getText().toString()),
					Double.valueOf(height.getText().toString()));
			rec.setFill(colorSelected);
			drawCanvas.getChildren().add(rec);	
			nodesList.add(rec);
			final Delta dragDelta = new Delta();
	        rec.setOnMousePressed(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent mouseEvent) 
	            {
	            	if(mouseEvent.isPrimaryButtonDown())
	            	{
	            		// record a delta distance for the drag and drop operation.
	            		dragDelta.x = rec.getTranslateX() - mouseEvent.getSceneX();
	            		dragDelta.y = rec.getTranslateY() - mouseEvent.getSceneY();
	            		rec.setCursor(Cursor.OPEN_HAND);
	            	}
	            	
	            	else
	            	{
	            		drawCanvas.getChildren().remove(rec);
	            		nodesList.remove(rec);
	            	}
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
	                if(rec.getX() +rec.getWidth() > drawCanvas.getWidth())
	                {
	                	rec.setX(mouseAnchorX);
	                }
	                
	                if(rec.getY() + rec.getHeight() > drawCanvas.getHeight())
	                {
	                	rec.setTranslateX(drawCanvas.getHeight()-rec.getHeight());
	                }
	                //checkBounds(rec);

	            }
	        });
		}
		
		if(shapeToDraw == Shapes.TRIANGLE)
		{
			
		}
	}
	
	class Delta  { double x, y;}
	
	public enum Shapes
	{
		NONE,
		CIRCLE,
		RECTANGLE,
		TRIANGLE;
	}
}
