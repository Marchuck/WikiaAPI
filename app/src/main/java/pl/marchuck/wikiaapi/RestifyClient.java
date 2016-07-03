package pl.marchuck.wikiaapi;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author Lukasz Marczak
 * @since 03.07.16.
 */
public class RestifyClient {


    public interface API {
        String endpoint = "http://192.168.0.36:8080/";

        @POST("user/{name}&{pass}")
        rx.Observable<ResponseBody> post(@Path("name") String name, @Path("pass") String pass);
    }

    public static rx.Observable<ResponseBody> postUser(String name, String pass) {

        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(API.endpoint)
                .build().create(API.class).post(name, pass);

    }
}
