package handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class handler 
{
    String IP;
    int PORT;
    public ServerSocket ssocket;
    public Socket server;

    BufferedReader bf;
    InputStreamReader in;
    PrintWriter pw;

    public Socket connection;
    public handler(String ipaddress, int port) 
    {
        this.PORT = port;
        this.IP = ipaddress;
    }

    public int setupHost() throws IOException
    {
        ssocket = new ServerSocket(PORT);
        
        server = ssocket.accept();  
        /*while(!this.server.isConnected())
        {
            System.out.println("Waiting for incoming connections...");
            try {System.out.wait(1000);} catch (InterruptedException e) {e.printStackTrace();}
        }*/
        return 1;
    }

    public void setupConnection()
    {
        try {
            connection = new Socket(this.IP, this.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getNewMessage(Socket s) throws IOException
    {
        if (!s.isConnected())
        {
            return null;
        }
        in = new InputStreamReader(s.getInputStream());
        bf = new BufferedReader(in);
        return bf.readLine();
    }
    public void sendNewMessage(Socket s, String message) throws IOException
    {
        if(!s.isConnected())
        {
            return;
        }
        pw = new PrintWriter(s.getOutputStream());
        pw.println(message);
        pw.flush();
    }
    public void closeConnections(boolean isServer) throws IOException // If true will attempt to close server related variables
    {
        if(!isServer)
        {
            connection.close();
            return;
        }
        pw.flush();
        pw.close();
        bf.close();
        in.close();
        ssocket.close();
        server.close();

    }

}
