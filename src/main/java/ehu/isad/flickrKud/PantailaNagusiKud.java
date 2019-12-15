package ehu.isad.flickrKud;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.FlickrRuntimeException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.comments.CommentsInterface;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import ehu.isad.Main;
import ehu.isad.db.*;
import ehu.isad.flickr.FlickrAPI;
import ehu.isad.model.Bilduma;
import ehu.isad.model.Etiketa;
import ehu.isad.model.ListaBildumak;
import ehu.isad.model.TaulaDatu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

public class PantailaNagusiKud implements Initializable {

  // Reference to the main application.
  private Main mainApp;

  private static String erabiltzaileID = ErabiltzaileDBKud.getIdErab();

    //FXML-ko elementuak
    @FXML
    private FlowPane flowPane;

    @FXML
    private TableView<TaulaDatu> tbData;

    @FXML
    private TableColumn<TaulaDatu, Image> argazkia;

    @FXML
    private TableColumn<TaulaDatu, String> izena;

    @FXML
    private TableColumn<TaulaDatu, String> etiketak;

    @FXML
    private TableColumn<TaulaDatu, Date> data;

    @FXML
    private TableColumn<TaulaDatu, String> deskribapena;

    @FXML
    private TableColumn<TaulaDatu, Integer> favs;

    @FXML
    private TableColumn<TaulaDatu, String> comments;

    @FXML
    private TableColumn<TaulaDatu, Button> checkBox;

    @FXML
    private ListView bildumenLista = new ListView();

    // add your data here from any source
    private ObservableList<TaulaDatu> taulaModels = FXCollections.observableArrayList();
    private ObservableList bildumaModel = FXCollections.observableArrayList();

    private ArrayList<TaulaDatu> editatutakoak = new ArrayList<>();


  @FXML
  private javafx.scene.control.Label erabiltzaileIzena = new javafx.scene.control.Label();

  public void setMainApp(Main main) {
    this.mainApp = main;
  }

  @FXML
  public void onClick(ActionEvent actionEvent) {

  }

  @FXML
  public void onClickLogOut(ActionEvent actionEvent) {

      FlickrAPI.getInstantzia().ezabatuFlickrInstantzia();
      this.mainApp.logoutAktibatu();
      File authsDir = new File(System.getProperty("user.home") + File.separatorChar + ".flickrAuth");

      if (authsDir.isDirectory()) {
          if (authsDir.list().length == 0) {
              authsDir.delete();
              System.out.println("El directorio " + authsDir + " ha sido borrado correctamente");
          } else {
              for (String temp : authsDir.list()) {
                  File fileDelete = new File(authsDir, temp);
                  fileDelete.delete();
              }
              if (authsDir.list().length == 0) {
                  authsDir.delete();
                  System.out.println("El directorio " + authsDir + " ha sido borrado correctamente");
              }
          }
      }

      ListaBildumak.getNireBilduma().listaHustu();
      BildumaDBKud.getInstantzia().bildumaGuztiakEzabatu();
      BildumaDBKud.getInstantzia().bildumaArgazkiLoturakEzabatu();
      ArgazkiDBKud.getInstantzia().argazkiGuztiakEzabatu();
      ArgazkiDBKud.getInstantzia().argazkiEtiketaLoturakEzabatu();
      EtiketaDBKud.getInstantzia().etiketaGuztiakEzabatu();
      ErabiltzaileDBKud.getInstantzia().ezabatuErabiltzailea();

      this.mainApp.kautoketaraEraman();
  }

  // Bilduma berri bat sortzeko
  public void bildumaBerriaSortu() {
      // Datu basean bilduma berri bat sortu bere izenarekin eta nahi dituen argazkiekin
      System.out.println("bilduma sortu");
      this.mainApp.bildumaSortuErakutsi();
  }

    // Argazki berria sartzeko botoia ematerakoan
    public void argazkiBerriaSortu() {
        this.mainApp.argazkiaIgoErakutsi();
    }


  public void jarriErabiltzaileIzena() {
      String izena = ErabiltzaileDBKud.getInstantzia().emanIzena();
      this.erabiltzaileIzena.setText(izena); }

