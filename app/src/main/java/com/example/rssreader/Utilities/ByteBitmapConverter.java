package com.example.rssreader.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class ByteBitmapConverter {

    public static Bitmap getBitmapFromBytes(byte[] data) {
        if (data == null)
            return null;
        return BitmapFactory.decodeByteArray(data , 0, data.length);
    }


    public static byte[] getBytesFromBitmap(Bitmap data) {
        if  (data == null)
            return new byte[]{};
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        data.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}
