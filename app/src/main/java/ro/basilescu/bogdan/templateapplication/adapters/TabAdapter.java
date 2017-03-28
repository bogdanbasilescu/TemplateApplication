package ro.basilescu.bogdan.templateapplication.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ro.basilescu.bogdan.templateapplication.R;
import ro.basilescu.bogdan.templateapplication.fragments.FirebaseFragment;
import ro.basilescu.bogdan.templateapplication.fragments.RetrofitFragment;
import ro.basilescu.bogdan.templateapplication.fragments.SQLiteFragment;

/**
 * Class for TabAdapter - FragmentPagerAdapter
 */
public class TabAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
    public enum Tab {

        // Define Tab items for the given constructor
        SqliteDatabase(R.string.tab1_title, R.mipmap.ic_launcher, R.string.tab1_title),
        FirebaseDatabase(R.string.tab2_title, R.mipmap.ic_launcher, R.string.tab2_title),
        Retrofit(R.string.tab3_title, R.mipmap.ic_launcher, R.string.tab3_title);

        // Define enum attributes
        public int titleResource;
        public int iconResource;
        public int labelResource;

        // Define constructor for Tab enum type
        Tab(int titleResource, int iconResource, int labelResource) {
            this.titleResource = titleResource;
            this.iconResource = iconResource;
            this.labelResource = labelResource;
        }
    }

    private Context mContext;
    private TabLayout mTabLayout;
    private TextView mActivityTitle;

    /**
     * Constructor
     *
     * @param fm            FragmentManager for FragmentPagerAdapter
     * @param context       Context
     * @param activityTitle ActivityTitle
     */
    public TabAdapter(@NonNull FragmentManager fm, @NonNull Context context, TextView activityTitle) {
        super(fm);
        mContext = context;
        mActivityTitle = activityTitle;
    }

    /**
     * Set Tab layout
     *
     * @param tabLayout The layout for the tab
     */
    public void setTabLayout(TabLayout tabLayout) {
        mTabLayout = tabLayout;
    }

    /**
     * Get fragment depending on the selected item
     *
     * @param position The position in the adapter
     * @return The instance of the selected fragment
     */
    @Override
    public Fragment getItem(int position) {
        if (position < 0 || position >= Tab.values().length)
            return null;

        switch (Tab.values()[position]) {
            case SqliteDatabase:
                return SQLiteFragment.newInstance();
            case FirebaseDatabase:
                return FirebaseFragment.newInstance();
            case Retrofit:
                return RetrofitFragment.newInstance();
        }
        return null;
    }

    /**
     * Get the number of items in the Tab
     *
     * @return the number of Tab items
     */
    @Override
    public int getCount() {
        return Tab.values().length;
    }

    /**
     * Get the title of the page depending on the selected fragment
     *
     * @param position The position in the adapter
     * @return The title of the selected fragment for the page
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(Tab.values()[position].titleResource);
    }

    /**
     * Set the layout for a tab item depending on the position of it in the adapter
     *
     * @param position The position in the adapter
     * @return The view that is inflated with the custom layout
     */
    public View getCustomView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tab_header_view, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(Tab.values()[position].titleResource);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        icon.setImageResource(Tab.values()[position].iconResource);
        return view;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * Change color for the title view from the Tab's layout when the page
     * corresponding to the Tab has been selected
     *
     * @param position The position in the adapter
     */
    @Override
    public void onPageSelected(int position) {
        int unselectedColor = ContextCompat.getColor(mContext, R.color.colorPrimaryDark);
        int selectedColor = ContextCompat.getColor(mContext, R.color.colorAccent);

        for (int i = 0; i < Tab.values().length; i++) {
            boolean selected = i == position;
            View view = mTabLayout.getTabAt(i).getCustomView();
            if (view != null) {
                TextView title = (TextView) view.findViewById(R.id.title);
                title.setTextColor(selected ? selectedColor : unselectedColor);
                ImageView icon = (ImageView) view.findViewById(R.id.icon);
                icon.setColorFilter(selected ? selectedColor : unselectedColor);
            }
        }
//        // Set activity title when selecting tab
//        mActivityTitle.setText(Tab.values()[position].titleResource);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
