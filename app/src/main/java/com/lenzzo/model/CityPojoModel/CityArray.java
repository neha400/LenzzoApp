package com.lenzzo.model.CityPojoModel;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityArray implements Serializable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("citylist")
    @Expose
    private List<CityList> citylist = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CityList> getCitylist() {
        return citylist;
    }

    public void setCitylist(List<CityList> citylist) {
        this.citylist = citylist;
    }

}
