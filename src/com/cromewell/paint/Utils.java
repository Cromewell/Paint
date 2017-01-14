package com.cromewell.paint;

import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jo on 04.11.2016.
 *
 */
public class Utils {

    public static List<String[]> loadPicture(File f){
        List<String[]> coordinates = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));

            String line;
            while((line = reader.readLine()) != null){
                String[] xy = line.split(":");
                coordinates.add(xy);
            }
            reader.close();

            return coordinates;
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void savePicture(File f, List<Node> nodes){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
            for(Node node: nodes){
                if(node instanceof Line) {
                    Line line = (Line) node;
                    writer.write("LINE:");
                    writer.write((int) line.getStartX() + ":");
                    writer.write((int) line.getStartY() + ":");
                    writer.write((int) line.getEndX() + ":");
                    writer.write((int) line.getEndY() + ":");
                    writer.write(line.getStrokeWidth() + ":");
                    writer.write(String.valueOf(line.getStroke()));
                    writer.newLine();
                }else if(node instanceof Circle){
                    Circle circle = (Circle) node;
                    writer.write("CIRCLE:");
                    writer.write((int) circle.getCenterX() + ":");
                    writer.write((int) circle.getCenterY() + ":");
                    writer.write((int) circle.getRadius() + ":");
                    writer.write(circle.getStrokeWidth() + ":");
                    writer.write(circle.getStroke() + ":");
                    writer.write(String.valueOf(circle.getFill()));
                    writer.newLine();
                }else if(node instanceof Rectangle){
                    Rectangle rectangle = (Rectangle) node;
                    writer.write("RECTANGLE:");
                    writer.write((int) rectangle.getX() + ":");
                    writer.write((int) rectangle.getY() + ":");
                    writer.write((int) rectangle.getWidth() + ":");
                    writer.write((int) rectangle.getHeight() + ":");
                    writer.write(rectangle.getStrokeWidth() + ":");
                    writer.write(rectangle.getStroke() + ":");
                    writer.write(String.valueOf(rectangle.getFill()));
                    writer.newLine();
                }
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
