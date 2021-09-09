package com.darren.goku.record;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.opengl.EGLContext;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;

public class MediaRecorder {

    private final String TAG = this.getClass().getName();
    private final int mWidth;
    private final int mHeight;
    private final String mPath;
    private final Context mContext;

    private MediaCodec mMediaCodec;
    private Surface mSurface;
    private EGLContext mGlContext;
    private MediaMuxer mMuxer;
    private Handler mHandler;
    private boolean isStart;
    private int track;
    private float mSpeed;
    private long mLastTimeStamp;
    private EGLEnv eglEnv;

    public MediaRecorder(Context context, String path, EGLContext glContext, int width, int
            height) {
        mContext = context.getApplicationContext();
        mPath = path;
        mWidth = width;
        mHeight = height;
        mGlContext = glContext;
    }

    public void start(float speed) throws IOException {
        Log.d(TAG, "******************************************************Start被调用******************************************************");
        mSpeed = speed;
        MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC,
                mWidth, mHeight);
        //Color space gets from surface
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities
                .COLOR_FormatSurface);
        //code rate
        format.setInteger(MediaFormat.KEY_BIT_RATE, 1500_000);
        //frame rate
        format.setInteger(MediaFormat.KEY_FRAME_RATE, 25);
        //Key frame interval
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 10);
        //Create an encoder
        mMediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
        //Configure the encoder
        mMediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        mSurface = mMediaCodec.createInputSurface();

        //The mixer (reuser) encapsulates the encoded h.264 as mp4
        mMuxer = new MediaMuxer(mPath,
                MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        mMediaCodec.start();
        HandlerThread handlerThread = new HandlerThread("codec-gl");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //Create EGL environment
                eglEnv = new EGLEnv(mContext,mGlContext, mSurface,mWidth, mHeight);
                isStart = true;
            }
        });
    }


    public void fireFrame(final int textureId, final long timestamp) {
        if (!isStart) {
            return;
        }
        //The opengl for recording is already bound to the handler thread, so you need to use the recorded opengl in this thread
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //draw
                eglEnv.draw(textureId,timestamp);
                codec(false);
            }
        });
    }


    private void codec(boolean endOfStream) {
        //End signal
        if (endOfStream) {
            mMediaCodec.signalEndOfInputStream();
        }
        while (true) {
            //Get the output buffer (encoded data is obtained from the output buffer)
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int encoderStatus = mMediaCodec.dequeueOutputBuffer(bufferInfo, 10_000);
            if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                //If it is the end that exits directly, otherwise continue the loop
                if (!endOfStream) {
                    break;
                }
            } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                //The output format has changed
                MediaFormat newFormat = mMediaCodec.getOutputFormat();
                track = mMuxer.addTrack(newFormat);
                mMuxer.start();
            } else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {

            } else {
                //Adjust the timestamp
                bufferInfo.presentationTimeUs = (long) (bufferInfo.presentationTimeUs / mSpeed);
                if (bufferInfo.presentationTimeUs <= mLastTimeStamp) {
                    bufferInfo.presentationTimeUs = (long) (mLastTimeStamp + 1_000_000 / 25 / mSpeed);
                }
                mLastTimeStamp = bufferInfo.presentationTimeUs;

                //Normally encoderStatus gets the buffer subscript
                ByteBuffer encodedData = mMediaCodec.getOutputBuffer(encoderStatus);
                if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                    bufferInfo.size = 0;
                }
                if (bufferInfo.size != 0) {
                    //Set where to start reading data
                    encodedData.position(bufferInfo.offset);
                    //Sets the total length of the data that can be read
                    encodedData.limit(bufferInfo.offset + bufferInfo.size);
                    //Output as mp4
                    mMuxer.writeSampleData(track, encodedData, bufferInfo);
                }
                // Release this buffer and store the new encoded data later
                mMediaCodec.releaseOutputBuffer(encoderStatus, false);
                //signalEndOfInputStream
                if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    break;
                }
            }
        }
    }


    public void stop() {
        Log.d(TAG, "******************************************************Stop被调用******************************************************");
        // release
        isStart = false;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                codec(true);
                mMediaCodec.stop();
                mMediaCodec.release();
                mMediaCodec = null;
                mMuxer.stop();
                mMuxer.release();
                eglEnv.release();
                eglEnv = null;
                mMuxer = null;
                mSurface = null;
                mHandler.getLooper().quitSafely();
                mHandler = null;
            }
        });
    }
}
