package com.bbcnewsreader.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bbcnewsreader.R;
import com.bbcnewsreader.activities.DetailsActivity;
import com.bbcnewsreader.db.DBHelper;
import com.bbcnewsreader.fragments.BookmarkFragment;
import com.github.zagum.switchicon.SwitchIconView;

import java.util.ArrayList;

public class NewsfeedRecyclerAdapter extends RecyclerView.Adapter<NewsfeedRecyclerAdapter.NewsfeedRecyclerHolder> {

    private ArrayList<String> titles;
    private ArrayList<String> descriptions;
    private ArrayList<String> urlA;
    private boolean isBookmarked;
    private DBHelper dbHelper;

    public NewsfeedRecyclerAdapter(ArrayList<String> titles, ArrayList<String> description, ArrayList<String> urlA, boolean b) {
        this.titles = titles;
        this.descriptions = description;
        this.urlA = urlA;
        this.isBookmarked = b;
    }

    @NonNull
    @Override
    public NewsfeedRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_parse_rv, parent, false);
        return new NewsfeedRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsfeedRecyclerHolder holder, int position) {
        holder.title.setText(titles.get(position));
        holder.description.setText(descriptions.get(position));

        if (isBookmarked) holder.bookmarkSIV.setIconEnabled(true);


    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class NewsfeedRecyclerHolder extends RecyclerView.ViewHolder{

        private final TextView title;
        private final TextView description;
        private LinearLayout newsFeedLL;
        private SwitchIconView bookmarkSIV;

        public NewsfeedRecyclerHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleTV);
            description = itemView.findViewById(R.id.descriptionTV);
            newsFeedLL = itemView.findViewById(R.id.newsfeedLL);
            bookmarkSIV = itemView.findViewById(R.id.bookmarkSIV);
            dbHelper = new DBHelper(itemView.getContext());

            newsFeedLL.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), DetailsActivity.class);
                intent.putExtra("url", urlA.get(getAdapterPosition()));
                itemView.getContext().startActivity(intent);
            });

            bookmarkSIV.setOnClickListener(v -> {

                if (isBookmarked)
                {
                  boolean isDeleted = dbHelper.deleteNewsFeed(titles.get(getAdapterPosition()));

                    if (isDeleted){
                        BookmarkFragment.getInstance().removeNews(getAdapterPosition());
                        Toast.makeText(itemView.getContext(), "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(itemView.getContext(), "Error in deletion!", Toast.LENGTH_SHORT).show();                }
                else
                {
                    boolean isInserted = dbHelper.insertNewsFeed(titles.get(getAdapterPosition()),descriptions.get(getAdapterPosition()), urlA.get(getAdapterPosition()));
                    if (isInserted) Toast.makeText(itemView.getContext(), "Bookmarked Successfully!", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(itemView.getContext(), "Already bookmarked!", Toast.LENGTH_SHORT).show();
                }

            });

        }
    }

    public void updateList(ArrayList<String> titlesTemp, ArrayList<String> descriptionTemp, ArrayList<String> urlATemp){
        titles = titlesTemp;
        descriptions = descriptionTemp;
        urlA = urlATemp;
        notifyDataSetChanged();
    }
}
