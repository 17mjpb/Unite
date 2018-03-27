package com.example.jtoco.unite;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity implements ValueEventListener{
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootReference = firebaseDatabase.getReference();
    private DatabaseReference mCategoryReference = mRootReference.child("category");
    private DatabaseReference mInterestReference = mCategoryReference.child("interest");

    private Button mUpdateProfilePictureButton;
    private Button mSelectNewInterests;
    private TextView mIntrest1;
    private TextView mIntrest2;
    private TextView mIntrest3;
    private CircleImageView mUserProfilePicture;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        mUpdateProfilePictureButton = findViewById(R.id.updateProfilePictureButton);
        mUserProfilePicture = findViewById(R.id.userProfilePic);

        mIntrest1 = findViewById(R.id.interest1);
        mIntrest2 = findViewById(R.id.interest2);
        mIntrest3 = findViewById(R.id.interest3);

        mSelectNewInterests = findViewById(R.id.newInterestsButton);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mUpdateProfilePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        mSelectNewInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToCategorySelection = new Intent(UserProfile.this,CategorySelection.class);
                mRootReference.child("category").removeValue();
                startActivity(goToCategorySelection);
            }
        });

    }
    private void printStarter(View view) {
        Intent getUpdatedUserName = getIntent();

        String updatedUserName = getUpdatedUserName.getStringExtra("Name");
        ((TextView)findViewById(R.id.userName)).setText(updatedUserName);
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                mUserProfilePicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        uploadImage();
    }


    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(UserProfile.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UserProfile.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
            }
        }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        final List<String> categoryArrayList = new ArrayList<String>();

        for (DataSnapshot child : dataSnapshot.getChildren()) {

            String categoryKey = child.getKey();

            if(categoryKey.equals("category")){

                String category = dataSnapshot.getValue(String.class);
                categoryArrayList.add(category);
            }
        }

        String[] categoryArrary = categoryArrayList.toArray(new String[categoryArrayList.size()]);
        mIntrest1.setText(categoryArrary[0]);
        mIntrest2.setText(categoryArrary[1]);
        mIntrest3.setText(categoryArrary[2]);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mCategoryReference.addValueEventListener(this);
        mInterestReference.addValueEventListener(this);

    }
}
