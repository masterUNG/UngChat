package masterung.androidthai.in.th.ungchat.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import masterung.androidthai.in.th.ungchat.MainActivity;
import masterung.androidthai.in.th.ungchat.R;
import masterung.androidthai.in.th.ungchat.utility.MyAlert;
import masterung.androidthai.in.th.ungchat.utility.RegisterModel;

/**
 * Created by masterung on 27/1/2018 AD.
 */

public class RegisterFragment extends Fragment {


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Toolbar Controller
        toolbarController();


    }   // Main Method

    private void toolbarController() {

        Toolbar toolbar = getView().findViewById(R.id.toolbarRegister);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);

//        Setup Title
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.register_th));

//        Setup Navigator
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

//        Setup Menu
        setHasOptionsMenu(true);


    }   // toolbar

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_register, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.itemRegister) {

            uploadValueToFirebase();

        }


        return super.onOptionsItemSelected(item);
    }

    private void uploadValueToFirebase() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please Wait Few Minus...");
        progressDialog.setMessage("Try To Save Your Value To Firebase");
        progressDialog.show();

//        Initial View
        EditText nameEditText = getView().findViewById(R.id.edtName);
        EditText emailEditText = getView().findViewById(R.id.edtEmail);
        EditText passwordEditText = getView().findViewById(R.id.edtPassword);

        final String nameString = nameEditText.getText().toString().trim();
        String emailString = emailEditText.getText().toString().trim();
        String passwordString = passwordEditText.getText().toString().trim();

        if (nameString.isEmpty() || emailString.isEmpty() || passwordString.isEmpty()) {

//            Have Space
            MyAlert myAlert = new MyAlert(getActivity());
            myAlert.normalDialog("Have Space",
                    "Please Fill All Every Blank");

        } else {

//            No Space
            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(emailString, passwordString)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                Toast.makeText(getActivity(), "Register Success",
                                        Toast.LENGTH_SHORT).show();

                                progressDialog.dismiss();

                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                String userUIDstring = firebaseUser.getUid();
                                Log.d("28JanV1", "userUID ==> " + userUIDstring);

//                                SetUp Model
                                final RegisterModel registerModel = new RegisterModel(userUIDstring, nameString);

//                                SetUp Diaplay
                                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest
                                        .Builder().setDisplayName(nameString).build();

                                firebaseUser.updateProfile(userProfileChangeRequest)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                DatabaseReference databaseReference = FirebaseDatabase
                                                        .getInstance()
                                                        .getReference()
                                                        .child("DetailUser");
                                                databaseReference.child(nameString).setValue(registerModel);

                                            }   // onSuccess
                                        });


//                                Back to MainFragment
                                getActivity().getSupportFragmentManager().popBackStack();

                            } else {

                                MyAlert myAlert = new MyAlert(getActivity());
                                myAlert.normalDialog("Cannot Register",
                                        task.getException().getMessage());

                            }


                        }   // onComplete
                    });



        }   // if


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        return view;
    }
}   // Main Class
