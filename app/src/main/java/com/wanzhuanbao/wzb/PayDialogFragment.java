package com.wanzhuanbao.wzb;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ====================================
 * 作    者：Ares(颜崔)
 * 地    址：https://github.com/Ares-yc
 * 描    述：
 * 版    本：1.0.0
 * 创建时间：2017/8/31/031.
 * 修改时间：2017/8/31/031.
 * ====================================
 */

public class PayDialogFragment extends DialogFragment implements View.OnClickListener{

    private RelativeLayout balanceRl,wechatRl;
    private ImageView balanceSelIv,wechatSelIv;
    private TextView moneyTv;
    private Button payBtn;
    private float money;

    @Override
    public void onStart() {
        super.onStart();
        //设置弹出框宽屏显示，适应屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
        getDialog().getWindow().setLayout( dm.widthPixels, getDialog().getWindow().getAttributes().height );

        //移动弹出菜单到底部
        WindowManager.LayoutParams wlp = getDialog().getWindow().getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(wlp);
        if (moneyTv != null){
            moneyTv.setText("￥"+money);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置背景透明+
        getDialog().getWindow().setWindowAnimations(R.style.upload_dialog_animation);//添加一组进出动画
        View view = inflater.inflate(R.layout.dialog_pay,container);
        initView(view);
        return view;
    }

    private void initView(View view) {

        balanceRl = view.findViewById(R.id.rl_dialog_pay_balance);
        wechatRl  = view.findViewById(R.id.rl_dialog_pay_wechat);

        balanceSelIv = view.findViewById(R.id.iv_dialog_pay_balance_selected);
        wechatSelIv  = view.findViewById(R.id.iv_dialog_pay_wechat_selected);

        moneyTv = view.findViewById(R.id.tv_dialog_pay_money);
        payBtn  = view.findViewById(R.id.btn_dialog_pay_sure);

        balanceRl.setOnClickListener(this);
        wechatRl.setOnClickListener(this);
        payBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_dialog_pay_balance:
                balanceSelIv.setVisibility(View.VISIBLE);
                wechatSelIv.setVisibility(View.INVISIBLE);
                break;
            case R.id.rl_dialog_pay_wechat:
                balanceSelIv.setVisibility(View.INVISIBLE);
                wechatSelIv.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_dialog_pay_sure:

                break;
        }
    }

    public void setMoney(float money){
        this.money = money;
    }
}