  @FXML
  public void syncEgin(ActionEvent actionEvent) throws FileNotFoundException {
      System.out.println("sync empezado");
      // 1. zatia
      // /tmp karpetan dagoen igo Flickr-rera eta Flickr-retik hartu sortu duen id-a sartzeko datu basean (idFlickr)
      // small size jaisten du eta resources-en guztiekin batera jartzen ditu argazki horiek
      // amaitzerakoan tmp-en dagoen ezabatzen du
      System.out.println("1. zatia");
      try {
          tmpArgazkiakIgo();
      } catch (FileNotFoundException e) {
          e.printStackTrace();
      }
      // 2. zatia
      // photosToDelete.txt fitxategian gure datubasean ezabatu ditugun, baina Flikcer-rera aldaketa igo ezin izan ditugun argazkiak daude
      // Beraz, txt horretan dauden argazkian ez daude  ez gure datu basean, ez gure datu egituran (ListaBildumak)
      // txt horretan dauden argazki guztiak Flikcr-retik ezabatu behar dira
      // txt-an dagoena ezabatu (Flickr-ren ondo ezabatuta daudenean argazki guztiak)
      System.out.println("2. zatia");
      try {
          ezabatuakKenduFlickerren();
      } catch (FileNotFoundException e) {
          e.printStackTrace();
      }
      // 3. zatia
      // Datubaseko id guztiak sartzen ditugu ArrayList batean
      // Gero, Flickr-ren argazkiak hartu eta banan banan konparatu id-ak -->
      //    --> Flickr-eko argazkia datubasean badago, ArrayList-etik ezabatzen du
      // Azkenean, Flickr-ren ez dauden argazkiak geratuko dira soilik ArrayList-ean
      // Argazki horiek (sobratzen direnak) datubasetik eta datu egituratik (ListaBildumak) ezabatzen ditugu
      System.out.println("3. zatia");
      try {
          ezabatuakKenduPrograman();
      } catch (Exception e) {
          e.printStackTrace();
      }

  }

  private void tmpArgazkiakIgo() throws FileNotFoundException {
        // tmp-n gordetako argazkiak flickerrera igoko dira
        File tmp = new File(Utils.tmpPath);
        HashMap<String,String> mapUpload = ArgazkiDBKud.getInstantzia().emanPhotosToUpload();

        String albumName = null;
        String idArgazkiDB = null;
        ArrayList<String> argazkiarenBildumak;
        int n = 0;

        if (tmp.isDirectory()) {
            String[] argazkiak = tmp.list(); //del archivo photos ToUpload ha conseguido una lista de fotos
            //TODO probar si funciona
            for (String a : argazkiak) {
                System.out.println("fotos vistas   " + n);n++;
                String titulua = Utils.getFileName(a); //fitxategiaren izena lortu
                idArgazkiDB = mapUpload.get(a); //fitxategiaren datu baseko id-a lortu
                argazkiarenBildumak = BildumaDBKud.getInstantzia().argazkiarenBildumak(idArgazkiDB);
                for(String bilIzena:argazkiarenBildumak){
                    ArrayList<String> argEtaBil= FlickrAPI.getInstantzia().argazkiaIgo(Utils.tmpPath+File.separatorChar+a,bilIzena); //flick-era argazkia igo
                    String sortuDenFlickrID = argEtaBil.get(0);
                    ArgazkiDBKud.getInstantzia().idFlickrSartu(sortuDenFlickrID, idArgazkiDB);
                    PhotosInterface photoInt = FlickrAPI.getInstantzia().getFlickr().getPhotosInterface();
                    try {// Argazkia flick-er era igoko da eta small-size-a deskargatuko da
                        Photo p = photoInt.getPhoto(sortuDenFlickrID);
                        Utils.downloadFileWithUrl(a, p.getSmallUrl());
                    } catch (FlickrException e) { e.printStackTrace(); }
                }

            }
                Utils.deleteAllFilesFromDir(Utils.tmpPath);
            ArgazkiDBKud.getInstantzia().clearPhotosToUpload();
            } else
                System.out.println("Errorea tmp karpeta irakurtzean");
    }

    private void ezabatuakKenduFlickerren() throws FileNotFoundException {
        try {
            PhotosInterface photoInt = FlickrAPI.getInstantzia().getFlickr().getPhotosInterface();
            ArrayList<String> deletedIDs = ArgazkiDBKud.getInstantzia().emanPhotosToDelete();
            for(String id:deletedIDs){
                photoInt.delete(id);
            }
            ArgazkiDBKud.getInstantzia().clearPhotosToDelete();
        }catch (FlickrException e) { e.printStackTrace(); }
    }

