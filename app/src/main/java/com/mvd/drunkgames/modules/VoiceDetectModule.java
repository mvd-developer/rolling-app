package com.mvd.drunkgames.modules;

import android.app.Application;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

public class VoiceDetectModule extends GameController {

    private MutableLiveData<GameEvents> liveData = new MutableLiveData<GameEvents>();
    private Application context;

    public VoiceDetectModule(Application application) {
        super(application);
        this.context = application;
    }


    @NotNull
    @Override
    public LiveData<GameEvents> subscribeUpdates() {
        isBlowing();
        return liveData;
    }

    @Override
    public void unSubscribeUpdates() {

    }


    public boolean isBlowing() {
        boolean recorder = true;
        int blow_value;

        int minSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
        AudioRecord ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, minSize);


        short[] buffer = new short[minSize];

        ar.startRecording();
        while (recorder) {

            ar.read(buffer, 0, minSize);
            for (short s : buffer) {
                Log.e("_cr", "Blow Value=" + s);
                if (Math.abs(s) > 27000)   //DETECT VOLUME (IF I BLOW IN THE MIC)
                {
                    blow_value = Math.abs(s);
                    Log.e("_cry", "Blow Value HIGH=" + blow_value);
                    ar.stop();
                    ar.release();
                    ar = null;
                    recorder = false;
                    return true;
                }
            }
        }
        return false;

    }


}
