package com.hardkernel.odroid.rotaryencoder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private Button mApply;

    private EditText mETPortAGPIO;
    private RadioButton mRBPortAHigh;
    private RadioButton mRBPortALow;
    private Switch mSWPortAPullupdn;
    private EditText mETPortAKeycode;

    private int mPortAGPIO;
    private int mPortAActive;
    private int mPortAPullupdn;
    private int mPortAKeycode;

    private EditText mETPortBGPIO;
    private RadioButton mRBPortBHigh;
    private RadioButton mRBPortBLow;
    private Switch mSWPortBPullupdn;
    private EditText mETPortBKeycode;

    private int mPortBGPIO;
    private int mPortBActive;
    private int mPortBPullupdn;
    private int mPortBKeycode;

    private EditText mETButtonGPIO;
    private RadioButton mRBButtonHigh;
    private RadioButton mRBButtonLow;
    private Switch mSWButtonPullupdn;
    private EditText mETButtonKeycode;

    private int mButtonGPIO;
    private int mButtonActive;
    private int mButtonPullupdn;
    private int mButtonKeycode;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mETPortAGPIO = (EditText) findViewById(R.id.et_a_gpio);
        mRBPortAHigh = (RadioButton) findViewById(R.id.rb_a_high);
        mRBPortALow = (RadioButton) findViewById(R.id.rb_a_low);
        mSWPortAPullupdn = (Switch) findViewById(R.id.sw_a_pull);
        mETPortAKeycode = (EditText) findViewById(R.id.et_a_keycode);

        mETPortBGPIO = (EditText) findViewById(R.id.et_b_gpio);
        mRBPortBHigh = (RadioButton) findViewById(R.id.rb_b_high);
        mRBPortBLow = (RadioButton) findViewById(R.id.rb_b_low);
        mSWPortBPullupdn = (Switch) findViewById(R.id.sw_b_pull);
        mETPortBKeycode = (EditText) findViewById(R.id.et_b_keycode);

        mETButtonGPIO = (EditText) findViewById(R.id.et_bt_gpio);
        mRBButtonHigh = (RadioButton) findViewById(R.id.rb_bt_high);
        mRBButtonLow = (RadioButton) findViewById(R.id.rb_bt_low);
        mSWButtonPullupdn = (Switch) findViewById(R.id.sw_bt_pull);
        mETButtonKeycode = (EditText) findViewById(R.id.et_bt_keycode);

        SharedPreferences pref = getSharedPreferences("rotary_encoder", Context.MODE_PRIVATE);

        mPortAGPIO = pref.getInt("port_a_gpio", 233);
        mPortAActive = pref.getInt("port_a_active", 1);
        mPortAPullupdn = pref.getInt("port_a_pullupdn", 1);
        mPortAKeycode = pref.getInt("port_a_keycode", 114);
        mPortBGPIO = pref.getInt("port_b_gpio", 236);
        mPortBActive = pref.getInt("port_b_active", 1);
        mPortBPullupdn = pref.getInt("port_b_pullupdn", 1);
        mPortBKeycode = pref.getInt("port_b_keycode", 115);
        mButtonGPIO = pref.getInt("button_gpio", 231);
        mButtonActive = pref.getInt("button_active", 1);
        mButtonPullupdn = pref.getInt("button_pullupdn", 1);
        mButtonKeycode = pref.getInt("button_keycode", 113);

        mApply = (Button) findViewById(R.id.bt_apply);
        mApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRotaryEncoderConfig();
                int result = setRotaryEncoder(mPortAGPIO, mPortAActive, mPortAPullupdn, mPortAKeycode,
                        mPortBGPIO, mPortBActive, mPortBPullupdn, mPortBKeycode,
                        mButtonGPIO, mButtonActive, mButtonPullupdn, mButtonKeycode);
                if (result == 0)
                    saveRotaryEncoderConfig();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(0);
    }

    private void initControl() {
        mETPortAGPIO.setText(String.valueOf(mPortAGPIO));
        if (mPortAActive == 1)
            mRBPortAHigh.setChecked(true);
        else
            mRBPortALow.setChecked(true);

        if (mPortAPullupdn == 1)
            mSWPortAPullupdn.setChecked(true);
        else
            mSWPortAPullupdn.setChecked(false);
        mETPortAKeycode.setText(String.valueOf(mPortAKeycode));

        mETPortBGPIO.setText(String.valueOf(mPortBGPIO));
        if (mPortBActive == 1)
            mRBPortBHigh.setChecked(true);
        else
            mRBPortBLow.setChecked(true);
        if (mPortAPullupdn == 1)
            mSWPortBPullupdn.setChecked(true);
        else
            mSWPortBPullupdn.setChecked(false);
        mETPortBKeycode.setText(String.valueOf(mPortBKeycode));

        mETButtonGPIO.setText(String.valueOf(mButtonGPIO));
        if (mButtonActive == 1)
            mRBButtonHigh.setChecked(true);
        else
            mRBButtonLow.setChecked(true);
        if (mButtonPullupdn == 1)
            mSWButtonPullupdn.setChecked(true);
        else
            mSWButtonPullupdn.setChecked(false);
        mETButtonKeycode.setText(String.valueOf(mButtonKeycode));
    }

    private void getRotaryEncoderConfig() {
        mPortAGPIO = Integer.parseInt(mETPortAGPIO.getText().toString());

        mPortAActive = 0;
        if (mRBPortAHigh.isChecked()) {
            mPortAActive = 1;
        }

        mPortAPullupdn = 0;
        if (mSWPortAPullupdn.isChecked()) {
            mPortAPullupdn = 1;
        }

        mPortAKeycode = Integer.parseInt(mETPortAKeycode.getText().toString());

        mPortBGPIO = Integer.parseInt(mETPortBGPIO.getText().toString());

        mPortBActive = 0;
        if (mRBPortBHigh.isChecked()) {
            mPortBActive = 1;
        }

        mPortBPullupdn = 0;
        if (mSWPortBPullupdn.isChecked()) {
            mPortBPullupdn = 1;
        }

        mPortBKeycode = Integer.parseInt(mETPortBKeycode.getText().toString());

        mButtonGPIO = Integer.parseInt(mETButtonGPIO.getText().toString());

        mButtonActive = 0;
        if (mRBButtonHigh.isChecked()) {
            mButtonActive = 1;
        }

        mButtonPullupdn = 0;
        if (mSWButtonPullupdn.isChecked()) {
            mButtonPullupdn = 1;
        }

        mButtonKeycode = Integer.parseInt(mETButtonKeycode.getText().toString());
    }

    private void saveRotaryEncoderConfig() {
        SharedPreferences pref = getSharedPreferences("rotary_encoder", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("port_a_gpio", mPortAGPIO);
        editor.putInt("port_a_active", mPortAActive);
        editor.putInt("port_a_pullupdn", mPortAPullupdn);
        editor.putInt("port_a_keycode", mPortAKeycode);
        editor.putInt("port_b_gpio", mPortBGPIO);
        editor.putInt("port_b_active", mPortBActive);
        editor.putInt("port_b_pullupdn", mPortBPullupdn);
        editor.putInt("port_b_keycode", mPortBKeycode);
        editor.putInt("button_gpio", mButtonGPIO);
        editor.putInt("button_active", mButtonActive);
        editor.putInt("button_pullupdn", mButtonPullupdn);
        editor.putInt("button_keycode", mButtonKeycode);
        editor.commit();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initControl();
        }
    };

    static {
        System.loadLibrary("rotary_encoder");
    }

    public static native int setRotaryEncoder(int port_a_gpio, int port_a_active, int port_a_pullupdn, int port_a_keycode,
                            int port_b_gpio, int port_b_active, int port_b_pullupdn, int port_b_keycode,
                            int button_gpio, int button_active, int button_pullupdn, int button_keycode);
}
