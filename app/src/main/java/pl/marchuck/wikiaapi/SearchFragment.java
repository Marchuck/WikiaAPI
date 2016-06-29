package pl.marchuck.wikiaapi;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    public static final String TAG = SearchFragment.class.getSimpleName();
    com.mypopsy.widget.FloatingSearchView searchView;
    SuggestionAdapter adapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        Log.d(TAG, "onCreateView: ");
        searchView =
                (com.mypopsy.widget.FloatingSearchView) v.findViewById(R.id.search);
//        SearchView searchView = (SearchView) v.findViewById(R.id.search);
        searchView.setActivated(true);
        adapter = new SuggestionAdapter();
//        searchView.setSuggestionsAdapter(C());
        searchView.setAdapter(adapter);


        SuggestionEngine itemSuggestionEngine = new SuggestionEngine(searchView, new ArrayList<String>());
        itemSuggestionEngine
                .onStartSuggest(new SuggestionEngine.StartActionListener() {
                    @Override
                    public void onStartSearch() {
                        showProgressBar(true);
                    }
                }).afterSuggest(new SuggestionEngine.ResultCallback<String>() {
            @Override
            public void onSuggested(List<String> suggestions) {
                Log.d(TAG, "onSuggested: " + listToString(suggestions));
                adapter.updateDataset(suggestions);
                searchView.setAdapter(adapter);
                showProgressBar(false);
            }
        }).init();


        /*FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchableAPI.getRxApi().getSuggestions("Kis")
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SuggestionEngine.VerboseSubscriber<SearchableAPI.SearchResponse>(TAG) {
                            @Override
                            public void onNext(SearchableAPI.SearchResponse searchResponse) {
                                Log.d(TAG, "onNext: " + listToString(searchResponse.getItems()));
                                Toast.makeText(getActivity(), searchResponse.getItems().get(0).getTitle(),
                                        Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });*/

        return v;
    }

    private void showProgressBar(boolean b) {
        searchView.getMenu().findItem(R.id.menu_progress).setVisible(b);
    }


    public static <T> String listToString(List<T> list) {
        if (list == null) {
            return "null";
        }
        if (list.size() == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(list.size() * 7);
        sb.append('[');
        sb.append(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            sb.append(", ");
            sb.append(list.get(i));
        }
        sb.append(']');
        return sb.toString();
    }

    public boolean hideSuggestions() {
        if (adapter.dataSet.isEmpty()) return false;
        adapter.updateDataset(new ArrayList<String>());
        searchView.setAdapter(adapter);
        return true;

    }
}
