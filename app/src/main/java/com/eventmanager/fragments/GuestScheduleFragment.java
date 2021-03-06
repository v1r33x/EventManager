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

import java.util.List;


/**
 * A simple {@link Fragment} subclass. This fragment shows the schedule of events in
 * {@link GuestActivity}.
 */
public class GuestScheduleFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private AppDatabase mDatabase;

    public GuestScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_schedule, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Get the reference to the database object.
        mDatabase = AppDatabase.getInstance(getActivity());

        //Initialize the RecyclerView.
        mRecyclerView = view.findViewById(R.id.guest_schedule_recycler);

        //Use a linear layout manager for the recycler.
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Improves performance when the content layout size does not change for different items.
        mRecyclerView.setHasFixedSize(true);

        //Get the list of all events. The database must not be accessed from the UI thread because
        //it may potentially lock the UI for a long time.
        new DatabaseTask().execute();

    }

    class DatabaseTask extends AsyncTask<Void, Void, List<Event>> {
        protected List<Event> doInBackground(Void... params) {
            return mDatabase.eventDao().getAllEvents();
        }

        protected void onPostExecute(List<Event> list) {
            mAdapter = new GuestScheduleAdapter(list);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
