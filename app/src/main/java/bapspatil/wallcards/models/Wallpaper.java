package bapspatil.wallcards.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bapspatil
 */

public class Wallpaper implements Parcelable {

    @SerializedName("id") private String id;
    @SerializedName("color") private String color;
    @SerializedName("likes") private int likes;
    @SerializedName("user") private User user;
    @SerializedName("urls") private URLs urls;

    public Wallpaper() { }

    public Wallpaper(String id, String color, int likes, User user, URLs urls) {
        this.id = id;
        this.color = color;
        this.likes = likes;
        this.user = user;
        this.urls = urls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public URLs getUrls() {
        return urls;
    }

    public void setUrls(URLs urls) {
        this.urls = urls;
    }

    @Override
    public String toString() {
        return "Wallpaper{" +
                "id='" + id + '\'' +
                ", color='" + color + '\'' +
                ", likes=" + likes +
                ", user=" + user +
                ", urls=" + urls +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.color);
        dest.writeInt(this.likes);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.urls, flags);
    }

    protected Wallpaper(Parcel in) {
        this.id = in.readString();
        this.color = in.readString();
        this.likes = in.readInt();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.urls = in.readParcelable(URLs.class.getClassLoader());
    }

    public static final Creator<Wallpaper> CREATOR = new Creator<Wallpaper>() {
        @Override
        public Wallpaper createFromParcel(Parcel source) {
            return new Wallpaper(source);
        }

        @Override
        public Wallpaper[] newArray(int size) {
            return new Wallpaper[size];
        }
    };
}
