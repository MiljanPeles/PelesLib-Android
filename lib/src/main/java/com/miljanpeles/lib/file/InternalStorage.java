package com.miljanpeles.lib.file;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class InternalStorage {

    private static final String TAG = "InternalStorage";

    private Context context;

    public InternalStorage(Context context) {
        this.context = context;
    }

    /**
     * Cuva bitmap u internom skladistu aplikacije
     * ** Pre koriscenja proveriti permisiju za WRITE_EXTERNAL_STORAGE
     * @param folder - naziv foldera u kojem se cuva bitmap
     * @param bitmapImage - bitmap
     * @param fileName - ime bitmapa za cuvanje bez ekstenzije
     * @return vraca apsolutnu putanju ka direktorijumu gde je slika smestena
     */
    public String saveBitmap(String folder, Bitmap bitmapImage, String fileName){
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());

        File directory = cw.getDir(folder, Context.MODE_PRIVATE); // dobijamo folder gde smestamo
        File mypath = new File(directory,fileName + ".jpg"); // kreiramo fajl

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Log.e(TAG, "Slika " + fileName + ".jpg uspesno sacuvana u " + directory.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    /**
     * Ucitava bitmap iz internog skladista
     * Proveriti permisije za WRITE_EXTERNAL_STORAGE pre koriscenja
     * @param folder - folder u kojem trazimo sliku
     * @param fileName - ime slike koju trazimo (bez ekstenzije)
     * @return - Vraca bitmap ili null ako bitmap nije pronadjen
     */
    public Bitmap loadBitmap(String folder, String fileName)
    {
        try {
            ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
            File directory = cw.getDir(folder, Context.MODE_PRIVATE);
            File f = new File(directory,fileName + ".jpg");
            Log.e(TAG, "Uspesno ucitao sliku sa putanje " + context.getFilesDir().getAbsolutePath() + File.separator + folder);

            //File[] files = f.listFiles();
            //for(File file : files) Log.e("FILE", "Fajl: " + file.getName());

            return BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  Upisivanje teksa u fajl u specificnom folderu
     * @param folder - Ime foldera u koji stavljamo fajl
     * @param fileName - ime fajla (bez ekstenzije)
     * @param text - tekst koji upisujemo
     */
    public void writeText(String folder, String fileName, String text) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(folder + File.separator + fileName + ".txt", Context.MODE_PRIVATE);
            fos.write(text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Cita tekst iz fajla u specificnom folderu
     * @param folder - folder iz kojeg citamo
     * @param fileName - fajl koji citamo
     * @return tekst koji procitamo
     * @throws FileNotFoundException - Ako file ne postoji baca exception
     */
    public String readText(String folder, String fileName) throws FileNotFoundException {

        String contents;
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(folder + File.separator + fileName + ".txt");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
        if(fis == null) {
            throw new NullPointerException();
        }

        InputStreamReader inputStreamReader = new InputStreamReader(fis);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            contents = stringBuilder.toString();
        }

        return contents;
    }

    /**
     * Lista svih fajlova internog skladista
     * @return - vraca niz stringova svih fajlova u internom skladistu
     */
    public String[] getFileNames() {
        return context.fileList();
    }
}
