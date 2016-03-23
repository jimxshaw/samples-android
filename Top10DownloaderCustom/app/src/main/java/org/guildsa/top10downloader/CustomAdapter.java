package org.guildsa.top10downloader;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        ImageView image = (ImageView) rowView.findViewById(R.id.image);

        // Retrieve each application from our array list of applications according to its position.
        Application rawApplication = values.get(position);

        String appImageUrl = rawApplication.getImageUrl();

        app.setText(rawApplication.toString());

        return rowView;
    }
}
