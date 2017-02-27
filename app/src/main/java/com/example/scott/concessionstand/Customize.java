package com.example.scott.concessionstand;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.BatchUpdateException;
import java.util.ArrayList;

import static android.R.attr.id;

public class Customize extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    Button b, bUp25, bDown25,btnSave;
    EditText name;
    TextView price;
    ListView lv1;
    ArrayList<String> aList = new ArrayList<String>();
    double p = 0;
    int numInList = 0;
    private int numAtStart = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "oncreate ran", Toast.LENGTH_SHORT).show();
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

        lv1 = (ListView) findViewById(R.id.lstCustomize1);
        lv1.setOnItemClickListener(this);

        ArrayAdapter<String> lstAdapter;
        SharedPreferences sharedNum = getSharedPreferences("num", 0);
        int num = sharedNum.getInt("numInList", 0);

        SharedPreferences shared = getSharedPreferences("myFile", 0);

        numAtStart = 0;
        for (int i = 0; i < num; i++) {
            String myName = shared.getString("ItemName"+i, "");
            float myPrice = shared.getFloat("ItemPrice"+i, 0);

            String newString= new String(myName + ", $" + myPrice);

            if(myName != "")
                aList.add(newString);

            lstAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, aList);
            lv1.setAdapter(lstAdapter);

            numAtStart++;
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

                String myName = name.getText().toString();
                String myPrice = price.getText().toString();

                String newString= new String();
                newString = String.format(myName + ", $" + "%.2f", myPrice);

                if (aList.size() < 8) {
                    if (myName != "") {
                        aList.add(newString);
                        numInList++;
                    }
                    else if (myName == "") {
                        aList.remove(newString);
                    }
                }
                else
                    Toast.makeText(this, "No more than 8 items allowed.", Toast.LENGTH_SHORT).show();


                lstAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, aList);
                lv1.setAdapter(lstAdapter);

                break;
            case R.id.btnSave:
                SharedPreferences shared = getSharedPreferences( "myFile", 0);
                SharedPreferences.Editor e = shared.edit();


                for (int i = 0; i < aList.size(); i++) {
                    Toast.makeText(this, "Got into button save for loop", Toast.LENGTH_SHORT).show();
                    String next = aList.get(i);
                    String item = next.substring(0,next.indexOf(","));
                    float price = Float.parseFloat(next.substring(next.indexOf("$")+1));
                    e.putString("ItemName" + i, item);
                    e.putFloat("ItemPrice" + i, price);
                    e.commit();
                }

                SharedPreferences sharedNum = getSharedPreferences("num", 0);
                SharedPreferences.Editor eNum = sharedNum.edit();
                eNum.putInt("numInList", numInList+numAtStart);
                eNum.commit();
                //Intent I = new Intent("com.example.Scott.concessionstand.Main");
                //startActivity(I);
                finish();
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        ArrayAdapter<String> lstAdapter;
        lstAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, aList);
        final ArrayAdapter<String> adapter = lstAdapter;

        SharedPreferences sharedNum = getSharedPreferences("num", 0);
        SharedPreferences.Editor eNum = sharedNum.edit();

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final int positionToRemove = position;
        alert.setMessage(aList.get(positionToRemove));
        alert.setCancelable(true);
        alert.setPositiveButton(
                "Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        aList.remove(positionToRemove);
                        lv1.setAdapter(adapter);
                        numInList--;
                    }
                });

        alert.setNegativeButton(
                "Edit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        AlertDialog.Builder alertEdit = new AlertDialog.Builder(Customize.this);
                        final EditText itemN = new EditText(Customize.this);
                        final EditText itemP = new EditText(Customize.this);
                        final View view;

                        itemN.setText(aList.get(position).substring(0,aList.get(position).indexOf(",")));
                        itemP.setText(aList.get(position).substring(aList.get(position).indexOf("$")+1));

                        LinearLayout ll = new LinearLayout(Customize.this);
                        ll.setOrientation(LinearLayout.HORIZONTAL);
                        ll.addView(itemN);
                        ll.addView(itemP);
                        alertEdit.setView(ll);


                        alertEdit.setCancelable(false);
                        alertEdit.setPositiveButton("Update",  new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                aList.set(position, String.format(itemN.getText().toString() + ", $" + "%.2f", Float.parseFloat(itemP.getText().toString())));
                                lv1.setAdapter(adapter);
                            }
                        });

                        AlertDialog alert = alertEdit.create();
                        alert.show();

                        dialog.cancel();
                    }
                });
        alert.setNeutralButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        eNum.putInt("numInList", numInList+numAtStart);
        eNum.commit();
        AlertDialog alert11 = alert.create();
        alert11.show();
    }
}

