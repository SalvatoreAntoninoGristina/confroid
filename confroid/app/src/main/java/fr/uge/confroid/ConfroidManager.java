package fr.uge.confroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import fr.uge.confroid.utlis.ConfroidUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class ConfroidManager {

    private static ConfroidManager confroidManager;

    private ConfroidManager() {}

    public static ConfroidManager getConfroidManager(Context context) {
        if (confroidManager == null) {
            confroidManager = new ConfroidManager();
        }
        return confroidManager;
    }

    public static void saveConfiguration(Context context, Bundle bundle) {
        //**** SAVE IN JSON FILE *****/
        JSONObject jsonObject = ConfroidUtils.fromBundleToJson(bundle);

        Log.i("dirName", String.valueOf(context.getFilesDir()));
        File file = new File(context.getFilesDir(),bundle.getString("name") + ".json");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(jsonObject.toString());
            Log.i("jsonObj", jsonObject.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bundle loadConfiguration(Context context, String name, String version) {

        /**** LOAD FROM JSON FILE *****/

        File file = new File(context.getFilesDir(),name + ".json");
        Log.i("bundle", file.getAbsolutePath());
        FileReader fileReader = null;
        String response = null;
        try {
            fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            response = stringBuilder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject  = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ConfroidUtils.fromJsonToBundle(jsonObject);
    }

    public static List<String> loadAllConfigurationNames(Context context) throws JSONException {
        //***** LOAD FROM JSON FILE *****/

        List<String> names = new ArrayList<>();

        for (String strFile : context.getFilesDir().list()) {
            names.add(strFile.substring(0, strFile.length()-5));
        }

        Log.i("names", names.toString());
        return names;

    }

    public static Intent loadAllConfigurationVersions(Context context, String name, String requestId) {
        //***** LOAD FROM JSON FILE *****/

        File file = new File(context.getFilesDir(), "config.json");
        FileReader fileReader = null;
        String response = null;
        try {
            fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            response = stringBuilder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();
        Map<String, Bundle> versions = new HashMap<>();

        try {
            jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if(obj.get("name").equals(name)){
                    Bundle bundle = new Bundle();
                    bundle.putString("tag", obj.getString("tag"));
                    bundle.putString("date", obj.get("date").toString());
                    bundle.putString("content", obj.get("content").toString());
                    versions.put(obj.getString("version"), bundle);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Bundle contentBundle = ConfroidUtils.toBundle(versions);

        Intent intent = new Intent();
        intent.putExtra("requestId", requestId);
        intent.putExtra("name", name);
        intent.putExtra("content", contentBundle);

        Log.i("loadVersions", requestId);
        Log.i("loadVersions", name);
        Log.i("loadVersions", contentBundle.toString());

        return intent;
    }
}
