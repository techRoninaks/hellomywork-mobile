package com.roninaks.hellomywork.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.helpers.EmailHelper;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.helpers.StringHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.StringTokenizer;

import static android.util.Log.e;
import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CareersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CareersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CareersFragment extends Fragment implements SqlDelegate {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    Bitmap bitmap;
    private EditText careerNameET, careerAddressET, careerPrimaryPhoneET, careerEmailET, careerSecondaryPhoneET, careerWhatsappTwoET;
    private Button careerUploadResumeBTN, careerSubmitBTN;
    private ImageView careersBackBTN;
    Context context = getContext();
    private static final int READ_REQUEST_CODE = 42;
    boolean resumeUploaded = false;
    private String displayName = null;
    private String resume;


    private OnFragmentInteractionListener mListener;

    public CareersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CareersFragment.
     */

    public static CareersFragment newInstance(String param1, String param2) {
        CareersFragment fragment = new CareersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_careers, container, false);
        careerNameET = view.findViewById(R.id.careerNameEditText);
        careerAddressET = view.findViewById(R.id.careerAddressEditText);
        careerPrimaryPhoneET = view.findViewById(R.id.careerPrimaryPhoneET);
        careerEmailET = view.findViewById(R.id.careerEmailEditText);
        careerSecondaryPhoneET = view.findViewById(R.id.careerSecondaryPhoneET);
        careerWhatsappTwoET = view.findViewById(R.id.careerWhatsappTwoET);
        careerUploadResumeBTN = view.findViewById(R.id.careerUploadBTN);
        careerSubmitBTN = view.findViewById(R.id.careerSubmitBTN);
        careersBackBTN = view.findViewById(R.id.imgBack);


        careerUploadResumeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadResume();
            }
        });

        careerSubmitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCareerForms();
            }
        });

        careersBackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).onBackPressed();
            }
        });
        return view;
    }

    private void uploadResume() {
        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.

//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
////
////        // Filter to only show results that can be "opened", such as a
////        // file (as opposed to a list of contacts or timezones)
////        intent.setType("application/pdf");
////        intent.addCategory(Intent.CATEGORY_OPENABLE);
////        startActivityForResult(intent, READ_REQUEST_CODE);

        String[] mimeTypes = {"application/pdf"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, READ_REQUEST_CODE);
    }


    private void validateCareerForms() {
        String name = careerNameET.getText().toString();
        String address = careerAddressET.getText().toString();
        String primaryPhone = careerPrimaryPhoneET.getText().toString();
        String email = careerEmailET.getText().toString();
        String secondaryPhone = careerSecondaryPhoneET.getText().toString();
        String whatsappTwo = careerWhatsappTwoET.getText().toString();

        if(name.isEmpty()){
            careerNameET.setError(getString(R.string.required_field));
            Toast.makeText(context, "Name is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(address.trim().isEmpty()){
            careerAddressET.setError(getString(R.string.required_field));
            Toast.makeText(context, "Address is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(primaryPhone.isEmpty()){
            careerPrimaryPhoneET.setError(getString(R.string.required_field));
            Toast.makeText(context, "Primary phone number is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!(primaryPhone.length() == 10)){
            careerPrimaryPhoneET.setError(getString(R.string.invalid_phone));
            Toast.makeText(context, "Primary phone does not contain 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }
        if(email.isEmpty()){
            careerEmailET.setError(getString(R.string.required_field));
            Toast.makeText(context, "Email is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!email.contains("@") && !email.contains(".")){
            careerEmailET.setError(getString(R.string.invalid_cred));
            Toast.makeText(context, "Email is not in proper format", Toast.LENGTH_SHORT).show();
            return;
        }
        if(secondaryPhone.isEmpty()){
            careerSecondaryPhoneET.setError(getString(R.string.required_field));
            Toast.makeText(context, "Secondary phone number is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(secondaryPhone.length() == 10){
            careerSecondaryPhoneET.setError(getString(R.string.invalid_phone));
            Toast.makeText(context, "Secondary phone does not contain 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }
        if(whatsappTwo.isEmpty()){
            careerWhatsappTwoET.setError(getString(R.string.required_field));
            Toast.makeText(context, "Whatsapp number is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(whatsappTwo.length() == 10){
            careerWhatsappTwoET.setError(getString(R.string.invalid_phone));
            Toast.makeText(context, "requires 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!resumeUploaded){
            Toast.makeText(context, "No resume selected", Toast.LENGTH_SHORT).show();
            return;
        }
        upload(resume,displayName);
        int zero = 0;
        SqlHelper sqlHelper = new SqlHelper(context, CareersFragment.this);
        sqlHelper.setExecutePath("careers.php");
        sqlHelper.setActionString("careers");
        sqlHelper.setMethod("POST");
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("address", address);
        contentValues.put("primaryphone", primaryPhone);
        contentValues.put("whatsapp", whatsappTwo);
        contentValues.put("email", email);
        contentValues.put("secondaryphone", secondaryPhone);
        contentValues.put("contacted", zero);
        sqlHelper.setParams(contentValues);
        sqlHelper.executeUrl(true);
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResponse(SqlHelper sqlHelper) {
        try {
            switch (sqlHelper.getActionString()){
                case "careers": {
//                  JSONObject jsonObject = sqlHelper.getJSONResponse().getJSONObject("data");
                    String response = sqlHelper.getStringResponse();
                    if (response.equals("successful")) {
                        Toast.makeText(context, "Inquiry has been saved", Toast.LENGTH_SHORT).show();
                        careerNameET.setText("");
                        careerNameET.setText("");
                        careerAddressET.setText("");
                        careerPrimaryPhoneET.setText("");
                        careerEmailET.setText("");
                        careerSecondaryPhoneET.setText("");
                        careerWhatsappTwoET.setText("");


                    } else if (response.equals("error")) {
//                recyclerView.setVisibility(View.GONE);
//                llContainerPlaceholder.setVisibility(View.VISIBLE);
//            }else if(response.equals(context.getString(R.string.unexpected))){
//                Toast.makeText(context, context.getString(R.string.unexpected), Toast.LENGTH_SHORT).show();
                    }
                }break;
                case "upload":{
                    String response = sqlHelper.getStringResponse();
                    if (!(response.equals("null"))) {
//                        Toast.makeText(context, "Upload completed", Toast.LENGTH_SHORT).show();
                        careerUploadResumeBTN.setBackgroundResource(R.drawable.career_button_color_radius);
                        careerUploadResumeBTN.setTextColor(getResources().getColor(R.color.colorTextWhitePrimary));
                        careerSubmitBTN.setBackgroundResource(R.drawable.card_background_shape);
                        careerSubmitBTN.setTextColor(getResources().getColor(R.color.colorTextBlackPrimary));
                    }
                    else {
                        Toast.makeText(context, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        File dir = context.getExternalFilesDir("TempFolder");
                        deleteRecursive(dir);
                    }catch (Exception e){}

                }break;
            }
        } catch (Exception e) {
            EmailHelper emailHelper = new EmailHelper(context, EmailHelper.TECH_SUPPORT, "Error: CareersFragment", e.getMessage() + "\n" + StringHelper.convertStackTrace(e));
            emailHelper.sendEmail();
        }

    }

    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK&& resultData != null) {
            if (hasPermission(requestCode,Manifest.permission.READ_EXTERNAL_STORAGE, context)){
                Uri uri = resultData.getData();
                String uriString = uri.toString();
                File myFile = new File(uri.getPath().toString());

                String filRealpath = getFileFromContentUri(uri);
                File myFilenew = new File(filRealpath);
                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        }
                    } finally {
                        cursor.close();
                    }
                } else if (uriString.startsWith("file://")) {
                    displayName = myFile.getName();
                }

                resume = encodeFileToBase64Binary(myFilenew);
                resumeUploaded= true;
            }
        }
    }


    private String encodeFileToBase64Binary(File yourFile) {
        int size = (int) yourFile.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(yourFile));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String encoded = Base64.encodeToString(bytes,Base64.NO_WRAP);
        return encoded;
    }

    private void upload(String encoded, String filename){
        try {
            SqlHelper sqlHelper = new SqlHelper(context, CareersFragment.this);
            sqlHelper.setExecutePath("loadresume.php");
            sqlHelper.setActionString("upload");
            ContentValues contentValues = new ContentValues();
            contentValues.put("image_bitmap",encoded);
            contentValues.put("image_name",filename);
            //contentValues.put("userPassword", password);
            sqlHelper.setParams(contentValues);
            sqlHelper.setMethod(getString(R.string.method_post));
            sqlHelper.executeUrl(true);
        }catch (Exception e){
            EmailHelper emailHelper = new EmailHelper(context, EmailHelper.TECH_SUPPORT, "Error: CareersFragment", e.getMessage() + "\n" + StringHelper.convertStackTrace(e));
            emailHelper.sendEmail();
        }
    }


    public static boolean hasPermission(int PERMISSION_REQUEST, String permission, Context context) {
        if (ContextCompat.checkSelfPermission(context,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    permission) &&
                    ContextCompat.checkSelfPermission(context,
                            permission)
                            != PackageManager.PERMISSION_GRANTED) {

                return false;

            } else {

                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{permission},
                        PERMISSION_REQUEST);

            }

            return false;
        } else {
            return true;


        }
    }


    private String getFileFromContentUri(Uri uri) {
        //This will be the file we will use (the one that will be copied)
        File file = null;
        try {
            //Create a temporary folder where the copy will be saved to
            File temp_folder = context.getExternalFilesDir("TempFolder");

            //Use ContentResolver to get the name of the original name
            //Create a cursor and pass the Uri to it
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            //Check that the cursor is not null
            assert cursor != null;
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            //Get the file name
            String filename = cursor.getString(nameIndex);
            //Close the cursor
            cursor.close();

            //open a InputStream by passing it the Uri
            //We have to do this in a try/catch
            InputStream is = null;
            try {
                is = context.getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //We now have a folder and a file name, now we can create a file
            file = new File(temp_folder + "/" + filename);

            //We can now use a BufferedInputStream to pass the InputStream we opened above to it
            BufferedInputStream bis = new BufferedInputStream(is);
            //We will write the byte data to the FileOutputStream, but we first have to create it
            FileOutputStream fos = new FileOutputStream(file);

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            //Below we will read all the byte data and write it to the FileOutputStream
            while ((count = bis.read(data)) != -1) {
                total += count;
                fos.write(data, 0, count);
            }
            //The FileOutputStream is done and the file is created and we can clean and close it
            fos.flush();
            fos.close();

        } catch (IOException e) {
            Log.e("IOException = ", String.valueOf(e));
        }

        //Finally we can pass the path of the file we have copied
        return file.getAbsolutePath();


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
}
