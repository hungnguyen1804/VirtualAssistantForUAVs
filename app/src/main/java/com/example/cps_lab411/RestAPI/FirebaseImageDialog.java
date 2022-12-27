package com.example.cps_lab411.RestAPI;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cps_lab411.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseImageDialog extends Dialog implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ArrayList<FirebaseModel> firebaseModelArrayList;
    private RecyclerImageAdapter recyclerImageAdapter;
    public FirebaseImageDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firebase_image_dialog);

        recyclerView = findViewById(R.id.recyclerViewFirebase);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);

        firebaseModelArrayList = new ArrayList<>();
        clearAll();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("images");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearAll();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FirebaseModel firebaseModel = new FirebaseModel();
                    firebaseModel.setImageurl(snapshot.getValue().toString());

                    firebaseModelArrayList.add(firebaseModel);
                }
                recyclerImageAdapter = new RecyclerImageAdapter(getContext(), firebaseModelArrayList);
                recyclerView.setAdapter(recyclerImageAdapter);
                recyclerImageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearAll() {
        if (firebaseModelArrayList != null) {
            firebaseModelArrayList.clear();

            if (recyclerImageAdapter != null) {
                recyclerImageAdapter.notifyDataSetChanged();
            }
        }
        firebaseModelArrayList = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {

    }
}
