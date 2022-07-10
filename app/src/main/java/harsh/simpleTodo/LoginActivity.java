package harsh.simpleTodo;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    RecyclerViewAdapter recyclerViewAdapter;
    private DatabaseReference mDatabase;
    ArrayList<Object> TodoList;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
            filling();


        }
        else{

            View v = findViewById(R.id.floatingActionButton);
            v.setVisibility(View.INVISIBLE);
            View vi = findViewById(R.id.SignUpLayout);
            vi.setVisibility(View.VISIBLE);
        } ;
        TodoList = new ArrayList<>();
       RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(TodoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        EditText editText1= (EditText)findViewById(R.id.event);
        EditText editText2= (EditText)findViewById(R.id.timeH);
        EditText editText3= (EditText)findViewById(R.id.timeM);
        EditText editText4= (EditText)findViewById(R.id.dateY);
        EditText editText5= (EditText)findViewById(R.id.dateM);
        EditText editText6= (EditText)findViewById(R.id.dateD);

        recyclerView.setAdapter(recyclerViewAdapter);
        Button button= (Button)findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckAllFields() == true){
                    settingTodo();
                }
            }
        });


    }
    public boolean CheckAllFields() {
        EditText editText1= (EditText)findViewById(R.id.event);
        EditText editText2= (EditText)findViewById(R.id.timeH);
        EditText editText3= (EditText)findViewById(R.id.timeM);
        EditText editText4= (EditText)findViewById(R.id.dateY);
        EditText editText5= (EditText)findViewById(R.id.dateM);
        EditText editText6= (EditText)findViewById(R.id.dateD);

        if (editText1.length() == 0) {
            editText1.setError("Enter the field correctly");
            return false;
        }

        if (editText2.length()!=2) {
            editText1.setError("Enter the field correctly");
            return false;
        }  
        if (editText3.length()!=2) {
            editText3.setError("Enter the field correctly");
            return false;
        }
        if (editText4.length()!=4) {
            editText1.setError("Enter the field correctly");
            return false;
        }
        if (editText5.length()!=2) {
            editText1.setError("Enter in correct format");
            return false;
        }
        if (editText6.length()!=2) {
            editText1.setError("Enter the field correctly");
            return false;
        }

        return true;
    }

    public void filling(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



//        userdata = new HashMap<>();
         recyclerViewAdapter = new RecyclerViewAdapter(TodoList);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Registered Users").child(user.getUid());
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when new child is added to
                // our data base and after adding new child
                // we are adding that item inside our array list and
                // notifying our adapter that the data in adapter is changed.

           //     userdata.put(snapshot.getKey(),snapshot.getValue().toString());
                Todo Todo = new Todo(snapshot.getKey() , snapshot.getValue().toString());
                TodoList.add(Todo);
                recyclerViewAdapter = new RecyclerViewAdapter(TodoList);
                RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(recyclerViewAdapter);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when the new child is added.
                // when the new child is added to our list we will be
                // notifying our adapter that data has changed.
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // below method is called when we remove a child from our database.
                // inside this method we are removing the child from our array list
                // by comparing with it's value.
                // after removing the data we are notifying our adapter that the
                // data has been changed.
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when we move our
                // child in our database.
                // in our code we are note moving any child.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // this method is called when we get any
                // error from Firebase with error.
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

    }
    public void options(View view){
        FirebaseUser user = mAuth.getCurrentUser();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Hi " +(user==null?"":user.getDisplayName()) +",\n"+
                "Welcome to Todo , Creating and syncing your Todo list is very easy.. \n" +
                "Create your Todo by click on the plus icon below\n" +
                "Not your Account?? Log out by click on button below");
        if(user !=null){
            alertDialogBuilder.setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    logout(view);
                }
            });
        }


        alertDialogBuilder.setNegativeButton("Close",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void signUp(View view){
        EditText EmailId = (EditText)findViewById(R.id.signupEmail);
        String email = EmailId.getText().toString();

        EditText edt = (EditText)findViewById(R.id.signUpPassword);
        String password = edt.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            EditText fullname = (EditText)findViewById(R.id.displayName);
                            String dname = fullname.getText().toString();
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileChangeRequesst= new UserProfileChangeRequest.Builder().setDisplayName(dname).build();
                            user.updateProfile(profileChangeRequesst);


                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("Registered Users");
                            myRef.child(user.getUid()).setValue(user.getDisplayName());


                            View v = findViewById(R.id.SignUpLayout);
                            v.setVisibility(View.INVISIBLE);
                            View ve = findViewById(R.id.loginLayout);
                            ve.setVisibility(View.VISIBLE);


                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }
                    }
                });
    }
    public void signIn(View view){

        EditText EmailId = (EditText)findViewById(R.id.signinEmail);
        String email = EmailId.getText().toString();
        EditText edt = (EditText)findViewById(R.id.signinPassword);
        String password = edt.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Successfully account logged in", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }
    public void showSignin(View view){
        View v = findViewById(R.id.SignUpLayout);
        v.setVisibility(View.INVISIBLE);
        View ve = findViewById(R.id.loginLayout);
        ve.setVisibility(View.VISIBLE);

    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();


        }
        else{

            View v = findViewById(R.id.floatingActionButton);
            v.setVisibility(View.INVISIBLE);
            View vi = findViewById(R.id.SignUpLayout);
            vi.setVisibility(View.VISIBLE);
        } ;
    }
    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        showSignin(view);
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
    public  void createTodo(View view){
        View ve = findViewById(R.id.mainLayout);
        ve.setVisibility(View.GONE);
        View vje = findViewById(R.id.setting);
        vje.setVisibility(View.VISIBLE);



    }
    public void toggle(View view){
        Button button=(Button)view;
    if(button.getText().toString().equals("AM")){
        button.setText("PM");
    }else{
        button.setText("AM");
    }
    }
    public void settingTodo(){

        FirebaseUser currentUser = mAuth.getCurrentUser();
        EditText timeH = (EditText) findViewById(R.id.timeH);
        EditText timeM = (EditText)findViewById(R.id.timeM);
        EditText dateD;
        dateD = (EditText)findViewById(R.id.dateD);
        EditText dateM = (EditText)findViewById(R.id.dateM);
        EditText dateY = (EditText)findViewById(R.id.dateY);
        Button button=(Button)findViewById(R.id.buttonAM);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        EditText event=(EditText)  findViewById(R.id.event);

         String date = dateY.getText().toString() + dateM.getText().toString() + dateD.getText().toString();
        String time=timeH.getText().toString()+timeM.getText().toString()+button.getText().toString();
        String Event=event.getText().toString();
        List<Object> anything = new ArrayList<Object>();
        anything.add(Event);
        anything.add(date);
        anything.add(time);
         mDatabase.child("Registered Users").child(currentUser.getUid()).child(date).child(Event).setValue(time);
        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
    }
    public void closesetter(View view){

        View ve = findViewById(R.id.mainLayout);
        ve.setVisibility(View.VISIBLE);
        View vje = findViewById(R.id.setting);
        vje.setVisibility(View.GONE);


    }


}