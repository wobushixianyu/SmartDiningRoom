package com.david.smartdiningroom.mvp.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.david.smartdiningroom.BaseFragment;
import com.david.smartdiningroom.R;

import butterknife.ButterKnife;

public class ClassifyFragment extends BaseFragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.classify_fragment_layout, container, false);
        ButterKnife.bind(this,view);
        return view;
    }
}
