package com.kt.roomdatabasetutorial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    //
    private List<User> mListUser;

    private IClickItemUser iClickItemUser;
    //interface cho sự kiện update
    public interface IClickItemUser{
        void updateUser(User user);
        //
        void deleteUser(User user);
    }
    //update

    public UserAdapter(IClickItemUser iClickItemUser) {
        this.iClickItemUser = iClickItemUser;
    }

    //
    public void setData(List<User> list){
        this.mListUser = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return new UserViewHolder(view);
    }

    //set dữ liệu lên
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final User user = mListUser.get(position);
        if (user == null){
            return;
        }
        //set dữ liệu lên giao diện
        holder.tvUsername.setText(user.getUsername());
        holder.tvAddress.setText(user.getAddress());

        //bắt sự kiện update
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemUser.updateUser(user);
            }
        });
        //
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemUser.deleteUser(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListUser != null){
            return mListUser.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        private TextView tvUsername;
        private TextView tvAddress;
        private Button btnUpdate;
        private Button btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAddress = itemView.findViewById(R.id.tv_address);
            tvUsername = itemView.findViewById(R.id.tv_username);
            btnUpdate = itemView.findViewById(R.id.btn_update);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
