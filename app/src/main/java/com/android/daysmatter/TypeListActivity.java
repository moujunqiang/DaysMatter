package com.android.daysmatter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import org.litepal.crud.DataSupport;

import java.util.List;

import com.android.daysmatter.adapter.TypeListAdapter;
import com.android.daysmatter.model.TypeBean;

/**
 * 分类列表。后面跟该分类有几个倒数本
 */
public class TypeListActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvBack;
    private RecyclerView mRvType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);
        mRvType = (RecyclerView) findViewById(R.id.rv_type);
        mRvType.setLayoutManager(new LinearLayoutManager(this));
        TypeListAdapter adapter = new TypeListAdapter();
        adapter.setTypeBeans(getDate());
        mRvType.setAdapter(adapter);
        adapter.setOnLoginClick(new TypeListAdapter.OnLoginClick() {
            @Override
            public void onLongClick(int position) {
            }

            @Override
            public void onClick(int position) {
                TypeBean value = getDate().get(position);
                Intent intent = new Intent(TypeListActivity.this, QueryByTypeActivity.class);
                intent.putExtra("type",value);
                startActivity(intent);
            }
        });
    }



    /**
     * 获取所有的分类列表数据
     */
    public List<TypeBean> getDate() {
        List<TypeBean> all = DataSupport.findAll(TypeBean.class);
        return all;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_back:
                finish();
                break;

        }
    }
}