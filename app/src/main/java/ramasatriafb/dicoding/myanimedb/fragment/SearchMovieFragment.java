package ramasatriafb.dicoding.myanimedb.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ramasatriafb.dicoding.myanimedb.R;
import ramasatriafb.dicoding.myanimedb.SearchActivity;
import ramasatriafb.dicoding.myanimedb.adapter.MovieAdapter;
import ramasatriafb.dicoding.myanimedb.api.ApiClient;
import ramasatriafb.dicoding.myanimedb.api.ApiInterface;
import ramasatriafb.dicoding.myanimedb.data.Movie;
import ramasatriafb.dicoding.myanimedb.data.ResultMovie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchMovieFragment extends Fragment {
    public ProgressDialog mProgress;

    private String searchChange = null;

    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    List<ResultMovie> resultMovieList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewMovie = inflater.inflate(R.layout.fragment_search_movie, container, false);
        setHasOptionsMenu(true);
        ((SearchActivity) getActivity())
                .setActionBarTitle(getString(R.string.activity_search_movie));

        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage(getString(R.string.progress_loading));
        mProgress.show();

        resultMovieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(getActivity());

        recyclerView = viewMovie.findViewById(R.id.rv_movie);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setRetainInstance(true);

        if (savedInstanceState != null) {
            ArrayList<ResultMovie> list;
            list = savedInstanceState.getParcelableArrayList("movie_anime");
            mProgress.dismiss();
            movieAdapter.setMovieResult(list);
            recyclerView.setAdapter(movieAdapter);

        } else {
            getSearchResult();
        }

        return viewMovie;
    }

    private void getSearchResult() {
        ApiInterface api = ApiClient.getRetrofit().create(ApiInterface.class);
        if (searchChange == null) {
            Call<Movie> call = api.getMovieAnime();
            call.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    if (response.isSuccessful()) {
                        mProgress.dismiss();
                        resultMovieList = response.body().getResults();
                        if(resultMovieList != null){
                            movieAdapter.setMovieResult(resultMovieList);
                            recyclerView.setAdapter(movieAdapter);
                        }

                        if(resultMovieList.size() == 0){
                            Toast.makeText(getActivity(), R.string.not_found_movie, Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
                    mProgress.dismiss();
                    Log.d("Error", t.getMessage());
                    Toast.makeText(getActivity(), getString(R.string.ErrorLoading)
                            , Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Call<Movie> call = api.getSearchMovies(searchChange);
            call.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    if (response.isSuccessful()) {
                        mProgress.dismiss();
                        resultMovieList = response.body().getResults();
                        if (resultMovieList != null) {
                            movieAdapter.setMovieResult(resultMovieList);
                            recyclerView.setAdapter(movieAdapter);
                        }
                        if (resultMovieList.size() == 0) {
                            Toast.makeText(getActivity(), R.string.not_found_movie, Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
                    Toast.makeText(getActivity(), getString(R.string.ErrorLoading)
                            , Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movie_anime", new ArrayList<>(movieAdapter.getList()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ArrayList<ResultMovie> list;
            list = savedInstanceState.getParcelableArrayList("movie_anime");
            movieAdapter.setMovieResult(list);
            recyclerView.setAdapter(movieAdapter);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);


        SearchView searchView  = new SearchView(getActivity());
        searchView.setQueryHint(getString(R.string.lookingformovie));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String nextText) {
                nextText = nextText.toLowerCase();
                    searchChange = nextText;
                    getSearchResult();
                return true;
            }
        });
        searchItem.setActionView(searchView);
    }

}

