package cn.bobsky.smartkey.fragment;


import cn.bobsky.smartkey.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentMain extends Fragment {

	public FragmentMain() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 View rootView = inflater.inflate(R.layout.fragment_main, container, false);
         return rootView;
	}

}
