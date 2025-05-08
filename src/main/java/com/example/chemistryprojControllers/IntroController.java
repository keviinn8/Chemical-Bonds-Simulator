package com.example.chemistryprojControllers;

import com.example.chemistryproj.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IntroController implements Initializable {

    @FXML
    private Button moleculeBtn;

    @FXML
    private Button ionicBtn;

    private Stage stage;
    private Scene scene;
    private Parent fxmlLoader;

    public void initialize(URL location, ResourceBundle resources) {
//        //File file = new File("C:\\Users\\Kevin\\IdeaProjects\\ChemistryProj\\src\\main\\java\\com\\example\\giphy.gif");
//        Image moleculeGif = new Image("file:src/main/resources/giphy.gif");
//        ImageView imageView = new ImageView(moleculeGif);
//        imageView.setFitHeight(220);
//        imageView.setFitWidth(260);
//        moleculeBtn.setGraphic(imageView);
//        //File file2 = new File("C:\\Users\\Kevin\\IdeaProjects\\ChemistryProj\\src\\main\\java\\com\\example\\giphy2.gif");
//        Image ionicGif = new Image("file:src/main/resources/giphy2.gif");
//        ImageView imageView2 = new ImageView(ionicGif);
//        imageView2.setFitHeight(220);
//        imageView2.setFitWidth(260);
//        ionicBtn.setGraphic(imageView2);
    }

    public void switchToMolecule(ActionEvent event) throws IOException {
        fxmlLoader = FXMLLoader.load(HelloApplication.class.getResource("Molecular-Scene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader);
        stage.setScene(scene);
        stage.show();

    }

    public void switchToIonic(ActionEvent event) throws IOException {
        fxmlLoader = FXMLLoader.load(HelloApplication.class.getResource("Ionic-Scene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader);
        stage.setScene(scene);
        stage.show();

    }

}