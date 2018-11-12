package com.xs.lightpuzzle.photopicker.fragment;

import android.support.v4.app.Fragment;

import com.xs.lightpuzzle.photopicker.IPhotoPicker;

/**
 * Created by xs on 2018/11/12.
 */

public class BaseFragment extends Fragment {

    protected IPhotoPicker mPhotoPicker;

    public void setPhotoPicker(IPhotoPicker photoPicker) {
        mPhotoPicker = photoPicker;
    }
}
