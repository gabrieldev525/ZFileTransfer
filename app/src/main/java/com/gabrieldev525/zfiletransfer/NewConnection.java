package com.gabrieldev525.zfiletransfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gabrieldev525.zfiletransfer.database.Controller;

public class NewConnection extends AppCompatActivity {

    private Button newConnectionBtn, cancelConnectionBtn;
    private EditText connHost, connName, connPort, connPassword, connUsername;
    private TextView errorMessage;
    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_connection);

        // init database controller
        controller = new Controller(getBaseContext());

        // error message text view
        errorMessage = (TextView) findViewById(R.id.errorMessage);

        // initialize all the input
        connHost = (EditText) findViewById(R.id.inputConnHost);
        connName = (EditText) findViewById(R.id.inputConnName);
        connPort = (EditText) findViewById(R.id.inputConnPort);
        connPassword = (EditText) findViewById(R.id.inputConnPassword);
        connUsername = (EditText) findViewById(R.id.inputConnUsername);

        newConnectionBtn = (Button) findViewById(R.id.save_connection);
        newConnectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = connName.getText().toString();
                String host = connHost.getText().toString();
                String username = connUsername.getText().toString();
                String password = connPassword.getText().toString();
                String port = connPort.getText().toString();

                if(isEmpty(name) || isEmpty(host) || isEmpty(username) || isEmpty(password) ||
                   isEmpty(port)) {
                    setErrorMessage("Preencha todos os campos");
                } else {
                    boolean result = controller.createConnection(name, host, Integer.parseInt(port), username, password);

                    Intent resultIntent = new Intent();


                    if(result) {
                        Toast.makeText(NewConnection.this,
                                        getResources().getString(R.string.connection_created_successfully),
                                        Toast.LENGTH_LONG).show();

                        // sending data to list of connection
                        resultIntent.putExtra("name", name);
                        resultIntent.putExtra("host", host);
                        resultIntent.putExtra("port", Integer.parseInt(port));
                        setResult(RESULT_OK, resultIntent);
                    } else {
                        Toast.makeText(NewConnection.this,
                                        getResources().getString(R.string.fail_on_create_connection),
                                        Toast.LENGTH_LONG).show();

                        setResult(RESULT_CANCELED, null);
                    }

                    finish();
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