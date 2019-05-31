package com.tuochebang.user.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ImageCompress {
    public static final String CONTENT = "content";
    public static final String FILE = "file";

    public static class CompressOptions {
        public static final int DEFAULT_HEIGHT = 800;
        public static final int DEFAULT_WIDTH = 400;
        public File destFile;
        public String filepath;
        public int id;
        public CompressFormat imgFormat = CompressFormat.JPEG;
        public int maxHeight = DEFAULT_HEIGHT;
        public int maxWidth = DEFAULT_WIDTH;
        public int quality = 30;
        public Uri uri;
    }

    private static class ImageCompressHolder {
        public static ImageCompress INSTANCE = new ImageCompress();

        private ImageCompressHolder() {
        }
    }

    public static ImageCompress getInstance() {
        return ImageCompressHolder.INSTANCE;
    }

    private Bitmap compressFromUri(Context context, CompressOptions compressOptions) {
        Bitmap bitmap;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap temp = BitmapFactory.decodeResource(context.getResources(), compressOptions.id, options);
        int actualWidth = options.outWidth;
        int actualHeight = options.outHeight;
        int desiredWidth = getResizedDimension(compressOptions.maxWidth, compressOptions.maxHeight, actualWidth, actualHeight);
        int desiredHeight = getResizedDimension(compressOptions.maxHeight, compressOptions.maxWidth, actualHeight, actualWidth);
        options.inJustDecodeBounds = false;
        options.inSampleSize = findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight);
        Bitmap destBitmap = BitmapFactory.decodeResource(context.getResources(), compressOptions.id, options);
        if (destBitmap.getWidth() > desiredWidth || destBitmap.getHeight() > desiredHeight) {
            bitmap = Bitmap.createScaledBitmap(destBitmap, desiredWidth, desiredHeight, true);
            destBitmap.recycle();
        } else {
            bitmap = destBitmap;
        }
        if (compressOptions.destFile != null) {
            compressFile(compressOptions, bitmap);
        }
        return bitmap;
    }

    private void compressFile(CompressOptions compressOptions, Bitmap bitmap) {
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(compressOptions.destFile);
        } catch (FileNotFoundException e) {
            Log.e("ImageCompress", e.getMessage());
        }
        bitmap.compress(compressOptions.imgFormat, compressOptions.quality, stream);
    }

    private static int findBestSampleSize(int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        float n = 1.0f;
        while (((double) (2.0f * n)) <= Math.min(((double) actualWidth) / ((double) desiredWidth), ((double) actualHeight) / ((double) desiredHeight))) {
            n *= 2.0f;
        }
        return (int) n;
    }

    private static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary, int actualSecondary) {
        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        }
        if (maxPrimary == 0) {
            return (int) (((double) actualPrimary) * (((double) maxSecondary) / ((double) actualSecondary)));
        } else if (maxSecondary == 0) {
            return maxPrimary;
        } else {
            double ratio = ((double) actualSecondary) / ((double) actualPrimary);
            int resized = maxPrimary;
            if (((double) resized) * ratio > ((double) maxSecondary)) {
                resized = (int) (((double) maxSecondary) / ratio);
            }
            return resized;
        }
    }

    public Drawable getCompressFromId(Context context, int id, int w, int h) {
        CompressOptions compressOptions = new CompressOptions();
        compressOptions.id = id;
        compressOptions.maxHeight = h;
        compressOptions.maxWidth = w;
        Bitmap bitmap = compressFromUri(context, compressOptions);
        System.out.println("**************End********************");
        System.out.println("Width:" + bitmap.getWidth() + ", Height:" + bitmap.getHeight());
        System.out.println("Size:" + bitmap.getByteCount());
        System.out.println("**********************************");
        return convertBitmap2Drawable(bitmap);
    }

    public Drawable convertBitmap2Drawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }
}
