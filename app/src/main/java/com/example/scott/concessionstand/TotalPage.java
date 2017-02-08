package com.example.scott.concessionstand;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class TotalPage extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    TextView hdTxt;
    TextView sTxt;
    TextView cTxt;
    TextView total;
    TextView cashBack;
    EditText OutOf;
    float totalPrice = 0;
    Button cancel;
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

        hd = getIntent().getIntExtra("HotDog",0);
        s = getIntent().getIntExtra("Soda",0);
        c = getIntent().getIntExtra("Candy",0);

        hdTxt.setText(hd + " x Hot dog: $" + (hd*1.50));
        sTxt.setText(s + " x Soda: $" + (s*1.00));
        cTxt.setText(c + " x Candy: $" + (c*0.75));
        totalPrice = (float) ((hd*1.50) + (s*1.00) + (c*0.75));
        total.setText("Total price: $" + totalPrice);
        OutOf.addTextChangedListener(this);
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
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().isEmpty()) {
            int o = Integer.parseInt(s.toString());
            if (o - totalPrice >= 0) {
                cashBack.setText("Change: $" + (o - totalPrice));
            } else {
                cashBack.setText("Insufficient money.");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        finish();
        Intent I = new Intent("com.example.Scott.concessionstand.Main");
        startActivity(I);

    }
}
