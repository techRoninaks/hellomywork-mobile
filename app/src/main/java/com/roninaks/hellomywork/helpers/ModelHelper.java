package com.roninaks.hellomywork.helpers;

import android.content.Context;

import com.roninaks.hellomywork.models.ProfilePostModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nihalpradeep on 05/09/18.
 */

public class ModelHelper {

    Context context;

    public ModelHelper(Context context) {
        this.context = context;
    }

    public ProfilePostModel buildProfilePostModel(JSONObject jsonObject) {
        try{
            ProfilePostModel profilePostModel = new ProfilePostModel();
            profilePostModel.setName(jsonObject.getString("name"));
            profilePostModel.setDescription(jsonObject.getString("des"));
            profilePostModel.setDate(jsonObject.getString("date"));
            profilePostModel.setLocation(jsonObject.getString("location"));
            profilePostModel.setTime(jsonObject.getString("role"));
            profilePostModel.setImageUri(jsonObject.getString("postimage"));

            return profilePostModel;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ProfilePostModel();
    }
}
