package com.gabrieldev525.zfiletransfer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.gabrieldev525.zfiletransfer.adapter.FTPBase;
import com.gabrieldev525.zfiletransfer.adapter.FtpListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<FTPBase> ftpConnList;
    private ListView ftpConnListView;
    private FloatingActionButton newConnectionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the list view
        ftpConnList = new ArrayList<FTPBase>();

        for(int i = 0; i < 10; i++) {
            FTPBase ftpConn = new FTPBase();
            ftpConn.setHost("Host " + i);
            ftpConn.setPort(80);
            ftpConn.setName("Connection " + i);

            ftpConnList.add(ftpConn);
        }

        // set the adapter with items to list
        ftpConnListView = (ListView) findViewById(R.id.ftp_listview);
        ftpConnListView.setAdapter(new FtpListAdapter(this, ftpConnList));

        // initialize the new connection button
        newConnectionBtn = (FloatingActionButton) findViewById(R.id.newConnectionBtn);
        newConnectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewConnection.class);
                startActivity(i);
            }
        });
    }
}
