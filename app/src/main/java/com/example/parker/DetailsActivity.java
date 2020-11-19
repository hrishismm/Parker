package com.example.parker;

import android.app.TimePickerDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.logging.Logger.global;

public class DetailsActivity extends AppCompatActivity {
TextView name,description,space,remaining,lat,lon;
    private FirebaseFirestore fstore=FirebaseFirestore.getInstance();
    //private CollectionReference col= db.collection("please");
    String userId ;
    TextView textview1,Textview2,fees;
    Button b1;
    TimePicker timepicker;
    private FirebaseAuth mAuth;
    private FirebaseAuth mAuth2;
    //private FirebaseFirestore fstore1=FirebaseFirestore.getInstance();
    Button b2,b3,b4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        name=findViewById(R.id.name);
        description=findViewById(R.id.desc);
        space=findViewById(R.id.space);
        remaining=findViewById(R.id.rem);
        lat=findViewById(R.id.lat);
        lon=findViewById(R.id.lon);
        b2=(Button)findViewById(R.id.timePicker);
        b3=(Button)findViewById(R.id.timePickerout);
        b4=(Button)findViewById(R.id.bill);

        mAuth = FirebaseAuth.getInstance();
       // timepicker.setIs24HourView(true);
        textview1=(TextView)findViewById(R.id.time);
        Textview2=(TextView)findViewById(R.id.timeout);
        fees=(TextView)findViewById(R.id.fees);

// Get Current time
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);


                TimePickerDialog timePickerDialog = new TimePickerDialog(DetailsActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                textview1.setText(hourOfDay + ":" + minute+":00");


                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);


                TimePickerDialog timePickerDialog = new TimePickerDialog(DetailsActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                Textview2.setText(hourOfDay + ":" + minute+":00");
                            }
                        }, hour, minute, true);
                timePickerDialog.show();

            }
        });
b4.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        String time1 = textview1.getText().toString();
        String time2 = Textview2.getText().toString();

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = null;
        try {
            date1 = format.parse(time1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = format.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long difference = date2.getTime() - date1.getTime();
        long hour = TimeUnit.MILLISECONDS.toHours(difference);

        Toast.makeText(DetailsActivity.this, Long.toString(hour), Toast.LENGTH_LONG).show();

       if(hour>0)
       {
          // Toast.makeText(DetailsActivity.this, "Positive", Toast.LENGTH_SHORT).show();
           if(hour<=2)
           {
               Toast.makeText(DetailsActivity.this, "50rs", Toast.LENGTH_SHORT).show();
           }
           if(hour>2 && hour<=8)
           {
               Toast.makeText(DetailsActivity.this, "100rs", Toast.LENGTH_SHORT).show();
           }
           if(hour>8 && hour<=24)
           {
               Toast.makeText(DetailsActivity.this, "120rs", Toast.LENGTH_SHORT).show();
           }


       }

    }
});

        b1=findViewById(R.id.book);
        final String title=getIntent().getStringExtra("title");
        //col.document(title);
        DocumentReference documentReference = fstore.collection("all").document(title);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                name.setText("Name:"+" "+value.get("title").toString());
                description.setText("Description:"+" "+value.get("description").toString());
                space.setText("No of parkings:"+" "+value.get("priority").toString());
                remaining.setText(value.get("remaining").toString());
                lat.setText(value.get("latitude").toString());
                lon.setText(value.get("longitude").toString());

            }
        });
        userId = mAuth.getCurrentUser().getUid();

b1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        fstore.collection("userparkings").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists())
                {
                    Toast.makeText(DetailsActivity.this, "Already have an active parking", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(remaining.getText().equals("0"))
                    {
                        Toast.makeText(DetailsActivity.this, "No parking space Available", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        int a1=Integer.valueOf(remaining.getText().toString());
                        int remainingnew=a1-1;

                        DocumentReference documentReference = fstore.collection("all").document(title);
                        Map<String, Object> user = new HashMap<>();
                        user.put("remaining", remainingnew);
                        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(DetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                        userId = mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference2=fstore.collection("userparkings").document(userId);
                        Map<String, Object> user1 = new HashMap<>();
                        user1.put("intime", textview1.getText().toString());
                        user1.put("outime", Textview2.getText().toString());
                        user1.put("name", name.getText().toString());
                        user1.put("description", description.getText().toString());
                        user1.put("latitude", lat.getText().toString());
                        user1.put("longitude", lon.getText().toString());
                        //user1.put("", message);
                        documentReference2.set(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Added Sucessfully", Toast.LENGTH_SHORT).show();

                            }
                        });




                        createPdf(name.getText().toString());
                    }

                }
            }
        });
        //Toast.makeText(DetailsActivity.this, (CharSequence) remaining, Toast.LENGTH_SHORT).show();

    }
});


    }

    private void createPdf(String sometext){
        // create a new document
        PdfDocument document = new PdfDocument();

        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(50, 200, 30, paint);
        paint.setColor(Color.BLACK);
        canvas.drawText("Parking Invoice", 80, 20, paint);
        canvas.drawText(name.getText().toString(), 80, 50, paint);
        canvas.drawText(description.getText().toString(), 80, 70, paint);
        canvas.drawText("latitude"+lat.getText().toString(), 80, 90, paint);
        canvas.drawText("longitude"+lon.getText().toString(), 80, 110, paint);
        canvas.drawText("Time in"+textview1.getText().toString(), 80, 130, paint);
        canvas.drawText("Time Out"+Textview2.getText().toString(), 80, 150, paint);
        canvas.drawText("Fees paid"+fees.getText().toString(), 80, 170, paint);

        //canvas.drawt
        // finish the page
        document.finishPage(page);
// draw text on the graphics object of the page


        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"ParkingInvoice.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }
    public String getCurrentTime(){
        String currentTime="Current Time: "+timepicker.getCurrentHour()+":"+timepicker.getCurrentMinute();
        return currentTime;
    }

}