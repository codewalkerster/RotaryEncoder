package com.hardkernel.odroid.rotaryencoder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by codewalker on 17. 3. 31.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            SharedPreferences pref = context.getSharedPreferences("rotary_encoder", Context.MODE_PRIVATE);

            int portAGPIO = pref.getInt("port_a_gpio", 0);
            int portAActive = pref.getInt("port_a_active", 0);
            int portAPullupdn = pref.getInt("port_a_pullupdn", 0);
            int portAKeycode = pref.getInt("port_a_keycode", 0);
            int portBGPIO = pref.getInt("port_b_gpio", 0);
            int portBActive = pref.getInt("port_b_active", 0);
            int portBPullupdn = pref.getInt("port_b_pullupdn", 0);
            int portBKeycode = pref.getInt("port_b_keycode", 0);
            int buttonGPIO = pref.getInt("button_gpio", 0);
            int buttonActive = pref.getInt("button_active", 0);
            int buttonPullupdn = pref.getInt("button_pullupdn", 0);
            int buttonKeycode = pref.getInt("button_keycode", 0);
            int temp = portAGPIO + portAActive + portAPullupdn + portAKeycode
                    + portBGPIO + portBActive + portBPullupdn + portBKeycode
                    + buttonGPIO + buttonActive + buttonPullupdn + buttonKeycode;

            if (temp > 0)
                MainActivity.setRotaryEncoder(portAGPIO, portAActive, portAPullupdn, portAKeycode,
                        portBGPIO, portBActive, portBPullupdn, portBKeycode,
                        buttonGPIO, buttonActive, buttonPullupdn, buttonKeycode);
        }
    }
}