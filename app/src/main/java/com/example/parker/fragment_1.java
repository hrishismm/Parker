package com.example.parker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class fragment_1 extends Fragment implements View.OnClickListener {
    private FirebaseFirestore fstore=FirebaseFirestore.getInstance();
    String userId ;
    private FirebaseAuth mAuth;
    TextView tv1,tv2,tv3,tv4;
    Button delete;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_1,container,false);/*Infating the activity with a fragment on the container created View v=*/

        tv1=(TextView)v.findViewById(R.id.parkingname);
        tv2=(TextView)v.findViewById(R.id.parkingdescription);
        tv3=(TextView)v.findViewById(R.id.parkinglatitude);
        tv4=(TextView)v.findViewById(R.id.parkinglongitude);
delete=(Button)v.findViewById(R.id.delete);
        mAuth = FirebaseAuth.getInstance();

        userId = mAuth.getCurrentUser().getUid();





fstore.collection("userparkings").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
    @Override
    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if(task.getResult().exists())
        {
            DocumentReference documentReference = fstore.collection("userparkings").document(userId);

            documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    tv1.setText("Name:"+" "+value.get("name").toString());
                    tv2.setText("Description"+" "+value.get("description").toString());
                    tv3.setText("Latitude:"+" "+value.get("latitude").toString());
                    tv4.setText("Longitude:"+" "+value.get("longitude").toString());

                }
            });



        }
        else {
            Toast.makeText(getContext(), "No Current Parkings", Toast.LENGTH_SHORT).show();
            tv2.setText("No current Parkings");
        }
    }
});



delete.setOnClickListener(this);


        return v;
    }

    @Override
    public void onClick(View view) {
        DocumentReference documentReference = fstore.collection("userparkings").document(userId);

        documentReference.delete();
        Intent intent = new Intent(getActivity(),MainActivity.class);
        startActivity(intent);

    }
}
