package com.moodtoday.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.moodtoday.db.DBHelper;
import com.moodtoday.R;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPass;
    Button btnJoin, btnLogin;
    DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DBHelper(LoginActivity.this);

        edtEmail = findViewById(R.id.edt_email);
        edtPass = findViewById(R.id.edt_password);
        btnJoin = findViewById(R.id.btn_join);
        btnLogin = findViewById(R.id.btn_login);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = edtEmail.getText().toString();
                if (strEmail.replace(" ", "").equals("")) {
                    Toast.makeText(LoginActivity.this, "이메일 주소를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String strPass = edtPass.getText().toString();
                if (strPass.replace(" ", "").equals("")) {
                    Toast.makeText(LoginActivity.this, "패스워드를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                /*MemberDTO member = dbHelper.selectMemberTable(strEmail, strPass);

                if (strEmail.equals(member.getEmail()) && strPass.equals(member.getPassword())) {
                    Toast.makeText(LoginActivity.this, "로그인 성공.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("email", strEmail);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "이메일 또는 패스워드를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                int count = dbHelper.selectMemberTable(strEmail, strPass);
                //int count = dbHelper.selectMemberTable(strEmail);

                if(count > 0 ) {
                    Toast.makeText(LoginActivity.this, "로그인 성공.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("email", strEmail);
                    startActivity(intent);
                    finish();
                }  else {
                    Toast.makeText(LoginActivity.this, "이메일 또는 패스워드를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
