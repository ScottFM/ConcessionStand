package com.example.scott.concessionstand;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashSet;
import java.util.Set;

import static android.R.attr.height;
import static android.R.attr.name;
import static android.R.attr.textSize;
import static android.R.attr.width;

public class TotalPage extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnClickListener {

    TextView total;
    TextView cashBack;
    EditText OutOf;
    float totalPrice = 0;
    Button cancel;
    Button done;
    LinearLayout lyt;
    int z = 0;

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

        setContentView(R.layout.activity_total_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUp();

        OutOf.setOnEditorActionListener(this);

        float sum = 0;
        //z = sharedDaily.getInt("NumItems", 0);

        for (int i = 0; i < numInList; i++) {
            String name = shared.getString("ItemName" + Integer.toString(i), "");
            float price = shared.getFloat("ItemPrice" + Integer.toString(i), 0);
            int val = shared.getInt("ItemQuantity" + Integer.toString(i), 0);

            if (name != "" && val >0) {
                String info = String.format(val + " x " + name + ": $" + "%.2f", (price * val));
                TextView tv = new TextView(this);
                tv.setText(info);
                tv.setTextSize(20);
                lyt.addView(tv);


                sum += price * val;
                z++;
            }
        }

        total.setText(String.format("Total: $" + "%.2f", sum));
        totalPrice = sum;
    }


    public void setUp() {
        total = (TextView) findViewById(R.id.txtTotal2);
        cashBack = (TextView) findViewById(R.id.txtCashBack);
        OutOf = (EditText) findViewById(R.id.edtOutOf);

        cancel = (Button) findViewById(R.id.btnCancel);
        cancel.setOnClickListener(this);

        done = (Button) findViewById(R.id.btnDone);
        done.setOnClickListener(this);

        lyt = (LinearLayout) this.findViewById(R.id.lytTop);

        sharedNum = getSharedPreferences("num", 0);
        shared = getSharedPreferences("myFile", 0);
        e = shared.edit();
        numInList = sharedNum.getInt("numInList",0);
        eNum = sharedNum.edit();
        sharedDaily = getSharedPreferences("ItemsDaily", 0);
        eDaily = sharedDaily.edit();

    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (!OutOf.getText().toString().isEmpty()) {
            float o = Float.parseFloat(OutOf.getText().toString());
            if ((o - totalPrice >= 0) && (actionId == EditorInfo.IME_ACTION_DONE)) {
                cashBack.setText(String.format("Change: $" + "%.2f", (o - totalPrice)));
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            } else {
                cashBack.setText("Insufficient money.");
                Toast.makeText(this, "Insufficient money.", Toast.LENGTH_SHORT).show();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:

                for (int i = 0; i < numInList; i++) {
                    e.putInt("ItemQuantity"+i, 0);
                }

                e.commit();
                Intent I = new Intent("com.example.Scott.concessionstand.Main");
                //I.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //This line allows the cancel button to clear the whole stack.
                //After clicking cancel, if you press the back button on the main activity, it will exit the app. That's how I want it.
                finish();
                startActivity(I);
                break;
            case R.id.btnDone:
                Intent I2 = new Intent("com.example.Scott.concessionstand.Main");

                Set<String> stringSet = new HashSet<String>();
                stringSet = sharedDaily.getStringSet("set", new HashSet<String>());

                for (int i = 0; i < numInList; i++) {
                    String name = shared.getString("ItemName" + Integer.toString(i), "");
                    float price = shared.getFloat("ItemPrice" + Integer.toString(i), 0);
                    int val = shared.getInt("ItemQuantity" + Integer.toString(i), 0);

                    stringSet.add(name);
                    e.putInt("ItemQuantity"+i, 0);


                    int newVal = sharedDaily.getInt(name + "val", 0);
                    newVal += val;
                    eDaily.putInt(name + "val", newVal);
                    eDaily.putFloat(name + "price", price);

                }
                e.commit();
                eDaily.putStringSet("set", stringSet);
                eDaily.commit();

                //gave problems for me with shared preferences
                //I2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //This line allows the cancel button to clear the whole stack.
                //After clicking cancel, if you press the back button on the main activity, it will exit the app. That's how I want it.
                startActivity(I2);
                break;
        }
    }
}
