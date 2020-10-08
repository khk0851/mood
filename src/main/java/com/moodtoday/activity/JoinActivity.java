package com.moodtoday.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.moodtoday.db.DBHelper;
import com.moodtoday.R;

public class JoinActivity extends AppCompatActivity {

    EditText edtEmail, edtPass, edtPassCheck, edtName;
    RadioGroup radioGroup;
    RadioButton rbtnMan, rbtnWoman;
    Button btnJoin;

    String strSex = "";

    DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        dbHelper = new DBHelper(JoinActivity.this);

        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기

        edtEmail = findViewById(R.id.edt_email);
        edtPass = findViewById(R.id.edt_password);
        edtPassCheck = findViewById(R.id.edt_password_check);
        edtName = findViewById(R.id.edt_name);

        radioGroup = findViewById(R.id.radio_group);
        rbtnMan = findViewById(R.id.rbtn_man);
        rbtnWoman = findViewById(R.id.rbtn_woman);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbtn_man) {
                    strSex = "남";
                } else {
                    strSex = "여";
                }
            }
        });

        btnJoin = findViewById(R.id.btn_join);
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = edtEmail.getText().toString();
                if (strEmail.replace(" ", "").equals("")) {
                    Toast.makeText(JoinActivity.this, "이메일 주소를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String strPass = edtPass.getText().toString();
                if (strPass.replace(" ", "").equals("")) {
                    Toast.makeText(JoinActivity.this, "패스워드를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String strPassCheck = edtPassCheck.getText().toString();
                if (strPassCheck.replace(" ", "").equals("")) {
                    Toast.makeText(JoinActivity.this, "패스워드를 한 번 더더 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String strName = edtName.getText().toString();
                if (strName.replace(" ", "").equals("")) {
                    Toast.makeText(JoinActivity.this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(strSex.equals("")) {
                    Toast.makeText(JoinActivity.this, "성별을 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!strPass.equals(strPassCheck)) {
                    Toast.makeText(JoinActivity.this, "패스워드가 틀립니다.", Toast.LENGTH_SHORT).show();
                    return;
                }


                dbHelper.insertMemberTable(strEmail, strPass, strName, strSex);
                Toast.makeText(JoinActivity.this, "회원 가입 완료.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
