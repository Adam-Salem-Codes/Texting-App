package logs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class log {

    String defaultLogFileName = "logs.txt";
    File logFile;
    private FileWriter fw;
    private BufferedReader br;
    public void createNewLogFile(String filename)
    {
        logFile = new File(filename);
    }

    public void writeLogs(String log)
    {
        try {
            fw = new FileWriter(logFile);
            fw.append(log + '\n');
            fw.flush();
        } catch (IOException e) {System.out.println("An error has occurred while writing to the connection logs....");}
    }
    public String readLogs(String filename) throws IOException
    {
        Scanner sc = new Scanner(new File(filename));
        String data = "";
        while (sc.hasNextLine())
          data += sc.nextLine();

        return data;
    }

    public void closeStreams() throws IOException
    {
        br.close();
        fw.close();
    }
}
