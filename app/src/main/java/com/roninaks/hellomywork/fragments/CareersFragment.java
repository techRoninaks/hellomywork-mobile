package com.roninaks.hellomywork.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.helpers.EmailHelper;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.helpers.StringHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CareersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CareersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CareersFragment extends Fragment implements SqlDelegate {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Bitmap bitmap;
    private EditText careerNameET, careerAddressET, careerPrimaryPhoneET, careerWhatsappOneET, careerEmailET, careerSecondaryPhoneET, careerWhatsappTwoET;
    private Button careerUploadResumeBTN, careerSubmitBTN;
    Context context = getContext();
    private static final int READ_REQUEST_CODE = 42;
    boolean resumeUploaded = false;


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
    // TODO: Rename and change types and number of parameters
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
        careerWhatsappOneET = view.findViewById(R.id.careerWhatsappOneET);
        careerEmailET = view.findViewById(R.id.careerEmailEditText);
        careerSecondaryPhoneET = view.findViewById(R.id.careerSecondaryPhoneET);
        careerWhatsappTwoET = view.findViewById(R.id.careerWhatsappTwoET);
        careerUploadResumeBTN = view.findViewById(R.id.careerUploadBTN);
        careerSubmitBTN = view.findViewById(R.id.careerSubmitBTN);


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
        return view;
    }

    private void uploadResume() {
        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.setType("images/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, READ_REQUEST_CODE);

    }


    private void validateCareerForms() {
        String name = careerNameET.getText().toString();
        String address = careerAddressET.getText().toString();
        String primaryPhone = careerPrimaryPhoneET.getText().toString();
        //String whatsappOne = careerWhatsappOneET.getText().toString();
        String email = careerEmailET.getText().toString();
        String secondaryPhone = careerSecondaryPhoneET.getText().toString();
        String whatsappTwo = careerWhatsappTwoET.getText().toString();

        if(name.isEmpty()){
            Toast.makeText(context, "Name is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(address.isEmpty()){
            Toast.makeText(context, "Address is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(primaryPhone.isEmpty()){
            Toast.makeText(context, "Primary phone number is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!(primaryPhone.length() == 10)){
            Toast.makeText(context, "Primary phone does not contain 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }
        if(whatsappTwo.isEmpty()){
            Toast.makeText(context, "Whatsapp number is empty", Toast.LENGTH_SHORT).show();
            return;
        }
//        if(!(whatsappTwo.length() == 10)){
//            Toast.makeText(context, "Whatsapp does not contain 10 digits", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(secondaryPhone.isEmpty()){
//            Toast.makeText(context, "Secondary phone number is empty", Toast.LENGTH_SHORT).show();
//        }
//        if(secondaryPhone.length() == 10){
//            Toast.makeText(context, "Secondary phone does not contain 10 digits", Toast.LENGTH_SHORT).show();
//        }
//        if(whatsappTwo.isEmpty()){
//            Toast.makeText(context, "Whatsapp Two number is empty", Toast.LENGTH_SHORT).show();
//        }
        if(whatsappTwo.length() == 10){
            Toast.makeText(context, "requires 10 digits", Toast.LENGTH_SHORT).show();
        }
        if(email.isEmpty()){
            Toast.makeText(context, "Email is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!email.contains("@") && !email.contains(".")){
            Toast.makeText(context, "Email is not in proper format", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!resumeUploaded){
            Toast.makeText(context, "No resume selected", Toast.LENGTH_SHORT).show();
            return;
        }
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
        sqlHelper.executeUrl(false);
    }

    // TODO: Rename method, update argument and hook method into UI event
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
                    if (response.equals("success")) {
                        Toast.makeText(context, "Inquiry has been saved", Toast.LENGTH_SHORT).show();
                        careerNameET.setText("");
                        careerNameET.setText("");
                        careerAddressET.setText("");
                        careerPrimaryPhoneET.setText("");
                        //careerWhatsappOneET.setText("");
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
                        Toast.makeText(context, "Upload completed", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                    }
                }break;
            }
        } catch (Exception e) {
            EmailHelper emailHelper = new EmailHelper(context, EmailHelper.TECH_SUPPORT, "Error: CareersFragment", e.getMessage() + "\n" + StringHelper.convertStackTrace(e));
            emailHelper.sendEmail();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            StringTokenizer tokens;
            String first,file_1;
            if (resultData != null) {
                uri = resultData.getData();
//                Log.i(TAG, "Uri: " + uri.toString());
                //String imgUrl = uri.toString();
                //File file = new File(uri.getPath().toString());


                //working image
                InputStream inputStream = null;
                try {
                    inputStream = context.getContentResolver().openInputStream(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmap = BitmapFactory.decodeStream(inputStream);
               // String encSt = encodeFileToBase64Binary(file);
                String imgencoded = encodeTobase64(bitmap);
                upload(imgencoded,"resume_id");
//                String uploadedFileName = file.getName().toString();
//                tokens = new StringTokenizer(uploadedFileName, ":");
//                first = tokens.nextToken();
//                file_1 = tokens.nextToken().trim();
//                txt_file_name_1.setText(file_1);


                //last try
//                String uriString = uri.toString();
//                File myFile = new File(uriString);
//                String path = myFile.getAbsolutePath();
//                //filepath =path;
//                String iup = getStringFile(myFile);
//                upload(iup,"nameImage");


            }
        }
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

//    private String encodeFileToBase64Binary(File file){
//        String encodedfile = null;
//        try {
//            FileInputStream fileInputStreamReader = new FileInputStream(file);
//            byte[] bytes = new byte[(int)file.length()];
//            fileInputStreamReader.read(bytes);
//            encodedfile = Base64.encodeToString(bytes, Base64.DEFAULT);
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return encodedfile;
//    }

//    public String getStringFile(File f) {
//        InputStream inputStream = null;
//        String encodedFile= "", lastVal;
//        try {
//            inputStream = new FileInputStream(f.getAbsolutePath());
//
//            byte[] buffer = new byte[10240];//specify the size to allow
//            int bytesRead;
//            ByteArrayOutputStream output = new ByteArrayOutputStream();
//            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);
//
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                output64.write(buffer, 0, bytesRead);
//            }
//            output64.close();
//            encodedFile =  output.toString();
//        }
//        catch (FileNotFoundException e1 ) {
//            e1.printStackTrace();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        lastVal = encodedFile;
//        return lastVal;
//    }

    private void upload(String encoded, String imageName){
        try {
            SqlHelper sqlHelper = new SqlHelper(context, CareersFragment.this);
            sqlHelper.setExecutePath("loadresume.php");
            sqlHelper.setActionString("upload");
            ContentValues contentValues = new ContentValues();
            contentValues.put("image_bitmap",encoded);
            contentValues.put("image_name",imageName);
            //contentValues.put("userPassword", password);
            sqlHelper.setParams(contentValues);
            sqlHelper.setMethod(getString(R.string.method_post));
            sqlHelper.executeUrl(true);
        }catch (Exception e){
            EmailHelper emailHelper = new EmailHelper(context, EmailHelper.TECH_SUPPORT, "Error: CareersFragment", e.getMessage() + "\n" + StringHelper.convertStackTrace(e));
            emailHelper.sendEmail();
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
