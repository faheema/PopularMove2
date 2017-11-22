package com.ap.fahm.popular_movie_app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Faheem on 18/08/17.
 */

    public final class Video implements Parcelable {

        public static final String SITE_YOUTUBE = "YouTube";
        public static final String TYPE_TRAILER = "Trailer";
        public static final Creator<Video> CREATOR = new Creator<Video>() {
            public Video createFromParcel(Parcel source) {
                return new Video(source);
            }

            public Video[] newArray(int size) {
                return new Video[size];
            }
        };

        @SerializedName("id")
        private String id;

        @SerializedName("iso_639_1")
        private String iso;

        @SerializedName("key")
        private String key;

        @SerializedName("name")
        private String name;

        @SerializedName("site")
        private String site;

        @SerializedName("size")
        private int size;

        @SerializedName("type")
        private String type;

        public Video() {
        }

        protected Video(Parcel in) {
            this.id = in.readString();
            this.iso = in.readString();
            this.key = in.readString();
            this.name = in.readString();
            this.site = in.readString();
            this.size = in.readInt();
            this.type = in.readString();
        }

        public String getId() {
            return id;
        }

        public Video setId(String id) {
            this.id = id;
            return this;
        }

        public String getIso() {
            return iso;
        }

        public Video setIso(String iso) {
            this.iso = iso;
            return this;
        }

        public String getKey() {
            return key;
        }

        public Video setKey(String key) {
            this.key = key;
            return this;
        }

        public String getName() {
            return name;
        }

        public Video setName(String name) {
            this.name = name;
            return this;
        }

        public String getSite() {
            return site;
        }

        public Video setSite(String site) {
            this.site = site;
            return this;
        }

        public int getSize() {
            return size;
        }

        public Video setSize(int size) {
            this.size = size;
            return this;
        }

        public String getType() {
            return type;
        }
        // --------------------------------------------------------------------------------------

        public Video setType(String type) {
            this.type = type;
            return this;
        }

        @Override
        public String toString() {
            return "Video{" +
                    "key='" + key + '\'' +
                    ", name='" + name + '\'' +
                    ", site='" + site + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.iso);
            dest.writeString(this.key);
            dest.writeString(this.name);
            dest.writeString(this.site);
            dest.writeInt(this.size);
            dest.writeString(this.type);
        }

}
