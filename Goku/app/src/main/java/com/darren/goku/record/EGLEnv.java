package com.darren.goku.record;

import android.content.Context;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.view.Surface;

import com.darren.goku.filter.RecordFilter;

public class EGLEnv {

    private final EGLConfig mEglConfig;
    private final EGLContext mEglContext;
    private final EGLSurface mEglSurface;
    private final RecordFilter recordFilter;
    private  EGLDisplay mEglDisplay;

    public EGLEnv(Context context,EGLContext mGlContext, Surface surface,int width,int height) {
        // Get the display window as OpenGL's drawing target
        mEglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        if (mEglDisplay == EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("eglGetDisplay failed");
        }

         // Initialize the display window
        int[] version = new int[2];
        if(!EGL14.eglInitialize(mEglDisplay, version,0,version,1)) {
            throw new RuntimeException("eglInitialize failed");
        }


        // Configure the property options
        int[] configAttribs = {
                EGL14.EGL_RED_SIZE, 8, //The number of red digits in the color buffer
                EGL14.EGL_GREEN_SIZE, 8,//The number of green bits in the color buffer
                EGL14.EGL_BLUE_SIZE, 8, //
                EGL14.EGL_ALPHA_SIZE, 8,//
                EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT, //opengl es 2.0
                EGL14.EGL_NONE
        };
        int[] numConfigs = new int[1];
        EGLConfig[] configs = new EGLConfig[1];
        //EGL selects a configuration based on the properties
        if (!EGL14.eglChooseConfig(mEglDisplay, configAttribs, 0, configs, 0, configs.length,
                numConfigs, 0)) {
            throw new RuntimeException("EGL error " + EGL14.eglGetError());
        }

        mEglConfig = configs[0];


        /**
         * EGL context
         */
        int[] context_attrib_list = {
                EGL14.EGL_CONTEXT_CLIENT_VERSION,2,
                EGL14.EGL_NONE
        };
        //Share data with EGLContext in GLSurfaceView so that you can get the image texture that is displayed after processing.
        mEglContext= EGL14.eglCreateContext(mEglDisplay,mEglConfig,mGlContext ,context_attrib_list,0);

        if (mEglContext == EGL14.EGL_NO_CONTEXT){
            throw new RuntimeException("EGL error " + EGL14.eglGetError());
        }


        /**
         * Create EGLSurface
         */
        int[] surface_attrib_list = {
                EGL14.EGL_NONE
        };
        mEglSurface = EGL14.eglCreateWindowSurface(mEglDisplay, mEglConfig, surface, surface_attrib_list, 0);
        if (mEglSurface == null){
            throw new RuntimeException("EGL error " + EGL14.eglGetError());
        }

        /**
         * The display that binds the current thread is display
         */
       if (!EGL14.eglMakeCurrent(mEglDisplay,mEglSurface,mEglSurface,mEglContext)){
           throw new RuntimeException("EGL error " + EGL14.eglGetError());
       }

        recordFilter = new RecordFilter(context);
        recordFilter.setSize(width,height);
    }

    public void draw(int textureId, long timestamp) {
        recordFilter.onDraw(textureId);
        EGLExt.eglPresentationTimeANDROID(mEglDisplay,mEglSurface,timestamp);
        //EGLSurface Dual buffer mode
        EGL14.eglSwapBuffers(mEglDisplay,mEglSurface);

    }


    public void release(){
        EGL14.eglDestroySurface(mEglDisplay,mEglSurface);
        EGL14.eglMakeCurrent(mEglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_CONTEXT);
        EGL14.eglDestroyContext(mEglDisplay, mEglContext);
        EGL14.eglReleaseThread();
        EGL14.eglTerminate(mEglDisplay);
        recordFilter.release();
    }
}
