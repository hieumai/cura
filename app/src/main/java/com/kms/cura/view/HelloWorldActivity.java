package com.kms.cura.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.controller.HelloWorldController;
import com.kms.cura.model.HelloWorldModel;
import com.kms.cura.entity.HelloWorldEntity;

public class HelloWorldActivity extends AppCompatActivity {
    private HelloWorldEntity helloWorld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_world);
        initView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent=new Intent(this,PatientSignUpActivity.class);
        startActivity(intent);
    }

    /**
     * Init view with entity object
     */
    public void initView() {
        // name for example
        String name = "My name is HelloWorld";
        helloWorld = HelloWorldController.getHelloWorldEntityByName(name);
        TextView textView = (TextView)findViewById(R.id.txt_helloWorld);
        textView.setText(helloWorld.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hello_world, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
