package ramasatriafb.dicoding.myanimedb.entity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;


@Entity(tableName = "favourite")

public class TvFavourite implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public int favouriteId;

    @ColumnInfo(name = "kontenid")
    public String kontenId;

    @ColumnInfo(name = "judul")
    public String judul;

    @ColumnInfo(name = "poster")
    public String poster;

    @ColumnInfo(name = "release_date")
    public String releaseDate;

    @ColumnInfo(name = "rating")
    public String rating;

    @ColumnInfo(name = "overview")
    public String overview;

    @ColumnInfo(name = "flag")
    public int flag;

    @ColumnInfo(name = "flag_save")
    public int flagSave;

    public int getFavouriteId() {
        return favouriteId;
    }

    public void setFavouriteId(int favouriteId) {
        this.favouriteId = favouriteId;
    }

    public String getKontenId() {
        return kontenId;
    }

    public void setKontenId(String kontenId) {
        this.kontenId = kontenId;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getFlagSave() {
        return flagSave;
    }

    public void setFlagSave(int flagSave) {
        this.flagSave = flagSave;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.favouriteId);
        dest.writeString(this.kontenId);
        dest.writeString(this.judul);
        dest.writeString(this.poster);
        dest.writeString(this.releaseDate);
        dest.writeString(this.rating);
        dest.writeString(this.overview);
        dest.writeInt(this.flag);
        dest.writeInt(this.flagSave);
    }

    public TvFavourite() {
    }

    protected TvFavourite(Parcel in) {
        this.favouriteId = in.readInt();
        this.kontenId = in.readString();
        this.judul = in.readString();
        this.poster = in.readString();
        this.releaseDate = in.readString();
        this.rating = in.readString();
        this.overview = in.readString();
        this.flag = in.readInt();
        this.flagSave = in.readInt();
    }

    public static final Creator<TvFavourite> CREATOR = new Creator<TvFavourite>() {
        @Override
        public TvFavourite createFromParcel(Parcel source) {
            return new TvFavourite(source);
        }

        @Override
        public TvFavourite[] newArray(int size) {
            return new TvFavourite[size];
        }
    };
}
