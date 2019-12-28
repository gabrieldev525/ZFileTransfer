package com.gabrieldev525.zfiletransfer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gabrieldev525.zfiletransfer.MainActivity;
import com.gabrieldev525.zfiletransfer.R;
import com.gabrieldev525.zfiletransfer.database.Controller;

import java.util.ArrayList;

public class FtpListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FTPBase> list;
    private Controller controller;

    public FtpListAdapter(Context context, ArrayList<FTPBase> list) {
        this.list = list;
        this.context = context;

        controller = new Controller(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final FTPBase ftpBase = list.get(position);
        View layout;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.ftp_list, null);
        } else {
            layout = convertView;
        }

        TextView connectionName = (TextView) layout.findViewById(R.id.connection_name);
        TextView connectionHostPort = (TextView) layout.findViewById(R.id.connection_host_port);
        ImageView dropMenuList = (ImageView) layout.findViewById(R.id.menu_list);

        dropMenuList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.list_connection_menu, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch(item.getItemId()) {
                            case R.id.delete_connection:
                                controller.deleteConnection(ftpBase.getId());

                                Intent intent = new Intent("list-menu");
                                intent.putExtra("remove_connection", position);
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                            default:
                                break;
                        }

                        return true;
                    }
                });
            }
        });

        connectionName.setText(ftpBase.getName());
        connectionHostPort.setText(ftpBase.getHost() + ':' + ftpBase.getPort());


        return layout;
    }
}
