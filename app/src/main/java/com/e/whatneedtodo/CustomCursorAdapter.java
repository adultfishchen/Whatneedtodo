package com.e.whatneedtodo;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e.whatneedtodo.data.TaskContract;


public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.TaskViewHolder> {

    // Class variables for the Cursor that holds task data and the Context
    private Cursor mCursor;
    private Context mContext;


    /**
     * Constructor for the CustomCursorAdapter that initializes the Context.
     *
     * @param mContext the current Context
     */
    public CustomCursorAdapter(Context mContext) {
        this.mContext = mContext;
    }


    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TaskViewHolder that holds the view for each task
     */
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.task_layout, parent, false);

        return new TaskViewHolder(view);
    }


    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(final TaskViewHolder holder,final int position) {

        // Indices for the _id, description, importance, and deadline columns
        int idIndex = mCursor.getColumnIndex(TaskContract.TaskEntry._ID);
        int descriptionIndex = mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION);
        int importanceIndex = mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_IMPORTANCE);
        int deadlineIndex =mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DEADLINE);
        mCursor.moveToPosition(position); // get to the right location in the cursor

        // Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        String description = mCursor.getString(descriptionIndex);
        int importance = mCursor.getInt(importanceIndex);
        String deadline = mCursor.getString(deadlineIndex);
        //Set values
        holder.itemView.setTag(id);
        holder.taskDescriptionView.setText(description);
        // Programmatically set the text and color for the importance as well as deadline TextView
        String importanceString = "" + importance; // converts int to String
        holder.importanceView.setText(importanceString);
        holder.deadlineView.setText(deadline);

        GradientDrawable importanceCircle = (GradientDrawable) holder.importanceView.getBackground();
        // Get the appropriate background color based on the importance
        int importanceColor = getImportanceColor(importance);
        importanceCircle.setColor(importanceColor);

    }


    /*
    Helper method for selecting the correct importance circle color.
    5 stars = dark red, 4 stars = light red, 3 stars = orange, 2 stars = dark yellow, 1 star = light yellow
    */
    private int getImportanceColor(int importance) {
        int importanceColor = 0;

        switch (importance) {
            case 1:
                importanceColor = ContextCompat.getColor(mContext, R.color.materialLightYellow);
                break;
            case 2:
                importanceColor = ContextCompat.getColor(mContext, R.color.materialDarkYellow);
                break;
            case 3:
                importanceColor = ContextCompat.getColor(mContext, R.color.materialOrange);
                break;
            case 4:
                importanceColor = ContextCompat.getColor(mContext, R.color.materialLightRed);
                break;
            case 5:
                importanceColor = ContextCompat.getColor(mContext, R.color.materialDarkRed);
                break;
            default:
                break;
        }
        return importanceColor;
    }


    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


    // Inner class for creating ViewHolders
    class TaskViewHolder extends RecyclerView.ViewHolder {

        // Class variables for the task description,  importance TextViews, and deadline TextViews
        TextView taskDescriptionView;
        TextView importanceView;
        TextView deadlineView;
        /**
         * Constructor for the TaskViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public TaskViewHolder(View itemView) {
            super(itemView);

            taskDescriptionView = (TextView) itemView.findViewById(R.id.taskDescription);
            importanceView = (TextView) itemView.findViewById(R.id.importanceTextView);
            deadlineView = (TextView)  itemView.findViewById(R.id.taskDeadline);
        }
    }

}