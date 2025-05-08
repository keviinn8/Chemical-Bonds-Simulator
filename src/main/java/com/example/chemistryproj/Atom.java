package com.example.chemistryproj;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import java.util.ArrayList;
import lombok.Getter;


public class Atom extends Pane {

    public Circle nucleus;
    public String name = "";
    @Getter
    private Label elementLabel;
    @Getter
    private Circle[] valenceElectrons;
    private double mouseX;
    private double mouseY;
    private static final double NUCLEUS_RADIUS = 30.0;
    private static final double ELECTRON_RADIUS = 4.0;

    public Atom(ArrayList<Atom> atom) {
        for(Atom a: atom){
            name = name + a.name;
        }
    }

    public Atom(String elementSymbol) {
        name = elementSymbol;
        nucleus = new Circle(NUCLEUS_RADIUS, Color.BLACK);
        getChildren().addAll(nucleus);

        elementLabel = new Label(elementSymbol);
        elementLabel.setFont(new Font("Times New Roman", 12));
        elementLabel.setTextFill(Color.WHITE);
        elementLabel.setTextAlignment(TextAlignment.CENTER);
        elementLabel.setLayoutX(-4);
        elementLabel.setLayoutY(-6);
        getChildren().addAll(elementLabel);

        // Add event handlers for mouse press, drag, and release
        setOnMousePressed(this::onMousePressed);
        setOnMouseDragged(this::onMouseDragged);
    }

    private void onMousePressed(MouseEvent event) {
        mouseX = event.getSceneX();
        mouseY = event.getSceneY();
    }

    private void onMouseDragged(MouseEvent event) {
        double deltaX = event.getSceneX() - mouseX;
        double deltaY = event.getSceneY() - mouseY;

        // Calculate the new position within the limits
        double newX = getLayoutX() + deltaX;
        double newY = getLayoutY() + deltaY;

        // Limit newX and newY within certain bounds
        double minX = NUCLEUS_RADIUS + ELECTRON_RADIUS; // Set your minimum x-coordinate limit
        double minY = NUCLEUS_RADIUS + ELECTRON_RADIUS; // Set your minimum y-coordinate limit
        double maxX = 590 - minX; // Set your maximum x-coordinate limit
        double maxY = 256 - minY; // Set your maximum y-coordinate limit

        newX = Math.min(Math.max(newX, minX), maxX);
        newY = Math.min(Math.max(newY, minY), maxY);

        // Update atom position
        relocate(newX, newY);

        mouseX = event.getSceneX();
        mouseY = event.getSceneY();
    }

    public static Atom createAtomFromElementSymbol(String elementSymbol) {
        int numberOfValenceElectrons = getNumberOfValenceElectrons(elementSymbol);
        Atom atom = new Atom(elementSymbol);
        atom.setValenceElectrons(numberOfValenceElectrons);
        return atom;
    }

    public void setValenceElectrons(int numberOfElectrons) {
        int[] angles = getPlacementOfElectrons(numberOfElectrons);
        valenceElectrons = new Circle[numberOfElectrons];
        double centerX = getWidth() / 2.0;
        double centerY = getHeight() / 2.0;

        for (int i = 0; i < numberOfElectrons; i++) {
            double angle = angles[i];
            double x = centerX + (NUCLEUS_RADIUS + ELECTRON_RADIUS) * Math.cos(Math.toRadians(angle));
            double y = centerY + (NUCLEUS_RADIUS + ELECTRON_RADIUS) * Math.sin(Math.toRadians(angle));
            Circle electron = new Circle(x, y, ELECTRON_RADIUS, Color.BLUE);
            valenceElectrons[i] = electron;
            getChildren().add(electron);
        }
    }

    public void removeElectronAtAngle(int angleIndexToRemove) {
        if (valenceElectrons != null && valenceElectrons.length > 0) {
            if (angleIndexToRemove >= 0 && angleIndexToRemove < valenceElectrons.length) {
                // Remove the electron at the specified angle index
                getChildren().remove(valenceElectrons[angleIndexToRemove]);
                valenceElectrons[angleIndexToRemove] = null;
            } else {
                System.out.println("Invalid angle index.");
            }
        }
    }

    public static int getNumberOfValenceElectrons(String elementSymbol) {
        return switch (elementSymbol) {
            case "H", "Li", "Na", "K" -> 1;
            case "Be", "Mg", "Ca" -> 2;
            case "B", "Al", "Ga" -> 3;
            case "C", "Si", "Ge" -> 4;
            case "N", "P", "As" -> 5;
            case "O", "S", "Se" -> 6;
            case "F", "Cl", "Br" -> 7;
            case "Ne", "Ar", "Kr" -> 8;
            case "He" -> 9;
            default -> 0;
        };
    }

    public int[] getPlacementOfElectrons(int numberOfElectrons){
        return switch (numberOfElectrons){
            case 1,2,3,4 -> new int[]{0, 180, 90, 270};
            case 5 -> new int[]{0, 90, 180, 265, 275};
            case 6 -> new int[]{0, 85, 180, 265, 275, 95};
            case 7 -> new int[]{5, 85, 180, 265, 275, 95, 355};
            case 8 -> new int[]{5, 85, 175, 265, 275, 95, 355, 185};
            case 9 -> new int[]{265, 275, 265, 275, 265, 275, 265, 275, 265};
            default -> new int[]{0};
        };
    }

    public boolean isSelected(){
        if(nucleus.getFill() == Color.GREEN){
            return true;
        }
        return false;
    }

    public int getAtomGroup(String elementSymbol) {
        return switch (elementSymbol) {
            case "H", "Li", "Na", "K", "F", "Cl", "Br" -> 1;
            case "Be", "Mg", "Ca", "O", "S", "Se" -> 2;
            case "B", "Al", "Ga", "N", "P", "As"  -> 3;
            case "C", "Si", "Ge" -> 4;
            default -> 0;
        };
    }
}
