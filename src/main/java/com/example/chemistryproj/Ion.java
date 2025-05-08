package com.example.chemistryproj;

import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class Ion {
    private static final int NUCLEUS_RADIUS = 10;
    public String ionName = "";
    public int charge;
    public boolean isMetal;
    public ArrayList<Electron> electronsArray;
    public ArrayList<Electron> originalElectronsArray;
    public Group ion;
    public Circle ionNucleus;
    public Circle ionCloud;
    public int numOfBonds;
    private static final double DIVIDE = 3.0 / 8.0;
    public Ion metal;
    public Ion nonMetal;
    public int neutrons;
    public int protons;
    public int counterOfBonds = 0;

    public Group groupIon;
    private String fullName;
    public int originalCharge;

    private String tempName = "";
    private static String[] superscripts = {
            "\u00B9", // Superscript One
            "\u00B2", // Superscript Two
            "\u00B3", // Superscript Three
            "\u2074", // Superscript Four
            "\u2075", // Superscript Five
            "\u2076", // Superscript Six
            "\u2077", // Superscript Seven
            "\u2078", // Superscript Eight
            "\u2079", // Superscript Nine
    };


    public Ion(String ionName, int charge, boolean isMetal, int numOfBonds, int neutrons, int protons, String fullname) {
        this.ionName = ionName;
        this.charge = charge;
        this.isMetal = isMetal;
        this.numOfBonds = numOfBonds;
        this.neutrons = neutrons;
        this.protons = protons;
        this.electronsArray = new ArrayList<>();
        this.originalElectronsArray = new ArrayList<>();
        this.ion = new Group();
        this.fullName = fullname;
        this.originalCharge = charge;
    }

    public Ion() {

    }

    public Ion(Ion metal, Ion nonMetal, Group g) {
        this.metal = metal;
        this.nonMetal = nonMetal;
        ionName = metal.ionName + nonMetal.ionName;
        this.ion = g;

    }

    public void addIon() {
        Color c;
        if (isMetal) {
            c = Color.SILVER;
        } else {
            c = Color.GREEN;
        }
        this.ionNucleus = new Circle(NUCLEUS_RADIUS, c);
        this.ionNucleus.setStroke(Color.BLACK);

        ionCloud = new Circle(this.ionNucleus.getCenterX(), this.ionNucleus.getCenterY(), NUCLEUS_RADIUS * 4);
        ionCloud.setFill(null);
        ionCloud.setStroke(Color.BLACK);
        ionCloud.centerXProperty().bind(ionNucleus.centerXProperty());
        ionCloud.centerYProperty().bind(ionNucleus.centerYProperty());
        ionCloud.setVisible(true);

        ion.getChildren().addAll(ionNucleus, ionCloud);
        double angleDiv = 360.0 / 8.0;
        for (int i = 0; i < charge; i++) {

            double d1 = ionCloud.getRadius() * Math.cos(Math.toRadians(angleDiv * i));
            double d2 = ionCloud.getRadius() * Math.sin(Math.toRadians(angleDiv * i));

            Electron e = new Electron(new Circle(4, Color.BLUE));
            e.electron.centerXProperty().bind(ionNucleus.centerXProperty().add(d1));
            e.electron.centerYProperty().bind(ionNucleus.centerYProperty().add(d2));
            rotateElectrons(e, (DIVIDE * i));
            electronsArray.add(e);
            originalElectronsArray.add(e);


            ion.getChildren().add(e.electron);
        }
        Text tx = new Text(ionName);
        if (!isMetal) {
            tx.setFill(Color.WHITE);
        } else {
            tx.setFill(Color.BLACK);
        }
        tx.xProperty().bind(ionNucleus.centerXProperty().subtract(tx.getLayoutBounds().getWidth() / 2));
        tx.yProperty().bind(ionNucleus.centerYProperty().add(ionNucleus.getCenterY() + 5));

        ion.getChildren().addAll(tx);
    }

    public static int transitionValenceElectrons(Ion metal, Ion nonMetal) {
        metal.stopElectrons();
        nonMetal.stopElectrons();
        int rr = metal.charge;
        int tot = nonMetal.charge + metal.charge;
        if (tot >= 9) {
            nonMetal.charge = 8;
            rr = metal.charge - (tot - 8);
            metal.charge = tot - 8;

        } else {
            nonMetal.charge = tot;
            metal.charge = 0;
        }
        return rr;
    }

    public static void exchangeElectrons(Ion metal, Ion nonMetal) {
        for (int i = nonMetal.electronsArray.size(); i < nonMetal.charge; i++) {
            if ((!metal.electronsArray.isEmpty())) {
                metal.electronsArray.getFirst().electron.setFill(Color.ORANGE);
                nonMetal.electronsArray.add(metal.electronsArray.getFirst());
                metal.electronsArray.removeFirst();
            }
        }

    }


    private void rotateElectrons(Electron e, double x) {
        e.path = new PathTransition(Duration.seconds(3), ionCloud, e.electron);
        e.path.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        e.path.setCycleCount(Animation.INDEFINITE);
        e.path.setInterpolator(Interpolator.LINEAR);
        e.path.playFrom(Duration.seconds(x));

    }

    public void playElectrons() {
        for (int i = 0; i < this.electronsArray.size(); i++) {
            rotateElectrons(this.electronsArray.get(i), DIVIDE * i);
        }
    }

    public void stopElectrons() {
        for (int i = 0; i < this.electronsArray.size(); i++) {
            this.electronsArray.get(i).path.pause();
        }
    }

    @Override
    public String toString() {
        if (metal == null && nonMetal == null) {
            return fullName + ":\nProtons: " + this.protons+ " Neutrons: " + this.neutrons;
        } else {
            if (tempName.equals("")) {
                String r, t;
                if (metal.isMetal) {
                    r = "\u207A";
                    t = "\u207B";
                } else {
                    t = "\u207A";
                    r = "\u207B";
                }
                tempName = metal.ionName + r + superscripts[metal.charge - 1] + nonMetal.ionName + t + superscripts[nonMetal.numOfBonds - 1];
            }
            return tempName;
        }
    }
}
