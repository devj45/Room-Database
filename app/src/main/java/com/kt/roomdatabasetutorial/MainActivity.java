package com.kt.roomdatabasetutorial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kt.roomdatabasetutorial.database.UserDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtAddress;
    private Button btnAddUser;
    private RecyclerView revUser;

    private UserAdapter userAdapter;
    private List<User> mListUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        initUi();

        userAdapter = new UserAdapter();
        mListUser = new ArrayList<>();
        userAdapter.setData(mListUser);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        revUser.setLayoutManager(linearLayoutManager);

        revUser.setAdapter(userAdapter);

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });
    }

    private void addUser() {
        String strUsername = edtUsername.getText().toString().trim();
        String strAddress = edtAddress.getText().toString().trim();

        if (TextUtils.isEmpty(strUsername) || TextUtils.isEmpty(strAddress)){
            return;
        }

        User user = new User(strUsername,strAddress);
        //
        UserDatabase.getInstance(this).userDAO().insetUser(user);
        Toast.makeText(this,"add user succesfully",Toast.LENGTH_SHORT).show();

        edtUsername.setText("");
        edtAddress.setText("");

        hideSoftKeyboard();

        mListUser = UserDatabase.getInstance(this).userDAO().getListUser();
        //set cho adapter
        userAdapter.setData(mListUser);
    }

    private void initUi() {
        edtUsername = findViewById(R.id.edt_username);
        edtAddress = findViewById(R.id.edt_address);
        btnAddUser = findViewById(R.id.btn_adduser);
        revUser = findViewById(R.id.rev_user);

    }

    //ẩn key board
    public void hideSoftKeyboard(){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }
}