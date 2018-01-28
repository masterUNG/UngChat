package masterung.androidthai.in.th.ungchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import masterung.androidthai.in.th.ungchat.fragment.ChatFragment;
import masterung.androidthai.in.th.ungchat.fragment.RegisterFragment;

public class ServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentServiceFragment, new ChatFragment())
                    .commit();

        }


    }   // Main Method

}   // Main Class
