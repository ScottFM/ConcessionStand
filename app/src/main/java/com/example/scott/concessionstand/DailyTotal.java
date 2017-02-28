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

import java.util.HashSet;
import java.util.Set;

public class DailyTotal extends AppCompatActivity implements View.OnClickListener {

    LinearLayout lyt;
    TextView txtdailyrev;
    Button reset;
    float totalPrice;

    SharedPreferences sharedNum;
    SharedPreferences shared;
    SharedPreferences.Editor e;
    int numInList;
    SharedPreferences.Editor eNum;
    SharedPreferences sharedDaily;
    SharedPreferences.Editor eDaily;


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_total);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUp();

        SharedPreferences shared = getSharedPreferences("ItemsDaily", 0);
        Set<String> stringSet = shared.getStringSet("set", new HashSet<String>());

        String[] setArray = stringSet.toArray(new String[0]);
        int numItems = stringSet.size();

        for (int i = 0; i < numItems; i++) {
            String name = setArray[i];
            float price = shared.getFloat(setArray[i]+"price", 0);
            int quantity = shared.getInt(setArray[i]+"val", 0);

            TextView tvQ = new TextView(this);
            TextView tvN = new TextView(this);
            TextView tvP = new TextView(this);

            tvQ.setText(String.valueOf(quantity));
            tvN.setText(name);
            tvP.setText(String.format("$" + "%.2f", price));

            LinearLayout llHorizontal = new LinearLayout(this);
            llHorizontal.setOrientation(LinearLayout.HORIZONTAL);

            if (name != "" && quantity > 0) {
                llHorizontal.addView(tvQ, 200, 100);
                llHorizontal.addView(tvN, 800, 100);
                llHorizontal.addView(tvP, 400, 100);
            }

            lyt.addView(llHorizontal);

            totalPrice += quantity*price;
        }
        txtdailyrev.setText(String.format("Daily profit: $" + "%.2f", totalPrice));
    }

    public void setUp() {
        txtdailyrev = (TextView) findViewById(R.id.txtDailyRev);
        lyt = (LinearLayout) findViewById(R.id.lytTable);

        reset = (Button) findViewById(R.id.btnReset);
        reset.setOnClickListener(this);

        sharedNum = getSharedPreferences("num", 0);
        shared = getSharedPreferences("myFile", 0);
        e = shared.edit();
        numInList = sharedNum.getInt("numInList",0);
        eNum = sharedNum.edit();
        sharedDaily = getSharedPreferences("ItemsDaily", 0);
        eDaily = sharedDaily.edit();
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
                        SharedPreferences.Editor eNum = sharedNum.edit();
                        int numInList = sharedNum.getInt("numInList", 0);

                        SharedPreferences shared = getSharedPreferences( "myFile", 0);
                        SharedPreferences.Editor e = shared.edit();

                        lyt.removeAllViews();
                        txtdailyrev.setText("Daily profit: $0.00");

                        SharedPreferences sharedDaily = getSharedPreferences( "ItemsDaily", 0);
                        SharedPreferences.Editor eDaily = sharedDaily.edit();

                        eDaily.clear();
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
