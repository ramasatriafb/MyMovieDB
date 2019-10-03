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
import ramasatriafb.dicoding.myanimedb.data.ResultTvShow;
import ramasatriafb.dicoding.myanimedb.db.AppDatabase;
import ramasatriafb.dicoding.myanimedb.entity.Favourite;

public class DetailTvActivity extends AppCompatActivity {
    private ImageView posterTV;
    private ImageView posterTVSmall;
    TextView titleTV;
    TextView firstAirTV;
    TextView ratingTV;
    TextView synopsisTV;
    Button btnLike;
    TextView textSukai;



    ResultTvShow result;
    private AppDatabase db;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvdetail);
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "favouritedb").allowMainThreadQueries().build();


        result = getIntent().getParcelableExtra("tv_show");
        String kontenIdCheck = result.getId().toString();

        Log.d("konten", kontenIdCheck);
        String cek = db.favouriteDAO().checkedKontenId(kontenIdCheck);

        Log.d("TES", cek);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        posterTV = findViewById(R.id.img_poster_tv);
        posterTVSmall = findViewById(R.id.img_poster_tv_small);
        titleTV = findViewById(R.id.tv_title_tv);
        firstAirTV = findViewById(R.id.tv_First_Air);
        ratingTV = findViewById(R.id.tv_rating_tv);
        synopsisTV = findViewById(R.id.tv_synopsis_tv);
        btnLike = findViewById(R.id.btnFavTv);
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

        Picasso.with(DetailTvActivity.this).
                load("https://image.tmdb.org/t/p/w500" + result.getPosterPath())
                .into(posterTV);
        posterTV.setAdjustViewBounds(true);
        posterTV.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Picasso.with(DetailTvActivity.this).
                load("https://image.tmdb.org/t/p/w185" + result.getPosterPath())
                .into(posterTVSmall);

        final String KontenId = String.valueOf(result.getId());
        final String Synopsis;
        titleTV.setText(result.getName());
        final String firstAirDate = formatDate("yyyy-MM-dd", "dd MMMM yyyy", result.getFirstAirDate());
        firstAirTV.setText(firstAirDate);
        ratingTV.setText(Double.toString(result.getVoteAverage()));
        if (result.getOverview().equalsIgnoreCase("")) {
            Synopsis = getString(R.string.Overview);
            synopsisTV.setText(getString(R.string.Overview));

        } else {
            Synopsis = result.getOverview();
            synopsisTV.setText(result.getOverview());
        }

        btnLike.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Favourite favourite = new Favourite();
                favourite.setKontenId(KontenId);
                favourite.setJudul(result.getName());
                favourite.setPoster(result.getPosterPath());
                favourite.setReleaseDate(result.getFirstAirDate());
                favourite.setRating(result.getVoteAverage().toString());
                favourite.setOverview(Synopsis);
                favourite.setFlag(1);
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
                Toast.makeText(DetailTvActivity.this, getString(R.string.DoneTv), Toast.LENGTH_SHORT).show();
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
