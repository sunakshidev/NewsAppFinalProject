package com.bbcnewsreader.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bbcnewsreader.R;
import com.bbcnewsreader.adapters.NewsfeedRecyclerAdapter;
import com.bbcnewsreader.db.DBHelper;

import java.util.ArrayList;

public class BookmarkFragment extends Fragment {

    private RecyclerView bookmarkRV;
    private ProgressBar progressBar;
    private EditText searchNewsFeedET;
    private DBHelper dbHelper;
    private ArrayList<String> titles, description, urlA;
    private NewsfeedRecyclerAdapter newsfeedRecyclerAdapter;
    private static BookmarkFragment instance;

    public static BookmarkFragment getInstance(){
        return instance;
    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bookmark_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reference(view);
        init();
        getNewsFromDB();

        searchNewsFeedET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString().toLowerCase());
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        instance = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    private void getNewsFromDB() {
        Cursor cursor = dbHelper.getNewsFeed();

        if (cursor.getCount()==0) Toast.makeText(getContext(), "There is No BookMarked!", Toast.LENGTH_SHORT).show();
        while (cursor.moveToNext())
        {
            titles.add(cursor.getString(1));
            description.add(cursor.getString(2));
            urlA.add(cursor.getString(3));
        }

        newsfeedRecyclerAdapter = new NewsfeedRecyclerAdapter(titles, description, urlA, true);
        bookmarkRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        bookmarkRV.setAdapter(newsfeedRecyclerAdapter);
        progressBar.setVisibility(View.GONE);
        bookmarkRV.setVisibility(View.VISIBLE);
    }

    private void init() {
        dbHelper = new DBHelper(getContext());
        titles = new ArrayList<>();
        description = new ArrayList<>();
        urlA = new ArrayList<>();
    }

    private void reference(View view) {
        bookmarkRV = view.findViewById(R.id.bookmarkRV);
        progressBar = view.findViewById(R.id.bookmarkPB);
        searchNewsFeedET = view.findViewById(R.id.searchNewsFeedET);
    }

    public void removeNews(int position)
    {
        titles.remove(position);
        description.remove(position);
        urlA.remove(position);
        newsfeedRecyclerAdapter.notifyDataSetChanged();
    }

    private void filter(String text){
        ArrayList<String> titlesTemp, descriptionTemp, urlATemp;

        titlesTemp = new ArrayList<>();
        descriptionTemp = new ArrayList<>();
        urlATemp = new ArrayList<>();

        for (int i=0; i<titles.size(); i++)
        {
            if (titles.get(i).toLowerCase().contains(text)){
                titlesTemp.add(titles.get(i));
                descriptionTemp.add(description.get(i));
                urlATemp.add(urlA.get(i));
            }
        }

        //update recyclerview
        newsfeedRecyclerAdapter.updateList(titlesTemp, descriptionTemp, urlATemp);
    }
}
