package com.example.chemistryproj;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;

import java.util.Random;

public class Electron {
    public AnimationTimer timer;
    public Sphere sphere;
    public Circle electron;
    PathTransition path;
    TranslateTransition transition;

    public Electron(Circle electron) {
        this.electron = electron;
        electron.setStroke(Color.BLACK);
    }

    public void setAnTimer() {
        Random random = new Random();
        int d = random.nextInt(5, 15);

        int r = random.nextInt(-2, 3);
        transition = new TranslateTransition(Duration.millis(50), this.sphere);
        transition.setByX(r);
        transition.setByY(r);
        transition.setByZ(r);
        transition.setCycleCount(1);
        transition.setAutoReverse(true);
        transition.play();
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                sphere.setRotate(sphere.getRotate() + d);
                if (!transition.getStatus().equals(Animation.Status.RUNNING)) {
                    transition.setByX(random.nextDouble(-2, 2));
                    transition.setByZ(random.nextDouble(-2, 2));
                    transition.setByY(random.nextDouble(-2, 2));
                }
            }
        };
    }
}
