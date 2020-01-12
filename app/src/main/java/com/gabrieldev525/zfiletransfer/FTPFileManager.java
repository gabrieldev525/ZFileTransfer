package com.gabrieldev525.zfiletransfer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.gabrieldev525.zfiletransfer.adapter.FileManagerAdapter;
import com.gabrieldev525.zfiletransfer.adapter.FileManagerBase;

import java.util.ArrayList;

public class FTPFileManager extends AppCompatActivity {
    private ArrayList<FileManagerBase> fileManagerList = new ArrayList<FileManagerBase>();
    private ListView fileManagerListView;
    private FileManagerAdapter fileManagerAdapter;

    private ConnectionManager connectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ftp_file_manager);

//        connectionManager = MainActivity.connectionManager;

//        String[] all_files = connectionManager.listCurrentDirectory();

        fileManagerListView = (ListView) findViewById(R.id.file_manager_list);

        for(int i = 0; i < 10; i++) {
            FileManagerBase file = new FileManagerBase();
            file.setName("file " + i);

            fileManagerList.add(file);
        }

        fileManagerAdapter = new FileManagerAdapter(this, fileManagerList);
        fileManagerListView.setAdapter(fileManagerAdapter);
    }
}
