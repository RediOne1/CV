package kuta.adrian.cv;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;

/**
 * Author:  Adrian
 * Index:   204423
 * Date:    01.10.2015
 */
public class NavigationAdapter extends CacheFragmentStatePagerAdapter {

	private static final Fragment[] fragments = new Fragment[]{new EducationFragment(), new EducationFragment(), new ExperienceFragment(), new ExperienceFragment()};

	public NavigationAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	protected Fragment createItem(int position) {
		// Initialize fragments.
		// Please be sure to pass scroll position to each fragments using setArguments.
		return fragments[position];
	}

	@Override
	public int getCount() {
		return fragments.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return fragments[position].toString();
	}
}
