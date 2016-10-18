package com.akash.applications.uploadimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private String encoded_string, image_name,datetime;
    private Bitmap bitmap;
    private File file;
    private Uri file_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                getFileUri();
                i.putExtra(MediaStore.EXTRA_OUTPUT,file_uri);
                startActivityForResult(i,10);
            }
        });
    }

    private void getFileUri() {
        datetime="2016-10-18 00:48:17";
        //datetime= new java.util.Date().toString();
        image_name="IMG_"+datetime;
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+File.separator+image_name);
        file_uri = Uri.fromFile(file);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==10 && resultCode==RESULT_OK)
            new Encode_image().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class Encode_image extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {

            bitmap = BitmapFactory.decodeFile(file_uri.getPath());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte[] array = stream.toByteArray();
            encoded_string = Base64.encodeToString(array,0);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest();
        }
    }

    private void makeRequest() {
        String param = "";
        try {
            param = URLEncoder.encode("type", "UTF-8")
                    + "=" + URLEncoder.encode("new", "UTF-8");

            param += "&" + URLEncoder.encode("uid", "UTF-8")
                    + "=" + URLEncoder.encode("100", "UTF-8");

            param += "&" + URLEncoder.encode("name", "UTF-8")
                    + "=" + URLEncoder.encode(image_name, "UTF-8");
            param += "&" + URLEncoder.encode("date", "UTF-8")
                    + "=" + URLEncoder.encode(datetime, "UTF-8");
            param += "&" + URLEncoder.encode("encoded_string", "UTF-8")
                    + "=" + URLEncoder.encode(encoded_string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpConnection http = new HttpConnection("http://akashapplications.hol.es/mfi/postimage.php");
        //Toast.makeText(this,param,Toast.LENGTH_LONG).show();
        http.sendPost(param);

        String rply=http.text.trim();
       //String rply = null;
        while(rply.equals(null)){
            Log.i("rply", rply);
            rply=http.serverReply().trim();
        }
      //  Toast.makeText(MainActivity.this,"Reply : "+rply.substring(0,25),Toast.LENGTH_LONG).show();
        if(rply.equals("success"))
            Toast.makeText(MainActivity.this,"Image Uploaded Successfully",Toast.LENGTH_LONG).show();
        else
        {
            Log.i("######","reply : "+rply);
            Toast.makeText(MainActivity.this,"Uploading failed",Toast.LENGTH_LONG).show();
        }
    }
}
