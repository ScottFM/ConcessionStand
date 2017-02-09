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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Customize extends AppCompatActivity implements View.OnClickListener {

    Button b;
    EditText name;
    EditText price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
            return true;
        }

        if (id == R.id.action_titlePage) {
            Intent I3 = new Intent("com.example.Scott.concessionstand.TotalPage");
            //startActivity(I3);

            startActivityForResult(I3, 1);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        Intent data = new Intent();

        String myName = name.getText().toString();
        Editable myPrice = price.getText();
        data.putExtra("Name", myName);
        data.putExtra("Price",myPrice);

        data.setData(Uri.parse(myName));
        //data.setData(Uri.parse(myName));
        setResult(RESULT_OK, data);
        finish();
    }
}
