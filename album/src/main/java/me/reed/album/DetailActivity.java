/*
 * Copyright 2016 Reed.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.reed.album;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author reed
 */
class DetailActivity extends BaseActivity {

    static final String PICTURES = "pictures";
    static final String POSITION = "position";

    private Toolbar detailToolbar;

    private ViewPager detailViewPager;

    private List<String> paths;

    private List<Fragment> fragments;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        paths = intent.getStringArrayListExtra(PICTURES);
        position = intent.getIntExtra(POSITION, -1);
        if (paths == null || position == -1) {
            finish();
        }
        fragments = new ArrayList<>();
        for (String path : paths) {
            Fragment fragment = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString(DetailFragment.PICTURE_PATH, path);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        initView();

    }

    private void initView() {
        detailToolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        detailToolbar.setTitle((position + 1) + "/" + paths.size());
        setSupportActionBar(detailToolbar);
        detailViewPager = (ViewPager) findViewById(R.id.viewPager_detail);
        detailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (AlbumUtil.toolbarColor != AlbumUtil.COLOR_DEFAULT) {
            detailToolbar.setBackgroundColor(AlbumUtil.toolbarColor);
        }
        if (AlbumUtil.textColor != AlbumUtil.COLOR_DEFAULT) {
            detailToolbar.setTitleTextColor(AlbumUtil.textColor);
        }
        detailViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                detailToolbar.setTitle((position + 1) + "/" + paths.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {
        DetailFragmentAdapter adapter = new DetailFragmentAdapter(getSupportFragmentManager(), fragments);
        detailViewPager.setAdapter(adapter);
        detailViewPager.setCurrentItem(position);
    }
}
