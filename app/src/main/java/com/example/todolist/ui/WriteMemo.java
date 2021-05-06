package com.example.todolist.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.todolist.R;
import com.example.todolist.databinding.WriteMemoBinding;
import com.example.todolist.logic.DatabaseHelper;

import java.util.Calendar;

public class WriteMemo extends AppCompatActivity {

    private WriteMemoBinding writeMemoBinding;

    String s;

    private long startnum;
    private long endnum;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_memo);
        writeMemoBinding = DataBindingUtil.setContentView(this,R.layout.write_memo);
        writeMemoBinding.start.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>

        dbHelper = new DatabaseHelper(this,"BookStory.db",null,1);

        initDate();
        inittime();

        writeMemoBinding.sureadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("start",writeMemoBinding.start.getText().toString());
                values.put("endtime",writeMemoBinding.end.getText().toString());
                values.put("main",writeMemoBinding.main.getText().toString());
                values.put("startnum",startnum);
                values.put("endnum",endnum);
                db.insert("Book",null,values);
                finish();
            }
        });

        writeMemoBinding.cancaladd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void showDatePickerDialog_1(EditText birthday) {
        long[] num = new long[1];
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(WriteMemo.this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startnum = 100000000*year+1000000*month+10000*dayOfMonth;
                System.out.println(startnum);
                birthday.setText(year+"/"+(month+1)+"/"+dayOfMonth);
            }
        },c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showDatePickerDialog_2(EditText birthday) {
        long[] num = new long[1];
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(WriteMemo.this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endnum = 100000000*year+1000000*month+10000*dayOfMonth;
                System.out.println(endnum);
                birthday.setText(year+"/"+(month+1)+"/"+dayOfMonth);
            }
        },c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void initDate() {
        EditText finalstart = writeMemoBinding.start;
        EditText finalend = writeMemoBinding.end;
        writeMemoBinding.start.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    showDatePickerDialog_1(finalstart);
                }
            }
        });

        writeMemoBinding.end.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    showDatePickerDialog_2(finalend);
                }
            }
        });

        writeMemoBinding.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDatePickerDialog_1(finalstart);
            }
        });

        writeMemoBinding.end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDatePickerDialog_2(finalend);
            }
        });

    }

    private void inittime() {
        long[] num = {0};
        writeMemoBinding.selecttime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                s = "/"+hourOfDay+":"+minute;
                num[0] = 100*hourOfDay+minute;
            }
        });

        writeMemoBinding.getstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeMemoBinding.start.setText(writeMemoBinding.start.getText() + s);
                startnum = startnum+num[0];
                dbHelper = new DatabaseHelper(WriteMemo.this,"BookStory.db",null,1);
            }
        });

        writeMemoBinding.getend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endnum = endnum+num[0];
                if (startnum<endnum) {
                    writeMemoBinding.end.setText(writeMemoBinding.end.getText() + s);
                }
                else {
                    Toast.makeText(WriteMemo.this,"时间选择错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}