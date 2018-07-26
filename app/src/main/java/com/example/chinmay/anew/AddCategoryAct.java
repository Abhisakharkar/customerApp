package com.example.chinmay.anew;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddCategoryAct extends AppCompatActivity {

    private ImageView catImg;
    private EditText catName;
    private ActionProcessButton btn;

    private String parentId;
    private int GALLERY_INTENT = 2;
    private Uri imageUri;
    private String imagePath;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        Intent intent = getIntent();
        if(intent == null){
            finish();
        }

        parentId = intent.getStringExtra("PARENT_ID");
        if(parentId == null){
            finish();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            Toast.makeText(this,"Please Login to continue",Toast.LENGTH_SHORT).show();
            finish();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Suggestion").child(firebaseAuth.getCurrentUser().getUid());

        catImg = findViewById(R.id.categorImgAtSuggest);
        catName = findViewById(R.id.nameAtSuggest);
        btn = findViewById(R.id.suggetsBtnAtAddCat);

        catImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri != null){
                    uploadImage();
                }else{
                    saveData();
                }
            }
        });
    }

    private void chooseImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(catImg);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage(){
        if(imageUri != null){
            btn.setProgress(1);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imagePath = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                    saveData();
                    btn.setProgress(100);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddCategoryAct.this,"Try With Other Image",Toast.LENGTH_SHORT).show();
                    btn.setProgress(-1);
                }
            });
        }else {

        }
    }

    private void saveData(){
        if(imagePath != null){
            databaseReference.child("Image").setValue(imagePath);
        }
        if(catName.getText().toString() == null){
            catName.setError("Please Enter Category Name");
        }else{
            databaseReference.child("Name").setValue(catName.getText().toString());
            databaseReference.child("Parent").setValue(parentId);
            finish();
        }
    }
}
