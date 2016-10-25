package com.myapp.mytvtest.mytvtest.activity;

import android.animation.Animator;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridView;

import com.myapp.mytvtest.mytvtest.R;
import com.myapp.mytvtest.mytvtest.fragment.DomesticFragment;
import com.myapp.mytvtest.mytvtest.fragment.HistoryFragment;
import com.myapp.mytvtest.mytvtest.fragment.HotFragment;
import com.myapp.mytvtest.mytvtest.fragment.OverseasFragment;
import com.open.androidtvwidget.bridge.OpenEffectBridge;
import com.open.androidtvwidget.utils.OPENLOG;
import com.open.androidtvwidget.view.MainUpView;
import com.open.androidtvwidget.view.ReflectItemView;
import com.open.androidtvwidget.view.ViewPagerTV;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //控件
    private TabLayout tabLayout_main;
    private ViewPagerTV viewPager_main;
    //Fragment
    private HotFragment hotFragment=new HotFragment();
    private DomesticFragment domesticFragment=new DomesticFragment();
    private OverseasFragment overseasFragment=new OverseasFragment();
    private HistoryFragment historyFragment=new HistoryFragment();
    //Adapter
    private FragmentPagerAdapter adapter;
    //title和Fragment集合
    private ArrayList<String> titleList=new ArrayList<>();//标题集合
    private ArrayList<Fragment> fragmentList=new ArrayList<>();//Fragmentd集合
    //焦点相关
    private MainUpView mainUpView;//边框
    private OpenEffectBridge mOpenEffectBridge;//边框动画
    private View mOldFocus;//焦点,4.3以下版本需要自己保存.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initAdapter();
//        initViewMove();

        viewPager_main.setAdapter(adapter);//viewPager设置适配器
        tabLayout_main.setupWithViewPager(viewPager_main);//TabLayout与ViewPager关联
//        viewPagerAllListener();//ViewPager全局焦点监听及相关

    }


    @Override
    protected void onResume() {
        viewPagerAllListener();
        super.onResume();
    }

    private void viewPagerAllListener() {
        //ViewPager全局焦点的监听
        viewPager_main.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            //参数列表，参数1旧焦点，参数2新焦点
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                int mPosition=viewPager_main.getCurrentItem();//获取当前的item
                //根据当前的position查找相关页面的边框id，然后获取到它的动画效果
                final MainUpView mainUpView= (MainUpView) fragmentList.get(mPosition).getView().findViewById(R.id.mainUpView);
                final  OpenEffectBridge bridge= (OpenEffectBridge) mainUpView.getEffectBridge();
                // 不是 ReflectitemView 的话.
                if (!(newFocus instanceof ReflectItemView)){
                    OPENLOG.D("onGlobalFocusChanged no ReflectItemView + " + (newFocus instanceof GridView));
                    mainUpView.setUnFocusView(mOldFocus);//设置焦点
                    bridge.setVisibleWidget(true);//隐藏动画
                    mOpenEffectBridge=null;
                }else {
                    OPENLOG.D("onGlobalFocusChanged yes ReflectItemView");
                    newFocus.bringToFront();//将新的焦点设置在前
                    mOpenEffectBridge=bridge;
                    //动画结束后显示边框
                    //防止翻页从另一边跑出来的问题

                    //边框动画的监听
                    bridge.setOnAnimatorListener(new OpenEffectBridge.NewAnimatorListener() {
                        //动画开始时的监听
                        @Override
                        public void onAnimationStart(OpenEffectBridge bridge, View view, Animator animation) {

                        }
                        //动画结束时的监听
                        @Override
                        public void onAnimationEnd(OpenEffectBridge bridge, View view, Animator animation) {

                            if (mOpenEffectBridge==bridge){
                                bridge.setVisibleWidget(false);
                            }
                        }
                    });
                    //根据position设置相关页面放大的倍数
                    float scale=1.0f;//放大的倍数
                    if (mPosition!=0){
                        scale=1.1f;
                    }
                    mainUpView.setFocusView(newFocus,mOldFocus,scale);//设置边框
                }
                mOldFocus=newFocus;
            }
        });
        viewPager_main.setOffscreenPageLimit(4);//画外页的限制
        viewPager_main.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position > 0) {
                    MainUpView mainUpView0 = (MainUpView) fragmentList.get(position - 1).getView().findViewById(R.id.mainUpView);
                    OpenEffectBridge bridge0 = (OpenEffectBridge) mainUpView0.getEffectBridge();
                    bridge0.setVisibleWidget(true);
                }
                //
                if (position < (viewPager_main.getChildCount() - 1)) {
                    MainUpView mainUpView1 = (MainUpView) fragmentList.get(position + 1).getView().findViewById(R.id.mainUpView);
                    OpenEffectBridge bridge1 = (OpenEffectBridge) mainUpView1.getEffectBridge();
                    bridge1.setVisibleWidget(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

//    private void initViewMove() {
//        for (Fragment fragment:fragmentList){
//            MainUpView mainUpView= (MainUpView) findViewById(R.id.mainUpView);
//            mainUpView.setEffectBridge(new EffectNoDrawBridge());
//            mainUpView.setUpRectResource(R.drawable.white_light_10);
//            mainUpView.setDrawUpRectPadding(new Rect(5,5,5,5));//设置边框间距
//            EffectNoDrawBridge bridget= (EffectNoDrawBridge) mainUpView.getEffectBridge();
//            bridget.setTranDurAnimTime(200);//设置动画的时间
//        }
//    }
    private void initData() {
        titleList.add("热门");
        titleList.add("国内");
        titleList.add("海外");
        titleList.add("历史");

        fragmentList.add(hotFragment);
        fragmentList.add(domesticFragment);
        fragmentList.add(overseasFragment);
        fragmentList.add(historyFragment);
    }

    private void initAdapter() {
        //参数列表需要获取一个Fragment管理器 getSupportFragmentManager
        adapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return hotFragment;
                    case 1:
                        return domesticFragment;
                    case 2:
                        return overseasFragment;
                    case 3:
                        return historyFragment;
                    default:
                        throw new RuntimeException("数据异常");
                }

            }
            @Override
            public String getPageTitle(int position) {
                return titleList.get(position);
            }
            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    private void initView() {
        tabLayout_main= (TabLayout) findViewById(R.id.tableLayout_main);
        viewPager_main= (ViewPagerTV) findViewById(R.id.viewPager_main);
        mainUpView= (MainUpView) findViewById(R.id.mainUpView);//边框
    }
}
