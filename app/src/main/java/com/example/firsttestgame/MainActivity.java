package com.example.firsttestgame;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(this);

    }
    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(0, 0);
    }

    //ボタンが押された時の処理
    public void onClick(View view){
        Intent intent = new Intent(this, GameActivity.class);  //インテントの作成
        startActivity(intent);                                 //画面遷移
        overridePendingTransition(0, 0);
    }
}
