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

public class TotalPage extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnClickListener {

    TextView total;
    TextView cashBack;
    EditText OutOf;
    float totalPrice = 0;
    Button cancel;
    Button done;
    LinearLayout lyt;
    int z = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_total_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUp();

        OutOf.setOnEditorActionListener(this);


        SharedPreferences sharedNum = getSharedPreferences("num", 0);
        int numInList = sharedNum.getInt("numInList", 0);

        SharedPreferences shared = getSharedPreferences( "myFile", 0);

        SharedPreferences sharedDaily = getSharedPreferences("ItemsDaily", 0);
        SharedPreferences.Editor eDaily = sharedDaily.edit();

        float sum = 0;
        z = sharedDaily.getInt("NumItems", 0);

        for (int i = 0; i < numInList; i++) {
            String name = shared.getString("ItemName" + Integer.toString(i), "");
            float price = shared.getFloat("ItemPrice" + Integer.toString(i), 0);
            int val = shared.getInt("ItemQuantity" + Integer.toString(i), 0);

            if (name != "" && val > 0) {
                String info = String.format(val + " x " + name + ": $" +"%.2f",(price*val));
                TextView tv = new TextView(this);
                tv.setText(info);
                lyt.addView(tv);

                sum += price*val;

                eDaily.putString(name+"name", name);
                eDaily.putInt(name+"val", val);
                eDaily.putFloat(name+"price", price);
                z++;
                eDaily.putInt("NumItems", z);
                eDaily.commit();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                finish();
                Intent I = new Intent("com.example.Scott.concessionstand.Main");
                I.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //This line allows the cancel button to clear the whole stack.
                //After clicking cancel, if you press the back button on the main activity, it will exit the app. That's how I want it.
                startActivity(I);
                break;
            case R.id.btnDone:
                Intent I2 = new Intent("com.example.Scott.concessionstand.Main");

                I2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //This line allows the cancel button to clear the whole stack.
                //After clicking cancel, if you press the back button on the main activity, it will exit the app. That's how I want it.
                startActivity(I2);
                break;
        }
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
}