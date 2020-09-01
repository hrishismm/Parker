package com.example.parker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class addparking extends Fragment {
EditText name,owner,size;
Button submit;
    FirebaseFirestore fstore;
//    private FirebaseAuth mAuth;
    String title1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.parking,container,false);  /*Infating the activity with a fragment on the container created View v=*/

        name=v.findViewById(R.id.parkingname);
        owner=v.findViewById(R.id.parkingowner);
        size=v.findViewById(R.id.parkingsize);
        submit=v.findViewById(R.id.buttonsubmit);
        fstore=FirebaseFirestore.getInstance();
         final DocumentReference noteRf=fstore.collection("parkingsno").document("Unique_number");


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name1 = name.getText().toString();
                final String owner1 = owner.getText().toString();
                final String size1 = size.getText().toString();
 noteRf.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
    @Override
    public void onSuccess(DocumentSnapshot documentSnapshot) {
if(documentSnapshot.exists()){
    String title=documentSnapshot.getString("no");
    Toast.makeText(getActivity(), title, Toast.LENGTH_SHORT).show();
    int titileint=Integer.parseInt(title);
    titileint=titileint+1;
    title1=String.valueOf(titileint);
    Toast.makeText(getContext(), title1, Toast.LENGTH_SHORT).show();
}else
{
    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
}
    }
});
                Random random=new Random();
                int randomnumber=random.nextInt();
                DocumentReference documentReference=fstore.collection("parkings").document(String.valueOf(randomnumber)) ;
                Map<String,Object> user=new HashMap<>();
                user.put("name",name1);
                user.put("owner",owner1);
                user.put("size",size1);
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Added Sucessfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });









        return v;
    }
}
