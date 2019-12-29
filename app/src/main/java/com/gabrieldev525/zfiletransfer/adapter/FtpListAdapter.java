package com.gabrieldev525.zfiletransfer.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.gabrieldev525.zfiletransfer.EditConnection;
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
                                AlertDialog.Builder alertConfirm = new AlertDialog.Builder(context);
                                alertConfirm.setTitle(context.getResources().getString(R.string.are_you_sure));
                                alertConfirm.setMessage(context.getResources().getString(R.string.really_delete_connection));
                                alertConfirm.setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        controller.deleteConnection(ftpBase.getId());

                                        Intent intent = new Intent("connection-action");
                                        intent.putExtra("remove_connection", position);
                                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                    }
                                });
                                alertConfirm.setNegativeButton(context.getResources().getString(R.string.cancel), null);
                                alertConfirm.show();
                                break;
                            case R.id.edit_connection:
                                Intent editIntent = new Intent(context, EditConnection.class);
                                editIntent.putExtra("id", ftpBase.getId());
                                editIntent.putExtra("name", ftpBase.getName());
                                editIntent.putExtra("host", ftpBase.getHost());
                                editIntent.putExtra("port", ftpBase.getPort());
                                editIntent.putExtra("username", ftpBase.getUsername());

                                ((Activity) context).startActivityForResult(editIntent, MainActivity.EDIT_CONNECTION);
                                break;
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
