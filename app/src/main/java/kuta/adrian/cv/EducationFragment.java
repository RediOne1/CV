package kuta.adrian.cv;


import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import kuta.adrian.cv.displayingbitmaps.ImageResizer;


/**
 * A simple {@link Fragment} subclass.
 */
public class EducationFragment extends Fragment implements OnMapReadyCallback {

	private GoogleMap mMap;
	private CardView university, secondarySchool;
	private Marker wroclawUniversityMarker, secondarySchoolMarker;
	private ScrollView scrollView;
	private int currentScrollY = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_education, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ImageResizer imageResizer = ((MainActivity) getActivity()).getImageResizer();
		SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		scrollView = (ScrollView) view.findViewById(R.id.scroll);
		scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
			@Override
			public void onScrollChanged() {
				currentScrollY = scrollView.getScrollY();
				university.setTranslationY(currentScrollY);
				secondarySchool.setTranslationY(currentScrollY / 2);
			}
		});

		ImageView transparentImageView = (ImageView) view.findViewById(R.id.transparent_image);

		transparentImageView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
					case MotionEvent.ACTION_DOWN:
						// Disallow ScrollView to intercept touch events.
						scrollView.requestDisallowInterceptTouchEvent(true);
						// Disable touch on transparent view
						return false;

					case MotionEvent.ACTION_UP:
						// Allow ScrollView to intercept touch events.
						scrollView.requestDisallowInterceptTouchEvent(false);
						return true;

					case MotionEvent.ACTION_MOVE:
						scrollView.requestDisallowInterceptTouchEvent(true);
						return false;

					default:
						return true;
				}
			}
		});

		university = (CardView) view.findViewById(R.id.university_cardView);
		university.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentScrollY > 100)
					scrollTo(0);
				else {
					animateCameraTo(wroclawUniversityMarker.getPosition());
					scrollTo(scrollView.getBottom());
				}
			}
		});
		secondarySchool = (CardView) view.findViewById(R.id.secondary_school_cardView);
		secondarySchool.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentScrollY > 100)
					scrollTo(0);
				else {
					animateCameraTo(secondarySchoolMarker.getPosition());
					scrollTo(scrollView.getBottom());
				}
			}
		});

		ImageView universityImage = (ImageView) view.findViewById(R.id.university_image);
		imageResizer.loadImage(R.drawable.pwr, universityImage);

		ImageView secondaryImage = (ImageView) view.findViewById(R.id.secondary_image);
		imageResizer.loadImage(R.drawable.ilo, secondaryImage);
	}

	private void animateCameraTo(LatLng position) {
		CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(position, 17, 50, 0));
		mMap.animateCamera(cameraUpdate, 3000, null);
	}

	private void scrollTo(int scrollY) {
		ValueAnimator valueAnimator = ValueAnimator.ofInt(currentScrollY, scrollY);
		valueAnimator.setDuration(500);
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int progress = (int) animation.getAnimatedValue();

				scrollView.scrollTo(0, progress);
			}
		});
		valueAnimator.start();
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		mMap.setBuildingsEnabled(true);
		mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				centerCamera();
			}
		});

		LatLng poland = new LatLng(51.8402427, 14.6461145);

		mMap.moveCamera(CameraUpdateFactory.newLatLng(poland));

		LatLng wroclawUniversity = new LatLng(51.1086759, 17.0579691);
		MarkerOptions markerOptions = new MarkerOptions()
				.position(wroclawUniversity)
				.title(getString(R.string.wroclawUniversity));
		wroclawUniversityMarker = mMap.addMarker(markerOptions);

		LatLng secondarySchool = new LatLng(51.5375181, 19.9985359);
		MarkerOptions markerOptions1 = new MarkerOptions()
				.position(secondarySchool)
				.title(getString(R.string.I_LO_in_tomaszow))
				.snippet(getString(R.string.in_tomaszow_maz));
		secondarySchoolMarker = mMap.addMarker(markerOptions1);
		centerCamera();
	}

	private void centerCamera() {
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		builder.include(wroclawUniversityMarker.getPosition());
		builder.include(secondarySchoolMarker.getPosition());

		LatLngBounds bounds = builder.build();
		int padding = 80; //offset from edges of the map in pixels
		CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
		mMap.animateCamera(cu, 1500, null);
	}

	@Override
	public String toString() {
		return "Edukacja";
	}
}
