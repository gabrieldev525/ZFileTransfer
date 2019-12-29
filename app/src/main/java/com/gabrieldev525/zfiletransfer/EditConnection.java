package com.gabrieldev525.zfiletransfer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gabrieldev525.zfiletransfer.database.Controller;

public class EditConnection extends AppCompatActivity {
    private Button newConnectionBtn, cancelConnectionBtn;
    private EditText connHost, connName, connPort, connPassword, connUsername;
    private TextView errorMessage, formTitle;
    private Controller controller;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_connection);

        Intent intent = getIntent();

        // init database controller
        controller = new Controller(getBaseContext());

        // error message text view
        errorMessage = (TextView) findViewById(R.id.errorMessage);

        formTitle = (TextView) findViewById(R.id.form_connection_title);
        formTitle.setText(getResources().getString(R.string.edit_connection));

        id = intent.getIntExtra("id", -1);

        // initialize all the input
        connHost = (EditText) findViewById(R.id.inputConnHost);
        connHost.setText(intent.getStringExtra("host"));

        connName = (EditText) findViewById(R.id.inputConnName);
        connName.setText(intent.getStringExtra("name"));

        connPort = (EditText) findViewById(R.id.inputConnPort);
        connPort.setText(Integer.toString(intent.getIntExtra("port", 21)));

        connPassword = (EditText) findViewById(R.id.inputConnPassword);

        connUsername = (EditText) findViewById(R.id.inputConnUsername);
        connUsername.setText(intent.getStringExtra("username"));

        newConnectionBtn = (Button) findViewById(R.id.save_connection);
        newConnectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = connName.getText().toString();
                String host = connHost.getText().toString();
                String username = connUsername.getText().toString();
                String password = connPassword.getText().toString();
                String port = connPort.getText().toString();

                if(isEmpty(name) || isEmpty(host) || isEmpty(username) || isEmpty(port)) {
                    setErrorMessage("Preencha todos os campos");
                } else {
                    if(id != -1) {
                        controller.editConnection(id, name, host, Integer.parseInt(port), username, password);

                        Toast.makeText(EditConnection.this,
                                       getResources().getString(R.string.connection_edited_successfully),
                                       Toast.LENGTH_SHORT).show();

                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK, resultIntent);

                        finish();
                    } else {
                        Toast.makeText(EditConnection.this,
                                       getResources().getString(R.string.error_edit_id_undefined),
                                       Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cancelConnectionBtn = (Button) findViewById(R.id.cancel_connection);
        cancelConnectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean isEmpty(String text) {
        return text.trim().isEmpty();
    }

    private void setErrorMessage(String text) {
        errorMessage.setText(text);
    }
}
