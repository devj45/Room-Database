package com.kt.roomdatabasetutorial;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kt.roomdatabasetutorial.database.UserDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtAddress;
    private Button btnAddUser;
    private RecyclerView revUser;
    private TextView tvDeleleAll;
    private EditText edtSearch;
    private ImageButton img_btn_search;
    private EditText edtYear;

    private UserAdapter userAdapter;
    private List<User> mListUser;

    private int MY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        initUi();

        userAdapter = new UserAdapter(new UserAdapter.IClickItemUser() {
            @Override
            public void updateUser(User user) {
                clickUpdateUser(user);
            }

            @Override
            public void deleteUser(User user) {
                clickDeleteUser(user);
            }
        });
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

        tvDeleleAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDeleteAllUser();
            }
        });


        //click search 1: search với icon tìm kiếm tại bàn phím hiện lên
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //sự kiện
                if (i == EditorInfo.IME_ACTION_SEARCH){
                    //logic search
                    handleSearchUser();
                }
                return false;
            }
        });
        //click search 2: search với icon search trên giao diện
        img_btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logic search
                handleSearchUser();
            }
        });
        //
        LoadData();
    }

    private void addUser() {
        String strUsername = edtUsername.getText().toString().trim();
        String strAddress = edtAddress.getText().toString().trim();
        String strYear = edtYear.getText().toString().trim();

        if (TextUtils.isEmpty(strUsername) || TextUtils.isEmpty(strAddress)){
            return;
        }

        User user = new User(strUsername,strAddress,strYear);
        //kiểm tra nếu user đã tồn tại
        if (isUserExist(user)){
            Toast.makeText(this,"User exist",Toast.LENGTH_SHORT).show();
            //return; tức cái phía sau nó không được thực hiện
            return;
        }

        //
        UserDatabase.getInstance(this).userDAO().insetUser(user);
        Toast.makeText(this,"add user succesfully",Toast.LENGTH_SHORT).show();

        edtUsername.setText("");
        edtAddress.setText("");
        edtYear.setText("");

        hideSoftKeyboard();

        LoadData();
    }

    private void initUi() {
        edtUsername = findViewById(R.id.edt_username);
        edtAddress = findViewById(R.id.edt_address);
        btnAddUser = findViewById(R.id.btn_adduser);
        revUser = findViewById(R.id.rev_user);
        tvDeleleAll = findViewById(R.id.tv_delete_all);
        edtSearch = findViewById(R.id.edt_search);
        img_btn_search = findViewById(R.id.img_btn_search);
        edtYear = findViewById(R.id.edt_year);

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

    private void LoadData(){
        mListUser = UserDatabase.getInstance(this).userDAO().getListUser();
        //set cho adapter
        userAdapter.setData(mListUser);
    }

    private boolean isUserExist(User user){
        List<User> list = UserDatabase.getInstance(this).userDAO().checkUser(user.getUsername());
        return list != null && !list.isEmpty();
    }
    //
    private void clickUpdateUser(User user){
        //
        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("object_user",user);
        intent.putExtras(bundle);
        startActivityForResult(intent, MY_REQUEST_CODE);
    }
    //delete
    private void clickDeleteUser(User user){
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete User")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //delete user
                        UserDatabase.getInstance(MainActivity.this).userDAO().deleteUser(user);
                        Toast.makeText(MainActivity.this,"Delete User Succesfuly",Toast.LENGTH_SHORT).show();

                        LoadData();
                    }
                })
                .setNegativeButton("No",null)
                .show();
    }
    //
    //
    private void clickDeleteAllUser(){
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete All User")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //delete user
                        UserDatabase.getInstance(MainActivity.this).userDAO().deleteAllUser();
                        Toast.makeText(MainActivity.this,"Delete All User Succesfuly",Toast.LENGTH_SHORT).show();

                        LoadData();
                    }
                })
                .setNegativeButton("No",null)
                .show();
    }

    //search
    private void handleSearchUser(){
        String strKeyword = edtSearch.getText().toString().trim();
        //xóa đi
        mListUser.clear();
        //set
        mListUser = UserDatabase.getInstance(MainActivity.this).userDAO().searchUser(strKeyword);
        //
        userAdapter.setData(mListUser);
        //ẩn bàn phím đi
        hideSoftKeyboard();

    }

    //nhận kết quả trả về khi update
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            LoadData();
        }
    }


}