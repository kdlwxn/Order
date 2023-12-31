package com.Eight.order.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.Eight.order.R;
import com.Eight.order.bean.FoodBean;
import com.bumptech.glide.Glide;

public class FoodActivity extends AppCompatActivity {
    private FoodBean bean;
    private TextView tv_food_name, tv_sale_num, tv_price;
    private ImageView iv_food_pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        //从店铺详情界面传递过来的菜的数据
        bean = (FoodBean) getIntent().getSerializableExtra("food");
        initView();
        setData();
    }
    /**
     * 初始化界面控件
     */
    private void initView() {
        tv_food_name = (TextView) findViewById(R.id.tv_food_name);
        tv_sale_num = (TextView) findViewById(R.id.tv_sale_num);
        tv_price = (TextView) findViewById(R.id.tv_price);
        iv_food_pic = (ImageView) findViewById(R.id.iv_food_pic);
    }
    /**
     * 设置界面数据
     */
    private void setData() {
        if (bean == null) return;
        tv_food_name.setText(bean.getFoodName());
        tv_sale_num.setText("月售" + bean.getSaleNum());
        tv_price.setText("￥" + bean.getPrice());
        Glide.with(this)
                .load(bean.getFoodPic())
                .error(R.mipmap.ic_launcher)
                .into(iv_food_pic);
    }
}