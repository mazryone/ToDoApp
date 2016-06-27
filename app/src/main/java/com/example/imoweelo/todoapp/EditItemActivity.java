package com.example.imoweelo.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    EditText etEditText2;
    int globalposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        globalposition = getIntent().getIntExtra("position",0);                    // import position data for item clicked (integer)
        String text = getIntent().getStringExtra("text");                        // import text data for item clicked (string)

        //DEBUGGING MESSAGES TO LOG
        //Log.d("DEBUGTEXT", text);
        //Log.d("DEBUGPOSITION",String.valueOf(position));

        etEditText2 = (EditText) findViewById(R.id.etEditText2);                 //
        etEditText2.setText(text);                                               // populate edit box with text from the item clicked (before user does any editing)
        etEditText2.requestFocus();                                             // set focus
        etEditText2.setSelection(etEditText2.getText().length());
    }

    public void onSaveEdit(View view) {
        Intent j = new Intent(EditItemActivity.this, MainActivity.class);
        j.putExtra("text",etEditText2.getText().toString());
        j.putExtra("position",globalposition);
        setResult(RESULT_OK,j);
        finish();
    }
}
