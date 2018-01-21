package com.vav.cn.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.content.IntentCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.vav.cn.HomeMemberActivity;
import com.vav.cn.R;
import com.vav.cn.config.Config;
import com.vav.cn.gcm.GcmConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Handrata Samsul on 11/11/2015.
 */
public class GeneralUtil {
    private static GeneralUtil instance = new GeneralUtil();
    private final String TAG_LOG = GeneralUtil.this.getClass().getSimpleName();

    public static GeneralUtil getInstance() {
        return instance;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public final static boolean isValidName(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else if (target.length() < 3) {
            return false;
        } else {
            return true;
        }
    }

    public final static boolean isValidPhoneNumber(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else if (target.length() < 8 || target.length() > 12) {
            return false;
        } else {
            return true;
        }
    }

    public final static boolean isValidPassword(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else if (target.length() < 8) {
            return false;
        } else {
            return true;
        }
    }

    public static String hashPassword(String password) {
        String hashPassword;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            md.update(password.getBytes());
            byte byteData[] = md.digest();
            hashPassword = Base64.encodeToString(byteData, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException(e);
        }
        return hashPassword;
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public static long randLong(long min, long max) {
        Random rand = new Random();
        long randomNum = rand.nextLong() * (max - min) + min;
        return randomNum;
    }

    public static String randomString(int maxLength) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(maxLength);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public static Typeface getFontTypeFace(Context context, String strTypeFace) {
        switch (strTypeFace) {
            case Config.FONT_PRIMARY:
                return Typeface.createFromAsset(context.getAssets(), "fonts/fzy3jw.ttf");
        }
        return null;
    }

    public static String getDateFromTimeMillis(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static int dpToPx(Context context, double dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public boolean isOnlineOrConnecting(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public Location getCurrentLocation(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        if (location == null) {

        }

        return location;
    }

    public Bitmap getCroppedCircularBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0x00424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public String readFromFile(String filePath) {
        String ret = "";
        File file = new File(filePath);
        if (!file.exists()) return ret;

        try {
            InputStream inputStream = new FileInputStream(file);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public String saveImageInExternalCacheDir(Context context, Bitmap bitmap, String myfileName) {
        String fileDir = getAppFolderPath(context);
        String fileName = myfileName.replace(' ', '_');
        ;
        String filePath = "";

        fileDir += File.separator + Config.IMAGE_DIR;
        File folderImage = new File(fileDir);

        boolean success = true;
        if (!folderImage.exists()) {
            success = folderImage.mkdir();
        }

        if (success) {
            //filePath = fileDir + File.separator + fileName + ".bmp";
            filePath = fileDir + File.separator + fileName + ".jpg";
            try {
                FileOutputStream fos = new FileOutputStream(new File(filePath));
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "Failed create dir", Toast.LENGTH_LONG).show();
        }
        Log.d(TAG_LOG, "Saved image to: " + filePath);
        return filePath;
    }

    public void closeSoftKeyboard(Activity activity) {
        //Close soft keyboard when save clicked
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        //////////
    }

    public void showSoftKeyboard(Activity activity) {
        //Close soft keyboard when save clicked
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
        //////////
    }

    public Boolean isDateExpired(String date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date expiredDate = dateFormat.parse(date);
            return new Date().after(expiredDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean isAlmostExpired(String date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date expiredDate = dateFormat.parse(date);
            Date currentDate = new Date();
            return getDateDiff(currentDate, expiredDate, TimeUnit.DAYS) <= 7;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String formatExpireDateFromServer(String expireDate) {
        String fmtString = "";
        SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat appFormat = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date date = serverFormat.parse(expireDate);

            fmtString = appFormat.format(date);

            return fmtString;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return fmtString;
    }

    public Date parseExpireDateFromServer(String expireDate) {
        String fmtString = "";
        SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return serverFormat.parse(expireDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public String getAppFolderPath(Context context) {
        String appFolderPath = PreferenceManagerCustom.getInstance().getFileDirPath();
        File folder = null;

        if (appFolderPath.equals("")) {
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) { // use external storage
                appFolderPath = getExternalFileDir(context);
            } else { //use internal storage
                appFolderPath = getInternalFileDir(context);
            }
            folder = new File(appFolderPath);
            if (folder.mkdir()) {
                PreferenceManagerCustom.getInstance().setFileDirPath(appFolderPath);
            }
        } else {
            folder = new File(appFolderPath);

            if (!folder.exists()) {
                if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) { // use external storage
                    appFolderPath = getExternalFileDir(context);
                } else { //use internal storage
                    appFolderPath = getInternalFileDir(context);
                }
                folder = new File(appFolderPath);
                if (folder.mkdir()) {
                    PreferenceManagerCustom.getInstance().setFileDirPath(appFolderPath);
                }
            }
        }
        return appFolderPath;
    }

    private String getExternalFileDir(Context context) {
        return Environment.getExternalStorageDirectory() + File.separator
                + context.getString(R.string.app_name);
    }

    private String getInternalFileDir(Context context) {
        return "/data/data/" + context.getPackageName()
                + File.separator + context.getString(R.string.app_name);
    }

    public void goToHomeMemberPage(Activity activity) {
        Intent intent = new Intent(activity, HomeMemberActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public String formatUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        return url;
    }

    public boolean checkPlayServices(Activity activity) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, GcmConfig.PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.d(TAG_LOG, "This device is not supported.");
                activity.finish();
            }
            return false;
        }
        return true;
    }

}

