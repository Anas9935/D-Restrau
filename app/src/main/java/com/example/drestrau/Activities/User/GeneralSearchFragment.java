package com.example.drestrau.Activities.User;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.drestrau.R;


public class GeneralSearchFragment extends Fragment implements SearchView.OnQueryTextListener{
SearchView sv;
    public GeneralSearchFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_general_search, container, false);
        initializeViews(view);

        return view;
    }

    private void initializeViews(View view) {
        sv=view.findViewById(R.id.search_view);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
