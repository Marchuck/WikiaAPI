package pl.marchuck.wikiaapi;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.mypopsy.widget.FloatingSearchView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * @author Lukasz Marczak
 * @since 17.06.16.
 */
public class SuggestionEngine {
    public static final String TAG = SuggestionEngine.class.getSimpleName();
    private FloatingSearchView searchView;

    private SearchView searchView1;
    private List<String> staticData;
    @NonNull
    private ResultCallback<String> resultCallback;
    @Nullable
    StartActionListener startActionListener;

    public SuggestionEngine(FloatingSearchView searchView, List<String> data) {
        this.searchView = searchView;
        this.staticData = data;
    }

    SearchableAPI.WikiaAPI api = SearchableAPI.getRxApi();

    public SuggestionEngine afterSuggest(@NonNull ResultCallback<String> resultCallback) {
        this.resultCallback = resultCallback;
        return this;
    }

    public rx.Observable<List<String>> getStaticDataSearch(final String query) {
        return Observable.just(getData(query));
    }

    private List<String> getData(String query) {
        List<String> sugg = new ArrayList<>();
        for (String s : staticData) if (s.contains(query)) sugg.add(s);
        return sugg;
    }

    public void init() {
        emitInputs(searchView)
//        emitSearchView(searchView1)
                .debounce(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .flatMap(new Func1<String, Observable<SearchableAPI.SearchResponse>>() {
                    @Override
                    public Observable<SearchableAPI.SearchResponse> call(final String s) {
                        return api.getSuggestions(s);
                    }
                }).map(SearchableAPI.toArrayList())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new VerboseSubscriber<List<String>>(TAG) {
                    @Override
                    public void onNext(List<String> strings) {
                        resultCallback.onSuggested(strings);
                    }
                });
    }

    public SuggestionEngine onStartSuggest(@Nullable StartActionListener listener) {
        this.startActionListener = listener;
        return this;
    }

    public interface StartActionListener {
        void onStartSearch();
    }

    public interface ResultCallback<T> {
        void onSuggested(List<T> suggestions);
    }

    public static rx.Observable<String> emitSearchView(final SearchView searchView) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        subscriber.onNext(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        subscriber.onNext(newText);
                        return false;
                    }
                });
            }
        });
    }

    public rx.Observable<String> emitInputs(final FloatingSearchView searchView) {
        Log.d(TAG, "emitInputs: ");
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                searchView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (startActionListener != null) startActionListener.onStartSearch();
                        if (s.length() > 2)
                            subscriber.onNext(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }
        });
    }

    public static abstract class VerboseSubscriber<T> extends Subscriber<T> {
        //public static final String TAG = VerboseSubscriber.class.getSimpleName();
        public String TAG;

        public VerboseSubscriber(String TAG) {
            this.TAG = TAG;
        }

        @Override
        public void onCompleted() {
            Log.d(TAG, "onCompleted: ");
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: " + e.getMessage());
            e.printStackTrace();
        }
    }
}