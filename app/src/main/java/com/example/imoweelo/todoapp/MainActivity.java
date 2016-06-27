package com.example.imoweelo.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> todoItems;
    ArrayAdapter<String> aToDoAdapter;
    ListView lvItems;
    EditText etEditText;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();                                                                          // on app launch, populate the list with memory file items or no items if 1st time
        lvItems = (ListView) findViewById(R.id.lvItems);                                               // instantiate view for list items in UI
        lvItems.setAdapter(aToDoAdapter);                                                              // link array adapter to our list items view [LINK #2], now default text is visible in UI
        etEditText = (EditText) findViewById(R.id.etEditText);                                         // instantiate edit text view in UI
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {                 // handles long click of list item (for deleting item)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);                                                            // delete item from our array list in Java
                aToDoAdapter.notifyDataSetChanged();                                                   // sync with the adapater to update the list in the UI
                writeItems();                                                                          // update the current array list to the memory file
                //Toast on success
                Toast.makeText(MainActivity.this, "Item Deleted!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {                         // handles short click of list item (for editing item)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);                      // define explicit intent to launch the edit activity
                i.putExtra("position",position);
                i.putExtra("text",aToDoAdapter.getItem(position));
                startActivityForResult(i,REQUEST_CODE);                                                // launch the edit activity
            }
        });
    }

    // on app launch, populate the list with memory file items or no items if 1st time
    public void populateArrayItems(){
        readItems();
        aToDoAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,todoItems);   // link array adapter to our array list "todoItems" [LINK #1]
    }

    // when clicking "Add" button to add an item after typing into plain text edit box
    public void onAddItem(View view) {
        aToDoAdapter.add(etEditText.getText().toString());                                             // add edit text to item list (last row) through the adapter directly
        etEditText.setText("");                                                                        // set edit text to empty (clear what user wrote)
        writeItems();                                                                                  // update the current array list to the memory file
        //Toast on success
        Toast.makeText(this, "New Item Added!", Toast.LENGTH_SHORT).show();
    }

    // reads from the file where the list is written
    private void readItems(){
        File filesDir = getFilesDir();
        File file = new File(filesDir,"todo.txt");                          // create file <<<< if it doesn't exist, else just open it with file pointer as "file">>>??????
        try{
            todoItems = new ArrayList<String>(FileUtils.readLines(file));   // instantiate our array list "todoItems" as reading all lines in file
        }
        catch(IOException e){
            todoItems = new ArrayList<String>();                            // instantiate our array list "todoItems" as empty, otherwise app will crash if file doesn't exit on first run
        }
    }

    // write to the file where the list is written
    private void writeItems(){
        File filesDir = getFilesDir();
        File file = new File(filesDir,"todo.txt");                          // create file <<<< if it doesn't exist, else just open it with file pointer as "file">>>??????
        try{
            FileUtils.writeLines(file,todoItems);                           // write lines to file for the whole array list "todoItems"
        }catch(IOException e){ }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent j) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String text = j.getStringExtra("text");
            int position = j.getIntExtra("position",0);
            todoItems.set(position,text);                                                      // delete item from our array list in Java
            aToDoAdapter.notifyDataSetChanged();                                                   // sync with the adapater to update the list in the UI
            writeItems();                                                                                  // update the current array list to the memory file
            //Toast on success
            Toast.makeText(this, "Item Edited!", Toast.LENGTH_SHORT).show();
        }
    }
}
