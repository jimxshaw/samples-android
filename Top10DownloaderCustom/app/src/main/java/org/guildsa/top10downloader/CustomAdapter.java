package org.guildsa.top10downloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Application> {
    private final Context context;
    private final ArrayList<Application> values;

    public CustomAdapter(Context context, ArrayList<Application> values) {
        super(context, R.layout.list_item, values);

        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        // Grab the views we want by their ids and cast them into the appropriate types.
        TextView app = (TextView) rowView.findViewById(R.id.app);

        //ImageView image = (ImageView) rowView.findViewById(R.id.image);

        // Retrieve each application from our array list of applications according to its position.
        Application rawApplication = values.get(position);

        // Retrieve the image url for each app.
        String appImageUrl = rawApplication.getImageUrl();

        // Instantiate our utility class to capture the app image from the app image url and set
        // that image to our list item image view.
        new DownloadImageTask((ImageView) rowView.findViewById(R.id.image)).execute(appImageUrl);

        // Capture and set the fields of each app to our list item text view.
        app.setText(rawApplication.toString());

        return rowView;
    }


    // This is an utility class that will asynchronously go to an image url, capture the image file,
    // convert it to a Bitmap object. The bitmap object will then be set to an image view.
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bitmapImageView;

        public DownloadImageTask(ImageView bmImage) {
            this.bitmapImageView = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            Bitmap bitmapImage = null;

            try {
                InputStream inputStream = new java.net.URL(imageUrl).openStream();
                bitmapImage = BitmapFactory.decodeStream(inputStream);
            }
            catch (Exception e) {
                Log.e("ImageUrl", e.getMessage());
                e.printStackTrace();
            }

            return bitmapImage;
        }

        protected void onPostExecute(Bitmap result) {
            bitmapImageView.setImageBitmap(result);
        }

    }
}
