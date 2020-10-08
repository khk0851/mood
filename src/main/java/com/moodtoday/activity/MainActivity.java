package com.moodtoday.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.moodtoday.R;
import com.moodtoday.db.DBHelper;
import com.moodtoday.dto.DiaryDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button btnLast, btnToday;
    DBHelper dbHelper;
    ArrayList<DiaryDTO> diaryList= new ArrayList<>();;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = getIntent().getStringExtra("email");

        dbHelper = new DBHelper(MainActivity.this);

        btnLast = findViewById(R.id.btn_last);
        btnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LastActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        btnToday = findViewById(R.id.btn_today);
        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(diaryList.size() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("오늘의 기록을 작성하셨습니다.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Intent intent = new Intent(MainActivity.this, TodayActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
            }
        });
    }

    String getTodayDate() {
        long now = System.currentTimeMillis();

        Date date = new Date(now);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
        String strDate = simpleDateFormat.format(date);

        return  strDate;
    }

    @Override
    protected void onResume() {
        super.onResume();


        diaryList.clear();
        diaryList = dbHelper.selectDiaryTable(getTodayDate(), email);
    }
}