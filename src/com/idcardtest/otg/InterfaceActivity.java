package com.idcardtest.otg;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.*;
import yishu.nfc.YSnfcCardReader.NFCardReader;
import yishu.nfc.YSnfcCardReader.IdentityCard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Method;

public class InterfaceActivity extends Activity {
    private static final int SETTING_SERVER_IP = 1;
    private TextView name;
    private TextView nametext;
    private TextView sex;
    private TextView sextext;
    private TextView mingzu;
    private TextView mingzutext;
    private TextView birthday;
    private TextView birthdaytext;
    private TextView address;
    private TextView addresstext;
    private TextView number;
    private TextView numbertext;
    private TextView qianfa;
    private TextView qianfatext;
    private TextView start;
    private TextView starttext;
    private TextView end;
    private TextView endtext;
    private TextView dncodetext;
    private TextView dncode;
    private TextView Readingtext;
    private ImageView idimg;
    private static Button onredo;
    private NFCardReader nfcReadCardAPI;
    private String remoteIP;
    private int remotePort;
    private int readflag=0;

    //滤掉组件无法响应和处理的Intent
    private Intent inintent = null;

    public static final int MESSAGE_VALID_NFCBUTTON = 16;
    /**
     * 服务器无法连接
     */
    public final static int SERVER_CANNOT_CONNECT = 90000001;
    /**
     * 开始读卡
     */
    public final static int READ_CARD_START = 10000001;
    /**
     * 读卡进度
     */
    public final static int READ_CARD_PROGRESS = 20000002;
    /**
     * 读卡成功
     */
    public final static int READ_CARD_SUCCESS = 30000003;
    /**
     * 读照片成功
     */
    public final static int READ_PHOTO_SUCESS = 40000004;
    /**
     * 读卡失败
     */
    public final static int READ_CARD_FAILED = 90000009;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        onredo = (Button) findViewById(R.id.scale);
        onredo.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                }
                return true;
            }
        });

        name = (TextView) findViewById(R.id.name);
        sex = (TextView) findViewById(R.id.sex);
        nametext = (TextView) findViewById(R.id.nametext);
        sextext = (TextView) findViewById(R.id.sextext);
        mingzu = (TextView) findViewById(R.id.mingzu);
        mingzutext = (TextView) findViewById(R.id.mingzutext);
        birthday = (TextView) findViewById(R.id.birthday);
        birthdaytext = (TextView) findViewById(R.id.birthdaytext);
        address = (TextView) findViewById(R.id.address);
        addresstext = (TextView) findViewById(R.id.addresstext);
        number = (TextView) findViewById(R.id.number);
        numbertext = (TextView) findViewById(R.id.numbertext);
        qianfa = (TextView) findViewById(R.id.qianfa);
        qianfatext = (TextView) findViewById(R.id.qianfatext);
        start = (TextView) findViewById(R.id.start);
        starttext = (TextView) findViewById(R.id.starttext);
        end = (TextView) findViewById(R.id.end);
        endtext = (TextView) findViewById(R.id.endtext);
        Readingtext = (TextView) findViewById(R.id.Readingtext);
        dncodetext = (TextView) findViewById(R.id.dncodetext);
        dncode = (TextView) findViewById(R.id.dncode);
        name.setText("姓名：");
        sex.setText("性别：");
        mingzu.setText("民族：");
        birthday.setText("出生年月：");
        address.setText("地址：");
        number.setText("身份证号码：");
        qianfa.setText("签发机关：");
        start.setText("有效时间：");
        end.setText("生效时间：");
        dncode.setVisibility(View.GONE);
        end.setVisibility(View.GONE);
        idimg = (ImageView) findViewById(R.id.idimg);
        Readingtext.setVisibility(View.GONE);
        Readingtext.setText("      正在读卡，请稍候...");
        Readingtext.setTextColor(Color.RED);
        //读取配置
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        remoteIP = mSharedPrefs.getString("remote_ip", "222.46.20.174");
        remotePort = mSharedPrefs.getInt("remote_port",9018);
        nfcReadCardAPI = new NFCardReader(mHandler, this);
        nfcReadCardAPI.setTheServer(remoteIP, remotePort);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        nfcReadCardAPI.writeFile("test 101");
        nfcReadCardAPI.writeFile("test 102");
        if (readflag==1)
        {
//			return;
        }
        nfcReadCardAPI.writeFile("test 103");
        inintent = intent;
        readflag=1;
        nametext.setText("");
        sextext.setText("");
        mingzutext.setText("");
        birthdaytext.setText("");
        addresstext.setText("");
        numbertext.setText("");
        qianfatext.setText("");
        starttext.setText("");
        endtext.setText("");
        dncodetext.setText("");

        idimg.setImageBitmap(null);
        Readingtext.setText("      正在读卡，请稍候...");
        Readingtext.setVisibility(View.VISIBLE);
        nfcReadCardAPI.writeFile("test 104");
        mHandler.sendEmptyMessageDelayed(MESSAGE_VALID_NFCBUTTON, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Boolean enablenfc = nfcReadCardAPI.EnableSystemNFCMessage();
        if (enablenfc == true) {

        } else {
            new AlertDialog.Builder(InterfaceActivity.this)
                    .setTitle("提示").setMessage("NFC初始化失败！")
                    .setPositiveButton("确定", null).show();
            Readingtext.setVisibility(View.GONE);
            onredo.setEnabled(true);
            onredo.setFocusable(true);
            onredo.setBackgroundResource(R.drawable.sfz_dq);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.setip:
			Intent serverIntent = new Intent(this, SetServerActivity.class);
			startActivityForResult(serverIntent, SETTING_SERVER_IP);
			return true;
		}
        return false;
    }

    /**
     * 设置menu显示icon
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {

        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return super.onMenuOpened(featureId, menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTING_SERVER_IP) {
            //读取配置
            SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            remoteIP = mSharedPrefs.getString("remote_ip", "222.46.20.174");
            remotePort = mSharedPrefs.getInt("remote_port",9018);
            nfcReadCardAPI.setTheServer(remoteIP, remotePort);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SERVER_CANNOT_CONNECT:
                    Readingtext.setText("      没有连接到服务器...");
                    onredo.setEnabled(true);
                    onredo.setFocusable(true);
                    onredo.setBackgroundResource(R.drawable.sfz_dq);
                    readflag=0;
                    break;
                case READ_CARD_START:
                    Readingtext.setVisibility(View.VISIBLE);
                    break;
                case READ_CARD_PROGRESS:
                    Readingtext.setText("      正在读卡，请稍候...");
                    break;
                case READ_CARD_SUCCESS:
                    IdentityCard identityCard = (IdentityCard) msg.obj;
                    nametext.setText(identityCard.name);
                    sextext.setText(identityCard.sex);
                    mingzutext.setText(identityCard.ethnicity);
                    birthdaytext.setText(identityCard.birth);
                    addresstext.setText(identityCard.address);
                    numbertext.setText(identityCard.cardNo);
                    qianfatext.setText(identityCard.authority);
                    starttext.setText(identityCard.period);
                    onredo.setEnabled(true);
                    onredo.setFocusable(true);
                    onredo.setBackgroundResource(R.drawable.sfz_dq);
                    readflag=0;
                    Readingtext.setVisibility(View.GONE);
                    break;
                case READ_PHOTO_SUCESS:
                    byte[] cardbmp = (byte[]) msg.obj;
                    Bitmap bm = BitmapFactory.decodeByteArray(cardbmp, 0, cardbmp.length);
                    idimg.setImageBitmap(bm);
                    readflag=0;
                    break;
                case READ_CARD_FAILED:
//                    int error = msg.arg1;
//                    Readingtext.setText("读卡失败！"+error);
                    Readingtext.setText("      读卡失败！");
                    onredo.setEnabled(true);
                    onredo.setFocusable(true);
                    onredo.setBackgroundResource(R.drawable.sfz_dq);
                    readflag=0;
                    break;
                case MESSAGE_VALID_NFCBUTTON:
                    nfcReadCardAPI.setTheServer(remoteIP, remotePort);
                    Boolean enablenfc = nfcReadCardAPI.EnableSystemNFCMessage();
                    if (enablenfc == true) {
                        Boolean judgenfc = nfcReadCardAPI.isNFC(inintent);
                        if (judgenfc == true) {
                            nfcReadCardAPI.readCardWithIntent(inintent);
                        } else {
                            Readingtext.setText("      读卡失败！");
                            onredo.setEnabled(true);
                            onredo.setFocusable(true);
                            onredo.setBackgroundResource(R.drawable.sfz_dq);
                            readflag=0;
                        }
                    } else {
                        Readingtext.setText("      读卡失败！");
                        onredo.setEnabled(true);
                        onredo.setFocusable(true);
                        onredo.setBackgroundResource(R.drawable.sfz_dq);
                        readflag=0;
                    }
                    break;
            }
        }
    };

}