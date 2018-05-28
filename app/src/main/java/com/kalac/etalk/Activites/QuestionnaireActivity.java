package com.kalac.etalk.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kalac.etalk.R;
import com.kalac.etalk.Utils.ConstantValue;
import com.kalac.etalk.Utils.SharePreferenceUtil;
import com.kalac.etalk.Utils.UIUtil;

public class QuestionnaireActivity extends BaseActivity implements View.OnClickListener {

    private Button btnDone;
    private Button btnExit;
    private Button btnJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        initView();
    }

    private void initView() {
        View statusBar = findViewById(R.id.statusBarView);
        //根据状态栏高度设置占位控件的高度
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = getStatusBarHeight();
        btnDone = findViewById(R.id.btn_questionnaire_done);
        btnExit = findViewById(R.id.btn_questionnaire_exit);
        btnJump = findViewById(R.id.btn_questionnaire_jump);
        btnDone.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnJump.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_questionnaire_done:
                SharePreferenceUtil.putInt(UIUtil.getContext(), ConstantValue.QUESTIONNAIRE_STATUS, ConstantValue.QUESTIONNAIRE_DONE);
                startActivity(new Intent(UIUtil.getContext(),MainActivity.class));
                finish();
                break;
            case R.id.btn_questionnaire_exit:
                SharePreferenceUtil.putInt(UIUtil.getContext(), ConstantValue.QUESTIONNAIRE_STATUS, ConstantValue.QUESTIONNAIRE_UNDONE);
                finish();
                break;
            case R.id.btn_questionnaire_jump:
                SharePreferenceUtil.putInt(UIUtil.getContext(), ConstantValue.QUESTIONNAIRE_STATUS, ConstantValue.QUESTIONNAIRE_UNDONE);
                startActivity(new Intent(UIUtil.getContext(),MainActivity.class));
                finish();
                break;
            default:
                break;
        }
    }
}
