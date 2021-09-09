package com.darren.goku.utils;

import android.os.HandlerThread;
import android.util.Size;

import androidx.camera.core.CameraX;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.lifecycle.LifecycleOwner;


public class CameraHelper {

    private HandlerThread handlerThread;
    private CameraX.LensFacing currentFacing = CameraX.LensFacing.BACK;
    private Preview.OnPreviewOutputUpdateListener listener;

    public CameraHelper(LifecycleOwner lifecycleOwner, Preview.OnPreviewOutputUpdateListener listener) {
        this.listener = listener;
        handlerThread = new HandlerThread("Analyze-thread");
        handlerThread.start();
        CameraX.bindToLifecycle(lifecycleOwner, getPreView());
    }

    private Preview getPreView() {
        // The resolution is not the final resolution. CameraX will automatically set the closest resolution based on the support of the device and your parameters.
        PreviewConfig previewConfig = new PreviewConfig.Builder()
                .setTargetResolution(new Size(640, 480))
                .setLensFacing(currentFacing) //Front or rear camera
                .build();
        Preview preview = new Preview(previewConfig);
        preview.setOnPreviewOutputUpdateListener(listener);
        return preview;
    }


}
