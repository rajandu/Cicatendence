package com.cicattendance;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class QRCode extends Fragment {

    View view;
    Button generteQrCode, scanQrCode; // buttons to generate and scan qr code
    ImageView imageViewQrCode;  // Image view to show the qr code to be scanned
    Spinner spinner; // spinner
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    String QrScanResult, date;  // result after scanning qr code , todays date
    String qrStringGenerate, qrStringGenerateFinal, current_user_uid, current_user_full_name;
    Calendar calendar;
    private SimpleDateFormat dateFormat;
    TextView dateTimedisplay;

    textEnc enc = new textEnc();

    public QRCode() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_qr_code, container, false);

        generteQrCode = view.findViewById(R.id.generateCode);
        scanQrCode = view.findViewById(R.id.regenerateQrCode);
        imageViewQrCode = view.findViewById(R.id.qrCodeImageView);
        spinner = view.findViewById(R.id.spinner);

        //testing purpose
        dateTimedisplay = view.findViewById(R.id.dataTimeDisplay);
        try {
            dateTimedisplay.setText(enc.decrypt("[B@48bc0e"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        calendar = Calendar.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            date = dateFormat.format(calendar.getTime());
        }


        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        current_user_uid = mAuth.getCurrentUser().getUid();


        // Spinner to display All the groups
        ArrayList<String> groupNameArrayList =new ArrayList<String>();
        groupNameArrayList.add("--Please select the group");

        firebaseFirestore.collection("USERS").document(current_user_uid).collection("GROUP").orderBy("group_name").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            if(task.isSuccessful()){
                                           groupNameArrayList.add(documentSnapshot.get("group_name").toString());

                            }else{
                                String error=task.getException().getMessage();
                                Toast.makeText(getActivity().getApplicationContext(),error,Toast.LENGTH_SHORT).show();
                            }
                        }
                        QuerySnapshot documentSnapshot=task.getResult();
                    }
                });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, groupNameArrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                qrStringGenerate = "{" +
                        "\"user_id\":\""  +mAuth.getCurrentUser().getUid()         +"\","+
                        "\"group\":\""    +groupNameArrayList.get(position)        +"\","+
                        "\"date\":\""     +date                                    +"\""+
                        "}";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getActivity().getApplicationContext(), "Please select the group to make QR code", Toast.LENGTH_SHORT).show();

            }
        });

        generteQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(qrStringGenerate, BarcodeFormat.QR_CODE, 400, 400);
                    imageViewQrCode.setImageBitmap(bitmap);
                } catch(Exception e) {

                }

            }
        });

        scanQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanOptions options = new ScanOptions();
                options.setPrompt("Scan a barcode");
                options.setCameraId(0);  // Use a specific camera of the device
                options.setBarcodeImageEnabled(true);
                options.setOrientationLocked(false);
                barcodeLauncher.launch(options);
            }
        });


        return view;
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {

                if(result.getContents() == null) {
                    Toast.makeText(getActivity().getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    QrScanResult = result.getContents();
                    try {
                        pushDataToFirebase(QrScanResult);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });

    private void pushDataToFirebase(String qrScanResult) throws JSONException {
        JSONObject obj = new JSONObject(QrScanResult);

        String user_id_qr=obj.getString("user_id"), group_qr = obj.getString("group"), date_qr=obj.getString("date");

        firebaseFirestore.collection("USERS").document(current_user_uid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            current_user_full_name = document.get("fullname").toString();

                            Map<String , String> groups = new HashMap<>();
                            groups.put("student_name",current_user_full_name);


                            firebaseFirestore.collection("USERS").document(user_id_qr).collection("GROUP").document(group_qr.trim().toUpperCase())
                                    .collection("DATES").document(date_qr).collection("ATTENDENCE").add(groups)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getActivity().getApplicationContext(), " Attendance Recorded ",Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(getActivity().getApplicationContext(),"Attendance Not Recorded", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }else{
                            Log.d(TAG, "get failed with ", task.getException());
                        }

                    }
                });
    }
}


