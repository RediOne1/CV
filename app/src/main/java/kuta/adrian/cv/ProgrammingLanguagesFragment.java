package kuta.adrian.cv;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgrammingLanguagesFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_programming_languages, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.programming_languages_linearLayout);
		List<ProgrammingLanguage> programmingLanguageList = new ArrayList<>();
		programmingLanguageList.add(new ProgrammingLanguage("Java", 5));
		programmingLanguageList.add(new ProgrammingLanguage("Android", 5));
		programmingLanguageList.add(new ProgrammingLanguage("PHP / HTML5", 2));
		programmingLanguageList.add(new ProgrammingLanguage("C / C++", 3));
		programmingLanguageList.add(new ProgrammingLanguage("JavaScript / C#", 1));
		programmingLanguageList.add(new ProgrammingLanguage("MySQL / SQLite", 4));
		ProgrammingLanguagesAdapter programmingLanguagesAdapter = new ProgrammingLanguagesAdapter(getContext(), programmingLanguageList);
		for (int i = 0; i < programmingLanguagesAdapter.getCount(); i++)
			linearLayout.addView(programmingLanguagesAdapter.getView(i, null, null));

	}

	@Override
	public String toString() {
		return "Języki Programowania";
	}
}
