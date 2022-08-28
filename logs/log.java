package logs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class log {

    String defaultLogFileName = "logs.txt";
    public File logFile;
    private BufferedWriter bw;
    private BufferedReader br;
    public void createNewLogFile(String filename)
    {
        logFile = new File(filename);
    }

    public void writeLogs(String log)
    {
        try {
            bw = new BufferedWriter(new FileWriter(logFile, true));
            bw.append(log + '\n');
            bw.flush();
        } catch (IOException e) {System.out.println("An error has occurred while writing to the connection logs");dots();}
    }
    public String readLogs(String filename)
    {
        Scanner sc = new Scanner("");
        try {sc = new Scanner(new File(filename));} catch (FileNotFoundException e) {System.out.println("An error has occured while trying to read the log file");dots();}
        String data = "";
        while (sc.hasNextLine())
          data += sc.nextLine();

        return data;
    }

    public void closeStreams() throws IOException
    {
        br.close();
        bw.close();
    }
    public void dots()
    {
        for(int i = 0; i < 3; i++){try {Thread.sleep(250);} catch (InterruptedException e) {e.printStackTrace();} System.out.print('.');}
        System.out.println();
    }
}
