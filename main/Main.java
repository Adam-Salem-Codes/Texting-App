package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String yesno;
        if(input.equals("j"))
        {
            l.createNewLogFile("Client_logs.txt");
            String ip = "";
            int port = -1;
            yesno = "";
            while(!yesno.equalsIgnoreCase("y") && !yesno.equalsIgnoreCase("n"))
            {
                System.out.println("Would you like to connect to someone you have previously connected to? (y/n)");
                yesno = scan.next();
            }
            // FRIENDS LIST PROMPT...

                prompt: do
                {
                    ArrayList<String> friendNames = new ArrayList<String>();
                    ArrayList<String> friendIPs = new ArrayList<String>();
                    ArrayList<Integer> friendPorts = new ArrayList<Integer>();
                    if(yesno.equalsIgnoreCase("y"))
                    {
                        String logs = l.readLogs("Client_logs.txt");

                        int numberOfFriends;
                        int length = 0;

                        BufferedReader br = new BufferedReader(new FileReader("Client_logs.txt"));
                        String currentLine;
                        BufferedReader br1 = new BufferedReader(new FileReader("Client_logs.txt"));
                        while(br1.readLine() != null)
                        {
                            currentLine = br.readLine();
                            if(currentLine.contains(".")) {
                                try {
                                    //System.out.println(length);
                                    friendNames.add(currentLine.split(": ")[1].split(",")[0]);
                                    friendIPs.add(currentLine.split(",")[1]);
                                    friendPorts.add(Integer.parseInt(friendIPs.get(length).split(":")[1]));
                                    friendIPs.set(length, friendIPs.get(length).split(":")[0].split("/")[1]);
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    System.out.print("Failed to load friends list");
                                    e.printStackTrace();
                                    dots();
                                    restartApplication(args);
                                }
                                
                                
                                //System.out.println(currentLine.split(": ")[1].split(",")[0]);         
                                length++;
                            }
                            System.out.println("has: " + hasDuplicate(friendNames));
                            System.out.println("get: " + getDuplicate(friendNames));
                            System.out.println("removed: " + String.valueOf(getDuplicate(friendNames).toString().replace("[", "").replace("]", "")));

                            if(hasDuplicate(friendNames))
                                friendNames.remove(String.valueOf(getDuplicate(friendNames).toString().replace("[", "").replace("]", "")));
                            
                            /*if((hasDuplicate(friendNames) && (getDuplicate(friendNames).get(t) instanceof String))){
                                if(friendNames.get(t).equalsIgnoreCase( (String) getDuplicate(friendNames).get(t) ) && t < friendNames.size() ){
                                    try {
                                    } catch (IndexOutOfBoundsException e) {
                                        System.out.print("An error has occurred");
                                        dots();
                                        String stackTrace;
                                        System.out.print("Would you like the stacktrace to be printed? y/n");
                                        stackTrace = scan.next();
                                        if(stackTrace.equalsIgnoreCase("y"))
                                        {
                                            e.printStackTrace();
                                        }
                                        else
                                        {
                                            System.out.print("Restarting");
                                            dots();
                                            restartApplication(args);
                                        }

                                    }
                                }
                            }*/
                        }
                        
                        numberOfFriends = friendNames.size();
                        br.close();
                        br1.close();
                        System.out.println("FRIENDS LIST: ");
                        for(int i = 0; i < numberOfFriends; i++)
                        {
                            System.out.println(friendNames.get(i) + ", " + friendIPs.get(i) + ", " + friendPorts.get(i));
                        }
                        boolean hasBeenFound = false;
                        System.out.println("Enter their windows account name: ");
                        String Name = scan.next();
                        int n = 0;
                        for(String name : friendNames)
                        {
                            if (name.equalsIgnoreCase("null"))
                            {
                                break;
                            }
                            System.out.println(Name);
                            System.out.println(friendNames.get(n));
                            if(friendNames.get(n).equalsIgnoreCase(Name))
                            {
                                System.out.print("Friend Found: " + Name);
                                dots();

                                ip = logs.split(",")[1];
                                port = Integer.parseInt(ip.split(":")[1]);
                                ip = ip.split(":")[0];
                                ip = ip.split("/")[1];
                                
                                System.out.println(ip + ", " + port);
                                hasBeenFound = true;
                                break prompt;
                            }
                            
                        }
                        if(!hasBeenFound)
                        {
                            System.out.println("We haven't found anyone with that name. Was that a typo? (y/n): ");
                            char typo = scan.next().charAt(0);
                            if(typo == 'y')
                            {

                                continue prompt;
                            }
                            else if(typo == 'n')
                            {
                                System.err.print("User could not be found in the log file");
                                dots();
                                System.out.print("Restarting");
                                dots();
                                try {
                                    restartApplication(args);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break prompt;
                            }
                            n++;
                     }
                }
                        
                        
                    else 
                    {
                        break prompt;
                    }
                } while(true);
                        // FRIENDS LIST PROMPT...
                l.createNewLogFile("Client_logs.txt");
            if(yesno.equalsIgnoreCase("n"))
            {
                
                while(!isValidIPAddress(ip))
                {
                    System.out.println("Enter IP Address:");
                    ip = scan.next();
                }
                while(port <= 0)
                {
                    System.out.println("Enter port:");
                    port = scan.nextInt();
                }
    
            }
            
            handler h = new handler(ip, port);
            System.out.print("Setting up connection");
            dots();
            int result = h.setupConnection();   
            int retry = 1;         
            while(result == 1 && !(retry > 2))
            {
                h.setupConnection();
                System.out.println("Try: " + retry + "/ 2");

                if(retry == 2)
                {
                    try {
                        System.out.print("Failed to connect to server restarting program");
                        dots();
                        restartApplication(args);
                    } catch (IOException e) {
                        System.out.print("Error while restarting exiting");
                        dots();
                        System.exit(1);
                    }
                }

                   
                retry++;
            }
            if(result == 2)
            {

            }

            Thread t1 = new Thread();

            try {
                h.sendNewMessage(h.connection, System.getProperty("user.name"));
                name = h.getNewMessage(h.connection);
                l.writeLogs("Client Log: " + name + ", " + h.connection.getRemoteSocketAddress().toString() + ", " + h.connection.getPort() + '.');
                //printLogsToConsole(false);
            } 
            catch (IOException | NullPointerException e1) {System.out.print("An unexpected error has occurred"); dots(); System.out.print("Restarting application"); dots(); restartApplication(args);}
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
        } // Connection j

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
        System.out.print("Waiting for connection");
        dots();
        //
        handler h = new handler("", Integer.parseInt(PORT));
        //
        h.setupHost();

        System.out.println("Server has connected...");

        Thread t1 = new Thread();

        try {

            h.sendNewMessage(h.server, System.getProperty("user.name"));

            name = h.getNewMessage(h.server);

            l.writeLogs("Server Log: " + name + ", " + h.server.getRemoteSocketAddress().toString() + ", " + h.server.getPort() + '.');

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
                            try {System.out.println(name + ": " + h.getNewMessage(h.server));} catch (IOException e) {System.out.println("An unexpected error has occurred"); break;}
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
            System.out.println(l.readLogs("Client_logs.txt"));
            return;
        }

        System.out.println(l.readLogs("Server_logs.txt"));

    }
    public static void dots()
    {
        for(int i = 0; i < 3; i++){try {Thread.sleep(250);} catch (InterruptedException e) {e.printStackTrace();} System.out.print('.');}
        System.out.println();
    }
    // Credit: https://stackoverflow.com/users/246263/veger, and https://www.codegrepper.com/code-examples/java/java+restart+program

    public static void restartApplication(String args[]) throws IOException 
    {
        new Main();
        Main.main(args);
    }
    //Credit for getDuplicate and hasDuplicate: https://stackoverflow.com/a/1404807
    @SuppressWarnings("rawtypes")
    public static <T> List getDuplicate(Collection<T> list) {

        final List<T> duplicatedObjects = new ArrayList<T>();
        Set<T> set = new HashSet<T>() {
        @Override
        public boolean add(T e) {
            if (contains(e)) {
                duplicatedObjects.add(e);
            }
            return super.add(e);
        }
        };
       for (T t : list) {
            set.add(t);
        }
        return duplicatedObjects;
    }
    
    
    public static <T> boolean hasDuplicate(Collection<T> list) {
        if (getDuplicate(list).isEmpty())
            return false;
        return true;
    }
    // Credit for validator: https://www.techiedelight.com/validate-ip-address-java/
    public static boolean isValidIPAddress(String ipv4)
    {
 
        // Regex for digit from 0 to 255.
        String zeroTo255
            = "(\\d{1,2}|(0|1)\\"
              + "d{2}|2[0-4]\\d|25[0-5])";
 
        // Regex for a digit from 0 to 255 and
        // followed by a dot, repeat 4 times.
        // this is the regex to validate an IP address.
        String regex
            = zeroTo255 + "\\."
              + zeroTo255 + "\\."
              + zeroTo255 + "\\."
              + zeroTo255;
 
        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
 
        // If the IP address is empty
        // return false
        if (ipv4 == null) {
            return false;
        }
 
        // Pattern class contains matcher() method
        // to find matching between given IP address
        // and regular expression.
        Matcher m = p.matcher(ipv4);
 
        // Return if the IP address
        // matched the ReGex
        return m.matches();
    }
}

