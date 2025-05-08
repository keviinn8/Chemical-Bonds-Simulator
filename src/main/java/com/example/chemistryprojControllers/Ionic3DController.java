package com.example.chemistryprojControllers;

import com.example.chemistryproj.Electron;
import com.example.chemistryproj.HelloApplication;
import com.example.chemistryproj.Ion;
import com.example.chemistryprojControllers.IonicController;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class Ionic3DController implements Initializable {
    @FXML
    private Button BackButton;
    @FXML
    private HBox hBox;
    @FXML
    private Pane pane;
    @FXML
    private Button switchButton;

    private ArrayList<Ion> arrayIons;
    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private Stage stage;
    SubScene scene;
    private ArrayList<Ion> arrayOf2Ions;

    private ArrayList<Ion> metalIons = new ArrayList<>();
    private ArrayList<Ion> nonMetalIons = new ArrayList<>();

    private int counter = 1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        arrayIons = IonicController.getArrIons();
        for (Ion ion : arrayIons) {
            if (ion.isMetal) {
                metalIons.add(ion);
            } else {
                nonMetalIons.add(ion);
            }
        }
        for (int i = 0; i < nonMetalIons.size(); i++) {
            Ion ion = nonMetalIons.get(i);
            Button b = new Button(nonMetalIons.get(i).toString());
            b.setMinSize(70, 50);
            hBox.getChildren().add(b);
            b.setOnAction(e -> {
                pane.getChildren().remove(scene);
                createSphereAndPutItInScene(ion);
            });
        }
        arrayOf2Ions = IonicController.getArrayOfIonsCreated();

    }

    private Group createNucleus(Ion ion, Color c) {
        double radius = 6;
        Group g = new Group();
        Random r = new Random();
        for (int i = 0; i < ion.protons; i++) {
            Sphere s = new Sphere(3);
            double d = r.nextDouble(0, 2 * Math.PI);
            double dx = r.nextDouble(-radius, radius + 1);
            double dy = r.nextDouble(-radius, radius + 1);
            double dz = r.nextDouble(-radius, radius + 1);

            s.setTranslateX(dx * Math.cos(d));
            s.setTranslateY(dy * Math.sin(d));
            s.setTranslateZ(dz);

            PhongMaterial material = new PhongMaterial();
            material.setDiffuseColor(c);
            s.setMaterial(material);

            TranslateTransition transition = new TranslateTransition(Duration.seconds(0.1), s);
            transition.setByX(r.nextDouble(-6, 6));
            transition.setByY(r.nextDouble(-6, 6));
            transition.setCycleCount(2);
            transition.setAutoReverse(true);
            transition.play();

            g.getChildren().add(s);

        }
        for (int i = 0; i < ion.neutrons; i++) {
            Sphere s = new Sphere(3);
            double d = r.nextDouble(0, 2 * Math.PI);
            double dx = r.nextDouble(-radius, radius + 1);
            double dy = r.nextDouble(-radius, radius + 1);
            double dz = r.nextDouble(-radius, radius + 1);


            s.setTranslateX(dx * Math.cos(d));
            s.setTranslateY(dy * Math.sin(d));
            s.setTranslateZ(dz);

            PhongMaterial material = new PhongMaterial();
            material.setDiffuseColor(Color.ORANGE);
            s.setMaterial(material);

            TranslateTransition transition = new TranslateTransition(Duration.seconds(0.1), s);
            transition.setByX(r.nextDouble(-6, 6));
            transition.setByY(r.nextDouble(-6, 6));
            transition.setCycleCount(2);
            transition.setAutoReverse(true);
            transition.play();

            g.getChildren().add(s);
        }

        return g;
    }

    private void initMouseControl(Group group, SubScene scene) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);
        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });
        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });
    }

    private void createSphereAndPutItInScene(Ion ion) {
        Group group = new Group();
        Camera camera = new PerspectiveCamera(true);
        scene = new SubScene(group, 1200, 500, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.LIGHTBLUE);
        scene.setCamera(camera);
        camera.translateXProperty().set(0);
        camera.translateYProperty().set(0);
        camera.translateZProperty().set(-500);

        group.translateXProperty().set(0);
        group.translateYProperty().set(0);
        group.translateZProperty().set(0);

        camera.setNearClip(1);
        camera.setFarClip(2000);
        initMouseControl(group, scene);
        if (pane.getScene() != null) {
            stage = (Stage) pane.getScene().getWindow();
            stage.addEventHandler(ScrollEvent.SCROLL, event -> {
                double delta = event.getDeltaY();
                group.translateZProperty().set(group.getTranslateZ() - delta);
            });
        }
        pane.getChildren().add(scene);

        Group g;
        if (ion.isMetal) {
            g = createNucleus(ion, Color.RED);
        } else {
            g = createNucleus(ion, Color.ROYALBLUE);
        }
        group.getChildren().add(g);
        int j = 0;
        for (int i = 0; i < ion.originalElectronsArray.size(); i++) {
            Electron e = ion.originalElectronsArray.get(i);
            if (e.timer != null) {
                ion.originalElectronsArray.get(i).timer.stop();
            }
            e.sphere = new Sphere(1);
            if (i % 2 == 0) {
                e.sphere.getTransforms().add(new Translate(0, j, 70));
                e.sphere.setRotationAxis(Rotate.X_AXIS);
            } else {
                e.sphere.getTransforms().add(new Translate(j, 0, 70));
                e.sphere.setRotationAxis(Rotate.Y_AXIS);
            }
            PhongMaterial material = new PhongMaterial();
            if (ion.isMetal) {
                material.setDiffuseColor(Color.BLUE);
            } else {
                material.setDiffuseColor(Color.GREEN);
            }
            e.sphere.setMaterial(material);
            group.getChildren().add(e.sphere);
            j += 3;
            e.setAnTimer();
            e.timer.start();
        }
        group.getChildren().add(new AmbientLight());
    }

    @FXML
    void goBack2D(ActionEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(HelloApplication.class.getResource("Ionic-Scene.fxml"));
        Stage stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene1 = new Scene(fxmlLoader);
        stage1.setScene(scene1);
        stage1.show();
    }

    @FXML
    void switchMetal(ActionEvent event) {
        hBox.getChildren().clear();
        switch (counter) {
            case 0 -> {
                for (int i = 0; i < nonMetalIons.size(); i++) {
                    Ion ion = nonMetalIons.get(i);
                    Button b = new Button(nonMetalIons.get(i).toString());
                    b.setMinSize(70, 50);
                    hBox.getChildren().add(b);
                    b.setOnAction(e -> {
                        pane.getChildren().remove(scene);
                        createSphereAndPutItInScene(ion);
                    });
                }
                counter = 1;
                switchButton.setText("Non-Metal");
            }
            case 1 -> {
                for (int i = 0; i < metalIons.size(); i++) {
                    Ion ion = metalIons.get(i);
                    Button b = new Button(metalIons.get(i).toString());
                    b.setMinSize(70, 50);
                    hBox.getChildren().add(b);
                    b.setOnAction(e -> {
                        pane.getChildren().remove(scene);
                        createSphereAndPutItInScene(ion);
                    });
                }
                if (arrayOf2Ions.isEmpty()) {
                    counter = 0;
                } else {
                    counter = 2;
                }
                switchButton.setText("Metal");

            }
            case 2 -> {
                for (int i = 0; i < arrayOf2Ions.size(); i++) {
                    Ion ion = arrayOf2Ions.get(i);
                    Button b = new Button(arrayOf2Ions.get(i).toString());
                    b.setMinSize(70, 50);
                    hBox.getChildren().add(b);
                    b.setOnAction(e -> {
                        pane.getChildren().remove(scene);
                        create2SphereAndPutItInScene(ion);
                    });
                }
                switchButton.setText("PoyIons");
                counter = 0;
            }
        }
    }

    private void create2SphereAndPutItInScene(Ion ion) {
        Group cation = createNucleus(ion.metal, Color.RED);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.rgb(255, 255, 255, 0.01));
        material.setSpecularColor(Color.rgb(255, 255, 255, 0.01));
        Sphere cationCloud = new Sphere(100);
        Group anion = createNucleus(ion.nonMetal, Color.BLUE);

        cation.setTranslateX(100);
        cationCloud.setTranslateX(100);
        anion.setTranslateX(-100);

        Group group = new Group();
        group.getChildren().add(cation);
        group.getChildren().add(anion);

        putElectrons(ion.metal, group, 100, 0, Color.YELLOW);
        putElectrons(ion.nonMetal, group, -100, 0, Color.GREEN);

        group.getChildren().add(new AmbientLight());
        Camera camera = new PerspectiveCamera(true);
        scene = new SubScene(group, 1200, 500, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.LIGHTBLUE);
        scene.setCamera(camera);

        camera.translateXProperty().set(0);
        camera.translateYProperty().set(0);
        camera.translateZProperty().set(-500);
        camera.setNearClip(1);
        camera.setFarClip(2000);

        group.translateXProperty().set(0);
        group.translateYProperty().set(0);
        group.translateZProperty().set(0);

        initMouseControl(group, scene);
        if (pane.getScene() != null) {
            stage = (Stage) pane.getScene().getWindow();
            stage.addEventHandler(ScrollEvent.SCROLL, event -> {
                double delta = event.getDeltaY();
                group.translateZProperty().set(group.getTranslateZ() - delta);
            });
        }
        pane.getChildren().add(scene);
    }

    private void putElectrons(Ion ion, Group group, double x, double y, Color c) {
        Random r = new Random();
        int j = 0;
        for (int i = 0; i < ion.electronsArray.size(); i++) {

            Electron e = ion.electronsArray.get(i);
            if (e.timer != null) {
                ion.electronsArray.get(i).timer.stop();
            }
            e.sphere = new Sphere(1, 100);
            if (i % 2 == 0) {
                e.sphere.setTranslateX(x);
                e.sphere.getTransforms().add(new Translate(0, y + j, 70));
                e.sphere.setRotationAxis(Rotate.X_AXIS);
            } else {
                e.sphere.setTranslateX(x);
                e.sphere.getTransforms().add(new Translate(j, 0, 70));
                e.sphere.setRotationAxis(Rotate.Y_AXIS);
            }

            PhongMaterial material = new PhongMaterial();
            if (i >= ion.originalElectronsArray.size()) {
                material.setDiffuseColor(Color.BLUE);
            } else {
                material.setDiffuseColor(c);
            }
            e.sphere.setMaterial(material);
            group.getChildren().add(e.sphere);
            j += 3;
            e.setAnTimer();
            e.timer.start();

        }
    }
}



