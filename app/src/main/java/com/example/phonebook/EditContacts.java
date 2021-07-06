package com.example.phonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditContacts extends AppCompatActivity {
    //creating variables
    EditText et1, et2, et3;
    Button b1;
    SQLiteDatabase db;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editcontacts_xml);
        //creating objects
        et1 = findViewById(R.id.a4_et1);
        et2 = findViewById(R.id.a4_et2);
        et3 = findViewById(R.id.a4_et3);
        b1 = findViewById(R.id.a4_b1);
        //getting the current intent
        intent = this.getIntent();
        //getting the string name
        String name = intent.getStringExtra("name");
        //connecting to database
        db = openOrCreateDatabase("phonebook", MODE_PRIVATE, null);
        //creating a query to select data of a contact
        String query = "select * from contacts where name = '" + name + "'";
        //cursor getting the data
        Cursor c = db.rawQuery(query, null);
        //checking if cursor have any data
        if(c.moveToFirst()) {
            //doing while there is data in cursor
            do {
                //setting data on edit text
                et1.setText(c.getString(0));
                et2.setText(c.getString(1));
                et3.setText(c.getString(2));
            } while(c.moveToNext());
        }
        //if cursor returns null
        else {
            //toast to tell users that there is some error
            Toast.makeText(this, "Error!!!", Toast.LENGTH_SHORT).show();
        }
        //listener for save button
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //query to delete the selected data from table
                String query1 = "delete from contacts where name = '" + name + "'";
                //executing the query
                db.execSQL(query1);
                //creating another query to insert new values in table
                String query2 =  "insert into contacts values('" + et1.getText().toString() + "', '" + et2.getText().toString() + "', '" + et3.getText().toString() + "')";
                //executing second query
                db.execSQL(query2);
                //toast notifying that contact has been saved
                Toast.makeText(EditContacts.this, "Contact Modified", Toast.LENGTH_SHORT).show();
                //intent to contact details class
                Intent intent = new Intent(EditContacts.this, ContactDetails.class);
                //string name of the contact
                String name = et1.getText().toString();
                //adding name in intent
                intent.putExtra("name", name);
                //starting contact details activity
                startActivity(intent);
            }
        });
    }
}