package com.roninaks.hellomywork.helpers;

import android.content.Context;

import com.roninaks.hellomywork.models.AnnouncementsModel;
import com.roninaks.hellomywork.models.CategoryModel;
import com.roninaks.hellomywork.models.ProfilePostModel;
import com.roninaks.hellomywork.models.SearchSuggestionsModel;
import com.roninaks.hellomywork.models.ServiceProviderModel;
import com.roninaks.hellomywork.models.TopPerformerModel;
import com.roninaks.hellomywork.models.UnionModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nihalpradeep on 05/09/18.
 */

public class ModelHelper {

    Context context;
    int id=0;

    public ModelHelper(Context context) {
        this.context = context;
    }

    public ModelHelper() {
    }


    public AnnouncementsModel buildAnnouncementModel(JSONObject jsonObject) {
        try{
            AnnouncementsModel announcementsModel = new AnnouncementsModel();
            String time = jsonObject.getString("modDate");
            String[] splitStr = time.split("\\s+");
            announcementsModel.setAnnouncementDate(splitStr[0]);
            announcementsModel.setAnnouncementMessage(jsonObject.getString("content"));
            announcementsModel.setGetAnnouncementTime(splitStr[1]);

            return announcementsModel;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new AnnouncementsModel();
    }

    public TopPerformerModel buildTopPerformerModel(JSONObject jsonObject) {
        try{
            TopPerformerModel topPerformerModel = new TopPerformerModel();
            id++;
            topPerformerModel.setId(String.valueOf(id));
            topPerformerModel.setUserName(jsonObject.getString("userName"));
            topPerformerModel.setUserConversions(jsonObject.getString("conversions"));
            return topPerformerModel;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new TopPerformerModel();
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
            profilePostModel.setId(jsonObject.getString("id"));
            profilePostModel.setLikeCount(jsonObject.getString("likes"));
            profilePostModel.setCommentCount(jsonObject.getString("commentnumber"));
            profilePostModel.setIsLiked(jsonObject.getString("isLiked"));
            profilePostModel.setImageLabel(jsonObject.getString("offer"));
            profilePostModel.setIsBoomarked(jsonObject.getString("IsBookmarked"));
            return profilePostModel;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ProfilePostModel();
    }
  
    public CategoryModel buildCategoryModel(JSONObject jsonObject){
        try {
            CategoryModel categoryModel = new CategoryModel();
            categoryModel.setId(Integer.parseInt(jsonObject.getString("union_id")));
            categoryModel.setName(jsonObject.getString("name"));
            categoryModel.setIcon(jsonObject.getString("image"));
            categoryModel.setLink(jsonObject.getString("link"));
            categoryModel.setFrequency(Integer.parseInt(jsonObject.getString("freq")));
            categoryModel.setColor(jsonObject.getString("color"));
            categoryModel.setTag(jsonObject.getString("tag"));
            categoryModel.setUnionName(jsonObject.getString("unionName"));
            return categoryModel;

        }catch (Exception e){

        }
        return new CategoryModel();
    }

    public ServiceProviderModel buildServiceProviderModel(JSONObject jsonObject, String type){
        try{
            ServiceProviderModel serviceProviderModel = new ServiceProviderModel();
            switch (type){
                case "premiumsignup":{
                    serviceProviderModel.setId(Integer.parseInt(jsonObject.getString("profile_id")));
                    serviceProviderModel.setImage(jsonObject.getString("profile_image"));
                    serviceProviderModel.setName(jsonObject.getString("name"));
                    serviceProviderModel.setRole(jsonObject.getString("role"));
                    serviceProviderModel.setRating(Float.parseFloat(jsonObject.getString("rating")));
                    serviceProviderModel.setLink(jsonObject.getString("profile_link"));
                    serviceProviderModel.setSublocation(jsonObject.getString("sublocation"));
                    serviceProviderModel.setWhatsapp(jsonObject.getString("whatapp"));
                    serviceProviderModel.setLocation(jsonObject.getString("location"));
                    serviceProviderModel.setSkills(jsonObject.getString("skills"));
                    serviceProviderModel.setUnionName(jsonObject.getString("union"));
                    serviceProviderModel.setWebsite(jsonObject.getString("website"));
                    serviceProviderModel.setPhone(jsonObject.getString("phone"));
                    serviceProviderModel.setEmail(jsonObject.getString("email"));
                    serviceProviderModel.setAddress(jsonObject.getString("address"));
                    serviceProviderModel.setCardUrl(jsonObject.getString("card"));
                    serviceProviderModel.setPassword(jsonObject.getString("password"));
                    serviceProviderModel.setPincode(jsonObject.getString("pincode"));
                    serviceProviderModel.setPhone2(jsonObject.getString("phone2"));
                    serviceProviderModel.setState(jsonObject.getString("state"));
                    serviceProviderModel.setCountry(jsonObject.getString("country"));
                    serviceProviderModel.setOrgType(jsonObject.getString("type"));
                    serviceProviderModel.setPrivate(jsonObject.getString("privatetag").equals("1"));
                    serviceProviderModel.setActive(jsonObject.getString("isActive").equals("1"));
                    serviceProviderModel.setProspect(jsonObject.getString("isProspect").equals("1"));
                    serviceProviderModel.setCategory(jsonObject.getString("category"));
                    break;
                }
                case "top_performers":{
                    serviceProviderModel.setId(Integer.parseInt(jsonObject.getString("profile_id")));
                    serviceProviderModel.setImage(jsonObject.getString("profile_image"));
                    serviceProviderModel.setName(jsonObject.getString("name"));
                    serviceProviderModel.setRole(jsonObject.getString("role"));
                    serviceProviderModel.setRating(Float.parseFloat(jsonObject.getString("rating")));
                    serviceProviderModel.setLink(jsonObject.getString("profile_link"));
                    break;
                }
                case "search_profiles":{
                    serviceProviderModel.setName(jsonObject.getString("userName"));
                    serviceProviderModel.setRole(jsonObject.getString("userRole"));
                    serviceProviderModel.setLocation(jsonObject.getString("userLoc"));
                    serviceProviderModel.setSublocation(jsonObject.getString("userSubLoc"));
                    serviceProviderModel.setWhatsapp(jsonObject.getString("userWPhone"));
                    serviceProviderModel.setEmail(jsonObject.getString("userMail"));
                    serviceProviderModel.setPhone(jsonObject.getString("userPhone"));
                    serviceProviderModel.setId(Integer.parseInt(jsonObject.getString("userId")));
                    serviceProviderModel.setRating(Float.parseFloat(jsonObject.getString("rating")));
                    serviceProviderModel.setReview(Integer.parseInt(jsonObject.getString("review")));
                    serviceProviderModel.setCardUrl(jsonObject.getString("card"));
                    serviceProviderModel.setPremium(jsonObject.getString("premium").equals("1"));
                    serviceProviderModel.setBookmarked(jsonObject.getString("is_bookmarked").equals("1"));
                }
            }
            return serviceProviderModel;
        }catch (Exception e){

        }
        return new ServiceProviderModel();
    }

    public UnionModel buildUnionModel(JSONObject jsonObject){
        try{
            UnionModel unionModel = new UnionModel();
            unionModel.setId(Integer.parseInt(jsonObject.getString("union_id")));
            unionModel.setName(jsonObject.getString("name"));
            unionModel.setLink(jsonObject.getString("link"));
            return unionModel;

        }catch (Exception e){

        }
        return new UnionModel();
    }

    public SearchSuggestionsModel buildSearchSuggestionsModel(String master, String type){
        SearchSuggestionsModel searchSuggestionsModel = new SearchSuggestionsModel();
        try{
            String splitString[] = master.split("~");
            searchSuggestionsModel.setCategoryName(splitString[0]);
            searchSuggestionsModel.setCategoryId(Integer.parseInt(splitString[1]));
            searchSuggestionsModel.setLocationId(Integer.parseInt(splitString[2]));
        }catch (Exception e){
            return new SearchSuggestionsModel();
        }
        return searchSuggestionsModel;
    }

//    public CommentsModel buildComments(JSONObject jsonObject) {
//        CommentsModel commentsModel = new CommentsModel();
//        commentsModel.getComment()
//    }
}
