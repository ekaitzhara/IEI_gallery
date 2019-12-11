package ehu.isad.flickrKud;

import ehu.isad.Main;
import ehu.isad.db.ArgazkiDBKud;
import ehu.isad.db.BildumaDBKud;
import ehu.isad.flickr.FlickrAPI;
import ehu.isad.model.ObsArgazkiIgo;
import ehu.isad.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;

import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ArgazkiaIgoKud implements Initializable {

    // Reference to the main application.
    private Main mainApp;
    private List<ObsArgazkiIgo> obsDatuak = new ArrayList<>();
    private ObservableList<ObsArgazkiIgo> igoModel = FXCollections.observableArrayList();

    @FXML
    private TableView<ObsArgazkiIgo> igotakoakTabla;

    @FXML
    private TableColumn<ObsArgazkiIgo, String> izena;

    @FXML
    private TableColumn<ObsArgazkiIgo, Button> botoia;


    @FXML
    private ComboBox bildumak = new ComboBox();

    @FXML
    private Label textoEstado; //dice si los artxibos se han subido a flickr o que solo se han subido a la app
    private Text uploadedFiles; //TODO texto de archivos que se van a subir a flickr

    public void setMainApp(Main main) {
        this.mainApp = main;
    }

    @FXML
    private void cleanList(ActionEvent actionEvent) throws Exception {
        System.out.println("cleanList");
        this.obsDatuak.clear();
    }

    @FXML
    private void uploadFiles(ActionEvent actionEvent) throws Exception {
        System.out.println("uploadFiles");
        FlickrAPI api = FlickrAPI.getInstantzia();
        ArrayList<String> photoPaths = new ArrayList<>();
        for(ObsArgazkiIgo data:obsDatuak){
            photoPaths.add(data.getPath());
        }
        try {
            if (api.hasConection()) { //aplikazioa apiarekin konekzioa badu ikusiko dugu
                uploadPhotosToApi(photoPaths);
                this.obsDatuak.clear();
            } else {
                uploadPhotosWithoutApi(photoPaths);
                this.obsDatuak.clear();
            }
        }catch (Exception e){
            //errorerenbat gertatu da, segurazki konexioa joan dela apia irekita zegoelarik
            // errore bat egon dela abisatu erabiltzaileari
            mainApp.erroreaBistaratu("uploadError");
            uploadPhotosWithoutApi();
        }

    }

    @FXML
    private void handleDragOver(DragEvent event){
        if(event.getDragboard().hasFiles()){
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    @FXML
    private void handleDrop(DragEvent event) throws IOException {
        // este metodo es para descargar la imagen a una carpeta de dentro del programa llamada temp
        // Pero hay muchas formas de hacerlo, por ejemplo subir la imagen a flickr directamente y descargarla a la direccion de las imagenes( asi quedaria tal cual la queremos almacenar)
        // La funcion con tem las pasaria a la carpeta temporal y de ahí las pasa a flickr, base de datos o donde toque
        List<File> files = event.getDragboard().getFiles();

        for(File fitxategi : files){
            obsDatuak.add(new ObsArgazkiIgo(fitxategi.getName(),fitxategi));
        }
        igoModel = FXCollections.observableArrayList(obsDatuak);
        igotakoakTabla.setItems(igoModel);
        igotakoakTabla.refresh();
    }

    private void uploadPhotosToApi(ArrayList<String> photoPaths) throws IOException {
        //this.igotakoFitxategiak
        //Argazkiak flickr-era igoko dira
        for(String path:photoPaths) {
            String bilIzena = bildumak.getValue().toString(); //meter en la DB la bilduma a la que subimos la foto
            String sortuDenFlickrID = FlickrAPI.getInstantzia().argazkiaIgo(path, bilIzena);
            //izena, deskribapena, Date data, String idFLickr, boolean gogokoaDa, String sortzaileID, String pUrl, Integer pFavs, Integer pKomentario, ArrayList<Etiketa> etiketaLista, Integer pViews
        }

    }

    private void uploadPhotosWithoutApi(ArrayList<String> photoPaths) throws IOException {
        // Argazkiak programara igotzeko baina flickerreko konexioa ondo ez doanean
        String dest = this.getClass().getResource("/data/dasiteam/flickr/tmp").getPath();
        for(String photo:photoPaths){  // foto == argazkiaren path
            // ARGAZKIEN ATRIBUTUAK
            // Atributos que no usamos
                String deskribapena = null; //de primeras se suben sin descripcion
                Date date = null; //TODO poner fecha de creacion de imagen extraida de metadatos
                boolean gogokoaDa = false; //de primeras siempre false
                String sortzaileID = null; //TODO esta me tienen con dudas de cuando la tengo que usar
            // Atributos not null: Son obligatorios de pasar a la DB
                Integer idDB; //id con autoincrement para DB
                String pIzena = Laguntzaile.getFileName(photo); //el nombre viene del mismo archivo
            // Atributos que varian por conexion
                String idFLickr = null;
            // Atributos que no estan en la base de datos: para futuros usos, pero que actualmente no utilizamos
                String size = null;
                Integer pFavs = null;
                Integer pKomentario = null;
                String pUrl = null;
            ArrayList<Etiketa> etiketaLista = null;
            Integer pViews = null;


            // DATU EGITURA: Argazkia sortu eta bilduman sartu
            int photoId = aDEgituranSartu(pIzena, deskribapena, date, idFLickr, gogokoaDa, sortzaileID, pUrl, pFavs, pKomentario, etiketaLista, pViews);
            // DATU BASEA: Argazkia eta bilduma igo, kontuan izanik ez dugula konexiorik
            aDBaseanSartu();
            //copy file to temp
            Laguntzaile.copyFileUsingStream(photo,dest);
            //photos to upload txt fitxategian argazkia sartu
            Laguntzaile.addDataToFile();
        }
    }

    private Integer aDEgituranSartu(String pIzena, String deskribapena, Date date, String idFLickr, boolean gogokoaDa, String sortzaileID, String pUrl, Integer pFavs, Integer pKomentario, ArrayList<Etiketa> etiketaLista, Integer pViews){
        Argazkia foto = new Argazkia(pIzena, deskribapena, date, idFLickr, gogokoaDa, sortzaileID, pUrl, pFavs, pKomentario, etiketaLista, pViews);
        String albumName = bildumak.getValue().toString();
        Bilduma uploadAlbum = ListaBildumak.getNireBilduma().emanBildumaIzenarekin(albumName);
        if(uploadAlbum==null){
            uploadAlbum.argazkiaGehitu(foto);
        }else{
            Bilduma newAlbum = ListaBildumak.getNireBilduma().bildumaSartu(albumName);
            newAlbum.argazkiaGehitu(foto);
        }
        return foto.getId();
    }


    private void aDBaseanSartu(Integer idDB, String photoIzena, String idFLickr, String albumName) {
        // metodo hau argazkiak eta bildumak datu basean sortzeko balio du
        // konexio gabe eta konezioarekin lan egingo du, baina era ezberdinetan

        ArgazkiDBKud.getInstantzia().argazkiaSartu(idDB, photoIzena, deskribapena, data, idFLickr, gogokoaDa, sortzaileID, pFavs, pKomentario);

        //Argazkia zein bilduman dagoen adierazi
        String bilIzena = bildumak.getValue().toString();

        List<String> bildumak = ListaBildumak.getNireBilduma().lortuBildumenIzenak();
        if(bildumak.contains(bilIzena)){
            // Bilduma hori jada sortuta dago, beraz zuzenean editatu BildumaArgazkia
            BildumaDBKud.getInstantzia().argazkiaBildumanSartu(bilIzena,photoId);
        }else{
            // Bilduma berria sortu eta argazkia sartu
            BildumaDBKud.getInstantzia().bildumaSartu(bilIzena,mainApp.username,"");
            BildumaDBKud.getInstantzia().argazkiaBildumanSartu(bilIzena,photoId);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(this.getClass().getResource("/data/dasiteam/flickr/tmp/thumb-1920-707960.png"));

        izena.setCellValueFactory(new PropertyValueFactory<>("izena"));
        botoia.setCellValueFactory(new PropertyValueFactory<>("botoia"));
        System.out.println("igo iniciado");
        //igotakoakTabla.getColumns();

        List<String> bil = ListaBildumak.getNireBilduma().lortuBildumenIzenak();
        List<String> str = new ArrayList<>();
        str.add("bil1");
        str.add("bil2");

        this.bildumak.getItems().addAll(bil);
        System.out.println("str añadidos");
    }

}