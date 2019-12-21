package com.gabrieldev525.zfiletransfer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NewConnection extends AppCompatActivity {

    private Button newConnectionBtn, cancelConnectionBtn;
    private EditText connHost, connName, connPort, connPassword, connUsername;
    private TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_connection);

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
                    Toast.makeText(NewConnection.this, "Sucesso", Toast.LENGTH_LONG).show();
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