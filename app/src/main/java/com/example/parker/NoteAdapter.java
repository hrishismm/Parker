package com.example.parker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note,NoteAdapter.NoteHolder> {
private OnItemclickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull Note model) {
holder.textViewTitle.setText(model.getTitle());
        holder.desc.setText(model.getDescription());
        holder.prio.setText(String.valueOf(model.getPriority()));
        holder.lat.setText(model.getLatitude());
        holder.lon.setText(model.getLongitude());
        holder.remaining.setText(String.valueOf(model.getRemaining()));

    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        return new NoteHolder(v);
    }

    public void deleteItemnew(int position)
    {

        getSnapshots().getSnapshot(position).getReference().delete();
    }
    class NoteHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView desc;
        TextView prio;
TextView remaining;
        TextView lat;
        TextView lon;

        public NoteHolder(View itemView) {
            super(itemView);
            textViewTitle=itemView.findViewById(R.id.title);
            desc=itemView.findViewById(R.id.description);
            prio=itemView.findViewById(R.id.text_view_priority);
            lat=itemView.findViewById(R.id.latitude);
            lon=itemView.findViewById(R.id.longitude);
remaining=itemView.findViewById(R.id.text_view_remaining);
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
        void onItemClick(DocumentSnapshot documentSnapshot,int position);
    }
    public  void  setOnItemClickListener(OnItemclickListener listener)
    {

this.listener=listener;

    }
}
