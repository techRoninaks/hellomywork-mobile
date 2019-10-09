package com.roninaks.hellomywork.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.fragments.PostAdFragment;
import com.roninaks.hellomywork.fragments.PremiumSignupFragment;
import com.roninaks.hellomywork.fragments.ProfileFragment;
import com.roninaks.hellomywork.helpers.PermissionsHelper;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.InputStream;

//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.roninaks.hellomywork.GlideApp;

public class ProfileImage extends Activity implements SqlDelegate {

    ImageView imageView;
    Button btnGallery, btnCamera, btnConfirm, btnDiscard;
    LinearLayout llMainButton, llConfirmButton;
    Uri imageUri;
    String imagePath, image, uploadUrl, fragment;
    Bitmap bitmap;
    Boolean imageChanged = false;

    RequestOptions requestOptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile_image);
            requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.profile_default);
            requestOptions.error(R.drawable.profile_default);
            uploadUrl = ProfileImage.this.getString(R.string.master_url) + "upload.php";
            imageView = findViewById(R.id.imageView);
            btnCamera = findViewById(R.id.btn_Camera);
            btnGallery = findViewById(R.id.btn_Gallery);
            btnConfirm = findViewById(R.id.btn_Confirm);
            btnDiscard = findViewById(R.id.btn_Discard);
            Button buttonExit = findViewById(R.id.btn_exit);
            llMainButton = findViewById(R.id.containerMainButtons);
            llConfirmButton = findViewById(R.id.containerConfirmButtons);
            final Intent intent = getIntent();
            final Bundle bundle = intent.getBundleExtra("bundle");
            if(bundle != null) {
                image = bundle.getString("image").contains("assets/img/profile/userimage/") ? getString(R.string.master_url).concat(bundle.getString("image")) : bundle.getString("image");
                fragment = bundle.getString("fragment");
                switch (fragment){
                    case "premiumsignup":{
                        bitmap = PremiumSignupFragment.bitmap;
                        break;
                    }
                    case "profile":{
                        bitmap = ProfileFragment.bitmap;
                        break;
                    }
                    case "postad":{
                        bitmap = PostAdFragment.bitmap;
                        break;
                    }
                }
            }
            if(bitmap == null) {
                Glide.with(this)
                        .asBitmap()
                        .load(Uri.parse(image))
                        .into(imageView);
            }else{
                Glide.with(this)
                        .asBitmap()
                        .load(bitmap)
                        .into(imageView);
            }
            buttonExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(imageChanged){
                        switch (fragment){
                            case "premiumsignup":{
                                PremiumSignupFragment.imageUrl = bundle!= null ? bundle.getString("image") : "";
                                break;
                            }
                            case "profile":{
                                ProfileFragment.imageUrl = bundle!= null ? bundle.getString("image") : "";
                                break;
                            }
                            case "postad":{
                                PostAdFragment.imageUrl = bundle!= null ? bundle.getString("image") : "";
                                break;
                            }
                        }
                    }
                    finish();
                }
            });
            btnGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dispatchGetPictureIntent();
                }
            });
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            switch (which) {
//                                case DialogInterface.BUTTON_POSITIVE: {
////                                SqlHelper sqlHelper = new SqlHelper(ProfileImage.this, ProfileImage.this);
////                                sqlHelper.setActionString("upload_profile_image");
////                                sqlHelper.setExecutePath("upload.php");
////                                sqlHelper.setMethod("POST");
////                                sqlHelper.setUploadFilePath(imagePath);
////                                ArrayList<NameValuePair> params = new ArrayList<>();
////                                params.add(new BasicNameValuePair("u_id", MainActivity.currentUserModel.getUserId()));
////                                sqlHelper.setParams(params);
////                                sqlHelper.uploadFile(true);
////                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadUrl, new Response.Listener<String>() {
////                                        @Override
////                                        public void onResponse(String response) {
////                                            Toast.makeText(ProfileImage.this, "Your profile image has been saved", Toast.LENGTH_SHORT).show();
////                                            Bundle bundle = new ModelHelper(ProfileImage.this).buildUserModelBundle(MainActivity.currentUserModel, "ProfileFragment");
////                                            bundle.putString("return_path", "ProfileFragment");
////                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
////                                            intent.putExtra("bundle", bundle);
////                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                            startActivity(intent);
////                                        }
////                                    }, new Response.ErrorListener() {
////                                        @Override
////                                        public void onErrorResponse(VolleyError error) {
////                                            Toast.makeText(ProfileImage.this, ProfileImage.this.getString(R.string.unexpected), Toast.LENGTH_SHORT).show();
////                                        }
////                                    }) {
////                                        @Override
////                                        protected Map<String, String> getParams() throws AuthFailureError {
////                                            String imageData = imageToString(bitmap);
////                                            Map<String, String> params = new HashMap<>();
////                                            params.put("image", imageData);
////                                            params.put("u_id", MainActivity.currentUserModel.getUserId());
////                                            return params;
////                                        }
////                                    };
////
////                                    RequestQueue requestQueue = Volley.newRequestQueue(ProfileImage.this);
////                                    requestQueue.add(stringRequest);
//                                    switch (fragment){
//                                        case "premiumsignup":{
//                                            PremiumSignupFragment.imageChanged = true;
//                                            PremiumSignupFragment.bitmap = bitmap;
//                                            PremiumSignupFragment.imageUrl = imageUri.toString();
//                                            break;
//                                        }
//                                        case "profile":{
//                                            ProfileFragment.imageChanged = true;
//                                            ProfileFragment.bitmap = bitmap;
//                                            ProfileFragment.imageUrl = imageUri.toString();
//                                            break;
//                                        }
//                                        case "postad":{
//                                            PostAdFragment.imageChanged = true;
//                                            PostAdFragment.bitmap = bitmap;
//                                            PostAdFragment.imageUrl = imageUri.toString();
//                                            break;
//                                        }
//                                    }
//                                    finish();
//                                    break;
//                                }
//                                case DialogInterface.BUTTON_NEGATIVE:
//                                    //No button clicked
//                                    break;
//                            }
//                        }
//                    };
//                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileImage.this);
//                    builder.setTitle("Upload Image");
//                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
//                            .setNegativeButton("No", dialogClickListener).show();
                    switch (fragment){
                        case "premiumsignup":{
                            PremiumSignupFragment.imageChanged = true;
                            PremiumSignupFragment.bitmap = bitmap;
                            PremiumSignupFragment.imageUrl = imageUri.toString();
                            break;
                        }
                        case "profile":{
                            ProfileFragment.imageChanged = true;
                            ProfileFragment.bitmap = bitmap;
                            ProfileFragment.imageUrl = imageUri.toString();
                            break;
                        }
                        case "postad":{
                            PostAdFragment.imageChanged = true;
                            PostAdFragment.bitmap = bitmap;
                            PostAdFragment.imageUrl = imageUri.toString();
                            break;
                        }
                    }
                    finish();
                }
            });
            btnDiscard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    Glide.with(ProfileImage.this)
                                            .setDefaultRequestOptions(requestOptions)
                                            .asBitmap()
                                            .load(image)
                                            .into(imageView);
                                    llConfirmButton.setVisibility(View.GONE);
                                    llMainButton.setVisibility(View.VISIBLE);
                                    imagePath = "";
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileImage.this);
                    builder.setTitle("Discard Image");
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            });
            performChecks();
        }catch (Exception e){
//            EmailHelper emailHelper = new EmailHelper(ProfileImage.this, EmailHelper.TECH_SUPPORT, "Error: ProfileImage", StringHelper.convertStackTrace(e));
//            emailHelper.sendEmail();
            Toast.makeText(ProfileImage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionsHelper.REQUEST_IMAGE_CAPTURE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(ProfileImage.this, getString(R.string.permission_camera_denied), Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case PermissionsHelper.REQUEST_READ_EXTERNAL_STORAGE:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                }
                return;
            }
            case PermissionsHelper.REQUEST_WRITE_EXTERNAL_STORAGE:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == PermissionsHelper.REQUEST_IMAGE_CAPTURE) {
                if (resultCode == RESULT_OK) {
                    if(fragment.equals("premiumsignup")){
                        CropImage.activity(imageUri)
                                .start(this);
                    }else {
                        Glide.with(this)
                                .setDefaultRequestOptions(requestOptions)
                                .asBitmap()
                                .load(getRealPathFromURI(imageUri))
                                .into(imageView);
                        llMainButton.setVisibility(View.GONE);
                        llConfirmButton.setVisibility(View.VISIBLE);
                        PremiumSignupFragment.imageUrl = imageUri.toString();
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        bitmap = BitmapFactory.decodeStream(inputStream);
                    }
                } else {
                    Toast.makeText(ProfileImage.this, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == PermissionsHelper.REQUEST_READ_EXTERNAL_STORAGE || requestCode == PermissionsHelper.REQUEST_WRITE_EXTERNAL_STORAGE && resultCode == RESULT_OK) {
                if (resultCode == RESULT_OK) {
                    Uri fullPhotoUri = data.getData();
                    imageUri = fullPhotoUri;
                    if(fragment.equals("premiumsignup")){
                        CropImage.activity(imageUri)
                                .start(this);
                    }else {
                        Glide.with(this)
                                .setDefaultRequestOptions(requestOptions)
                                .asBitmap()
                                .load(fullPhotoUri)
                                .into(imageView);
                        llMainButton.setVisibility(View.GONE);
                        llConfirmButton.setVisibility(View.VISIBLE);
                        PremiumSignupFragment.imageUrl = fullPhotoUri.toString();
                        InputStream inputStream = getContentResolver().openInputStream(fullPhotoUri);
                        bitmap = BitmapFactory.decodeStream(inputStream);
                    }
                } else {
                    Toast.makeText(ProfileImage.this, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
            } else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    imageUri = result.getUri();
                    Glide.with(this)
                            .setDefaultRequestOptions(requestOptions)
                            .asBitmap()
                            .load(imageUri)
                            .into(imageView);
                    llMainButton.setVisibility(View.GONE);
                    llConfirmButton.setVisibility(View.VISIBLE);
                    PremiumSignupFragment.imageUrl = imageUri.toString();
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        }catch (Exception e){
//            EmailHelper emailHelper = new EmailHelper(ProfileImage.this, EmailHelper.TECH_SUPPORT, "Error: ProfileImage", StringHelper.convertStackTrace(e));
//            emailHelper.sendEmail();
        }
    }




    /***
     * Performs necessary hardware checks
     */
    private void performChecks(){
        PackageManager packageManager = getPackageManager();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            btnCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(new PermissionsHelper(ProfileImage.this).requestPermissions(PermissionsHelper.REQUEST_IMAGE_CAPTURE))
                        dispatchTakePictureIntent();
                }
            });
            btnCamera.setVisibility(View.VISIBLE);
        }
    }


    /***
     * Pass intent to start Camera app
     */
    private void dispatchTakePictureIntent() {
        try {
            if(new PermissionsHelper(ProfileImage.this).requestPermissions(PermissionsHelper.REQUEST_WRITE_EXTERNAL_STORAGE)){
                ContentValues values = new ContentValues();
                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, PermissionsHelper.REQUEST_IMAGE_CAPTURE);
                }
            }

        }catch (Exception e){
//            EmailHelper emailHelper = new EmailHelper(ProfileImage.this, EmailHelper.TECH_SUPPORT, "Error: ProfileImage", StringHelper.convertStackTrace(e));
//            emailHelper.sendEmail();
        }
    }

    /***
     * Pass intent to start Gallery app
     */
    private void dispatchGetPictureIntent() {
        try {
            if(new PermissionsHelper(ProfileImage.this).requestPermissions(PermissionsHelper.REQUEST_WRITE_EXTERNAL_STORAGE)){
                Intent getPictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getPictureIntent.setType("image/*");
                getPictureIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                getPictureIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                if (getPictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(Intent.createChooser(getPictureIntent, "Select Picture"), PermissionsHelper.REQUEST_WRITE_EXTERNAL_STORAGE);
                }
            }

        }catch (Exception e){
//            EmailHelper emailHelper = new EmailHelper(ProfileImage.this, EmailHelper.TECH_SUPPORT, "Error: ProfileImage", StringHelper.convertStackTrace(e));
//            emailHelper.sendEmail();
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onResponse(SqlHelper sqlHelper) {
        try{

        }catch (Exception e){
//            EmailHelper emailHelper = new EmailHelper(ProfileImage.this, EmailHelper.TECH_SUPPORT, "Error: ProfileImage", StringHelper.convertStackTrace(e));
//            emailHelper.sendEmail();
        }
    }
}
