package com.android.daysmatter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.android.daysmatter.adapter.MatterLinearAdapter;
import com.android.daysmatter.helper.MatterComparator;
import com.android.daysmatter.model.Matter;
import com.android.daysmatter.model.TypeBean;

import static java.lang.Math.abs;

/**
 * 通过分类查询倒数本
 */
public class QueryByTypeActivity extends AppCompatActivity {


    private List<Matter> mMatterList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private MatterLinearAdapter MyAdapterLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_query_by_type);
        //deal with database
        LitePal.getDatabase();

        TypeBean type = (TypeBean) getIntent().getSerializableExtra("type");
        //get list and setDatabase
        mMatterList = DataSupport.where("typeId=?", type.getId() + "").find(Matter.class);
        //mMatterList = DataSupport.findAll(Matter.class);

        //set recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        MyAdapterLinear = new MatterLinearAdapter(mMatterList);
        mRecyclerView.setAdapter(MyAdapterLinear);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(type.getName());

    }


    /**
     * 按优先级排序列表(根据日期）
     */

    private List<Matter> sortMatterList(List<Matter> matterList) {
        Collections.sort(matterList, new MatterComparator());
        return matterList;
    }

    private void doRefreshForLinear(MatterLinearAdapter adapter) {
        if (!mMatterList.isEmpty()) {
            adapter = new MatterLinearAdapter(mMatterList);
            mRecyclerView.setAdapter(adapter);
            mMatterList = sortMatterList(mMatterList);
            Log.i("INFO", "sorting list");
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("INFO", "calling onResume!");
        doRefreshForLinear(MyAdapterLinear);

    }


}
