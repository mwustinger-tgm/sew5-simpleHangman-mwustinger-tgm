package fileworker;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileWorker {
    public static void main(String[] args) {
        FileWorker.setHighscore("Martin", 3);
    }
    public static List<String> getWords() {
        List<String> l = new ArrayList<>();
        try {
            URI uri = Thread.currentThread().getContextClassLoader().getResource("ListOfWords.txt").toURI();
            File file = new File(uri);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                l.add(line.toUpperCase());
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    public static synchronized void setHighscore(String name, int remainingTries) {
        try {
            List<String> scores = new ArrayList<>();

            URI uri = Thread.currentThread().getContextClassLoader().getResource("Highscores.txt").toURI();
            File file = new File(uri);

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                scores.add(line);
            }
            br.close();

            for (int i = 0; i < scores.size(); i++) {
                String[] value = scores.get(i).split(" ");
                if (Integer.parseInt(value[0]) <= remainingTries) {
                    scores.add(i, remainingTries + " " + name);
                    break;
                }
            }

            PrintWriter pw = new PrintWriter(new FileWriter(file), true);

            for (int i = 0; i < scores.size() && i < 10; i++) {
                pw.println(scores.get(i));
            }
            pw.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

