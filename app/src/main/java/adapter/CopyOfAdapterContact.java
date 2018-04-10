package adapter;

import interfac.CallMap;
import interfac.InterfaceOkOrDel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.tools.Tools;
import util.tools.picasso.NetImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cc.Constant;
import com.cc.R;


 
/**
 * @author Walker
 * @date 2017-3-3 下午2:27:37
 * Description: 查找cc用户结果lv适配器
 */
public   class CopyOfAdapterContact extends BaseAdapter    {
	private Context context; // 运行上下文
	private List<Map<String, Object>>  listItems = null; // listview的数据集合
	private LayoutInflater listContainer; // 视图容器
	
	//控件集合实例
	private ViewHolderUser viewHolderUser ;
	private ViewHolderGroup viewHolderGroup ;
	//布局类型
	  final int TYPE_USER = 0;
	  final int TYPE_GROUP = 1;
	  final int TYPE_3 = 2;
	//控件集合
	public final class ViewHolderUser {  
 		public ImageView ivprofile; 
		public ImageView ivsex;
		public TextView tvusername; 
		public TextView tvstatus;  
		public TextView tvsign; 
	}
	public final class ViewHolderGroup { 
		public ImageView ivprofile; 
		public TextView tvusername;  
	}
	

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) { 
		viewHolderUser = null;  
		viewHolderGroup = null;  
		int type = getItemViewType(position);	//得到No.i条数据布局类型
		
		//构建或者取出可复用布局
		if (convertView == null) { //若无可复用布局
			if(type== TYPE_USER){
				viewHolderUser = new ViewHolderUser();
				convertView = listContainer.inflate(R.layout.listview_adapter_contact_user_item, null);	// 获取list_item布局文件的视图
				viewHolderUser.ivsex = (ImageView) convertView .findViewById(R.id.ivsex);
				viewHolderUser.ivprofile = (ImageView) convertView .findViewById(R.id.ivprofile);
				//listItemView.tvid = (TextView) convertView .findViewById(R.id.tvid);
				viewHolderUser.tvusername = (TextView) convertView .findViewById(R.id.tvusername);
				viewHolderUser.tvstatus = (TextView) convertView .findViewById(R.id.tvstatus);
				
				convertView.setTag(viewHolderUser);// 设置控件集到convertView
			}else if(type== TYPE_GROUP){
				viewHolderGroup = new ViewHolderGroup();
				convertView = listContainer.inflate(R.layout.listview_adapter_contact_group_item, null);// 获取list_item布局文件的视图
				viewHolderGroup.ivprofile = (ImageView) convertView .findViewById(R.id.ivprofile);
				viewHolderGroup.tvusername = (TextView) convertView .findViewById(R.id.tvusername);
			
				convertView.setTag(viewHolderGroup);	// 设置控件集到convertView
			}
		} else {//若有可复用布局
			if(type== TYPE_USER){
				viewHolderUser = (ViewHolderUser) convertView.getTag();
			}else if(type== TYPE_GROUP){
				viewHolderGroup = (ViewHolderGroup) convertView.getTag();
			}
		}
		// 设置文字和图片和监听
		if(type== TYPE_USER){ 
			viewHolderUser.tvusername.setText(Tools.getList(listItems, position, "USERNAME").toString()) ;
			viewHolderUser.tvstatus.setText(Tools.getList(listItems, position, "STATUS").toString()) ;
		 	NetImage.loadNetImageInto(context, Tools.getList(listItems, position, "PROFILEPATH").toString(), viewHolderUser.ivprofile);
		 	NetImage.loadImage( context,Tools.getList(listItems, position, "SEX").toString().equals("男") ? R.drawable.boy:R.drawable.girl, viewHolderUser.ivsex);
		}else if(type== TYPE_GROUP){  
			viewHolderGroup.tvusername.setText(Tools.getList(listItems, position, "USERNAME").toString()) ;
		 	NetImage.loadNetImageInto(context, Tools.getList(listItems, position, "PROFILEPATH").toString(), viewHolderGroup.ivprofile);
		}
		 
		return convertView;
	}
	
	//必须实现，通知adapter有几种布局类型
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	//必须实现，让adapter可控布局类型
	@Override
	public int getItemViewType(int position) {
		 if( listItems.get(position).get("TYPE").toString().equals("user")){
			 return TYPE_USER;	
		 }else if( listItems.get(position).get("TYPE").toString().equals("group")){
			 return TYPE_GROUP;	
		 } 
		 return -1;
	}
	
	
	


	public CopyOfAdapterContact(Context context, List<Map<String, Object>> listItems) {
		this.context = context;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.listItems = listItems;
	 
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int i) {
		return listItems.get(i);
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}


 


	 
	

}
