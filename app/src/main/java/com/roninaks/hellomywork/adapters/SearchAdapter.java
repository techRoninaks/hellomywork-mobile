package com.roninaks.hellomywork.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.models.SearchSuggestionsModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class SearchAdapter extends ArrayAdapter{
    //Private Global Members
    private Fragment fragment;
    private Context context;
    private ArrayList<SearchSuggestionsModel> searchSuggestionsModels,dataListAllItems;
    private ListFilter listFilter = new ListFilter();


    public SearchAdapter(@NonNull Context context, int resource, ArrayList<SearchSuggestionsModel> searchSuggestionsModels, Fragment fragment) {
        super(context, resource, searchSuggestionsModels);
        this.fragment = fragment;
        this.searchSuggestionsModels = searchSuggestionsModels;
    }

    @Override
    public int getCount() {
        if(searchSuggestionsModels != null)
            return searchSuggestionsModels.size();
        else
            return 0;
    }

    @Override
    public String getItem(int position) {
        return searchSuggestionsModels.get(position).getCategoryName();
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_suggestions_listitem, parent, false);
        }

        TextView strName = (TextView) view.findViewById(R.id.tvText);
        strName.setText(getItem(position));
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (dataListAllItems == null) {
                synchronized (lock) {
                    dataListAllItems = new ArrayList<>(searchSuggestionsModels);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<SearchSuggestionsModel> matchValues = new ArrayList<>();

                for (int i = 0; i < dataListAllItems.size(); i++) {
                    if (dataListAllItems.get(i).getCategoryName().toLowerCase().contains(searchStrLowerCase)) {
                        matchValues.add(dataListAllItems.get(i));
                    }
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                searchSuggestionsModels = (ArrayList<SearchSuggestionsModel>)results.values;
            } else {
                searchSuggestionsModels = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
