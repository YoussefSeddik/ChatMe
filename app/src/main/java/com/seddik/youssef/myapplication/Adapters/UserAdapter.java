package com.seddik.youssef.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.seddik.youssef.myapplication.MessageActivity;
import com.seddik.youssef.myapplication.R;
import com.seddik.youssef.myapplication.model.Chat;
import com.seddik.youssef.myapplication.model.User;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<User> mUsers;
    private boolean isChat;
    String last_msg;

    public UserAdapter(Context mContext, List<User> mUsers, boolean isChat) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_users_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = mUsers.get(position);
        holder.username.setText(user.getUsername());
        if (user.getImageURL().toString().equals("default")) {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
        }

        if (isChat) {
            last_msg(user.getId(), holder.last_msg);
        } else {
            holder.last_msg.setVisibility(View.GONE);
        }
        if (isChat) {
            if (user.getStatus().equals("online")) {
                holder.online.setVisibility(View.VISIBLE);
            } else {
                holder.online.setVisibility(View.GONE);
            }
        } else {
            holder.online.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                intent.putExtra("username", user.getUsername());
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile_image;
        CircleImageView online;
        TextView username;
        TextView last_msg;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.users_item_profile_image);
            online = itemView.findViewById(R.id.img_on);
            username = itemView.findViewById(R.id.users_item_username);
            last_msg = itemView.findViewById(R.id.last_msg);
        }
    }

    private void last_msg(final String userid, final TextView textView) {
        last_msg = "default";
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    assert chat != null;
                    assert fUser != null;
                    if (FirebaseAuth.getInstance().getUid() != null) {
                        if (chat.getReceiver().equals(fUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getReceiver().equals(fUser.getUid())) {
                            last_msg = chat.getMessage();
                        }
                    }
                }
                switch (last_msg) {
                    case "default":
                        textView.setText("No last message");
                        break;
                    default:
                        textView.setText(last_msg);
                        break;
                }
                last_msg = "default";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
