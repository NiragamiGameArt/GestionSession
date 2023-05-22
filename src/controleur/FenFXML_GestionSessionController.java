package controleur;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import modele.GestionSql;
import modele.Session;

public class FenFXML_GestionSessionController implements Initializable {

    @FXML
private TableView<Session> tableSessions;

@FXML
private TableColumn<Session, Integer> colonneId;

@FXML
private TableColumn<Session, String> colonneLibFormation;

@FXML
private TableColumn<Session, Date> colonneDateDebut;

@FXML
private TableColumn<Session, Integer> colonneNbPlaces;

@FXML
private TableColumn<Session, Integer> colonneNbInscrits;

@FXML
Button btnPDF;

Stage secondaryStage;

    private ObservableList<Session> sessions;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialisation du TableView et des colonnes
    colonneId.setCellValueFactory(new PropertyValueFactory<>("id"));
    colonneLibFormation.setCellValueFactory(new PropertyValueFactory<>("libFormation"));
    colonneDateDebut.setCellValueFactory(new PropertyValueFactory<>("date_debut"));
    colonneNbPlaces.setCellValueFactory(new PropertyValueFactory<>("nb_places"));
    colonneNbInscrits.setCellValueFactory(new PropertyValueFactory<>("nb_inscrits"));    
    tableSessions.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Session>()
        {
            @Override
            public void changed(ObservableValue<? extends Session> observable, Session oldValue, Session newValue)
            {
                // Si une ligne sélectionnée alors
                if (newValue == null)
                {
                    btnPDF.setVisible(false);
                }
                else
                {
                    btnPDF.setVisible(true);
                }
            }
        });

    // Récupérer les sessions et les ajouter à la TableView
    ObservableList<Session> sessions = GestionSql.getLesSessionsToutes();
    tableSessions.setItems(sessions);
    }

    @FXML
     public void handleGestionPDF() 
    {
    // Conservation de l'item selectionné dans le TableView dans MainApp
    MainApp.setMaSessionSelectionnee(tableSessions.getSelectionModel().getSelectedItem());
    try {
        secondaryStage = new Stage();
            secondaryStage.setTitle("Confirmation de Session");
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/vue/FenFXML_GenerPDF.fxml"));
            //Session maSession = (Session)tableSessions.getSelectionModel().getSelectedItem();
            AnchorPane rootLayout = (AnchorPane) loader.load();
            Scene scene = new Scene(rootLayout);
            secondaryStage.setScene(scene);
            secondaryStage.show();
        }
        catch (IOException e)
        {
            System.out.println("Erreur chargement seconde fenetre : " + e.getMessage());
        }
}
}