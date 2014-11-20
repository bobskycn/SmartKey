package cn.bobsky.smartkey.fragment;

import cn.bobsky.smartkey.R;
import cn.bobsky.smartkey.view.ItemContainerView;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentMain extends Fragment {

	private ItemContainerView mContainerView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		mContainerView = (ItemContainerView) rootView
				.findViewById(R.id.itemContainerView1);
		mContainerView.newInstance(1);
		return rootView;
	}

}
