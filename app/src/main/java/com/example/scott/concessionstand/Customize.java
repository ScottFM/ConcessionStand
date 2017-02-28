package com.example.scott.concessionstand;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Customize extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    Button b, bUp25, bDown25,btnSave, btnClearItems;
    EditText name;
    TextView price;
    ListView lv1;
    ArrayList<String> aList;
    double p = 0;
    Set<String> stringSet;

    SharedPreferences sharedNum;
    SharedPreferences shared;
    SharedPreferences.Editor e;
    int numInList;
    SharedPreferences.Editor eNum;
    SharedPreferences sharedDaily;
    SharedPreferences.Editor eDaily;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUp();

        ArrayAdapter<String> lstAdapter;

        stringSet = sharedDaily.getStringSet("set", new HashSet<String>());

        for (int i = 0; i < aList.size(); i++) {
            String next = aList.get(i);
            String item = next.substring(0,next.indexOf(","));
            float price = Float.parseFloat(next.substring(next.indexOf("$")+1));

            stringSet.add(item);
        }
        eDaily.commit();

        for (int i = 0; i < numInList; i++) {
            String myName = shared.getString("ItemName"+i, "");
            float myPrice = shared.getFloat("ItemPrice"+i, 0);

            String newString= new String(String.format(myName + ", $" + "%.2f", myPrice));

            if(myName != "")
                aList.add(newString);

            lstAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, aList);
            lv1.setAdapter(lstAdapter);
        }
    }

    public void setUp() {
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

        btnClearItems = (Button)findViewById(R.id.btnClearItems);
        btnClearItems.setOnClickListener(this);

        lv1 = (ListView) findViewById(R.id.lstCustomize1);
        lv1.setOnItemClickListener(this);

        stringSet = new HashSet<String>();
        aList = new ArrayList<String>();

        sharedNum = getSharedPreferences("num", 0);
        shared = getSharedPreferences("myFile", 0);
        e = shared.edit();
        numInList = sharedNum.getInt("numInList",0);
        eNum = sharedNum.edit();
        sharedDaily = getSharedPreferences("ItemsDaily", 0);
        eDaily = sharedDaily.edit();
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

                String myName;
                float myPrice;
                if (!price.getText().toString().isEmpty() && !name.getText().toString().isEmpty()) {
                    myName = name.getText().toString();
                    myPrice = Float.parseFloat(price.getText().toString());
                }
                else {
                    myName = "";
                    myPrice = 0;
                }

                String newString= new String();
                newString = String.format(myName + ", $" + "%.2f", (myPrice));

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
            case R.id.btnClearItems:
                aList.clear();
                numInList = 0;

                lstAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, aList);
                lv1.setAdapter(lstAdapter);
                break;
            case R.id.btnSave:

                //e.clear();
                for (int i = 0; i < numInList; i++) {
                    String next = aList.get(i);
                    String name = next.substring(0,next.indexOf(","));
                    float price = Float.parseFloat(next.substring(next.indexOf("$")+1));


                    e.putString("ItemName"+i, name);
                    e.putFloat("ItemPrice"+i, price);
                    e.commit();
                    stringSet.add(name);

                }
                eDaily.putStringSet("set", stringSet);
                eDaily.commit();

                SharedPreferences.Editor eNum = sharedNum.edit();
                eNum.putInt("numInList", numInList);
                eNum.commit();
                //Intent I = new Intent("com.example.Scott.concessionstand.Main");
                //startActivity(I);
                finish();
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        String name;

        ArrayAdapter<String> lstAdapter;
        lstAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, aList);
        final ArrayAdapter<String> adapter = lstAdapter;

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
                        alertEdit.setMessage("Edit name or price here.");
                        final EditText itemN = new EditText(Customize.this);
                        final EditText itemP = new EditText(Customize.this);
                        final View view;

                        //itemN.setInputType(InputType.TYPE_CLASS_TEXT);
                        itemP.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);

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
                                if (!itemP.getText().toString().isEmpty()) {
                                    float price = roundToNearest25(Float.parseFloat(itemP.getText().toString()));
                                    if (itemN.getText().toString().isEmpty()) {
                                        aList.set(position, String.format("Nameless, $" + "%.2f", price));
                                    }
                                    else {
                                        aList.set(position, String.format(itemN.getText().toString() + ", $" + "%.2f", price));
                                    }
                                }
                                    else {
                                    if (itemN.getText().toString().isEmpty()) {
                                        aList.set(position, String.format("Nameless, $0.00"));
                                    }
                                    else {
                                        aList.set(position, itemN.getText().toString() + ", $0.00");
                                    }
                                }
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
        alert.show();

        eNum.putInt("numInList", numInList);
        eNum.commit();
    }

    public float roundToNearest25( float f) {
        double temp = f%0.25;
        if (temp >= 0.125)
        {
            f += 0.25-temp;
            //Toast.makeText(this, "Price should be in increments of $0.25", Toast.LENGTH_SHORT).show();
        }
        else if (temp < 0.12){
            f-=temp;
            //Toast.makeText(this, "Price should be in increments of $0.25", Toast.LENGTH_SHORT).show();
        }

        return f;

    }
}

