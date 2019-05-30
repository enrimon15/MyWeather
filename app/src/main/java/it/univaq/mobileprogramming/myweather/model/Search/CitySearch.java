package it.univaq.mobileprogramming.myweather.model.Search;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

public class CitySearch implements SearchSuggestion {

    private String name;
    private boolean mIsHistory = false;

    public  CitySearch (String name) {
        this.name=name;
    }

    public CitySearch(Parcel source) {
        this.name = source.readString();
        this.mIsHistory = source.readInt() != 0;
    }

    public static final Creator<CitySearch> CREATOR = new Creator<CitySearch>() {
        @Override
        public CitySearch createFromParcel(Parcel in) {
            return new CitySearch(in);
        }

        @Override
        public CitySearch[] newArray(int size) {
            return new CitySearch[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public void setIsHistory(boolean isHistory) {
        this.mIsHistory = isHistory;
    }

    public boolean getIsHistory() { return this.mIsHistory; }

    @Override
    public String getBody() { return name; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) { }
}
