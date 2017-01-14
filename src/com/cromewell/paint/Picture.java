package com.cromewell.paint;

import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jo on 04.11.2016.
 *
 */
public class Picture {

    private List<String[]> coordinates;

    public Picture(List<String[]> coordinates) {
        if(coordinates == null){
            this.coordinates = new ArrayList<>();
        }else {
            this.coordinates = coordinates;
        }
    }

    public List<Node> getNodes(){
        List<Node> nodes = new ArrayList();
        for(String[] s: coordinates){
            if(s[0].equals("LINE")) {
                Line line = new Line(Integer.parseInt(s[1]), Integer.parseInt(s[2]),
                        Integer.parseInt(s[3]), Integer.parseInt(s[4]));
                line.setStrokeWidth(Double.parseDouble(s[5]));
                line.setStroke(Paint.valueOf(s[6]));
                nodes.add(line);
            }else if(s[0].equals("CIRCLE")){
                Circle circle = new Circle(Integer.parseInt(s[1]), Integer.parseInt(s[2]), Integer.parseInt(s[3]));
                circle.setStrokeWidth(Double.parseDouble(s[4]));
                circle.setStroke(Paint.valueOf(s[5]));
                circle.setFill(Paint.valueOf(s[6]));
                nodes.add(circle);
            }else if(s[0].equals("RECTANGLE")){
                Rectangle rectangle = new Rectangle(Integer.parseInt(s[1]), Integer.parseInt(s[2]), Integer.parseInt(s[3]),
                        Integer.parseInt(s[4]));
                rectangle.setStrokeWidth(Double.parseDouble(s[5]));
                rectangle.setStroke(Paint.valueOf(s[6]));
                rectangle.setFill(Paint.valueOf(s[7]));
                nodes.add(rectangle);
            }
        }

        return nodes;
    }
}
