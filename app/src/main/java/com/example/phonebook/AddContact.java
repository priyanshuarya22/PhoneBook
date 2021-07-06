package com.example.phonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddContact extends AppCompatActivity {
    //creating variables
    EditText et1, et2, et3;
    Button b1;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontact_xml);
        //creating objects
        et1 = findViewById(R.id.a2_et1);
        et2 = findViewById(R.id.a2_et2);
        et3 = findViewById(R.id.a2_et3);
        b1 = findViewById(R.id.a2_b1);
        //setting listener to save button
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //connecting to database
                db = openOrCreateDatabase("phonebook", MODE_PRIVATE, null);
                //executing a create table query if the table already does not exist
                db.execSQL("create table if not exists contacts(name text, pno text, email text)");
                //creating a query to insert into contacts table
                String query = "insert into contacts values('" + et1.getText().toString() + "', '" + et2.getText().toString() + "', '" + et3.getText().toString() + "')";
                //executing the query
                db.execSQL(query);
                //closing the database
                db.close();
                //creating a toast to inform user that the contact is saved
                Toast.makeText(AddContact.this, "Contact Saved", Toast.LENGTH_SHORT).show();
                //creating a intent to home class
                Intent intent = new Intent(AddContact.this, Home.class);
                //staring home activity
                startActivity(intent);
            }
        });
    }
}