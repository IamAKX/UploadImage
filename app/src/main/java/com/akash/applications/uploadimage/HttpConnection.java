package com.akash.applications.uploadimage;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by amit on 14/10/16.
 */

public class HttpConnection {
    public static String url;

    /**
     *
     * @param url Url to connect
     */

    public static String text=new String();
   public HttpConnection(String url)
    {

        this.url=url;


    }
    public void sendPost(String param)
    {
        new NetworkManager().execute(param);
       // nw.execute(param);
    }

   public String serverReply()
    {

        return text;
    }

    /**
     *
     */
    private void POST(String param)
    {
        HttpURLConnection urlConnection = null;
        String urlParam=param;
        try{
            URL url = new URL(HttpConnection.url);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Length", "" +
                    Integer.toString(param.getBytes().length));
            urlConnection.setRequestProperty("Content-Language", "en-US");
            OutputStream os = urlConnection.getOutputStream();
            os.write(urlParam.getBytes());
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }


            text = sb.toString();
          //  UserInfoManager.setUsername(text);
            if(!text.isEmpty()) {
                Log.i("posti", text);
                setText(text);
            }
            else
                Log.i("post","EMPTY RESPONSE");
        }catch(Exception e){
            Log.d("Exception downloading", e.toString());
        }finally{

            if (urlConnection != null) urlConnection.disconnect();
        }

    }

    private void setText(String s) {

        text=s;
       Log.i("postiii", text);
    }

    private class NetworkManager extends AsyncTask<String,Integer,String> {
        // ProgressDialog dialog;




        @Override
        protected String doInBackground(String... strings) {
            POST(strings[0]);
            Log.i("posti", strings[0]);
            return text;
        }

        @Override
        protected void onPostExecute(String s) {
            if(text==null){
                return;
            }else{
                Log.i("afterpostreply",text);
            }
        }


    }
}
