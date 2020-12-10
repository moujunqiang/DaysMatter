package com.android.daysmatter.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import com.android.daysmatter.R;
import com.android.daysmatter.helper.AssetsUtils;

/**
 * 添加分类 图片适配器
 */
public class TypeImageAdapter extends RecyclerView.Adapter<TypeImageAdapter.TypeHolder> {
    List<String> typeBeans;
    private Context context;

    public void setTypeBeans(List<String> typeBeans) {
        this.typeBeans = typeBeans;
    }

    private int selectPosition;

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public List<String> getTypeBeans() {
        return typeBeans;
    }

    OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public TypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type_image, parent, false);
        return new TypeHolder(inflate);
    }

    @Override
    public void onBindViewHolder(TypeHolder holder, final int position) {
        String typeBean = typeBeans.get(position);
        holder.ivType.setImageBitmap(AssetsUtils.readBitmapFromAssets(context.getResources().getAssets(), "book_icon/" + typeBean));
        if (selectPosition == position) {
            holder.itemView.setBackgroundResource(R.drawable.ic_icon_selected);
        } else {
            holder.itemView.setBackgroundResource(0);
        }
        holder.ivType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onClick(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return typeBeans == null ? 0 : typeBeans.size();
    }

    class TypeHolder extends RecyclerView.ViewHolder {
        private ImageView ivType;

        public TypeHolder(View itemView) {
            super(itemView);
            ivType = itemView.findViewById(R.id.iv_type);
        }
    }

    public interface OnItemClick {
        void onClick(int position);
    }

}
