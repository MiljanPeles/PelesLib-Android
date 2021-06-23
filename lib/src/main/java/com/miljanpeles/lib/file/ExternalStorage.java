package com.miljanpeles.lib.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExternalStorage {

    private Context context;

    public ExternalStorage(Context context) {
        this.context = context;
    }

    /**
     * Upisivanje teksta u fajl u public direktorijumu androida
     * @param directory - Direktorijum u koji upisujemo
     * @param fileName - Ime fajla u koji upisujemo (sa ili bez ekstenzije)
     * @param text - Tekst koji upisujemo
     * @param overwrite - true - tekst se dodaje u fajlu, false - fajl se brise totalno i upisuje se novi tekst
     * @return true ako je uspesno upisan fajl, false ako nije
     */
    public boolean writeTextInPublicDirectory(ExternalDirectory directory, String fileName, String text, boolean overwrite) {
        boolean success = false;
        File dir, file;
        if(isExternalStorageWritable()) {

            dir = Environment.getExternalStoragePublicDirectory(directory.getDirectory());

            if(!dir.exists()) dir.mkdirs();

            file = new File(dir, fileName);

            try (FileOutputStream fos = new FileOutputStream(file, overwrite)) {
                //file.createNewFile();
                // ako ima true onda ce sadrzaj da se dodaje postojacem sadrzaju
                fos.write(text.getBytes());
                success = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    /**
     * citanje teksta iz public direktorijuma androida
     * @param directory - directory iz kojeg citamo
     * @param fileName - file iz kojeg citamo (sa ili bez ekstenzije)
     * @return tekst koji je procitan ili null ako nesto nije u redu
     */
    public String readTextFromPublicDirectory(ExternalDirectory directory, String fileName) {

        File dir, file;
        StringBuffer sbuffer = null;
        if(isExternalStorageWritable()) {

            dir = Environment.getExternalStoragePublicDirectory(directory.getDirectory());

            if(!dir.exists()) dir.mkdirs();

            file = new File(dir, fileName);

            try (FileInputStream fstream = new FileInputStream(file)) {
                sbuffer = new StringBuffer();
                int i;
                while ((i = fstream.read()) != -1){
                    sbuffer.append((char)i);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sbuffer == null ? null : sbuffer.toString();
    }

    /**
     *
     * Upisivanje teksta u custom folder androida
     * @param directory - Direktorijum u koji upisujemo
     * @param fileName - Ime fajla u koji upisujemo
     * @param text - Tekst koji upisujemo (sa ili bez ekstenzije)
     * @param overwrite - true - tekst se dodaje u fajlu, false - fajl se brise totalno i upisuje se novi tekst
     * @return true ako je uspesno upisan fajl, false ako nije
     */
    public boolean writeTextInCustomDirectory(String directory, String fileName, String text, boolean overwrite) {
        boolean success = false;
        File root, dir, file;
        if(isExternalStorageWritable()) {

            //root = context.getExternalFilesDir(null);
            root = Environment.getExternalStorageDirectory();
            dir = new File(root.getAbsolutePath() + "/" + directory);

            if(!dir.exists()) dir.mkdirs();

            file = new File(dir, fileName);

            try (FileOutputStream fos = new FileOutputStream(file, overwrite)) {
                //file.createNewFile();
                // ako ima true onda ce sadrzaj da se dodaje postojacem sadrzaju
                fos.write(text.getBytes());
                success = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    /**
     * citanje teksta iz custom foldera androida
     * @param directory - directory iz kojeg citamo
     * @param fileName - file iz kojeg citamo (sa ili bez ekstenzije)
     * @return tekst koji je procitan ili null ako nesto nije u redu
     */
    public String readTextFromCustomDirectory(String directory, String fileName) {

        File dir, file;
        StringBuffer sbuffer = null;
        if(isExternalStorageWritable()) {

            dir = new File(Environment.getExternalStorageDirectory().toString() + "/" + directory);

            if(!dir.exists()) dir.mkdirs();

            file = new File(dir, fileName);

            try (FileInputStream fstream = new FileInputStream(file)) {
                sbuffer = new StringBuffer();
                int i;
                while ((i = fstream.read()) != -1){
                    sbuffer.append((char)i);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sbuffer == null ? null : sbuffer.toString();
    }

    /**
     * Upisivanje slike u eksterno skladiste
     * @param directory - Ime foldera u koji ce se slika upisati
     * @param namePrefix - prefiks imena (primer: Slika, puno ime Slika-2139283921938.jpg)
     * @param bitmap - bitmap koji upisujemo
     * @return - true ako je slika upisana, false ako nije
     */
    public boolean saveImage(String directory, String namePrefix, Bitmap bitmap) {
        boolean success = false;
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + directory);

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        String fname = namePrefix + "-" + System.currentTimeMillis() +".jpg";
        File file = new File (myDir, fname);
        if (file.exists()) file.delete();

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * Citanje slike iz eksternog skladista
     * @param directory direktorijum iz kojeg citamo
     * @param fileName - ime slike sa ekstenzijom
     * @return procitani bitmap
     */
    public Bitmap loadImage(String directory, String fileName) {
        return BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + File.separator + directory + File.separator + fileName);
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state); // Both Read and write operations available
    }

    private boolean isExternamStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(state); // Only Read operation available
    }

}
