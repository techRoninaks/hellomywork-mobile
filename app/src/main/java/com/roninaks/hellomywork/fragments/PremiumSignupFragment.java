package com.roninaks.hellomywork.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;

import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.activities.ProfileImage;
import com.roninaks.hellomywork.helpers.ModelHelper;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.helpers.StringHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;
import com.roninaks.hellomywork.models.CategoryModel;
import com.roninaks.hellomywork.models.ServiceProviderModel;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

//import androidx.appcompat.widget.SwitchCompat;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PremiumSignupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PremiumSignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

//TODO Add Bubble
public class PremiumSignupFragment extends Fragment implements SqlDelegate {
    //Constant Declaration
    private static final String ARG_PARAM1 = "type";
    private static final String ARG_PARAM2 = "userId";
    private static final String ARG_PARAM3 = "employeeId";

    //Public Static Methods
    public static String imageUrl = "";
    public static boolean imageChanged = false;
    public static Bitmap bitmap;

    //Variable Declaration
    private String type;
    private String userId;
    private String employeeId;
    private boolean categoryLoaded = false;
    private ArrayList<CategoryModel> categoryList;
    private ArrayList<String> countryList;
    private ArrayList<String> stateList;
    private ArrayList<String> locationList;
    private ServiceProviderModel serviceProviderModel;
    private Context context;
    private View rootView;

    //Listener Declaration
    private OnFragmentInteractionListener mListener;

    //Views Declaration
    private EditText etName, etAddress, etPincode, etPrimaryPhone, etSublocation, etUnion, etEmail, etPassword, etConfirmPassword, etWhatsapp, etRole, etWebsite, etSkills, etSecondaryContacts;
    private Spinner spCountry, spState, spLocation, spOrgType, spCategory;
    private CheckBox cbProspect;
    private Button btnUploadImage, btnSave, btnSubmit;
    private SwitchCompat switchPrivacy;
    private ImageView ivBack;

    public PremiumSignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Type of Operation Edit/New.
     * @param param2 User Id.
     * @return A new instance of fragment PremiumSignupFragment.
     */

