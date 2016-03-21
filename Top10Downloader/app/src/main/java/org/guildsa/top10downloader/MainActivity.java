package org.guildsa.top10downloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button btnParse;
    private ListView xmlListView;
    private String mFileContents; // Retrieved data returned from our downloadXMLFile method.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnParse = (Button) findViewById(R.id.btnParse);
        btnParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseApplications parseApplications = new ParseApplications(mFileContents);
                parseApplications.process();
            }
        });
        xmlListView = (ListView) findViewById(R.id.xmlListView);

        // Instantiate our private async task class and execute it with our Apple xml link.
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
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

    // Our private class will take advantage of the asynchronous processing that's built into Android.
    // AsyncTask<params, progress, result> takes three parameters that we'll define in order to set
    // up the task. First is the type of the parameters sent to the task upon execution. Our xml file
    // links from Apple will be of type String. Second is the type of the progress units published
    // during the background computation. We don't have a progress bar. Our async task simply executes
    // so we return Void. Third is the type of the result of the background computation, which will
    // be of type String.
    private class DownloadData extends AsyncTask<String, Void, String> {



        // Our doInBackground method will execute and process whatever the async task need to be done.
        // Then it automatically calls the onPostExecute method.
        @Override
        protected String doInBackground(String... params) {
            // Any methods that takes in a parameter type... means when we call the method, we are
            // allowed to pass in a variable number of arguments of that specified type.
            mFileContents = downloadXMLFile(params[0]);
            if (mFileContents == null) {
                Log.d("DownloadData", "Downloading error");
            }

            return mFileContents;
        }

        // The doInBackground's returned String mFileContents is what is being passed in as the
        // argument on our onPOstExecute method.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("DownloadData", "Result was: " + result);


        }

        private String downloadXMLFile(String urlPath) {
            // XML files generally are not downloaded all at once. They can be pulled 500 characters at
            // a time for example. We'll need a buffer to hold each piece of the download as they're
            // pulled.
            StringBuilder tempBuffer = new StringBuilder();

            try {
                // Define the url based on the url path string that's passed in. Open an http url
                // connection to check if the file can be extracted.
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Get the HTTP response code and log it in order to test the open connection.
                int response = connection.getResponseCode();
                Log.d("DownloadData", "The response code was " + response);

                // Define the input stream by using our connection. Then process what's inside that
                // stream by using the input stream reader.
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                // Define a couple of local variables to store what's read in our input stream. We'll
                // read the input 500 characters at a time until the end of the stream. The number of
                // chars through the while loop each time is stored in the charRead variable. If
                // charRead becomes 0 then the stream reader has reached the end of the stream and
                // we exit the while loop.
                int charRead;
                char[] inputBuffer = new char[500];

                while (true) {
                    charRead = isr.read(inputBuffer);

                    if (charRead <= 0) {
                        break;
                    }

                    // Take the characters from our character array, copy them and append it to our
                    // final string builder variable up top.
                    // The String method definition is copyValueOf(char[] data, int start, int length).
                    tempBuffer.append(String.copyValueOf(inputBuffer, 0, charRead));
                }

                return tempBuffer.toString();
            }
            catch (IOException e) {
                Log.d("DownloadData", "IO Exception reading data: " + e.getMessage());
                e.printStackTrace();
            }
            catch (SecurityException e) {
                Log.d("DownloadData", "Security Exception. Needs permission? " + e.getMessage());
                e.printStackTrace();
            }

            // This return statement is needed to break out of our downloadXMLFile method in case
            // an exception occurred.
            return null;
        }
    }
}
