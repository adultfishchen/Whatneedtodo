package com.e.whatneedtodo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.e.whatneedtodo.data.TaskContract;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateTaskActivity extends AppCompatActivity {

    RatingBar mRatingBar;
    int mImportance=0;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        mRatingBar = findViewById(R.id.ratingbar01);

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mImportance=(int) mRatingBar.getRating();
            }
        });

        //To query data from the database
        Intent intent =getIntent();
        String id = intent.getStringExtra(Intent.EXTRA_TEXT);
        Cursor cursor =  getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,null, TaskContract.TaskEntry._ID + "="+id,null,null);

        cursor.moveToFirst();

        int idIndex = cursor.getColumnIndex(TaskContract.TaskEntry._ID);
        int importanceIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_IMPORTANCE);
        int descriptionIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION);
        int deadlineIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DEADLINE);

        String description = cursor.getString(descriptionIndex);
        int importance = cursor.getInt(importanceIndex);
        String deadline = cursor.getString(deadlineIndex);

        TextView tv01 = (TextView)findViewById(R.id.tv01);
        tv01.setText("Task : "+description);

        RatingBar rb = (RatingBar)findViewById(R.id.ratingbar01);
        rb.setRating(importance);

        EditText editText = (EditText)findViewById(R.id.editTextDeadline1);
        editText.setText(deadline);


    }


    /**
     * onClickUpdateTask is called when the "UPDATE" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    public void onClickUpdateTask(View view) {

        // Check if EditText is empty, if not retrieve input and store it in a ContentValues object
        // If the EditText input is empty -> don't create an entry
        Intent intent =getIntent();
        String id = intent.getStringExtra(Intent.EXTRA_TEXT);
        Cursor cursor =  getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,null, TaskContract.TaskEntry._ID + "="+id,null,null);

        //Set date format
        String deadline = ((EditText) findViewById(R.id.editTextDeadline1)).getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try {
           format.setLenient(false);
           Date date = format.parse( deadline);
            System.out.println(date);
            if((new Date().after(date))|| deadline.length() ==0)
                return;
        } catch (ParseException e) {
            System.out.println("日期解析失败！"+e.getMessage());
            return;
        }



        // Insert new task data via a ContentResolver
        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();
        // Put the task description , selected mImportance, and deadline into the ContentValues
        contentValues.put(TaskContract.TaskEntry.COLUMN_IMPORTANCE, mImportance);
        contentValues.put(TaskContract.TaskEntry.COLUMN_DEADLINE,deadline);

        // Update the content values via a ContentResolver
        int update = getContentResolver().update(TaskContract.TaskEntry.CONTENT_URI,contentValues,TaskContract.TaskEntry._ID + "="+id,null);

        if(update > 0){
            Toast.makeText(this, "修改成功", Toast.LENGTH_LONG).show();
            finish();
        }
        else{
            Toast.makeText(this, "修改資料，請確認資料是否", Toast.LENGTH_LONG).show();
        }


    }

    private Date stringToDate(String aDate,String aFormat) {

        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

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



