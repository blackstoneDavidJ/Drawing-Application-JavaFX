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
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;

public class Controller implements Initializable
{
	@FXML
	private RadioButton rectangle, circle;
	@FXML
	private CheckBox collisions;
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
	private Tools shapeToDraw;
	private double mouseAnchorX;
	private double mouseAnchorY;
	private static final int BACKGROUND_WIDTH_CENTER = 1080/2;
	private static final int BACKGROUND_HEIGHT_CENTER = 718/2;
	
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
		adjustWindow();
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
		DropShadow drop = new DropShadow();
		drop.setColor(Color.BLACK);
		drop.setBlurType(BlurType.GAUSSIAN);
		drop.setOffsetX(5);
		drop.setOffsetY(5);
		drop.setRadius(2);
		drawCanvas.setEffect(drop);
		drawCanvas.setLayoutX(BACKGROUND_WIDTH_CENTER-(windowL/2));
		drawCanvas.setLayoutY(BACKGROUND_HEIGHT_CENTER-(windowH/2));
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
		
	}
	
	public void setTool()
	{
		drawCanvas.setCursor(Cursor.DEFAULT);
		if(rectangle.isSelected()) 
		{
			shapeToDraw = Tools.RECTANGLE;
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
		
		else if(circle.isSelected()) 
		{
			shapeToDraw = Tools.CIRCLE;
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
		
		else if(triangle.isSelected()) 
		{
			shapeToDraw = Tools.TRIANGLE;
		}		
	}

	public void createShape()
	{
		if(shapeToDraw == Tools.CIRCLE)
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
					if(collisions.isSelected())
					{
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
				}
			});
		}
		
		if(shapeToDraw == Tools.RECTANGLE)
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
	            }
	        });
		}
		
		if(shapeToDraw == Tools.TRIANGLE)
		{
			Point2D center = new Point2D(drawCanvas.getWidth()/2, drawCanvas.getHeight()/2);
			Polygon triangle = createTriangle(center,10,90);
			drawCanvas.getChildren().add(triangle);
			nodesList.add(triangle);
		}
		
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
	            public void handle(MouseEvent event) 
	            {	
	            	checkPencilCollisions(graphicsContext, event);         
	            }

				private void checkPencilCollisions(GraphicsContext graphicsContext, MouseEvent event)
				{
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
	
	private Polygon createTriangle(Point2D origin, double length, double angle)
	{
	    Polygon fovTriangle = new Polygon(0d, 0d,(length * Math.tan(angle)), -length,
	            -(length * Math.tan(angle)), -length
	    );
	    
	    fovTriangle.setLayoutX(origin.getX());
	    fovTriangle.setLayoutY(origin.getY());
	    fovTriangle.setFill(colorSelected);
	    return fovTriangle;
	}
	
	class Delta  { double x, y;}
	
	private enum Tools
	{
		NONE,
		CIRCLE,
		RECTANGLE,
		TRIANGLE,
		PENCIL,
		ERASER;
	}
}
