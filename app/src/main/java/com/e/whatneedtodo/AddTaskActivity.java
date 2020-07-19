package com.e.whatneedtodo;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.e.whatneedtodo.data.TaskContract;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

    public class AddTaskActivity extends AppCompatActivity {

    // Declare a member variable to keep track of a task's selected mImportance
    RatingBar mRatingBar;
    int mImportance=0;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mRatingBar = findViewById(R.id.ratingbar1);

        //Sets the listener to be called when the rating changes.
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
             mImportance=(int) mRatingBar.getRating();
            }
        });


    }


    /**
     * onClickAddTask is called when the "ADD" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    public void onClickAddTask(View view) {

        // Check if EditText is empty, if not retrieve input and store it in a ContentValues object
        // If the EditText input is empty -> don't create an entry
        String input = ((EditText) findViewById(R.id.editTextTaskDescription)).getText().toString();
        String deadline = ((EditText) findViewById(R.id.editTextDeadline)).getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
       //To verify whether user's input format is correct.
        try {
            //Tell whether date/time parsing is to be lenient.
            format.setLenient(false);
            //A Date parsed from the string.
            Date date = format.parse( deadline);
            System.out.println(date);
            //if date is  after current or no input, doing date transformation
            if((new Date().after(date))|| deadline.length() ==0)
                return;
        } catch (ParseException e) {
            System.out.println("日期解析失败！"+e.getMessage());
            return;
        }

        if (input.length() == 0) {
            return;
        }

        // Insert new task data via a ContentResolver
        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();
        // Put the task description a, selected mImportance and enter the task's deadline into the ContentValues
        contentValues.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, input);
        contentValues.put(TaskContract.TaskEntry.COLUMN_IMPORTANCE, mImportance);
        contentValues.put(TaskContract.TaskEntry.COLUMN_DEADLINE,deadline);
        Log.d("TEST", contentValues.getAsString(TaskContract.TaskEntry.COLUMN_DEADLINE));

        // Insert the content values via a ContentResolver
        Uri uri = getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, contentValues);

        // Finish activity (this returns back to MainActivity)
        finish();
    }

    private Date stringToDate(String aDate,String aFormat) {

        //if no date input, return null
        if(aDate==null) return null;
        // keep track of the current position during parsing
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }

    //Play the background music
    @Override
    protected void onResume() {
        super.onResume();
        Music.play(this);}

    //Stop the background music
    @Override
    protected void onPause() {
        super.onPause();
        Music.stop(this);
    }

    //Stop the background music
    @Override
    public void finish() {
        Music.stop(this);
        super.finish();
    }
}