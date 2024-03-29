package com.example.flappybird;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static TextView score_txt,txt_score_over,txt_high_score;
    public static RelativeLayout rl_game_over;
    private static Button btn_basla;
    private OyunView gv;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics dm=new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_HEIGHT=dm.heightPixels;
        Constants.SCREEN_WIDTH=dm.widthPixels;

        setContentView(R.layout.activity_main); // Bu satırdan önce score_txt'ye erişim sağla
        score_txt=findViewById(R.id.score_txt); // score_txt'ye erişim sağla
        txt_score_over=findViewById(R.id.txt_score_over);
        txt_high_score=findViewById(R.id.txt_high_score);
        rl_game_over=findViewById(R.id.rl_game_over);
        gv=findViewById(R.id.gv);
        btn_basla=findViewById(R.id.btn_basla);
        btn_basla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gv.setStart(true);
                score_txt.setVisibility(View.VISIBLE);
                btn_basla.setVisibility(View.INVISIBLE);

            }
        });
        rl_game_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_basla.setVisibility(View.VISIBLE);
                rl_game_over.setVisibility(View.INVISIBLE);
                gv.setStart(false);
                gv.reset();

            }
        });
    }
}