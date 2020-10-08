package com.moodtoday.activity;

import android.app.Dialog;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.moodtoday.AppDefine;
import com.moodtoday.R;
import com.moodtoday.db.DBHelper;

import org.ini4j.Ini;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TodayActivity extends AppCompatActivity {

    ImageView iv1, iv2, iv3, iv4, iv5;
    ImageView iv_w_1, iv_w_2, iv_w_3, iv_w_4, iv_w_5;
    EditText edtContents;
    //EditText edtWeather, edtMusic, edtContents;

    DBHelper dbHelper;
    int weatherNum = 0;
    int moodNum = 0;

    String email;
    String sDate = "", sMusic = "", sWording = "", sContents = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        email = getIntent().getStringExtra("email");

        dbHelper = new DBHelper(TodayActivity.this);

        initCtrl();
    }

    private void initCtrl() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기

        //edtWeather = findViewById(R.id.edt_weather);
        //edtMusic = findViewById(R.id.edt_music);
        edtContents = findViewById(R.id.edt_contents);

        iv1 = findViewById(R.id.iv1);
        iv2 = findViewById(R.id.iv2);
        iv3 = findViewById(R.id.iv3);
        iv4 = findViewById(R.id.iv4);
        iv5 = findViewById(R.id.iv5);

        iv_w_1 = findViewById(R.id.iv_w_1);
        iv_w_2 = findViewById(R.id.iv_w_2);
        iv_w_3 = findViewById(R.id.iv_w_3);
        iv_w_4 = findViewById(R.id.iv_w_4);
        iv_w_5 = findViewById(R.id.iv_w_5);

        iv1.setOnClickListener(mIvClickListener);
        iv2.setOnClickListener(mIvClickListener);
        iv3.setOnClickListener(mIvClickListener);
        iv4.setOnClickListener(mIvClickListener);
        iv5.setOnClickListener(mIvClickListener);

        iv_w_1.setOnClickListener(mIvWClickListener);
        iv_w_2.setOnClickListener(mIvWClickListener);
        iv_w_3.setOnClickListener(mIvWClickListener);
        iv_w_4.setOnClickListener(mIvWClickListener);
        iv_w_5.setOnClickListener(mIvWClickListener);
    }

    String getTodayDate() {
        long now = System.currentTimeMillis();

        Date date = new Date(now);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
        String strDate = simpleDateFormat.format(date);

        return strDate;
    }

    public void saveDiary() {
        sDate = getTodayDate();
        //sDate = ((LineEditText)findViewById(R.id.edt_date)).getText().toString();
        sContents = edtContents.getText().toString();

        if (weatherNum == 0) {
            Toast.makeText(TodayActivity.this, "날씨의 오늘을 선택하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sContents.equals("")) {
            Toast.makeText(TodayActivity.this, "기록의 오늘을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (moodNum == 0) {
            Toast.makeText(TodayActivity.this, "기분 이모티콘을 선택하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Asset에서 음악, 글귀를 읽어와 랜덤 지정

        String sFileName = "music.txt";
        String sCate = "";
        if(weatherNum == 1){
            sCate = "맑음";
        }
        else if(weatherNum == 2){
            sCate = "구름";
        }
        else if(weatherNum == 3){
            sCate = "비";
        }
        else if(weatherNum == 4){
            sCate = "바람";
        }
        else if(weatherNum == 5){
            sCate = "눈";
        }
        sMusic = getRandomItem(sFileName, sCate);
        if(TextUtils.isEmpty(sMusic)){
            Toast.makeText(TodayActivity.this, "추천 음악을 불러올수없습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        sFileName = "wording.txt";
        if(moodNum == 1){
            sCate = "행복";
        }
        else if(moodNum == 2){
            sCate = "우울";
        }
        else if(moodNum == 3){
            sCate = "쏘쏘";
        }
        else if(moodNum == 4){
            sCate = "뿌듯";
        }
        else if(moodNum == 5){
            sCate = "분노";
        }

        sWording = getRandomItem(sFileName, sCate);
        if(TextUtils.isEmpty(sWording)){
            Toast.makeText(TodayActivity.this, "추천 글귀를 불러올수없습니다", Toast.LENGTH_SHORT).show();
            return;
        }


        final Dialog dialog = new Dialog(TodayActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_diary);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Window window = dialog.getWindow();

        int x = (int) (size.x);
        int y = (int) (size.y * 0.9);

        window.setLayout(x, y);

        //날짜
        TextView tvDate = dialog.findViewById(R.id.tv_date);
        tvDate.setText(sDate);

        //기분 아이콘
        ImageView ivIcon = dialog.findViewById(R.id.iv_icon);
        ivIcon.setImageResource(AppDefine.moodIconArr[moodNum - 1]);

        //날씨 아이콘
        ImageView ivWIcon = dialog.findViewById(R.id.iv_w_icon);
        ivWIcon.setImageResource(AppDefine.weatherIconArr[weatherNum - 1]);

        //기록의 오늘
        TextView tvContents = dialog.findViewById(R.id.tv_contents);
        tvContents.setText(sContents);
        //음악의 오늘
        TextView tvMusic = dialog.findViewById(R.id.tv_music);
        tvMusic.setText(sMusic);
        //위로의 오늘
        TextView tvWording = dialog.findViewById(R.id.tv_wording);
        tvWording.setText(sWording);

        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button btnOk = dialog.findViewById(R.id.btn_ok);
        btnOk.setText("저장");
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long lResult = dbHelper.insertDiaryTable(sDate, weatherNum, sContents, moodNum, sMusic, sWording, email);
                if(lResult == -1){
                    Toast.makeText(TodayActivity.this, "DB 입력 실패\n다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(TodayActivity.this, "저장 완료", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        dialog.show();      // 다이얼로그 출력
    }

    public String getRandomItem(String sFileName, String sCate){
        AssetManager assetMgr = getResources().getAssets();
        InputStream inputStream = null;
        Ini.Section section = null;
        try {
            inputStream = assetMgr.open(sFileName);
            Ini iniFile = new Ini(inputStream);
            section = iniFile.get(sCate);
            int nSize = section.size();
            Random random = new Random(System.currentTimeMillis());
            int nRandomIdx = random.nextInt(nSize)+1;
            return section.get(String.valueOf(nRandomIdx));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveDiary();
                break;
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener mIvClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            iv1.setScaleX((float) 1.5);
            iv1.setScaleY((float) 1.5);

            iv2.setScaleX((float) 1.5);
            iv2.setScaleY((float) 1.5);

            iv3.setScaleX((float) 1.5);
            iv3.setScaleY((float) 1.5);

            iv4.setScaleX((float) 1.5);
            iv4.setScaleY((float) 1.5);

            iv5.setScaleX((float) 1.5);
            iv5.setScaleY((float) 1.5);

            switch (v.getId()) {
                case R.id.iv1:
                    moodNum = 1;
                    iv1.setScaleX((float) 2);
                    iv1.setScaleY((float) 2);
                    break;
                case R.id.iv2:
                    moodNum = 2;
                    iv2.setScaleX((float) 2);
                    iv2.setScaleY((float) 2);
                    break;
                case R.id.iv3:
                    moodNum = 3;
                    iv3.setScaleX((float) 2);
                    iv3.setScaleY((float) 2);
                    break;
                case R.id.iv4:
                    moodNum = 4;
                    iv4.setScaleX((float) 2);
                    iv4.setScaleY((float) 2);
                    break;
                case R.id.iv5:
                    moodNum = 5;
                    iv5.setScaleX((float) 2);
                    iv5.setScaleY((float) 2);
                    break;
            }
        }
    };

    View.OnClickListener mIvWClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            iv_w_1.setScaleX((float) 1.0);
            iv_w_1.setScaleY((float) 1.0);

            iv_w_2.setScaleX((float) 1.0);
            iv_w_2.setScaleY((float) 1.0);

            iv_w_3.setScaleX((float) 1.0);
            iv_w_3.setScaleY((float) 1.0);

            iv_w_4.setScaleX((float) 1.0);
            iv_w_4.setScaleY((float) 1.0);

            iv_w_5.setScaleX((float) 1.0);
            iv_w_5.setScaleY((float) 1.0);

            switch (v.getId()) {
                case R.id.iv_w_1:
                    weatherNum = 1;
                    iv_w_1.setScaleX((float) 1.5);
                    iv_w_1.setScaleY((float) 1.5);
                    break;
                case R.id.iv_w_2:
                    weatherNum = 2;
                    iv_w_2.setScaleX((float) 1.5);
                    iv_w_2.setScaleY((float) 1.5);
                    break;
                case R.id.iv_w_3:
                    weatherNum = 3;
                    iv_w_3.setScaleX((float) 1.5);
                    iv_w_3.setScaleY((float) 1.5);
                    break;
                case R.id.iv_w_4:
                    weatherNum = 4;
                    iv_w_4.setScaleX((float) 1.5);
                    iv_w_4.setScaleY((float) 1.5);
                    break;
                case R.id.iv_w_5:
                    weatherNum = 5;
                    iv_w_5.setScaleX((float) 1.5);
                    iv_w_5.setScaleY((float) 1.5);
                    break;
            }
        }
    };
}
