package com.cromewell.paint;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;


public class Main extends Application {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 500;
    private BorderPane root;
    private Color curColor = Color.BLACK;
    private int penWidth = 4; //default
    private double oldX;
    private double oldY;
    private DrawMode drawMode = DrawMode.PEN; //default

    @Override
    public void start(Stage primaryStage) throws Exception{
        root = new BorderPane();
        root.setStyle("-fx-background-color: #FFFFFF");
        primaryStage.setTitle("Paint");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.setX(Screen.getPrimary().getBounds().getWidth()/2-WIDTH/2);
        primaryStage.setY(Screen.getPrimary().getBounds().getHeight()/2-HEIGHT/2);

        initListeners();
        initSettingsWindow();

        primaryStage.show();
    }

    private void initSettingsWindow() {
        Stage stage = new Stage();
        VBox vbox = new VBox(10);
        initComponents(vbox, stage);
        stage.setTitle("Settings");
        stage.setX(Screen.getPrimary().getBounds().getWidth()/2+WIDTH/2);
        stage.setY(Screen.getPrimary().getBounds().getHeight()/2-HEIGHT/2);
        stage.setScene(new Scene(vbox, 200, 400));

        stage.show();
    }

    private void initComponents(VBox vbox, Stage stage) {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem loadFile = new MenuItem("Load existing picture...");
        MenuItem saveFile = new MenuItem("Save file...");
        fileMenu.getItems().addAll(loadFile, saveFile);
        menuBar.getMenus().add(fileMenu);
        vbox.getChildren().add(menuBar);

        loadFile.setOnAction(e->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open picture...");
            File picFile = fileChooser.showOpenDialog(stage);
            Picture pic = new Picture(Utils.loadPicture(picFile));
            List<Node> nodes = pic.getNodes();
            for(Node n: nodes){
                root.getChildren().add(n);
            }
        });

