package com.gabrieldev525.zfiletransfer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gabrieldev525.zfiletransfer.R;

import java.util.ArrayList;

public class FileManagerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FileManagerBase> list;

    public FileManagerAdapter(Context context, ArrayList<FileManagerBase> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FileManagerBase file = list.get(position);
        View layout;


        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.ftp_file_list, null);
        } else {
            layout = convertView;
        }

        TextView fileName = layout.findViewById(R.id.file_name);
        fileName.setText(file.getName());

        return layout;
    }
}
