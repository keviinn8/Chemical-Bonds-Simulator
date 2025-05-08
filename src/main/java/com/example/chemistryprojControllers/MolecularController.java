package com.example.chemistryprojControllers;

import com.example.chemistryproj.Atom;
import com.example.chemistryproj.HelloApplication;
import com.example.chemistryproj.Molecule;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MolecularController{

    @FXML
    private GridPane periodicTable;
    @FXML
    private Pane moleculePane;
    @FXML
    private ListView<String> molList;
    public Label messageLabel;
    private Atom atomClicked = null;
    // Variables to store selected atoms for each position
    private Atom topLeftAtom = null;
    private Atom topRightAtom = null;
    private Atom bottomLeftAtom = null;
    private Atom bottomRightAtom = null;
    private Atom centerAtom = null;
    private static final Duration ANIMATION_DURATION = Duration.seconds(1);
    private static final double DISTANCE_BETWEEN_ATOMS = 64.0;
    private Stage stage;
    private Scene scene;
    private Parent fxmlLoader;
    private Random random = new Random();
    private static final String[][] elements = {
            {"H", "", "", "", "", "", "", "", "",  "He"},
            {"Li", "Be", "", "",  "B", "C", "N", "O", "F", "Ne"},
            {"Na", "Mg", "", "",  "Al", "Si", "P", "S", "Cl", "Ar"},
            {"K", "Ca", "", "",  "Ga", "Ge", "As", "Se", "Br", "Kr"}
    };

    public void initialize() {
        populateElementsGrid();
//        moleculePane.setOnMouseClicked(event -> handleAtomClicked(event));
    }

    public void backToIntro(ActionEvent event) throws IOException {
        fxmlLoader = FXMLLoader.load(HelloApplication.class.getResource("Intro-Scene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader);
        stage.setScene(scene);
        stage.show();
    }

    private void populateElementsGrid() {
        for (int row = 0; row < elements.length; row++) {
            for (int col = 0; col < elements[row].length; col++) {
                String elementName = elements[row][col];
                if (!elementName.isEmpty()) {
                    Button button = new Button(elementName);
                    button.setMinSize(50, 50);
                    button.setOnAction(ActionEvent -> handleElementButtonPress());
                    periodicTable.add(button, col, row);
                }
            }
        }
    }

    // Method to handle button presses for displaying atoms

    @FXML
    private void handleElementButtonPress() {
        Button clickedButton = (Button) periodicTable.getScene().getFocusOwner();
        String elementSymbol = clickedButton.getText();
        displayAtom(elementSymbol);
    }

    // Method to display the atom representation in the pane

    private void displayAtom(String elementSymbol) {
        Atom atom = createAtom(elementSymbol);
        moleculePane.getChildren().add(atom);
    }

    // Method to create atom representation based on the element symbol

    private Atom createAtom(String elementSymbol) {
        Atom atom = Atom.createAtomFromElementSymbol(elementSymbol);
        changeLocation(atom);
        return atom;
    }

    private Atom changeLocation(Atom atom){
        double paneWidth = moleculePane.getWidth();
        double paneHeight = moleculePane.getHeight();
        double atomWidth = atom.getWidth();
        double atomHeight = atom.getHeight();

        // Calculate the range for random positions
        double minX = paneWidth * 0.25; // 25% from the left edge
        double minY = paneHeight * 0.25; // 25% from the top edge
        double maxX = paneWidth * 0.75 - atomWidth; // 25% from the right edge
        double maxY = paneHeight * 0.75 - atomHeight; // 25% from the bottom edge

        // Generate random positions within the adjusted range
        double randomX = minX + random.nextDouble() * (maxX - minX);
        double randomY = minY + random.nextDouble() * (maxY - minY);

        atom.relocate(randomX, randomY);

        return atom;
    }

    // Method to clear the moleculePane

    @FXML
    private void clearMoleculePane() {
        moleculePane.getChildren().clear();
        atomClicked = null;
        topLeftAtom = null;
        topRightAtom = null;
        bottomLeftAtom = null;
        bottomRightAtom = null;
        centerAtom = null;
    }

    public void covalentInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Covalent Bonds Theory");
        alert.setContentText("Covalent bond, in chemistry, the interatomic linkage that results from the sharing of an " +
                "electron pair between two atoms. The binding arises from the electrostatic attraction of their nuclei " +
                "for the same electrons. A covalent bond forms when the bonded atoms have a lower total energy than " +
                "that of widely separated atoms.");
        alert.showAndWait();
    }

    public void nobleInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Noble Atoms Theory");
        alert.setContentText("The Noble Atoms are the atoms that are stable and don't have any single electrons." +
                "Therefore, the Noble Atoms cannot be bonded with any other atom.");
        alert.showAndWait();
    }

    public void applicationInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information About The Application");
        alert.setContentText("1) Pick the appropriate atoms from the periodic table presented to you. " +
                "\n 2) Click the react button and watch it bond together. " +
                "\n 3) Select the pre-done molecules if you want" +
                "\n 4) Once you have the molecule, click the 3d button to view the molecule in 3d.");
        alert.showAndWait();
    }

    public void applicationHowTo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("How To Use This Application");
        alert.setContentText("After to pick which atoms to work with, you can pick up to five: " +
                "\n - Double-click the atom to select it, it should turn green " +
                "\n - Right-click a selected atom to delete it from the blue area" +
                "\n - Press Separate to undo the react made" +
                "\n - Press reset to clear the blue area");
        alert.showAndWait();
    }

