package com.eventmanager.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventmanager.R;
import com.eventmanager.activities.GuestActivity;
import com.eventmanager.database.AppDatabase;
import com.eventmanager.database.entity.Event;
import com.eventmanager.database.entity.Speaker;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass. This fragment shows the speakers attending the events in
 * {@link GuestActivity}.
 */
public class GuestSpeakersFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private AppDatabase mDatabase;

    public GuestSpeakersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_speakers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Get the reference to the database object.
        mDatabase = AppDatabase.getInstance(getActivity());

        //Initialize the RecyclerView.
        mRecyclerView = view.findViewById(R.id.guest_speakers_recycler);

        //Use a linear layout manager for the recycler.
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Improves performance when the content layout size does not change for different items.
        mRecyclerView.setHasFixedSize(true);

        //Get the list of all events. The database must not be accessed from the UI thread because
        //it may potentially lock the UI for a long time.
        new DatabaseTask().execute();
    }

    private class DatabaseTask extends AsyncTask<Void, Void, RecyclerView.Adapter> {
        protected RecyclerView.Adapter doInBackground(Void... params) {
            List<Speaker> speakerList = mDatabase.eventDao().getAllSpeakers();

            //Remove the dummy speakers. Dummy speakers are inserted when an event is created.
            //They do not have their attributes set up yet, so do not include them in this list.creation of dummy speaker from speaker to events fragmen
            for(Speaker s : speakerList) {
                if(s.getName().equals("") || s.getDetails().equals("")) {
                    speakerList.remove(s);
                }
            }

            //The list of events of the speakers. For a speaker on index i in the speaker list,
            //the corresponding event is also on index i in speakerEvents list.
            //TODO: This is very hacky. Improve it.
            List<Event> speakerEventsList = new ArrayList<>(speakerList.size());
            for(Speaker s : speakerList) {
                speakerEventsList.add(mDatabase.eventDao().getSpeakerEvent(s.getEventID()).get(0));
            }

            return new GuestSpeakersAdapter(speakerList, speakerEventsList, mRecyclerView, getContext());
        }

        protected void onPostExecute(RecyclerView.Adapter adapter) {
            mAdapter = adapter;
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
