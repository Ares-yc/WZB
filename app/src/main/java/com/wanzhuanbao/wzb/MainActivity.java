package com.wanzhuanbao.wzb;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private PayDialogFragment payDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        payDialogFragment = new PayDialogFragment();
    }

    public void show(View view){
        float a=(float) (Math.random()*100);

        float numb=a;
        int itemNum=3;//小数点前的位数
        float totalNumb = numb*itemNum;
        float num=(float)(Math.round(totalNumb*100)/100);//如果要求精确小数点前4位就*10000然后/10000
        payDialogFragment.setMoney(num);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(payDialogFragment,"payDialogFragment");
        ft.commitAllowingStateLoss();
    }
}
