package com.Eight.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.Eight.order.R;
import com.Eight.order.activity.FoodActivity;
import com.Eight.order.bean.FoodBean;
import com.bumptech.glide.Glide;
import java.util.List;
public class MenuAdapter extends BaseAdapter {
    private Context context;//调用此 adapter 的上下文
    private List<FoodBean> fblist;//菜单列表数据
    private OnSelectListener onSelectListener;//加入购物车按钮的监听事件
    public MenuAdapter(Context context, OnSelectListener onSelectListener) {
        this.context = context;
        this.onSelectListener = onSelectListener;
    }
    /**
     * 设置数据更新界面
     */
    public void setData(List<FoodBean> fblist){
        this.fblist = fblist;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {//获取菜单的 Item 总数
        return fblist == null ? 0 : fblist.size();
    }
    @Override
    public Object getItem(int position) {//根据 position 得到对应的 Item 的对象
        return fblist == null ? null : fblist.get(position);
    }
    @Override
    public long getItemId(int position) {//根据 position 得到对应的 item 的 id
        return position;
    }
    /**
     * 得到相应 position 对应的 Item 的视图，position 是当前 Item 的位置，
     * convertView 参数是滚出屏幕的 Item 的 view
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        //利用 convertView
        if (convertView == null){
            vh = new ViewHolder();//实例化一个控件容器
            //加载菜单子布局
            convertView = LayoutInflater.from(context).inflate(R.layout.menu_item,null);
            //初始控件变量
            vh.tv_food_name = convertView.findViewById(R.id.tv_food_name);
            vh.tv_taste = convertView.findViewById(R.id.tv_taste);
            vh.tv_sale_num = convertView.findViewById(R.id.tv_sale_num);
            vh.tv_price = convertView.findViewById(R.id.tv_price);
            vh.btn_add_car = convertView.findViewById(R.id.btn_add_car);
            vh.iv_food_pic = convertView.findViewById(R.id.iv_food_pic);
            convertView .setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        //获取 position 对应的 Item 的数据对象
        final FoodBean bean = (FoodBean) getItem(position);
        if (bean != null){
            vh.tv_food_name.setText(bean.getFoodName());
            vh.tv_taste.setText(bean.getTaste());
            vh.tv_sale_num.setText(" 月售 "+bean.getSaleNum());
            vh.tv_price.setText(" ￥"+bean.getPrice());
            Glide.with(context)
                    .load(bean.getFoodPic())
                    .error(R.mipmap.ic_launcher)
                    .into(vh.iv_food_pic);
        }
        //每个 item 的点击事件
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到菜品详情界面
                if (bean == null) return;
                Intent intent = new Intent(context, FoodActivity.class);
                //把菜品的详细信息传递到菜品详情界面
                intent.putExtra("food",bean);
                context.startActivity(intent);
            }
        });
        vh.btn_add_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//加入购物车按钮的点击事件
                onSelectListener.onSelectAddCar(position);
            }
        });
        return convertView;
    }
    class ViewHolder{
        public TextView tv_food_name,tv_taste,tv_sale_num,tv_price;
        public Button btn_add_car;
        public ImageView iv_food_pic;
    }
    public interface OnSelectListener{
        void onSelectAddCar(int position);//处理加入购物车按钮的方法
    }
}