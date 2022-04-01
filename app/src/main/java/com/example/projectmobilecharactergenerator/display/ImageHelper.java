package com.example.projectmobilecharactergenerator.display;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.projectmobilecharactergenerator.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageHelper {
    public static String saveArt(Bitmap bitmap, String name, Context appContext)
    {
        String artPath = null;
        if (bitmap != null)
        {
            Toast.makeText(appContext, "Saving " + name + " image ...", Toast.LENGTH_SHORT);
            ContextWrapper cw = new ContextWrapper(appContext);
            String fileName = name.replaceAll(" ", "_").replaceAll("/", "_") + "_" + String.valueOf(System.currentTimeMillis()) + ".png";
            File directory = cw.getDir(appContext.getString(R.string.art_dir), Context.MODE_PRIVATE);
            File filePath = new File(directory, fileName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(filePath);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                artPath = filePath.getAbsolutePath();
            } catch (Exception e)
            {
                artPath = null;
            }
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return artPath;
    }

    public static Bitmap loadArt(String path)
    {
        Bitmap art = null;
        if (path != null)
        {
            try {
                File file = new File(path);
                FileInputStream fileInputStream = new FileInputStream(file);
                art = BitmapFactory.decodeStream(fileInputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return art;
    }
}
