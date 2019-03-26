package collector;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;


public class HttpHelper {


    public static HttpResponse executeRequest(final String endpoint){
        HttpResponse response = null;
        HttpGet req = new HttpGet(endpoint);
        HttpClient client = HttpClientBuilder.create().build();
        try {
            response = client.execute(req);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


    public static String streamTostring(InputStream stream) {
        String data;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            int i;
            while ((i = stream.read()) != -1) {
                stringBuilder.append((char) i);
            }
            data = stringBuilder.toString();
        } catch (Exception e) {
            data = "No data Streamed.";
        }
        return data;
    }

}