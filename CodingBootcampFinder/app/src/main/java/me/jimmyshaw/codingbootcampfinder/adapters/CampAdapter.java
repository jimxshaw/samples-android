package me.jimmyshaw.codingbootcampfinder.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import me.jimmyshaw.codingbootcampfinder.R;
import me.jimmyshaw.codingbootcampfinder.models.Camp;

public class CampAdapter extends RecyclerView.Adapter<CampAdapter.CampHolder> {

    private ArrayList<Camp> mCamps;

    private Context mContext;

    public CampAdapter(Context context, ArrayList<Camp> camps) {
        mContext = context;
        mCamps = camps;
    }

    @Override
    public CampHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_camp_card_view, parent, false);

        return new CampHolder(cardView);
    }

    @Override
    public void onBindViewHolder(CampHolder holder, int position) {
        final Camp camp = mCamps.get(position);
        holder.updateUI(camp);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Camp details clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCamps.size();
    }

    public class CampHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewCampTitle;
        private TextView mTextViewCampAddress;
        private ImageView mImageViewCampImage;

        public CampHolder(View itemView) {
            super(itemView);

            mTextViewCampTitle = (TextView) itemView.findViewById(R.id.text_view_camp_title);
            mTextViewCampAddress = (TextView) itemView.findViewById(R.id.text_view_camp_address);
            mImageViewCampImage = (ImageView) itemView.findViewById(R.id.image_view_camp_image);

        }

        public void updateUI(Camp camp) {
            String uri = camp.getImgUrl();

            int resource = mImageViewCampImage.getResources().getIdentifier(uri, null, mImageViewCampImage.getContext().getPackageName());

            mImageViewCampImage.setImageResource(resource);
            mTextViewCampTitle.setText(camp.getLocationTitle());
            mTextViewCampAddress.setText(camp.getLocationAddress());
        }
    }
}