    private void ezabatuakKenduPrograman(){
        ArrayList<String> flickrIdDB = ArgazkiDBKud.getInstantzia().emanIdFlickrGuztiak();
        PhotosetsInterface bildumaInt = FlickrAPI.getInstantzia().getFlickr().getPhotosetsInterface();
        try {
            Iterator bildumak = bildumaInt.getList(FlickrAPI.getInstantzia().getNsid()).getPhotosets().iterator();
            while (bildumak.hasNext()) { // bildumak dauden bitartean, zeharkatu
                Photoset set = (Photoset) bildumak.next(); // bilduma lortu
                // set es la bilduma
                PhotoList photos = bildumaInt.getPhotos(set.getId(), 500, 1);  // bildumaren lehenengo 500 argazki lortu
                Iterator it = photos.iterator();
                while (it.hasNext()) {
                    Photo argazki = (Photo) it.next();
                    if (flickrIdDB.contains(argazki.getId()))
                        flickrIdDB.remove(argazki.getId());
                }
            }
        } catch (FlickrException e) { e.printStackTrace(); }

        // Puntu honetan DBan bai eta Flickerren ez dauden argazkiak daude
        for (String ezabatzekoID: flickrIdDB) {
            // DBtik ezabatu
            ArgazkiDBKud.getInstantzia().argazkiaEzabatuIdFlickrrekin(ezabatzekoID);
            // ListaBildumatik ezabatu
            ListaBildumak.getNireBilduma().argazkiaEzabatu(ezabatzekoID);
        }
    }

    @FXML
  public void programaItxi(ActionEvent actionEvent){
      this.mainApp.itxi();
      System.out.println("itxi click");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

      taularenEzaugarriakJarri();


  }

