package com.example.parker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class NoteAdapterFeedback extends FirestoreRecyclerAdapter<Note, NoteAdapterFeedback.NoteHolder> {
private OnItemclickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NoteAdapterFeedback(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull Note model) {
holder.name.setText(model.getName());
        holder.email.setText(model.getEmail());
        holder.phoneno.setText(model.getPhoneno());
        holder.message.setText(model.getMessage());

    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_itemfeedback,parent,false);
        return new NoteHolder(v);
    }

    public void deleteItemnew(int position)
    {

        getSnapshots().getSnapshot(position).getReference().delete();
    }
    class NoteHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView email;
        TextView phoneno;
TextView message;

        public NoteHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.title);
            email=itemView.findViewById(R.id.description);
            phoneno=itemView.findViewById(R.id.text_view_priority);
            message=itemView.findViewById(R.id.latitude);
             itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null)
                    {

                        listener.onItemClick(getSnapshots().getSnapshot(position),position);

                    }
                }
            });
        }
    }
    public  interface  OnItemclickListener
    {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public  void  setOnItemClickListener(OnItemclickListener listener)
    {

this.listener=listener;

    }
}
