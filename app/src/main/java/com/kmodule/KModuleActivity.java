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
import com.kade.module.util.MyFunc;
import com.kmodule.demo.R;
import com.yishengkj.testtools.utils.ComBean;

import java.util.Locale;

import top.defaults.colorpicker.ColorPickerPopup;

public class KModuleActivity extends AppCompatActivity {
    private static final String TAG = "KModuleActivity";
    private ScrollView sv_console;
    private LinearLayout ll_console;

    private String sPort = "/dev/ttyS3";

    private TextView tv_port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_k_module);
        sv_console = findViewById(R.id.sv_console);
        ll_console = findViewById(R.id.ll_console);
        tv_port = findViewById(R.id.tv_port);
        tv_port.setText(sPort);
    }

    private void initSdk() {
        KModuleManagerListener kModuleManagerListener = new KModuleManagerListener() {
            @Override
            public void onSerialDataWrite(String sPort, ComBean comBean) {
                Log.i(TAG, "onSerialDataSend:" + MyFunc.ByteArrToHex(comBean.bRec));
                addConsoleLog("Send: " + MyFunc.ByteArrToHex(comBean.bRec));
            }

            @Override
            public void onSerialDataRead(String sPort, ComBean comBean) {
                Log.i(TAG, "onSerialDataRead:" + MyFunc.ByteArrToHex(comBean.bRec));
                addConsoleLog("Receive: " + MyFunc.ByteArrToHex(comBean.bRec));
            }
        };
        HardwareConfig config = new HardwareConfig.Builder(sPort, 9600)
                .readTimeoutMs(5000)
                .isDebug(true)
                .kModuleManagerListener(kModuleManagerListener)
                .build();
        try {
            KModuleSDK.init(config);
            addConsoleLog("init success");
        } catch (SerialPortOpenFailedException e) {
            Log.e(TAG, "init failed：" + e.getMessage());
            addConsoleLog("init failed：" + e.getMessage());
        }
    }

    private void addConsoleLog(String s) {
        runOnUiThread(() -> {
            String time = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()).toString();
            TextView textView = new TextView(this);
            textView.setText(time + "  " + s);
            textView.setTextColor(Color.GREEN);
            ll_console.addView(textView);
            sv_console.fullScroll(ScrollView.FOCUS_DOWN);
        });
    }

    public void onClickSelectPort(View view) {
        //弹出选择串口对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Port");
        final String[] ports = {"/dev/ttyS1", "/dev/ttyS2", "/dev/ttyS3", "/dev/ttyS4", "/dev/ttyS5", "/dev/ttyS6", "/dev/ttyS7"};
        builder.setItems(ports, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sPort = ports[which];
                tv_port.setText(sPort);
                addConsoleLog("Selected Port：" + sPort);
            }
        });
        builder.show();
    }

    public void onClickInitSDK(View view) {
        initSdk();
    }

    //Set the red LED to steady on.
    //LED 灯红色常亮
    public void onClickLEDRedOn(View view) {
        try {
            KModuleSDK.getInstance().ledRedOn();
        } catch (Exception e) {
            addConsoleLog(e.getMessage());
        }

    }

    //Set the green LED to steady on.
    //LED 灯绿色常亮
    public void onClickLEDGreenOn(View view) {
        try {
            KModuleSDK.getInstance().ledGreenOn();
        } catch (Exception e) {
            addConsoleLog(e.getMessage());
        }

    }

    //Set the blue LED to steady on.
    //LED 灯蓝色常亮
    public void onClickLEDBlueOn(View view) {
        try {
            KModuleSDK.getInstance().ledBlueOn();
        } catch (Exception e) {
            addConsoleLog(e.getMessage());
        }
    }

    //Turn on the relay.
    //继电器打开
    public void onClickRelayOn(View view) {
        try {
            KModuleSDK.getInstance().relayOn();
        } catch (Exception e) {
            addConsoleLog(e.getMessage());
        }
    }

    //Turn off the relay.
    //继电器关闭
    public void onClickRelayOff(View view) {
        try {
            KModuleSDK.getInstance().relayOff();
        } catch (Exception e) {
            addConsoleLog(e.getMessage());
        }

    }


    //Set the red LED to blink.
    //设置红色LED闪烁
    public void onClickLEDRedBlink(View view) {
        try {
            KModuleSDK.getInstance().ledRedBlink();
        } catch (Exception e) {
            addConsoleLog(e.getMessage());
        }
    }

    //Set the green LED to blink.
    //设置绿色LED闪烁
    public void onClickLEDGreenBlink(View view) {
        try {
            KModuleSDK.getInstance().ledGreenBlink();
        } catch (Exception e) {
            addConsoleLog(e.getMessage());
        }
    }

    //Set the blue LED to blink.
    //设置蓝色LED闪烁
    public void onClickLEDBlueBlink(View view) {
        try {
            KModuleSDK.getInstance().ledBlueBlink();
        } catch (Exception e) {
            addConsoleLog(e.getMessage());
        }
    }

    //Turn off all LEDs.
    //关闭所有LED灯
    public void onClickLEDOff(View view) {
        try {
            KModuleSDK.getInstance().ledOff();
        } catch (Exception e) {
            addConsoleLog(e.getMessage());
        }

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
                        String hex = colorHex(color);
                        String colorHEx = hex.substring(4, hex.length());

                        try {

                        } catch (Exception e) {
                            Log.e(TAG, "onClickLEDRedOn: " + e.getMessage());
                            addConsoleLog("onClickLEDRedOn: " + e.getMessage());
                        }
                        try {
                            KModuleSDK.getInstance().ledCustomColorAndOn(colorHEx);
                        } catch (Exception e) {
                            addConsoleLog(e.getMessage());
                        }
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
                        String hex = colorHex(color);
                        String colorHEx = hex.substring(4, hex.length());

                        try {

                        } catch (Exception e) {
                            Log.e(TAG, "onClickLEDRedOn: " + e.getMessage());
                            addConsoleLog("onClickLEDRedOn: " + e.getMessage());
                        }
                        try {
                            KModuleSDK.getInstance().ledCustomColorBlink(colorHEx);
                        } catch (Exception e) {
                            addConsoleLog(e.getMessage());
                        }
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
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // 对话框确认按钮
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int currentBrightness = seekBar.getProgress() + minBrightness;
                try {
                    KModuleSDK.getInstance().ledBrightness(currentBrightness);
                } catch (Exception e) {
                    addConsoleLog(e.getMessage());
                }

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
        try {
            KModuleSDK.getInstance().ledFullBrightness();
        } catch (Exception e) {
            addConsoleLog(e.getMessage());
        }

    }

    //Turn on the beeper.
    //蜂鸣器开启
    public void onClickBeepOn(View view) {
        try {
            KModuleSDK.getInstance().beepOn();
        } catch (Exception e) {
            addConsoleLog(e.getMessage());
        }

    }

    //Turn off the beeper.
    //蜂鸣器关闭
    public void onClickBeepOff(View view) {
        try {
            KModuleSDK.getInstance().beepOff();
        } catch (Exception e) {
            addConsoleLog(e.getMessage());
        }

    }

    //Set LED marquee effect.
    //设置LED流水灯效果
    public void onClickLEDMarquee(View view) {
        try {
            KModuleSDK.getInstance().ledMarquee();
        } catch (Exception e) {
            addConsoleLog(e.getMessage());
        }
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
                    try {
                        KModuleSDK.getInstance().setOpenTime(time);
                    } catch (Exception e) {
                        addConsoleLog(e.getMessage());
                    }

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
        try {
            KModuleSDK.getInstance().remoteOpen();
        } catch (Exception e) {
            addConsoleLog(e.getMessage());
        }

    }

    //Send super admin card command.
    //发送超级管理员卡命令
    public void onClickSendAdminCard(View view) {
        try {
            KModuleSDK.getInstance().sendSuperAdminCard("");
        } catch (Exception e) {
            addConsoleLog(e.getMessage());
        }

    }

    //Change Wiegand output format.
    //更改韦根输出格式
    public void onClickChangeWiegandOutput(View view) {
        try {
            KModuleSDK.getInstance().virtualWeighing();
        } catch (Exception e) {
            addConsoleLog(e.getMessage());
        }
    }

    //Set card number output to decimal format.
    //设置卡号输出为十进制格式
    public void onClickCardNumberOutputDec(View view) {
        try {
            KModuleSDK.getInstance().cardNumberOutputToggleEec();
        } catch (Exception e) {
            addConsoleLog(e.getMessage());
        }

    }

    //Set card number output to hexadecimal format.
    //设置卡号输出为十六进制格式
    public void onClickCardNumberOutputHex(View view) {
        try {
            KModuleSDK.getInstance().cardNumberOutputToggleHex();
        } catch (Exception e) {
            addConsoleLog(e.getMessage());
        }
    }

    //Set card number output to reverse decimal format.
    //设置卡号输出为反向十进制格式
    public void onClickCardNumberOutputDecReverse(View view) {
        try {
            KModuleSDK.getInstance().cardNumberOutputToggleDecReverse();
        } catch (Exception e) {
            addConsoleLog(e.getMessage());
        }
    }

    //Send serial port data.
    //发送串口数据
    public void onClickSendSerialPort(View view) {
        try {
            KModuleSDK.getInstance().sendSerialData(new byte[]{0x01});
        } catch (Exception e) {
            addConsoleLog(e.getMessage());
        }
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