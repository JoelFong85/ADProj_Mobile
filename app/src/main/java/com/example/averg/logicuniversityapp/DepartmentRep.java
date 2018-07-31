package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DepartmentRep extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_rep);
        Button b1=(Button)findViewById(R.id.button4);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DepartmentRep.this,ChangeCollectionpoint.class);
                startActivity(i);
            }
        });
        Button b2=(Button)findViewById(R.id.button5);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DepartmentRep.this,ViewCollectionhistory.class);
                startActivity(i);
            }
        });
        Button b3=(Button)findViewById(R.id.button6);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DepartmentRep.this,NewRequestActivity.class);
                startActivity(i);
            }
        });
    }
}

