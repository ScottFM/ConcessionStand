package com.example.scott.concessionstand;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.id;

public class Customize extends AppCompatActivity implements View.OnClickListener {

    Button b;
    EditText name;
    EditText price;
    ListView lv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = (EditText) findViewById(R.id.edtName);
        price = (EditText) findViewById(R.id.edtPrice);

        b = (Button) findViewById(R.id.btnAdd);
        b.setOnClickListener(this);
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

    public void onClick(View v) {

        switch(id) {
            case R.id.btnAdd:
                ArrayAdapter<Bundle> lstAdapter;

                lv1 = (ListView) findViewById(R.id.lstCustomize1);

                ArrayList<Bundle> aList = new ArrayList<Bundle>();

                String myName = name.getText().toString();
                String myPrice = price.getText().toString();
                Bundle newBundle = new Bundle();
                newBundle.putString(myName, myPrice);

                aList.add(newBundle);
                //aList.add("Options2");
                //aList.add("Options3");

                lstAdapter = new ArrayAdapter<Bundle>(this, android.R.layout.simple_list_item_1, aList);

                lv1.setAdapter(lstAdapter);

                finish();
                break;
        }
    }
}
