package blog.groundhao.com.mygroundhao.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.Map;
import java.util.Random;

import blog.groundhao.com.mygroundhao.R;
import blog.groundhao.com.mygroundhao.engine.Apn;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 2016/3/9.
 */
public class SplashActivity extends AppCompatActivity {

    private static final float SCALE_END = 1.13F;
    @Bind(R.id.tv_splash_content)
    TextView content;
    @Bind(R.id.tv_splash_title)
    TextView title;
    @Bind(R.id.iv_splash)
    ImageView splashImage;

    private static final int[] SPLASH_ARRAY = {
            R.drawable.splash0,
            R.drawable.splash1,
            R.drawable.splash2,
            R.drawable.splash3,
            R.drawable.splash4,
            R.drawable.splash5,
            R.drawable.splash6,
            R.drawable.splash7,
            R.drawable.splash8,
            R.drawable.splash9,
            R.drawable.splash10,
            R.drawable.splash11,
            R.drawable.splash12,
            R.drawable.splash13,
            R.drawable.splash14,
            R.drawable.splash15,
            R.drawable.splash16,
    };

    private static final String[] SPLASH_CONTENT = {
            "持着干干净净的初衷,赶很远的路",
            "迷失的人迷失了,相逢的人会再相逢",
            "一个人不应当虚度一天的时光",
            "喜欢就在一起,别错过了相爱的最好时机",
            "无法忍受的痛苦就一笑置之",
            "抬起头,看到自己的希望",
            "在时间的长河里,波澜不惊",
            "每一条走上来的路,都有不得不跋涉的理由",
            "越是看起来简单,内心越丰盛",
            "踏实的学习,好好的积累",
            "一切都是马上经历",
            "是的,我很重要",
            "明白了生活,懂得了自己",
            "人生都是走着走着就开阔了",
            "不用在乎别人的评价",
            "把握每一天,享受每一天",
            "任何时候都可以开始做自己想做的事",
            "你只有足够努力,才能与你喜欢的更相配"
    };
    private Random random;
    private static final int ANIMATION_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        Logger.init();
        ButterKnife.bind(this);
        random = new Random(SystemClock.elapsedRealtime());
        Logger.d(SystemClock.elapsedRealtime() + "");
        splashImage.setImageResource(SPLASH_ARRAY[random.nextInt(SPLASH_ARRAY.length)]);
        content.setText(SPLASH_CONTENT[random.nextInt(SPLASH_CONTENT.length)]);
        animateImage();
        Map<String, String> heads = Apn.getHeads();
        StringBuffer sb = new StringBuffer();
        sb.append("手机信息");
        for (Map.Entry<String, String> entrie : heads.entrySet()) {

            sb.append(entrie.getKey() + ":" + entrie.getValue()+"\n");
        }
        Logger.e(sb.toString());
    }

    private void animateImage() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(splashImage, "scaleX", 1F, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(splashImage, "scaleY", 1F, SCALE_END);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIMATION_DURATION).play(animatorX).with(animatorY);
        set.start();

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        });
    }


}
