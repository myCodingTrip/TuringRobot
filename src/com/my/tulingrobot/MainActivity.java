package com.my.tulingrobot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.my.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity implements HttpGetDataListener, OnClickListener{
	private HttpData httpData;
	private List<ListData> lists;
	private ListView lv;
	private EditText sendtext;
	private Button send_btn;
	private String content_str;
	static final String url = "http://www.tuling123.com/openapi/api?key=ecd9d7e5ce6c2596d65f3334afe91895&info=";
	private TextAdapter adapter;
	private String[] welcome_array;
	private long currentTime, oldTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     
        initView();
    }
    private void initView() {
    	sendtext = (EditText) findViewById(R.id.sendText);
    	send_btn = (Button) findViewById(R.id.send_btn);
    	send_btn.setOnClickListener(this);
    	lists = new ArrayList<ListData>();
    	adapter = new TextAdapter(lists, this);
    	lv = (ListView) findViewById(R.id.lv);
    	lv.setAdapter(adapter);
    	ListData listData;
    	listData = new ListData(getRandomWelcomeTips(), ListData.RECEIVER, getTime());
    	lists.add(listData);
	}
    private String getRandomWelcomeTips(){
    	String welcome_tip = null;
    	welcome_array = this.getResources().getStringArray(R.array.welcome_tips);
    	int index = (int) (Math.random()*(welcome_array.length - 1));
    	welcome_tip = welcome_array[index];
    	return welcome_tip;
    }
	@Override
	public void getDataUrl(String data) {
		//System.out.println(data);
		parseText(data);
	}
	//解析json格式数据
	public void parseText(String str){
		try {
			JSONObject jb = new JSONObject(str);
//			System.out.println(jb.getString("code"));
//			System.out.println(jb.getString("text"));
			ListData listData;
			listData = new ListData(jb.getString("text"), ListData.RECEIVER, getTime());
			lists.add(listData);
			adapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	@Override
	public void onClick(View v) {
		//获取发送的内容
		content_str = sendtext.getText().toString();
		sendtext.setText("");
		String dropk = content_str.replace(" ", "");
		String droph = dropk.replace("\n", "");
		ListData listData;
		listData = new ListData(content_str, ListData.SEND, getTime());
		lists.add(listData);
		if(lists.size() > 30){
			for (int i = 0; i < lists.size(); i++) {
				lists.remove(i);
			}
		}
		adapter.notifyDataSetChanged();
		httpData = (HttpData) new HttpData(url+droph,this).execute();
	}
	public String getTime(){
		currentTime = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		Date curDate = new Date();
		String str = format.format(curDate);
		if(currentTime - oldTime >= 5*60*1000){
			oldTime = currentTime;
			return str;
		}
		return "";
	}
}
