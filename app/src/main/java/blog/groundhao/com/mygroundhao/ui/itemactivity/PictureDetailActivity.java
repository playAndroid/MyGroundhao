package blog.groundhao.com.mygroundhao.ui.itemactivity;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.nineoldandroids.animation.ObjectAnimator;
import com.orhanobut.logger.Logger;

import blog.groundhao.com.mygroundhao.R;
import blog.groundhao.com.mygroundhao.engine.uibest.BestActivity;
import blog.groundhao.com.mygroundhao.model.Comments;
import blog.groundhao.com.mygroundhao.utils.ShowToastUtils;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 2016/4/19.
 */
public class PictureDetailActivity extends BestActivity {
    @Bind(R.id.img_back)
    ImageButton img_back;
    @Bind(R.id.img_share)
    ImageButton img_share;
    @Bind(R.id.tv_oo)
    TextView tv_oo;
    @Bind(R.id.tv_xx)
    TextView tv_xx;
    @Bind(R.id.img_chat)
    ImageButton img_chat;
    @Bind(R.id.img_download)
    ImageButton img_download;
    @Bind(R.id.relative_layout)
    RelativeLayout relative_layout;
    @Bind(R.id.linear_layout)
    LinearLayout linear_layout;
    @Bind(R.id.image_icon)
    SimpleDraweeView mSimpleDraweeView;
    @Bind(R.id.progress)
    ProgressBar progressBar;
    public static final int ANIMATION_DURATION = 500;
    boolean isHide = false;
    private ControllerListener<ImageInfo> controllerListener;
    //    @Override
//    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_detail);
        initView();
        initData();
        registerListener();
    }

    private void registerListener() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowToastUtils.Short("分享");
            }
        });
        mSimpleDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBar();
            }
        });
        tv_oo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowToastUtils.Short("OO没用");
            }
        });
        tv_xx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowToastUtils.Short("XX也没用");
            }
        });
        img_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowToastUtils.Short("吐槽");
            }
        });
        img_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowToastUtils.Short("保存图片");
            }
        });


    }

    private void initData() {
        registerControllerListener();
        Comments comments = (Comments) getIntent().getSerializableExtra(DATA_NEWSTHING);
        Logger.e("进入图片详情页" + comments.getComment_author());
        String stringUrl = comments.getPics()[0];
        Uri uri = Uri.parse(stringUrl);
//        if (stringUrl.endsWith(".gif")) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(controllerListener)
                .setUri(uri)
                .setAutoPlayAnimations(true)
//                . // other setters
                .build();

        mSimpleDraweeView.setController(controller);
//        } else {
//            mSimpleDraweeView.setImageURI(uri);
//        }


    }

    private void registerControllerListener() {
        controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable ImageInfo imageInfo,
                    @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                QualityInfo qualityInfo = imageInfo.getQualityInfo();
                /**
                 * 最终的图像收到!" +
                 “大小% d x % d”,
                 “质量水平% d,足够好:% s,完整的质量:% s”
                 */
                Logger.e("Final image received! " +
                                "Size %d x %d",
                        "Quality level %d, good enough: %s, full quality: %s",
                        imageInfo.getWidth(),
                        imageInfo.getHeight(),
                        qualityInfo.getQuality(),
                        qualityInfo.isOfGoodEnoughQuality(),
                        qualityInfo.isOfFullQuality());
                progressBar.setVisibility(View.GONE);
                Logger.e("onFinalImageSet被调用");
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                /**
                 * 中间图片收到了
                 */
                Logger.e("Intermediate image received");
                Logger.e("onIntermediateImageSet被调用");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
//                FLog.e(getClass(), throwable, "Error loading %s", id)
                Logger.e("onFailure被调用");
            }
        };
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        toggleBar();
    }

    private void initView() {
        ButterKnife.bind(this);
    }


    private void toggleBar() {

        if (!isHide) {
            isHide = true;
            ObjectAnimator.ofFloat(linear_layout, "translationY", 0, linear_layout.getMeasuredHeight())
                    .setDuration(ANIMATION_DURATION)
                    .start();
            ObjectAnimator.ofFloat(relative_layout, "translationY", 0, -relative_layout.getMeasuredHeight())
                    .setDuration(ANIMATION_DURATION)
                    .start();
        } else {
            isHide = false;
            ObjectAnimator.ofFloat(linear_layout, "translationY", linear_layout.getMeasuredHeight(), 0)
                    .setDuration(ANIMATION_DURATION)
                    .start();
            ObjectAnimator.ofFloat(relative_layout, "translationY", -relative_layout.getMeasuredHeight(), 0)
                    .setDuration(ANIMATION_DURATION)
                    .start();
        }
    }
}
