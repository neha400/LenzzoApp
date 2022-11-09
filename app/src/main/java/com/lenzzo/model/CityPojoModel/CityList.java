package com.lenzzo.model.CityPojoModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CityList implements Serializable {

    @SerializedName("zip_id")
    @Expose
    private String zipId;
    @SerializedName("region_id")
    @Expose
    private String regionId;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("default_name")
    @Expose
    private Object defaultName;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("delivery_charge")
    @Expose
    private String deliveryCharge;
    @SerializedName("country_id")
    @Expose
    private String countryId;

    public String getZipId() {
        return zipId;
    }

    public void setZipId(String zipId) {
        this.zipId = zipId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(Object defaultName) {
        this.defaultName = defaultName;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

}