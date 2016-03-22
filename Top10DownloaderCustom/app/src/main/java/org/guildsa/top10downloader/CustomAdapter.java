package org.guildsa.top10downloader;

import android.content.Context;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Application> {
    private final Context context;
    private final ArrayList<Application> values;

    public CustomAdapter(Context context, ArrayList<Application> values) {
        super(context, R.layout.list_item, values);

        this.context = context;
        this.values = values;
    }
}
