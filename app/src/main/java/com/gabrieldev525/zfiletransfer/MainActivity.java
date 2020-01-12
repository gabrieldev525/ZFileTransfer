package com.gabrieldev525.zfiletransfer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gabrieldev525.zfiletransfer.adapter.FTPBase;
import com.gabrieldev525.zfiletransfer.adapter.FtpListAdapter;
import com.gabrieldev525.zfiletransfer.database.Controller;
import com.gabrieldev525.zfiletransfer.database.FTPDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ConnectionManager connectionManager;

    public ArrayList<FTPBase> ftpConnList = new ArrayList<FTPBase>();
    private ListView ftpConnListView;
    private FtpListAdapter ftpListAdapter;
    private FloatingActionButton newConnectionBtn;

    // request code to start activity for result
    public static final int CREATE_CONNECTION = 1;
    public static final int EDIT_CONNECTION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // this connectionManager contains the methods to work with the ftp
        connectionManager = new ConnectionManager();

        // set the adapter with items to list
        ftpConnListView = (ListView) findViewById(R.id.ftp_listview);
        ftpConnListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FTPBase ftpBase = ftpConnList.get(position);

                Log.i("ftp - info", ftpBase.getHost());
                Log.i("ftp - info", ftpBase.getUsername());
                Log.i("ftp - info", ftpBase.getPassword());
                Log.i("ftp - info", Integer.toString(ftpBase.getPort()));

                FtpClientConnect connect = new FtpClientConnect(ftpBase.getHost(), ftpBase.getUsername(), ftpBase.getPassword(),
                                                       ftpBase.getPort());

                connect.execute();
            }
        });
        ftpListAdapter = new FtpListAdapter(this, ftpConnList);
        ftpConnListView.setAdapter(ftpListAdapter);

        // load the connection from database
        loadConnection();

        // initialize the new connection button
        newConnectionBtn = (FloatingActionButton) findViewById(R.id.newConnectionBtn);
        newConnectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewConnection.class);
                startActivityForResult(i, CREATE_CONNECTION);
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("connection-action"));
    }

    public void loadConnection() {
        // initialize the list view
        ftpConnList.clear();

        Controller controller = new Controller(getBaseContext());
        Cursor cursor = controller.getConnections();

        // iterate with the cursor data and set it in the list view
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            FTPBase ftpConn = new FTPBase();

            // get the current iterator cursor data to set in the list
            String name = cursor.getString(cursor.getColumnIndex(FTPDB.NAME));
            String host = cursor.getString(cursor.getColumnIndex(FTPDB.HOST));
            String username = cursor.getString(cursor.getColumnIndex(FTPDB.USERNAME));
            String password = cursor.getString(cursor.getColumnIndex(FTPDB.PASSWORD));
            int port = cursor.getInt(cursor.getColumnIndex(FTPDB.PORT));
            int id = cursor.getInt(cursor.getColumnIndex(FTPDB.ID));

            // set the data in the class to appear in the list
            ftpConn.setHost(host);
            ftpConn.setPort(port);
            ftpConn.setName(name);
            ftpConn.setUsername(username);
            ftpConn.setPassword(password);
            ftpConn.setId(id);

            ftpConnList.add(ftpConn);
            cursor.moveToNext();
        }

        ftpListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CREATE_CONNECTION) {
            if(resultCode == RESULT_OK) {
                FTPBase ftpConn = new FTPBase();
                ftpConn.setName(data.getStringExtra("name"));
                ftpConn.setHost(data.getStringExtra("host"));
                ftpConn.setPort(data.getIntExtra("port", 0));
                ftpConn.setUsername(data.getStringExtra("username"));
                ftpConn.setPassword(data.getStringExtra("password"));
                ftpConn.setId(data.getIntExtra("id", -1));
                ftpConnList.add(ftpConn);

                ftpListAdapter.notifyDataSetChanged();
            }
        } else if(requestCode == EDIT_CONNECTION) {
            if(resultCode == RESULT_OK) {
                loadConnection();
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

    public class FtpClientConnect extends AsyncTask<String, Void, Boolean> {
        private String host;
        private String username;
        private String password;
        private int port;
        private AlertDialog alertLoading;

        public FtpClientConnect(String host, String username, String password, int port) {
            super();

            this.host = host;
            this.username = username;
            this.password = password;
            this.port = port;
        }

        @Override
        protected void onPreExecute() {
            AlertDialog.Builder loading = new AlertDialog.Builder(MainActivity.this);
            loading.setTitle(getResources().getString(R.string.connecting));
            loading.setMessage(getResources().getString(R.string.trying_connect_server) + "\n" + host + ":" + port);
            loading.setCancelable(false);
            loading.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cancel(true);
                }
            });
            loading.show();

            alertLoading = loading.create();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean status = connectionManager.ftpConnect(host, username, password, port);
            Log.w("status", "" + status);

            if(status) {
                String[] files = connectionManager.listCurrentDirectory();

//                for(int i = 0; i < files.length; i++) {
//                    Log.i("files", files[i]);
//                }
            }
            return status;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            alertLoading.dismiss();

            if(result) {
//                Log.i("files", connectionManager.listCurrentDirectory())

//                for(int i = 0; i < files.length; i++) {
//                    Log.i("files", files[i]);
//                }
                Intent i = new Intent(MainActivity.this, FTPFileManager.class);
//                i.putExtra("connection", connectionManager);
                startActivity(i);
            }
        }
    }
}
