package com.example.realcreation;

import android.os.AsyncTask;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.amazonaws.auth.CognitoCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;

    home_jfragment hFrag;
    nearby_jfragment nFrag;
    profile_jfragment pFrag;
    MenuItem prevMenuItem;

    private static final String TAG = Credential.class.getSimpleName();
    private CognitoCredentialsProvider cognitoCredentialsProvider;
    String id;
    String TableName = "Users";

    CognitoUserSession cognitoUserSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        final BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navView);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home_button:
                                viewPager.setCurrentItem(0);
                                break;

                            case R.id.location_button:
                                viewPager.setCurrentItem(1);
                                break;

                            case R.id.profile_button:
                                viewPager.setCurrentItem(2);
                                break;

                        }
                        return false;
                    }
                });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);
    }

    private void init(){
        cognitoUserSession = AppHelper.getCurrSession();
        new Credential().execute();
        databaseRetrieve();
    }

    private void databaseRetrieve() {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient(cognitoCredentialsProvider);
        AttributeValue attributeValue = new AttributeValue("1");
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("Id", attributeValue);
        client.getItem(TableName, key);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        hFrag = new home_jfragment();
        nFrag = new nearby_jfragment();
        pFrag = new profile_jfragment();
        adapter.addFragment(hFrag);
        adapter.addFragment(nFrag);
        adapter.addFragment(pFrag);
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                Toast.makeText(this,"Search",Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
        }
        return true;
    }


    private class Credential extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            cognitoCredentialsProvider = AppHelper.getCognitoCredentialsProvider();
            id = cognitoCredentialsProvider.getIdentityId();
            return objects;
        }
        @Override
        protected void onPostExecute(Object o) {
            Log.d(TAG, "Done ");
        }
    }
}
