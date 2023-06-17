package com.Eight.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Eight.order.R;
import com.Eight.order.activity.ShopDetailActivity;
import com.Eight.order.bean.ShopBean;
import com.bumptech.glide.Glide;

import java.util.List;
public class ShopAdapter extends BaseAdapter {
    private Context context;
    private List<ShopBean> sblist;
    public ShopAdapter(Context context) {
        this.context = context;
    }
    /**
     * 设置数据更新界面
     * @return
     */
    public void setData(List<ShopBean> sblist){
        this.sblist = sblist;
        notifyDataSetChanged();//更新界面
    }
    @Override
    public int getCount() {//获取 Item 总数
        return sblist == null ? 0 : sblist.size();
    }
    @Override
    public Object getItem(int position) {//根据 position 得到相应位置的 item 对象
        return sblist == null ? null : sblist.get(position);
    }
    @Override
    public long getItemId(int position) {//根据 position 得到对应的 item 的 id
        return position;
    }
    /**
     * 得到相应 position 对应的 Item 的视图，position 是当前 Item 的位置，
     * convertView 参数是滚出屏幕的 Item 的 View
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        //利用 converView
        if (convertView == null){
            vh = new ViewHolder();
            //加载子布局
            convertView = LayoutInflater.from(context).inflate(R.layout.shop_item,null);
            //初始化控件变量
            vh.iv_shop_pic = convertView.findViewById(R.id.iv_shop_pic);
            vh.tv_shop_name = convertView.findViewById(R.id.tv_shop_name);
            vh.tv_sale_num = convertView.findViewById(R.id.tv_sale_num);
            vh.tv_cost = convertView.findViewById(R.id.tv_cost);
            vh.tv_welfare = convertView.findViewById(R.id.tv_welfare);
            vh.tv_time = convertView.findViewById(R.id.tv_time);
            convertView.setTag(vh);//将控件容器添加到缓存
        }else {
            vh = (ViewHolder) convertView.getTag();//报错说类型不匹配，需要强转
        }
        //获取 position 对应的 Item 的数据对象，要将对象封装到 ShopBean 类中
        final ShopBean bean = (ShopBean) getItem(position);
        //把数据显示对应的控件上
        if (bean != null){
            vh.tv_shop_name.setText(bean.getShopName());
            vh.tv_sale_num.setText(" 月售 "+bean.getSaleNum());
            vh.tv_cost.setText(" 起送￥ "+bean.getOfferPrice()+
                    " 配送￥ "+bean.getDistributionCost());
            vh.tv_time.setText(bean.getTime());
            vh.tv_welfare.setText(bean.getWelfare());
            Glide.with(context)
                    .load(bean.getShopPic())
                    .error(R.mipmap.ic_launcher)
                    .into(vh.iv_shop_pic);
        }
        //每个 item 的点击事件
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到店铺详情界面
                if (bean == null) return;
                Intent intent = new Intent(context, ShopDetailActivity.class);
                //把店铺的详细信息传递到店铺详情界面
                intent.putExtra("shop",bean);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        public TextView tv_shop_name,tv_sale_num,tv_cost,tv_welfare,tv_time;
        public ImageView iv_shop_pic;
    }
}