package com.kmodule;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kade.module.KModuleSDK;
import com.kade.module.base.HardwareConfig;
import com.kade.module.base.error.SerialPortOpenFailedException;
import com.kade.module.base.listener.KModuleManagerListener;
import com.kmodule.demo.R;

public class KModuleActivity extends AppCompatActivity {
    private static final String TAG = "KModuleActivity";
    private ScrollView sv_console;
    private LinearLayout ll_console;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_k_module);
        sv_console = findViewById(R.id.sv_console);
        ll_console = findViewById(R.id.ll_console);
        initSdk();
    }

    private void initSdk() {
        KModuleManagerListener kModuleManagerListener = new KModuleManagerListener() {
            @Override
            public void onSerialDataRead(String sPort, byte[] buffer, int size) {
                Log.i(TAG, "onSerialDataRead: " + new String(buffer, 0, size));
                addConsoleLog(new String(buffer, 0, size));
            }
        };
        HardwareConfig config = new HardwareConfig.Builder("/dev/ttyS3", 9600)
                .readTimeoutMs(5000)
                .isDebug(true)
                .kModuleManagerListener(kModuleManagerListener)
                .build();
        try {
            KModuleSDK.init(config);
        } catch (SerialPortOpenFailedException e) {
            Log.e(TAG, "init fai;ed：" + e.getMessage());
        }
    }

    private void addConsoleLog(String s) {
        runOnUiThread(() -> {
            String time = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()).toString();
            TextView textView = new TextView(this);
            textView.setText(time + "          " + s);
            textView.setTextColor(Color.GREEN);
            ll_console.addView(textView);
            sv_console.fullScroll(ScrollView.FOCUS_DOWN);
        });
    }

    public void onClickLEDRedOn(View view) {
        KModuleSDK.getInstance().ledRedOn();
    }

    public void onClickLEDGreenOn(View view) {
        KModuleSDK.getInstance().ledGreenOn();
    }



    public void onClickRelayOn(View view) {
        KModuleSDK.getInstance().relayOn();
    }

    public void onClickRelayOff(View view) {
        KModuleSDK.getInstance().relayOff();
    }


    public void onClickBeepOn(View view) {
        KModuleSDK.getInstance().beepOn();
    }
    public void onClickBeepOff(View view) {
        KModuleSDK.getInstance().beepOff();
    }

    public void onClickSetOpenTime(View view) {
        KModuleSDK.getInstance().setOpenTime(10);
    }

    public void onClickRemoteOpen(View view) {
        KModuleSDK.getInstance().remoteOpen();
    }

    public void onClickSendAdminCard(View view) {
        KModuleSDK.getInstance().sendSuperAdminCard();
    }

    public void onClickChangeWiegandOutput(View view) {
        KModuleSDK.getInstance().virtualWeighing();
    }


    public void onClickCardNumberOutputDec(View view) {
        KModuleSDK.getInstance().cardNumberOutputToggleEec();
    }

    public void onClickCardNumberOutputHex(View view) {
        KModuleSDK.getInstance().cardNumberOutputToggleHex();
    }

    public void onClickCardNumberOutputDecReverse(View view) {
        KModuleSDK.getInstance().cardNumberOutputToggleDecReverse();
    }

    public void onClickLEDBlueOn(View view) {
        KModuleSDK.getInstance().ledBlueOn();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (KModuleSDK.getInstance() != null) {
            KModuleSDK.getInstance().destroy();
        }
    }
}
