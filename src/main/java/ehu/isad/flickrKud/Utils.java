package ehu.isad.flickrKud;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Scanner;

public class Utils {

    private static boolean setupPropertiesHutsik = false;
    public static String home = System.getProperty("user.home")+File.separatorChar+".flickr";
    public static String tmpPath = Utils.globalPath("/data/dasiteam/flickr/tmp");//GLOBAL PATH TIENE HOME
    public static String argazkiakPath = Utils.globalPath("/data/dasiteam/flickr/argazkiak");

    public static void setupPropHutsa() {
        setupPropertiesHutsik = true;
    }
    public static boolean emanSetupPropStatus() {
        return setupPropertiesHutsik;
    }

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
        PrintWriter writer = new PrintWriter(filePath);
        writer.print("");
        writer.close();
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

    public static void appendStrToFile(String fileName, String str) {
        try {
            // Open given file in append mode.
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(fileName, true));
            out.write("\n"+str);
            out.close();
        }
        catch (IOException e){}
    }

    public static String getDateFromMetadata(String filePath){
        String date = null;
        return date;
    }

    public static String globalPath(String path){
        String newPath=path.replace('/',File.separatorChar);
        return home+newPath;
    }

    public static void copyFolder(String sourcePath, String destinationPath) throws IOException {
        File sourceFolder = new File(sourcePath);
        File destinationFolder = new File(destinationPath);
        //Check if sourceFolder is a directory or file
        //If sourceFolder is file; then copy the file directly to new location
        if (sourceFolder.isDirectory())
        {
            //Verify if destinationFolder is already present; If not then create it
            if (!destinationFolder.exists()) {
                destinationFolder.mkdir();
                System.out.println("Directory created :: " + destinationFolder);
            }

            //Get all files from source directory
            String files[] = sourceFolder.list();

            //Iterate over all files and copy them to destinationFolder one by one
            for (String file : files)
            {
                File srcFile = new File(sourceFolder, file);
                File destFile = new File(destinationFolder, file);

                //Recursive function call
                copyFolder(srcFile.getPath(), destFile.getPath());
            }
        }
        else
        {
            //Copy the file content from one place to another
            Files.copy(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied :: " + destinationFolder);
        }
    }

}
