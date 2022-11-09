package com.lenzzo.model.CityPojoModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Response implements Serializable {

    @SerializedName("city_Array")
    @Expose
    private CityArray cityArray;

    public CityArray getCityArray() {
        return cityArray;
    }

    public void setCityArray(CityArray cityArray) {
        this.cityArray = cityArray;
    }

}
