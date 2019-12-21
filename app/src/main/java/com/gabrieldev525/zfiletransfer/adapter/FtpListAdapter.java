package com.gabrieldev525.zfiletransfer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gabrieldev525.zfiletransfer.R;

import java.util.ArrayList;

public class FtpListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FTPBase> list;

    public FtpListAdapter(Context context, ArrayList<FTPBase> list) {
        this.list = list;
        this.context = context;

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
        FTPBase ftpBase = list.get(position);
        View layout;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.ftp_list, null);
        } else {
            layout = convertView;
        }

        TextView connectionName = (TextView) layout.findViewById(R.id.connection_name);
        TextView connectionHostPort = (TextView) layout.findViewById(R.id.connection_host_port);

        connectionName.setText(ftpBase.getName());
        connectionHostPort.setText(ftpBase.getHost() + ':' + ftpBase.getPort());


        return layout;
    }
}
