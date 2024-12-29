package ma.ensaj.staysafe10.ui.auth;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import ma.ensaj.staysafe10.R;
import ma.ensaj.staysafe10.ui.auth.adapter.LoginAdapter;

public class LoginRegisterActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    float alpha = 0;
    TabLayout.Tab tabLogin, tabSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);



        tabLogin = tabLayout.newTab().setText("Login");
        tabSignup = tabLayout.newTab().setText("Signup");

        tabLayout.addTab(tabLogin);
        tabLayout.addTab(tabSignup);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Animation setup

        tabLayout.setTranslationY(300);


        tabLayout.setAlpha(alpha);

        // Animate views

        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();

        // Tab selection listener
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}