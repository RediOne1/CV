package kuta.adrian.cv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Author:  Adrian
 * Index:   204423
 * Date:    10.10.2015
 */
public class ProgrammingLanguagesAdapter extends BaseAdapter {

	private List<ProgrammingLanguage> programmingLanguageList;
	private LayoutInflater layoutInflater;

	public ProgrammingLanguagesAdapter(Context context, List<ProgrammingLanguage> programmingLanguageList) {
		this.programmingLanguageList = programmingLanguageList;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return programmingLanguageList.size();
	}

	@Override
	public ProgrammingLanguage getItem(int position) {
		return programmingLanguageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		ProgrammingLanguage programmingLanguage = getItem(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.programming_language_layout, parent, false);
			viewHolder.name = (TextView) convertView.findViewById(R.id.programming_language_name);
			viewHolder.languagesLinearLayout = (LinearLayout) convertView.findViewById(R.id.programming_language_skill_layout);
			convertView.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.name.setText(programmingLanguage.name);


		for (int i = 1; i <= 6; i++) {
			ImageView imageView = new ImageView(layoutInflater.getContext());
			if (i <= programmingLanguage.skill)
				imageView.setImageResource(R.drawable.filled_circle);
			else
				imageView.setImageResource(R.drawable.empty_circle);
				viewHolder.languagesLinearLayout.addView(imageView);
		}
		return convertView;
	}

	public static class ViewHolder {

		public LinearLayout languagesLinearLayout;
		public TextView name;
	}
}
