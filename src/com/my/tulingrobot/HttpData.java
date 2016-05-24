package com.my.tulingrobot;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class HttpData extends AsyncTask<String, Void, String> {
	private HttpClient mHttpClient;
	private HttpGet mHttpGet;
	private HttpResponse mHttpResponse;
	private HttpEntity mHttpEntity;
	private InputStream in;
	private HttpGetDataListener listener;
	//HttpGet��������	
	private String url;
	public HttpData(String url, HttpGetDataListener listener){
		this.url = url;
		this.listener = listener;
	}
	@Override
	protected String doInBackground(String... arg0) {
		try {
			mHttpClient = new DefaultHttpClient();
			//���λ�ÿ�ʼд����HttpGet();�������˺ܴ�����⡣
			mHttpGet = new HttpGet(url);
			mHttpResponse = mHttpClient.execute(mHttpGet);
			mHttpEntity = mHttpResponse.getEntity();
			in = mHttpEntity.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line=br.readLine()) != null) {
				sb.append(line);			
			}
			return sb.toString();
		} catch (Exception e) {
		}
		return null;
	}
	@Override
	protected void onPostExecute(String result) {
		listener.getDataUrl(result);
		super.onPostExecute(result);
	}
}