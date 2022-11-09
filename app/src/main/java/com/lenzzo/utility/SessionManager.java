package com.lenzzo.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;

    private static String USER_ID = "user_id";
    private static String USER_EMAIL = "user_email";
    private static String USER_PASSWORD = "user_password";

    private static String USER_SAVE_PASSWORD = "user_save_password";

    private static String RANDOM_VALUE = "random_value";
    private static String USER_NAME = "user_name";
    private static String COUNTRY_CODE = "country_code";
    private static String CURRENCY_CODE = "currency_code";

    private static String BRAND_ID = "brand_id";
    private static String LEFT_EYE_POWER = "left_eye_power";
    private static String RIGHT_EYE_POWER = "right_eye_power";
    private static String LEFT_CYL_POWER = "left_cyl_power";
    private static String RIGHT_CYL_POWER = "right_cyl_power";
    private static String LEFT_AXIS_POWER = "left_axis_power";
    private static String RIGHT_AXIS_POWER = "right_axis_power";
    private static String LEFT_ADD_POWER = "left_cyl_power";
    private static String RIGHT_ADD_POWER = "right_cyl_power";
    private static String EYE_POWER_TYPE = "power_type";


    private static String LANGUAGE_SELECTED ="language_selected";
    private static String FROM_NOTIFICATION = "from_notification";
    private static String COLLECTION_BRAND_ID = "collection_brand_id";
    private static String USER_TYPE = "user_type";
    private static String APPLY_FILTER = "apply_filter";

    private static String PUSH_BRAND_ID = "push_brand_id";
    private static String PUSH_FAMILY_ID = "push_family_id";

    private static String power_select_typ = "power_selection_type";


    private static String REDEEM_POINT_STATUS = "redeem_point_status";
    private static String REDEEM_POINT_FLAG_STATUS = "flag_status";

    private static String OLD_TOTAL = "with_offer_total";
    private static String NEW_TOTAL = "without_offer_total";
    private static String NEW_SUB_TOTAL = "new_sub_total";
    private static String Express_Delivery_Charges = "express_delivery_charges";

    private static String City_Zipp_code = "city_zipp_code";


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(Utils.PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    public void clearPreferences() {
        pref.edit().clear().apply();

    }

    public void clearSelectedredeempoint() {
        pref.edit().remove("redeem_point_status")

                .apply();

    }

    public void clearNewSubTotal() {
        pref.edit().remove("new_sub_total")

                .apply();

    }

    public void clearexpresscharge() {
        pref.edit().remove("express_delivery_charges")

                .apply();

    }

    public void clearzippcode() {
        pref.edit().remove("city_zipp_code")

                .apply();

    }

    public void clearSelectedPreferences() {
        pref.edit().remove("user_id")
                .remove("user_email")
                .remove("user_password")
                .remove("user_save_password")
                .remove("random_value")
                .remove("user_name")
                .remove("brand_id")
                .remove("left_eye_power")
                .remove("right_eye_power")
                .remove("from_notification")
                .remove("collection_brand_id")
                .remove("user_type")
                .remove("apply_filter")
                .remove("user_type")
                .remove("apply_filter")
                .remove("redeem_point_status")
                .remove("flag_status")
                .remove("with_offer_total")
                .remove("without_offer_total")
                .remove("new_sub_total")
                .remove("express_delivery_charges")
                .remove("city_zipp_code")
                .apply();

    }

    public void removeId(String userId,String pass){
        pref.edit().remove(userId).remove(pass).apply();
    }

    public String getPushFamilyId() {
        return pref.getString(PUSH_FAMILY_ID, "");
    }

    public void setPushFamilyId(String familyId) {
        editor.putString(PUSH_FAMILY_ID, familyId);
        editor.commit();
    }

    public String getPower_select_typ() {
        return pref.getString(power_select_typ, "");
    }

    public void setPower_select_typ(String powerSelectTyp) {
        editor.putString(power_select_typ, powerSelectTyp);
        editor.commit();
    }


    public String getRedeemPointStatus() {
        return pref.getString(REDEEM_POINT_STATUS, "");
    }

    public void setRedeemPointStatus(String redeemPointStatus) {
        editor.putString(REDEEM_POINT_STATUS, redeemPointStatus);
        editor.commit();
    }

    public String getRedeemPointFlagStatus() {
        return pref.getString(REDEEM_POINT_FLAG_STATUS, "");
    }

    public void setRedeemPointFlagStatus(String flagStatus) {
        editor.putString(REDEEM_POINT_FLAG_STATUS, flagStatus);
        editor.commit();
    }

    public String getOldTotal() {
        return pref.getString(OLD_TOTAL, "");
    }

    public void setOldTotal(String with_offer_total) {
        editor.putString(OLD_TOTAL, with_offer_total);
        editor.commit();
    }

    public String getNewTotal() {
        return pref.getString(NEW_TOTAL, "");
    }

    public void setNewTotal(String without_offer_total) {
        editor.putString(NEW_TOTAL, without_offer_total);
        editor.commit();
    }

    public String getNewSubTotal() {
        return pref.getString(NEW_SUB_TOTAL, "");
    }

    public void setNewSubTotal(String new_sub_total) {
        editor.putString(NEW_SUB_TOTAL, new_sub_total);
        editor.commit();
    }

    public String getExpress_Delivery_Charges() {
        return pref.getString(Express_Delivery_Charges, "");
    }

    public void setExpress_Delivery_Charges(String express_Delivery_Charges) {
        editor.putString(Express_Delivery_Charges, express_Delivery_Charges);
        editor.commit();
    }

    public String getCity_Zipp_code() {
        return pref.getString(City_Zipp_code, "");
    }

    public void setCity_Zipp_code(String city_code) {
        editor.putString(City_Zipp_code, city_code);
        editor.commit();
    }



    public String getPushBrandId() {
        return pref.getString(PUSH_BRAND_ID, "");
    }

    public void setPushBrandId(String brandId) {
        editor.putString(PUSH_BRAND_ID, brandId);
        editor.commit();
    }

    public String getUserType() {
        return pref.getString(USER_TYPE, "");
    }

    public void setUserType(String userType) {
        editor.putString(USER_TYPE, userType);
        editor.commit();
    }

    public String getUserId() {
        return pref.getString(USER_ID, "");
    }

    public void setUserId(String userId) {
        editor.putString(USER_ID, userId);
        editor.commit();
    }

    public String getUserEmail() {
        return pref.getString(USER_EMAIL, "");
    }

    public void setUserEmail(String userEmail) {
        editor.putString(USER_EMAIL, userEmail);
        editor.commit();
    }

    public String getUserPassword() {
        return pref.getString(USER_PASSWORD, "");
    }

    public void setUserPassword(String userPassword) {
        editor.putString(USER_PASSWORD, userPassword);
        editor.commit();
    }

    public String getUserSavePassword() {
        return pref.getString(USER_SAVE_PASSWORD, "");
    }

    public void setUserSavePassword(String userSavePassword) {
        editor.putString(USER_SAVE_PASSWORD, userSavePassword);
        editor.commit();
    }

    public String getRandomValue() {
        return pref.getString(RANDOM_VALUE, "");
    }

    public void setRandomValue(String randomValue) {
        editor.putString(RANDOM_VALUE, randomValue);
        editor.commit();
    }

    public String getUserName() {
        return pref.getString(USER_NAME, "");
    }

    public void setUserName(String userName) {
        editor.putString(USER_NAME, userName);
        editor.commit();
    }

    public String getCountryCode() {
        return pref.getString(COUNTRY_CODE, "");
    }

    public void setCountryCode(String countryCode) {
        editor.putString(COUNTRY_CODE, countryCode);
        editor.commit();
    }

    public String getCurrencyCode() {
        return pref.getString(CURRENCY_CODE, "");
    }

    public void setCurrencyCode(String currencyCode) {
        editor.putString(CURRENCY_CODE, currencyCode);
        editor.commit();
    }

    public String getBrandId() {
        return pref.getString(BRAND_ID, "");
    }

    public void setBrandId(String brandId) {
        editor.putString(BRAND_ID, brandId);
        editor.commit();
    }

    public String getLeftEyePower() {
        return pref.getString(LEFT_EYE_POWER, "");
    }

    public void setLeftEyePower(String leftEyePower) {
        editor.putString(LEFT_EYE_POWER, leftEyePower);
        editor.commit();
    }

    public String getRightEyePower() {
        return pref.getString(RIGHT_EYE_POWER, "");
    }

    public void setRightEyePower(String rightEyePower) {
        editor.putString(RIGHT_EYE_POWER, rightEyePower);
        editor.commit();
    }

    public String getLanguageSelected() {
        return pref.getString(LANGUAGE_SELECTED, "");
    }

    public void setLanguageSelected(String languageSelected) {
        editor.putString(LANGUAGE_SELECTED, languageSelected);
        editor.commit();
    }

    public boolean getIsFromOfferNotification() {
        return pref.getBoolean(FROM_NOTIFICATION, false);
    }

    public void setIsFromOfferNotification(boolean flag) {
        editor.putBoolean(FROM_NOTIFICATION, flag);
        editor.commit();
    }

    public String getCollectionBrandId() {
        return pref.getString(COLLECTION_BRAND_ID, "");
    }

    public void setCollectionBrandId(String collection_brand_id) {
        editor.putString(COLLECTION_BRAND_ID, collection_brand_id);
        editor.commit();
    }

    public boolean shouldFilterApply() {
        return pref.getBoolean(APPLY_FILTER, false);
    }

    public void setShouldFilterApply(boolean flag) {
        editor.putBoolean(APPLY_FILTER, flag);
        editor.commit();
    }

    public String getLeftCylPower() {
        return pref.getString(LEFT_CYL_POWER, "");
    }

    public void setLeftCylPower(String leftCylPower) {
        editor.putString(LEFT_CYL_POWER, leftCylPower);
        editor.commit();
    }

    public String getRightCylPower() {
        return pref.getString(RIGHT_CYL_POWER, "");
    }

    public void setRightCylPower(String rightCylPower) {
        editor.putString(RIGHT_CYL_POWER, rightCylPower);
        editor.commit();
    }
    public String getLeftAxisPower() {
        return pref.getString(LEFT_AXIS_POWER, "");
    }

    public void setLeftAxisPower(String leftAxisPower) {
        editor.putString(LEFT_AXIS_POWER, leftAxisPower);
        editor.commit();
    }
    public String getRightAxisPower() {
        return pref.getString(RIGHT_AXIS_POWER, "");
    }

    public void setRightAxisPower(String rightAxisPower) {
        editor.putString(RIGHT_AXIS_POWER, rightAxisPower);
        editor.commit();
    }
    public String getRightAddPower() {
        return pref.getString(RIGHT_ADD_POWER, "");
    }

    public void setRightAddPower(String rightAddPower) {
        editor.putString(RIGHT_ADD_POWER, rightAddPower);
        editor.commit();
    }

    public String getLeftAddPower() {
        return pref.getString(LEFT_ADD_POWER, "");
    }

    public void setLeftAddPower(String rightAddPower) {
        editor.putString(LEFT_ADD_POWER, rightAddPower);
        editor.commit();
    }

}
