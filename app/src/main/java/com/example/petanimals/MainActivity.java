package com.example.petanimals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.PrecomputedText;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import data.PetContract;
import data.PetContract.PetEntry;
import data.PetDbHelper;

public class MainActivity extends AppCompatActivity {
    public static PetDbHelper petDbHelper;
    public static SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById (R.id.fab);
        fab.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, EditorActivity.class);
                startActivity (intent);
            }
        });

        petDbHelper = new PetDbHelper (this);

        displayDatabaseInfo ();
    }

    private void insertPet() {

        sqLiteDatabase = petDbHelper.getWritableDatabase ();

        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues ();
        values.put (PetEntry.COLUMN_PET_NAME, "Toto");
        values.put (PetEntry.COLUMN_PET_BREED, "Terrier");
        values.put (PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put (PetEntry.COLUMN_PET_WEIGHT, 7);

        // Insert a new row for Toto in the database, returning the ID of that new row.
        // The first argument for sqLiteDatabase.insert() is the pets table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for Toto.
        long newRowId = sqLiteDatabase.insert (PetEntry.TABLE_NAME, null, values);
        Log.v ("MainActivity", "newRowId" + newRowId);

    }

    @Override
    protected void onStart() {
        super.onStart ();
        displayDatabaseInfo ();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater ().inflate (R.menu.menue_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId ()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet ();
                displayDatabaseInfo ();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected (item);
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {

        // Create and/or open a database to read from it
        SQLiteDatabase db = petDbHelper.getReadableDatabase ();

        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT
        };

        Cursor cursor = db.query (
                PetEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        TextView displayView = (TextView) findViewById (R.id.text_view_pet);

        try {
            // Create a header in the Text View that looks like this:
            //
            // The pets table contains <number of rows in Cursor> pets.
            // _id - name - breed - gender - weight
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.setText ("The pets table contains " + cursor.getCount () + " pets.\n\n");
            displayView.append (PetEntry._ID + " - " +
                    PetEntry.COLUMN_PET_NAME + " - " +
                    PetEntry.COLUMN_PET_BREED + " - " +
                    PetEntry.COLUMN_PET_GENDER + " - " +
                    PetEntry.COLUMN_PET_WEIGHT +
                    "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex (PetEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex (PetEntry.COLUMN_PET_NAME);
            int breedColumnIndex = cursor.getColumnIndex (PetEntry.COLUMN_PET_BREED);
            int genderColumnIndex = cursor.getColumnIndex (PetEntry.COLUMN_PET_GENDER);
            int weightColumnIndex = cursor.getColumnIndex (PetEntry.COLUMN_PET_WEIGHT);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext ()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt (idColumnIndex);
                String currentName = cursor.getString (nameColumnIndex);
                String currentBreed = cursor.getString (breedColumnIndex);
                int currentGender = cursor.getInt (genderColumnIndex);
                int currentWeight = cursor.getInt (weightColumnIndex);

                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append (("\n" + currentID + " - " +
                        currentName + " - " +
                        currentBreed + " - " +
                        currentGender + " - " +
                        currentWeight));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close ();
        }
    }
}
