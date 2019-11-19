package com.example.rssreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rssreader.Models.RSSItem;

import java.util.List;


public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView rssItemView;

        private ItemViewHolder(View itemView) {
            super(itemView);
            rssItemView = itemView.findViewById(R.id.titleTextView);
        }
    }

    private final LayoutInflater inflater;
    private List<RSSItem> items; // Cached copy of words

    ItemListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        if (items != null) {
            RSSItem current = items.get(position);
            holder.rssItemView.setText(current.getTitle());
        } else {
            // Covers the case of data not being ready yet.
            holder.rssItemView.setText("No news");
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
