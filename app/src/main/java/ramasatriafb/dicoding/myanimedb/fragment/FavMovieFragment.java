package ramasatriafb.dicoding.myanimedb.fragment;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ramasatriafb.dicoding.myanimedb.R;
import ramasatriafb.dicoding.myanimedb.adapter.FavMovieAdapter;
import ramasatriafb.dicoding.myanimedb.db.AppDatabase;
import ramasatriafb.dicoding.myanimedb.entity.Favourite;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavMovieFragment extends Fragment {

    RecyclerView recyclerView;
    FavMovieAdapter favMovieAdapter;
    List<Favourite> resultFavMovieList;
    ProgressBar progressBar;
    private AppDatabase db;
    TextView warningData;


    public FavMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fav_movie, container, false);
        db = Room.databaseBuilder(getActivity(),
                AppDatabase.class, "favouritedb").allowMainThreadQueries().build();

        progressBar = view.findViewById(R.id.progress_bar);
        warningData = view.findViewById(R.id.warningData);
        resultFavMovieList = new ArrayList<>();
        favMovieAdapter = new FavMovieAdapter(getActivity());

        recyclerView = view.findViewById(R.id.rv_movie);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setRetainInstance(true);
        if (savedInstanceState != null) {
            ArrayList<Favourite> list;
            list = savedInstanceState.getParcelableArrayList("fav_movie");
            favMovieAdapter.setMovieResult(list);
            recyclerView.setAdapter(favMovieAdapter);
        } else {
            movieLoad();
        }
        return view;
    }

    private void movieLoad() {
        showProgressBar();
        resultFavMovieList.addAll(Arrays.asList((db.favouriteDAO().selectAllMovies())));
        favMovieAdapter.setMovieResult(resultFavMovieList);
        recyclerView.setAdapter(favMovieAdapter);
        hideProgressBar();
    }


    void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), getString(R.string.Loading)
                , Toast.LENGTH_SHORT).show();

    }

    void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("fav_movie", new ArrayList<>(favMovieAdapter.getList()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ArrayList<Favourite> list;
            list = savedInstanceState.getParcelableArrayList("fav_movie");
            favMovieAdapter.setMovieResult(list);
            recyclerView.setAdapter(favMovieAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        resultFavMovieList.clear();
        movieLoad();
        if (resultFavMovieList.size() == 0) {
            warningData.setVisibility(View.VISIBLE);
            warningData.setText(getString(R.string.EmptyDataFavMovie));
        }
    }
}
