package ramasatriafb.dicoding.myanimedb.adapter;

import android.content.Context;
import android.content.Intent;
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
import ramasatriafb.dicoding.myanimedb.detail.DetailFavActivity;
import ramasatriafb.dicoding.myanimedb.entity.Favourite;

public class FavMovieAdapter extends RecyclerView.Adapter<FavMovieAdapter.FavMovieHolder> {
    private List<Favourite> favouritesMovieList = new ArrayList<>();

    private Context context;

    public FavMovieAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public FavMovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid_movie, parent, false);

        return new FavMovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavMovieHolder favMovieHolder, final int position) {
        try{
            favMovieHolder.bindView(favouritesMovieList.get(position));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setMovieResult(List<Favourite> favouritesMovie) {
        this.favouritesMovieList = favouritesMovie;
    }

    public List<Favourite> getList() {
        return favouritesMovieList;
    }


    @Override
    public int getItemCount() {
        return favouritesMovieList.size();
    }

    class FavMovieHolder extends RecyclerView.ViewHolder {
        ImageView Poster;

        public FavMovieHolder(View itemView) {
            super(itemView);
            Poster = itemView.findViewById(R.id.img_poster_movie);
        }

        public void bindView(final Favourite favourite) {
            Picasso.with(itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w185" + favourite.getPoster())
                    .into(Poster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), DetailFavActivity.class);
                    i.putExtra("fav_movie", favourite);
                    itemView.getContext().startActivity(i);
                }
            });
        }

    }


}
