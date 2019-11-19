package com.mvd.drunkgames.modules;

import android.app.Application;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.Button;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mvd.drunkgames.R;

import org.jetbrains.annotations.NotNull;

public class VoiceDetectModule extends GameController {

    private int MIN_SCREAM_LIMIT = 27000;
    private MutableLiveData<GameEvents> liveData = new MutableLiveData<>();

    public int getMinScreamLimit() {
        return MIN_SCREAM_LIMIT;
    }

    public void setMinScreamLimit(int minScreamLimit) {
        this.MIN_SCREAM_LIMIT = minScreamLimit;
    }

    //////////////
    private MutableLiveData<String> maxVolumeLiveData = new MutableLiveData<>();
    ///////////////


    private Application context;
    AudioRecord ar;

    public VoiceDetectModule(Application application) {
        super(application);
        this.context = application;
    }


    @NotNull
    @Override
    public LiveData<GameEvents> subscribeUpdates() {
       // isBlowing();
        return liveData;
    }

    public LiveData<String> getMaxVolumeLiveData() {
        return maxVolumeLiveData;
    }

    @Override
    public void unSubscribeUpdates() {
        if (ar != null) {
            ar.stop();
            ar.release();
            ar = null;
        }
    }


    public boolean isBlowing() {
        boolean recorder = true;
        int blow_value;

        int minSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
        ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, minSize);


        short[] buffer = new short[minSize];

        ar.startRecording();
        while (recorder) {

            ar.read(buffer, 0, minSize);
            for (short s : buffer) {
                Log.e("_cr", "Blow Value=" + s);
                if (Math.abs(s) > MIN_SCREAM_LIMIT)   //DETECT VOLUME (IF I BLOW IN THE MIC)
                {
                    blow_value = Math.abs(s);
                    Log.e("_cry", "Blow Value HIGH=" + blow_value);
                    ar.stop();
                    ar.release();
                    ar = null;
                    recorder = false;
                    maxVolumeLiveData.postValue(String.valueOf(blow_value));
                    liveData.postValue(GameEvents.SCREAM);
                    return true;
                }
            }
        }
        return false;
    }
}
