package com.example.phonebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity {
    //initialising variables
    SQLiteDatabase db;
    EditText et1;
    Button b1, b2, b3;
    LinearLayout ll;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_xml);
        //connecting to sqlite database
        db = openOrCreateDatabase("phonebook", MODE_PRIVATE, null);
        //creating objects
        et1 = findViewById(R.id.a1_et1);
        b1 = findViewById(R.id.a1_b1);
        b2 = findViewById(R.id.a1_b2);
        ll = findViewById(R.id.a1_ll1);
        //adding listener to go button
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting name of searched contact from edit text
                String name = et1.getText().toString();
                //creating a new intent
                Intent intent = new Intent(Home.this, ContactDetails.class);
                //putting the name of contact to be searched in the intent
                intent.putExtra("name", name);
                //starting the intent
                startActivity(intent);
            }
        });
        //setting listener for add contact button
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a new intent
                Intent intent = new Intent(Home.this, AddContact.class);
                //starting the new intent
                startActivity(intent);
            }
        });
        //getting the intent which brought up this activity
        Intent intent = this.getIntent();
        //extracting a boolean variable check with default value false
        boolean check = intent.getBooleanExtra("check",false);
        //checking if the check is true and the search failed
        if(check) {
            //showing the toast to inform the user that the contact they searched does not exist
            Toast.makeText(this, "Contact doesn't exist!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    //creating a context menu
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //getting menu inflater
        MenuInflater mf = this.getMenuInflater();
        //storing reference of the button in variable b3
        b3 = (Button)v;
        //inflating the context menu
        mf.inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    //code to execute when a item from context menu is selected
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        //getting the string name from the button
        String name = b3.getText().toString();
        //checking if the item we selected is a edit button
        if(item.getItemId() == R.id.cm_i1) {
            //creating a intent sending user to edit contact page
            Intent intent = new Intent(Home.this, EditContacts.class);
            //putting contact name along with intent
            intent.putExtra("name", name);
            //starting the intent
            startActivity(intent);
        }
        //checking if the item selected is a delete button
        else if(item.getItemId() ==R.id.cm_i2) {
            //creating a delete query
            String query1 = "delete from contacts where name = '" + name + "'";
            //executing the query
            db.execSQL(query1);
            //creating a toast to inform the user that the contact has been deleted
            Toast.makeText(this, "Contact Deleted", Toast.LENGTH_SHORT).show();
            //creating a intent for this activity
            Intent intent = getIntent();
            //relaunching this activity
            startActivity(intent);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    //code that will run whenever this activity starts
    protected void onStart() {
        super.onStart();
        //remove all existing views
        ll.removeAllViews();
        //function class to show all contacts
        showContacts();
    }
    //function to display all the contacts
    void showContacts() {
        //creating a table called contacts if it already doesn't exist
        db.execSQL("create table if not exists contacts(name text, pno text, email text)");
        //creating a query to fetch all the names from our contacts table
        String query = "select name from contacts";
        //executing the query
        Cursor c = db.rawQuery(query, null);
        //checking if the cursor have any data
        if(c.moveToFirst()) {
            //doing while the cursor still have records
            do {
                //creating a new button
                Button b = new Button(Home.this);
                //registering it for context menu so we can later use the same
                registerForContextMenu(b);
                //setting text on button
                b.setText(c.getString(0));
                //setting text size
                b.setTextSize(35);
                //adding the button to linear layout
                ll.addView(b);
                //setting a listener for that button
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //creating a intent to contact details activity
                        Intent intent = new Intent(Home.this, ContactDetails.class);
                        //putting the name of contact with the intent
                        intent.putExtra("name", b.getText().toString());
                        //starting the contact details activity
                        startActivity(intent);
                    }
                });
            } while(c.moveToNext());
        }
        //this code will execute when there are no contacts saved
        else {
            //creating a new text view
            TextView tv = new TextView(Home.this);
            //setting text
            tv.setText("Nothing to show");
            //setting text size
            tv.setTextSize(24);
            //adding text view to liner layout
            ll.addView(tv);
        }
    }
}