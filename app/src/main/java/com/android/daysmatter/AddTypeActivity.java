package com.android.daysmatter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.android.daysmatter.adapter.TypeImageAdapter;
import com.android.daysmatter.model.TypeBean;

/**
 * 添加新的倒数本分类
 */
public class AddTypeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvBack;
    /**
     * 保存
     */
    private TextView mTvTypeSave;
    /**
     * 点击这里输入本子名字
     */
    private EditText mMatterContentInput;
    private RecyclerView mRvTypeImage;
    private String imageRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type);
        initView();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);
        mTvTypeSave = (TextView) findViewById(R.id.tv_type_save);
        mTvTypeSave.setOnClickListener(this);
        mMatterContentInput = (EditText) findViewById(R.id.matter_content_input);
        mRvTypeImage = (RecyclerView) findViewById(R.id.rv_type_image);
        mRvTypeImage.setLayoutManager(new GridLayoutManager(this, 6));
        final TypeImageAdapter adapter = new TypeImageAdapter();
        adapter.setOnItemClick(new TypeImageAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
                imageRes = adapter.getTypeBeans().get(position);
                adapter.setSelectPosition(position);
                adapter.notifyDataSetChanged();
            }
        });
        try {
            List<String> book_icon = Arrays.asList(getAssets().list("book_icon"));
            imageRes = book_icon.get(0);
            adapter.setTypeBeans(book_icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRvTypeImage.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_type_save:
                if (mMatterContentInput.getText().toString().isEmpty()) {
                    Toast.makeText(this, "请输入名字", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(imageRes)) {
                    Toast.makeText(this, "请选择图标", Toast.LENGTH_SHORT).show();
                    return;
                }
                //保存分类
                new TypeBean(mMatterContentInput.getText().toString(), "book_icon/"+imageRes).save();
                finish();
                break;
        }
    }
}