package com.david.smartdiningroom.mvp.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.david.smartdiningroom.mvp.view.activity.MyOrderActivity;
import com.david.smartdiningroom.utils.AppManager;

import java.util.Objects;

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
                AppManager.jump(MyOrderActivity.class);
                break;
            case R.id.tv_coupon:
                break;
            case R.id.tv_service:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri uri = Uri.parse("tel:"+10086);
                intent.setData(uri);
                startActivity(intent);
                break;
            case R.id.tv_logout:
                logout();
                break;
        }
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle("提示");
        builder.setMessage("确定要注销当前账号，并退出APP吗？");
        builder.setCancelable(false);
        dialog = builder.create();
        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
                AppManager.jumpAndFinish(LoginActivity.class);
            }
        });
        builder.show();
    }
}
