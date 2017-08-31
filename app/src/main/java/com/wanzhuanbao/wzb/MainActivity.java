package com.wanzhuanbao.wzb;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wanzhuanbao.wzb.PayDialogFragment.PayDialogListener;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements PayDialogListener{

    private PayDialogFragment payDialogFragment;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        payDialogFragment = PayDialogFragment.newInstance(null);

        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, false);
        // 将该app注册到微信
        api.registerApp(Constant.APP_ID);
    }

    public void show(View view){
        //随机数模拟应付金额
        float a=(float) (Math.random()*100);
        float numb=a;
        int itemNum=3;//小数点前的位数
        float totalNumb = numb*itemNum;
        float num=(float)(Math.round(totalNumb*100)/100);//如果要求精确小数点前4位就*10000然后/10000

        //设置Dialog显示应付金额
        payDialogFragment.setMoney(num);

        //显示支付选择框
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(payDialogFragment,"payDialogFragment");
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onPayment(int payStyle) {
        if (payStyle == Constant.PAY_STYLE_BALANCE){
            //余额支付
            Toast.makeText(MainActivity.this,"余额支付",Toast.LENGTH_LONG).show();
        }else{
            //微信支付
            payByWeChat();
        }
    }

    //微信支付
    private void payByWeChat() {
        String url = "http://wxpay.wxutil.com/pub_v2/app/app_pay.php";
        //支付按钮禁止点击，以防用户重复点击
//        payBtn.setEnabled(false);
        Toast.makeText(MainActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
        try {
            Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
            Response<String> response = NoHttp.startRequestSync(request);
            if (response != null && response.get() != null) {
                JSONObject json = new JSONObject(response.get());
                if (!json.has("retcode")) {
                    PayReq req = new PayReq();
                    req.appId = json.getString("appid");
                    req.partnerId = json.getString("partnerid");
                    req.prepayId = json.getString("prepayid");
                    req.nonceStr = json.getString("noncestr");
                    req.timeStamp = json.getString("timestamp");
                    req.packageValue = json.getString("package");
                    req.sign = json.getString("sign");
                    req.extData = "app data"; // optional
                    Toast.makeText(MainActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    api.sendReq(req);
                } else {
                    Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
                    Toast.makeText(MainActivity.this, "返回错误" + json.getString("retmsg"), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("PAY_GET", "服务器请求错误");
                Toast.makeText(MainActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //支付按钮开放点击
//        payBtn.setEnabled(true);

    }
}
