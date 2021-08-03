package com.bbcnewsreader.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bbcnewsreader.app.App;
import com.bbcnewsreader.R;
import com.bbcnewsreader.adapters.NewsfeedRecyclerAdapter;
import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView parseRV;
    private ProgressBar homePB;
    private ArrayList<String> titles, description, urlA;
    private NewsfeedRecyclerAdapter newsfeedRecyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reference(view);
        Activity context = getActivity();
        if (App.mContext.isNetworkConnected()) new ParseData().execute();
        else {
            final Snackbar snackbar = Snackbar.make(context.findViewById(android.R.id.content), "No Internet Connection!", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("CLOSE", view1 -> snackbar.dismiss());
            snackbar.show();
        }
    }

    private void reference(View view) {
        homePB = view.findViewById(R.id.homePB);
        parseRV = view.findViewById(R.id.parseRV);
    }

    public class ParseData extends AsyncTask<Integer, Void, Exception> {

        Exception exception = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Exception doInBackground(Integer... params) {

            titles = new ArrayList<>();
            description = new ArrayList<>();
            urlA = new ArrayList<>();

            try {
                URL url = new URL("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");

                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setDoInput(true);
                http.connect();
                InputStream is = http.getInputStream();
                //creates new instance of PullParserFactory that can be used to create XML pull parsers
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                //Specifies whether the parser produced by this factory will provide support
                //for XML namespaces
                factory.setNamespaceAware(false);

                //creates a new instance of a XML pull parser using the currently configured
                //factory features
                XmlPullParser xpp = factory.newPullParser();

                // We will get the XML from an input stream
                xpp.setInput(is, "UTF_8");

                /* We will parse the XML content looking for the "<title>" tag which appears inside the "<item>" tag.
                 * We should take into consideration that the rss feed name is also enclosed in a "<title>" tag.
                 * Every tag begins with these lines: "<channel><title>Feed_Name</title> etc."
                 * We should skip the "<title>" tag which is a child of "<channel>" tag,
                 * and take into consideration only the "<title>" tag which is a child of the "<item>" tag
                 *
                 * In order to achieve this, we will make use of a boolean variable called "insideItem".
                 */
                boolean insideItem = false;

                // Returns the type of current event: START_TAG, END_TAG, START_DOCUMENT, END_DOCUMENT etc..
                int eventType = xpp.getEventType(); //loop control variable

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    //if we are at a START_TAG (opening tag)
                    if (eventType == XmlPullParser.START_TAG) {
                        //if the tag is called "item"
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        }
                        else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem) {
                                // extract the text between <title> and </title>
                                titles.add(xpp.nextText());
                            }
                        }
                        else if (xpp.getName().equalsIgnoreCase("link")) {
                            if (insideItem) {
                                // extract the text between <link> and </link>
                                urlA.add(xpp.nextText());
                            }
                        }
                        else if (xpp.getName().equalsIgnoreCase("description")) {
                            if (insideItem) {
                                // extract the text between <link> and </link>
                                description.add(xpp.nextText());
                            }
                        }

                    }
                    else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                        insideItem =  false;
                    }

                    eventType = xpp.next(); //move to next element by increment the eventType

                }

            } catch (XmlPullParserException | IOException e) {
                exception = e;
            }

            return exception;
        }

        @Override
        protected void onPostExecute(Exception s) {
            super.onPostExecute(s);
            newsfeedRecyclerAdapter = new NewsfeedRecyclerAdapter(titles, description, urlA, false);
            parseRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            parseRV.setAdapter(newsfeedRecyclerAdapter);
            homePB.setVisibility(View.GONE);
            parseRV.setVisibility(View.VISIBLE);
        }
    }


}
