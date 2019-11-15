package com.example.rssreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rssreader.Models.RSSItem;

import java.util.List;

public class XMLItemListAdapter extends RecyclerView.Adapter<XMLItemListAdapter.XMLItemViewHolder>{
    class XMLItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemTitleView, itemPubDateView;
        private final ImageView itemImageView;


        private XMLItemViewHolder(View itemView) {
            super(itemView);
            itemTitleView = itemView.findViewById(R.id.titleTextView);
            itemPubDateView = itemView.findViewById(R.id.pubDateTextView);
            itemImageView = itemView.findViewById(R.id.imageView);
        }
    }

    private final LayoutInflater inflater;
    private List<RSSItem> items; // Cached copy of words

    XMLItemListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public XMLItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new XMLItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(XMLItemViewHolder holder, int position) {
        if (items != null) {
            RSSItem current = items.get(position);
            holder.itemTitleView.setText(current.title);
            holder.itemPubDateView.setText(current.pubDate.toString());

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


    @Override
    public int getItemCount() {
        if (items != null)
            return items.size();
        else return 0;
    }
}
