package gr.example.zografos.vasileios.aspeedometerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    DBHelper dbHelper;
    public static final String USER_PREF = "UserPref" ;
    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DBHelper(this);
        sharedPrefs = getSharedPreferences(USER_PREF, MODE_PRIVATE);

        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView uname = findViewById(R.id.uname);
                TextView pwd = findViewById(R.id.pwd);

                String unameTxt = uname.getText().toString();
                String pwdTxt = pwd.getText().toString();

                DBHelper helper = LoginActivity.this.dbHelper;

                SharedPreferences prefs = LoginActivity.this.sharedPrefs;

                if (unameTxt.length() > 0 && pwdTxt.length() > 0) {
                    if (helper.checkUser(unameTxt, pwdTxt)) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("username", unameTxt);
                        editor.commit();

                        Toast.makeText(LoginActivity.this, "Authentication completed!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(LoginActivity.this, "Username or password is incorrect.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Username and password are required.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
