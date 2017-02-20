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
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class TotalPage extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnClickListener {



    TextView hdTxt;
    TextView sTxt;
    TextView cTxt;
    TextView total;
    TextView cashBack;
    EditText OutOf;
    String totalP;
    float totalPrice = 0;
    Button cancel;
    Button done;
    int hd;
    int s;
    int c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUp();

        hd = getIntent().getIntExtra("HotDog", 0);
        s = getIntent().getIntExtra("Soda", 0);
        c = getIntent().getIntExtra("Candy", 0);

        hdTxt.setText(String.format(hd + " x Hot dog: $" + "%.2f", (hd * 1.50)));
        sTxt.setText(String.format(s + " x Soda: $" + "%.2f", (s * 1.00)));
        cTxt.setText(String.format(c + " x Candy: $" + "%.2f", (c * 0.75)));
        totalPrice = (float) ((hd * 1.50) + (s * 1.00) + (c * 0.75));
        totalP = (String.format("%.2f", (hd * 1.50) + (s * 1.00) + (c * 0.75)));
        total.setText(String.format("Total price: $" + "%.2f", totalPrice));
        OutOf.setOnEditorActionListener(this);
    }


    public void setUp() {
        hdTxt = (TextView) findViewById(R.id.txtHotDog2);
        sTxt = (TextView) findViewById(R.id.txtSoda2);
        cTxt = (TextView) findViewById(R.id.txtCandy2);
        total = (TextView) findViewById(R.id.txtTotal2);
        cashBack = (TextView) findViewById(R.id.txtCashBack);
        OutOf = (EditText) findViewById(R.id.edtOutOf);
        cancel = (Button) findViewById(R.id.btnCancel);
        cancel.setOnClickListener(this);
        done = (Button) findViewById(R.id.btnDone);
        done.setOnClickListener(this);
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
                SharedPreferences shared = getSharedPreferences("myFile", 0);
                int hdd = shared.getInt("hotDogsDaily", 0);
                int sd = shared.getInt("sodaDaily", 0);
                int cd = shared.getInt("candyDaily", 0);
                SharedPreferences.Editor e = shared.edit();
                e.putInt("hotDogsDaily", hdd + hd);
                e.putInt("sodaDaily", sd + s);
                e.putInt("candyDaily", cd + c);
                e.commit();
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
        if (id == R.id.action_Home) {
            Intent I = new Intent("com.example.Scott.concessionstand.Main");
            //startActivity(I1);

            startActivity(I);
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
}