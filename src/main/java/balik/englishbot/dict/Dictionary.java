package balik.englishbot.dict;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dictionary {
    private static Logger LOG = Logger.getLogger(Dictionary.class);
    private final static String INPUT = "/vocabulary/dictionary.txt";
    private Map<String, String> words = new HashMap<>();
    private List<String> keys = new ArrayList<>();
    private String dictionaryAsString = "";
    private static Dictionary instance = null;

    private Dictionary() {
        createDictionary();
    }

    public static synchronized Dictionary getInstance() {
        if (instance == null) {
            instance = new Dictionary();
            LOG.info("Dictionary instance created.");
        }
        return instance;
    }

    public synchronized String getDictionaryAsString() {
        return dictionaryAsString;
    }

    private synchronized void createDictionary() {
        try (InputStream in = this.getClass().getResourceAsStream(INPUT);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {

            String line = bufferedReader.readLine();
            int i = 1;
            while (line != null) {
                String[] parseString = line.split("=");
                //todo: replace with stringbuilder
                dictionaryAsString += (i++) + ". " + parseString[0] + "—" + parseString[1] + "\n";
                keys.add(parseString[1]);
                words.put(parseString[1], parseString[0]);
                line = bufferedReader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("Dictionary creation error!\n" + e.getMessage());
        }
    }

    public synchronized String getAnswer(String key) {
        return words.get(key);
    }

    public String getKeyByIndex(int index) {
        return keys.get(index - 1);
    }

    public Integer getSize() {
        return keys.size();
    }
}
