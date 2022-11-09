package com.lenzzo.updatedVersionChecker;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;

public class GooglePlayStoreAppVersionNameLoader extends AsyncTask<String, Void, String> {

    String newVersion = "";
    String currentVersion = "";
    VersionCheckListner mWsCallerVersionListener;
    boolean isVersionAvailabel;
    boolean isAvailableInPlayStore;
    Context mContext;
    String mStringCheckUpdate = "";

    public GooglePlayStoreAppVersionNameLoader(Context mContext, VersionCheckListner callback) {
        mWsCallerVersionListener = callback;
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    /*  private boolean web_update(){
          try {
              String curVersion = applicationContext.getPackageManager().getPackageInfo(package_name, 0).versionName;
              String newVersion = curVersion;
              newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + package_name + "&hl=en")
                      .timeout(30000)
                      .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                      .referrer("http://www.google.com")
                      .get()
                      .select("div[itemprop=softwareVersion]")
                      .first()
                      .ownText();
              return (value(curVersion) < value(newVersion)) ? true : false;
          } catch (Exception e) {
              e.printStackTrace();
              return false;
          }
      }*/
    @Override
    protected String doInBackground(String... urls) {
        try {
            Log.d("DoInBackground","Here");
            String mStringCheckUpdate = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
            Log.d("CurrentVersion",mStringCheckUpdate);
            isAvailableInPlayStore = true;
            if (isNetworkAvailable(mContext)) {
                mStringCheckUpdate = Jsoup.connect("https://play.google.com/store/apps/details?id=" + mContext.getPackageName())
                        .timeout(10000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();
                Log.d("CheckResponse",mStringCheckUpdate);
                Log.d("PlayStoreVersion",mStringCheckUpdate);
                return mStringCheckUpdate;
            }

        } catch (Exception e) {
            isAvailableInPlayStore = false;
            Log.d("ExceptionResponse",mStringCheckUpdate);
            e.printStackTrace();
            return mStringCheckUpdate;
        } catch (Throwable e) {
            isAvailableInPlayStore = false;
            Log.d("ThrowableResponse",mStringCheckUpdate);
            return mStringCheckUpdate;
            // e.printStackTrace();
        }

        return mStringCheckUpdate;
    }

    @Override
    protected void onPostExecute(String string) {
        Log.d("Post","Here");
        if (isAvailableInPlayStore == true) {
            newVersion = string;
            Log.d("new Version", newVersion);
            checkApplicationCurrentVersion();
            if (currentVersion.equalsIgnoreCase(newVersion)) {
                isVersionAvailabel = false;
               // Toast.makeText(mContext,"Your app is updated.", Toast.LENGTH_LONG).show(); //mContext.getResources().getString(R.string.app_upto_date)
            } else {
                isVersionAvailabel = true;
            }
            mWsCallerVersionListener.onGetResponse(isVersionAvailabel);
        }
    }

    /**
     * Method to check current app version
     */
    public void checkApplicationCurrentVersion() {
        Log.d("checkApp","Here");
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        currentVersion = packageInfo.versionName;
        Log.e("currentVersion", currentVersion);
    }

    /**
     * Method to check internet connection
     * @param context
     * @return
     */
    public boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}