package bapspatil.wallcards.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bapspatil
 */

public class User implements Parcelable {

    @SerializedName("name") private String name;
    @SerializedName("profile_image") private ProfileImage profileImage;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", profileImage=" + profileImage +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProfileImage getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }

    public User(String name, ProfileImage profileImage) {

        this.name = name;
        this.profileImage = profileImage;
    }

    public User() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeParcelable(this.profileImage, flags);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.profileImage = in.readParcelable(ProfileImage.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
