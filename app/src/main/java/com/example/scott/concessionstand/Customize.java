package com.example.scott.concessionstand;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import java.sql.BatchUpdateException;
import java.util.ArrayList;

import static android.R.attr.id;

public class Customize extends AppCompatActivity implements View.OnClickListener {

    Button b, bUp25, bDown25,btnSave;
    EditText name;
    TextView price;
    ListView lv1;
    ArrayList<String> aList = new ArrayList<String>();
    double p = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = (EditText) findViewById(R.id.edtName);
        price = (TextView) findViewById(R.id.txtPriceCust);

        b = (Button) findViewById(R.id.btnAdd);
        b.setOnClickListener(this);

        bDown25 = (Button) findViewById(R.id.btnDown25);
        bDown25.setOnClickListener(this);

        bUp25 = (Button) findViewById(R.id.btnUp25);
        bUp25.setOnClickListener(this);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
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

        switch(v.getId()) {
            case R.id.btnUp25:
                p += 0.25;
                price.setText(String.format("%.2f", p));
                break;
            case R.id.btnDown25:
                if (p > 0) {
                    p -= 0.25;
                }
                price.setText(String.format("%.2f", p));
                break;
            case R.id.btnAdd:
                ArrayAdapter<String> lstAdapter;

                lv1 = (ListView) findViewById(R.id.lstCustomize1);

                String myName = name.getText().toString();
                String myPrice = price.getText().toString();

                String newString= new String(myName + ", $" + myPrice);

                if (aList.size() < 8)
                    aList.add(newString);
                else
                Toast.makeText(this, "No more than 8 items allowed.", Toast.LENGTH_SHORT).show();

                lstAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, aList);
                lv1.setAdapter(lstAdapter);

                break;
            case R.id.btnSave:
                SharedPreferences shared = getSharedPreferences( "myFile", 0);
                SharedPreferences.Editor e = shared.edit();
                for (int i = 0; i < aList.size(); i++) {
                    String next = aList.get(i);
                    String item = next.substring(0,next.indexOf(","));
                    String price = next.substring(next.indexOf("$", -1));
                    e.putString("ItemName" + i, item);
                    e.putString("ItemPrice" + i, price);
                }
                e.commit();

                Intent I = new Intent("com.example.Scott.concessionstand.Main");
                startActivity(I);
                finish();
                break;

        }
    }
}
