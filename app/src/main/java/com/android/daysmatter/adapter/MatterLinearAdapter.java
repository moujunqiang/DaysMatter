package com.android.daysmatter.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.android.daysmatter.MatterDetailActivity;
import com.android.daysmatter.R;
import com.android.daysmatter.helper.Utility;
import com.android.daysmatter.model.Matter;

import static java.lang.Math.abs;

/**
 * 首页 列表适配器
 */
public class MatterLinearAdapter extends RecyclerView.Adapter<MatterLinearAdapter.ViewHolder> {

    private List<Matter> mMatterList = new ArrayList<>();

    private Context mContext;
    private int counter = 1;
    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView matterContent,matterCount,matterAfter,matterDaytext;
        public ViewHolder(final View v,int viewType){
            super(v);
            matterContent = v.findViewById(R.id.matter_content);
            matterCount = v.findViewById(R.id.matter_day_count);
            matterAfter = v.findViewById(R.id.matter_after_text);
            matterDaytext = v.findViewById(R.id.matter_day_text);

        }
    }

    public MatterLinearAdapter(List<Matter> list){
        mMatterList = list;
    }

    @Override
    public void onBindViewHolder(MatterLinearAdapter.ViewHolder holder, int position) {
        Matter matter = mMatterList.get(position);

        String matterContent = matter.getMatterContent();
        if (matterContent.length()>5){
            matterContent = matterContent.substring(0,4)+"...";
        }
        holder.matterContent.setText(matterContent);


        long duration = Utility.getDateInterval(matter.getTargetDate());
        String dateStr = new SimpleDateFormat("MM月dd日").format(matter.getTargetDate());


        if (position>0){
            //和前一个倒数日时间比较天数，相同则计数器加一
            Matter before = mMatterList.get(position-1);

            if (before!=null){
                if (Utility.getDateInterval(before.getTargetDate())==Utility.getDateInterval(matter.getTargetDate())){
                    counter++;
                }
            }
        }


        //重置计数器对下一个判断
        counter = 1;


        //duration不同的时候显示不同的文字提示
        String beforetext = "";
        String aftertext = "";
        if(duration < 0) {
            aftertext = "已经";
            ColorStateList list = ContextCompat.getColorStateList(mContext, R.color.expired_light);
            holder.matterCount.setBackgroundTintList(list);
            list = ContextCompat.getColorStateList(mContext,R.color.expired);
            holder.matterDaytext.setBackgroundTintList(list);

        }
        else if(duration>0){
            aftertext = "还有";
            ColorStateList list = ContextCompat.getColorStateList(mContext, R.color.future_light);
            holder.matterCount.setBackgroundTintList(list);
            list = ContextCompat.getColorStateList(mContext,R.color.future);
            holder.matterDaytext.setBackgroundTintList(list);

        }else if (duration==0){
            aftertext = "还有";

            ColorStateList list = ContextCompat.getColorStateList(mContext, R.color.future_light);
            holder.matterCount.setBackgroundTintList(list);
            list = ContextCompat.getColorStateList(mContext,R.color.future);
            holder.matterDaytext.setBackgroundTintList(list);

        }
        holder.matterAfter.setText(aftertext);
        holder.matterCount.setText(Long.toString(abs(duration)));


    }


    @Override
    public int getItemCount() {
        return mMatterList.size();
    }

    @Override
    public MatterLinearAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        final View view  = LayoutInflater.from(mContext).inflate(R.layout.matter_list_item_linear,parent,false);
        final MatterLinearAdapter.ViewHolder hd = new MatterLinearAdapter.ViewHolder(view,viewType);

        hd.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = hd.getAdapterPosition();
                Matter matter = mMatterList.get(position);
                MatterDetailActivity.actionStart(mContext,matter,mMatterList,position);
                Log.i("INFO","enter detail activity");
            }
        });
        return hd;

    }



}