    private void taularenEzaugarriakJarri() {
        tbData.setEditable(true);

        izena.setCellValueFactory(new PropertyValueFactory<>("izena"));
        etiketak.setCellValueFactory(new PropertyValueFactory<>("etiketak"));
        data.setCellValueFactory(new PropertyValueFactory<>("data"));
        deskribapena.setCellValueFactory(new PropertyValueFactory<>("deskribapena"));
        favs.setCellValueFactory(new PropertyValueFactory<>("favs"));
        comments.setCellValueFactory(new PropertyValueFactory<>("comments"));

        checkBox.setCellValueFactory(new PropertyValueFactory<>("checkBox"));

        // IZENA
        Callback<TableColumn<TaulaDatu, String>, TableCell<TaulaDatu, String>> defaultTextFieldCellFactoryIzena
                = TextFieldTableCell.<TaulaDatu>forTableColumn();

        izena.setCellFactory(col -> {
            TableCell<TaulaDatu, String> cell = defaultTextFieldCellFactoryIzena.call(col);
            cell.itemProperty().addListener((obs, oldValue, newValue) -> {
                TableRow row = cell.getTableRow();
                if (row == null) {
                    cell.setEditable(false);
                } else {
                    TaulaDatu item = (TaulaDatu) cell.getTableRow().getItem();
                    if (item == null) {
                        cell.setEditable(false);
                    } else {
                        cell.setEditable(true);
                    }
                }
            });
            return cell ;
        });

        izena.setOnEditCommit(
                t -> {
                    if (!this.editatutakoak.contains(t.getTableView().getItems().get(t.getTablePosition().getRow())))
                        this.editatutakoak.add(t.getTableView().getItems().get(t.getTablePosition().getRow()));
                    t.getTableView().getItems().get(t.getTablePosition().getRow())
                            .setIzena(t.getNewValue());
                }
        );

        // ETIKETAK
        Callback<TableColumn<TaulaDatu, String>, TableCell<TaulaDatu, String>> defaultTextFieldCellFactoryEtiketa
                = TextFieldTableCell.<TaulaDatu>forTableColumn();

        etiketak.setCellFactory(col -> {
            TableCell<TaulaDatu, String> cell = defaultTextFieldCellFactoryEtiketa.call(col);
            cell.itemProperty().addListener((obs, oldValue, newValue) -> {
                TableRow row = cell.getTableRow();
                if (row == null) {
                    cell.setEditable(false);
                } else {
                    TaulaDatu item = (TaulaDatu) cell.getTableRow().getItem();
                    if (item == null) {
                        cell.setEditable(false);
                    } else {
                        cell.setEditable(true);
                    }
                }
            });
            return cell ;
        });

        etiketak.setOnEditCommit(
                t -> {
                    if (!this.editatutakoak.contains(t.getTableView().getItems().get(t.getTablePosition().getRow())))
                        this.editatutakoak.add(t.getTableView().getItems().get(t.getTablePosition().getRow()));
                    t.getTableView().getItems().get(t.getTablePosition().getRow())
                            .setEtiketak(t.getNewValue());
                }
        );


        // DATA
        Callback<TableColumn<TaulaDatu, Date>, TableCell<TaulaDatu, Date>> defaultTextFieldCellFactoryDate
                = TextFieldTableCell.<TaulaDatu, Date>forTableColumn(new StringConverter<Date>() {
            @Override
            public String toString(Date object) {
                if (object == null)
                    return "Hori ez da data bat";
                return object.toString();
            }

            @Override
            public Date fromString(String string) {
                if (!string.contains("-"))
                    return null;
                String[] aux = string.split("-");
                int cont=1;
                int urteaDatan = 0;
                int hilabeteDatan = 0;
                for (String s : aux) {
                    try {
                        Integer i = Integer.parseInt(s);
                        if (cont == 1) {
                            urteaDatan = i;
                            if ((s.length() != 4) || (i > Calendar.getInstance().get(Calendar.YEAR)))
                                return null;
                        }
                        if (cont == 2) {
                            hilabeteDatan = i;
                            if ((i>12) || ((urteaDatan == Calendar.getInstance().get(Calendar.YEAR)) && (i > Calendar.getInstance().get(Calendar.MONTH)+1)))
                                return null;
                        }
                        if (cont == 3) {
                            if ((i > 31) || ((urteaDatan == Calendar.getInstance().get(Calendar.YEAR)) && (hilabeteDatan == Calendar.getInstance().get(Calendar.MONTH)+1) && (i > Calendar.getInstance().get(Calendar.DAY_OF_MONTH))))
                                return null;
                        }
                        cont += 1;

                    } catch (NumberFormatException excepcion) {
                        return null;
                    }
                }
                Date pDate = Date.valueOf(string);
                if (pDate != null)
                    return pDate;
                return null;
            }
        });

        data.setCellFactory(col -> {
            TableCell<TaulaDatu, Date> cell = defaultTextFieldCellFactoryDate.call(col);
            cell.itemProperty().addListener((obs, oldValue, newValue) -> {
                TableRow row = cell.getTableRow();
                if (row == null) {
                    cell.setEditable(false);
                } else {
                    TaulaDatu item = (TaulaDatu) cell.getTableRow().getItem();
                    if (item == null) {
                        cell.setEditable(false);
                    } else {
                        cell.setEditable(true);
                    }
                }
            });
            return cell ;
        });


        data.setOnEditCommit(
                t -> {
                    if (!this.editatutakoak.contains(t.getTableView().getItems().get(t.getTablePosition().getRow())))
                        this.editatutakoak.add(t.getTableView().getItems().get(t.getTablePosition().getRow()));
                    t.getTableView().getItems().get(t.getTablePosition().getRow())
                            .setData(t.getNewValue());
                }
        );

        // KOMENTATU
        Callback<TableColumn<TaulaDatu, String>, TableCell<TaulaDatu, String>> defaultTextFieldCellFactoryComment
                = TextFieldTableCell.<TaulaDatu>forTableColumn();

        comments.setCellFactory(col -> {
            TableCell<TaulaDatu, String> cell = defaultTextFieldCellFactoryComment.call(col);
            cell.itemProperty().addListener((obs, oldValue, newValue) -> {
                TableRow row = cell.getTableRow();
                if (row == null) {
                    cell.setEditable(false);
                } else {
                    TaulaDatu item = (TaulaDatu) cell.getTableRow().getItem();
                    if (item == null) {
                        cell.setEditable(false);
                    } else {
                        cell.setEditable(true);
                    }
                }
            });
            return cell ;
        });

        comments.setOnEditCommit(
                t -> {
                    if (!this.editatutakoak.contains(t.getTableView().getItems().get(t.getTablePosition().getRow())))
                        this.editatutakoak.add(t.getTableView().getItems().get(t.getTablePosition().getRow()));
                    t.getTableView().getItems().get(t.getTablePosition().getRow())
                            .setComments(t.getNewValue());
                }
        );

        // DESKRIBAPENA
        Callback<TableColumn<TaulaDatu, String>, TableCell<TaulaDatu, String>> defaultTextFieldCellFactoryDesk
                = TextFieldTableCell.<TaulaDatu>forTableColumn();

        deskribapena.setCellFactory(col -> {
            TableCell<TaulaDatu, String> cell = defaultTextFieldCellFactoryDesk.call(col);
            cell.itemProperty().addListener((obs, oldValue, newValue) -> {
                TableRow row = cell.getTableRow();
                if (row == null) {
                    cell.setEditable(false);
                } else {
                    TaulaDatu item = (TaulaDatu) cell.getTableRow().getItem();
                    if (item == null) {
                        cell.setEditable(false);
                    } else {
                        cell.setEditable(true);
                    }
                }
            });
            return cell ;
        });

        deskribapena.setOnEditCommit(
                t -> {
                    if (!this.editatutakoak.contains(t.getTableView().getItems().get(t.getTablePosition().getRow())))
                        this.editatutakoak.add(t.getTableView().getItems().get(t.getTablePosition().getRow()));
                    t.getTableView().getItems().get(t.getTablePosition().getRow())
                            .setDeskribapena(t.getNewValue());
                }
        );


        // ARGAZKIA
        argazkia.setCellValueFactory(new PropertyValueFactory<TaulaDatu, Image>("argazkia"));
        argazkia.setCellFactory(p -> new TableCell<>() {
            public void updateItem(Image image, boolean empty) {
                if (image != null && !empty){
                    final ImageView imageview = new ImageView();
                    imageview.setFitHeight(100);
                    imageview.setFitWidth(100);
                    imageview.setImage(image);
                    setGraphic(imageview);
                    setAlignment(Pos.CENTER);
                    // tbData.refresh();
                }else{
                    setGraphic(null);
                    setText(null);
                }
            };
        });
    }


