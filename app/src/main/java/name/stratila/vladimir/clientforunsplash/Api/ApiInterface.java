package name.stratila.vladimir.clientforunsplash.Api;

import name.stratila.vladimir.clientforunsplash.Models.Photo;
import name.stratila.vladimir.clientforunsplash.Models.SearchResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import static name.stratila.vladimir.clientforunsplash.Api.ApiHelper.API_KEY;

public interface ApiInterface {
    @Headers("Authorization: Client-ID "+API_KEY)
    @GET("/photos")
    Call<List<Photo>> getPhotos(
            @Query("page") int page,
            @Query("per_page") int perPage
    );

    @Headers("Authorization: Client-ID "+API_KEY)
    @GET("/search/photos")
    Call<SearchResult> searchPhotos(
            @Query("query") String query,
            @Query("page") int page,
            @Query("per_page") int perPage

    );

}
