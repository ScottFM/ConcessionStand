package com.example.scott.concessionstand;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity implements View.OnClickListener, onValueChangedListener {

    TextView rt;
    Button clear;
    Button total;
    GridLayout grid;
    //ArrayList<UpDownBox> udbList;
    UpDownBox[] udbList;

    SharedPreferences sharedNum;
    SharedPreferences shared;
    SharedPreferences.Editor e;
    int numInList;
    SharedPreferences.Editor eNum;


    public void setUpItems() {
        rt = (TextView) findViewById(R.id.txtRunningTotal);
        clear = (Button) findViewById(R.id.btnClear);
        total = (Button) findViewById(R.id.btnTotal);
        grid = (GridLayout) findViewById(R.id.grdLayout);

        udbList = new UpDownBox[8];

        sharedNum = getSharedPreferences("num", 0);
        shared = getSharedPreferences("myFile", 0);
        e = shared.edit();
        numInList = sharedNum.getInt("numInList",0);
        eNum = sharedNum.edit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpItems();

        clear.setOnClickListener(this);
        total.setOnClickListener(this);

        udbList = new UpDownBox[8];
        setUDBs();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUDBs();
    }

    public void setUDBs() {
        if (grid.getChildCount() > 0) { //Removes all items in the layout
            grid.removeAllViews();
        }

        float sum = 0;
        //eNum.putInt("numInList", 0);

        numInList = sharedNum.getInt("numInList",0);
        for (int i = 0; i < numInList; i++) {
            String name = shared.getString("ItemName" +i, "");
            float price = shared.getFloat("ItemPrice" +i, 0);
            int quantity = shared.getInt("ItemQuantity" + i, 0);

            if (name != "" && price != 0) {
                UpDownBox newUDB = new UpDownBox(this);
                newUDB.setItem(name);
                newUDB.setPrice(price);
                newUDB.setVal(quantity);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.setMargins(80, 50, 80, 50);
                newUDB.setLayoutParams(params);

                udbList[i] = newUDB;
                grid.addView(newUDB);
                sum += price * quantity;
                udbList[i].setOnValueChangedListener(this);
            }

            e.putString("ItemName" + i, name);
            e.putFloat("ItemPrice" + i, price);

            e.commit();
        }

        rt.setText(String.format("Running Total - $" + "%.2f",sum));
    }

    @Override
    protected void onPause() {
        super.onPause();

        for (int i = 0; i < numInList; i++) {
            int quantity = udbList[i].returnVal();

            e.putInt("ItemQuantity"+i, quantity);
            e.commit();
        }
    }

    protected void onSaveInstanceState(Bundle savedInstance) {  //this function lets you save the info if you rotate the phone or pause the activity

        for (int i = 0; i < numInList; i++) {
            String name = shared.getString("ItemName" + Integer.toString(i), "");
            float price = shared.getFloat("ItemPrice" + Integer.toString(i), 0);
            int quantity = shared.getInt("ItemQuantity"+i, 0);

            savedInstance.putInt("quantity"+i, quantity);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstance) {  //opens the saved info from previous function
        for (int i = 0; i < numInList; i++) {
            int val = savedInstance.getInt("quantity"+i, 0);
            e.putInt("ItemQuantity"+i, val);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Customize) {
            Intent I = new Intent("com.example.Scott.concessionstand.Customize");
            startActivity(I);

            int numInList = grid.getChildCount();

            eNum.putInt("numInList", numInList);
            eNum.commit();

            //finish();
            return true;
        }

        if (id == R.id.action_dailyTotal) {
            Intent I2 = new Intent("com.example.Scott.concessionstand.DailyTotals");
            startActivity(I2);
            //finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btnClear:
                for (int i = 0; i < udbList.length; i++) {
                    udbList[i].setVal(0);
                }
                rt.setText(String.format("Running Total - $0.00"));
                break;
            case R.id.btnTotal:
                for (int i = 0; i < numInList; i++) {
                    int quantity = udbList[i].returnVal();
                    e.putInt("ItemQuantity" + i, quantity);
                    e.commit();
                }

                Intent I = new Intent("com.example.Scott.concessionstand.TotalPage");
                startActivity(I);
                //finish();
                break;
        }
    }


    @Override
    public void onValueChanged(View v) {
        float sum = 0;

        for (int i = 0; i < numInList; i++) {
            float price = shared.getFloat("ItemPrice" + Integer.toString(i), 0);
            int val = udbList[i].returnVal();

            sum += price * val;
        }

        rt.setText(String.format("Running Total - $" + "%.2f",sum));
    }
}
