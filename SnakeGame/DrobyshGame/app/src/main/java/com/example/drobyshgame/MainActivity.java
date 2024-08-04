package com.example.drobyshgame;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.time.LocalTime;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    @SuppressLint("StaticFieldLeak")
    private static MainActivity instance;

    public MutableLiveData<Result> liveData = new MutableLiveData<>();

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private TextView showScore;

    private AppCompatImageButton resButton;

    private AppCompatImageButton topButton;
    private AppCompatImageButton downButton;
    private AppCompatImageButton leftButton;
    private AppCompatImageButton rightButton;

    private int snakeColor;
    private int snakeSpeed;
    private Game game;

    FoodPoint f1, f2, f3, f4, f5, f6;

    public void updateScore(int score){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showScore.setText(String.valueOf(score));
            }
        });
    }

    public void ruinedGame(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        game.getSnakeTimer().stop();
        builder.setTitle("Game Over!");
        builder.setMessage("Your score is "+game.getScore());
        builder.setCancelable(false);
        builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                game.initGame(f1, f2, f3, f4, f5, f6);
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        instance = this;


        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);
        showScore = findViewById(R.id.score);

        game = new Game(surfaceView, showScore);

        resButton = findViewById(R.id.results);

        topButton = findViewById(R.id.up_button); // 3
        leftButton = findViewById(R.id.left_button); // 1
        rightButton = findViewById(R.id.right_button); // 4
        downButton = findViewById(R.id.down_button); // 2

        resButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.getSnakeTimer().stop();
                Intent intent = new Intent(MainActivity.this, TableResultsActivity.class);
                startActivity(intent);
            }
        });


        topButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(game.getMoveSide()!=2){
                    game.setMoveSide(3);
                }
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(game.getMoveSide()!=4){
                    game.setMoveSide(1);
                }
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(game.getMoveSide()!=1){
                    game.setMoveSide(4);
                }
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(game.getMoveSide()!=3){
                    game.setMoveSide(2);
                }
            }
        });


        liveData.observe(this, new Observer<Result>() {
            @Override
            public void onChanged(Result resultik) {
                AppDataBase db = DataApp.getApp().getDatabase();
                ResultDao resultDao = db.resultDao();

                if(resultDao.getAll().size() > 49){
                    resultDao.deleteMinimum();
                }

                Result result = new Result();
                result.duration = resultik.duration;
                result.score = resultik.score;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    result.id = LocalTime.now().getNano();
                }
                else {
                    result.id = resultDao.getAll().size();
                }
                resultDao.insert(result);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        game.setSurfaceHolder(surfaceHolder);

        f1 = new FoodPoint(0,0, 1, Color.BLUE, R.raw.hold);
        f2 = new FoodPoint(0,0, 3, Color.RED, R.raw.take);
        f3 = new FoodPoint(0, 0, 5, Color.WHITE, R.raw.rockroll);
        f4 = new FoodPoint(0,0,7, Color.GREEN, R.raw.bangarang2);
        f4 = new FoodPoint(0,0, 8, Color.GRAY, R.raw.bangarang);
        f5 = new FoodPoint(0, 0, 9, Color.parseColor("#FFA500"), R.raw.bangarang3);

        game.initGame(f1, f2, f3, f4, f5);

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    public static MainActivity getInstance(){
        return instance;
    }
}