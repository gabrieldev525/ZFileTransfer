package com.gabrieldev525.zfiletransfer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.gabrieldev525.zfiletransfer.adapter.FTPBase;
import com.gabrieldev525.zfiletransfer.adapter.FtpListAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<FTPBase> ftpConnList;
    private ListView ftpConnListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ftpConnList = new ArrayList<FTPBase>();

        for(int i = 0; i < 10; i++) {
            FTPBase ftpConn = new FTPBase();
            ftpConn.setHost("Host " + i);
            ftpConn.setPort(80);
            ftpConn.setName("Connection " + i);

            ftpConnList.add(ftpConn);
        }

        ftpConnListView = (ListView) findViewById(R.id.ftp_listview);
        ftpConnListView.setAdapter(new FtpListAdapter(this, ftpConnList));
    }
}
