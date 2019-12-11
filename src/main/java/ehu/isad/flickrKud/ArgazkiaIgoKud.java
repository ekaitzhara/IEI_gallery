package ehu.isad.flickrKud;

import ehu.isad.Main;
import ehu.isad.db.ArgazkiDBKud;
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

        //para onclik
        /*
        String filePath = "";
        for(File fitxategi : igotakoFitxategiak){
            filePath = fitxategi.getPath();
            File newFile = new File(tmpPath+fitxategi.getName());
            copyFileUsingStream(fitxategi,newFile);
            ObsArgazkiIgo obs = new ObsArgazkiIgo(filePath);
            data.add(obs);


        }

         */

        //igotakoakTabla.setItems(data);
        //igotakoakTabla.setItems();

        //para hacer ventana dinamica
        //texto.setText("aaaa");
        //ui1();
        //Actualizar texto estado para que diga que ha pasado
    }

    private void uploadPhotosWithoutApi(ArrayList<String> photoPaths) throws IOException {
        // 1. argazkiak datu basera, id izan gabe
        // argazkien kopia sortu temp fitxategian
        // argazkiak db-ra igo baina id gabe
        // argazkiak datu egituran sartu
        // igoko beharko den argazki izena eta id txt batean gorde
        String dest = this.getClass().getResource("/data/dasiteam/flickr/tmp").getPath();
        for(String path:photoPaths){
            Laguntzaile.copyFileUsingStream(path,dest);//copy file to temp


            // Argazkia datu basean sartu
            Integer idDB = 0;
            String pIzena = Laguntzaile.getFileName(path);
            String deskribapena = null;
            Date data = null; //TODO poner fecha de creacion de imagen extraida de metadatos
            String idFLickr = null;

            boolean gogokoaDa = false;
            String sortzaileID = null;
            String pUrl = null;
            Integer pFavs = null;
            Integer pKomentario = null;
            ArrayList<Etiketa> etiketaLista = null;
            Integer pViews = null;
            ArgazkiDBKud.getInstantzia().argazkiaSartu();
            public void argazkiaSartu(Integer idDB, String pIzena, String deskribapena, Date data, String idFLickr, boolean gogokoaDa, String sortzaileID) {

                //Argazkia zein bilduman dagoen adierazi
            String bilIzena = bildumak.getValue().toString();

            // Argazkia datu egituran sartu
            Argazkia foto = new Argazkia(pIzena, deskribapena, data, idFLickr, gogokoaDa, sortzaileID, pUrl, pFavs, pKomentario, etiketaLista, pViews);

            //Argazkia Bilduman sartu

                    // crear foto en datu egitura, hacer nuevo eraikitzaile
            // subir esa foto a la base de datos

            // mirar si la bilduma esta en la DB
                // si esta pillar su id
                // else crearla
            //cuando sube la foto a la base de datos, pillo la id de la DB y
            // editar el txt
        }
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

    private void insertPhotoDB(Integer id, String izena, String deskribapena, Date data, String idFLickr, String sortzaileID){

        ArgazkiDBKud.getInstantzia().argazkiaSartu(id, izena, deskribapena, data, idFLickr, gogokoaDa, sortzaileID);
    }



/*
    //por si hace falta, si no se borra
    private void ui1(){
        loadUI("ui1");
    }

    private void loadUI(String ui){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource( "/view/"+ ui + ".fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        bordes.setLeft(root);
    }

 */

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