package cn.bobsky.smartkey.view;

import cn.bobsky.smartkey.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class ItemContainerView extends LinearLayout {

	public ItemContainerView(Context context) {
		super(context);
		initView(context);
	}

	public ItemContainerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public ItemContainerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private Context mContext;
	private TextView itemContainerTitle;
	private LinearLayout itemContainer;

	private void initView(Context context) {
		mContext = context;
		LayoutInflater.from(mContext).inflate(R.layout.item_container_layout,
				this);
		itemContainerTitle = (TextView) this
				.findViewById(R.id.item_container_title);
		itemContainer = (LinearLayout) this.findViewById(R.id.item_container);

	}
	public void newInstance(int typeInt) {
		
		itemContainer.removeAllViews();
		itemContainerTitle.setText("µ¥»÷");
//		entryBar.setEntryName(entryName);
//		entryDisplayList.addView(entryBar, 0);
		
		for (int i = 0; i < 5; i++) {
	
			ItemSwitchView mItemSwitchView = new ItemSwitchView(mContext);
			mItemSwitchView.newInstance(1, "title", true);
			itemContainer.addView(mItemSwitchView, i );					
		}		

	}

}
