package umpl.analyzer;

import java.util.HashMap;
import java.util.List;

public class Anaylzer {
    HashMap<String, List<String>> links;

    public void pushLink(String link, List<String> places) {
        links.put(link, places);
    }
}
