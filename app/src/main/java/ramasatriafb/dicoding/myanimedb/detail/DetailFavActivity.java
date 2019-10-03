package ramasatriafb.dicoding.myanimedb.detail;

import android.annotation.SuppressLint;
import androidx.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ramasatriafb.dicoding.myanimedb.R;
import ramasatriafb.dicoding.myanimedb.db.AppDatabase;
import ramasatriafb.dicoding.myanimedb.entity.Favourite;

public class DetailFavActivity extends AppCompatActivity {
    private ImageView posterMovie;
    private ImageView posterMovieSmall;
    TextView titleMovie;
    TextView releaseDateMovie;
    TextView ratingMovie;
    TextView synopsisMovie;
    Button btnDelete;

    Favourite favourite;

    private AppDatabase db;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_fav);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "favouritedb").build();

        posterMovie = findViewById(R.id.img_poster_movie);
        posterMovieSmall = findViewById(R.id.img_poster_movie_small);
        titleMovie = findViewById(R.id.tv_title_movie);
        releaseDateMovie = findViewById(R.id.tv_releaseDate_movie);
        ratingMovie = findViewById(R.id.tv_rating_movie);
        synopsisMovie = findViewById(R.id.tv_synopsis_movie);
        btnDelete = findViewById((R.id.btnHpsFavourite));

        favourite = getIntent().getParcelableExtra("fav_movie");

        Toast.makeText(this, getString(R.string.Loading)
                , Toast.LENGTH_SHORT).show();

        Picasso.with(DetailFavActivity.this).
                load("https://image.tmdb.org/t/p/w500" + favourite.getPoster())
                .into(posterMovie);
        posterMovie.setAdjustViewBounds(true);
        posterMovie.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Picasso.with(DetailFavActivity.this).
                load("https://image.tmdb.org/t/p/w185" + favourite.getPoster())
                .into(posterMovieSmall);

        final int idFavourite = favourite.getFavouriteId();
        titleMovie.setText(favourite.getJudul());
        String releaseDate = formatDate("yyyy-MM-dd", "dd MMMM yyyy", favourite.getReleaseDate());

        releaseDateMovie.setText(releaseDate);
        ratingMovie.setText(favourite.getRating());
        synopsisMovie.setText(favourite.getOverview());

        btnDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                onDeleteData(idFavourite);
                onBackPressed();
            }
        });
    }

    private void onDeleteData(final int idFavourite){
        new AsyncTask<String, String, Boolean>(){
            @Override
            protected Boolean doInBackground(String... params) {
                Boolean success = false;
                try{
                    db.favouriteDAO().deleteByFavouriteId(idFavourite);

                }catch (Exception e){
                    if(e.getMessage()!=null)
                        e.printStackTrace();
                }
                return success;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                Toast.makeText(DetailFavActivity.this, getString(R.string.DoneDeleteMovie), Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    public static String formatDate(String fromFormat, String toFormat, String dateToFormat) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat inFormat = new SimpleDateFormat(fromFormat);
        Date date = null;
        try {
            date = inFormat.parse(dateToFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat outFormat = new SimpleDateFormat(toFormat);

        return outFormat.format(date);
    }
}
