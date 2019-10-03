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
import ramasatriafb.dicoding.myanimedb.entity.TvFavourite;

public class DetailFavTvActivity extends AppCompatActivity {
    private ImageView posterTv;
    private ImageView posterTvSmall;
    TextView titleTv;
    TextView releaseDateTv;
    TextView ratingTv;
    TextView synopsisTv;
    Button btnDelete;

    TvFavourite tvFavourite;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_fav_tv);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "favouritedb").build();

        posterTv = findViewById(R.id.img_poster_tvshow);
        posterTvSmall = findViewById(R.id.img_poster_tv_small);
        titleTv = findViewById(R.id.tv_title_tv);
        releaseDateTv = findViewById(R.id.tv_First_Air);
        ratingTv = findViewById(R.id.tv_rating_tv);
        synopsisTv = findViewById(R.id.tv_synopsis_tv);
        btnDelete = findViewById((R.id.btnHpsFavourite));

        tvFavourite = getIntent().getParcelableExtra("favtv_show");

        Toast.makeText(this, getString(R.string.Loading)
                , Toast.LENGTH_SHORT).show();

        Picasso.with(DetailFavTvActivity.this).
                load("https://image.tmdb.org/t/p/w500" + tvFavourite.getPoster())
                .into(posterTv);
        posterTv.setAdjustViewBounds(true);
        posterTv.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Picasso.with(DetailFavTvActivity.this).
                load("https://image.tmdb.org/t/p/w185" + tvFavourite.getPoster())
                .into(posterTvSmall);

        final int idFavourite = tvFavourite.getFavouriteId();
        titleTv.setText(tvFavourite.getJudul());
        String releaseDate = formatDate("yyyy-MM-dd", "dd MMMM yyyy", tvFavourite.getReleaseDate());

        releaseDateTv.setText(releaseDate);
        ratingTv.setText(tvFavourite.getRating());
        synopsisTv.setText(tvFavourite.getOverview());

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
                Toast.makeText(DetailFavTvActivity.this, getString(R.string.DoneDeleteMovie), Toast.LENGTH_SHORT).show();
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
