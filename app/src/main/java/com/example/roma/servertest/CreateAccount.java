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
 * Created by Roma on 8/27/2016.
 * create new account  get  user name and psw from user send to server and parse response
 *  server response:
 *  "user name taken" -> user name taken :/
 *  "userCreated"  -> all good user created!
 *  else -> something went wrong.
 */
public class CreateAccount extends Activity implements View.OnClickListener{

    TextView userName;
    TextView psw;
    TextView rePsw;
    Button createBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        userName = (TextView) findViewById(R.id.userName);
        psw = (TextView) findViewById(R.id.psw);
        rePsw = (TextView) findViewById(R.id.re_psw);

        createBtn = (Button) findViewById(R.id.create_account);

        createBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String nameString = userName.getText().toString();
        String pswString = psw.getText().toString();
        String rePswString = rePsw.getText().toString();

        if(TextUtils.isEmpty(nameString))
            userName.setError("Cant be empty");
        else if(TextUtils.isEmpty(pswString))
            psw.setError("Cant be empty");
        else if (TextUtils.isEmpty(rePswString))
            rePsw.setError("Cant be empty");
        else if (!pswString.equals(rePswString))
            rePsw.setError("Password do not match");
        else{
            ReadFromDB readFromDB = new ReadFromDB(this,nameString,pswString);
            readFromDB.execute();

        }
    }

    class ReadFromDB extends AsyncTask< Void, Void, String> {

        Activity activity;
        ProgressDialog progressDialog;
        String name;
        String psw;

        //constructor
        public ReadFromDB(Activity _e,String _name, String _psw){
            activity=_e;
            name=_name;
            psw=_psw;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(activity, "", "Creating User...");
        }


        @Override
        protected String doInBackground(Void... params) {
            String message="";
            try{
                URL url = new URL("http://5.29.207.103:8080/Chess/ChessServlet");
                // URL url = new URL("http://10.0.2.2:8080/Chess/ChessServlet");

                URLConnection connection = url.openConnection();
                connection.setRequestProperty("Action","createUser");
                Log.d("chess", "connecting to db: createUser");

                connection.setRequestProperty("UserName", name);
                connection.setRequestProperty("Password", psw);
                connection.setDoOutput(true);

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
            }
            return message;
        }

        @Override

        /*
        check the msg from server  if msg is "user name taken"
         */
        public void onPostExecute(String message){
            Log.i("chess",message);
            progressDialog.dismiss();
            //check returend msg from server
            switch (message) {
                case "user name taken":                 // is message is error then account no such account
                    Toast.makeText(activity, "User name \"" + name + "\" is taken.", Toast.LENGTH_LONG).show();
                    break;
                case "userCreated":                                       // else msg is json with use info show personal info activity
                    Toast.makeText(activity, "User Created", Toast.LENGTH_LONG).show();
                    activity.finish();
                    break;
                default:
                    Toast.makeText(activity, "Oops somthing whent wrong :(", Toast.LENGTH_LONG).show();
                    break;
            }

        }
    }//end of inner class ReadFromDB
}
