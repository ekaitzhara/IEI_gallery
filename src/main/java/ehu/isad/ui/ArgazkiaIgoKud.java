package ehu.isad.ui;

import ehu.isad.Main;
import ehu.isad.flickr.FlickrAPI;
import ehu.isad.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ArgazkiaIgoKud implements Initializable {

    // Reference to the main application.
    private Main mainApp;
    private List<File> igotakoFitxategiak = new ArrayList<>();

    @FXML
    private BorderPane bordes;

    @FXML
    private ComboBox comboBox = new ComboBox();

    @FXML
    private Text textoEstado; //dice si los artxibos se han subido a flickr o que solo se han subido a la app
    private Text uploadedFiles; //TODO texto de archivos que se van a subir a flickr

    public void setMainApp(Main main) {
        this.mainApp = main;
    }

    @FXML
    private void cleanList(ActionEvent actionEvent) throws Exception {
        System.out.println("cleanList");
        this.igotakoFitxategiak.clear();
    }

    @FXML
    private void uploadFiles(ActionEvent actionEvent) throws Exception {
        System.out.println("uploadFiles");
        FlickrAPI api = FlickrAPI.getInstantzia();
        try {
            if (api.hasConection()) { //aplikazioa apiarekin konekzioa badu ikusiko dugu
                uploadPhotosToApi();
            } else {
                uploadPhotosWithoutApi();
            }
        }catch (Exception e){
            //errorerenbat gertatu da, segurazki konexioa joan dela apia irekita zegoelarik
            // errore bat egon dela abisatu erabiltzaileari
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
        System.out.println(getClass().getResource("temp"));
        //String tempPath = "C:\\Users\\anderdu\\IdeaProjects\\DASIproject\\src\\main\\resources\\temp";
        String tempPath = "src/main/resources/temp";
        List<File> files = event.getDragboard().getFiles();
        this.igotakoFitxategiak.addAll(files); //fitxategiak igotako fitxategien registrora igoko da


        //para hacer ventana dinamica
        //texto.setText("aaaa");
        //ui1();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        List<String> bil = ListaBildumak.getNireBilduma().lortuBildumenIzenak();

        List<String> str = new ArrayList<>();
        str.add("bil1");
        str.add("bil2");

        this.comboBox.getItems().addAll(str);
        System.out.println("str añadidos");

    }


    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    private void uploadPhotosWithoutApi(){
        // 1. argazkiak datu basera, id izan gabe
        // argazkien kopia sortu temp fitxategian
        // argazkiak db-ra igo baina id gabe
        // argazkiak datu egituran sartu
    }

    private void uploadPhotosToApi(){
        //this.igotakoFitxategiak
        argazkiakApiraIgo();
        aragazkiakApitikDeskargatu();
    }

    private void argazkiakApiraIgo(){
        //Argazkiak flickr-era igoko dira
    }

    private void aragazkiakApitikDeskargatu(){
        //argazkiak flickr-en daudela dakigu
        //Argazkiak DB-an sartu
    }



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

}