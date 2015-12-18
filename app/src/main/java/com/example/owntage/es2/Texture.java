package com.example.owntage.es2;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.InputStream;

/**
 * Created by Owntage on 10/20/2015.
 */
public class Texture {
    int[] textureID = new int[1];
    public Texture(Resources resources, String filename)
    {

        int identifier = resources.getIdentifier(filename, "drawable", MainActivity.PACKAGE_NAME);
        Log.e("texture identifier: ", "value: " + identifier + " filename: " + filename);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.CONTEXT.getResources(), identifier, options);

        GLES20.glGenTextures(1, textureID, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        if(textureID[0] == 0)
        {
            Log.e("texture", "texture ID equals zero");
        }
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
    }
    void bind()
    {
        //GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID[0]);
    }
}
