package ThreadDesignPattern;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Downloader {

    public static List<String> download() throws IOException{
        HttpURLConnection connection = (HttpURLConnection) new URL("http://www.baidu.com/").openConnection();
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))){
            String line;
            while ((line = reader.readLine()) != null){
                lines.add(line);
            }
        }
        return lines;
    }

}
