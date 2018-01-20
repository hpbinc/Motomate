package com.hashim.motomate;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class spider extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spider);

        String [] s=getIntent().getStringArrayExtra("pass");

        ListView simpleList = (ListView)findViewById(R.id.l1);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.listv, R.id.txt2, s);
        simpleList.setAdapter(arrayAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


}
