package org.guildsa.todolist;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.guildsa.todolist.db.TaskContract;
import org.guildsa.todolist.db.TaskDBHelper;

// MainActivity inherits from many super activity classes. The highest
// super class is Activity, where all sub activity classes such as ListActivity, AppCompatActivity
// and etc. inherit.
public class MainActivity extends AppCompatActivity {
    private TaskDBHelper helper;
    private ListView listView;
    private ListAdapter listAdapter;

    // The @Override Android Studio annotation is not mandatory but is quite useful to use anyway.
    // Its purpose is to tell the compiler that we're overriding a method from a class we're
    // inheriting from. Once the compiler knows this, it can help us identify errors in our code.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // We call super.onCreate(bundleAlias) just in case the super activity class needs to do
        // some configuring prior to our activity kicking off.
        super.onCreate(savedInstanceState);
        // This sets the activity content from a layout resource, R. The resource will be inflated,
        // adding all top-level views to the activity.
        setContentView(R.layout.main);

        /*// Code to retrieve list items from SQLite and log them to the console.
        SQLiteDatabase sqlDB = new TaskDBHelper(this).getWritableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                                    new String[]{ TaskContract.Columns.TASK },
                                    null, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            Log.d("MainActivity cursor",
                    cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.Columns.TASK)));
        }*/

        updateUI();
    }

    // This method is used to add a task menu in the top right corner.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // This method is to get a selected item from the task menu.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add a task");
                builder.setMessage("What do you want to do?");
                final EditText inputField = new EditText(this);
                builder.setView(inputField);

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String task = inputField.getText().toString();
                        Log.d("MainActivity", task);

                        helper = new TaskDBHelper(MainActivity.this);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.clear();
                        values.put(TaskContract.Columns.TASK, task);

                        db.insertWithOnConflict(TaskContract.TABLE, null, values,
                                SQLiteDatabase.CONFLICT_IGNORE);

                        updateUI();
                    }
                });

                builder.setNegativeButton("Cancel", null);

                builder.create().show();

                return true;

            default:
                return false;
        }
    }

    private void updateUI() {
        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                                    new String[] { TaskContract.Columns._ID, TaskContract.Columns.TASK },
                                    null, null, null, null, null);

        listAdapter = new SimpleCursorAdapter(this,
                                              R.layout.task_view,
                                              cursor,
                                              new String[] { TaskContract.Columns.TASK },
                                              new int[] { R.id.taskTextView },
                                              0);

        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(listAdapter);
    }

    public void onDoneButtonClick(View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
        String task = taskTextView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                                    TaskContract.TABLE,
                                    TaskContract.Columns.TASK,
                                    task);

        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();
    }

}
