package com.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MyServer {
    //use arrayList to stroe all of the socket and users
    ArrayList al = new ArrayList();
    ArrayList users = new ArrayList();
    //create the Serversocket and client Socket
    ServerSocket ss;
    Socket s;

    public static final int PORT = 10;
    public static final String UPDATE_USER = "update:";
    public static final String LOGOUT_MESSAGE = "@logout";


    public MyServer() {
        //create a server socket,bound to the specified port
        try {
            ss = new ServerSocket(PORT);
            System.out.println("Server Start" + ss);
            while (true) {
                //Listens for a connection to be made to this socket and accepts it
                s = ss.accept();
                //get a obejct of the MyThread
                Runnable r = new MyThread(s,al,users);
                //allocate a new thread object
                Thread t = new Thread(r);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
