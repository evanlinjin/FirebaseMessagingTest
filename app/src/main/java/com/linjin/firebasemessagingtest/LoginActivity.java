package com.linjin.firebasemessagingtest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class LoginActivity extends AppCompatActivity {
    public final static String _NAME_ = "com.linjin.firebasemessagingtest.NAME";
    private final static String ACCESS_PASSWORD = "c0mc0mwirele$$";

    private final static String[] nameList = {"Ed", "Eric", "Evan", "Helen"};

    SharedPreferences sharedPref;

    EditText mPassword;
    Button mButton;
    Spinner mNameSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPref = getPreferences(Context.MODE_PRIVATE);

        String name = getName();
        if (!name.isEmpty() && getPass().equals(ACCESS_PASSWORD)) {
            startChat(name);
//            finish();
//            return;
        }

        mPassword = (EditText) findViewById(R.id.password_edit);
        mButton = (Button) findViewById(R.id.button);
        mNameSpinner = (Spinner) findViewById(R.id.name_spinner);

        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mNameSpinner.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    attemptStart();
                    return true;
                }
                return false;
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptStart();
            }
        });
    }

    private void attemptStart() {
        String nameStr = mNameSpinner.getSelectedItem().toString();
        String pwStr = mPassword.getText().toString();
        if (pwStr.equals(ACCESS_PASSWORD)
                && !nameStr.isEmpty()
                && !nameStr.startsWith(" ")) {
            setPass(pwStr);
            startChat(setName(nameStr));
        } else {
            mPassword.setText("");
        }
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
