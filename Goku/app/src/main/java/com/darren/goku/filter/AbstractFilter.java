package com.darren.goku.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.darren.goku.utils.OpenGLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class AbstractFilter {
    FloatBuffer vertexBuffer; //Vertex coordinate cache
    FloatBuffer textureBuffer; // Texture coordinates
    int mWidth;
    int mHeight;
    int program;
    int vPosition;
    int vCoord;
    int vTexture;

    public AbstractFilter(Context context, int vertexShaderId, int fragmentShaderId) {
        //Prepare the data
        vertexBuffer = ByteBuffer.allocateDirect(4 * 4 * 2).order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        textureBuffer = ByteBuffer.allocateDirect(4 * 4 * 2).order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        initCoord();
        initGL(context,vertexShaderId,fragmentShaderId);
    }

    public void initGL(Context context,int vertexShaderId, int fragmentShaderId) {
        String vertexSharder = OpenGLUtils.readRawTextFile(context, vertexShaderId);
        String fragSharder = OpenGLUtils.readRawTextFile(context, fragmentShaderId);
        //shader program
        program = OpenGLUtils.loadProgram(vertexSharder, fragSharder);

        //Gets the variable index in the program
        vPosition = GLES20.glGetAttribLocation(program, "vPosition");
        vCoord = GLES20.glGetAttribLocation(program, "vCoord");
        vTexture = GLES20.glGetUniformLocation(program, "vTexture");
    }

    public void initCoord(){
        vertexBuffer.clear();
        vertexBuffer.put(OpenGLUtils.VERTEX);

        textureBuffer.clear();
        textureBuffer.put(OpenGLUtils.TEXURE);
    }


    public void setSize(int width, int height) {
        mWidth = width;
        mHeight = height;
    }


    public int onDraw(int texture) {
        //Set the drawing area
        GLES20.glViewport(0, 0, mWidth, mHeight);


        GLES20.glUseProgram(program);

        vertexBuffer.position(0);
        //normalized
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        //The CPU transmits data to the GPU, which is not read by default by the shader. We need to enable it before we can read it
        GLES20.glEnableVertexAttribArray(vPosition);


        textureBuffer.position(0);
        //normalized
        GLES20.glVertexAttribPointer(vCoord, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);
 
        GLES20.glEnableVertexAttribArray(vCoord);


        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture);
        GLES20.glUniform1i(vTexture,0);

        beforeDraw();
        //Notification drawing
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);

        return texture;
    }

    public void beforeDraw() {
    }

    public void release(){
        GLES20.glDeleteProgram(program);
    }
}
