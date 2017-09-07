package com.zxventures.beer.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class ImageUtils.
 */
public class ImageUtils {

    private static final String PICTURES_FORMAT = ".jpg";

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            Log.e("ImageUtils", "rotateBitmap", e);
        }
        return bitmap;
    }

    /**
     * Gets the rounded corner bitmap.
     *
     * @param bitmap the bitmap
     * @param pixels the pixels
     * @return the rounded corner bitmap
     */
    public static Bitmap getRoundedCornerBitmap(final Bitmap bitmap,
                                                final int pixels) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap getCircularBitmapImage(Bitmap source) throws OutOfMemoryError {

        Bitmap bitmap = null;

        if (source != null) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            // Decode image size
            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }
            bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            squaredBitmap.recycle();
        }

        return bitmap;
    }


    /**
     * Creates a circular bitmap and uses whichever dimension is smaller to determine the width
     * <br/>Also constrains the circle to the leftmost part of the image
     *
     * @param bitmap
     * @return bitmap
     */
    public static Bitmap getCircularBitmap(Bitmap bitmap) throws NullPointerException, OutOfMemoryError {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int width = bitmap.getWidth();
        if (bitmap.getWidth() > bitmap.getHeight())
            width = bitmap.getHeight();
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, width);
        final RectF rectF = new RectF(rect);
        final float roundPx = width / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * Copy stream.
     *
     * @param is the is
     * @param os the os
     */
    public static void CopyStream(final InputStream is, final OutputStream os) {
        final int buffer_size = 1024;
        try {
            final byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                final int count = is.read(bytes, 0, buffer_size);
                if (count == -1) {
                    break;
                }
                os.write(bytes, 0, count);
            }
        } catch (final Exception e) {
            Log.e("ImageUtils", "CopyStream", e);
        }
    }

    /**
     * Gets the byte count.
     *
     * @param bitmap the bitmap
     * @return the byte count
     */
    public static int getByteCount(final Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * Copy stream.
     *
     * @param is the is
     * @param os the os
     */
    public static void copyStream(final InputStream is, final OutputStream os) {

        final int bufferSize = 1024;

        try {

            final byte[] bytes = new byte[bufferSize];

            for (; ; ) {

                final int count = is.read(bytes, 0, bufferSize);

                if (count == -1) {
                    break;
                }

                os.write(bytes, 0, count);
            }
        } catch (final IOException e) {
            Log.e("ImageUtilities", "copyStream", e);
        }
    }

    /**
     * Creates the marker icon.
     *
     * @param backgroundImage the background image
     * @param text            the text
     * @param width           the width
     * @param height          the height
     * @return the drawable
     */
    public static Drawable createMarkerIcon(final Drawable backgroundImage,
                                            final String text,
                                            final int width, final int height) {

        final Bitmap canvasBitmap = Bitmap.createBitmap(width, height,
                Config.ARGB_8888);
        // Create a canvas, that will draw on to canvasBitmap.
        final Canvas imageCanvas = new Canvas(canvasBitmap);

        // Set up the paint for use with our Canvas
        final Paint imagePaint = new Paint();
        imagePaint.setTextAlign(Align.CENTER);
        imagePaint.setTextSize(60f);

        // Draw the image to our canvas
        backgroundImage.draw(imageCanvas);

        // Draw the text on top of our image
        imageCanvas.drawText(text, width / 2, height / 2, imagePaint);

        // Combine background and text to a LayerDrawable
        final LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                backgroundImage, new BitmapDrawable(canvasBitmap)
        });
        return layerDrawable;
    }

    /**
     * Drawable to bitmap.
     *
     * @param drawable the drawable
     * @return the bitmap
     */
    public static Bitmap drawableToBitmap(final Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        return ((BitmapDrawable) drawable).getBitmap();
    }

    /**
     * Gets the bitmap from resource.
     *
     * @param defaultDisplay the display
     * @param resId          the res id
     * @return the bitmap from resource
     */
    public static Bitmap getBitmapFromResource(final Display defaultDisplay, Resources res,
                                               final int resId) {

        final DisplayMetrics metrics = new DisplayMetrics();

        defaultDisplay.getMetrics(metrics);
        final int screenDensity = metrics.densityDpi;

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 1;
        options.inScaled = true;
        options.inTargetDensity = screenDensity;

        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeBitmapFromResource(Resources res, int resId,
                                                  int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * Decode sampled bitmap from uri.
     *
     * @param path      the path
     * @param reqWidth  the req width
     * @param reqHeight the req height
     * @return the bitmap
     */
    public static Bitmap decodeBitmapFromUri(final String path,
                                             final int reqWidth, final int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static int dpToPx(final Resources res, int dp) {
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static int pxToDp(final Resources res, int px) {
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        return File.createTempFile(
                imageFileName,  /* prefix */
                PICTURES_FORMAT,/* suffix */
                storageDir/* directory */);
    }

    public static File createInternalImageFile(final Context ctx) throws IOException {
        ContextWrapper cw = new ContextWrapper(ctx.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = cw.getDir("images", Context.MODE_PRIVATE);

        return File.createTempFile(
                imageFileName,  /* prefix */
                PICTURES_FORMAT,/* suffix */
                storageDir/* directory */);
    }

    public static String saveBitmap(Bitmap bitmapImage) throws NullPointerException {
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = ImageUtils.createImageFile();

            FileOutputStream fos = new FileOutputStream(photoFile.getAbsolutePath());

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
        } catch (IOException e) {
            Log.e("ImageUtilities", "saveBitmap", e);
        }
        assert photoFile != null;
        return photoFile.getAbsolutePath();
    }

    public static String saveBitmapPrivate(Bitmap bitmapImage) throws NullPointerException {
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = ImageUtils.createImageFile();

            FileOutputStream fos = new FileOutputStream(photoFile.getAbsolutePath());

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
        } catch (IOException e) {
            Log.e("ImageUtilities", "saveBitmap", e);
        }
        assert photoFile != null;
        return photoFile.getAbsolutePath();
    }

    /**
     * Resize bitmap.
     *
     * @param originalBitmap the original bitmap
     * @param w              the w
     * @param h              the h
     * @return the bitmap
     */
    public static Bitmap resizeBitmap(final Bitmap originalBitmap, final int w,
                                      final int h) {

        final int width = originalBitmap.getWidth();
        final int height = originalBitmap.getHeight();

        // calculate the scale
        final float scaleWidth = (float) w / width;
        final float scaleHeight = (float) h / height;

        // create a matrix for the manipulation
        final Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);

        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        return Bitmap.createBitmap(originalBitmap, 0, 0, width, height, matrix,
                true);
    }

    // Scale and maintain aspect ratio given a desired width
    // BitmapScaler.scaleToFitWidth(bitmap, 100);
    public static Bitmap scaleToFitWidth(Bitmap b, int width) {
        float factor = width / (float) b.getWidth();
        return Bitmap.createScaledBitmap(b, width, (int) (b.getHeight() * factor), true);
    }

    // Scale and maintain aspect ratio given a desired height
    // BitmapScaler.scaleToFitHeight(bitmap, 100);
    public static Bitmap scaleToFitHeight(Bitmap b, int height) {
        float factor = height / (float) b.getHeight();
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factor), height, true);
    }

    // Decodes image and scales it to reduce memory consumption
    public static Bitmap decodeFile(File f, int REQUIRED_SIZE) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {//unused}
            return null;
        }
    }

    public static Bitmap decodeFile(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(filePath, options);
    }

    private String saveBitmapToInternalSorage(final Context ctx, Bitmap bitmapImage) {

        ContextWrapper cw = new ContextWrapper(ctx.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("images", Context.MODE_PRIVATE);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_" + PICTURES_FORMAT;
        // Create imageDir
        File mypath = new File(directory, imageFileName);

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
        } catch (IOException e) {
            Log.e("ImageUtilities", "getBitmap", e);
        }
        return directory.getAbsolutePath();
    }

}