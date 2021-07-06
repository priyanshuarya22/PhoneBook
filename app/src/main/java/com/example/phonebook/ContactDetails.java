package com.example.phonebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class ContactDetails extends AppCompatActivity {
    //creating variables
    Intent intent;
    TextView tv1, tv2, tv3;
    SQLiteDatabase db;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactsdetails_xml);
        //creating objects
        tv1 = findViewById(R.id.a3_tv1);
        tv2 = findViewById(R.id.a3_tv2);
        tv3 = findViewById(R.id.a3_tv3);
        //getting the intent which started this activity
        intent = this.getIntent();
        //extracting the name of contact from intent
        name = intent.getStringExtra("name");
        //connecting to the database
        db = openOrCreateDatabase("phonebook", MODE_PRIVATE, null);
        //creating a query to select a record with a specific name
        String query = "select * from contacts where name = '" + name + "'";
        //cursor getting the result after query execution with default value null
        Cursor c = db.rawQuery(query, null);
        //checking if the cursor has any data
        if(c.moveToFirst()) {
            //doing while cursor has values
            do {
                //setting requested data on text view
                tv1.setText(c.getString(0));
                tv2.setText(c.getString(1));
                tv3.setText(c.getString(2));
            } while(c.moveToNext());
        }
        //this code will execute if the cursor has no data
        else {
            //creating a intent to home class
            Intent intent = new Intent(ContactDetails.this, Home.class);
            //putting a boolean value
            intent.putExtra("check", true);
            //starting home activity
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //create a option menu
        MenuInflater mf =this.getMenuInflater();
        //inflate the option menu
        mf.inflate(R.menu.context_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //check if the item selected is edit
        if(item.getItemId() == R.id.cm_i1) {
            //create a intent to edit contact activity
            Intent intent = new Intent(ContactDetails.this, EditContacts.class);
            //putting the name of contact along with the intent
            intent.putExtra("name", name);
            //starting the edit contact activity
            startActivity(intent);
        }
        //check if the selected item is delete
        else if(item.getItemId() ==R.id.cm_i2) {
            //creating a query to delete contact
            String query1 = "delete from contacts where name = '" + name + "'";
            //executing the query
            db.execSQL(query1);
            //creating a toast to inform users that the contact was successfully deleted
            Toast.makeText(this, "Contact Deleted", Toast.LENGTH_SHORT).show();
            //creating intent of this activity
            Intent intent = getIntent();
            //relaunching this activity
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}