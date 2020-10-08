package com.moodtoday.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.moodtoday.AppDefine;
import com.moodtoday.R;
import com.moodtoday.db.DBHelper;
import com.moodtoday.dto.DiaryDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LastActivity extends AppCompatActivity {

    // 월 텍스트뷰
    private TextView tvDate;

    // 기분의 지난달
    private ImageView ivPreMood;
    private TextView tvPreMood;

    // 그리드뷰 어댑터
    private GridAdapter gridAdapter;

    // 일 저장 할 리스트
    private ArrayList<DateDTO> dayList;

    // 그리드뷰
    private GridView gridView;

    // 캘린더 변수
    private Calendar mCal;

    // 현재 월과 얼마나 차이나는지 저장하는 변수, 예) 이번달이면 0, 다음달이면 1, 이전달이면 -1
    int monthDiff = 0;

    Button btnPrev, btnNext;

    ArrayList<DiaryDTO> diaryList;
    DBHelper dbHelper;


    String email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기

        email = getIntent().getStringExtra("email");

        dbHelper = new DBHelper(LastActivity.this);

        diaryList = new ArrayList<>();
        diaryList = dbHelper.selectDiaryTable("", email);

        tvDate = (TextView) findViewById(R.id.tv_date);
        gridView = (GridView) findViewById(R.id.gridview);

        //gridview 요일 표시
        dayList = new ArrayList<DateDTO>();

        gridAdapter = new GridAdapter(getApplicationContext(), dayList);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DateDTO dateDTO = dayList.get(position);
                String date = dateDTO.getYear() + "-" + dateDTO.getMonth() + "-" + dateDTO.getDay();

                for(int i=0; i<diaryList.size(); i++) {
                    String diaryDate = diaryList.get(i).getDate();

                    if(date.equals(diaryDate)) {
                        showDiaryDialog(diaryList.get(i));
                    }
                }

            }
        });

        ivPreMood = findViewById(R.id.iv_premood);
        tvPreMood = findViewById(R.id.tv_premood_tag);

        getNow(0);

        btnPrev = findViewById(R.id.btn_prev);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthDiff--;
                getNow(monthDiff);
            }
        });
        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthDiff++;
                getNow(monthDiff);
            }
        });
    }

    public void getNow(int diff) {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1 + diff;
        calendar.set(year, month - 1, 1);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        //현재 날짜 텍스트뷰에 뿌려줌
        int nMonth = calendar.get(Calendar.MONTH) + 1;
        tvDate.setText(nMonth + "월");

        mCal = Calendar.getInstance();

        //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
        mCal.set(year, month - 1, 1);

        final int dayNum = calendar.get(Calendar.DAY_OF_WEEK);

        dayList.clear();

        //1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum; i++) {
            DateDTO date = new DateDTO();
            date.setYear("");
            date.setMonth("");
            date.setDay("");
            dayList.add(date);
        }

        setCalendarDate(mCal.get(Calendar.MONTH) + 1);

        //기분의 전달
        int nPreYear = year;
        int nPreMonth = nMonth - 1;
        if(nPreMonth == 0){
            nPreYear--;
            nPreMonth = 12;
        }

        String sSearchDate = nPreYear + "-" + nPreMonth;
        int nSum = 0;
        ArrayList<DiaryDTO> arrListDiary = dbHelper.selectDiaryTable(sSearchDate, email);
        if(arrListDiary == null || arrListDiary.size() < 1){
            findViewById(R.id.const_premood).setVisibility(View.GONE);
        }
        else{
            findViewById(R.id.const_premood).setVisibility(View.VISIBLE);
            for (int i = 01; i < arrListDiary.size(); i++) {
                nSum += arrListDiary.get(i).getIconNum();
            }

            int nPreMoodIdx = nSum / arrListDiary.size();
            ivPreMood.setImageResource(AppDefine.moodIconArr[nPreMoodIdx]);
            tvPreMood.setText(AppDefine.moodTextArr[nPreMoodIdx]);
        }
    }

    // 해당 월에 표시할 일 수 구함
    private void setCalendarDate(int month) {
        mCal.set(Calendar.MONTH, month - 1);

        int yyyy = mCal.get(Calendar.YEAR);
        int mm = mCal.get(Calendar.MONTH) + 1;

        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            DateDTO date = new DateDTO();
            date.setYear(yyyy + "");
            date.setMonth(mm + "");
            date.setDay("" + (i + 1));
            dayList.add(date);
        }

        if ((dayList.size() % 7) != 0) {
            int i = 7 - (dayList.size() % 7);

            for (int j = 0; j < i; j++) {
                DateDTO date = new DateDTO();
                date.setYear("");
                date.setMonth("");
                date.setDay("");
                dayList.add(date);
            }
        }
        gridAdapter.notifyDataSetChanged();
    }

    // 그리드뷰 어댑터
    public class GridAdapter extends BaseAdapter {

        private final List<DateDTO> list;
        private final LayoutInflater inflater;

        public GridAdapter(Context context, List<DateDTO> list) {
            this.list = list;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public DateDTO getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            convertView = inflater.inflate(R.layout.item_calendar_gridview, parent, false);
            holder = new ViewHolder();

            holder.tvItemCalendar = (TextView) convertView.findViewById(R.id.tv_item_calendar);
            holder.ivItemCalendar = convertView.findViewById(R.id.iv_item_calendar);

            convertView.setTag(holder);

            holder.tvItemCalendar.setText("" + list.get(position).getDay());

            for (int i = 0; i < diaryList.size(); i++) {
                String diaryDate = diaryList.get(i).getDate();
                String date = getItem(position).getYear() + "-" + getItem(position).getMonth() + "-" + getItem(position).getDay();

                if (diaryDate.equals(date)) {
                    int imgNum = diaryList.get(i).getIconNum();
                    holder.ivItemCalendar.setImageResource(AppDefine.moodIconArr[imgNum - 1]);
                }
            }

            //해당 날짜 텍스트 컬러,배경 변경
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
            String todayDate = format.format(calendar.getTime());
            String calendarDate = getItem(position).getYear() + "-" + getItem(position).getMonth() + "-" + getItem(position).getDay();

            // 년, 월, 일이 오늘과 같다면 오늘 날짜 표시
            if (todayDate.equals(calendarDate)) {
                holder.tvItemCalendar.setBackgroundColor(Color.parseColor("#A0C5FF"));
            } else {
                holder.tvItemCalendar.setBackgroundColor(Color.parseColor("#00000000"));
            }
            return convertView;
        }
    }

    public void showDiaryDialog(DiaryDTO diaryDTO) {
        final Dialog dialog = new Dialog(LastActivity.this);
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
        tvDate.setText(diaryDTO.getDate());

        //기분 아이콘
        ImageView ivIcon = dialog.findViewById(R.id.iv_icon);
        int iconNumber = diaryDTO.getIconNum();
        ivIcon.setImageResource(AppDefine.moodIconArr[iconNumber - 1]);

        //날씨 아이콘
        ImageView ivWIcon = dialog.findViewById(R.id.iv_w_icon);
        int iconWNumber = diaryDTO.getWeather();
        ivWIcon.setImageResource(AppDefine.weatherIconArr[iconWNumber - 1]);

        //기록의 오늘
        TextView tvContents = dialog.findViewById(R.id.tv_contents);
        tvContents.setText(diaryDTO.getContents());
        //음악의 오늘
        TextView tvMusic = dialog.findViewById(R.id.tv_music);
        tvMusic.setText(diaryDTO.getMusic());
        //위로의 오늘
        TextView tvWording = dialog.findViewById(R.id.tv_wording);
        tvWording.setText(diaryDTO.getWording());

        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        btnCancel.setVisibility(View.GONE);

        Button btnOk = dialog.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();      // 다이얼로그 출력
    }


    private class ViewHolder {
        TextView tvItemCalendar;
        ImageView ivItemCalendar;
    }

    public class DateDTO {
        String year;
        String month;
        String day;

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }
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
