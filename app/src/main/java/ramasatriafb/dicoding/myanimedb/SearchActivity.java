package ramasatriafb.dicoding.myanimedb;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ramasatriafb.dicoding.myanimedb.fragment.SearchMovieFragment;
import ramasatriafb.dicoding.myanimedb.fragment.SearchTvFragment;

public class SearchActivity extends AppCompatActivity {
    private TextView mTextMessage;
    Fragment fragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search_movie:
                    fragment = new SearchMovieFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;

                case R.id.navigation_search_tv:
                    fragment = new SearchTvFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if(savedInstanceState==null){
            fragment = new SearchMovieFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                .commit();
        }
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

}
