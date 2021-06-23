package com.miljanpeles.lib.bitmap;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class BitmapHelper {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;

        try {
            assert input != null;
            ei = new ExifInterface(input);

            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);

                default:
                    return img;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return img;
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0,0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    /**
     * Ako je bitmap horizontalan i sirine manje od dimens ne radi nista, ako je vece stavi ga na dimens i vrati takav bitmap
     * Ako je bitmap vertikalan i visine manje od dimens ne radi nista, ako je vece stavi ga na dimens i vrati takav bitmap
     * @param bm - bitmap koji se resizuje
     * @return resizovan bitmap
     */
    public static Bitmap getResizedBitmap(Bitmap bm, int dimens) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float aspectRatio = width / (float)height;
        int scaleWidth, scaleHeight;

        // horizontalna slika
        if(width > height) {
            if(width < dimens) return bm;

            scaleWidth = dimens;
            scaleHeight = Math.round(scaleWidth / aspectRatio);
        }
        // vertikalna slika
        else if(height > width) {
            if(height < dimens) return bm;

            scaleHeight = dimens;
            scaleWidth = Math.round(scaleHeight * aspectRatio);
        }
        // kvadratna slika
        else {
            if(width < dimens) return bm;

            scaleWidth = dimens;
            scaleHeight = dimens;
        }


        return Bitmap.createScaledBitmap(bm, scaleWidth, scaleHeight, false);
    }

    /**
     * Kompersuje bitmap
     * @param bitmap - bitmap za kompresovanje
     * @return - kompresovani bitmap
     */
    public static Bitmap compressBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
        return BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
    }

    /**
     * Konvertuje bitmap u Strinb Base64
     * @param imageBitmap - bitmap za konvertovanje
     * @return - string base64
     */
    public static String convertBitmapToBase64(Bitmap imageBitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream); // kompresuj bitmap
        byte[] byteArray = stream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    /**
     * Vraza size bitmapa u bajtovima
     * @param bitmap - bitmap od kojeg zelimo da vidimo size
     * @return - long u bajtovima
     */
    public static long getBitmapSize(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        //Log.e("BITMAP", "Size: " + lengthbmp);
        return imageInByte.length;
    }

    private static final double THUMBNAIL_SIZE = 80;

    /**
     * Vraca thumbnail od bitmapa
     * @param context - context
     * @param uri - uri
     * @return thumbnail bitmap
     * @throws FileNotFoundException - ako fajl nije pronadjen
     * @throws IOException - ioexception
     */
    public static Bitmap getThumbnail(Context context, Uri uri) throws FileNotFoundException, IOException{
        InputStream input = context.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = Math.max(onlyBoundsOptions.outHeight, onlyBoundsOptions.outWidth);

        double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//
        input = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }

    public long getFileSize(final File file) {
        if (file == null || !file.exists())
            return 0;
        if (!file.isDirectory())
            return file.length();
        final List<File> dirs = new LinkedList<>();
        dirs.add(file);
        long result = 0;
        while (!dirs.isEmpty()) {
            final File dir = dirs.remove(0);
            if (!dir.exists())
                continue;
            final File[] listFiles = dir.listFiles();
            if (listFiles == null || listFiles.length == 0)
                continue;
            for (final File child : listFiles) {
                result += child.length();
                if (child.isDirectory())
                    dirs.add(child);
            }
        }

        return result;
    }

    /*public static String imageUriToStringForServer(Context context, Uri uri){
        if(uri != null) {
            InputStream iStream = null;
            try {
                iStream = context.getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte[] byteArray = new byte[0];
            try {
                assert iStream != null;
                byteArray = getBytes(iStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Base64.encodeToString(byteArray, Base64.NO_WRAP);
        } else{
            return null;
        }
    }

    private static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }*/

    /**
     * Uzima ekstenziju slike
     * @param context - context
     * @param uri - uri
     * @return - string ekstenzije
     */
    public static String getImageExtension(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        }
        return extension;
    }

    /**
     * Iz resource u bitmap
     * @param context context
     * @param resourceId resource id
     * @return bitmap
     */
    public static Bitmap getBitmapFromResources(Context context, int resourceId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        return BitmapFactory.decodeResource(context.getResources(), resourceId, options);
    }


    private int sizeOf(Bitmap data) {
        return data.getRowBytes() * data.getHeight();
    }
}
