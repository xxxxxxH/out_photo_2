package net.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class Utils {
    /*
     *Bitmap转byte数组
     */
    public static byte[] BitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /*
     *byte数组转Bitmap
     */
    public static Bitmap BytesToBitmap(byte[] bis) {
        return BitmapFactory.decodeByteArray(bis, 0, bis.length);
    }
}
