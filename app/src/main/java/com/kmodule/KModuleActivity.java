package com.kmodule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kade.module.KModuleSDK;
import com.kade.module.base.HardwareConfig;
import com.kade.module.base.error.SerialPortOpenFailedException;
import com.kade.module.base.listener.KModuleManagerListener;
import com.kmodule.demo.R;

import java.util.Locale;

import top.defaults.colorpicker.ColorPickerPopup;

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
            addConsoleLog("init fai;ed：" + e.getMessage());
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

    //Set the red LED to steady on.
    //LED 灯红色常亮
    public void onClickLEDRedOn(View view) {
        KModuleSDK.getInstance().ledRedOn();
    }

    //Set the green LED to steady on.
    //LED 灯绿色常亮
    public void onClickLEDGreenOn(View view) {
        KModuleSDK.getInstance().ledGreenOn();
    }

    //Set the blue LED to steady on.
    //LED 灯蓝色常亮
    public void onClickLEDBlueOn(View view) {
        KModuleSDK.getInstance().ledBlueOn();
    }

    //Turn on the relay.
    //继电器打开
    public void onClickRelayOn(View view) {
        KModuleSDK.getInstance().relayOn();
    }

    //Turn off the relay.
    //继电器关闭
    public void onClickRelayOff(View view) {
        KModuleSDK.getInstance().relayOff();
    }


    //Set the red LED to blink.
    //设置红色LED闪烁
    public void onClickLEDRedBlink(View view) {
        KModuleSDK.getInstance().ledRedBlink();
    }

    //Set the green LED to blink.
    //设置绿色LED闪烁
    public void onClickLEDGreenBlink(View view) {
        KModuleSDK.getInstance().ledGreenBlink();
    }

    //Set the blue LED to blink.
    //设置蓝色LED闪烁
    public void onClickLEDBlueBlink(View view) {
        KModuleSDK.getInstance().ledBlueBlink();
    }

    //Turn off all LEDs.
    //关闭所有LED灯
    public void onClickLEDOff(View view) {
        KModuleSDK.getInstance().ledOff();
    }

    //Set custom color for LED to steady on.
    //设置LED自定义颜色常亮
    public void onClickLEDSetColorOn(View view) {
        new ColorPickerPopup.Builder(view.getContext())
                .initialColor(Color.RED) // Set initial color
                .enableAlpha(true) // Enable alpha slider or not
                .okTitle("Ok")
                .cancelTitle("Cancel")
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(view, new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(int color) {
                        String hex= colorHex(color);
                        String colorHEx = hex.substring(4,hex.length());
                        KModuleSDK.getInstance().ledCustomColorAndOn(colorHEx);
                        view.setBackgroundColor(color);
                    }
                });
    }

    //Set custom color for LED to blink.
    //设置LED自定义颜色闪烁
    public void onClickLEDSetColorBlink(View view) {
        new ColorPickerPopup.Builder(view.getContext())
                .initialColor(Color.RED) // Set initial color
                .enableAlpha(true) // Enable alpha slider or not
                .okTitle("Ok")
                .cancelTitle("Cancel")
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(view, new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(int color) {
                        String hex= colorHex(color);
                        String colorHEx = hex.substring(4,hex.length());
                        KModuleSDK.getInstance().ledCustomColorBlink(colorHEx);
                        view.setBackgroundColor(color);
                    }
                });
    }

    //Set LED brightness.
    //设置LED亮度
    public void onClickLEDSetBrightness(View view) {
        // LED 亮度控制：弹出 SeekBar 对话框供用户调节亮度
        int defaultBrightness = 80; // 默认亮度值
        int minBrightness = 1;     // 最小亮度值
        int maxBrightness = 99;    // 最大亮度值
        // 构建 SeekBar 对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Set LED Brightness");
        // 创建 SeekBar 控件
        SeekBar seekBar = new SeekBar(view.getContext());
        seekBar.setMax(maxBrightness - minBrightness);
        seekBar.setProgress(defaultBrightness - minBrightness); // 适配进度条起始值

        // 设置进度条刻度显示（可选）
        TextView progressTv = new TextView(view.getContext());
        progressTv.setText("value：" + defaultBrightness);
        progressTv.setPadding(50, 20, 50, 20);
        progressTv.setGravity(Gravity.CENTER);

        // 线性布局承载 TextView 和 SeekBar
        LinearLayout layout = new LinearLayout(view.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(progressTv);
        layout.addView(seekBar);
        builder.setView(layout);

        // SeekBar 进度变化监听
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int currentBrightness = progress + minBrightness;
                progressTv.setText("value：" + currentBrightness);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // 对话框确认按钮
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int currentBrightness = seekBar.getProgress() + minBrightness;
                KModuleSDK.getInstance().ledBrightness(currentBrightness);
                dialog.dismiss();
            }
        });

        // 对话框取消按钮
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // 显示对话框
        builder.show();
    }

    //Set LED to full brightness.
    //设置LED最大亮度
    public void onClickLEDSetFullBrightness(View view) {
        KModuleSDK.getInstance().ledFullBrightness();
    }

    //Turn on the beeper.
    //蜂鸣器开启
    public void onClickBeepOn(View view) {
        KModuleSDK.getInstance().beepOn();
    }

    //Turn off the beeper.
    //蜂鸣器关闭
    public void onClickBeepOff(View view) {
        KModuleSDK.getInstance().beepOff();
    }

    //Set LED marquee effect.
    //设置LED流水灯效果
    public void onClickLEDMarquee(View view) {
        KModuleSDK.getInstance().ledMarquee();
    }

    //Set Relay Open Time
    //设置继电器开启时间
    public void onClickSetOpenTime(View view) {
        //弹出dialog，输入时设置打开时间
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Set Open Time (s)");
        // 创建一个EditText用于输入时间
        final android.widget.EditText input = new android.widget.EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        input.setHint("input open time (s)");
        input.setText("10"); // default
        builder.setView(input);
        // 设置确认按钮
        builder.setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                String timeStr = input.getText().toString();
                try {
                    int time = Integer.parseInt(timeStr);
                    addConsoleLog("Set Open Time：" + time + "s");
                    KModuleSDK.getInstance().setOpenTime(time);
                } catch (NumberFormatException e) {
                    addConsoleLog("Invalid input, please enter a number");
                }
            }
        });
        // 设置取消按钮
        builder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    //Remote control to open.
    //远程控制打开
    public void onClickRemoteOpen(View view) {
        KModuleSDK.getInstance().remoteOpen();
    }

    //Send super admin card command.
    //发送超级管理员卡命令
    public void onClickSendAdminCard(View view) {
        KModuleSDK.getInstance().sendSuperAdminCard();
    }

    //Change Wiegand output format.
    //更改韦根输出格式
    public void onClickChangeWiegandOutput(View view) {
        KModuleSDK.getInstance().virtualWeighing();
    }

    //Set card number output to decimal format.
    //设置卡号输出为十进制格式
    public void onClickCardNumberOutputDec(View view) {
        KModuleSDK.getInstance().cardNumberOutputToggleEec();
    }

    //Set card number output to hexadecimal format.
    //设置卡号输出为十六进制格式
    public void onClickCardNumberOutputHex(View view) {
        KModuleSDK.getInstance().cardNumberOutputToggleHex();
    }

    //Set card number output to reverse decimal format.
    //设置卡号输出为反向十进制格式
    public void onClickCardNumberOutputDecReverse(View view) {
        KModuleSDK.getInstance().cardNumberOutputToggleDecReverse();
    }

    private String colorHex(int color) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "0x%02X%02X%02X%02X", a, r, g, b);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (KModuleSDK.getInstance() != null) {
            KModuleSDK.getInstance().destroy();
        }
    }
}