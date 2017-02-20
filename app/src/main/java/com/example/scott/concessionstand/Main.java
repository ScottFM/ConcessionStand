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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Main extends AppCompatActivity implements View.OnClickListener {

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

        clear.setOnClickListener(this);
        total.setOnClickListener(this);

        SharedPreferences s = getSharedPreferences("myFile", 0);

        int hotDogsDaily = s.getInt("myValue", 0);

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
        if (id == R.id.action_Home) {
            Intent I1 = new Intent("com.example.Scott.concessionstand.Main");
            //startActivity(I1);

            startActivity(I1);
            return true;
        }

        if (id == R.id.action_Customize) {
            Intent I = new Intent("com.example.Scott.concessionstand.Customize");
            //startActivity(I2);

            startActivityForResult(I, 1);
            finish();
            return true;
        }

        if (id == R.id.action_totalPage) {
            Intent I3 = new Intent("com.example.Scott.concessionstand.TotalPage");
            //startActivity(I3);

            startActivityForResult(I3, 1);
            finish();
            return true;
        }

        if (id == R.id.action_dailyTotal) {
            Intent I4 = new Intent("com.example.Scott.concessionstand.DailyTotals");
            //startActivity(I3);

            startActivityForResult(I4, 1);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent response) {
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
                break;
            case R.id.btnTotal:
                Intent I = new Intent("com.example.Scott.concessionstand.TotalPage");
                I.putExtra("HotDog", udHd.returnVal());
                I.putExtra("Soda", udS.returnVal());
                I.putExtra("Candy", udC.returnVal());
                startActivityForResult(I, 1);
                break;

        }
        rt.setText(String.format("Running total - $" + "%.2f",(udHd.returnVal()*1.5 + udS.returnVal()*1 + udC.returnVal()*0.75)));
    }
}
