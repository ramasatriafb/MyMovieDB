package ramasatriafb.dicoding.myanimedb.api;

import ramasatriafb.dicoding.myanimedb.data.Movie;
import ramasatriafb.dicoding.myanimedb.data.TvShow;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    String DB_API = "9413542f6df14542c3eb2963343a39e5";
    String LINK_MOVIE = "&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_genres=16&with_original_language=ja";
    String LINK_TV_SHOW = "&sort_by=popularity.desc&page=1&timezone=Japan%2FTokyo&with_genres=16&include_null_first_air_dates=false&with_original_language=ja";
    String LANGUAGE = "&language=en-US";
    @GET("discover/movie?api_key=" + DB_API)
//    @GET("discover/movie?api_key=" + DB_API + LINK_MOVIE)
    Call<Movie> getMovieAnime();

    @GET("discover/tv?api_key=" + DB_API)
//    @GET("discover/tv?api_key=" + DB_API + LINK_TV_SHOW)
    Call<TvShow> getTvShow();

//    @GET("search/movie?api_key=" + DB_API + LINK_MOVIE)
    @GET("search/movie?api_key=" + DB_API + LANGUAGE)
    Call<Movie> getSearchMovies(@Query("query") String querySearch);

//    @GET("search/tv?api_key=" + DB_API + LINK_TV_SHOW)
    @GET("search/tv?api_key=" + DB_API + LANGUAGE)
    Call<TvShow> getSearchTv(@Query("query") String querySearch);
//    https://api.themoviedb.org/3/search/movie?api_key={API KEY}&language=en-US&

//    Movies: https://api.themoviedb.org/3/search/movie?api_key={API KEY}&language=en-US&query={MOVIE NAME}
    // https://api.themoviedb.org/3/discover/movie?api_key={API KEY}&primary_release_date.gte={TODAY DATE}&primary_release_date.lte={TODAY DATE}

    @GET("discover/movie?api_key=" + DB_API)
    Call<Movie> getMovieRelease(@Query("primary_release_date.gte") String date ,@Query("primary_release_date.lte") String date2);
}
