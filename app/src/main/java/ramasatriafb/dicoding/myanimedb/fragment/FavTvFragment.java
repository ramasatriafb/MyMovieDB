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
import ramasatriafb.dicoding.myanimedb.adapter.FavTvAdapter;
import ramasatriafb.dicoding.myanimedb.db.AppDatabase;
import ramasatriafb.dicoding.myanimedb.entity.TvFavourite;


public class FavTvFragment extends Fragment {
    RecyclerView recyclerView;
    FavTvAdapter favTvAdapter;
    List<TvFavourite> resultFavTvList;
    ProgressBar progressBar;
    private AppDatabase db;
    TextView warningData;


    public FavTvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fav_tv, container, false);
        db = Room.databaseBuilder(getActivity(),
                AppDatabase.class, "favouritedb").allowMainThreadQueries().build();

        progressBar = view.findViewById(R.id.progress_bar);
        warningData = view.findViewById(R.id.warningData);
        resultFavTvList = new ArrayList<>();
        favTvAdapter = new FavTvAdapter(getActivity());

        recyclerView = view.findViewById(R.id.rv_tv);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setRetainInstance(true);
        if (savedInstanceState != null) {
            ArrayList<TvFavourite> list;
            list = savedInstanceState.getParcelableArrayList("fav_tv");
            favTvAdapter.setTvResult(list);
            recyclerView.setAdapter(favTvAdapter);
        } else {
            tvLoad();
        }
        return view;
    }

    private void tvLoad() {
        showProgressBar();
        resultFavTvList.addAll(Arrays.asList((db.favouriteDAO().selectAllTvs())));
        favTvAdapter.setTvResult(resultFavTvList);
        recyclerView.setAdapter(favTvAdapter);
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
        outState.putParcelableArrayList("fav_tv", new ArrayList<>(favTvAdapter.getList()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ArrayList<TvFavourite> list;
            list = savedInstanceState.getParcelableArrayList("fav_tv");
            favTvAdapter.setTvResult(list);
            recyclerView.setAdapter(favTvAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        resultFavTvList.clear();
        tvLoad();
        if (resultFavTvList.size() == 0) {
            warningData.setVisibility(View.VISIBLE);
            warningData.setText(getString(R.string.EmptyDataFavTv));
        }
    }
}
