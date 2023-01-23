package com.cicattendance;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class QRCode extends Fragment {

    View view;
    Button generteQrCode, scanQrCode;
    ImageView imageViewQrCode;
    Spinner spinner;
    String selectedGroupFromSpinner;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    public static GroupAdapter groupAdapter;
    String QrScanResult, date;
    Calendar calendar;
    private SimpleDateFormat dateFormat;

    public QRCode() {
        // Required empty public constructor
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

        calendar = Calendar.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            date = dateFormat.format(calendar.getTime());
        }


        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String userEmail = mAuth.getCurrentUser().getEmail().toString();

        ArrayList<String> groupNameArrayList =new ArrayList<String>();
        groupNameArrayList.add("--Please select the group");

        firebaseFirestore.collection("USERS").document(mAuth.getCurrentUser().getUid()).collection("GROUP").orderBy("group_name").get()
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
               selectedGroupFromSpinner  = parent.getItemAtPosition(position).toString();
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
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(selectedGroupFromSpinner+mAuth.getCurrentUser().getUid().toString(), BarcodeFormat.QR_CODE, 400, 400);
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
                    QrScanResult = result.getContents().toString();


                }
            });




}


