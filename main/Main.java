package main;

import java.io.IOException;
import java.util.Scanner;
import handlers.handler;
import logs.log;

class Main 
{        
    static String name = "";

    public static void main(String[] args) 
    {
        Scanner scan = new Scanner(System.in); 
        String input = "";

        System.out.println("If you continue you agree that this program can do the following: Connect to other computers, access your windows account name, and access your IP address if necessary (will not be stored outside of your computer).");

        while(!input.equals("h") && !input.equals("j")){ 
            System.out.println("Do you want to host, or join a friend? h/j:");
            input = scan.next();
        }
        if(input.equals("j"))
        {
            String ip = "";
            int port = -1;

            while(!(ip.length() >= 7))
            {
                System.out.println("Enter IP Address (localhost for same wifi network): ");
                ip = scan.next();
            }
            while(port <= 0)
            {
                System.out.println("Enter port:");
                port = scan.nextInt();
            }
            handler h = new handler(ip, port);
            h.setupConnection();
            Thread t1 = new Thread();
            log l = new log();
            l.createNewLogFile("log.txt");
            try {
                h.sendNewMessage(h.connection, System.getProperty("user.name"));
                name = h.getNewMessage(h.connection);
                l.writeLogs(name);
            } 
            catch (IOException e1) {System.out.println("An unexpected error has occurred");}

            while(h.connection.isConnected() && !(h.connection.isClosed()))
            {
                System.out.println("Message...: ");
                try {
                    t1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(true)
                                try {System.out.println(name + ": " + h.getNewMessage(h.connection));} catch (IOException e) {System.out.println("An unexpected error has occurred");}
                        }
                    });
                    t1.start();
                    h.sendNewMessage(h.connection, scan.next());
                } catch (IOException e) {System.out.println("An unexpected error has occurred... Closing program");break;}

                
            }
            try {h.closeConnections(true); t1.join();} catch (IOException | InterruptedException e1) {System.out.println("An unexpected error has occurred");}
            scan.close();
            return;
        } // j
        
        // h
        String PORT = "";
        
        while(!(PORT.length() > 0)) { 
            System.out.print("Enter the port you would like to host on...:");
            PORT = scan.next();
            System.out.println();
        }
        //
        handler h = new handler("", Integer.parseInt(PORT));
        //
        try {h.setupHost();} catch (IOException e) {System.out.println("An unexpected error has occurred");}
        System.out.println("Server has connected...");
        Thread t1 = new Thread();
        log l = new log();
        l.createNewLogFile("log.txt");
        try {
            h.sendNewMessage(h.server, System.getProperty("user.name"));
            name = h.getNewMessage(h.server);
            l.writeLogs(name);
        } catch (IOException e1) {System.out.println("An unexpected error has occurred");}
        while(h.server.isConnected() && !(h.server.isClosed()))
        {
            try {

                t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(true)
                            try {System.out.println(name + ": " + h.getNewMessage(h.server));} catch (IOException e) {System.out.println("An unexpected error has occurred");}
                    }
                });
                t1.start();
                
                System.out.println("Message...: ");
                h.sendNewMessage(h.server, scan.next());


            } catch (IOException e) {System.out.println("An unexpected error has occurred... Closing program");break;}
            
        }
        try {t1.join(); h.closeConnections(true);} catch (InterruptedException | IOException e) {System.out.println("An unexpected error has occurred");}
        scan.close();
    }    
}
