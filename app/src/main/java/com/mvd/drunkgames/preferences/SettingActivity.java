package com.mvd.drunkgames.preferences;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mvd.drunkgames.R;

public class SettingActivity extends AppCompatActivity {

    private SeekBar seekBarMicrophone;
    private SeekBar seekBarAccelerometer;
    private TextView microphoneBoarderValue;
    private TextView microphoneBoarderCheckTv;
    private TextView accelerometerBoarderValue;
    private RadioGroup modeRadioGroup;
    private RadioButton deathRadioButton;
    private RadioButton countDownRadioButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_java);
        seekBarMicrophone = findViewById(R.id.seekBarMicrophone);
        seekBarAccelerometer = findViewById(R.id.seekBarAccelerometer);
        microphoneBoarderValue = findViewById(R.id.microphoneBoarderValue);
        microphoneBoarderCheckTv = findViewById(R.id.microphoneBoarderCheckTv);
        accelerometerBoarderValue = findViewById(R.id.accelerometerBoarderValue);
        modeRadioGroup = findViewById(R.id.modeRadioGroup);
        deathRadioButton = findViewById(R.id.deathRadioButton);
        countDownRadioButton = findViewById(R.id.countDownRadioButton);

        seekBarMicrophone.setProgress(PrefsManager.INSTANCE.getVoiceDetectionLowBoarder());
        microphoneBoarderValue.setText(String.valueOf(seekBarMicrophone.getProgress()));
        seekBarMicrophone.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                microphoneBoarderValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                PrefsManager.INSTANCE.setVoiceDetectionLowBoarder(seekBar.getProgress());
            }
        });
        seekBarAccelerometer.setProgress((int) PrefsManager.INSTANCE.getAccelerometrSensitivity());
        accelerometerBoarderValue.setText(String.valueOf(seekBarAccelerometer.getProgress()));
        seekBarAccelerometer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                accelerometerBoarderValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                PrefsManager.INSTANCE.setAccelerometrSensitivity(seekBar.getProgress());
            }
        });
        checkGameMode();

        modeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.deathRadioButton) {
                    PrefsManager.INSTANCE.setGameMode(0);
                } else {
                    PrefsManager.INSTANCE.setGameMode(1);
                }
            }
        });
    }

    private void checkGameMode() {
        int mode = PrefsManager.INSTANCE.getGameMode();
        if (mode == 0) {
            deathRadioButton.setChecked(true);
            countDownRadioButton.setChecked(false);
        } else {
            deathRadioButton.setChecked(false);
            countDownRadioButton.setChecked(true);
        }
    }
}
