package com.example.firsttestgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import java.util.Locale;
import java.util.Random;

import android.view.View;
import android.widget.ImageView;

public class GameActivity extends AppCompatActivity {

    private static final long START_TIME = 5000;
    private static final long SUB_START_TIME = 2500;

    private TextView mTextViewCountDown;
    private TextView mSubTextViewCountDown;
    private Button mButtonStartPause;
    private Button getmButtonReset;
    private Button mButtonSlash;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private boolean mSubTimerRunning;

    private long mTimeLeftInMillis = START_TIME;
    private long mSubTimeLeftInMillis = SUB_START_TIME;

    // 斬るフラグ
    private static boolean slash_flg = false;

    // ゲーム数
    private static int game_count = 0;

    // image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("mTimerRunningの初期値は？ " + mTimerRunning);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mSubTextViewCountDown = findViewById(R.id.sub_text_view_countdown);
//        mButtonStartPause = findViewById(R.id.button_start_pause);
        getmButtonReset = findViewById(R.id.button_reset);
        mButtonSlash = findViewById(R.id.button_slash);

        // 初期
        mTextViewCountDown.setVisibility(View.INVISIBLE);
        mSubTextViewCountDown.setVisibility(View.INVISIBLE);
        mButtonSlash.setVisibility(View.INVISIBLE);

        game_count = 0;
        resetTimer();
        startTimer();
        /*
        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("mTimerRunningの値は？ " + mTimerRunning);
                if (mTimerRunning) {
                    pauseTimer();
                } else if (!mSubTimerRunning) {
                    startTimer();
                }
            }
        });
        */

        mButtonSlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView view = findViewById(R.id.back);
                view.setSelected(false); // -> something_default.pngに切り替わる
                mButtonSlash.setVisibility(View.INVISIBLE);
                if (slash_flg) {
                    // 精工
                    successGame();
                } else {
                    failureGame();
                }
            }
        });

        getmButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        updateCountDownText();
        updateSubCountDownText();
    }
    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(0, 0);
    }

    private void startTimer() {

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                startSubTimer();
                mTimerRunning = false;
                slash_flg = true;
                ImageView view = findViewById(R.id.back);
                view.setSelected(true); // -> something_selected.pngに切り替わる
                // view.setSelected(false); // -> something_default.pngに切り替わる
         //       mButtonStartPause.setText("おせ！");

                getmButtonReset.setVisibility(View.INVISIBLE);
            }
        }.start();

        mTimerRunning = true;
        getmButtonReset.setVisibility(View.INVISIBLE);
        mButtonSlash.setVisibility(View.VISIBLE);
    }

    private void startSubTimer() {
        mCountDownTimer = new CountDownTimer(mSubTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mSubTimeLeftInMillis = millisUntilFinished;
                updateSubCountDownText();
            }

            @Override
            public void onFinish() {
                mSubTimerRunning = false;
                mSubTimeLeftInMillis = SUB_START_TIME;
                updateSubCountDownText();
                failureGame();
            }
        }.start();

        mTimerRunning = false;
        mSubTimerRunning = true;
    }


    private void pauseTimer() {
        System.out.println("一時停止処理前のmTimerRunningは？ " + mTimerRunning);
        mCountDownTimer.cancel();
        mTimerRunning = false;
        System.out.println("一時停止処理後のmTimerRunningは？ " + mTimerRunning);
    //    mButtonStartPause.setText("スタート");

    //    getmButtonReset.setVisibility(View.VISIBLE);
        mButtonSlash.setVisibility(View.INVISIBLE);
    }

    private void successGame() {

        mCountDownTimer.cancel();
        mTimerRunning = false;
        mSubTimerRunning = false;
        game_count++;
    //    mButtonStartPause.setText("成功！");

    //    getmButtonRes et.setVisibility(View.VISIBLE);
    //    mButtonSlash.setVisibility(View.INVISIBLE);
        findViewById(R.id.pig).setVisibility(View.INVISIBLE);

        ImageView pig_animation = findViewById(R.id.pig_animation);
        pig_animation.setBackgroundResource(R.drawable.animation_pig_victory01);
        AnimationDrawable frameAnimation = (AnimationDrawable) pig_animation.getBackground();
        // アニメーションの開始
        frameAnimation.stop(); // 念のため一度ストップ
        frameAnimation.selectDrawable(0); // 0フレーム目に戻す
        frameAnimation.setOneShot(true);
        frameAnimation.start();

        checkIfAnimationDoneVicroty(frameAnimation);

    }

    private void failureGame() {

        // 敗北演出
        ImageView view = findViewById(R.id.pig);
        view.setSelected(true); // -> something_selected.pngに切り替わる

        mCountDownTimer.cancel();
        mTimerRunning = false;
        mSubTimerRunning = false;
    //    game_count++;
    //    mButtonStartPause.setText("失敗！");

    //    getmButtonReset.setVisibility(View.VISIBLE);
        mButtonSlash.setVisibility(View.INVISIBLE);

        Intent intent = new Intent(this, ResultActivity.class);  //インテントの作成
        // String game_num = String.valueOf(game_count);
        Bundle extras = new Bundle();
        extras.putString("result_game_count",String.valueOf(game_count));
        game_count = 0;
    //    extras.putSerializable("RESULT_GAMECOUNT", game_count);
    //    int game_num = Integer.parseInt(game_count.toString());
        intent.putExtras(extras);
     //   intent.putExtra("RESULT_GAMECOUNT", String.valueOf(game_count));
        startActivity(intent);  //画面遷移
        overridePendingTransition(0, 0);
    }
    private void closeGame() {
        ImageView pig_animation = findViewById(R.id.shoji_close);
        pig_animation.setBackgroundResource(R.drawable.animation_shoji_open_close);
        AnimationDrawable animationShojiClose = (AnimationDrawable) pig_animation.getBackground();
        // アニメーションの開始
        findViewById(R.id.shoji_close).setVisibility(View.VISIBLE);
    //    animationShojiClose.selectDrawable(0); // 0フレーム目に戻す
        animationShojiClose.setOneShot(true);
        animationShojiClose.start();

        checkIfAnimationDoneShojiClose(animationShojiClose);
    }

    // shoji open
    private void openGame() {
        ImageView shoji_open_animation = findViewById(R.id.shoji_open);
        shoji_open_animation.setBackgroundResource(R.drawable.animation_shoji_close01);
        AnimationDrawable animationShojiOpen = (AnimationDrawable) shoji_open_animation.getBackground();
        // アニメーションの開始
        findViewById(R.id.shoji_open).setVisibility(View.VISIBLE);
    //    animationShojiClose.selectDrawable(0); // 0フレーム目に戻す
        animationShojiOpen.setOneShot(true);
        animationShojiOpen.start();

        checkIfAnimationDoneShojiOpen(animationShojiOpen);

     //   resetTimer();
     //   startTimer();
    }

    private void resetTimer() {
    //    mButtonStartPause.setText("スタート");
        slash_flg = false;

        Random r = new Random();
        int newTime = r.nextInt(5000) + 500;
        int newSubTime = r.nextInt(500) + 100;
        mTimeLeftInMillis = newTime;
        mSubTimeLeftInMillis = newSubTime;
        updateCountDownText();
        updateSubCountDownText();

    //    mButtonStartPause.setVisibility(View.VISIBLE);
        mButtonSlash.setVisibility(View.INVISIBLE);

        // getmButtonReset.setVisibility(View.INVISIBLE);
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timerLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timerLeftFormatted);
    }

    private void updateSubCountDownText() {
        int minutes = (int) (mSubTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mSubTimeLeftInMillis / 1000) % 60;
        String subTimerLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mSubTextViewCountDown.setText(subTimerLeftFormatted);
    }

    // animation check
    public void checkIfAnimationDoneVicroty(AnimationDrawable anim){
        final AnimationDrawable a = anim;
        int timeBetweenChecks = 500; // この値はパラパラアニメの duration と一致させる
        Handler h = new Handler();
        h.postDelayed(new Runnable(){
            public void run() {
                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)){
                    checkIfAnimationDoneVicroty(a);
                } else{
                    // アニメーションが終わったときの処理...
                    findViewById(R.id.pig).setVisibility(View.VISIBLE);
                    closeGame();

                }
            }

        }, timeBetweenChecks);
    }

    public void checkIfAnimationDoneShojiClose(AnimationDrawable anim){
        final AnimationDrawable a = anim;
        int timeBetweenChecks = 100; // この値はパラパラアニメの duration と一致させる
        Handler h = new Handler();
        h.postDelayed(new Runnable(){
            public void run(){
                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)){
                    checkIfAnimationDoneVicroty(a);

                } else{
                    // アニメーションが終わったときの処理...
                    findViewById(R.id.shoji_close).setVisibility(View.INVISIBLE);

                    ImageView pig_animation = findViewById(R.id.shoji_close);
                    pig_animation.setBackgroundResource(R.drawable.animation_shoji_open_close);
                    AnimationDrawable animationShojiClose = (AnimationDrawable) pig_animation.getBackground();
                    animationShojiClose.stop(); // 念のため一度ストップ
                 //   animationShojiClose.selectDrawable(0); // 0フレーム目に戻す

                 //   openGame();
                    resetTimer();
                    startTimer();
                }
            }

        }, timeBetweenChecks);
    }

    public void checkIfAnimationDoneShojiOpen(AnimationDrawable anim){
        final AnimationDrawable a = anim;
        int timeBetweenChecks = 80; // この値はパラパラアニメの duration と一致させる
        Handler h = new Handler();
        h.postDelayed(new Runnable(){
            public void run(){
                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)){
                    checkIfAnimationDoneVicroty(a);
                } else{
                    // アニメーションが終わったときの処理...
                    findViewById(R.id.shoji_open).setVisibility(View.INVISIBLE);

                    ImageView shoji_open_animation = findViewById(R.id.shoji_open);
                    shoji_open_animation.setBackgroundResource(R.drawable.animation_shoji_close01);
                    AnimationDrawable animationShojiOpen = (AnimationDrawable) shoji_open_animation.getBackground();
                    animationShojiOpen.stop(); // 念のため一度ストップ
                    animationShojiOpen.selectDrawable(0); // 0フレーム目に戻す

                    resetTimer();
                    startTimer();
                }
            }

        }, timeBetweenChecks);
    }
}
