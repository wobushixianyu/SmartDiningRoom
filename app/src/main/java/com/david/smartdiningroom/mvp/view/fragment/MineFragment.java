package com.david.smartdiningroom.mvp.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.david.smartdiningroom.BaseFragment;
import com.david.smartdiningroom.R;
import com.david.smartdiningroom.mvp.view.activity.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MineFragment extends BaseFragment {

    @BindView(R.id.img_header)
    CircleImageView imgHeaderView;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.user_address)
    TextView mUserAddress;
    private AlertDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.tv_order, R.id.tv_coupon, R.id.tv_service, R.id.tv_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_order:
                break;
            case R.id.tv_coupon:
                break;
            case R.id.tv_service:
                break;
            case R.id.tv_logout:
                logout();
                break;
        }
    }

    private void logout(){
        if (dialog != null && !dialog.isShowing()){
            dialog.show();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("提示");
            builder.setMessage("确定要注销当前账号，并退出APP吗？");
            builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
            });
            dialog = builder.create();
            dialog.show();
        }
    }
}
