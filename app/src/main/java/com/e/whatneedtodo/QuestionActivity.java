package com.e.whatneedtodo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class QuestionActivity extends Activity {
    Button mButtonyes;
    Button mButtonno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //設定Yes鍵按下時的反應
        mButtonyes=findViewById(R.id.buttonYes);
        mButtonyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QuestionActivity.this,"Great! ",Toast.LENGTH_SHORT).show();
                QuestionActivity.this.finish();
            }
        });

        //設定No鍵按下時的反應
        mButtonno=findViewById(R.id.buttonNo);
        mButtonno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QuestionActivity.this,"Oops! To do it ASAP~  ",Toast.LENGTH_SHORT).show();
                QuestionActivity.this.finish();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Music.play(this);}

    @Override
    protected void onPause() {
        super.onPause();
        Music.stop(this);
    }

    @Override
    public void finish() {
        Music.stop(this);
        super.finish();
    }
}
