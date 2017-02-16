package com.example.scott.concessionstand;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DailyTotal extends AppCompatActivity implements View.OnClickListener {

    TextView txtNumHotDogsDaily, txtNumSodaDaily, txtNumCandyDaily, txtdailyrev;
    TextView hdRev, sRev, cRev;
    Button reset;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_total);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtNumHotDogsDaily = (TextView) findViewById(R.id.txtNumHotDogDT);
        txtNumSodaDaily = (TextView) findViewById(R.id.txtNumSodaDT);
        txtNumCandyDaily = (TextView) findViewById(R.id.txtNumCandyDT);
        hdRev = (TextView) findViewById(R.id.txtHotDogRev);
        sRev = (TextView) findViewById(R.id.txtSodaRev);
        cRev = (TextView) findViewById(R.id.txtCandyRev);
        txtdailyrev = (TextView) findViewById(R.id.txtDailyRev);
        reset = (Button) findViewById(R.id.btnReset);
        reset.setOnClickListener(this);

        SharedPreferences shared = getSharedPreferences( "myFile", 0);
        int hdd = shared.getInt("hotDogsDaily", 0);
        int sd = shared.getInt("sodaDaily", 0);
        int cd = shared.getInt("candyDaily", 0);
        txtNumHotDogsDaily.setText(Integer.toString(hdd));
        txtNumSodaDaily.setText(Integer.toString(sd));
        txtNumCandyDaily.setText(Integer.toString(cd));
        hdRev.setText(String.format("$" + "%.2f",(hdd*1.50)));
        sRev.setText(String.format("$" + "%.2f",(sd*1.0)));
        cRev.setText(String.format("$" + "%.2f",(cd*.75)));
        txtdailyrev.setText(String.format("$" + "%.2f",(hdd*1.50)+(sd*1)+(cd*0.75)));

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
            finish();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences shared = getSharedPreferences( "myFile", 0);  //Reset the shared values to 0;
        int hdd = 0;
        int sd = 0;
        int cd = 0;
        SharedPreferences.Editor e = shared.edit();
        e.putInt("hotDogsDaily", hdd);
        e.putInt("sodaDaily", sd);
        e.putInt("candyDaily", cd);
        e.commit();

        txtNumHotDogsDaily.setText(Integer.toString(hdd));              //Reset the textviews to display 0
        txtNumSodaDaily.setText(Integer.toString(sd));
        txtNumCandyDaily.setText(Integer.toString(cd));

        hdRev.setText(String.format("$" + "%.2f",(hdd*1.50)));          //Reset the textviews to display 0
        sRev.setText(String.format("$" + "%.2f",(sd*1.0)));
        cRev.setText(String.format("$" + "%.2f",(cd*.75)));
        txtdailyrev.setText(String.format("$" + "%.2f",(hdd*1.50)+(sd*1)+(cd*0.75)));
    }
}
