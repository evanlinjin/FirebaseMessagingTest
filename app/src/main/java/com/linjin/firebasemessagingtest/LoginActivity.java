package com.linjin.firebasemessagingtest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    public final static String _NAME_ = "com.linjin.firebasemessagingtest.NAME";
    private final static String ACCESS_PASSWORD = "git";

    SharedPreferences sharedPref;

    EditText mName, mPassword;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPref = getPreferences(Context.MODE_PRIVATE);

        String name = getName();
        if (!name.isEmpty() && getPass().equals(ACCESS_PASSWORD)) {
            startChat(name);
            finish();
            return;
        }

        mName = (EditText) findViewById(R.id.name_edit);
        mPassword = (EditText) findViewById(R.id.password_edit);
        mButton = (Button) findViewById(R.id.button);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = mName.getText().toString();
                String pwStr = mPassword.getText().toString();
                if (pwStr.equals(ACCESS_PASSWORD)
                        && !nameStr.isEmpty()
                        && !nameStr.startsWith(" ")) {
                    setPass(pwStr);
                    startChat(setName(nameStr));
                }
            }
        });
    }

    private void startChat(String name) {
        Intent intent = new Intent(this, MessagingActivity.class);
        intent.putExtra(_NAME_, name);
        startActivity(intent);
        finish();
    }

    private String getName() {
        return sharedPref.getString("name", "");
    }

    private String setName(String name) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", name);
        editor.apply();
        return name;
    }

    private String getPass() {
        return sharedPref.getString("pass", "");
    }

    private void setPass(String pass) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("pass", pass);
        editor.apply();
    }
}
