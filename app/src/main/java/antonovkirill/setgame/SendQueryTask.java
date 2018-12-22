package antonovkirill.setgame;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendQueryTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String ... strings) {
        String data = strings[0];
        String server_url = strings[1];
        StringBuilder answer = new StringBuilder();

        try {
            URL url = new URL(server_url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            
            urlConnection.getOutputStream().write(data.getBytes());

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                answer.append(line);
            }

            urlConnection.disconnect();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return answer.toString();
    }

}
