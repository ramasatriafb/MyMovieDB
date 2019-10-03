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
import ramasatriafb.dicoding.myanimedb.adapter.TvAdapter;
import ramasatriafb.dicoding.myanimedb.api.ApiClient;
import ramasatriafb.dicoding.myanimedb.api.ApiInterface;

import ramasatriafb.dicoding.myanimedb.data.ResultTvShow;
import ramasatriafb.dicoding.myanimedb.data.TvShow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchTvFragment extends Fragment {
    public ProgressDialog mProgress;

    private String searchChange = null;


    RecyclerView recyclerView;
    TvAdapter tvAdapter;
    List<ResultTvShow> resultTvShows;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewTv = inflater.inflate(R.layout.fragment_search_tvshow, container, false);
        setHasOptionsMenu(true);
        ((SearchActivity) getActivity())
                .setActionBarTitle(getString(R.string.activity_search_tv));

        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage(getString(R.string.progress_loading));
        mProgress.show();

        resultTvShows = new ArrayList<>();
        tvAdapter = new TvAdapter(getActivity());

        recyclerView = viewTv.findViewById(R.id.rv_tv);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setRetainInstance(true);

        if (savedInstanceState != null) {
            ArrayList<ResultTvShow> list;
            list = savedInstanceState.getParcelableArrayList("tv_show");
            mProgress.dismiss();
            tvAdapter.setResultTvShows(list);
            recyclerView.setAdapter(tvAdapter);
        } else {
            getSearchResult();
        }

        return viewTv;
    }

    private void getSearchResult() {
        ApiInterface api = ApiClient.getRetrofit().create(ApiInterface.class);
        if (searchChange == null) {
            Call<TvShow> call = api.getTvShow();
            call.enqueue(new Callback<TvShow>() {
                @Override
                public void onResponse(Call<TvShow> call, Response<TvShow> response) {
                    if (response.isSuccessful()) {
                        mProgress.dismiss();
                        resultTvShows = response.body().getResults();
                        if(resultTvShows != null){
                            tvAdapter.setResultTvShows(resultTvShows);
                            recyclerView.setAdapter(tvAdapter);
                        }

                        if(resultTvShows.size() == 0){
                            Toast.makeText(getActivity(), R.string.not_found_tv, Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<TvShow> call, Throwable t) {
                    mProgress.dismiss();
                    Log.d("Error", t.getMessage());
                    Toast.makeText(getActivity(), getString(R.string.ErrorLoading)
                            , Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Call<TvShow> call = api.getSearchTv(searchChange);
            call.enqueue(new Callback<TvShow>() {
                @Override
                public void onResponse(Call<TvShow> call, Response<TvShow> response) {
                    if (response.isSuccessful()) {
                        mProgress.dismiss();
                        resultTvShows = response.body().getResults();
                        if (resultTvShows != null) {
                            tvAdapter.setResultTvShows(resultTvShows);
                            recyclerView.setAdapter(tvAdapter);
                        }
                        if (resultTvShows.size() == 0) {
                            Toast.makeText(getActivity(), R.string.not_found_tv, Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<TvShow> call, Throwable t) {
                    Toast.makeText(getActivity(), getString(R.string.ErrorLoading)
                            , Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("tv_show", new ArrayList<>(tvAdapter.getList()));
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ArrayList<ResultTvShow> list;
            list = savedInstanceState.getParcelableArrayList("tv_show");
            tvAdapter.setResultTvShows(list);
            recyclerView.setAdapter(tvAdapter);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);


        SearchView searchView  = new SearchView(getActivity());
        searchView.setQueryHint(getString(R.string.lookingfortv));
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

