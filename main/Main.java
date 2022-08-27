package main;

import java.io.IOException;
import java.util.Scanner;
import handlers.handler;
import logs.log;

class Main 
{        
    static String name = "";
    static log l = new log();
    static Scanner scan;
    public static void main(String[] args) throws IOException 
    {

        String input = setup();

        if(input.equals("j"))
        {
            l.createNewLogFile("Client_logs.txt");
            String ip = "";
            int port = -1;
            String yesno = "";
            String Name = "";
            while(!yesno.equalsIgnoreCase("y") && !yesno.equalsIgnoreCase("n"))
            {
                System.out.println("Would you like to connect to someone you have previously connected to? (y/n)");
                yesno = scan.next();
            }
            prompt: do {
            if(yesno.equalsIgnoreCase("y"))
            {
                System.out.println("Enter their windows account name: ");
                Name = scan.next();
                if(l.readLogs("Client_logs.txt").toLowerCase().indexOf(Name.toLowerCase()) != -1)
                {
                    System.out.println("Friend Found!");
                    for(int i = 0; i < 3; i++){try {Thread.sleep(250);} catch (InterruptedException e) {e.printStackTrace();} System.out.print('.');}
                    System.out.println();

                    break prompt;
                }
                else
                {
                    System.out.println("We haven't found anyone with that name. Was that a typo? (y/n): ");
                    char typo = scan.next().charAt(0);
                    if(typo == 'y')
                    {
                        continue prompt;
                    }
                    else if(typo == 'n')
                    {
                        break prompt;
                    }
                }
            }
            else 
            {
                break prompt;
            }
        } while(true);

            while(!(ip.length() >= 7))
            {
                System.out.println("Enter IP Address:");
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

            try {
                h.sendNewMessage(h.connection, System.getProperty("user.name"));
                name = h.getNewMessage(h.connection);
                l.writeLogs("Client Log: " + name + " " + ip + " " + port);
                //printLogsToConsole(false);
            } 
            catch (IOException e1) {System.out.println("An unexpected error has occurred");}
            int i = 0;
            while(h.connection.isConnected() && !(h.connection.isClosed()))
            {
                if(i == 0)
                    System.out.println("Have fun!");
                
                try {
                    t1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(true)
                                try {System.out.println(name + ": " + h.getNewMessage(h.connection));} catch (IOException e) {System.out.println("An unexpected error has occurred");break;}
                        }
                    });
                    t1.start();

                    h.sendNewMessage(h.connection, scan.nextLine());

                } catch (IOException e) {System.out.println("An unexpected error has occurred... Closing program");break;}
                i++;
                
            }
            try {h.closeConnections(true); t1.join(); l.closeStreams();} catch (IOException | InterruptedException e1) {System.out.println("An unexpected error has occurred");}

            scan.close();

            return;
        } // j

        l.createNewLogFile("Server_logs.txt");

        //printLogsToConsole(true);
        // h
        String PORT = "";
        
        while(!(PORT.length() > 0))
        { 

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

        try {

            h.sendNewMessage(h.server, System.getProperty("user.name"));

            name = h.getNewMessage(h.server);

            l.writeLogs("Server Log: " + name + " " + h.server.getRemoteSocketAddress().toString() + " " + PORT);

        } catch (IOException e1) {System.out.println("An unexpected error has occurred");}

        int i = 0;

        while(h.server.isConnected() && !(h.server.isClosed()))
        {
            if(i == 0)
                System.out.println("Have fun!");
            
            try {

                t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(true)
                            try {System.out.println(name + ": " + h.getNewMessage(h.server));} catch (IOException e) {System.out.println("An unexpected error has occurred");}
                    }
                });
                t1.start();
                h.sendNewMessage(h.server, scan.nextLine());


            } catch (IOException e) {System.out.println("An unexpected error has occurred... Closing program");break;}
            i++;
        }
        try {t1.join(); h.closeConnections(true);l.closeStreams();} catch (InterruptedException | IOException e) {System.out.println("An unexpected error has occurred");}
        scan.close();
    }

    public static String setup()
    {
        // SETUP
        scan = new Scanner(System.in); 
        String input = "";

        System.out.println("If you continue you agree that this program can do the following: Connect to other computers, access your windows account name, and access your IP address if necessary (will not be stored outside of your computer).");

        while(!input.equals("h") && !input.equals("j")){
            System.out.println("Do you want to host, or join a friend? h/j:");
            input = scan.next();
        }
        return input;
        // SETUP
    }

    public static void printLogsToConsole(boolean isServer)
    {
        if(!isServer)
        {
            try {
                System.out.println(l.readLogs("Client_logs.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            System.out.println(l.readLogs("Server_logs.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