    public void hartuEtaGordeDatuakFlickr() throws FlickrException {

        ArrayList<Bilduma> bildumak = ListaBildumak.getNireBilduma().getLista();
        if(ListaBildumak.getNireBilduma().listaHutsikDago())
            ListaBildumak.getNireBilduma().listaBete(); // paras descargar las bildumas
        //bildumak = ListaBildumak.getNireBilduma().getLista();
        System.out.println("ListaBildumak bete egin da");
        // Hartu datu guztiak singleton-tik eta sartu taulan

    }

    public void sartuBildumakListan() {

      List<String> bildumenIzenak = ListaBildumak.getNireBilduma().lortuBildumenIzenak();
      bildumaModel = FXCollections.observableArrayList(bildumenIzenak);
      this.bildumenLista.setItems(bildumaModel);
      this.bildumenLista.getSelectionModel().selectFirst();
    }

    @FXML
    public void bildumanKlikatu(MouseEvent mouseEvent) {

        boolean zerbaitKlikaturik = false;
        Iterator itr = tbData.getItems().iterator();
        while (itr.hasNext()) {
            TaulaDatu t = (TaulaDatu) itr.next();
            if (t.getCheckBox().isSelected())
                zerbaitKlikaturik = true;
        }
        if (zerbaitKlikaturik == true) {
            this.mainApp.zerbaitKlikaturikPantaila();
        } else if (!this.editatutakoak.isEmpty()) {
            this.mainApp.zerbaitEditaturikPantaila();
        } else {
            sartuDatuakTaulan();
            this.taularenEzaugarriakJarri();
        }
    }

    public void sartuDatuakTaulan() {

      // Hasierako aldirako, jarri lehenengo bilduma aukeratu bezala
      //datu baseko datuak bildumetan sartu
      ListaBildumak datuEgitura = ListaBildumak.getNireBilduma();
      if(!datuEgitura.utsik()){
          String bilduma = (String) bildumenLista.getSelectionModel().getSelectedItem();
          this.taulaModels = FXCollections.observableArrayList(
                  ListaBildumak.getNireBilduma().emanTaularakoDatuak(bilduma)
          );
          this.tbData.setItems(taulaModels);
      }
    }


    public void taulaRefreshEgin() {
      tbData.refresh();
    }