    public static PremiumSignupFragment newInstance(String param1, String param2, String param3) {
        PremiumSignupFragment fragment = new PremiumSignupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(ARG_PARAM1);
            userId = getArguments().getString(ARG_PARAM2);
            employeeId = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public void onResume() {
        if(imageChanged){
            btnUploadImage.setBackgroundResource(R.drawable.career_button_color_radius);
            btnUploadImage.setTextColor(getResources().getColor(R.color.colorTextWhitePrimary));
            btnSubmit.setBackgroundResource(R.drawable.card_background_shape);
            btnSubmit.setTextColor(getResources().getColor(R.color.colorTextBlackPrimary));
        }
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_premium_signup, container, false);
        rootView = v;
        context = getActivity();
        countryList = new ArrayList<>();
        stateList = new ArrayList<>();
        locationList = new ArrayList<>();
        etName = (EditText) v.findViewById(R.id.editText_Name);
        etAddress = (EditText) v.findViewById(R.id.editText_Address);
        etPincode = (EditText) v.findViewById(R.id.editText_Pin);
        etPrimaryPhone = (EditText) v.findViewById(R.id.editText_PrimaryNumber);
        etEmail = (EditText) v.findViewById(R.id.editText_Email);
        etPassword = (EditText) v.findViewById(R.id.editText_Password);
        etConfirmPassword = (EditText) v.findViewById(R.id.editText_PasswordConfirm);
        etWhatsapp = (EditText) v.findViewById(R.id.editText_Whatsapp);
        etRole = (EditText) v.findViewById(R.id.editText_Role);
        etWebsite = (EditText) v.findViewById(R.id.editText_Website);
        etSkills = (EditText) v.findViewById(R.id.editText_Skills);
        etSecondaryContacts = (EditText) v.findViewById(R.id.editText_SecondaryContact);
        etSublocation = (EditText) v.findViewById(R.id.editText_Sublocation);
        etUnion = (EditText) v.findViewById(R.id.editText_Union);
        cbProspect = (CheckBox) v.findViewById(R.id.cb_Prospect);
        btnUploadImage = (Button) v.findViewById(R.id.btn_Upload);
        btnSave = (Button) v.findViewById(R.id.btn_Save);
        btnSubmit = (Button) v.findViewById(R.id.btn_Submit);
        spCountry = (Spinner) v.findViewById(R.id.spinner_country);
        spState = (Spinner) v.findViewById(R.id.spinner_state);
        spLocation = (Spinner) v.findViewById(R.id.spinner_location);
        spOrgType = (Spinner) v.findViewById(R.id.spinner_org_type);
        spCategory = (Spinner) v.findViewById(R.id.spinner_category);
        switchPrivacy = (SwitchCompat) v.findViewById(R.id.switch_Privacy);
        ivBack = (ImageView) v.findViewById(R.id.imgBack);

        //Setting defaults
        if(userId != null){
            if(!userId.isEmpty()){
                loadUserDetails();
            }
        }
        if(employeeId == null)
            employeeId = ((MainActivity) context).isAdminLoggedIn();
        if(employeeId != null){
            if(!employeeId.isEmpty()){
                btnSave.setVisibility(View.VISIBLE);
                cbProspect.setVisibility(View.VISIBLE);
            }
        }
        Resources res = context.getResources();
        countryList.addAll(Arrays.asList(res.getStringArray(R.array.countries_array)));
        stateList.addAll(Arrays.asList(res.getStringArray(R.array.india_states)));
        locationList.addAll(Arrays.asList(res.getStringArray(R.array.india_top_places)));
        ArrayAdapter<String> adapterStates = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, stateList);
        adapterStates.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapterCountries = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, countryList);
        adapterCountries.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, locationList);
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapterOrgType = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item,context.getResources().getStringArray(R.array.org_array));
        adapterOrgType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOrgType.setAdapter(adapterOrgType);
        spCountry.setAdapter(adapterCountries);
        spLocation.setAdapter(adapterLocation);
        spState.setAdapter(adapterStates);
        loadCategories();
        //On click listeners
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate()){
                    submitInformation();
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInformation();
            }
        });
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileImage.class);
                Bundle bundle = new Bundle();
                bundle.putString("image", imageUrl);
                bundle.putString("fragment", "premiumsignup");
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                    etUnion.setText(categoryList.get(position-1).getUnionName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).onBackPressed();
            }
        });
        return  v;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResponse(SqlHelper sqlHelper) {
        try{
            switch(sqlHelper.getActionString()){
                case "categories":{
                    JSONArray jsonArray = new JSONArray(sqlHelper.getStringResponse());
                    String response = jsonArray.getJSONObject(0).getString("response");
                    if(response.equals(context.getString(R.string.response_success))){
                        buildCategories(jsonArray);
                    }else{
                        Toast.makeText(context, "Sorry. Your category list seems empty", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case "user":{
                    JSONArray jsonArray = new JSONArray(sqlHelper.getStringResponse());
                    String response = jsonArray.getJSONObject(0).getString("response");
                    if(response.equals(context.getString(R.string.response_success))){
                        populateUserDetails(jsonArray);
                    }else{
                        Toast.makeText(context, "Sorry. Fetching user details failed", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case "submit":{
                    if(type.toLowerCase().equals("new")) {
                        Fragment fragment = PlansFragment.newInstance(userId, "");
                        ((MainActivity) context).initFragment(fragment, "plans");
                    }else{
                        ((MainActivity) context).onBackPressed();
                    }
                    break;
                }
                case "save":{
                    Toast.makeText(context, "Information saved successfully", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }catch(Exception e){
            Toast.makeText(context, context.getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    /***
     * Checks all form fields for valid entires
     * @return
     */
    public boolean validate(){
        boolean cancel = false;
        try {
            String name = etName.getText().toString();
            String address = etAddress.getText().toString();
            String pincode = etPincode.getText().toString();
            String primaryPhone = etPrimaryPhone.getText().toString();
            String email = etEmail.getText().toString();
            String country = spCountry.getSelectedItem().toString();
            String state = spState.getSelectedItem().toString();
            String location = spLocation.getSelectedItem().toString();
            String subLocation = etSublocation.getText().toString();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();
            String orgType = spOrgType.getSelectedItem().toString();
            String category = spCategory.getSelectedItem().toString();
            String role = etRole.getText().toString();

            if (name.isEmpty()) {
                etName.setError(context.getString(R.string.ps_error_required));
                cancel = true;
            }
            if (address.isEmpty()) {
                etAddress.setError(context.getString(R.string.ps_error_required));
                cancel = true;
            }
            if (pincode.isEmpty()) {
                etPincode.setError(context.getString(R.string.ps_error_required));
                cancel = true;
            }
            if (primaryPhone.isEmpty()) {
                etPrimaryPhone.setError(context.getString(R.string.ps_error_required));
                cancel = true;
            }
            if (email.isEmpty()) {
                etEmail.setError(context.getString(R.string.ps_error_required));
                cancel = true;
            }else if(!isValidEmail(email)){
                etEmail.setError(context.getString(R.string.ps_error_invalid_email));
                cancel = true;
            }
            if (country.isEmpty() || country.toLowerCase().equals("country")) {
                TextView error = (TextView) spCountry.getSelectedView();
                error.setError(context.getString(R.string.ps_error_required));
                cancel = true;
            }
            if (state.isEmpty() || state.toLowerCase().equals("state")) {
                TextView error = (TextView) spState.getSelectedView();
                error.setError(context.getString(R.string.ps_error_required));
                cancel = true;
            }
            if (location.isEmpty() || location.toLowerCase().equals("location")) {
                TextView error = (TextView) spLocation.getSelectedView();
                error.setError(context.getString(R.string.ps_error_required));
                cancel = true;
            }
            if (subLocation.isEmpty()) {
                etSublocation.setError(context.getString(R.string.ps_error_required));
                cancel = true;
            }
            if (password.isEmpty()) {
                etPassword.setError(context.getString(R.string.ps_error_required));
                cancel = true;
            } else if (!password.equals(confirmPassword)) {
                etConfirmPassword.setError(context.getString(R.string.ps_error_password_mismatch));
                cancel = true;
            }
            if (orgType.isEmpty() || orgType.toLowerCase().equals("organisation")) {
                TextView error = (TextView) spOrgType.getSelectedView();
                error.setError(context.getString(R.string.ps_error_required));
                cancel = true;
            }
            if (category.isEmpty() || category.toLowerCase().equals("category")) {
                TextView error = (TextView) spCategory.getSelectedView();
                error.setError(context.getString(R.string.ps_error_required));
                cancel = true;
            }
            if (role.isEmpty()) {
                etRole.setError(context.getString(R.string.ps_error_required));
                cancel = true;
            }
        }catch (Exception e){
            Toast.makeText(context, context.getString(R.string.response_error), Toast.LENGTH_SHORT).show();
            cancel = true;
        }
        return cancel;
    }

    //Loaders
    private void loadCategories(){
        SqlHelper sqlHelper = new SqlHelper(context, PremiumSignupFragment.this);
        sqlHelper.setExecutePath("getunion.php");
        sqlHelper.setMethod("GET");
        sqlHelper.setActionString("categories");
        ContentValues params = new ContentValues();
        sqlHelper.setParams(params);
        sqlHelper.executeUrl(true);
    }

    private void loadUserDetails(){
        SqlHelper sqlHelper = new SqlHelper(context, PremiumSignupFragment.this);
        sqlHelper.setExecutePath("getprofilepre.php");
        sqlHelper.setMethod("POST");
        sqlHelper.setActionString("user");
        ContentValues params = new ContentValues();
        params.put("id", userId);
        sqlHelper.setParams(params);
        sqlHelper.executeUrl(true);
    }

    private void saveInformation(){
        SqlHelper sqlHelper = new SqlHelper(context, PremiumSignupFragment.this);
        sqlHelper.setExecutePath("postprofiledata.php");
        sqlHelper.setMethod("POST");
        sqlHelper.setActionString("save");
        ContentValues params = new ContentValues();
        params.put("userId", userId);
        params.put("name", etName.getText().toString());
        params.put("email", etEmail.getText().toString());
        params.put("password", etPassword.getText().toString());
        params.put("phone", etPrimaryPhone.getText().toString());
        params.put("category", spCategory.getSelectedItem().toString());
        params.put("role", etRole.getText().toString());
        params.put("country", "IN");
        params.put("type", spOrgType.getSelectedItem().toString());
        String address = etAddress.getText().toString();
        address = address.replace(" ", "&#32;");
        params.put("address", address);
        params.put("state", spState.getSelectedItem().toString());
        params.put("location", spLocation.getSelectedItem().toString());
        params.put("sublocation", etSublocation.getText().toString());
        params.put("pincode", etPincode.getText().toString());
        params.put("union", etUnion.getText().toString());
        params.put("whatsapp", etWhatsapp.getText().toString());
        params.put("image", imageChanged ? StringHelper.imageToString(bitmap) : "1");
        params.put("website", etWebsite.getText().toString());
        params.put("phone2", etSecondaryContacts.getText().toString());
        params.put("skills", etSkills.getText().toString());
        params.put("privatetag", switchPrivacy.isChecked() ? "1" : "0");
        params.put("prospectTag", cbProspect.isChecked() ? "1" : "0");
        params.put("employId", employeeId);
        params.put("mob", "1");
        sqlHelper.setParams(params);
        sqlHelper.executeUrl(true);
    }

    private void submitInformation(){
        SqlHelper sqlHelper = new SqlHelper(context, PremiumSignupFragment.this);
        sqlHelper.setExecutePath("postprofiledata.php");
        sqlHelper.setMethod("POST");
        sqlHelper.setActionString("submit");
        ContentValues params = new ContentValues();
        params.put("id", userId);
        params.put("name", etName.getText().toString());
        params.put("email", etEmail.getText().toString());
        params.put("password", etPassword.getText().toString());
        params.put("phone", etPrimaryPhone.getText().toString());
        params.put("category", spCategory.getSelectedItem().toString());
        params.put("role", etRole.getText().toString());
        params.put("country", "IN");
        params.put("type", spOrgType.getSelectedItem().toString());
        String address = etAddress.getText().toString();
        address = address.replace(" ", "&#32;");
        params.put("address", address);
        params.put("state", spState.getSelectedItem().toString());
        params.put("location", spLocation.getSelectedItem().toString());
        params.put("sublocation", etSublocation.getText().toString());
        params.put("pincode", etPincode.getText().toString());
        params.put("union", etUnion.getText().toString());
        params.put("whatsapp", etWhatsapp.getText().toString());
        params.put("image", imageChanged ? StringHelper.imageToString(bitmap) : "1");
        params.put("website", etWebsite.getText().toString());
        params.put("phone2", etSecondaryContacts.getText().toString());
        params.put("skills", etSkills.getText().toString());
        params.put("privatetag", switchPrivacy.isChecked() ? "1" : "0");
        params.put("prospectTag", cbProspect.isChecked() ? "1" : "0");
        params.put("employId", employeeId);
        params.put("mob", "1");
        sqlHelper.setParams(params);
        sqlHelper.executeUrl(true);
    }

    //private functions
    private void buildCategories(JSONArray jsonArray){
        try{
            categoryList = new ArrayList<>();
            ArrayList<String> categoryStrings = new ArrayList<>();
            categoryStrings.add("Category");
            for(int i = 1; i < jsonArray.length(); i++){
                CategoryModel categoryModel = new ModelHelper().buildCategoryModel(jsonArray.getJSONObject(i));
                categoryList.add(categoryModel);
                categoryStrings.add(categoryModel.getName());
            }
            ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item,categoryStrings);
            adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCategory.setAdapter(adapterCategories);
            categoryLoaded = true;
            setSelectedCategory();

        }catch (Exception e){

        }
    }

    private void populateUserDetails(JSONArray jsonArray){
        try{
            serviceProviderModel = new ModelHelper().buildServiceProviderModel(jsonArray.getJSONObject(1), "premiumsignup");
            etName.setText(serviceProviderModel.getName());
            String address = serviceProviderModel.getAddress();
            address = address.replace("&#32;", " ");
            if(!address.equals("null") || !address.isEmpty())
                etAddress.setText(address);
            if(!(serviceProviderModel.getPincode().equals("null") || serviceProviderModel.getPincode().isEmpty()))
                etPincode.setText(serviceProviderModel.getPincode());
            if(!(serviceProviderModel.getEmail().equals("null") || serviceProviderModel.getEmail().isEmpty()))
                etEmail.setText(serviceProviderModel.getEmail());
            if(!(serviceProviderModel.getPhone().equals("null") || serviceProviderModel.getPhone().isEmpty()))
                etPrimaryPhone.setText(serviceProviderModel.getPhone());
            if(!(serviceProviderModel.getPhone2().equals("null") || serviceProviderModel.getPhone2().isEmpty()))
                etSecondaryContacts.setText(serviceProviderModel.getPhone2());
            if(!(serviceProviderModel.getSkills().equals("null") || serviceProviderModel.getSkills().isEmpty()))
                etSkills.setText(serviceProviderModel.getSkills());
            if(!(serviceProviderModel.getPassword().equals("null") || !serviceProviderModel.getPassword().isEmpty()))
                etPassword.setText(serviceProviderModel.getPassword());
            if(!(serviceProviderModel.getSublocation().equals("null") || !serviceProviderModel.getSublocation().isEmpty()))
                etSublocation.setText(serviceProviderModel.getSublocation());
            if(!(serviceProviderModel.getRole().equals("null") || !serviceProviderModel.getRole().isEmpty()))
                etRole.setText(serviceProviderModel.getRole());
            if(!(serviceProviderModel.getWebsite().equals("null") || !serviceProviderModel.getWebsite().isEmpty()))
                etWebsite.setText(serviceProviderModel.getWebsite());
            if(!(serviceProviderModel.getUnionName().equals("null") || !serviceProviderModel.getUnionName().isEmpty()))
                etUnion.setText(serviceProviderModel.getUnionName());
            cbProspect.setChecked(serviceProviderModel.isProspect());
            switchPrivacy.setChecked(serviceProviderModel.isPrivate());
            imageUrl = serviceProviderModel.getImage();
            //Set Country Spinner
            for(int i = 0; i < spCountry.getAdapter().getCount(); i++){
                if(countryList.get(i).toLowerCase().equals("india")){
                    spCountry.setSelection(i);
                    break;
                }
            }
            //Set Location Spinner
            for(int i = 0; i < spLocation.getAdapter().getCount(); i++){
                if(locationList.get(i).toLowerCase().equals(serviceProviderModel.getLocation().toLowerCase())){
                    spLocation.setSelection(i);
                    break;
                }
            }
            //Set State Spinner
            for(int i = 0; i < spState.getAdapter().getCount(); i++){
                if(stateList.get(i).toLowerCase().equals(serviceProviderModel.getState().toLowerCase())){
                    spState.setSelection(i);
                    break;
                }
            }
            //Set Org Type Spinner
            if(serviceProviderModel.getOrgType().toLowerCase().contains("private")){
                spOrgType.setSelection(1);
            }else if(!(serviceProviderModel.getOrgType().toLowerCase().isEmpty() || serviceProviderModel.getOrgType().toLowerCase().equals("null"))){
                spOrgType.setSelection(2);
            }
            //Set Category Spinner
            if(categoryLoaded)
                setSelectedCategory();
        }catch (Exception e){
            Toast.makeText(context, context.getString(R.string.unexpected), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidEmail(String email){
        boolean isValid = false;
        if(email.contains("@"))
            isValid = true;
        return isValid;
    }

    private void setSelectedCategory(){
        //Set Category Spinner
        if(serviceProviderModel != null) {
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).getName().toLowerCase().equals(serviceProviderModel.getCategory().toLowerCase())) {
                    spCategory.setSelection(i + 1);
                    break;
                }
            }
        }
    }
}
