package com.gabrieldev525.zfiletransfer;

import android.os.AsyncTask;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;

public class Utils {

    private FTPClient ftpClient;

    /**
     * this function connect with the ftp server
     * is seted the host and all the credentials to the server
     * this need be called before of all managment of ftp server
     *
     * @param host - the ftp host to connect
     * @param username - the username to connect in this ftp host
     * @param password - the password to connect in this ftp host
     * @param port - the port to access the ftp host, usually the port is 21 to ftp and 22 to ssh
     *
     * '@return true if the connection as successfully and false if it failed
     */
    public boolean ftpConnect(String host, String username, String password, int port) {
        try {
            ftpClient = new FTPClient();

            ftpClient = new FTPClient();
            ftpClient.connect(host);
            boolean status = ftpClient.login(username, password);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            return status;

//            //connect to te host
//            ftpClient.connect(host, port);
//
//            // check the response (reply) code, if true connection successfully
//            if(FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
//                boolean status = ftpClient.login(username, password);
//
//                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//                ftpClient.enterLocalPassiveMode();
//                return status;
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        return false;
    }


    /**
     * this function disconnect of ftp server
     *
     * @return true if the operation was successfully or false if it failed
     */
    public boolean ftpDisconnect() {
        try {
            ftpClient.logout();
            ftpClient.disconnect();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