    @FXML
    public void gordeBotoia(ActionEvent actionEvent) {
        // Editatu dituenak gorde
        //this.editatutakoak erabili
        for (TaulaDatu t : this.editatutakoak) {
            long l = t.getData().getTime();
            java.util.Date igotzekoData = new java.util.Date(l);
            String[] tags = t.getEtiketak().split(", ");
            PhotosInterface argazkiInterface = FlickrAPI.getInstantzia().getFlickr().getPhotosInterface();
            CommentsInterface komentarioInterface = FlickrAPI.getInstantzia().getFlickr().getCommentsInterface();
            String idFlickr = ArgazkiDBKud.getInstantzia().emanIdFlickr(t.getArgazkiId());
            try {
                argazkiInterface.setDates(idFlickr, null, igotzekoData, null);
                argazkiInterface.setMeta(idFlickr, t.getIzena(), t.getDeskribapena());
                argazkiInterface.addTags(idFlickr, tags);


                komentarioInterface.addComment(idFlickr, t.getComments());

                ArgazkiDBKud.getInstantzia().editatuDatuak(t.getArgazkiId(), t.getData(), t.getIzena(), t.getDeskribapena());
                ListaBildumak.getNireBilduma().argazkiaEditatu(idFlickr, t.getData(), t.getIzena(), t.getDeskribapena(), tags);

                for (String s : tags) {
                    String etikId = EtiketaDBKud.getInstantzia().etiketaEmanIzenarekin(s);
                    if (etikId == null) {
                        EtiketaDBKud.getInstantzia().etiketaSartu(Utils.etiketenIncGlobala--, s);
                        etikId = EtiketaDBKud.getInstantzia().etiketaEmanIzenarekin(s);
                        EtiketaDBKud.getInstantzia().etiketaArgazkianSartu(Integer.parseInt(etikId), t.getArgazkiId());
                    } else {
                        EtiketaDBKud.getInstantzia().editatuArgazkiarenEtiketak(t.getArgazkiId(), etikId);
                    }
                }

            } catch (FlickrException | FlickrRuntimeException e) {
                e.printStackTrace();
                this.mainApp.argazkiaEditatuError();
            }
        }
        this.editatutakoak.clear();
        this.sartuDatuakTaulan();
    }

    @FXML
    public void deuseztatuAldaketak(ActionEvent actionEvent) {
        this.editatutakoak.clear();
        this.sartuDatuakTaulan();
    }

    @FXML
    public void ezabatuArgazkiak(ActionEvent actionEvent) {
        Iterator it = tbData.getItems().iterator();
        while (it.hasNext()) {
            TaulaDatu t = (TaulaDatu) it.next();
            if (t.getCheckBox().isSelected()) {
                PhotosInterface pi = FlickrAPI.getInstantzia().getFlickr().getPhotosInterface();
                String idFlickr = ArgazkiDBKud.getInstantzia().emanIdFlickr(t.getArgazkiId());

                try {
                    pi.delete(idFlickr);
                } catch (FlickrException | FlickrRuntimeException e) {
                    ArgazkiDBKud.getInstantzia().addPhotoToDelete(idFlickr);
                    this.mainApp.syncEginMezua();
                }
                ArgazkiDBKud.getInstantzia().argazkiaEzabatuIdFlickrrekin(idFlickr);
                BildumaDBKud.getInstantzia().kenduArgazkiaBildumatik(t.getArgazkiId());
                if (t.getEtiketak() != null)
                    EtiketaDBKud.getInstantzia().kenduArgazkiaEtiketatik(t.getArgazkiId());

                ListaBildumak.getNireBilduma().argazkiaEzabatu(idFlickr);

                File argazkiJPG = new File(Utils.argazkiakPath + File.separatorChar + idFlickr + ".jpg");
                argazkiJPG.delete();
            }
        }
        this.sartuDatuakTaulan();
    }

    @FXML
    public void ezabatuBilduma(ActionEvent actionEvent) {

        String bilduma = (String) bildumenLista.getSelectionModel().getSelectedItem();
        PhotosetsInterface bInterface = FlickrAPI.getInstantzia().getFlickr().getPhotosetsInterface();
        String idBilduma = BildumaDBKud.getInstantzia().emanBildumaIzenarekin(bilduma);
        if (idBilduma != "0000") {
            try {
                bInterface.delete(idBilduma);
                BildumaDBKud.getInstantzia().ezabatuBilduma(bilduma);
                ArgazkiDBKud.getInstantzia().kenduBildumaArgazkiatik(bilduma);
                ListaBildumak.getNireBilduma().bildumaEzabatu(bilduma);
            } catch (FlickrException | FlickrRuntimeException e) {
                this.mainApp.bildumaEzabatuError();
            }
        }
        this.sartuBildumakListan();
        this.sartuDatuakTaulan();

    }

    public void ateraBotoiariEman(ActionEvent actionEvent) {
        this.mainApp.kautoketaraEraman();
    }
}