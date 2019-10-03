package ramasatriafb.dicoding.myanimedb.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ramasatriafb.dicoding.myanimedb.R;
import ramasatriafb.dicoding.myanimedb.detail.DetailFavTvActivity;
import ramasatriafb.dicoding.myanimedb.entity.TvFavourite;

public class FavTvAdapter extends RecyclerView.Adapter<FavTvAdapter.FavTvHolder> {
    private List<TvFavourite> favouritesTvList = new ArrayList<>();

    private Context context;

    public FavTvAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public FavTvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid_tvshow, parent, false);

        return new FavTvHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavTvHolder favMovieHolder, final int position) {
        try{
            favMovieHolder.bindView(favouritesTvList.get(position));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setTvResult(List<TvFavourite> favouritesMovie) {
        this.favouritesTvList = favouritesMovie;
    }

    public List<TvFavourite> getList() {
        return favouritesTvList;
    }


    @Override
    public int getItemCount() {
        return favouritesTvList.size();
    }

    class FavTvHolder extends RecyclerView.ViewHolder {
        ImageView Poster;

        public FavTvHolder(View itemView) {
            super(itemView);
            Poster = itemView.findViewById(R.id.img_poster_tvshow);
        }

        public void bindView(final TvFavourite tvFavourite) {
            Picasso.with(itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w185" + tvFavourite.getPoster())
                    .into(Poster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), DetailFavTvActivity.class);
                    i.putExtra("favtv_show", tvFavourite);
                    itemView.getContext().startActivity(i);
                }
            });
        }

    }


}
