package com.example.a2021858398_individualassignment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListEntry extends AppCompatActivity {

    private static final String TAG = "ListEntry";
    DatabaseHelper DBH;
    private ListView LV;

    ArrayList<Entry> entryList;
    Entry entry;

    private ArrayAdapter adapter1;

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        DBH = new DatabaseHelper(this);

        entryList = new ArrayList<>();

        Cursor data = DBH.getData();

        int numRows = data.getCount();

        if(numRows == 0){
            showToast("No data recorded!");
        }else{
            int n =0;
            while(data.moveToNext()){
                entry = new Entry (data.getInt(0), data.getDouble(1), data.getDouble(2), data.getDouble(3));
                entryList.add(n, entry);

                n++;
            }

            LV = (ListView) findViewById(R.id.listview);
            QuadColumn_ListAdapter adapter = new QuadColumn_ListAdapter(this, R.layout.list_adapter, entryList);
            LV = (ListView)findViewById(R.id.listview);
            LV.setAdapter(adapter);

            LV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    Entry currentTarget = (Entry) parent.getItemAtPosition(position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(ListEntry.this);
                    builder.setCancelable(true);
                    builder.setTitle("Item Options");
                    builder.setMessage("What would you like to do? (ID: " + String.valueOf(currentTarget.getID()) + ")");
                    builder.setPositiveButton("Delete",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DBH = new DatabaseHelper(ListEntry.this);
                                    DBH.deleteRow(String.valueOf(currentTarget.getID()));
                                    showToast("Deleted!");

                                    finish();
                                    startActivity(getIntent());
                                }
                            });
                    builder.setNegativeButton("Load", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showToast("Data Loaded!");
                            Entry currentTarget = (Entry) parent.getItemAtPosition(position);
                            Log.d("CLICK", "CLICKED ON A ROW");
                            //onBackPressed();
                            Intent i = new Intent(ListEntry.this, MainActivity.class);

                            i.putExtra("weight", String.valueOf(currentTarget.getWeight()));
                            i.putExtra("height", String.valueOf(currentTarget.getHeight()));
                            i.putExtra("BMI", String.valueOf(currentTarget.getBMI()));

                            startActivity(i);
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return false;
                }
            });

        }
    }

}
