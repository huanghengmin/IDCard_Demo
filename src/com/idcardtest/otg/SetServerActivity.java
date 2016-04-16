/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.idcardtest.otg;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * This Activity appears as a dialog. It lists any paired devices and
 * devices detected in the area after discovery. When a device is chosen
 * by the user, the MAC address of the device is sent back to the parent
 * Activity in the result Intent.
 */
public class SetServerActivity extends Activity {
    // Debugging

    private IpEditer ip;
    private EditText port;
    private Button button_ok;
    private String remoteIP;
    private int remotePort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the window
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.layout_ip_admin);
        ip = (IpEditer) findViewById(R.id.ip_device);
        port = (EditText) findViewById(R.id.port_device);
        //读取配置
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        remoteIP = mSharedPrefs.getString("remote_ip", "222.46.20.174");
        remotePort = mSharedPrefs.getInt("remote_port",9018);
        ip.setText(remoteIP);
        port.setText(String.valueOf(remotePort));
        button_ok = (Button) findViewById(R.id.button_ok);
        button_ok.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                remoteIP = ip.getText().toString();
                String r_port = port.getText().toString();
                //重新配置
                SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = mSharedPrefs.edit();
                editor.putString("remote_ip", remoteIP);
                editor.putInt("remote_port", Integer.parseInt(r_port));
                editor.commit();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
