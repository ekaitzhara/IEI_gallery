package ehu.iei.flickrKud;

import ehu.iei.Main;
import ehu.iei.db.ArgazkiDBKud;
import ehu.iei.db.BildumaDBKud;
import ehu.iei.flickr.FlickrAPI;
import ehu.iei.flickrKud.Utils;
import ehu.iei.model.ObsArgazkiIgo;
import ehu.iei.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ArgazkiaIgoKud implements Initializable {

    // Reference to the main application.
    private Main mainApp;
    private List<ObsArgazkiIgo> obsDatuak = new ArrayList<>();
    private ObservableList<ObsArgazkiIgo> igoModel = FXCollections.observableArrayList(new ObsArgazkiIgo("nombre", new File("")));

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
        if (bildumak != null) {
            for (ObsArgazkiIgo data : obsDatuak) {
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
            } catch (Exception e) {
                //errorerenbat gertatu da, segurazki konexioa joan dela apia irekita zegoelarik
                // errore bat egon dela abisatu erabiltzaileari
                mainApp.erroreaBistaratu("uploadError");
                //uploadPhotosWithoutApi(photoPaths);
            }
        } else {
            System.out.println("bildumaren bat aukeratu behar duzu");
        }
    }

    @FXML
    private void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    @FXML
    private void handleDrop(DragEvent event) throws IOException {
        // este metodo es para descargar la imagen a una carpeta de dentro del programa llamada temp
        // Pero hay muchas formas de hacerlo, por ejemplo subir la imagen a flickr directamente y descargarla a la direccion de las imagenes( asi quedaria tal cual la queremos almacenar)
        // La funcion con tem las pasaria a la carpeta temporal y de ahí las pasa a flickr, base de datos o donde toque
        List<File> files = event.getDragboard().getFiles();

        for (File fitxategi : files) {
            ObsArgazkiIgo obs = new ObsArgazkiIgo(fitxategi.getName(), fitxategi);
            obsDatuak.add(obs);
        }
        igoModel = FXCollections.observableArrayList(obsDatuak);
        igotakoakTabla.setItems(igoModel);
        //igotakoakTabla.refresh();
    }

    private void uploadPhotosToApi(ArrayList<String> photoPaths) throws IOException {
        //this.igotakoFitxategiak
        //Argazkiak flickr-era igoko dira
        for (String path : photoPaths) {
            // ARGAZKIA FLIKR-ERA IGO
            String bilIzena = bildumak.getValue().toString();
            ArrayList<String> photoAndAlbumId = FlickrAPI.getInstantzia().argazkiaIgo(path, bilIzena);
            String sortuDenFlickrID = photoAndAlbumId.get(0);
            String sortuDenAlbumID = null;
            if (photoAndAlbumId.size() == 2) {
                sortuDenAlbumID = photoAndAlbumId.get(1);
            }
            FlickrAPI.getInstantzia().argazkiaDeskargatu(Utils.argazkiakPath, sortuDenFlickrID, "small");// non gorde, id, tamaina

            // ARGAZKIEN ATRIBUTUAK
            // Atributos que no usamos
            String deskribapena = null; //de primeras se suben sin descripcion
            Date date = null; //TODO poner fecha de creacion de imagen extraida de metadatos
            boolean gogokoaDa = false; //de primeras siempre false
            String sortzaileID = null; //TODO esta me tienen con dudas de cuando la tengo que usar
            // Atributos not null: Son obligatorios de pasar a la DB
            Integer idDB; //id con autoincrement para DB
            String pIzena = Utils.getFileName(path); //el nombre viene del mismo archivo
            // Atributos que varian por conexion
            // Atributos que no estan en la base de datos: para futuros usos, pero que actualmente no utilizamos
            String size = null;
            Integer pFavs = null;
            Integer pKomentario = null;
            String pUrl = null;
            ArrayList<Etiketa> etiketaLista = null;
            Integer pViews = null;

            // DATU EGITURA: Argazkia sortu eta bilduman sartu
            AbstractMap.SimpleEntry<Argazkia, Bilduma> argazkiEtaBilduma = aDEgituranSartu(pIzena, deskribapena, date, sortuDenFlickrID, gogokoaDa, sortzaileID, pUrl, pFavs, pKomentario, etiketaLista, pViews, sortuDenAlbumID);
            // DATU BASEA: Argazkia eta bilduma igo, kontuan izanik ez dugula konexiorik
            aDBaseanSartu(argazkiEtaBilduma.getKey(), argazkiEtaBilduma.getValue());
        }

    }

    private void uploadPhotosWithoutApi(ArrayList<String> photoPaths) throws IOException {
        // Argazkiak programara igotzeko baina flickerreko konexioa ondo ez doanean
        for (String photo : photoPaths) {  // foto == argazkiaren path
            // ARGAZKIEN ATRIBUTUAK
            // Atributos que no usamos
            String deskribapena = null; //de primeras se suben sin descripcion
            Date date = null; //TODO poner fecha de creacion de imagen extraida de metadatos
            boolean gogokoaDa = false; //de primeras siempre false
            String sortzaileID = null; //TODO esta me tienen con dudas de cuando la tengo que usar
            // Atributos not null: Son obligatorios de pasar a la DB
            Integer idDB; //id con autoincrement para DB
            String pIzena = Utils.getFileName(photo); //el nombre viene del mismo archivo
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
            AbstractMap.SimpleEntry<Argazkia, Bilduma> argazkiEtaBilduma = aDEgituranSartu(pIzena, deskribapena, date, idFLickr, gogokoaDa, sortzaileID, pUrl, pFavs, pKomentario, etiketaLista, pViews, null);
            // DATU BASEA: Argazkia eta bilduma igo, kontuan izanik ez dugula konexiorik
            aDBaseanSartu(argazkiEtaBilduma.getKey(), argazkiEtaBilduma.getValue());
            //copy file to temp
            Utils.copyFileUsingStream(photo, Utils.tmpPath);
            //photos to upload txt fitxategian argazkia sartu
            ArgazkiDBKud.getInstantzia().addPhotoToUpload(pIzena + ".png", argazkiEtaBilduma.getKey().getId().toString(), argazkiEtaBilduma.getValue().getIzena().toString());
        }
    }

    private AbstractMap.SimpleEntry<Argazkia, Bilduma> aDEgituranSartu(String pIzena, String deskribapena, Date date, String idFLickr, boolean gogokoaDa, String sortzaileID, String pUrl, Integer pFavs, Integer pKomentario, ArrayList<Etiketa> etiketaLista, Integer pViews, String bilID) {
        Argazkia foto = new Argazkia(pIzena, deskribapena, date, idFLickr, gogokoaDa, sortzaileID, pUrl, pFavs, pKomentario, etiketaLista, pViews);
        String albumName = bildumak.getValue().toString();
        Bilduma uploadAlbum = ListaBildumak.getNireBilduma().emanBildumaIzenarekin(albumName);
        if (uploadAlbum != null) { // albuma dago
            uploadAlbum.argazkiaGehitu(foto);
            AbstractMap.SimpleEntry<Argazkia, Bilduma> entry = new AbstractMap.SimpleEntry<>(foto, uploadAlbum);
            return entry;
        } else { //albuma ez dago, berria sortu
            Bilduma newAlbum = ListaBildumak.getNireBilduma().bildumaSartu(albumName, bilID);
            newAlbum.argazkiaGehitu(foto);
            AbstractMap.SimpleEntry<Argazkia, Bilduma> entry = new AbstractMap.SimpleEntry<>(foto, newAlbum);
            return entry;
        }

    }


    private void aDBaseanSartu(Argazkia photo, Bilduma album) {
        // metodo hau argazkiak eta bildumak datu basean sortzeko balio du
        // konexio gabe eta konezioarekin lan egingo du, baina era ezberdinetan
        ArgazkiDBKud.getInstantzia().argazkiaSartu(photo);
        //Argazkia zein bilduman dagoen adierazi
        String bilIzena = bildumak.getValue().toString();
        String photoId = photo.getId().toString();
        List<String> bildumak = ListaBildumak.getNireBilduma().lortuBildumenIzenak();
        if (bildumak.contains(bilIzena)) {
            // Bilduma hori jada sortuta dago, beraz zuzenean editatu BildumaArgazkia
            BildumaDBKud.getInstantzia().argazkiaBildumanSartu(bilIzena, photoId);
        } else {
            // Bilduma berria sortu eta argazkia sartu
            BildumaDBKud.getInstantzia().bildumaSartu(bilIzena, album.getId(), mainApp.username, "");
            BildumaDBKud.getInstantzia().argazkiaBildumanSartu(bilIzena, photoId);
        }
    }

    public void argazkiaKendu(int index){
        obsDatuak.remove(index);
        igoModel = FXCollections.observableArrayList(obsDatuak);
        igotakoakTabla.setItems(igoModel);
    }

    public void keyPressed(KeyEvent e) {
        if(e.getCode()==KeyCode.DELETE){
            System.out.println("delete");
            argazkiaKendu(igotakoakTabla.getSelectionModel().getFocusedIndex());
        }
    }

    public void bildumakComboboxKargatu() {
        List<String> bil = ListaBildumak.getNireBilduma().lortuBildumenIzenak();
        if(this.bildumak.getItems().isEmpty())
            this.bildumak.getItems().addAll(bil);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        izena.setCellValueFactory(new PropertyValueFactory<ObsArgazkiIgo, String>("izena"));

        botoia.setCellValueFactory(new PropertyValueFactory<ObsArgazkiIgo, Button>("botoia"));

        EventHandler<ActionEvent> buttonHandler = event -> {
            igoModel.remove(igotakoakTabla.getSelectionModel().getFocusedIndex());
        };

        botoia.setCellFactory(p -> new TableCell<>() {
            public void updateItem(Button botoi, boolean empty) {
                if (botoi != null && !empty) {
                    final Button button = new Button();
                    button.setText("Delete");
                    button.setOnAction(buttonHandler);
                    setGraphic(button);
                    setAlignment(Pos.CENTER);
                    // tbData.refresh();
                } else {
                    setGraphic(null);
                    setText(null);
                }
            }

            ;
        });

        botoia.addEventHandler(ActionEvent.ACTION, buttonHandler);

        System.out.println("igo iniciado");
        //igotakoakTabla.getColumns();

    }

    public void atzeraBotoiaKlik(ActionEvent actionEvent) {
        this.obsDatuak.clear();
        this.igoModel.clear();
        this.mainApp.pantailaNagusiaErakutsi();
    }
}