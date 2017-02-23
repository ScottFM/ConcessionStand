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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends AppCompatActivity implements View.OnClickListener, onValueChangedListener {

    TextView rt;
    Button clear;
    Button total;
    UpDownBox udHd;
    UpDownBox udS;
    UpDownBox udC;

    public void setUpItems() {
        rt = (TextView) findViewById(R.id.txtRunningTotal);
        clear = (Button) findViewById(R.id.btnClear);
        total = (Button) findViewById(R.id.btnTotal);
        udHd = (UpDownBox) findViewById(R.id.udHotDog);
        udS = (UpDownBox) findViewById(R.id.udSoda);
        udC = (UpDownBox) findViewById(R.id.udCandy);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpItems();

        rt.setText(String.format("Running Total - $" + "%.2f",((udHd.returnVal()*1.50)+(udS.returnVal()*1)+(udC.returnVal()*0.75))));

        clear.setOnClickListener(this);
        total.setOnClickListener(this);

        udHd.setOnValueChangedListener(this);
        udS.setOnValueChangedListener(this);
        udC.setOnValueChangedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void onSaveInstanceState(Bundle savedInstance) {  //this function lets you save the info if you rotate the phone or pause the activity
        savedInstance.putInt("udHd", udHd.returnVal());
        savedInstance.putInt("udS", udS.returnVal());
        savedInstance.putInt("udC", udC.returnVal());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstance) {  //opens the saved info from previous function
        int udHdVal = savedInstance.getInt("udHd");
        int udSVal = savedInstance.getInt("udS");
        int udCVal = savedInstance.getInt("udC");

        udHd.setVal(udHdVal);
        udS.setVal(udSVal);
        udC.setVal(udCVal);

        rt.setText(String.format("Running Total - $" + "%.2f",((udHd.returnVal()*1.50)+(udS.returnVal()*1)+(udC.returnVal()*0.75))));
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
            startActivityForResult(I, 1);
            //finish();
            return true;
        }

        if (id == R.id.action_dailyTotal) {
            Intent I2 = new Intent("com.example.Scott.concessionstand.DailyTotals");
            startActivityForResult(I2, 1);
            //finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent response) {    //for customizing eventually
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                int p = getIntent().getIntExtra("Price",0);
                String n = response.getData().toString();
                Toast.makeText(this, n + " " + p, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btnClear:
                udHd.setVal(0);
                udS.setVal(0);
                udC.setVal(0);
                rt.setText(String.format("Running Total - $" + "%.2f",((udHd.returnVal()*1.50)+(udS.returnVal()*1)+(udC.returnVal()*0.75))));
                break;
            case R.id.btnTotal:
                Intent I = new Intent("com.example.Scott.concessionstand.TotalPage");
                I.putExtra("HotDog", udHd.returnVal());
                I.putExtra("Soda", udS.returnVal());
                I.putExtra("Candy", udC.returnVal());
                startActivityForResult(I, 1);
                break;
        }

        //Create a new event that listens for the value to change.
        //Event listens for any change in the custom view.
    }


    @Override
    public void onValueChanged(View v) {
        rt.setText(String.format("Running Total - $" + "%.2f",((udHd.returnVal()*1.50)+(udS.returnVal()*1)+(udC.returnVal()*0.75))));
        //how to make this account for change of new value instead of old
        //how to make the clear button be onvaluechanged
    }
}
