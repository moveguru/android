package com.mega.android;

import java.util.ArrayList;
import java.util.List;

import com.mega.sdk.MegaApiAndroid;
import com.mega.sdk.MegaNode;
import com.mega.sdk.NodeList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class FileBrowserGridFragment extends Fragment implements OnClickListener, OnItemClickListener{

	Context context;
	ActionBar aB;
	ListView gridView;
	MegaBrowserGridAdapter adapter;
	
	MegaApiAndroid megaApi;
	NodeList nodes;
	
	ArrayList<Long> historyNodes = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		log("onCreate");

		if (megaApi == null){
			megaApi = ((MegaApplication) ((Activity)context).getApplication()).getMegaApi();
		}

		nodes = megaApi.getChildren(megaApi.getRootNode());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if (aB == null){
			aB = ((ActionBarActivity)context).getSupportActionBar();
		}
		aB.setTitle(getString(R.string.section_cloud_drive));

		View v = inflater.inflate(R.layout.fragment_filebrowsergrid, container, false);
		
		gridView = (ListView) v.findViewById(R.id.file_grid_view_browser);
        gridView.setOnItemClickListener(null);
        gridView.setItemsCanFocus(false);
		adapter = new MegaBrowserGridAdapter(context, nodes);
		adapter.setPositionClicked(-1);
		if (historyNodes != null){
			adapter.setHistoryNodes(historyNodes);
			nodes = megaApi.getChildren(megaApi.getNodeByHandle(historyNodes.get(historyNodes.size()-1)));
			adapter.setNodes(nodes);
		}
		gridView.setAdapter(adapter);
		
		return v;
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        aB = ((ActionBarActivity)activity).getSupportActionBar();
    }
	
	@Override
	public void onClick(View v) {

		switch(v.getId()){

		}
	}
	
	@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
//		Intent i = new Intent(context, FilePropertiesActivity.class);
//		i.putExtra("imageId", items.get(position).getImageId());
//		i.putExtra("name", items.get(position).getName());
//		startActivity(i);
    }
	
	public int onBackPressed(){
		
		ArrayList<Long> historyNodes = adapter.getHistoryNodes();
		
		if (adapter.getPositionClicked() != -1){
			adapter.setPositionClicked(-1);
			adapter.notifyDataSetChanged();
			return 1;
		}
		else if (historyNodes.size() > 1){
			long handle = historyNodes.get(historyNodes.size()-2);
			log("handle a retirar: " + handle);
			historyNodes.remove(historyNodes.size()-1);
			adapter.setHistoryNodes(historyNodes);
			nodes = megaApi.getChildren(megaApi.getNodeByHandle(handle));
			adapter.setNodes(nodes);
			return 2;
		}
		else{
			return 0;
		}
	}
	
	public ArrayList<Long> getHistoryNodes(){
		return adapter.getHistoryNodes();
	}
	
	public void setHistoryNodes(ArrayList<Long> historyNodes){
		
		this.historyNodes = historyNodes;
		if (adapter != null){
			adapter.setHistoryNodes(historyNodes);
		}
	}
	
	private static void log(String log) {
		Util.log("FileBrowserGridFragment", log);
	}

}