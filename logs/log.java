package logs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class log {
    

    String defaultLogFileName = "logs.txt";
    File logFile;

    public void createNewLogFile(String filename)
    {
        logFile = new File(filename);
    }

    public void writeLogs(String log)
    {
        try {
            FileWriter fw = new FileWriter(defaultLogFileName);
            fw.append(log + '\n');
            fw.flush();
            fw.close();
        } catch (IOException e) {}
    }
}
