package kuta.adrian.cv;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import kuta.adrian.cv.displayingbitmaps.ImageResizer;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


	public HomeFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_home, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ImageResizer imageResizer = ((MainActivity) getActivity()).getImageResizer();
		ImageView me = (ImageView) view.findViewById(R.id.my_image);
		imageResizer.loadImage(R.drawable.me, me);
	}

	@Override
	public String toString() {
		return "Strona główna";
	}
}