        saveFile.setOnAction(e->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save picture...");
            File pic = fileChooser.showSaveDialog(stage);
            List<Node> nodes = root.getChildren();
            Utils.savePicture(pic, nodes);
        });

        ColorPicker cp = new ColorPicker();
        cp.setTooltip(new Tooltip("Line color"));
        cp.setValue(Color.BLACK);
        cp.setOnAction(e-> curColor = cp.getValue());
        vbox.getChildren().add(cp);

        ChoiceBox<Integer> cb = new ChoiceBox(FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10));
        cb.setTooltip(new Tooltip("Line width"));
        cb.setValue(4);
        cb.setOnAction(e-> penWidth = cb.getValue());
        vbox.getChildren().add(cb);

        ImageView penView = new ImageView("com/cromewell/paint/res/imgs/pen.png");
        penView.setFitHeight(30);
        penView.setFitWidth(30);
        Button penButton = new Button("", penView);
        ImageView circleView = new ImageView("com/cromewell/paint/res/imgs/circle.png");
        circleView.setFitHeight(30);
        circleView.setFitWidth(30);
        Button circleButton = new Button("", circleView);
        ImageView rectangleView = new ImageView("com/cromewell/paint/res/imgs/rectangle.png");
        rectangleView.setFitHeight(30);
        rectangleView.setFitWidth(30);
        Button rectangleButton = new Button("", rectangleView);
        ImageView circlePenView = new ImageView("com/cromewell/paint/res/imgs/circlePen.png");
        circlePenView.setFitHeight(30);
        circlePenView.setFitWidth(30);
        Button circlePenButton = new Button("", circlePenView);
        ImageView lineView = new ImageView("com/cromewell/paint/res/imgs/line.png");
        lineView.setFitHeight(30);
        lineView.setFitWidth(30);
        Button lineButton = new Button("", lineView);
        ImageView ereaserView = new ImageView("com/cromewell/paint/res/imgs/ereaser.png");
        ereaserView.setFitHeight(30);
        ereaserView.setFitWidth(30);
        Button ereaserButton = new Button("", ereaserView);
        vbox.getChildren().addAll(penButton, circlePenButton,circleButton, rectangleButton, lineButton, ereaserButton);

        penButton.setOnMouseClicked(e->{
            if(drawMode != DrawMode.PEN){
                drawMode = DrawMode.PEN;
                initListeners();
            }
        });

        circlePenButton.setOnMouseClicked(e->{
            if(drawMode != DrawMode.CIRCLEPEN){
                drawMode = DrawMode.CIRCLEPEN;
                initListeners();
            }
        });

        circleButton.setOnMouseClicked(e->{
            if(drawMode != DrawMode.CIRCLE){
                drawMode = DrawMode.CIRCLE;
                initListeners();
            }
        });

        rectangleButton.setOnMouseClicked(e->{
            if(drawMode != DrawMode.RECTANGLE){
                drawMode = DrawMode.RECTANGLE;
                initListeners();
            }
        });

        lineButton.setOnAction(e->{
            if(drawMode != DrawMode.LINE){
                drawMode = DrawMode.LINE;
                initListeners();
            }
        });

        ereaserButton.setOnAction(e->{
            if(drawMode != DrawMode.EREASER){
                drawMode = DrawMode.EREASER;
                initListeners();
            }
        });
    }

    private void initListeners() {
        if(drawMode == DrawMode.PEN) {
            root.setOnMouseDragged(e-> drawPen(e, curColor));
            root.setOnMousePressed(e -> {
                oldX = e.getX();
                oldY = e.getY();
            });
            root.setOnMouseClicked(e-> drawPen(e, curColor));
            root.setOnMouseReleased(e-> {});
        }else if(drawMode == DrawMode.CIRCLEPEN){
            root.setOnMouseDragged(this::drawCirclePen);
            root.setOnMousePressed(e -> {
                oldX = e.getX();
                oldY = e.getY();
            });
            root.setOnMouseClicked(e->{});
            root.setOnMouseReleased(e-> {});
        }else if(drawMode == DrawMode.RECTANGLE){
            Rectangle rectangle = new Rectangle();
            root.setOnMouseDragged(e-> {
                double w = (oldX > e.getX())? 0: e.getX()-oldX;
                double h = (oldY > e.getY())? 0: e.getY()-oldY;
                rectangle.setHeight(h);
                rectangle.setWidth(w);
            });
            root.setOnMousePressed(e -> {
                oldX = e.getX();
                oldY = e.getY();
                rectangle.setX(oldX);
                rectangle.setY(oldY);
                rectangle.setStroke(curColor);
                rectangle.setStrokeWidth(penWidth);
                rectangle.setFill(Color.TRANSPARENT);
                root.getChildren().add(rectangle);
            });
            root.setOnMouseClicked(e->{});
            root.setOnMouseReleased(e-> initListeners());
        }else if(drawMode == DrawMode.LINE){
            Line line = new Line();
            root.setOnMouseDragged(e->{
                line.setEndX(e.getX());
                line.setEndY(e.getY());
            });
            root.setOnMousePressed(e -> {
                oldX = e.getX();
                oldY = e.getY();
                line.setStartX(oldX);
                line.setStartY(oldY);
                line.setEndX(oldX);
                line.setEndY(oldY);
                line.setStrokeWidth(penWidth);
                line.setStroke(curColor);
                root.getChildren().add(line);
            });
            root.setOnMouseClicked(e->{});
            root.setOnMouseReleased(e-> {initListeners();});
        }else if(drawMode == DrawMode.CIRCLE){
            Circle circle = new Circle();
            root.setOnMouseDragged(e-> circle.setRadius(calcRadius(e)));
            root.setOnMousePressed(e ->{
                oldX = e.getX();
                oldY = e.getY();
                circle.setCenterX(oldX);
                circle.setCenterY(oldY);
                circle.setStroke(curColor);
                circle.setStrokeWidth(penWidth);
                circle.setFill(Color.TRANSPARENT);
                root.getChildren().add(circle);
            });
            root.setOnMouseClicked(e->{});
            root.setOnMouseReleased(e-> initListeners());
        }else if(drawMode == DrawMode.EREASER){
            root.setOnMouseDragged(e->{
                drawPen(e, Color.WHITE);
            });
            root.setOnMousePressed(e -> {
                oldX = e.getX();
                oldY = e.getY();
            });
            root.setOnMouseClicked(e->{
                drawPen(e, Color.WHITE);
            });
            root.setOnMouseReleased(e-> {});
        }
    }

    private double calcRadius(MouseEvent e) {
        double a = (oldX > e.getX())? oldX-e.getX(): e.getX()-oldX;
        double b = (oldY > e.getY())? oldY-e.getY(): e.getY()-oldY;
        return Math.sqrt(a*a+b*b);
    }

    private void drawCirclePen(MouseEvent e) {
        double a = (oldX > e.getX())? oldX-e.getX(): e.getX()-oldX;
        double b = (oldY > e.getY())? oldY-e.getY(): e.getY()-oldY;
        double radius = Math.sqrt(a*a+b*b);
        Circle circle = new Circle(oldX-radius, oldY-radius, radius);
        circle.setStroke(curColor);
        circle.setStrokeWidth(penWidth);
        root.getChildren().add(circle);
        oldX = e.getX();
        oldY = e.getY();
    }

    private void drawPen(MouseEvent e, Color color){
        Line line = new Line(oldX, oldY, e.getX(), e.getY());
        line.setStroke(color);
        line.setStrokeWidth(penWidth);
        root.getChildren().add(line);
        oldX = e.getX();
        oldY = e.getY();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
