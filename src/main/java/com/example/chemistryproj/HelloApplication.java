package com.example.chemistryproj;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;


public class HelloApplication extends Application {
    Pane original = new Pane();

    Scene scene = new Scene(original,850,450,true);

    public void start(Stage stage) throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Intro-Scene.fxml"));
        SubScene Introfxml = new SubScene(fxmlLoader.load(), 850, 250);
        Group mol = new Group();
        SubScene molDisplay = new SubScene(mol, 300, 200,true, SceneAntialiasing.BALANCED);

        molDisplay.setFill(Color.rgb(149, 107, 124));
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(500);
        camera.setTranslateZ(-350);
        molDisplay.setCamera(camera);


        Sphere oxygen = new Sphere(20);
        PhongMaterial oxygenMaterial = new PhongMaterial();
        oxygenMaterial.setDiffuseColor(Color.rgb(43, 126, 137));
        oxygen.setMaterial(oxygenMaterial);
        oxygen.setRotationAxis(Rotate.Z_AXIS);
        oxygen.setLayoutX(0);
        oxygen.setLayoutY(-30);


        Sphere hydrogen1 = new Sphere(10);
        PhongMaterial hydrogenMaterial = new PhongMaterial();
        hydrogenMaterial.setDiffuseColor(Color.rgb(13, 68, 63));
        hydrogen1.setMaterial(hydrogenMaterial);
        hydrogen1.setRotationAxis(Rotate.Z_AXIS);
        hydrogen1.setLayoutY(9);
        hydrogen1.setLayoutX(50);

        Sphere hydrogen2 = new Sphere(10);
        hydrogen2.setMaterial(hydrogenMaterial);
        hydrogen2.setRotationAxis(Rotate.Z_AXIS);
        hydrogen2.setLayoutX(-50);
        hydrogen2.setLayoutY(9);


        Cylinder bond1 = new Cylinder(4, 40);
        PhongMaterial bondMaterial = new PhongMaterial();
        bondMaterial.setDiffuseColor(Color.rgb(90, 90, 90));
        bond1.setMaterial(bondMaterial);
        bond1.setRotate(50);
        bond1.setLayoutX(-30);
        bond1.setLayoutY(-10);

        Cylinder bond2 = new Cylinder(4, 40);
        bond2.setMaterial(bondMaterial);
        bond2.setRotate(-50);
        bond2.setLayoutX(30);
        bond2.setLayoutY(-10);

        mol.getChildren().addAll(oxygen, hydrogen1, hydrogen2, bond1, bond2);

        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(10), mol);
        rotateTransition.setAxis(Rotate.Y_AXIS);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(Animation.INDEFINITE);
        rotateTransition.play();


        Group ion = new Group();
        SubScene IonicAni = new SubScene(ion,300,200,true, SceneAntialiasing.BALANCED);
        IonicAni.setFill(Color.rgb(106,160,210));
        PerspectiveCamera ionC = new PerspectiveCamera(true);
        ionC.setTranslateZ(-50);
        IonicAni.setCamera(ionC);

        Sphere nucleus = new Sphere(3);
        nucleus.setMaterial(new PhongMaterial(Color.rgb(214,102,102)));
        Sphere e1 = new Sphere(2);
        e1.setMaterial(new PhongMaterial(Color.rgb(175,141,167)));
        Sphere e2 = new Sphere(2);
        e2.setMaterial(new PhongMaterial(Color.rgb(175,141,167)));

        e1.setTranslateX(20);
        e2.setTranslateX(-20);

        ion.getChildren().addAll(nucleus,e1,e2);

        Rotate electrontRotation = new Rotate();
        electrontRotation.setAxis(Rotate.Y_AXIS);
        ion.getTransforms().add(electrontRotation);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20),event -> {
            electrontRotation.setAngle(electrontRotation.getAngle()+1);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


        original.getChildren().add(Introfxml);
        original.getChildren().add(molDisplay);
        original.getChildren().add(IonicAni);
        IonicAni.setLayoutY(200);
        IonicAni.setLayoutX(490);
        molDisplay.setLayoutX(60);
        molDisplay.setLayoutY(200);

        stage.setScene(scene);
        stage.setTitle("Chemistry Application");

        stage.show();
    }
    public Scene rt(){
        return scene;
    }
    public static void main(String[] args) {
        launch();
    }
}