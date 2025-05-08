package com.example.chemistryprojControllers;

import com.example.chemistryproj.Electron;
import com.example.chemistryproj.HelloApplication;
import com.example.chemistryproj.Ion;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class IonicController implements Initializable {
    @FXML
    private Button playPause;
    @FXML
    private Button resetButton;
    @FXML
    private Pane animationPane;
    @FXML
    private HBox boxIons;
    @FXML
    private ListView<Ion> ionListView;
    @FXML
    private Button button3D;
    @FXML
    private Circle circleCenter;
    @FXML
    private Label label1;
    private Stage stage;
    private Scene scene;
    private Parent fxmlLoader;
    static String[] ions = {"O", "C", "I", "P", "Cl"};
    static String[] metals = {"Au", "Ag", "Cu", "Fe", "Hg", "Zn", "Na"};
    private static Button[] nonMetalButtons = new Button[ions.length];
    private static Button[] metalButtons = new Button[metals.length];
    private double mouseX, mouseY, mouseX1, mouseY1;
    private static ArrayList<Ion> arrIons;
    private int counter;
    private ObservableList<Ion> arrIons1;
    private ArrayList<Ion> selectedIon = new ArrayList<>();
    private static ArrayList<Ion> arrayOfIonsCreated = new ArrayList<>();
    private boolean isItMetal;
    private Ion current = null;
    private FillTransition ft;
    AnimationTimer animation;
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
        label1.setVisible(false);
    }));

    public void initialize(URL location, ResourceBundle resources) {
        isItMetal = false;
        ionListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        counter = 0;
        arrIons = new ArrayList<>();
        mouseX = mouseX1 = mouseY = mouseY1 = 0;
        System.out.println("hello");
        for (int i = 0; i < ions.length; i++) {
            Button b = new Button(ions[i]);
            b.setMinSize(70, 50);
            b.setOnAction(e -> {
                ionButton(b.getText());
            });
            boxIons.getChildren().add(b);
            nonMetalButtons[i] = b;
        }

        for (int i = 0; i < metals.length; i++) {
            Button b = new Button(metals[i]);
            b.setMinSize(70, 50);
            b.setOnAction(e -> {
                ionButton(b.getText());
            });
            metalButtons[i] = b;
        }
        arrIons1 = FXCollections.observableArrayList(arrIons);
        ionListView.setItems(arrIons1);

    }

    public static ArrayList getArrIons() {
        return arrIons;
    }

    public static ArrayList getArrayOfIonsCreated() {
        return arrayOfIonsCreated;
    }

    private void setUpIon(Ion ion, int i) {
        if (counter < 10) {
            ion.addIon();
            ion.ionNucleus.setCenterX(i);
            if (ion.isMetal) {
                ion.ionNucleus.setCenterY(200);
            } else {
                ion.ionNucleus.setCenterY(300);
            }
            animationPane.getChildren().add(ion.ion);
            ion.ion.setOnMousePressed(e -> {
                mouseX = e.getSceneX() - ion.ionNucleus.getCenterX();
                mouseY = e.getSceneY() - ion.ionNucleus.getCenterY();
            });
            ion.ion.setOnMouseDragged(e -> {
                ion.stopElectrons();
                if (e.getSceneX() < (animationPane.getWidth()) && e.getSceneX() > 50) {
                    ion.ionNucleus.setCenterX((int) (e.getSceneX() - mouseX));
                }
                if (e.getSceneY() < (animationPane.getHeight()) && e.getSceneY() > 50) {
                    ion.ionNucleus.setCenterY((int) (e.getSceneY() - mouseY));
                }
                ion.playElectrons();
            });
            ion.ion.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 & e.getButton() == MouseButton.PRIMARY) {
                    if (current == null) {
                        ion.stopElectrons();
                        ion.ionNucleus.setCenterX((int) circleCenter.getBoundsInParent().getCenterX());
                        ion.ionNucleus.setCenterY((int) circleCenter.getBoundsInParent().getCenterY());
                        ion.playElectrons();
                        current = ion;
                        ft = new FillTransition(Duration.seconds(0.3), current.ionNucleus, Color.RED, Color.YELLOW);
                        ft.setAutoReverse(true);
                        ft.setCycleCount(Animation.INDEFINITE);
                        ft.play();
                        current.ionNucleus.setFill(Color.GREEN);
                    } else {
                        if (current.equals(ion)) {
                            System.out.println("same");
                        } else {
                            isIonCompatible(current, ion);
                        }
                    }
                }
                if (e.getButton() == MouseButton.SECONDARY) {
                    Label l = new Label();
                    l.setText(ion.toString());
                    l.setStyle("-fx-background-color: transparent; -fx-padding: 5px;");
                    l.setLayoutX(e.getSceneX());
                    l.setLayoutY(e.getSceneY());
                    animationPane.getChildren().add(l);
                    PauseTransition p = new PauseTransition(Duration.seconds(3));
                    p.setOnFinished(er -> {
                        animationPane.getChildren().remove(l);
                    });
                    p.play();

                }

            });
        } else {
            label1.setText("Maximum number of ions");
            label1.setVisible(true);
            timeline.play();
        }
    }

    private void ionButton(String element) {
        Ion ion;
        switch (element) {
            case "O" -> {
                ion = new Ion(element, 6, false, 2, 8, 8, "Oxigen");
                switchInside(ion, 50);
            }
            case "C" -> {
                ion = new Ion(element, 6, false, 2, 6, 6, "Carbon");
                switchInside(ion, 150);
            }
            case "I" -> {
                ion = new Ion(element, 7, false, 2, 74, 53, "Iodine");
                switchInside(ion, 250);
            }
            case "P" -> {
                ion = new Ion(element, 5, false, 1, 15, 16, "Phosphorus");
                switchInside(ion, 350);
            }
            case "Cl" -> {
                ion = new Ion(element, 7, false, 1, 30, 17, "Chloride");
                switchInside(ion, 450);
            }

            case "Ag" -> {
                ion = new Ion(element, 1, true, 1, 11, 11, "Silver");
                switchInside(ion, 150);
            }
            case "Au" -> {
                ion = new Ion(element, 1, true, 1, 60, 47, "Gold");
                switchInside(ion, 50);
            }
            case "Cu" -> {
                ion = new Ion(element, 1, true, 2, 34, 29, "Copper");
                switchInside(ion, 250);
            }
            case "Fe" -> {
                ion = new Ion(element, 2, true, 2, 30, 26, "Iron");
                switchInside(ion, 350);
            }
            case "Hg" -> {
                ion = new Ion(element, 2, true, 2, 122, 80, "Mercury");
                switchInside(ion, 450);
            }
            case "Zn" -> {
                ion = new Ion(element, 2, true, 2, 34, 30, "Zinc");
                switchInside(ion, 550);
            }
            case "Na" -> {
                ion = new Ion(element, 1, true, 1, 12, 11, "Sodium");
                switchInside(ion, 650);
            }
        }
    }

    private void switchInside(Ion ion, int i) {
        counter++;
        setUpIon(ion, i);
        boolean boo = false;
        for (Ion io : arrIons) {
            if (io.ionName.equals(ion.ionName)) {
                boo = true;
                break;
            }
        }
        if (!boo) {
            arrIons.add(ion);
        }
        ion.stopElectrons();
        ion.playElectrons();
    }

    @FXML
    public void goBackhome(ActionEvent event) throws IOException {
        fxmlLoader = FXMLLoader.load(HelloApplication.class.getResource("Intro-Scene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void playOrPauseAnimation(ActionEvent event) {
        animationPane.getChildren().clear();
        arrayOfIonsCreated.clear();
        arrIons.clear();
        selectedIon.clear();
        ionListView.getItems().clear();
        current = null;
    }

    PauseTransition ptX = new PauseTransition(Duration.millis(1));
    PauseTransition ptY = new PauseTransition(Duration.millis(1));

    private void performeLoopX(Ion i1, boolean signX, int comienzoX, int finX) {
        ptX = new PauseTransition(Duration.millis(1));
        if ((signX && comienzoX >= finX) || (!signX && comienzoX <= finX)) {
            return;
        } else {
            i1.ionNucleus.setCenterX(comienzoX);
            ptX.play();
            ptX.setOnFinished(e -> {
                int c;
                if (signX) {
                    c = comienzoX + 5;
                } else {
                    c = comienzoX - 5;
                }
                performeLoopX(i1, signX, c, finX);
            });
        }
    }

    private void performeLoopY(Ion i1, boolean signY, int comienzoY, int finY) {
        ptY = new PauseTransition(Duration.millis(1));
        if ((signY && comienzoY >= finY) || (!signY && comienzoY <= finY)) {
            return;
        } else {
            i1.ionNucleus.setCenterY(comienzoY);
            ptY.play();
            ptY.setOnFinished(e -> {
                int c;
                if (signY) {
                    c = comienzoY + 5;
                } else {
                    c = comienzoY - 5;
                }
                performeLoopY(i1, signY, c, finY);
            });
        }
    }

    int yo = 0;

    private void moveElectron(Ion metal, Ion nonMetal, Circle c, boolean sign, int comienzo, int fin, int r) {
        PauseTransition reloj = new PauseTransition(Duration.millis(1));
        if ((!sign && comienzo > fin) || (sign && comienzo < fin)) {
            yo++;
            if (r == yo) {
                c.centerXProperty().unbind();
                c.centerYProperty().unbind();
                Ion.exchangeElectrons(metal, nonMetal);
                metal.playElectrons();
                nonMetal.playElectrons();
            }
            return;
        } else {
            c.setCenterX(comienzo);
            reloj.play();
            reloj.setOnFinished(e -> {
                int num;
                if (!sign) {
                    num = comienzo + 5;
                } else {
                    num = comienzo - 5;
                }
                moveElectron(metal, nonMetal, c, sign, num, fin, r);
            });
        }
    }

    public void ionicInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ionic Bonds Theory");
        alert.setContentText("An ionic bond is a type of chemical bond that forms between two atoms with very different electronegativities, " +
                "resulting in the transfer of electrons between the atoms. This transfer of electrons leads to the formation of positive and negative ions, " +
                "which are held together by the electrostatic attraction between the oppositely charged ions.");
        alert.showAndWait();
    }

    public void applicationInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information About The Application");
        alert.setContentText("1) Double click the first ion. " +
                "\n 2) Double click the second ion that you want to bond it to, make sure both ions are nonmetal and metal, not the same. " +
                "\n 3) Then press 3D to see the ion in 3D or continue creating new Ions.");
        alert.showAndWait();
    }

    public void applicationHowTo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("How To Use This Application");
        alert.setContentText("There can be up to 16 ions at a time " +
                "\n - Double-click the ion to select it, it will go to the center and change colors" +
                "\n - When the animation starts, the non metal will move towards the metal" +
                "\n - Press clear to clear all the ions" +
                "\n - Right click an ion to learn more information about it");
        alert.showAndWait();
    }

    boolean b = false;
    int crt;
    AnimationTimer an;

    private void performeLoop(Ion nonMetal, Ion metal, boolean signX, int comienzoX, int finX, boolean signY, int comienzoY, int finY) {
        performeLoopX(nonMetal, signX, comienzoX, finX);
        performeLoopY(nonMetal, signY, comienzoY, finY);
        b = false;
        an = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (!b && ptX.getStatus().equals(Animation.Status.STOPPED) && ptY.getStatus().equals(Animation.Status.STOPPED)) {
                    crt = 0;
                    yo = 0;
                    int r = Ion.transitionValenceElectrons(metal, nonMetal);
                    System.out.println(nonMetal.charge + crt);
                    List<Electron> electronsCopy = new ArrayList<>(metal.electronsArray);
                    for (Electron e : electronsCopy) {
                        if (nonMetal.charge + crt <= 9) {
                            e.electron.centerXProperty().unbind();
                            e.electron.centerYProperty().unbind();
                            moveElectron(metal, nonMetal, e.electron, signX, (int) e.electron.getCenterX(), (int) nonMetal.electronsArray.getFirst().electron.getCenterX(), r);
                        } else {
                            metal.electronsArray.remove(e);
                        }
                        crt++;
                    }
                    b = true;
                    ann();
                }
            }
        };
        an.start();
    }

    private void ann() {
        an.stop();
    }

    private void animateThe2IonsReaction(Ion i1, Ion i2) {
        Ion nonMetal, metal;
        if (i1.isMetal) {
            metal = i1;
            nonMetal = i2;
        } else {
            nonMetal = i1;
            metal = i2;
        }
        Circle circle1 = nonMetal.ionNucleus;
        Circle circle2 = metal.ionNucleus;

        boolean signX = true;
        if (nonMetal.ionNucleus.getCenterX() > metal.ionNucleus.getCenterX()) {
            signX = false;
        }
        boolean signY = true;
        if (nonMetal.ionNucleus.getCenterY() > metal.ionNucleus.getCenterY()) {
            signY = false;
        }
        nonMetal.stopElectrons();
        metal.stopElectrons();

        Group g = new Group();
        Ion ion = new Ion(metal, nonMetal, g);

        performeLoop(nonMetal, metal, signX, (int) nonMetal.ionNucleus.getCenterX(), (int) (circle2.getCenterX() - 2 * nonMetal.ionCloud.getRadius()), signY, (int) nonMetal.ionNucleus.getCenterY(), (int) metal.ionNucleus.getCenterY());

        circle1.setFill(Color.BLUE);
        circle2.setFill(Color.RED);
        g.getChildren().addAll(nonMetal.ion, metal.ion);
        arrayOfIonsCreated.add(ion);
        nonMetal.ion.setOnMouseDragged(el -> {
        });
        metal.ion.setOnMouseDragged(el -> {
        });
        animationPane.getChildren().add(g);
        g.setOnMousePressed(e4 -> {
            mouseX = e4.getSceneX() - nonMetal.ionNucleus.getCenterX();
            mouseY = e4.getSceneY() - nonMetal.ionNucleus.getCenterY();
            mouseX1 = e4.getSceneX() - metal.ionNucleus.getCenterX();
            mouseY1 = e4.getSceneY() - metal.ionNucleus.getCenterY();
        });
        g.setOnMouseDragged(e5 -> {
            metal.stopElectrons();
            nonMetal.stopElectrons();
            if (e5.getSceneX() < (animationPane.getWidth()) && e5.getSceneX() > 0) {
                nonMetal.ionNucleus.setCenterX(e5.getSceneX() - mouseX);
                metal.ionNucleus.setCenterX(e5.getSceneX() - mouseX1);
            }
            if (e5.getSceneY() < (animationPane.getHeight()) && e5.getSceneY() > 0) {
                nonMetal.ionNucleus.setCenterY(e5.getSceneY() - mouseY);
                metal.ionNucleus.setCenterY(e5.getSceneY() - mouseY1);
            }
            metal.playElectrons();
            nonMetal.playElectrons();

        });
        g.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                Label l = new Label();
                l.setText(ion.toString());
                l.setStyle("-fx-background-color: transparent; -fx-padding: 5px;");
                l.setLayoutX(e.getSceneX());
                l.setLayoutY(e.getSceneY());
                animationPane.getChildren().add(l);
                PauseTransition p = new PauseTransition(Duration.seconds(3));
                p.setOnFinished(er -> {
                    animationPane.getChildren().remove(l);
                });
                p.play();

            }
        });
        metal.ion.setOnMouseClicked(e -> {
        });
        nonMetal.ion.setOnMouseClicked(e -> {
        });

        ionListView.getItems().add(ion);
        current = null;


        metal.groupIon = g;
        nonMetal.groupIon = g;

    }

    private void isIonCompatible(Ion i1, Ion i2) {
        if (i1.isMetal != i2.isMetal) {
            if (i1.counterOfBonds != i1.numOfBonds || i2.counterOfBonds != i2.numOfBonds) {
                ft.stop();
                animateThe2IonsReaction(i1, i2);
            } else {
                label1.setText("There are no more valence electrons");
                label1.setVisible(true);
                timeline.play();
            }
        } else {
            String s;
            if (i1.isMetal) {
                s = "metals";
            } else {
                s = "non-metals";
            }
            label1.setText("Both elements are " + s);
            label1.setVisible(true);
            timeline.play();
        }
    }

    @FXML
    void changeTo3D(ActionEvent event) throws IOException {
        fxmlLoader = FXMLLoader.load(HelloApplication.class.getResource("Ionic-3D.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader, 1200, 800, true);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void resetAnimation(ActionEvent event) {
        boxIons.getChildren().clear();
        if (isItMetal) {
            resetButton.setText("Non-Metals");
            for (int i = 0; i < ions.length; i++) {
                boxIons.getChildren().add(nonMetalButtons[i]);
            }
            isItMetal = false;
        } else {
            resetButton.setText("Metals");
            isItMetal = true;
            for (int i = 0; i < metals.length; i++) {
                boxIons.getChildren().add(metalButtons[i]);
            }
        }

    }

}
