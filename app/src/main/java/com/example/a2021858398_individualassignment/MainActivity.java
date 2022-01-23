/*

NAME: MUHAMAD RAHMAT BIN MUSTAFA
STUDENT ID: 2021858398
GROUP: CS2703B
COURSE CODE: ICT602
LECTURER: SIR ATIF RAMLAN
SESSION: OCT2021 - FEB2022
TITLE: INDIVIDUAL ASSIGNMENT - BMI CALCULATOR

 */

package com.example.a2021858398_individualassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Inet4Address;

public class MainActivity extends AppCompatActivity {

    //global variables initialization
    TextView BMI, category, healthRisk;
    EditText weight, height, inputID;
    Button go,viewEntry, load;
    double BMIResult;
    private static final String fileName = "entry.txt";

    DatabaseHelper DBH;

    //a function to insert data into the db
    public void insertData(double weight, double height, double BMI){

        Boolean insert = DBH.insertRow(String.valueOf(weight), String.valueOf(height), String.valueOf(BMI));

        if (insert)
            showToast("Data Recorded!");
        else
            showToast("Error Recording Data!");
    }

    //a function to display a toast
    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //a function to calculate the BMI
    protected double calcBMI(double weight, double height){
        return (weight / Math.pow(height, 2));
    }

    //a function to determine BMI category
    protected String getCategory(double BMI){
        String result = " ";

        if(BMI <= 18.4)
            result = "Underweight";
        else if(BMI <= 24.9)
            result = "Normal Weight";
        else if(BMI <= 29.9)
            result = "Overweight";
        else if(BMI <= 34.9)
            result = "Moderately Obese";
        else if(BMI <= 39.9)
            result = "Severely Obese";
            else
                result = "Very Severely Obese";

        return result;
    }

    //a function to determine the BMI's risk level
    protected String getRisk(double BMI){
        String result = " ";

        if(BMI <= 18.4)
            result = "Malnutrition Risk";
        else if(BMI <= 24.9)
            result = "Low Risk";
        else if(BMI <= 29.9)
            result = "Enhanced Risk";
        else if(BMI <= 34.9)
            result = "Medium Risk";
        else if(BMI <= 39.9)
            result = "High Risk";
            else
                result = "Very High Risk";

        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assigning the elements ID's with its variables
        //textview
        BMI = (TextView) findViewById(R.id.txtBMI);
        category = (TextView) findViewById(R.id.txtCategory);
        healthRisk = (TextView) findViewById(R.id.txtHealthRisk);

        //edittext
        weight = (EditText) findViewById(R.id.txtWeight);
        height = (EditText) findViewById(R.id.txtHeight);
        inputID = (EditText) findViewById(R.id.txtID);

        //button
        go = (Button) findViewById(R.id.btnCalc);
        viewEntry = (Button) findViewById(R.id.btnViewEntries);
        load = (Button)findViewById(R.id.btnLoad);

        //sqlite helper
        DBH = new DatabaseHelper(this);

        //click event listener go btnCalc
        go.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //checking the content of txtWeight and txtHeight
                if(!weight.getText().toString().isEmpty() && !height.getText().toString().isEmpty()) {
                    //if both is not null, proceed
                    BMIResult = calcBMI(Double.parseDouble(weight.getText().toString()), Double.parseDouble(height.getText().toString()));
                    BMI.setText(String.format("%.2f", BMIResult));
                    category.setText(getCategory(BMIResult));
                    healthRisk.setText(getRisk(BMIResult));

                    insertData(Double.parseDouble(weight.getText().toString()), Double.parseDouble(height.getText().toString()), BMIResult);
                }else{
                    //if either one of both is null, display a toast
                    showToast("Please enter both fields!");
                }
            }
        });

        //invoke intent to ListEntry class
        viewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListEntry.class);
                startActivity(intent);
            }
        });

        //load data from the ID input
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check wheter the edittext is empty
                if(String.valueOf(inputID.getText()).isEmpty()){
                    //if so, display toast
                    showToast("Please enter the ID!");
                } else{
                    //else, proceed with data fetching attempt from db
                    Cursor target = DBH.loadID((inputID.getText().toString()));
                    target.moveToFirst();
                    //check whether the data exists or not
                    if(target == null || target.getCount() <= 0)
                        //if it doesn't exist, display toast
                        showToast("ID not found!");
                    else {
                        //else, load the data into edittext's and textview's
                        weight.setText(String.valueOf(target.getDouble(1)));
                        height.setText(String.valueOf(target.getDouble(2)));
                        BMI.setText(String.format("%.2f", target.getDouble(3)));
                        category.setText(getCategory(target.getDouble(3)));
                        healthRisk.setText(getRisk(target.getDouble(3)));
                        showToast("Data Loaded!");
                    }
                }
            }
        });

        //data passing from ListEntry class when a row is clicked
        Bundle extras = getIntent().getExtras();
        //check whether data is passed
        if (extras != null) {
            //if so, set all the edittext's and textview's based on values retrieved
            weight.setText(extras.getString("weight"));
            height.setText(extras.getString("height"));
            BMI.setText(String.format("%.2f", Double.parseDouble(extras.getString("BMI"))));
            category.setText(getCategory(Double.parseDouble(extras.getString("BMI"))));
            healthRisk.setText(getRisk(Double.parseDouble(extras.getString("BMI"))));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.option1:
                Intent i = new Intent(MainActivity.this, About.class);
                startActivity(i);
                return true;

            case R.id.option2:
                //showToast("Select Item 2!");

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle("Wipe All Entries");
                builder.setMessage("Are you sure? Upon clicking Confirm, all data will be lost.");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBH = new DatabaseHelper(MainActivity.this);
                                DBH.wipeDB();
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}