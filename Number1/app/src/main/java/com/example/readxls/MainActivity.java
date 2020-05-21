
package com.example.readxls;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
 
public class MainActivity extends AppCompatActivity {


    TextView indexTV, koreanTV, chineseTV, englishTV;

    //NumberInfo 객체의 배열 선언
    NumberInfo[] numberInfos;
    //현재 몇번째인지 확인할 int 선언
    int nowIndex;
    int colorIndex;

    int centerWidth;

    String[] strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //처음 시작은 0이기 때문에 0
        nowIndex = 0;
        colorIndex = 101;

        strings = getString(R.string.color_list).split("#");

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        centerWidth = displayMetrics.widthPixels / 2;

        indexTV = (TextView) findViewById(R.id.index);
        koreanTV = (TextView) findViewById(R.id.korean);
        chineseTV = (TextView) findViewById(R.id.chinese);
        englishTV = (TextView) findViewById(R.id.english);

        // 넘버인포배열을 초기화, 배열의 길이는 엑셀 파일이 102번째까지 있어서 102로함
        numberInfos = new NumberInfo[102];
        // 반복문 돌면서 객체 하나하나 초기화했음.
        for (int i = 0; i < numberInfos.length; i++) {
            numberInfos[i] = new NumberInfo("", "", "", "");
        }
//        Excel();

        csv();


        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.touch);

        indexTV.setText(numberInfos[nowIndex].index);
        koreanTV.setText(numberInfos[nowIndex].korean);
        chineseTV.setText(numberInfos[nowIndex].chinese);
        englishTV.setText(numberInfos[nowIndex].english);
        if (colorIndex != 0){
            linearLayout.setBackgroundColor(Color.parseColor("#"+strings[colorIndex]));
        }

        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // 터치가 다운됐을때만 작동하도록
                if (event.getAction() == event.ACTION_DOWN) {

                    if (nowIndex == 101){
                        Intent intent = new Intent(MainActivity.this, finishActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (event.getX()>= centerWidth && nowIndex < 101){
                        nowIndex++;
                        colorIndex--;
                    }else if (event.getX() < centerWidth && nowIndex > 0){
                        nowIndex--;
                        colorIndex++;
                    }
                    //터치하면 다음번호로 넘어감
//                    nowIndex++;
                    //배열의 nowindex번째 객체의 값들
                    indexTV.setText(numberInfos[nowIndex].index);
                    koreanTV.setText(numberInfos[nowIndex].korean);
                    chineseTV.setText(numberInfos[nowIndex].chinese);
                    englishTV.setText(numberInfos[nowIndex].english);
                    if (colorIndex != 0){
                        linearLayout.setBackgroundColor(Color.parseColor("#"+strings[colorIndex]));
                    }
                }
                return true;
            }
        });


        //추가로 보내주는 파일은 numbers.csv파일은 assets폴더에,
        //OnCreate 메서드에서 Excel()을 실행했던건 주석처리하고 csv()를 추가해준다.


//        public void Excel() {
//
//            // 엑셀파일을 읽어서 저장하기 위한 객체 생성
//            Workbook workbook = null;
//            Sheet sheet = null;
//
//            try {
//                //에셋폴더에서 numbers.xls 파일 읽기
//                InputStream inputStream = getBaseContext().getResources().getAssets().open("numbers.xls");
//                workbook = Workbook.getWorkbook(inputStream);
//
//                //엑셀 파일에서 0번째 시트를 읽음r
//                sheet = workbook.getSheet(0);
//
//                //row = 세로
//                //column = 가로
//
//                // 시작과 끝점 설정
//                // getColumn 메서드는 (int) 번째 가로줄의 셀이 담긴 배열을 반환한다.
//                // 따라서 RowEnd는 배열의 길이가 된다. (길이는 3이지만 배열상으론 0,1,2번째 값이기 떄문에 -1)
//                int Column = 0,
//                        RowStart = 1, RowEnd = sheet.getColumn(0).length;
//
//                // row의 시작점부터 row의 끝까지 반복
//                for (int row = 0; row < RowEnd; row++) {
//                    //getCell(가로,세로)는 가로 세로번째에 있는 셀 반환
//                    //셀에서 getContents()로 셀 안의 내용을 가져와 String변수에 저장
////                String index = sheet.getCell(Column, row).getContents();
//                    //Column이 0이면 숫자 1이면 한글 2면 한자 3이면 영어
////                String english=  sheet.getCell(Column+1,row).getContents();
//                    //이 부분은 리스트뷰에 추가하는 부분이기 떄문에 필요없음. 우리는 클래스 값에다 넣어주면 될듯
//
//                    //for문 row번째 객체의 값들에 엑셀의 값들 넣어주기
//                    numberInfos[row].index = sheet.getCell(0, row).getContents();
//                    numberInfos[row].korean = sheet.getCell(1, row).getContents();
//                    numberInfos[row].chinese = sheet.getCell(2, row).getContents();
//                    numberInfos[row].english = sheet.getCell(3, row).getContents();
//
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (BiffException e) {
//                e.printStackTrace();
//            } finally {
//                //이 부분도 마찬가지로 리스트뷰
//                // 끝나면 워크북을 소멸
//                workbook.close();
//            }
//        }

    }
    public void csv(){
        BufferedReader bufferedReader = null;
        try {

            //bufferedReader 가 글자 하나하나 읽어주는거라 인코딩이 잘되는듯
            bufferedReader = new BufferedReader(new InputStreamReader(getResources().getAssets().open("numbers.csv")));
            //CSVReader를 사용하려면 build.gradle에서 dependencies에 compile 'com.opencsv:opencsv:3.9'을 추가해줘야함. 추가하고 sync now
            CSVReader csvReader = new CSVReader(bufferedReader);

            String[] record = null;
            //반복문을 돌면서 readNext를 실행한다. 이는 csvReader의 기능을 활용해 한줄 한줄 내려가며 읽는것이며
            //readNext는 한줄에 담긴 숫자,한글,한자,영어를 배열에 담아 반환한다. 반환한 값은 String배열 record에 차례대로 담긴다.
            //record가 null이 아닐때까지 반복이니 null이 되면 반복문 끝
            for (int i = 0; (record = csvReader.readNext()) != null; i++) {

                //반복문에서 i가 csv파일의 한줄이 되니 NumberInfo[i]로 할수있다.
                numberInfos[i].index = record[0];
                numberInfos[i].korean = record[1];
                numberInfos[i].chinese = record[2];
                numberInfos[i].english = record[3];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //추가로 보내주는 파일은 numbers.csv파일은 assets폴더에,
        //OnCreate 메서드에서 Excel()을 실행했던건 주석처리하고 csv()를 추가해준다.

    }
}