package bapspatil.wallcards.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bapspatil
 */

public class URLs implements Parcelable {

    @SerializedName("raw") private String raw;
    @SerializedName("full") private String full;
    @SerializedName("regular") private String regular;
    @SerializedName("small") private String small;
    @SerializedName("thumb") private String thumb;

    @Override
    public String toString() {
        return "URLs{" +
                "raw='" + raw + '\'' +
                ", full='" + full + '\'' +
                ", regular='" + regular + '\'' +
                ", small='" + small + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public URLs(String raw, String full, String regular, String small, String thumb) {
        this.raw = raw;
        this.full = full;
        this.regular = regular;
        this.small = small;
        this.thumb = thumb;
    }

    public URLs(String regular) {
        this.raw = "";
        this.full = "";
        this.regular = regular;
        this.small = "";
        this.thumb = "";
    }

    public URLs() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.raw);
        dest.writeString(this.full);
        dest.writeString(this.regular);
        dest.writeString(this.small);
        dest.writeString(this.thumb);
    }

    protected URLs(Parcel in) {
        this.raw = in.readString();
        this.full = in.readString();
        this.regular = in.readString();
        this.small = in.readString();
        this.thumb = in.readString();
    }

    public static final Creator<URLs> CREATOR = new Creator<URLs>() {
        @Override
        public URLs createFromParcel(Parcel source) {
            return new URLs(source);
        }

        @Override
        public URLs[] newArray(int size) {
            return new URLs[size];
        }
    };
}
