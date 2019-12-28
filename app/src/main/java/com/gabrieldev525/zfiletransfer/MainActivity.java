package com.gabrieldev525.zfiletransfer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.gabrieldev525.zfiletransfer.adapter.FTPBase;
import com.gabrieldev525.zfiletransfer.adapter.FtpListAdapter;
import com.gabrieldev525.zfiletransfer.database.Controller;
import com.gabrieldev525.zfiletransfer.database.FTPDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ArrayList<FTPBase> ftpConnList;
    private ListView ftpConnListView;
    private FtpListAdapter ftpListAdapter;
    private FloatingActionButton newConnectionBtn;

    // request code to start activity for result
    private static final int CREATE_CONNECTION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the list view
        ftpConnList = new ArrayList<FTPBase>();

        Controller controller = new Controller(getBaseContext());
        Cursor cursor = controller.getConnections();

        // iterate with the cursor data and set it in the list view
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            FTPBase ftpConn = new FTPBase();

            // get the current iterator cursor data to set in the list
            String name = cursor.getString(cursor.getColumnIndex(FTPDB.NAME));
            String host = cursor.getString(cursor.getColumnIndex(FTPDB.HOST));
            int port = cursor.getInt(cursor.getColumnIndex(FTPDB.PORT));
            int id = cursor.getInt(cursor.getColumnIndex(FTPDB.ID));

            // set the data in the class to appear in the list
            ftpConn.setHost(host);
            ftpConn.setPort(port);
            ftpConn.setName(name);
            ftpConn.setId(id);

            ftpConnList.add(ftpConn);
            cursor.moveToNext();
        }

        // set the adapter with items to list
        ftpConnListView = (ListView) findViewById(R.id.ftp_listview);
        ftpListAdapter = new FtpListAdapter(this, ftpConnList);
        ftpConnListView.setAdapter(ftpListAdapter);

        // initialize the new connection button
        newConnectionBtn = (FloatingActionButton) findViewById(R.id.newConnectionBtn);
        newConnectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewConnection.class);
                startActivityForResult(i, CREATE_CONNECTION);
            }
        });


        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("list-menu"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CREATE_CONNECTION) {
            if(resultCode == RESULT_OK) {
                FTPBase ftpConn = new FTPBase();
                ftpConn.setName(data.getStringExtra("name"));
                ftpConn.setHost(data.getStringExtra("host"));
                ftpConn.setPort(data.getIntExtra("port", 0));
                ftpConn.setId(data.getIntExtra("id", -1));
                ftpConnList.add(ftpConn);

                ftpListAdapter.notifyDataSetChanged();
            }
        }
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int removeConnectionIndex = intent.getIntExtra("remove_connection", -1);

            if(removeConnectionIndex != -1) {
                ftpConnList.remove(removeConnectionIndex);
                ftpListAdapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(),
                               getResources().getString(R.string.connection_deleted_successfully),
                               Toast.LENGTH_LONG).show();
            }
        }
    };
}
