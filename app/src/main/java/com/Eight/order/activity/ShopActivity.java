package com.Eight.order.activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.Eight.order.R;
import com.Eight.order.adapter.ShopAdapter;
import com.Eight.order.bean.ShopBean;
import com.Eight.order.utils.Constant;
import com.Eight.order.utils.JsonParse;
import com.Eight.order.views.ShopListView;
import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class ShopActivity extends AppCompatActivity {
    //第 1 步定义变量
    private TextView tv_back,tv_title;//返回键与标题控件
    private ShopListView slv_list;//店铺列表控件
    private ShopAdapter adapter;//列表的适配器
    private RelativeLayout rl_title_bar;//标题栏的布局
    private Mhandler mhandler;//自定义的消息处理器
    public static final int MSG_SHOP_OK = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        //第 2 步：初始化控件变量
        init();
        //第 3 步：初始化数据，即访问服务器，并发送请求，获取数据
        initData();
        //第 4 步：实例化一个消息处理对象
        mhandler = new Mhandler();
    }
    private void initData() {
        //要访问服务器
        //创建一个 http 客户端对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //用构建者模式创建请求
        Request request = new Request.Builder()
                .url(Constant.WEB_SITE+Constant.REQUEST_SHOP_URL)
                .get()//请求方式为 get，主要有两大类请求方式：get 和 post
                .build();
        //创建 call 对象
        Call call = okHttpClient.newCall(request);
        //开启异步线程访问网络
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //从服务器获取店铺数据
                String res = response.body().string();//访问到的 JSON 数据
                Message msg = new Message();//创建消息对象
                msg.what = MSG_SHOP_OK;
                msg.obj = res;
                mhandler.sendMessage(msg);
            }
        });
    }
    /**
     * 初始化界面控件
     */
    private void init() {
        tv_back = findViewById(R.id.tv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("店铺");//设置标题
        rl_title_bar = findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.blue_color));//设置标题栏背景
        tv_back.setVisibility(View.GONE);//设置不显示返回键
        slv_list = findViewById(R.id.slv_list);
        adapter = new ShopAdapter(this);//实例化一个 ShopAdapter 的对象
        slv_list.setAdapter(adapter);//设置列表的适配器
    }
    /**
     * 消息处理
     */
    private class Mhandler extends Handler {
        @Override
        public void dispatchMessage(@NonNull Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what){
                case MSG_SHOP_OK:
                    if (msg.obj != null){
                        String vlResult = (String) msg.obj;//将响应体 json 数据赋值给vlResult
                        //解析获取的 JSON 数据
                        List<ShopBean> shopList = JsonParse.getInstance()
                                .getShopList(vlResult);
                        adapter.setData(shopList);
                    }
                    break;
            }
        }
    }
    /**
     * 退出订餐应用
     */
    protected Long exitTime;//记录第一次点击时的时间
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN){
            if ((System.currentTimeMillis() - exitTime) > 2000){
                Toast.makeText(ShopActivity.this,"再按一次退出订餐应用",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }else {
                ShopActivity.this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}