package ramasatriafb.dicoding.myanimedb.detail;

import android.annotation.SuppressLint;
import androidx.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import ramasatriafb.dicoding.myanimedb.data.ResultMovie;
import ramasatriafb.dicoding.myanimedb.db.AppDatabase;
import ramasatriafb.dicoding.myanimedb.entity.Favourite;

public class DetailActivity extends AppCompatActivity {
    private ImageView posterMovie;
    private ImageView posterMovieSmall;
    TextView titleMovie;
    TextView releaseDateMovie;
    TextView ratingMovie;
    TextView synopsisMovie;
    Button btnLike;
    TextView textSukai;

    ResultMovie result;

    private AppDatabase db;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "favouritedb").allowMainThreadQueries().build();


        result = getIntent().getParcelableExtra("movie");
        String kontenIdCheck = result.getId().toString();

        Log.d("konten", kontenIdCheck);
        String cek = db.favouriteDAO().checkedKontenId(kontenIdCheck);

        Log.d("TES", cek);
        setContentView(R.layout.activity_detail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        posterMovie = findViewById(R.id.img_poster_movie);
        posterMovieSmall = findViewById(R.id.img_poster_movie_small);
        titleMovie = findViewById(R.id.tv_title_movie);
        releaseDateMovie = findViewById(R.id.tv_releaseDate_movie);
        ratingMovie = findViewById(R.id.tv_rating_movie);
        synopsisMovie = findViewById(R.id.tv_synopsis_movie);
        btnLike = findViewById(R.id.btnFavourite);
        textSukai = findViewById(R.id.textSukai);

        Toast.makeText(this, getString(R.string.Loading)
                , Toast.LENGTH_SHORT).show();
        int tas = Integer.parseInt(cek);
        if(tas!=0){
            textSukai.setText(getString(R.string.DisabledFav));
            textSukai.setVisibility(View.VISIBLE);
            btnLike.setVisibility(View.GONE);
            Log.d("TAS", "bener");
        }else{
            btnLike.setVisibility(View.VISIBLE);
            Log.d("TAS", "salah");
        }
//        if(cek.equals('1')){
//            btnLike.setVisibility(View.GONE);
//        }

        Picasso.with(DetailActivity.this).
                load("https://image.tmdb.org/t/p/w500" + result.getPosterPath())
                .into(posterMovie);
        posterMovie.setAdjustViewBounds(true);
        posterMovie.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Picasso.with(DetailActivity.this).
                load("https://image.tmdb.org/t/p/w185" + result.getPosterPath())
                .into(posterMovieSmall);

        final String KontenId = String.valueOf(result.getId());
        final String Synopsis;

        titleMovie.setText(result.getTitle());
        String releaseDate = formatDate("yyyy-MM-dd", "dd MMMM yyyy", result.getReleaseDate());
        releaseDateMovie.setText(releaseDate);
        ratingMovie.setText(Double.toString(result.getVoteAverage()));
        if (result.getOverview().equalsIgnoreCase("")) {
            Synopsis = getString(R.string.Overview);
            synopsisMovie.setText(getString(R.string.Overview));

        } else {
            Synopsis = result.getOverview();
            synopsisMovie.setText(result.getOverview());
        }

        btnLike.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Favourite favourite = new Favourite();
                favourite.setKontenId(KontenId);
                favourite.setJudul(result.getTitle());
                favourite.setPoster(result.getPosterPath());
                favourite.setReleaseDate(result.getReleaseDate());
                favourite.setRating(result.getVoteAverage().toString());
                favourite.setOverview(Synopsis);
                favourite.setFlag(0);
                favourite.setFlagSave(1);
                insertData(favourite);
                textSukai.setText(getString(R.string.DisabledFav));
                textSukai.setVisibility(View.VISIBLE);
                btnLike.setVisibility(View.GONE);

            }
        });

    }

    private void insertData(final Favourite favourite){

        new AsyncTask<Void, Void, Long>(){
            @Override
            protected Long doInBackground(Void... voids) {
                // melakukan proses insert data
                long status = db.favouriteDAO().insertFavourite(favourite);
                return status;
            }

            @Override
            protected void onPostExecute(Long status) {
                Toast.makeText(DetailActivity.this, getString(R.string.DoneMovie), Toast.LENGTH_SHORT).show();
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
