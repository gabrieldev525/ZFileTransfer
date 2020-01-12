package com.gabrieldev525.zfiletransfer;

import android.os.AsyncTask;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.Serializable;

public class ConnectionManager implements Serializable {

    private FTPClient ftpClient;

    /**
     * setter and getter of the ftp client
     */
    public void setClient(FTPClient client) {
        this.ftpClient = client;
    }
    public FTPClient getClient() {
        return this.ftpClient;
    }

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
            ftpClient.connect(host);
            boolean status = ftpClient.login(username, password);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            return status;
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

    /**
     *
     * @return a array of strings with the name of all files and folders in the directory
     */
    public String[] listCurrentDirectory() {
        try {
            return ftpClient.listNames();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *
     * @param path - the path to the directory to enter
     * @return true if the access is ok, else return false. In case of error, return false true
     */
    public boolean openDirectory(String path) {
        try {
            return this.ftpClient.changeWorkingDirectory(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