@FXML
    private void handleAtomClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            for (Node node : moleculePane.getChildren()) {
                if (node instanceof Atom && node.getBoundsInParent().contains(event.getX(), event.getY())) {
                    atomClicked = (Atom) node;

                    if (!atomClicked.isSelected()) {
                        // Select the clicked atom
                        atomClicked.nucleus.setFill(Color.GREEN);
                        atomClicked.nucleus.setStroke(Color.BLACK);

                        // Determine the position based on the number of currently selected atoms
                        if (topLeftAtom == null) {
                            // Top left
                            atomClicked.setLayoutX(moleculePane.getWidth() / 4);
                            atomClicked.setLayoutY(moleculePane.getHeight() / 4);
                            topLeftAtom = atomClicked;
                        } else if (topRightAtom == null) {
                            // Top right
                            atomClicked.setLayoutX(3 * moleculePane.getWidth() / 4);
                            atomClicked.setLayoutY(moleculePane.getHeight() / 4);
                            topRightAtom = atomClicked;
                        } else if (bottomLeftAtom == null) {
                            // Bottom left
                            atomClicked.setLayoutX(moleculePane.getWidth() / 4);
                            atomClicked.setLayoutY(3 * moleculePane.getHeight() / 4);
                            bottomLeftAtom = atomClicked;
                        } else if (bottomRightAtom == null) {
                            // Bottom right
                            atomClicked.setLayoutX(3 * moleculePane.getWidth() / 4);
                            atomClicked.setLayoutY(3 * moleculePane.getHeight() / 4);
                            bottomRightAtom = atomClicked;
                        } else if (centerAtom == null) {
                        // Center
                        atomClicked.setLayoutX(moleculePane.getWidth() / 2);
                        atomClicked.setLayoutY(moleculePane.getHeight() / 2);
                        centerAtom = atomClicked;
                    }
                    } else {
                        // Deselect the clicked atom
                        atomClicked.nucleus.setFill(Color.BLACK);
                        atomClicked.nucleus.setStroke(Color.BLACK);
                        changeLocation(atomClicked);
                        removeAtomFromSelection(atomClicked);
                    }
                    break;
                }
            }
        } else if (event.getButton() == MouseButton.SECONDARY) {
            // Right-click to remove the selected atom
            for (Node node : moleculePane.getChildren()) {
                if (node instanceof Atom && node.getBoundsInParent().contains(event.getX(), event.getY())) {
                    Atom clickedAtom = (Atom) node;
                    moleculePane.getChildren().remove(clickedAtom);
                    removeAtomFromSelection(clickedAtom);
                    break;
                }
            }
        }
    }

    // Helper method to remove an atom from the selected atoms
    private void removeAtomFromSelection(Atom atomToRemove) {
        if (atomToRemove == topLeftAtom) {
            topLeftAtom = null;
        } else if (atomToRemove == topRightAtom) {
            topRightAtom = null;
        } else if (atomToRemove == bottomLeftAtom) {
            bottomLeftAtom = null;
        } else if (atomToRemove == bottomRightAtom) {
            bottomRightAtom = null;
        } else if (atomToRemove == centerAtom) {
            centerAtom = null;
        }
    }

    private Atom findCentralAtom() {
        Atom centralAtom = null;
        int maxAtomGroup = Integer.MIN_VALUE;

        for (Node node : moleculePane.getChildren()) {
            if (node instanceof Atom && ((Atom) node).isSelected()) {
                Atom atom = (Atom) node;
                int atomGroup = atom.getAtomGroup(atom.name);
                if (atomGroup > maxAtomGroup) {
                    maxAtomGroup = atomGroup;
                    centralAtom = atom;
                }
            }
        }

        return centralAtom;
    }
    private List<String> allReactions = new ArrayList<>();

    @FXML
    public void atomsReact() {
        if (isNobleAtomSelected()) {
            createMessage("Can't react with a noble atom");
        } else if (atomClicked == null) {
            createMessage("Select atoms!");
        } else {
            StringBuilder reactionStringBuilder = new StringBuilder();
            // Find the atom with the most single electrons
            Atom centralAtom = findCentralAtom();
            reactionStringBuilder.append(centralAtom.name).append(" ");

            // Check the atomGroup of the central atom
            int centralAtomGroup = centralAtom.getAtomGroup(centralAtom.name);
            int maxReactingAtoms = centralAtomGroup;

            double centerX = moleculePane.getWidth() / 2.0;
            double centerY = moleculePane.getHeight() / 2.0;

            // Calculate the target position for the central atom (placed in the middle of the pane)
            double targetX = centerX;
            double targetY = centerY;

            // Create a transition for the central atom to move to the center of the pane
            TranslateTransition centralTransition = new TranslateTransition(ANIMATION_DURATION, centralAtom);
            centralTransition.setToX(targetX - centralAtom.getLayoutX());
            centralTransition.setToY(targetY - centralAtom.getLayoutY());

            // Create a parallel transition to handle all atom movements
            ParallelTransition parallelTransition = new ParallelTransition();
            parallelTransition.getChildren().add(centralTransition);

            // Get the placement angles for other selected atoms
            int[] angles = {0, 180, 90, 270};

            for (Node node : moleculePane.getChildren()) {
                if (node instanceof Atom && node != centralAtom && ((Atom) node).isSelected() && maxReactingAtoms > 0) {
                    Atom atom = (Atom) node;
                    if (atom.getAtomGroup(atom.name) == 1){
                        atom.removeElectronAtAngle(0);
                    }

                    // Calculate the target position based on the remaining available positions
                    double angle = Math.toRadians(angles[maxReactingAtoms - 1]);
                    maxReactingAtoms--;

                    // Calculate the target position for the atom based on its angle
                    targetX = centerX + (DISTANCE_BETWEEN_ATOMS * Math.cos(angle));
                    targetY = centerY + (DISTANCE_BETWEEN_ATOMS * Math.sin(angle));

                    // Create a transition for the atom to move to the target position
                    TranslateTransition transition = new TranslateTransition(ANIMATION_DURATION, atom);
                    transition.setToX(targetX - atom.getLayoutX());
                    transition.setToY(targetY - atom.getLayoutY());

                    // Add the transition to the parallel transition
                    parallelTransition.getChildren().add(transition);

                    reactionStringBuilder.append(atom.name).append(" ");
                }
            }

            allReactions.add(reactionStringBuilder.toString());
            ObservableList<String> listItems = FXCollections.observableArrayList(allReactions);
            molList.setItems(listItems);

            // Play the parallel transition
            parallelTransition.play();
        }
    }

    private boolean isNobleAtomSelected() {
        for (Node node : moleculePane.getChildren()) {
            if (node instanceof Atom) {
                Atom atom = (Atom) node;
                String elementSymbol = atom.name;
                if (elementSymbol.equals("He") || elementSymbol.equals("Ne") || elementSymbol.equals("Ar") || elementSymbol.equals("Kr")) {
                    return true;
                }
            }
        }
        return false;
    }

    public void createMessage(String string){
        Duration showDuration = Duration.seconds(2);
        Duration fadeDuration = Duration.seconds(1);
        messageLabel = new Label(string);
        messageLabel.setLayoutY(moleculePane.getHeight() - 20);
        moleculePane.getChildren().add(messageLabel);
        PauseTransition pause = new PauseTransition(showDuration);
        pause.setOnFinished(event -> {
            FadeTransition fade = new FadeTransition(fadeDuration, messageLabel);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(event1 -> moleculePane.getChildren().remove(messageLabel));
            fade.play();
        });

        pause.play();
    }

    @FXML
    public void reverseAnimation(){
        ParallelTransition parallelTransition = new ParallelTransition();

        for (Node node : moleculePane.getChildren()) {
            if (node instanceof Atom) {
                Atom atom = (Atom) node;

                // Create a transition for the atom to go back to its original position
                TranslateTransition transition = new TranslateTransition(ANIMATION_DURATION, atom);
                transition.setToX(0); // Move to original X position
                transition.setToY(0); // Move to original Y position

                // Add the transition to the parallel transition
                parallelTransition.getChildren().add(transition);
            }
        }

        // Play the parallel transition
        parallelTransition.play();

    }

    @FXML
    private void addMethane() {
        Molecule methane = Molecule.createMethane();
        moleculePane.getChildren().add(methane);
    }

    @FXML
    private void addWater() {
        Molecule water = Molecule.createWater();
        moleculePane.getChildren().add(water);
    }

    @FXML
    private void addPotassiumFluoride() {
        Molecule potassiumFluoride = Molecule.createPotassiumFluoride();
        moleculePane.getChildren().add(potassiumFluoride);
    }

    @FXML
    private void addAmmonia() {
        Molecule ammonia = Molecule.createAmmonia();
        moleculePane.getChildren().add(ammonia);
    }

    @FXML
    private void addAluminumChloride() {
        Molecule aluminumChloride = Molecule.createAluminumChloride();
        moleculePane.getChildren().add(aluminumChloride);
    }

    @FXML
    private void addCarbonDioxide() {
        Molecule carbonDioxide = Molecule.createCarbonDioxide();
        moleculePane.getChildren().add(carbonDioxide);
    }
}
