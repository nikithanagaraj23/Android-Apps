package com.example.nikki.powerescue;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Nikki on 13-10-2014.
 */
public class Contacts  extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    EditText mText,mText2;
    Button mAdd;
    ListView mList;

    com.example.nikki.powerescue.MyDbHelper mHelper;
    SQLiteDatabase mDb;
    Cursor mCursor;
    SimpleCursorAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_activity);


        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");

        mText = (EditText)findViewById(R.id.name);
        mText2 = (EditText)findViewById(R.id.phone);

        mText.setText(name);
        mText2.setText(phone);

        mAdd = (Button)findViewById(R.id.add);
        mAdd.setOnClickListener(this);
        mList = (ListView)findViewById(R.id.list);
        mList.setOnItemClickListener(this);

        mHelper = new com.example.nikki.powerescue.MyDbHelper(this);   //setting the database
    }

    @Override
    public void onResume() {
        super.onResume();
        mDb = mHelper.getWritableDatabase();
        String[] columns = new String[] {"_id", com.example.nikki.powerescue.MyDbHelper.COL_NAME, com.example.nikki.powerescue.MyDbHelper.COL_DATE};
        mCursor = mDb.query(com.example.nikki.powerescue.MyDbHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        String[] headers = new String[] {com.example.nikki.powerescue.MyDbHelper.COL_NAME, com.example.nikki.powerescue.MyDbHelper.COL_DATE};
        mAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                mCursor, headers, new int[]{android.R.id.text1, android.R.id.text2});
        mList.setAdapter(mAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        mDb.close();
        mCursor.close();
    }
    @Override
    public void onClick(View v) {
        ContentValues cv = new ContentValues(2);
        cv.put(com.example.nikki.powerescue.MyDbHelper.COL_NAME, mText.getText().toString());
        cv.put(com.example.nikki.powerescue.MyDbHelper.COL_DATE, mText2.getText().toString());

      //  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     //   cv.put(com.example.special_topic.app.MyDbHelper.COL_DATE, dateFormat.format(new Date())); //Insert 'now' as the date
        mDb.insert(com.example.nikki.powerescue.MyDbHelper.TABLE_NAME, null, cv);
        mCursor.requery();
        mAdapter.notifyDataSetChanged();
        mText.setText(null);
        mText2.setText(null);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        mCursor.moveToPosition(position);
        String rowId = mCursor.getString(0); //Column 0 of the cursor is the id
        mDb.delete(com.example.nikki.powerescue.MyDbHelper.TABLE_NAME, "_id = ?", new String[]{rowId});
        mCursor.requery();
        mAdapter.notifyDataSetChanged();
    }

}
