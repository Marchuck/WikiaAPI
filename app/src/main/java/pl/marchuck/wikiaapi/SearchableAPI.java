package pl.marchuck.wikiaapi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.functions.Func1;

/**
 * @author Lukasz Marczak
 * @since 29.06.16.
 */
public class SearchableAPI {

    public static class SearchResponse {
        private List<Suggestion> items = new ArrayList<>();

        public SearchResponse() {
        }

        public List<Suggestion> getItems() {
            return items;
        }

        public void setItems(List<Suggestion> items) {
            this.items = items;
        }
    }

    public static class Suggestion {
        private String title;

        @Override
        public String toString() {
            return title;
        }

        public Suggestion() {
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public interface WikiaAPI {

        String naruto_endpoint = "http://naruto.wikia.com/";

        @GET("api/v1/SearchSuggestions/List")
        rx.Observable<SearchResponse> getSuggestions(@Query("query") String query);
    }

    public static Func1<SearchResponse, List<String>> toArrayList() {
        return new Func1<SearchResponse, List<String>>() {
            @Override
            public List<String> call(SearchResponse searchResponse) {
                List<String> suggestions = new ArrayList<>();
                for (Suggestion s : searchResponse.getItems())
                    suggestions.add(s.getTitle());
                return suggestions;
            }
        };
    }


    private static Retrofit buildRxRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WikiaAPI.naruto_endpoint)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static WikiaAPI getRxApi() {
        return buildRxRetrofit().create(WikiaAPI.class);
    }

}
