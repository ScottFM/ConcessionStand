package com.example.scott.concessionstand;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DailyTotal extends AppCompatActivity implements View.OnClickListener {

    LinearLayout lyt;
    TextView txtdailyrev;
    Button reset;
    float totalPrice;


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_total);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtdailyrev = (TextView) findViewById(R.id.txtDailyRev);
        lyt = (LinearLayout) findViewById(R.id.lytTable);

        reset = (Button) findViewById(R.id.btnReset);
        reset.setOnClickListener(this);

        SharedPreferences shared = getSharedPreferences("ItemsDaily", 0);
        int numItems = shared.getInt("NumItems",0);

        for (int i = 0; i < numItems; i++) {
            String name = shared.getString("ItemName"+i, "");
            float price = shared.getFloat("ItemPrice"+i, 0);
            int quantity = shared.getInt("ItemQuantity"+i, 0);

            TextView tvQ = new TextView(this);
            TextView tvN = new TextView(this);
            TextView tvP = new TextView(this);

            tvQ.setText(String.valueOf(quantity));
            tvN.setText(name);
            tvP.setText(String.format("$" + "%.2f", price));

            LinearLayout llHorizontal = new LinearLayout(this);
            llHorizontal.setOrientation(LinearLayout.HORIZONTAL);

            llHorizontal.addView(tvQ, 200, 100);
            llHorizontal.addView(tvN, 600, 100);
            llHorizontal.addView(tvP, 400, 100);
            lyt.addView(llHorizontal);

            totalPrice += quantity*price;
        }

        txtdailyrev.setText(String.format("Daily profit: $" + "%.2f", totalPrice));

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
            startActivityForResult(I, 1);
            //finish();
            return true;
        }

        if (id == R.id.action_dailyTotal) {
            Intent I2 = new Intent("com.example.Scott.concessionstand.DailyTotals");
            startActivityForResult(I2, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Clear all values?");
        alert.setCancelable(true);
        alert.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedNum = getSharedPreferences("num", 0);
                        int numInList = sharedNum.getInt("numInList", 0);

                        SharedPreferences shared = getSharedPreferences( "myFile", 0);

                        lyt.removeAllViews();
                        txtdailyrev.setText("Daily profit: $0.00");

                        SharedPreferences sharedDaily = getSharedPreferences( "ItemsDaily", 0);
                        SharedPreferences.Editor eDaily = sharedDaily.edit();

                        eDaily.putInt("NumIxtems", 0);
                        eDaily.commit();

                    }
                });
        alert.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        alert.show();
    }
}
