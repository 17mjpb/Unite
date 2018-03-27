package com.example.jtoco.unite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CategorySelection extends AppCompatActivity implements ValueEventListener {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootReference = firebaseDatabase.getReference();
    private DatabaseReference mCategoryReference = mRootReference.child("category");
    private DatabaseReference mInterestReference = mRootReference.child("interest");

    int n = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_selection);

        ImageButton sports = findViewById(R.id.imageButton);
        ImageButton movies = findViewById(R.id.imageButton2);
        ImageButton music = findViewById(R.id.imageButton3);
        ImageButton animals = findViewById(R.id.imageButton4);
        ImageButton books = findViewById(R.id.imageButton5);
        ImageButton tv = findViewById(R.id.imageButton6);
        ImageButton art = findViewById(R.id.imageButton7);
        ImageButton games = findViewById(R.id.imageButton8);

        do {
            sports.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Intent sportsb=new Intent(CategorySelection.this,Sports.class);
//                startActivity(sportsb);
                    mCategoryReference.setValue("Sports");
                }
            });

            movies.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Intent moviesb=new Intent(CategorySelection.this,Movies.class);
//                startActivity(moviesb);
                    mCategoryReference.setValue("Movies");
                }
            });
            music.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Intent musicb=new Intent(CategorySelection.this,Music.class);
//                startActivity(musicb);
                    mCategoryReference.setValue("Music");
                }
            });
            animals.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Intent animalsb=new Intent(CategorySelection.this,Animals.class);
//                startActivity(animalsb);
                    mCategoryReference.setValue("Animals");
                }
            });
            books.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Intent booksb=new Intent(CategorySelection.this,Books.class);
//                startActivity(booksb);
                    mCategoryReference.setValue("Books");
                }
            });
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Intent tvb=new Intent(CategorySelection.this,TvShows.class);
//                startActivity(tvb);
                    mCategoryReference.setValue("TV Shows");
                }
            });
            art.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Intent artb=new Intent(CategorySelection.this,Art.class);
//                startActivity(artb);
                    mCategoryReference.setValue("Art");
                }
            });
            games.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Intent gamesb=new Intent(CategorySelection.this,Games.class);
//                startActivity(gamesb);
                    mCategoryReference.setValue("Games");
                }
            });
            n++;
        }
        while(n<3);

        Intent goBackToProfile = new Intent(CategorySelection.this, UserProfile.class);
        startActivity(goBackToProfile);

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

}
