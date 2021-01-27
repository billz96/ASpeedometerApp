package gr.example.zografos.vasileios.aspeedometerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        Button registerBtn = (Button) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper helper = MainActivity.this.dbHelper;

                TextView uname = (TextView) findViewById(R.id.newuname);
                TextView pwd = (TextView) findViewById(R.id.newpwd);

                String unameTxt = uname.getText().toString();
                String pwdTxt = pwd.getText().toString();
                if (unameTxt.length() > 0 && pwdTxt.length() > 0) {
                    if (helper.userExists(unameTxt)) {
                        Toast.makeText(MainActivity.this, "Username already taken.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Registration completed!", Toast.LENGTH_SHORT).show();
                        helper.insertUser(unameTxt, pwdTxt);
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Username and password are required.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView loginTxt = (TextView) findViewById(R.id.loginTxt);
        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
