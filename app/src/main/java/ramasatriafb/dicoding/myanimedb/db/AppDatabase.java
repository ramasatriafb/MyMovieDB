package ramasatriafb.dicoding.myanimedb.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ramasatriafb.dicoding.myanimedb.entity.Favourite;

@Database(entities = {Favourite.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FavouriteDAO favouriteDAO();

}
