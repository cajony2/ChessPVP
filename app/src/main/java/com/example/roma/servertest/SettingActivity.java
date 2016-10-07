package com.example.roma.servertest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Roma on 10/7/2016.
 *
 */
public class SettingActivity extends Activity implements View.OnClickListener {

    Button saveBtn;
    TextView serverIpTV;
    TextView portTV;
    TextView servletNameTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        saveBtn =(Button) findViewById(R.id.SaveButton);
        serverIpTV =(TextView)findViewById(R.id.ServerIpTextView);
        portTV =(TextView)findViewById(R.id.PortTV);
        servletNameTV = (TextView) findViewById(R.id.ServletNameTV);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String serverIp = serverIpTV.getText().toString();
        String port = portTV.getText().toString();
        String servletName = servletNameTV.getText().toString();

        if(TextUtils.isEmpty(serverIp))
            serverIpTV.setError("Cant be empty");
        else if(TextUtils.isEmpty(port))
            portTV.setError("Cant be empty");
        else if (TextUtils.isEmpty(servletName))
            servletNameTV.setError("Cant be empty");
        else{
            String urlSuffix = serverIp+":"+port+"/"+servletName;
            ReadFromDB readFromDB = new ReadFromDB(this,urlSuffix);
            readFromDB.execute();

        }

    }

    class ReadFromDB extends AsyncTask< Void, Void, String> {

        Activity activity;
        ProgressDialog progressDialog;
        String urlString;

        //constructor
        public ReadFromDB(Activity _e,String urlSuffix){
            activity=_e;
            urlString = "http://"+urlSuffix;
            Log.i("chess","serverUrl: "+urlString);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(activity, "", "Checking connection");
        }


        @Override
        protected String doInBackground(Void... params) {
            String message="";
            try{

                URL url = new URL(urlString);

                URLConnection connection = url.openConnection();
                connection.setRequestProperty("Action","createUser");
                Log.d("chess", "checking connection url: "+urlString);
                connection.setDoOutput(true);
                connection.setConnectTimeout(10000);
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());

                out.write("");

                out.close();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String nextLine;

                while ((nextLine = in.readLine()) != null) {
                    message += nextLine;
                }

                Log.d("chess","the return message "+message);
                in.close();
            }
            catch (Exception e1)
            {
                e1.printStackTrace();
                message="timeOut";
            }
            return message;
        }

        @Override
        public void onPostExecute(String message){
            String toastMsg;
            progressDialog.dismiss();
            if(message.equals("ACK"))
                toastMsg = "connection to server was successful";
            else
                toastMsg = "Connection Failed";

            Toast.makeText(activity,toastMsg, Toast.LENGTH_LONG).show();

        }
    }//end of inner class ReadFromDB
}
