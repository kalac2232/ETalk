package com.kalac.etalk.Activites;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.kalac.etalk.R;
import com.kalac.etalk.Utils.ConstantValue;
import com.kalac.etalk.Utils.SharePreferenceUtil;
import com.kalac.etalk.Utils.UIUtil;

import java.io.InputStream;
import java.util.ArrayList;

public class QuestionnaireActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivRight;
    private ImageView ivWrong;
    private Button btnExit;
    private Button btnJump;
    /**
     * 学生行为题目
     */
    private ArrayList<String> studentList_behavior;
    /**
     * 学生心理题目
     */
    private ArrayList<String> studentList_psycho;
    /**
     * 成年人行为题目
     */
    private ArrayList<String> adultList_behavior;
    /**
     * 成年人心理题目
     */
    private ArrayList<String> adultList_psycho;
    private static final int STUDENT_QUESTION = 0;
    private static final int ADULT_QUESTION = 1;
    private static final int BEHAVIOR_QUESTION = 1;
    private static final int PSYCHO_QUESTION = 0;
    private LinearLayout llSelected_adult;
    private LinearLayout llSelected_student;
    private LinearLayout llSelectProfession;
    private LinearLayout llQuestion;
    private ImageView ivProfessionIcon;
    private TextView tvQuestion;
    private int current_questionindex = 0;
    enum Profession {
        student,adult
    }
    private Profession selected_Profreeion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        initView();
        initData();
    }

    private static final String TAG = "QuestionnaireActivity";
    private void initData() {
        studentList_behavior = new ArrayList<>();
        studentList_psycho = new ArrayList<>();
        adultList_behavior = new ArrayList<>();
        adultList_psycho = new ArrayList<>();
        //拿到本地JSON 并转成String
        InputStream is = getResources().openRawResource(R.raw.question);
        String jsonFile = readJsonFile(is);
        //Json的解析类对象
        JsonParser parser = new JsonParser();
        if (jsonFile != null) {
            //将JSON的String 转成一个JsonArray对象
            JsonArray jsonArray = parser.parse(jsonFile).getAsJsonArray();
            Gson gson = new Gson();
            ArrayList<QuestionBean> questionBeanList = new ArrayList<>();
            //加强for循环遍历JsonArray
            for (JsonElement user : jsonArray) {
                //使用GSON，直接转成Bean对象
                QuestionBean questionBean = gson.fromJson(user, QuestionBean.class);
                questionBeanList.add(questionBean);
            }
            //遍历数组 将问题分类
            for(QuestionBean questionBean : questionBeanList) {
                if (questionBean.career_type == STUDENT_QUESTION) {
                    if (questionBean.question_type == BEHAVIOR_QUESTION) {
                        studentList_behavior.add(questionBean.question);
                    } else {
                        studentList_psycho.add(questionBean.question);
                    }
                } else {
                    if (questionBean.question_type == BEHAVIOR_QUESTION) {
                        adultList_behavior.add(questionBean.question);
                    } else {
                        adultList_psycho.add(questionBean.question);
                    }
                }
            }

            Log.i(TAG, "initData: "+studentList_behavior.size());
            Log.i(TAG, "initData: "+studentList_psycho.size());
            Log.i(TAG, "initData: "+adultList_behavior.size());
            Log.i(TAG, "initData: "+adultList_psycho.size());
        }

    }
    private static String readJsonFile(InputStream is)  {
        try {
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            String data = new String(bytes);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private void initView() {
        View statusBar = findViewById(R.id.statusBarView);
        //根据状态栏高度设置占位控件的高度
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = getStatusBarHeight();
        llSelectProfession = findViewById(R.id.ll_selectprofession);
        llQuestion = findViewById(R.id.ll_question);
        llSelected_adult = findViewById(R.id.ll_imadult);
        llSelected_adult = findViewById(R.id.ll_imadult);
        llSelected_student = findViewById(R.id.ll_imstudent);
        tvQuestion = findViewById(R.id.tv_question);
        ivRight = findViewById(R.id.iv_questionnaire_right);
        ivWrong = findViewById(R.id.iv_questionnaire_wrong);
        btnExit = findViewById(R.id.btn_questionnaire_exit);
        btnJump = findViewById(R.id.btn_questionnaire_jump);
        //显示当前选择的职业图标
        ivProfessionIcon = findViewById(R.id.iv_current_professionicon);
        llSelected_adult.setOnClickListener(this);
        llSelected_student.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnJump.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.ll_imadult:
                //隐藏选择职业页，显示问题页
                llSelectProfession.setVisibility(View.GONE);
                llQuestion.setVisibility(View.VISIBLE);
                Drawable drawable1 = UIUtil.getDrawable(R.drawable.ic_questionaire_adult_transparent);
                ivProfessionIcon.setImageDrawable(drawable1);
                selected_Profreeion = Profession.adult;
                showQuestion();
                break;
            case R.id.ll_imstudent:
                llSelectProfession.setVisibility(View.GONE);
                llQuestion.setVisibility(View.VISIBLE);
                Drawable drawable2 = UIUtil.getDrawable(R.drawable.ic_questionaire_student_transparent);
                ivProfessionIcon.setImageDrawable(drawable2);
                selected_Profreeion = Profession.student;
                showQuestion();
                break;
            case R.id.iv_questionnaire_right:
                recordScoreAndNext(true);
                break;
            case R.id.iv_questionnaire_wrong:
                recordScoreAndNext(false);
                break;
            case R.id.btn_questionnaire_exit:
                SharePreferenceUtil.putInt(UIUtil.getContext(), ConstantValue.QUESTIONNAIRE_STATUS, ConstantValue.QUESTIONNAIRE_UNDONE);
                startActivity(new Intent(UIUtil.getContext(),MainActivity.class));
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

    private void showQuestion() {
        if (selected_Profreeion == Profession.student) {
            tvQuestion.setText(studentList_behavior.get(current_questionindex));
        } else {
            tvQuestion.setText(adultList_behavior.get(current_questionindex));
        }
    }

    /** 记录分数并下一道题
     * @param answer 回答问题的正确性
     */
    private void recordScoreAndNext(boolean answer) {
        int score = 0;
        //当前所做题目的索引+1
        current_questionindex++;
        if (selected_Profreeion == Profession.student) {
            if (current_questionindex < studentList_behavior.size()) {
                tvQuestion.setText(studentList_behavior.get(current_questionindex));
                score = answer? score+1 :score;
            } else if(current_questionindex < studentList_behavior.size() + studentList_psycho.size()-1){
                tvQuestion.setText(studentList_psycho.get(current_questionindex - studentList_behavior.size()));
                score = answer? score+1 :score;
            } else if (current_questionindex == studentList_behavior.size() + studentList_psycho.size() -1 ) {
                tvQuestion.setText(studentList_psycho.get(current_questionindex - studentList_behavior.size()));
                score = answer? score+1 :score;
                Toast.makeText(UIUtil.getContext(),"完成所有测试题，即将进入主界面",Toast.LENGTH_SHORT).show();
                UIUtil.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //到末尾进入主页面
                        startActivity(new Intent(UIUtil.getContext(), MainActivity.class));
                        finish();
                    }
                },1500);

            }
        } else {
            if (current_questionindex < adultList_behavior.size()) {
                tvQuestion.setText(adultList_behavior.get(current_questionindex));
                score = answer? score+1 :score;
            } else if(current_questionindex < adultList_behavior.size() + adultList_psycho.size()-1){
                tvQuestion.setText(adultList_psycho.get(current_questionindex - adultList_behavior.size()));
                score = answer? score+1 :score;
            } else if (current_questionindex == adultList_behavior.size() + adultList_psycho.size() -1 ) {
                tvQuestion.setText(adultList_psycho.get(current_questionindex - adultList_behavior.size()));
                score = answer? score+1 :score;
                Toast.makeText(UIUtil.getContext(),"完成所有测试题，即将进入主界面",Toast.LENGTH_SHORT).show();
                UIUtil.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //到末尾进入主页面
                        startActivity(new Intent(UIUtil.getContext(), MainActivity.class));
                        finish();
                    }
                },1500);
            }
        }

    }

    class QuestionBean{
        private String question;
        private int career_type;
        private int question_type;
        @Override
        public String toString() {
            return "Question{" +
                    "question='" + question + '\'' +
                    ", career_type=" + career_type +
                    ", question_type=" + question_type +
                    '}';
        }
    }
}
