package fileworker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;

public class FileWorker {
    public static List<String> getWords() {
        List<String> l = new ArrayList<>();
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource("ListOfWords.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                l.add(line.toUpperCase());
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }
}

