package org.guildsa.firebasetest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{

    private final static String TAG = MainActivity.class.getSimpleName();

    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // Replace the file "google-services.json" in the app's folder with yours from
    // YOUR Firebase Console!
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    private final static String EMAIL = "android_user@gmail.com";
    private final static String PASSWORD = "password";
    private final static String SCREEN_NAME = "Kevin";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabase;

    private String mPostKey;
    private DatabaseReference mCommentsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if(!FirebaseApp.getApps(this).isEmpty()) {
//            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//        }

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null)
                {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged: User is signed in." + user.getUid());
                }
                else
                {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged: User is signed out.");
                }
            }
        };

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (mAuthListener != null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public String getUserUid()
    {

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null)
        {
            // User is signed in
            return user.getUid();
        }
        else
        {
            // No user is signed in
            return "";
        }
    }

    private void writeNewUser(String userId, String name)
    {

        User user = new User(name);

        mDatabase.child("users").child(userId).setValue(user);
    }

    public void onCreateUserBtn(View v)
    {

        Log.d(TAG, "onCreateUserBtn() called!");

        mAuth.createUserWithEmailAndPassword(EMAIL, PASSWORD)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {

                        Log.d(TAG, "createUserWithEmailAndPassword:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        if (task.isSuccessful())
                        {

                            Toast.makeText(MainActivity.this, "Authentication success.", Toast.LENGTH_SHORT).show();

                            writeNewUser(task.getResult().getUser().getUid(), SCREEN_NAME);

                        }
                        else
                        {
                            Log.w(TAG, "createUserWithEmailAndPassword", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onSignInUserBtn(View v)
    {

        Log.d(TAG, "onSignInUserBtn() called!");

        mAuth.signInWithEmailAndPassword(EMAIL, PASSWORD)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {

                        Log.d(TAG, "signInWithEmailAndPassword:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        if (task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Sign-In success.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Log.w(TAG, "signInWithEmailAndPassword", task.getException());
                            Toast.makeText(MainActivity.this, "Sign-In failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onCreatePostBtn(View v)
    {

        Log.d(TAG, "onCreatePostBtn() called!");

        final String title = "Post Title";
        final String body = "My awesome post is about this!";

        final String userId = getUserUid();

        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {

                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        if (user == null)
                        {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                        }
                        else
                        {
                            // Write new post
                            writeNewPost(userId, user.username, title, body);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    private void writeNewPost(String userId, String username, String title, String body)
    {

        // Create simultaneous, new post entries at...
        //
        //     /user-posts/$userid/$postid
        //
        // and at...
        //
        //     /posts/$postid

        // Push a post so we can get a unique key back.
        String key = mDatabase.child("posts").push().getKey();

// Keep track of the key for the current or active post.
        mPostKey = key;

        Post post = new Post(userId, username, title, body);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    public void onFetchPostsBtn(View v)
    {

        Log.d(TAG, "onFetchPostsBtn() called!");

        Query queryUserPosts = mDatabase.child("user-posts").child(getUserUid());

        queryUserPosts.addListenerForSingleValueEvent(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {

                            Post post = postSnapshot.getValue(Post.class);

                            System.out.println(post.author + " - " + post.title);

// Grab the last Post keys so this demo app can post Comments to it.
                            mPostKey = post.uid;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        Log.w(TAG, "onCancelled: ", databaseError.toException());
                    }
                });
    }

    public void onCreateCommentBtn(View v)
    {

        Log.d(TAG, "onCreateCommentBtn() called!");

        mCommentsReference = mDatabase.child("post-comments").child(mPostKey);

        final String uid = getUserUid();

        mDatabase.child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        // Get user information
                        User user = dataSnapshot.getValue(User.class);
                        String authorName = user.username;

                        // Create new comment object
                        String commentText = "My comment about this post!";
                        Comment comment = new Comment(uid, authorName, commentText);

                        // Push the comment.
                        mCommentsReference.push().setValue(comment);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                });
    }

    public void onFetchCommentsBtn(View v)
    {

        Log.d(TAG, "onFetchCommentsBtn() called!");

        Query queryPostComments = mDatabase.child("post-comments").child(mPostKey);

        queryPostComments.addListenerForSingleValueEvent(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {

                            Comment comment = postSnapshot.getValue(Comment.class);

                            System.out.println(comment.author + " - " + comment.text);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        Log.w(TAG, "onCancelled: ", databaseError.toException());
                    }
                });

    }

//    public void onRemoveCommentsBtn(View v) {
//
//        Log.d(TAG, "onRemoveCommentsBtn() called!");
//
//    }
}
