package com.xs.lightpuzzle.photopicker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.photopicker.adapter.SelectedPhotoAdapter;
import com.xs.lightpuzzle.photopicker.entity.Album;
import com.xs.lightpuzzle.photopicker.entity.Photo;
import com.xs.lightpuzzle.photopicker.util.MediaStoreHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by xs on 2018/11/12.
 * <p>
 * 图片选择器界面
 * 场景介绍:
 * 场景一: SrcActivity <--> PhotoPickerActivity
 * 场景二: AActivity --> PhotoPickerActivity --> BActivity
 */

@RuntimePermissions
public class PhotoPickerActivity extends AppCompatActivity
        implements View.OnClickListener, IPhotoPicker {

    protected static final String EXTRA_SCENE = "extra_scene";
    protected static final String EXTRA_MAX_COUNT = "extra_max_count";
    protected static final String EXTRA_ORIGINAL_PHOTOS = "extra_selected_photos";

    protected static final String EXTRA_TEMPLATE_ID = "extra_template_id";
    protected static final String EXTRA_TEMPLATE_CATEGORY = "extra_template_category";
    protected static final String EXTRA_PUZZLE_ACTIVITY_CLAZZ = "extra_puzzle_activity_clazz";

    /**
     * 获取被选中的单张图片的key
     */
    public static final String KEY_SELECTED_PHOTO = "key_selected_photo";
    /**
     * 获取被选中的多张图片的key
     */
    public static final String KEY_SELECTED_PHOTOS = "key_selected_photos";

    /**
     * 请求码, 用于场景一的单选
     */
    public static final int RADIO_PICK_REQ_CODE = 269;
    /**
     * 请求码, 用于场景一的多选
     */
    public static final int MULTI_PICK_REQ_CODE = 885;

    /** ===================== 统一使用场景进行switch处理 ===================== */
    /**
     * 场景一: 单选
     */
    public static final int SCENE_1_RADIO = 0;
    /**
     * 场景一: 多选(多选: 单选图片要么被选中, 要么不被选中)
     */
    public static final int SCENE_1_MULTI = 1;
    /**
     * 场景一: 拼图(多选: 单张图片可被多次选中)
     */
    public static final int SCENE_1_PUZZLE = 3;
    /**
     * 场景二: 拼图(多选: 单张图片可被多次选中)
     */
    public static final int SCENE_2_PUZZLE = 4;

    /**
     * 默认可选图片的最大值
     */
    private static final int DEFAULT_MAX_COUNT = 9;

    /**
     * 选图场景: 默认为场景一中的单选场景
     */
    private int mScene = SCENE_1_RADIO;

    /**
     * 可选图片的最大数目
     */
    private int mMaxCount = DEFAULT_MAX_COUNT;

    /**
     * 之前已选中的图片
     */
    private ArrayList<Photo> mOriginalPhotos;

    /**
     * 拼图的模板Id
     */
    private Class<? extends Activity> mPuzzleActivityClazz;
    private String mTemplateId;
    private int mTemplateCategory;

    /**
     * 被选中的图片
     */
    private ArrayList<Photo> mSelectedPhotos;

    // --- Permission

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE})
    public void requestPermission() {
        initData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        PhotoPickerActivityPermissionsDispatcher
                .onRequestPermissionsResult(this, requestCode, grantResults);

    }

    // --- Permission

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_picker_activity);
        ButterKnife.bind(this);
        init(savedInstanceState);
        initView();
        PhotoPickerActivityPermissionsDispatcher
                .requestPermissionWithPermissionCheck(this);
    }

    private void init(Bundle savedInstanceState) {

        Intent intent = getIntent();
        if (intent == null) {
            throw new NullPointerException("Bundle can't be null");
        }
        mScene = intent.getIntExtra(EXTRA_SCENE, SCENE_1_RADIO);
        switch (mScene) {
            case SCENE_1_MULTI:
                mMaxCount = intent.getIntExtra(EXTRA_MAX_COUNT, DEFAULT_MAX_COUNT);
                mOriginalPhotos = intent.getParcelableArrayListExtra(EXTRA_ORIGINAL_PHOTOS);
                break;
            case SCENE_2_PUZZLE:
                mMaxCount = intent.getIntExtra(EXTRA_MAX_COUNT, DEFAULT_MAX_COUNT);
                mTemplateId = intent.getStringExtra(EXTRA_TEMPLATE_ID);
                mTemplateCategory = intent.getIntExtra(EXTRA_TEMPLATE_CATEGORY, 0);
                mPuzzleActivityClazz = (Class<? extends Activity>) intent
                        .getSerializableExtra(EXTRA_PUZZLE_ACTIVITY_CLAZZ);
                break;
            case SCENE_1_RADIO:
            default:
                break;
        }

        mSelectedPhotos = new ArrayList();
    }

    private void initView() {
        mBackImageBtn.setOnClickListener(this);
        mPhotoRootRelativeLayout.setOnClickListener(this);
        mAlbumRootRelativeLayout.setOnClickListener(this);
        mNextStepRootRelativeLayout.setOnClickListener(this);

        switch (mScene) {
            case SCENE_1_RADIO:
                setupRadioSceneView();
                break;
            case SCENE_1_MULTI:
            case SCENE_2_PUZZLE:
                setupMultiSceneView();
                break;
            default:
                break;
        }
    }

    /**
     * 设置单选场景视图
     */
    private void setupMultiSceneView() {
        if (mOriginalPhotos != null) {
            mSelectedPhotos.addAll(mOriginalPhotos);
        }

        mTipsTextView.setText(getString(
                R.string.photo_picker_aty_puzzle_count_tips, mMaxCount));
    }

    /**
     * 设置多选场景视图
     */
    private void setupRadioSceneView() {
        mSelectedRootRelativeLayout.setVisibility(View.GONE);
    }

    /**
     * 相册列表, 注意: mAlbums.get(0)指向的相册包含了手机中所有图片
     */
    private List<Album> mAlbums;

    /**
     * 手机中所有的图片集合
     */
    private List<Photo> mAllPhotos;

    /**
     * 底部被选中的图片列表适配器
     */
    private SelectedPhotoAdapter mSelectedPhotoAdapter;

    private PhotoPickerController mController;

    /**
     * 获取权限后，初始数据
     */
    private void initData() {
        mController = PhotoPickerController
                .getDefault(this, R.id.photo_pager_aty_fl_container);

        MediaStoreHelper.getAlubums(this, new MediaStoreHelper.Callback() {
            @Override
            public void onResultCallback(List<Album> albums) {
                mAlbums = albums;
                if (albums != null) {
                    mAllPhotos = albums.get(0).getPhotos();

                    mController.getPhotoListFragment().setPhotos(mAllPhotos);
                }
            }
        });

        mController.showPhotoList(mAllPhotos);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mSelectedRecyclerView.setLayoutManager(linearLayoutManager);

        mSelectedPhotoAdapter = new SelectedPhotoAdapter(mSelectedPhotos, Glide.with(this));
        mSelectedRecyclerView.setAdapter(mSelectedPhotoAdapter);

        mSelectedPhotoAdapter.setOnItemClickListener(new SelectedPhotoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Photo photo) {
                int id = view.getId();
                if (id == R.id.selected_item_ib_remove) {
                    if (mSelectedPhotos.contains(photo)) {
                        mSelectedPhotos.remove(photo);
                        mSelectedPhotoAdapter.notifyDataSetChanged();
                    }

                    setSelectedCountText();
                }
            }
        });
    }

    private void setSelectedCountText() {
        if (mScene == SCENE_1_MULTI || mScene == SCENE_2_PUZZLE) {
            mSelectedCountTextView.setText(mSelectedPhotos.size() + "");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photo_picker_aty_ib_back:
                onBack();
                break;
            case R.id.photo_picker_aty_rl_photo:
                if (mState != STATE_PHOTO) {
                    mPhotoTextView.setTextColor(getColor(getResources(), R.color.photo_picker_aty_title_indicator));
                    mPhotoIndView.setVisibility(View.VISIBLE);
                    mAlbumTextView.setTextColor(Color.WHITE);
                    mAlbumIndView.setVisibility(View.INVISIBLE);

                    mController.showPhotoList(mAllPhotos);

                    mState = STATE_PHOTO;
                }
                break;
            case R.id.photo_picker_aty_rl_album:
                if (mState != STATE_ALBUM) {
                    mAlbumTextView.setTextColor(getColor(getResources(), R.color.photo_picker_aty_title_indicator));
                    mAlbumIndView.setVisibility(View.VISIBLE);
                    mPhotoTextView.setTextColor(Color.WHITE);
                    mPhotoIndView.setVisibility(View.INVISIBLE);

                    showAlumList();

                    mState = STATE_ALBUM;
                }
                break;
            case R.id.photo_picker_aty_rl_next_step_root:
                switch (mScene) {
                    // 场景一
                    case SCENE_1_MULTI:
                        if (mSelectedPhotos.size() > 0) {
                            Intent s1MultiIntent = new Intent();
                            s1MultiIntent.putParcelableArrayListExtra(KEY_SELECTED_PHOTOS,
                                    mSelectedPhotos);
                            setResult(RESULT_OK, s1MultiIntent);
                            finish();
                        } else {
                            toast(getString(R.string.photo_picker_aty_please_choose_photo));
                        }
                        break;
                    // 场景二
                    case SCENE_2_PUZZLE:
                        Intent puzzleIntent = new Intent(this, mPuzzleActivityClazz);
                        puzzleIntent.putExtra("template_id", mTemplateId);
                        puzzleIntent.putExtra("template_category", mTemplateCategory);
                        puzzleIntent.putParcelableArrayListExtra("photos", mSelectedPhotos);
                        startActivity(puzzleIntent);
                        break;
                    default:
                        break;
                }
                break;
        }

    }

    private void showAlumList() {
        List<Album> albums = new ArrayList<>();
        albums.addAll(mAlbums);
        albums.remove(0);
        mController.showAlbumList(albums);
    }

    private void onBack() {
        switch (mState) {
            case STATE_PHOTO:
            case STATE_ALBUM:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
            case STATE_SUB_PHOTO:
                showAlumList();
                mState = STATE_ALBUM;

                mAlbumNameTextView.setVisibility(View.INVISIBLE);
                mAlbumRootRelativeLayout.setVisibility(View.VISIBLE);
                mPhotoRootRelativeLayout.setVisibility(View.VISIBLE);
                break;
            case STATE_PAGER:
                if (mPreviewAll) {
                    mController.showPhotoList(mAllPhotos);
                    mState = STATE_PHOTO;
                } else {
                    mController.showPhotoList(null);
                    mState = STATE_SUB_PHOTO;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public int getScene() {
        return mScene;
    }

    @Override
    public List<Photo> getSelectedPhotos() {
        return mSelectedPhotos;
    }

    @Override
    public void selectPhoto(Photo photo) {

        switch (mScene) {
            case SCENE_1_RADIO:
                // 带返回码的单张选图
                Intent intent = new Intent();
                intent.putExtra(KEY_SELECTED_PHOTO, photo);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case SCENE_1_MULTI:
            case SCENE_2_PUZZLE:
                if (moreMaxCount()) {
                    return;
                }
                addPhotoToSelects(photo);
                setSelectedCountText();
                break;
            default:
                break;
        }
    }

    /**
     * 当前的处于Fragment栈顶部的Fragment
     */
    private int mState = STATE_PHOTO;

    private boolean mPreviewAll = true;

    private static final int STATE_PHOTO = 0;
    private static final int STATE_ALBUM = 1;
    private static final int STATE_SUB_PHOTO = 2;
    private static final int STATE_PAGER = 3;

    @Override
    public void previewPhoto(int currItem, List<Photo> photos) {

        mPreviewAll = mState == STATE_PHOTO;
        mState = STATE_PAGER;

        mController.showPhotoPager(currItem, photos);
    }

    @Override
    public void openAlbum(Album album) {
        mState = STATE_SUB_PHOTO;

        mAlbumNameTextView.setText(album.getName());
        mAlbumNameTextView.setVisibility(View.VISIBLE);
        mAlbumRootRelativeLayout.setVisibility(View.INVISIBLE);
        mPhotoRootRelativeLayout.setVisibility(View.INVISIBLE);

        mController.showPhotoList(album.getPhotos());

    }

    private void addPhotoToSelects(Photo photo) {
        mSelectedPhotos.add(photo);
        mSelectedPhotoAdapter.notifyDataSetChanged();
    }

    /**
     * 提示用户选中的图片的数目已超出最大数目
     */
    private boolean moreMaxCount() {
        if (mSelectedPhotos.size() >= mMaxCount) {
            toast(getString(R.string.photo_picker_aty_over_max_count_tips, mMaxCount));
            return true;
        }
        return false;
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onDestroy() {
        PhotoPickerController.destroy();
        super.onDestroy();
    }

    @BindView(R.id.photo_picker_aty_ib_back)
    ImageButton mBackImageBtn;

    @BindView(R.id.photo_picker_aty_tv_photo)
    TextView mPhotoTextView;

    @BindView(R.id.photo_picker_v_photo_ind)
    View mPhotoIndView;

    @BindView(R.id.photo_picker_aty_rl_photo)
    RelativeLayout mPhotoRootRelativeLayout;

    @BindView(R.id.photo_picker_aty_tv_album_name)
    TextView mAlbumNameTextView;

    @BindView(R.id.photo_picker_aty_tv_album)
    TextView mAlbumTextView;

    @BindView(R.id.photo_picker_v_album_ind)
    View mAlbumIndView;

    @BindView(R.id.photo_picker_aty_rl_album)
    RelativeLayout mAlbumRootRelativeLayout;

    @BindView(R.id.photo_pager_aty_fl_container)
    FrameLayout mContainer;

    @BindView(R.id.photo_picker_aty_tv_tips)
    TextView mTipsTextView;

    @BindView(R.id.photo_pager_aty_tv_selected_count)
    TextView mSelectedCountTextView;

    @BindView(R.id.photo_picker_aty_rl_next_step_root)
    RelativeLayout mNextStepRootRelativeLayout;

    @BindView(R.id.photo_pager_aty_rv_selected)
    RecyclerView mSelectedRecyclerView;

    @BindView(R.id.photo_pager_aty_rl_tips_root)
    RelativeLayout mTipsRootRelativeLayout;

    @BindView(R.id.photo_picker_aty_rl_selected_root)
    RelativeLayout mSelectedRootRelativeLayout;


    @SuppressWarnings("deprecation")
    @ColorInt
    public static int getColor(Resources resources, @ColorRes int id) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return resources.getColor(id, null);
        } else {
            return resources.getColor(id);
        }
    }
}
