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

        String appImageUrl = rawApplication.getImageUrl();

        new DownloadImageTask((ImageView) rowView.findViewById(R.id.image)).execute(appImageUrl);

        app.setText(rawApplication.toString());

        return rowView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap appImage = null;

            try {
                InputStream inputStream = new java.net.URL(urlDisplay).openStream();
                appImage = BitmapFactory.decodeStream(inputStream);
            }
            catch (Exception e) {
                Log.e("ImageUrl", e.getMessage());
                e.printStackTrace();
            }

            return appImage;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }
}
