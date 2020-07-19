package com.e.whatneedtodo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.e.whatneedtodo.data.TaskContract;



public class ShowDetailedTaskActivity extends AppCompatActivity {




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detailedtask);
        //To load data from the database into view
        LoadData();

        Button mButton=findViewById(R.id.button1);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent updateTaskIntent = new Intent(ShowDetailedTaskActivity.this, UpdateTaskActivity.class);
                Log.d("TEST", "onClick: " + getIntent().getStringExtra(Intent.EXTRA_TEXT));
                updateTaskIntent.putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra(Intent.EXTRA_TEXT) );
                startActivity(updateTaskIntent);
            }
        });
    }

    @Override
    protected void onResume() {
            super.onResume();
            Music.play(this);
            LoadData();
    }

    //The method to load data from the database
    private void LoadData(){
        Intent intent =getIntent();
        String id = intent.getStringExtra(Intent.EXTRA_TEXT);
        //To query data from the database
        Cursor cursor =  getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,null, TaskContract.TaskEntry._ID + "="+id,null,null);

        int idIndex = cursor.getColumnIndex(TaskContract.TaskEntry._ID);
        int descriptionIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION);
        int importanceIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_IMPORTANCE);
        int deadlineIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DEADLINE);
        cursor.moveToFirst(); //Move the cursor to the first row.

        //To input data into right place
        String description = cursor.getString(descriptionIndex);
        int importance = cursor.getInt(importanceIndex);
        String deadline = cursor.getString(deadlineIndex);
        TextView tv1 = (TextView)findViewById(R.id.tv1);
        TextView tv3 = (TextView)findViewById(R.id.tv3);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingbar1);
        tv1.setText("Task : "+description);
        ratingBar.setRating(importance);
        tv3.setText("Deadline : "+deadline);
    }

    //Stop the music
    @Override
    protected void onPause() {
        super.onPause();
        Music.stop(this);
    }

    //Stop the music
    @Override
    public void finish() {
        Music.stop(this);
        super.finish();
    }
}

