package com.example.owntage.es2;

import static android.opengl.GLES20.*;

import android.opengl.Matrix;
import android.util.Pair;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Created by Owntage on 11/20/2015.
 */
public class QuadMesh {

    int[] vertexBufferIndex = new int[2]; //0 - positions, 1 - textures
    Shader shader;
    Texture tileset;
    int posAttribLocation;
    int texPosAttribLocation;
    FloatBuffer coordsFB;
    FloatBuffer texCoordsFB;
    int numCoords;

    public QuadMesh(Shader shader, Texture tileset, ArrayList<Float> coords, ArrayList<Float> texCoords) {
        this.shader = shader;
        this.tileset = tileset;
        numCoords = coords.size();

        float[] coordsArray = new float[coords.size()];
        float[] texCoordsArray = new float[texCoords.size()];

        for(int i = 0; i < coords.size(); i++) {
            coordsArray[i] = coords.get(i);
        }

        for(int i = 0; i < texCoords.size(); i++) {
            texCoordsArray[i] = texCoords.get(i);
        }

        ByteBuffer coordsByteBuffer = ByteBuffer.allocateDirect(coordsArray.length * 4);
        ByteBuffer texCoordsByteBuffer = ByteBuffer.allocateDirect(texCoordsArray.length * 4);
        coordsByteBuffer.order(ByteOrder.nativeOrder());
        texCoordsByteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer coordsFloatBuffer = coordsByteBuffer.asFloatBuffer();
        FloatBuffer texCoordsFloatBuffer = texCoordsByteBuffer.asFloatBuffer();
        coordsFloatBuffer.put(coordsArray);
        texCoordsFloatBuffer.put(texCoordsArray);
        coordsFloatBuffer.position(0);
        texCoordsFloatBuffer.position(0);
        /*
        glGenBuffers(2, vertexBufferIndex, 0);
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferIndex[0]);
        glBufferData(vertexBufferIndex[0], coordsFloatBuffer.capacity() * 4, coordsFloatBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferIndex[1]);
        glBufferData(vertexBufferIndex[1], texCoordsFloatBuffer.capacity() * 4, texCoordsFloatBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        */

        coordsFB = coordsFloatBuffer;
        texCoordsFB = texCoordsFloatBuffer;

        posAttribLocation = glGetAttribLocation(shader.getShaderProgram(), "vPosition");
        texPosAttribLocation = glGetAttribLocation(shader.getShaderProgram(), "vTexture");
    }
    public void draw() {
        shader.bind();
        tileset.bind();
        Matrix.setIdentityM(shader.getTransform(), 0);
        shader.updateTransform();

        glEnableVertexAttribArray(posAttribLocation);
        glVertexAttribPointer(posAttribLocation, 2, GL_FLOAT, false, 0, coordsFB);

        glEnableVertexAttribArray(texPosAttribLocation);
        glVertexAttribPointer(texPosAttribLocation, 2, GL_FLOAT, false, 0, texCoordsFB);
        glDrawArrays(GL_TRIANGLES, 0, numCoords / 2);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}
