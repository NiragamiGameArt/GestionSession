/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import modele.Client;
import modele.GestionSql;
import modele.Session;

/**
 * FXML Controller class
 *
 * @author petit
 */
public class FenFXML_GenerPDFController implements Initializable
{
@FXML
private TableView<Client> ListeInscrits;
@FXML
private TableColumn<Client, String> colonneNom;

@FXML
private TableColumn<Client, String> colonneAdresse;

@FXML
private TableColumn<Client, String> colonneCP;

@FXML
private TableColumn<Client, String> colonneVille;

@FXML
private TableColumn<Client, String> colonneEmail;



    private ObservableList<Client> Clients;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // Ajout des inscrits de la session dans la tableView ListeInscrit
        ListeInscrits.setItems(GestionSql.getInscritSession(MainApp.getMaSessionSelectionnee().getId()));
        // Initialise le TableView ListeInscrits
        colonneNom.setCellValueFactory(new PropertyValueFactory<Client, String>("nom"));
        colonneAdresse.setCellValueFactory(new PropertyValueFactory<Client, String>("adresse"));
        colonneCP.setCellValueFactory(new PropertyValueFactory<Client, String>("cp"));
        colonneVille.setCellValueFactory(new PropertyValueFactory<Client, String>("ville"));
        colonneEmail.setCellValueFactory(new PropertyValueFactory<Client, String>("email"));
    }
    @FXML
    public void handleGenerPDF() throws FileNotFoundException, DocumentException, IOException {
        Session sessionSelect = MainApp.getMaSessionSelectionnee();
        //Création de l'instance de Document.
        Document document = new Document();
        //Création d'une instance de OutputStream.
        String home = System.getProperty("user.home");
        OutputStream outputStream = new FileOutputStream(new File(home + "/Downloads/Emargement_" + sessionSelect.getId() + ".pdf"));
        //Création de l'instance PDFWriter
        PdfWriter.getInstance(document, outputStream);
        //Ouverture du document.
        document.open();
        //Ajout d'un titre
        Paragraph titreSession = new Paragraph(sessionSelect.getLibFormation());
        titreSession.setAlignment(Element.ALIGN_CENTER);
        document.add(titreSession);
        //Ajout du nombres de places restantes
        Paragraph nbPlacesInscrits = new Paragraph("Places restantes : " + (sessionSelect.getNb_places() - sessionSelect.getNb_inscrits()));
        nbPlacesInscrits.setAlignment(Element.ALIGN_CENTER);
        document.add(nbPlacesInscrits);
        //Ajout du nombres d'inscrits
        Paragraph nbInscrits = new Paragraph("Nombre d'inscrits : " + sessionSelect.getNb_inscrits());
        nbInscrits.setAlignment(Element.ALIGN_CENTER);
        document.add(nbInscrits);
        Paragraph vide = new Paragraph("\n");
        vide.setAlignment(Element.ALIGN_CENTER);
        document.add(vide);
        //Création du tableau. Ici 6 colonnes
        PdfPTable pdfPTable = new PdfPTable(6);
        //Création des cellules du tableau
        PdfPCell pdfPCell1 = new PdfPCell(new Paragraph("Nom"));
        PdfPCell pdfPCell2 = new PdfPCell(new Paragraph("Adresse"));
        PdfPCell pdfPCell3 = new PdfPCell(new Paragraph("Cp"));
        PdfPCell pdfPCell4 = new PdfPCell(new Paragraph("Ville"));
        PdfPCell pdfPCell5 = new PdfPCell(new Paragraph("Email"));
        PdfPCell pdfPCell6 = new PdfPCell(new Paragraph("Signature"));
        //Ajout des cellules dans le tableau
        pdfPTable.addCell(pdfPCell1);
        pdfPTable.addCell(pdfPCell2);
        pdfPTable.addCell(pdfPCell3);
        pdfPTable.addCell(pdfPCell4);
        pdfPTable.addCell(pdfPCell5);
        pdfPTable.addCell(pdfPCell6);
        
        for (Client c : ListeInscrits.getItems()) {            
            pdfPCell1 = new PdfPCell(new Paragraph(c.getNom()));
            pdfPCell2 = new PdfPCell(new Paragraph(c.getAdresse()));
            pdfPCell3 = new PdfPCell(new Paragraph(c.getCp()));
            pdfPCell4 = new PdfPCell(new Paragraph(c.getVille()));
            pdfPCell5 = new PdfPCell(new Paragraph(c.getEmail()));
            //Ajout des cellules dans le tableau
            pdfPTable.addCell(pdfPCell1);
            pdfPTable.addCell(pdfPCell2);
            pdfPTable.addCell(pdfPCell3);
            pdfPTable.addCell(pdfPCell4);
            pdfPTable.addCell(pdfPCell5);
            pdfPTable.addCell("");
        }
        //Ajout du tableau dans le document
        document.add(pdfPTable);
        //Fermeture du document et du outputStream.
        document.close();
        outputStream.close();
        System.out.println("Pdf créé avec succès !");
    }
}   