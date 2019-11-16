package com.example.rssreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rssreader.Models.RSSItem;
import com.example.rssreader.Utilities.OffsetDateTimeToStringConverter;

import java.time.OffsetDateTime;
import java.util.List;

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
            holder.itemTitleView.setText(current.title);
            holder.itemPubDateView.setText(OffsetDateTimeToStringConverter.getStringFromDate(current.pubDate));
            holder.itemDescriptionView.setText(getDescriptionForItem(current.description));

            ImageDownloader id = new ImageDownloader();
            id.download(current.image, holder.itemImageView);
        } else {
            // Covers the case of data not being ready yet.
            holder.itemTitleView.setText("No news");
        }
    }

    void setItems(List<RSSItem> rssItems) {
        items = rssItems;
        notifyDataSetChanged();
    }

    private String getDescriptionForItem(String fullDescription) {
        String[] words = fullDescription.split(" ");
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < 15; i++) {
            if(!words[i].equals("\n") &&!words[i].equals("\t"))
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
