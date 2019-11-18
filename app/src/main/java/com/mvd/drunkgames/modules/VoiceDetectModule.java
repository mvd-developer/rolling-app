package com.mvd.drunkgames.modules;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

public class VoiceDetectModule extends GameController {

    private MutableLiveData<GameEvents> liveData;
    private Context context;

    VoiceDetectModule(Application application, MutableLiveData<GameEvents> liveData) {
        super(application);
        this.context = application;
        this.liveData = liveData;
    }




    @NotNull
    @Override
    public LiveData<GameEvents> subscribeUpdates() {
        return null;
    }

    @Override
    public void unSubscribeUpdates() {

    }
}
