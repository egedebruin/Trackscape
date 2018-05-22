package handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsonHandler {

    private JsonElement jsonElement;

    public JsonHandler(String fileName) {
        File file = new File(fileName);
        JsonParser parser = new JsonParser();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            jsonElement = parser.parse(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
