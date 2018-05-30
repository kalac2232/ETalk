package com.kalac.etalk.Activites;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.kalac.etalk.Fragments.GroupFragment;
import com.kalac.etalk.Fragments.MainFragment;
import com.kalac.etalk.Fragments.MineFragment;
import com.kalac.etalk.Fragments.TrainFragment;
import com.kalac.etalk.Fragments.TreatmentFragment;
import com.kalac.etalk.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Fragment currentFragment = null;
    private RadioButton rbMain;
    private RadioButton rbGroup;
    private RadioButton rbTreatment;
    private RadioButton rbTrain;
    private RadioButton rbMy;
    private MainFragment mainFragment;
    private GroupFragment groupFragment;
    private TrainFragment trainFragment;
    private TreatmentFragment treatmentFragment;
    private MineFragment mineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        View statusBar = findViewById(R.id.statusBarView);
        //根据状态栏高度设置占位控件的高度
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = getStatusBarHeight();
        rbMain = findViewById(R.id.rb_main);
        rbGroup = findViewById(R.id.rb_group);
        rbTreatment = findViewById(R.id.rb_treatment);
        rbTrain = findViewById(R.id.rb_train);
        rbMy = findViewById(R.id.rb_my);

        mainFragment = new MainFragment();
        groupFragment = new GroupFragment();
        trainFragment = new TrainFragment();
        treatmentFragment = new TreatmentFragment();
        mineFragment = new MineFragment();

        showFragment(mainFragment);

        rbMain.setOnClickListener(this);
        rbGroup.setOnClickListener(this);
        rbTreatment.setOnClickListener(this);
        rbTrain.setOnClickListener(this);
        rbMy.setOnClickListener(this);
    }

    /**
     * 显示相应的fragment
     * @param fragment 要显示的fragment
     */
    public void showFragment(Fragment fragment) {
        //创建fragmentManager对象
        FragmentManager fragmentManager = getFragmentManager();
        //开启事务 创建事务对象
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //如果之前没有添加过
        if (!fragment.isAdded()) {
            if (currentFragment != null) {
                //隐藏fragment
                fragmentTransaction.hide(currentFragment);
            }
            fragmentTransaction.add(R.id.fl_content,fragment);
        } else {
            if (currentFragment != null) {
                fragmentTransaction.hide(currentFragment);
            }
            fragmentTransaction.show(fragment);
        }
        //全局变量，记录当前显示的fragment
        currentFragment = fragment;
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.rb_main:
                showFragment(mainFragment);
                break;
            case R.id.rb_group:
                showFragment(groupFragment);
                break;
            case R.id.rb_train:
                showFragment(trainFragment);
                break;
            case R.id.rb_treatment:
                showFragment(treatmentFragment);
                break;
            case R.id.rb_my:
                showFragment(mineFragment);
                break;
            default:
                break;
        }
    }
}
