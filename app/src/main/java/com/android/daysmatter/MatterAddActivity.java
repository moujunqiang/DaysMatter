package com.android.daysmatter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.android.daysmatter.helper.AssetsUtils;
import com.android.daysmatter.model.Matter;
import com.android.daysmatter.model.TypeBean;

import org.litepal.crud.DataSupport;

/**
 * 添加倒数本
 */

public class MatterAddActivity extends AppCompatActivity implements View.OnClickListener {


    private Button saveMatter;

    private EditText matterContent;

    private static List<Matter> sMatterList = new ArrayList<>();


    public static TextView datetime;
    private RelativeLayout rlCaretory;
    private ImageView ivTypeRes;
    private TextView tvTypeName;
    private TypeBean typeBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_matter);

        datetime = (TextView) findViewById(R.id.datetime);

        initTypeData();
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String dateStr = new SimpleDateFormat("yyy-MM-dd").format(date);
        String weekdayStr = new SimpleDateFormat("EEEE").format(date);
        datetime.setText(dateStr + " " + weekdayStr);


        datetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });


        saveMatter = (Button) findViewById(R.id.save_event);
        saveMatter.setOnClickListener(this);
        rlCaretory = findViewById(R.id.rl_category);
        rlCaretory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MatterAddActivity.this, TypeManagerActivity.class), 100);
            }
        });
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("新增倒数本");
        matterContent = (EditText) findViewById(R.id.matter_content_input);

        ivTypeRes = findViewById(R.id.iv_type_res);
        tvTypeName = findViewById(R.id.tv_type_name);
        List<TypeBean> all = DataSupport.findAll(TypeBean.class);
        typeBean = all.get(0);
        ivTypeRes.setImageBitmap(AssetsUtils.readBitmapFromAssets(getResources().getAssets(), typeBean.getImageRes()));
        tvTypeName.setText(typeBean.getName());

    }

    public void initTypeData() {
        List<TypeBean> all = DataSupport.findAll(TypeBean.class);
        if (all.size() == 0) {//如果没有分类数据默认添加三条
            new TypeBean("生活", "book_icon/ico_0@3x.png").save();
            new TypeBean("学习", "book_icon/ico_1@3x.png").save();
            new TypeBean("纪念日", "book_icon/ico_2@3x.png").save();
        }
    }

    public static void actionStart(Context context, List<Matter> matterList) {
        Intent i = new Intent(context, MatterAddActivity.class);
        sMatterList = matterList;
        context.startActivity(i);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 101) {
            typeBean = (TypeBean) data.getSerializableExtra("type");
            if (typeBean != null) {
                ivTypeRes.setImageBitmap(AssetsUtils.readBitmapFromAssets(getResources().getAssets(), typeBean.getImageRes()));
                tvTypeName.setText(typeBean.getName());
            }
        }


    }

    @Override
    public void onClick(View view) {
        Matter matter = new Matter();
        if (matterContent.getText().length() > 8) {
            matterContent.getText().clear();
            Toast.makeText(MatterAddActivity.this, "请重新输入！", Toast.LENGTH_SHORT).show();
        } else if (matterContent.getText().length() == 0) {
            Toast.makeText(MatterAddActivity.this, "不许输入空事件！", Toast.LENGTH_SHORT).show();
        } else {
            matter.setMatterContent(matterContent.getText().toString());

            String dateStr = datetime.getText().toString().split(" ")[0];
            try {
                matter.setTargetDate(new SimpleDateFormat("yyyy-MM-dd").parse(dateStr));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar c = Calendar.getInstance();
            matter.setCreateDate(c.getTime());
            matter.setTypeId(typeBean.getId());
            matter.save();
            sMatterList.add(matter);
            Toast.makeText(MatterAddActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    /**
     * 日期选择弹框
     */
    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String[] dateStrArr = datetime.getText().toString().split(" ")[0].split("-");
            int year = Integer.parseInt(dateStrArr[0]);
            int month = Integer.parseInt(dateStrArr[1]) - 1;
            int day = Integer.parseInt(dateStrArr[2]);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }


        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            String dateStr = "";
            String weekdayStr = "";
            try {
                dateStr = i + "-" + (i1 + 1) + "-" + i2;
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                weekdayStr = new SimpleDateFormat("EEEE").format(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            datetime.setText(dateStr + " " + weekdayStr);
        }
    }
}
