package com.example.ashok.json;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
    TextView tv;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv=(TextView)findViewById(R.id.textView);
    }
    public void fun(View v)
    {
        new task().execute();
    }


    class task extends AsyncTask<String, String,Void>
    {
        private ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
       //InputStream is = null ;
        String s="";
        protected void onPreExecute()
        {
            progressDialog.setMessage("Fetching data...");
            progressDialog.show();

            progressDialog.setOnCancelListener(new OnCancelListener()
            {
                public void onCancel(DialogInterface arg0)
                {
                    task.this.cancel(true);
                }
            });
        }

        @Override
        protected Void doInBackground(String... params)
        {
            String url="http://date.jsontest.com/";
            try
            {
                URL u=new URL(url);
                URLConnection con=u.openConnection();
                InputStream in=con.getInputStream();
                BufferedReader br=new BufferedReader(new InputStreamReader(in));
                String tmp=br.readLine();
                while(tmp!=null)
                {
                    s=s+tmp;
                    tmp=br.readLine();
                }
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }
            catch(Exception e)
            {
                Log.e("Peers", "Error in http connection "+e.toString());
            }
            return null;
        }

        protected void onPostExecute(Void v)
        {
            try
            {
                JSONObject Jasonobject=new JSONObject(s);
                String time=Jasonobject.getString("time");
                String ms=Jasonobject.getString("milliseconds_since_epoch");
                String date=Jasonobject.getString("date");
                tv.setText("Time = "+time+"\nMilliseconds = "+ms+"\nDate = "+date);
                progressDialog.cancel();
            }
            catch(Exception e)
            {
                Log.e("Peers", "Error parsing data "+e.toString());
            }
        }
    }
}