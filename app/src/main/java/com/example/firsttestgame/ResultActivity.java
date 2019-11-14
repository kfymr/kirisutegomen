package com.example.firsttestgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        findViewById(R.id.titleButton).setOnClickListener(this);

        TextView resultNum = (TextView) findViewById(R.id.resultNum);

        Intent intent = getIntent();
        Bundle resultData = intent.getExtras();
        String gameCount = resultData.getString("result_game_count");

        resultNum.setText("あなたの記録：" + gameCount + "戦");
    }
    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(0, 0);
    }

    //ボタンが押された時の処理
    public void onClick(View view){

        Intent intent = new Intent(this, MainActivity.class);  //インテントの作成
        startActivity(intent);                                 //画面遷移
        overridePendingTransition(0, 0);
    }
}
