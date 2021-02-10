package fr.uge.confroid.utlis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import androidx.annotation.RequiresApi;
import fr.uge.confroid.sqlite.ConfroidContract;
import fr.uge.confroid.sqlite.ConfroidDbHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.function.Consumer;

public class ConfroidUtils {

    public static Bundle toBundle(Object object) {
        Bundle bundle = new Bundle();
        if (object instanceof List) {
            List list = (List) object;
            for (Object elem : list) {
                if (elem instanceof String) {
                    bundle.putString(list.indexOf(elem) + "", (String) elem);
                } else if (elem instanceof Byte) {
                    bundle.putByte(list.indexOf(elem) + "", (byte) elem);
                } else if (elem instanceof Integer) {
                    bundle.putInt(list.indexOf(elem) + "", (int) elem);
                } else if (elem instanceof Float) {
                    bundle.putFloat(list.indexOf(elem) + "", (float) elem);
                } else if (elem instanceof Boolean) {
                    bundle.putBoolean(list.indexOf(elem) + "", (boolean) elem);
                } else if (elem instanceof List || elem instanceof Map) {
                    bundle.putBundle(list.indexOf(elem) + "", toBundle(elem));
                } else if (elem instanceof Bundle) {
                    bundle.putBundle(list.indexOf(elem)+ "", (Bundle) elem);
                }
            }
        } else if (object instanceof Map) {
            Map map = (Map) object;
            for (Object key : map.keySet()) {
                Object value = map.get(key);
                if (value instanceof String) {
                    bundle.putString(key + "", (String) value);
                } else if (value instanceof Byte) {
                    bundle.putByte(key + "" + "", (byte) value);
                } else if (value instanceof Integer) {
                    bundle.putInt(key + "" + "", (int) value);
                } else if (value instanceof Float) {
                    bundle.putFloat(key + "" + "", (float) value);
                } else if (value instanceof Boolean) {
                    bundle.putBoolean(key + "" + "", (boolean) value);
                } else if (value instanceof List || value instanceof Map) {
                    bundle.putBundle(key + "", toBundle(value));
                } else if (value instanceof Bundle) {
                    bundle.putBundle(key+ "", (Bundle) value);
                }
            }
        }
        return bundle;
    }

    public static String fromBundleToString(Bundle bundle) {
        return fromBundleToString(bundle, 0);
    }

    private static String fromBundleToString(Bundle bundle, int tabNumber) {
        String content = "";
        for (String key : bundle.keySet()) {
            for (int i = 0; i < tabNumber; i++) {
                content += "\t";
            }
            content += key + ": ";
            Object contentObject = bundle.get(key);
            if (contentObject instanceof Bundle) {
                content += "\n" + fromBundleToString((Bundle) contentObject, tabNumber + 1);
            } else {
                content += contentObject.toString();
            }
            content += "\n";
        }
        return content;
    }

    public static JSONObject fromBundleToJson(Bundle bundle){
        JSONObject jsonObject = new JSONObject();
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            Log.i("jsonObject", key);
            try {
                Object value = bundle.get(key);
                if(value instanceof Bundle)
                    jsonObject.put(key, fromBundleToJson((Bundle) value));
                else
                    jsonObject.put(key, bundle.get(key));
                //jsonObject.put(key, JSONObject.wrap(bundle.get(key)));
            } catch(JSONException e) {
                Log.i("jsonObject", "Ecc");
                //Handle exception here
            }
        }

        return jsonObject;
    }

    public static Bundle jsonToBundle(JSONObject jsonObject) throws JSONException {
        Bundle bundle = new Bundle();
        Iterator iter = jsonObject.keys();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            Object value = jsonObject.get(key);

            if(value instanceof JSONObject)
                bundle.putBundle(key, jsonToBundle((JSONObject) value));
            else
                bundle.putString(key, value.toString());
        }
        return bundle;
    }

    /*
    public static void saveConfiguration (Context context, String name, Object value, String versionName) {
        ConfroidDbHelper dbHelper = new ConfroidDbHelper(context);

        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ConfroidContract.ConfroidEntry.COLUMN_NAME_NAME, name);

        HashMap<String, Object> content = new HashMap<String,Object>((HashMap)value);
        values.put(ConfroidContract.ConfroidEntry.COLUMN_NAME_CONTENT, new JSONObject(content).toString());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ConfroidContract.ConfroidEntry.TABLE_NAME, null, values);
    }

    public static <T> void loadConfiguration (Context context, String name, String version, Consumer<T> callback) {
        ConfroidDbHelper dbHelper = new ConfroidDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                ConfroidContract.ConfroidEntry.COLUMN_NAME_NAME,
                ConfroidContract.ConfroidEntry.COLUMN_NAME_CONTENT
        };

        // Filter results WHERE "name" = name
        String selection = ConfroidContract.ConfroidEntry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = { name };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ConfroidContract.ConfroidEntry.COLUMN_NAME_NAME + " DESC";

        Cursor cursor = db.query(
                ConfroidContract.ConfroidEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,           // don't group the rows
                null,            // don't filter by row groups
                sortOrder               // The sort order
        );

        List<String> itemContents = new ArrayList<>();
        while(cursor.moveToNext()) {
            String itemContent = cursor.getString(
                    cursor.getColumnIndexOrThrow(ConfroidContract.ConfroidEntry.COLUMN_NAME_CONTENT));
            itemContents.add(itemContent);
        }
        cursor.close();

        for(String content : itemContents){
            Log.i("content", content);
        }
    }

    public static <T> void subscribeConfiguration (Context context, String name, Consumer <T> callback) {

    }

    public static <T> void cancelConfigurationSubscription (Context context, Consumer <T> callback) {

    }

    public static void getConfigurationVersions (Context context, String name, Consumer <List <Version>> callback) {
        
    }
     */

}
