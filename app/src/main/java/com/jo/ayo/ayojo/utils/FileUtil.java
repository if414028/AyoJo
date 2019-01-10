package com.jo.ayo.ayojo.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.jo.ayo.ayojo.BuildConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileUtil {
    private static final int FILETYPE_IMAGE = 1;
    private static final int REQUEST_FROM_CAMERA = 1001;
    private static final int REQUEST_FROM_ALBUM = 1002;
    private static String photoImagePath;

    public static void getFromCamera(Context ctx) throws IOException {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri fileUri = getOutputMediaFileUri(FILETYPE_IMAGE, ctx);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        ((Activity) ctx).startActivityForResult(intent, REQUEST_FROM_CAMERA);
    }

    public static void getFromAlbum(Context ctx) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((Activity) ctx).startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_FROM_ALBUM);
    }

    public static Uri getOutputMediaFileUri(int type, Context context) throws IOException {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            return Uri.fromFile(getOutputMediaFile(type));
        } else {
            return FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".provider", getOutputMediaFile(type));
        }
    }

    public static String downloadFromGoogleDrive(Context ctx, Uri uri) {
        String mimeType = ctx.getContentResolver().getType(uri);
        Cursor returnCursor = ctx.getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();

        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Bolalob");
        File mypath = new File(directory, returnCursor.getString(nameIndex));

        try {
            InputStream in = ctx.getContentResolver().openInputStream(uri);
            FileOutputStream fos = new FileOutputStream(mypath);
            BitmapFactory.decodeStream(in).compress(mimeType.contains("png") ?
                    Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return directory.getAbsolutePath() + "/" + returnCursor.getString(nameIndex);
    }

    public static File getOutputMediaFile(int type) throws IOException {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Bolalob");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("TAG", "Oops! Failed create " + "Bolalob" + " directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == FILETYPE_IMAGE) {
            mediaFile = File.createTempFile("IMG_" + timeStamp, ".jpg", mediaStorageDir);
        } else {
            return null;
        }

        photoImagePath = mediaFile.getAbsolutePath();

        return mediaFile;
    }

    private static File getTempImageFile(Context ctx, String filePath) {
        File dir;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            dir = prepareExternalCacheDir(ctx);
        } else {
            dir = ctx.getCacheDir();
        }

        String filename = filePath.substring(filePath.lastIndexOf('/') + 1);

        System.out.println("filename : " + filename);

        return new File(dir, "temp_" + filename);
    }

    private static File prepareExternalCacheDir(Context ctx) {
        String relativepath = "/Android/data/" + ctx.getPackageName() + "/Cache";
        File file = new File(android.os.Environment.getExternalStorageDirectory(), relativepath);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        return file;
    }

    public static String compressImage(Context ctx, String imagePath, int finalSize) {
        imagePath = imagePath != null ? imagePath : photoImagePath;
        File file = getTempImageFile(ctx, imagePath);

        int rotation = 0;
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("6")) {
                rotation = 90;
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")) {
                rotation = 270;
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")) {
                rotation = 180;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = resizeImage(imagePath, finalSize);
        bitmap = rotate(bitmap, rotation);

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Uri.fromFile(file).getPath();
    }

    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
    }

    public static Bitmap resizeImage(String imagePath, int requiredSize) {

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imagePath, options);
            int scale = 1;
            while (options.outWidth / scale / 2 >= requiredSize && options.outHeight / scale / 2 >= requiredSize)
                scale *= 2;

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(imagePath, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) { // ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1]; //internal storage
                } else {
                    return "/storage/sdcard1/" + split[1]; //sd card
                }
            } else if (isDownloadsDocument(uri)) {  // DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) { // MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            System.out.println("yiha");
            System.out.println(uri.toString());
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        } else {
            System.out.println("here");
            System.out.println(uri.getScheme());
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @NonNull
    public static MultipartBody.Part prepareFilePart(String partName, File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
}
