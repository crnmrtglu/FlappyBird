package com.example.flappybird;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class OyunView extends View {
    private Bird bird;
    private Handler handler;
    private Runnable run;
    private ArrayList<Pipe> arrPipes;
    private int sumpipe,distance;
    private int score,highscore=0;
    private boolean start;
    private Context context;

    public OyunView(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);
        this.context=context;
        SharedPreferences sp= context.getSharedPreferences("gamesetting",Context.MODE_PRIVATE);
        if(sp!=null){
            highscore=sp.getInt("high score",0);

        }
        score=0;

        start=false;
        initBird();
        initPipe();
        handler = new Handler();
        run = new Runnable() {
            public void run() {
                invalidate();
            }
        };
    }

    private void initPipe() {
        sumpipe = 6;
        distance = 600 * Constants.SCREEN_HEIGHT / 1920;
        arrPipes = new ArrayList<>();

        for (int i = 0; i < sumpipe; i++) {
            Pipe pipe;
            Bitmap pipeBitmap;

            int pipeX = Constants.SCREEN_WIDTH + i * ((Constants.SCREEN_WIDTH + 200 * Constants.SCREEN_WIDTH / 1080) / sumpipe);

            if (i % 2 == 0) {
                // Çift indeksli boruları alt borular olarak oluştur
                pipe = new Pipe(pipeX, Constants.SCREEN_HEIGHT / 2 + distance / 2, 200 * Constants.SCREEN_WIDTH / 1080, Constants.SCREEN_HEIGHT / 2);
                pipeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pipe1);
            } else {
                // Tek indeksli boruları üst borular olarak oluştur
                pipe = new Pipe(pipeX, Constants.SCREEN_HEIGHT / 2 - distance / 2 - Constants.SCREEN_HEIGHT / 2, 200 * Constants.SCREEN_WIDTH / 1080, Constants.SCREEN_HEIGHT / 2);
                pipeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pipe2);
                pipe.randomY();
            }

            pipe.setBm(pipeBitmap);
            arrPipes.add(pipe);
        }
    }


    private void initBird() {
        bird = new Bird();

        // Ekran boyutlarını Constants sınıfından alarak kullanma
        int screenWidth = Constants.SCREEN_WIDTH;
        int screenHeight = Constants.SCREEN_HEIGHT;

        // Kuşun ekranın ortasında olması için kuşun konumunu belirliyoruz
        int birdWidth = 100 * screenWidth / 1080;
        int birdHeight = 100 * screenHeight / 1920;
        int birdX = screenWidth / 2 - birdWidth / 2;
        int birdY = screenHeight / 2 - birdHeight / 2;

        bird.setWidth(birdWidth);
        bird.setHeight(birdHeight);
        bird.setX(birdX);
        bird.setY(birdY);

        ArrayList<Bitmap> arrbm = new ArrayList<>();
        arrbm.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird1));
        arrbm.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird2));
        bird.setArrbm(arrbm);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(start){
            bird.draw(canvas);

            // Boruları çiz
            for (int i = 0; i < arrPipes.size(); i++) {
                if(bird.getRect().intersect(arrPipes.get(i).getRect())|| bird.getY()-bird.getHeight()<0|| bird.getY()>Constants.SCREEN_HEIGHT){
                    Pipe.speed=0;
                    MainActivity.txt_score_over.setText(MainActivity.txt_score_over.getText());
                    MainActivity.txt_high_score.setText("Highest Score: " +highscore);
                    MainActivity.score_txt.setVisibility(INVISIBLE);
                    MainActivity.rl_game_over.setVisibility(VISIBLE);
                }
                Pipe pipe = arrPipes.get(i);
                if (this.bird.getX() + this.bird.getWidth() > pipe.getX() && this.bird.getX() + this.bird.getWidth() <= pipe.getX() + Pipe.speed && i < sumpipe / 2) {
                    score++;
                    if(score>highscore){
                        highscore=score;
                        SharedPreferences sp=context.getSharedPreferences("gamesetting",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putInt("high score",highscore);
                        editor.apply();
                    }
                    MainActivity.score_txt.setText("" + score);
                }
                if (pipe.getX() < -pipe.getWidth()) {
                    // Boru ekranın solunda ise sağ tarafa al ve rastgele Y konumunu ayarla
                    pipe.setX(Constants.SCREEN_WIDTH);
                    if (i < sumpipe / 2) {
                        pipe.randomY();
                    } else {
                        // İkinci yarısı için önceki borunun altına yerleştir
                        int prevPipeIndex = i - sumpipe / 2;
                        pipe.setY(arrPipes.get(prevPipeIndex).getY() + arrPipes.get(prevPipeIndex).getHeight() + distance);
                    }
                }
                // Boruyu çiz
                pipe.draw(canvas);
            }

        }
        else{
            if(bird.getY()>Constants.SCREEN_HEIGHT/2){
                bird.setDrop(-15*Constants.SCREEN_HEIGHT/1920);
            }
            bird.draw(canvas);
        }

        // Handler ile sürekli güncelleme sağla
        handler.postDelayed(run, 10);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        // Çift tıklamada klavyenin çıkmaması için
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getPointerCount() == 2) {
                // Çift tıklama olduğunda klavyeyi açma
                return true;
            }
            bird.setDrop(-15);
        }
        return true;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public void reset() {
        MainActivity.score_txt.setText("0");
        score=0;

        initPipe();
        initBird();
    }
}