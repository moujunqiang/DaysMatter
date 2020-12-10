package com.android.daysmatter.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
 * 首页 宫格显示列表 适配器
 */
public class MatterGridAdapter extends RecyclerView.Adapter<MatterGridAdapter.ViewHolder> {

    private List<Matter> mMatterList = new ArrayList<>();
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView matterContent,matterDate,matterCount,matterBefore,matterAfter;
        private LinearLayout matterHeader;
        public ViewHolder(final View v){
            super(v);
            matterContent = v.findViewById(R.id.matter_content);
            matterDate = v.findViewById(R.id.matter_date);
            matterCount = v.findViewById(R.id.matter_day_count);
            matterBefore = v.findViewById(R.id.matter_before_text);
            matterAfter = v.findViewById(R.id.matter_after_text);
            matterHeader = v.findViewById(R.id.header_linear);

        }
    }

    public MatterGridAdapter(List<Matter> list){
        mMatterList = list;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Matter matter = mMatterList.get(position);
        String matterContent = matter.getMatterContent();
        if (matterContent.length()>5){
            matterContent = matterContent.substring(0,4)+"...";
        }
        holder.matterContent.setText(matterContent);
        if(matter.getTargetDate() != null) {
            holder.matterDate.setText(new SimpleDateFormat("yyy年MM月dd日").
                    format(matter.getTargetDate()));
        }
        long duration = Utility.getDateInterval(matter.getTargetDate());
        String beforetext = "";
        String aftertext = "";
        if(duration < 0) {
            aftertext = "已经";
            ColorStateList list = ContextCompat.getColorStateList(mContext, R.color.expired);
            holder.matterHeader.setBackgroundTintList(list);
        } else if(duration >=0) {
            aftertext = "还有";
            beforetext = "距离";
            ColorStateList list = ContextCompat.getColorStateList(mContext, R.color.future);
            holder.matterHeader.setBackgroundTintList(list);
        }
        holder.matterAfter.setText(aftertext);
        holder.matterBefore.setText(beforetext);
        holder.matterCount.setText(Long.toString(abs(duration)));



    }

    public List<Matter> getMatterList(){
        return mMatterList;
    }

    @Override
    public int getItemCount() {
        return mMatterList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        final View view  = LayoutInflater.from(mContext).inflate(R.layout.matter_list_item_grid,parent,false);
        final ViewHolder hd = new ViewHolder(view);

        hd.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = hd.getAdapterPosition();
                Matter matter = mMatterList.get(position);
                MatterDetailActivity.actionStart(mContext,matter,mMatterList,position);
            }
        });
        return hd;

    }

}
