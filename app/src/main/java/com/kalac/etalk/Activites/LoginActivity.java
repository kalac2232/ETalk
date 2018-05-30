package com.kalac.etalk.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kalac.etalk.R;
import com.kalac.etalk.Utils.ConstantValue;
import com.kalac.etalk.Utils.SharePreferenceUtil;
import com.kalac.etalk.Utils.UIUtil;

/**
 * 登陆界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {


    private EditText etPhone;
    private EditText etIdentifyCode;
    private Button btnLogin;
    private Button btnSendCode;
    private ImageView ivQQLogin;
    private ImageView ivWechatLogin;
    /**
     * 发送验证码的间隔时间
     */
    private static final int countDownNumber = 10;
    /**
     * 当前剩余不可发送验证码时间
     */
    private int remainderTime = 0;
    private String[] phoneSections = {"134","135","136","137","138","139","150","151","152","157","158","159","182","183","184","187","178","188","147","170",
            "130","131","132","145","155","156","175","176","185","186","133","153","177","180","181","189"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

    }

    private void initView() {
        View statusBar = findViewById(R.id.statusBarView);
        //根据状态栏高度设置占位控件的高度
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = getStatusBarHeight();
        //找到控件
        etPhone = findViewById(R.id.et_login_phone);
        etIdentifyCode = findViewById(R.id.et_login_code);
        btnLogin = findViewById(R.id.btn_login);
        btnSendCode = findViewById(R.id.btn_login_sendcode);
        ivQQLogin = findViewById(R.id.iv_login_qq);
        ivWechatLogin = findViewById(R.id.iv_login_wechat);
        //添加点击事件监听
        btnLogin.setOnClickListener(this);
        btnSendCode.setOnClickListener(this);
        ivQQLogin.setOnClickListener(this);
        ivWechatLogin.setOnClickListener(this);
        //添加文本事件监听
        etIdentifyCode.addTextChangedListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_login:
                //按下登陆键的操作
                login();
                break;
            case R.id.btn_login_sendcode:
                //发送验证码
                sendIdentifyCode();
                break;
            case R.id.iv_login_qq:
                //qq登陆
                break;
            case R.id.iv_login_wechat:
                //微信登陆
                break;
            default:
                break;
        }
    }

    private void sendIdentifyCode() {
        boolean checkResult = checkPhoneNumber(etPhone.getText().toString());
        if (checkResult) {
            //对发送按钮的状态进行更改
            new Thread() {
                @Override
                public void run() {
                    for (remainderTime = countDownNumber; remainderTime> 0;remainderTime--) {
                        UIUtil.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                String tip = "重新发送("+remainderTime+")";
                                btnSendCode.setText(tip);
                                //设置按钮不可点击
                                btnSendCode.setEnabled(false);
                            }
                        });
                        Log.i(TAG, "run: i: "+remainderTime);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    UIUtil.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            //恢复按钮状态
                            btnSendCode.setText(UIUtil.getString(R.string.send_identifycode));
                            btnSendCode.setEnabled(true);
                        }
                    });

                }
            }.start();
        }

    }

    /**
     * 检查手机号码的正确性
     * @param phone 要检查的手机号
     * @return 是否正确
     */
    private boolean checkPhoneNumber(String phone) {

        if (phone.isEmpty() || phone.length() != 11) {
            Toast.makeText(UIUtil.getContext(),"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
            return false;
        }
        //截取字符串前三位与号段库进行比较
        String substring = phone.substring(0, 3);
        for (String phoneSection: phoneSections) {
            if (substring.equals(phoneSection)) {
                return true;
            }
        }
        Toast.makeText(UIUtil.getContext(),"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
        return false;
    }

    private static final String TAG = "LoginActivity";
    private void login() {
        boolean checkResult = checkPhoneNumber(etPhone.getText().toString());
        if (checkResult) {
            if (etIdentifyCode.getText().length() == 6) {
                //登陆操作
                SharePreferenceUtil.putBoolean(UIUtil.getContext(),ConstantValue.ISLOGIN,true);
                //获取调查问卷的完成情况
                int questionnaireStatus = SharePreferenceUtil.getInt(UIUtil.getContext(), ConstantValue.QUESTIONNAIRE_STATUS, ConstantValue.QUESTIONNAIRE_UNDONE);
                if (questionnaireStatus != ConstantValue.QUESTIONNAIRE_DONE) {
                    //如果不等与完成则跳问卷调查页
                    Intent intent = new Intent(UIUtil.getContext(), QuestionnaireActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //如果完成了问卷调查 直接跳主页
                    Intent intent = new Intent(UIUtil.getContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            } else {
                Toast.makeText(UIUtil.getContext(),"请输入验证码",Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        //当输入6位验证码后自动进行登陆验证
        if (s.length() == 6) {
            login();
        }
    }
}

