package com.mvd.drunkgames.modules;

import android.app.Application;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;


public class VoiceDetectModule extends GameController {

    private int MIN_SCREAM_LIMIT = 27000;
    private MutableLiveData<GameEvents> liveData = new MutableLiveData<>();
    final Handler handler = new Handler();
    int minSize;
    int blow_value;
    short[] buffer;
    Runnable runnable;

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

    @Override
    public LiveData<GameEvents> subscribeUpdates() {
        Log.e("_cryy", "subscribeUpdates " + this.getClass().toString());
        return liveData;
    }

    @NotNull
   // @Override
    public LiveData<String> getMaxVolumeLiveData() {
        return maxVolumeLiveData;
    }

    @Override
    public void unSubscribeUpdates() {
        stopDetection();
    }

    public void stopDetection() {
        if (ar != null) {
            ar.stop();
            ar.release();
            ar = null;
        }
        if (handler != null)
            handler.removeCallbacks(runnable);
    }

    public void startDetection() {
        minSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
        ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, minSize);
        buffer = new short[minSize];
        ar.startRecording();

        runnable = new Runnable() {
            public void run() {
                if (ar != null) {
                    ar.read(buffer, 0, minSize);
                    for (short s : buffer) {
                        Log.e("_cr", "Blow Value=" + s);
                        if (Math.abs(s) > MIN_SCREAM_LIMIT)   //DETECT VOLUME (IF I BLOW IN THE MIC)
                        {
                            handler.removeCallbacks(runnable);
                            blow_value = Math.abs(s);
                            Log.e("_cry", "Blow Value HIGH=" + blow_value);
                            maxVolumeLiveData.postValue(String.valueOf(blow_value));
                            liveData.postValue(GameEvents.SCREAM);
                            break;
                        }
                    }
                }
                handler.postDelayed(this, 100);
            }
        };
        handler.post(runnable);
    }
}
