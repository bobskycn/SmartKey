package cn.bobsky.smartkey.view;

import org.w3c.dom.Text;

import cn.bobsky.smartkey.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class ItemSwitchView extends LinearLayout {

	public ItemSwitchView(Context context) {
		super(context);
		initView(context);
		// TODO Auto-generated constructor stub
	}

	public ItemSwitchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		// TODO Auto-generated constructor stub
	}

	public ItemSwitchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
		// TODO Auto-generated constructor stub
	}

	private Context mContext;
	private ImageView itemImage;
	private TextView itemTitle;
	private Switch itemSwitch;

	private void initView(Context context) {
		mContext = context;
		LayoutInflater.from(mContext).inflate(R.layout.item_switch, this);
		itemImage = (ImageView) this.findViewById(R.id.item_image);
		itemTitle = (TextView) this.findViewById(R.id.item_title);
		itemSwitch = (Switch) this.findViewById(R.id.item_switch);

	}

	public void newInstance(int resId,CharSequence title,boolean  activated) {
		itemImage.setImageResource(R.drawable.item_stop);
		itemTitle.setText("·þÎñÆô¶¯");
		itemSwitch.setActivated(true);
	}

}
