package com.example.parker;

        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;

        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;

        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.EventListener;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.FirebaseFirestoreException;
        import com.google.firebase.firestore.QuerySnapshot;

        import java.util.ArrayList;
        import java.util.List;

public class all_feedbacks extends Fragment {
    private List<String> namesList=new ArrayList<>();
    ListView listView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.all_feedbacks,container,false);  /*Infating the activity with a fragment on the container created View v=*/
        final FirebaseFirestore db=FirebaseFirestore.getInstance();
        listView=(ListView)v.findViewById(R.id.l1);

        db.collection("Feedback").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                namesList.clear();
                for(DocumentSnapshot snapshot: value) {

                    namesList.add(snapshot.getString("name"));
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_selectable_list_item,namesList);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);

            }
        });
        return v;

    }
}
