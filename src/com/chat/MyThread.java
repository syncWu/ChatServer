package com.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class MyThread implements Runnable{

    Socket s;
    ArrayList al;
    ArrayList users;
    String username;
    public MyThread(Socket s, ArrayList al, ArrayList users) {
        this.s = s;
        this.al = al;
        this.users = users;
        //use the input stream to add all of the socket or username
        try {
            DataInputStream dis = new DataInputStream(s.getInputStream());
            username = dis.readUTF();
            al.add(s);
            users.add(username);
            //tell everyone and send the new user list
            tellEveryOne(username+"login"+ new Date());
            sendNewUserList();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //login:the user said logout:remove user and current socket
    @Override
    public void run() {
        String s1;
        try {
            DataInputStream dis = new DataInputStream(s.getInputStream());
            //said
            do {
                s1 = dis.readUTF();
                if (s1.toLowerCase().equals(MyServer.LOGOUT_MESSAGE)) break;
                tellEveryOne(username + "said" + s1);
            }while(true);
            //if logout, remove the username and the current socket
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            dos.writeUTF(MyServer.LOGOUT_MESSAGE);
            //be sure that all your data from the buffer is writen
            dos.flush();
            users.remove(username);
            tellEveryOne(username + "logout");
            sendNewUserList();
            al.remove(s);
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void tellEveryOne(String s1) {
        //use the java util itertor to get all of the socket
        Iterator it = al.iterator();
        while (it.hasNext()){
            try {
                //use the output stream to broadcast the message s1 to all of the object
                Socket temp=(Socket)it.next();

                DataOutputStream dos=new DataOutputStream(temp.getOutputStream());
                dos.writeUTF(s1);
                //be sure that all your data from the buffer is writen
                dos.flush();

            }catch (Exception e){
                System.err.println(e);
            }
        }
    }

    private void sendNewUserList(){
        tellEveryOne(MyServer.UPDATE_USER + users.toString());
    }
}
