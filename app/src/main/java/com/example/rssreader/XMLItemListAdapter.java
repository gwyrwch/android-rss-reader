package com.example.rssreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rssreader.Models.RSSItem;
import com.example.rssreader.Utilities.ByteBitmapConverter;
import com.example.rssreader.Utilities.OffsetDateTimeToStringConverter;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class XMLItemListAdapter extends RecyclerView.Adapter<XMLItemListAdapter.XMLItemViewHolder>{
    class XMLItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemTitleView, itemPubDateView, itemDescriptionView;
        private final ImageView itemImageView;

        private XMLItemViewHolder(View itemView) {
            super(itemView);
            itemTitleView = itemView.findViewById(R.id.titleTextView);
            itemPubDateView = itemView.findViewById(R.id.pubDateTextView);
            itemImageView = itemView.findViewById(R.id.imageView);
            itemDescriptionView = itemView.findViewById(R.id.descriptionTextView);
        }
    }

    private final LayoutInflater inflater;


    public List<RSSItem> getItems() {
        return items;
    }

    private List<RSSItem> items;
    private View.OnClickListener itemOnClickListener;

    XMLItemListAdapter(Context context, View.OnClickListener itemOnClickListener) {
        inflater = LayoutInflater.from(context);
        this.itemOnClickListener = itemOnClickListener;

//        final Timer timer =  new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                notifyDataSetChanged();
//            }
//        }, 0, 500);
    }

    @Override
    public XMLItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item, parent, false);
        itemView.setOnClickListener(itemOnClickListener);
        itemView.setTag(this);
        return new XMLItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(XMLItemViewHolder holder, int position) {
        if (items != null) {
            RSSItem current = items.get(position);
            holder.itemTitleView.setText(current.getTitle());
            holder.itemPubDateView.setText(OffsetDateTimeToStringConverter.getStringFromDate(current.getPubDate()));
            holder.itemDescriptionView.setText(getDescriptionForItem(current.getDescription()));

            holder.itemImageView.setImageBitmap(ByteBitmapConverter.getBitmapFromBytes(current.getBitmap()));

        } else {
            holder.itemTitleView.setText("No news");
        }
    }


    void setItems(List<RSSItem> rssItems) {
        items = rssItems;
        notifyDataSetChanged();
    }

    private String getDescriptionForItem(String fullDescription) {
        if (fullDescription == null) {
            return "";
        }
        fullDescription = fullDescription.trim();
        String[] words = fullDescription.split(" ");
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < Math.min(15, words.length); i++) {
            if(!words[i].equals("\n") && !words[i].equals("\t"))
                res.append(words[i]).append(" ");
        }
        res.append("...");

        return res.toString();
    }



    @Override
    public int getItemCount() {
        if (items != null)
            return items.size();
        else return 0;
    }
}
