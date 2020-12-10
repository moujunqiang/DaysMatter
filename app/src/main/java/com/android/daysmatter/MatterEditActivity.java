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

import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.daysmatter.helper.AssetsUtils;
import com.android.daysmatter.model.Matter;
import com.android.daysmatter.model.TypeBean;

/**
 * 编辑倒数本
 */

public class MatterEditActivity extends AppCompatActivity implements View.OnClickListener {


    private Button saveMatter;

    private EditText matterContent;

    public static TextView datetime;


    public static Matter sMatter;
    private RelativeLayout rlCaretory;
    private ImageView ivTypeRes;
    private TextView tvTypeName;
    TypeBean typeBean;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_matter);
        // Find button in add event layout and set it the click listener
        saveMatter = (Button) findViewById(R.id.save_event);
        saveMatter.setOnClickListener(this);

        matterContent = (EditText) findViewById(R.id.matter_content_input);
        matterContent.setText(sMatter.getMatterContent());


        // find the datetime text view element in add event layout
        datetime = (TextView) findViewById(R.id.datetime);

        // Getting current date time and set for text view component
        Date date = sMatter.getTargetDate();
        String dateStr = new SimpleDateFormat("yyy-MM-dd").format(date);
        String weekdayStr = new SimpleDateFormat("EEEE").format(date);
        datetime.setText(dateStr + " " + weekdayStr);

        // Datetime text view component click listener that to show the date time picker
        datetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePickerFragment = new MatterEditActivity.DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        rlCaretory = findViewById(R.id.rl_category);
        rlCaretory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MatterEditActivity.this, TypeManagerActivity.class), 100);
            }
        });
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView tvTitle=findViewById(R.id.tv_title);
        tvTitle.setText("编辑倒数本");
        matterContent = (EditText) findViewById(R.id.matter_content_input);

        ivTypeRes = findViewById(R.id.iv_type_res);
        tvTypeName = findViewById(R.id.tv_type_name);
         typeBean = DataSupport.find(TypeBean.class, sMatter.getTypeId());
        if (typeBean!=null){
            ivTypeRes.setImageBitmap(AssetsUtils.readBitmapFromAssets(getResources().getAssets(), typeBean.getImageRes()));
            tvTypeName.setText(typeBean.getName());
        }else {
            ivTypeRes.setImageBitmap(AssetsUtils.readBitmapFromAssets(getResources().getAssets(), "book_icon/ico_0@3x.png"));
            tvTypeName.setText("生活");
        }

    }

    public static void actionStart(Context context, Matter matter) {
        Intent i = new Intent(context, MatterEditActivity.class);
        sMatter = matter;
        context.startActivity(i);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        typeBean = (TypeBean) data.getSerializableExtra("type");
        ivTypeRes.setImageBitmap(AssetsUtils.readBitmapFromAssets(getResources().getAssets(), typeBean.getImageRes()));
        tvTypeName.setText(typeBean.getName());
    }

    @Override
    public void onClick(View view) {
        sMatter.setMatterContent(matterContent.getText().toString());
        String dateStr = datetime.getText().toString().split(" ")[0];
        try {
            sMatter.setTargetDate(new SimpleDateFormat("yyyy-MM-dd").parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sMatter.setTypeId(typeBean.getId());
        sMatter.save();
        finish();
    }

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
