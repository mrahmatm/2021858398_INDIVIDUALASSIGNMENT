package com.example.a2021858398_individualassignment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class QuadColumn_ListAdapter extends ArrayAdapter<Entry> {
    private LayoutInflater mInflater;
    private ArrayList<Entry> entries;
    private  int mViewResourceId;


    public QuadColumn_ListAdapter(Context context, int textViewResourceId, ArrayList<Entry> entries1){
        super(context, textViewResourceId, entries1);
        entries = entries1;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parents){
        convertView = mInflater.inflate(mViewResourceId, null);

        Entry entry = entries.get(position);

        if(entry != null){
            TextView col0 = (TextView) convertView.findViewById(R.id.col0);
            TextView col1 = (TextView) convertView.findViewById(R.id.col1);
            TextView col2 = (TextView) convertView.findViewById(R.id.col2);
            TextView col3 = (TextView) convertView.findViewById(R.id.col3);

            if(col0 != null){
                col0.setText(String.valueOf(entry.getID()));
            }

            if(col1 != null){
                col1.setText(String.format("%.2f", entry.getWeight()));
            }

            if(col2 != null){
                col2.setText(String.format("%.2f", entry.getHeight()));
            }

            if(col3 != null){
                col3.setText(String.format("%.2f", entry.getBMI()));
            }


        }

        return  convertView;
    }
}
