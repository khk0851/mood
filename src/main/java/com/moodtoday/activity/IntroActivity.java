package com.moodtoday.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.moodtoday.R;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ImageView iv1 = findViewById(R.id.iv1);
        ImageView iv2 = findViewById(R.id.iv2);
        ImageView iv3 = findViewById(R.id.iv3);
        ImageView iv4 = findViewById(R.id.iv4);
        ImageView iv5 = findViewById(R.id.iv5);

        Animation shake1 = AnimationUtils.loadAnimation(IntroActivity.this, R.anim.shake1);
        Animation shake2 = AnimationUtils.loadAnimation(IntroActivity.this, R.anim.shake2);
        Animation shake3 = AnimationUtils.loadAnimation(IntroActivity.this, R.anim.shake3);
        Animation shake4 = AnimationUtils.loadAnimation(IntroActivity.this, R.anim.shake4);
        Animation shake5 = AnimationUtils.loadAnimation(IntroActivity.this, R.anim.shake5);

        iv1.setAnimation(shake1);
        iv2.setAnimation(shake2);
        iv3.setAnimation(shake3);
        iv4.setAnimation(shake4);
        iv5.setAnimation(shake5);

        Handler handler = new Handler();
        handler.postDelayed(new splashHandler(), 2000);
    }

    private class splashHandler implements Runnable {

        @Override
        public void run() {
            startActivity(new Intent(getApplication(), LoginActivity.class));
            IntroActivity.this.finish();
        }
    }
}
