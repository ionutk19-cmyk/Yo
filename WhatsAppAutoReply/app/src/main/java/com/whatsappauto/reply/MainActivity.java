package com.whatsappauto.reply;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.whatsappauto.reply.databinding.ActivityMainBinding;
import com.whatsappauto.reply.fragments.AutoReplyFragment;
import com.whatsappauto.reply.fragments.ScheduledMessageFragment;
import com.whatsappauto.reply.services.MessageSchedulerService;
import com.whatsappauto.reply.utils.WhatsAppHelper;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int currentTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        setupViewPager();
        setupFab();

        // Start the message scheduler service
        startService(new Intent(this, MessageSchedulerService.class));

        // Check if accessibility service is enabled
        checkAccessibilityService();
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabs, binding.viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Auto Reply");
                            break;
                        case 1:
                            tab.setText("Scheduled Messages");
                            break;
                    }
                }
        ).attach();

        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupFab() {
        binding.fab.setOnClickListener(view -> {
            Fragment currentFragment = getSupportFragmentManager()
                    .findFragmentByTag("f" + currentTab);
            
            if (currentFragment instanceof AutoReplyFragment) {
                ((AutoReplyFragment) currentFragment).showAddDialog();
            } else if (currentFragment instanceof ScheduledMessageFragment) {
                ((ScheduledMessageFragment) currentFragment).showAddDialog();
            }
        });
    }

    private void checkAccessibilityService() {
        if (!WhatsAppHelper.isAccessibilityServiceEnabled(this)) {
            Toast.makeText(this, "Please enable accessibility service", Toast.LENGTH_LONG).show();
            WhatsAppHelper.openAccessibilitySettings(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            WhatsAppHelper.openAccessibilitySettings(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new AutoReplyFragment();
                case 1:
                    return new ScheduledMessageFragment();
                default:
                    return new AutoReplyFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}