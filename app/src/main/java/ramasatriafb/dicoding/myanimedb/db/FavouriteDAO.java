package ramasatriafb.dicoding.myanimedb.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import ramasatriafb.dicoding.myanimedb.entity.Favourite;
import ramasatriafb.dicoding.myanimedb.entity.TvFavourite;

@Dao
public interface FavouriteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertFavourite(Favourite favourite);

    @Query("DELETE FROM favourite WHERE favouriteId = :favouriteId")
    abstract void deleteByFavouriteId(int favouriteId);

    @Query("SELECT count (*) FROM favourite WHERE kontenId = :kontenId")
    abstract String checkedKontenId(String kontenId);


    @Query("SELECT * FROM favourite WHERE flag = 0 and flag_save = 1 order by favouriteId desc")
    Favourite[] selectAllMovies();

    @Query("SELECT * FROM favourite WHERE flag = 1 and flag_save = 1 order by favouriteId desc")
    TvFavourite[] selectAllTvs();

}
