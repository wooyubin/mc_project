package com.cookandroid.mc_project_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button button;     //저장버튼
    DatePicker datePicker;  // 날짜를 선택하는 위젯
    EditText editText;  // 글을 기록하는 부분
    String filename;  //파일 입출력을 위해 저장할 파일이름

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("일기장");

        button = findViewById(R.id.button);
        datePicker = findViewById(R.id.datepicker);
        editText = findViewById(R.id.edit_text);


        // datePicker를 현재날짜로 초기화해주기 위해 오늘의 년, 월, 일을 받아온다.
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);


        // datepicker를 오늘의 날짜로 초기값을 정해준다.
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {

            // datepicker에서 날짜가 바뀔때마다 파일이름을 정해준다
            // readDiary메소드를 통해 파일이 존재하면 파일의 내용을 가져오고
            // 그렇지 않다면 null을 가져온다.
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {

                filename = Integer.toString(i) + "_" + Integer.toString(i1) + "_" + Integer.toString(i2);
                String str = readDiary(filename);
                editText.setText(str);
                button.setEnabled(true);


            }
        });


        //파일 생성, 쓰기단계
        //editText에 적혀있는 내용의 byte값을 가져와
        //파일에 쓰기한다.

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FileOutputStream outFs = openFileOutput(filename, Context.MODE_PRIVATE);
                    String str = editText.getText().toString();
                    outFs.write(str.getBytes());
                    outFs.close();
                    Toast.makeText(MainActivity.this, filename + "이 저장", Toast.LENGTH_SHORT).show();
                    button.setText("수정하기");

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });



    }

    // 파일 읽기 단계
    // 기록을 하지 않은 경우도 있으니 파일이 없는 경우도 있다.
    // try catch를 통해 파일이 없는경우는 Hint를 띄워준다.
    String readDiary(String filename){

        String diaryStr = null;
        FileInputStream inFs;
        try {  //파일이 있는경우
            inFs = openFileInput(filename);

            byte[] txt = new byte[500];
            inFs.read(txt);
            inFs.close();
            diaryStr = (new String(txt)).trim();
            button.setText("수정하기");


        }catch (IOException e){  // 파일이 없는 경우
            editText.setHint("일기 없음");
            button.setText("새로 저장");

        }

        return diaryStr;
    }
}