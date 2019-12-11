package ehu.isad.flickrKud;

import java.io.*;
import java.net.URL;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Scanner;

public class Laguntzaile {


    public static void copyFileUsingStream(String pSource, String pDest) throws IOException {
        File source= new File(pSource);
        File dest= new File(pDest);

        //
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

    public static void downloadFileWithUrl(String url, String dest){
        // fitxategi bat deskargatzeko url bat emanez
        try (
                BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream());
                FileOutputStream fileOS = new FileOutputStream(dest)) {
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        } catch (IOException e) {
            // handles IO exceptions
        }
    }

    public static void clearFile(String filePath) throws FileNotFoundException {
        // fitxategia hutsik
        // txt baten barruko informazioa garbizteko
        Formatter f = new Formatter(filePath);
        Scanner s = new Scanner(filePath);
        //go through and do this every time in order to delete previous crap
        while(s.hasNext()){
            f.format(" ");
        }
    }

    public static void deleteAllFilesFromDir(String dir){
        File fDir= new File(dir);
        File[] ezabatzeko = fDir.listFiles();
        for (File f : ezabatzeko)
            f.delete();
    }

    public static String getFileName(String path){
        File p = new File(path);
        return p.getName().split("\\.")[0];
    }

    public static HashMap getHashTableFromTxt(String path) throws FileNotFoundException {
        HashMap mapa = new HashMap<String,String>();
        File file = new File(path);
        Scanner s = new Scanner(file);
        while(s.hasNextLine()) {
            String line = s.nextLine();
            String argazkiIzena = line.split(",")[0];
            String idArgazkiDB = line.split(",")[1];
            mapa.put(argazkiIzena,idArgazkiDB);
        }
        s.close();
        return mapa;
    }

    public static void addDataToFile(String filePath){

    }






}
