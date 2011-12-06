package com.facebook.cin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.android.*;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.*;
import com.facebook.cin.exercicio.ShowJason;

public class Face extends ListActivity {

	   private Facebook facebook = new Facebook("147547492015406");
	   private Handler mHandler = new Handler();
	   private SharedPreferences mPrefs;
	   private ArrayAdapter<String> a;

	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setListDataSource();
	       accessFacebook();
	       getFacebookFriends();
	   }

	   public void onActivityResult(int requestCode, int resultCode, Intent data) {
	       super.onActivityResult(requestCode, resultCode, data);
	       facebook.authorizeCallback(requestCode, resultCode, data);
	   }

	       private void setListDataSource() {
	               ListView lv = getListView();
	               a = new ArrayAdapter<String> (this,R.layout.list_item,R.id.tvv);
	               lv.setAdapter(a);
	           lv.setTextFilterEnabled(true);
	       }

	       private void accessFacebook() {
	               mPrefs = getPreferences(MODE_PRIVATE);
	       String access_token = mPrefs.getString("access_token", null);
	       long expires = mPrefs.getLong("access_expires", 0);
	       if(access_token != null) {
	           facebook.setAccessToken(access_token);
	       }
	       if(expires != 0) {
	           facebook.setAccessExpires(expires);
	       }
	       if(!facebook.isSessionValid()) {
	                facebook.authorize(this, new String[] {}, new DialogListener() {
	                public void onComplete(Bundle values) {
	                    SharedPreferences.Editor editor = mPrefs.edit();
	                    editor.putString("access_token", facebook.getAccessToken());
	                    editor.putLong("access_expires", facebook.getAccessExpires());
	                    editor.commit();
	                }
	                public void onFacebookError(FacebookError error) {}
	                public void onError(DialogError e) {}
	                public void onCancel() {}
	             });
	       }
	       }

	       private void getFacebookFriends() {
	               AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
	       mAsyncRunner.request("me", new meRequestListener());
	       mAsyncRunner.request("me/friends", new meFriendsListener());
	       }

	   public class meFriendsListener implements RequestListener {
	       public void onComplete(final String response, Object state) {
	               mHandler.post(new Runnable() {
	                       public void run() {
	                               try {
	                                       updateListDataSourceWithFriends(response);
	                               sortListDataSource();
	                   } catch (JSONException e) {
	                       Log.e("Facebook","Formato não reconhecido: " + e.getMessage());
	                   }
	               }
	                               private void sortListDataSource() {
	                                       a.sort(null);
	                               }
	                               private void updateListDataSourceWithFriends(
	                                               final String response) throws JSONException {
	                                       JSONArray js;
	                                       js = new JSONObject(response).getJSONArray("data");
	                                       
	                                       // Imprimir Objeto JSON
	                                       
	                                       ShowJason.getInstance().printjson(js);
	                                       
	                                       for (int i = 0; i < js.length(); i++) {
	                                               a.add((String) js.getJSONObject(i).get("name"));
	                                       }
	                               }
	            });
	       }
	       public void onFacebookError(FacebookError e, final Object state) {}
	       public void onFileNotFoundException(FileNotFoundException e,
	               final Object state) {}
	       public void onIOException(IOException e, final Object state) {}
	       public void onMalformedURLException(MalformedURLException e,
	               final Object state) {}
	   }

	   public class meRequestListener implements RequestListener {
	       public void onComplete(String response, Object state) {}
	       public void onFacebookError(FacebookError e, Object state) {}
	       public void onFileNotFoundException(FileNotFoundException e, Object state) {}
	       public void onIOException(IOException e, Object state) {}
	       public void onMalformedURLException(MalformedURLException e, Object state) {}
	   }
	}
