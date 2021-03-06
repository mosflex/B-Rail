package be.ontime;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro2;

import be.ontime.Utils.PrefsUtils;
import be.ontime.widget.SlideIntro;

/**
 * Created by Jawad on 26-06-16.
 */
public class IntroActivity extends AppIntro2 {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(SlideIntro.newInstance(R.layout.intro1));
        addSlide(SlideIntro.newInstance(R.layout.intro2));
        addSlide(SlideIntro.newInstance(R.layout.intro3));

        setFadeAnimation();

        setImmersiveMode(true);
        //setGoBackLock(true);
        //setColorTransitionsEnabled(true);

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
       // addSlide(AppIntroFragment.newInstance(getString(R.string.title_intro_1), getString(R.string.description_intro_1), R.drawable.ic_search_white_256dp, Color.parseColor("#5C6BC0")));
        //addSlide(AppIntroFragment.newInstance(getString(R.string.title_intro_2), getString(R.string.description_intro_2), R.drawable.ic_playlist_add_white_256dp, Color.parseColor("#00BCD4")));
        //addSlide(AppIntroFragment.newInstance(getString(R.string.title_intro_3), getString(R.string.description_intro_3), R.drawable.ic_mood_white_256dp, Color.parseColor("#1ab394")));

        // OPTIONAL METHODS
        // Override bar/separator color.
        //setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        //showSkipButton(false);
       // setProgressButtonEnabled(false);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
        //setVibrate(true);
       // setVibrateIntensity(30);
    }
    private void loadMainActivity(){
        PrefsUtils.markTosAccepted(IntroActivity.this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        loadMainActivity();
       // Toast.makeText(getApplicationContext(), getString(R.string.skip), Toast.LENGTH_SHORT).show();
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        loadMainActivity();
    }
    public void getStarted(View v){
        loadMainActivity();
    }
    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
