package com.utbm.georace.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.utbm.georace.R;
import com.utbm.georace.model.Race;
import com.utbm.georace.model.Track;
import com.utbm.georace.model.User;
import com.utbm.georace.tools.Globals;
import com.utbm.georace.tools.WebService;

import java.util.Date;

public class CreateAccountActivity extends Activity {
    User newUser;
    BuildNewUserTasks newUserBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Button mValiderButton = (Button) findViewById(R.id.validerCreationUser);
        mValiderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView mLoginTV = (TextView) findViewById(R.id.userLogin);
                String userLogin = mLoginTV.getText().toString();
                TextView mPassword1TV = (TextView) findViewById(R.id.userPassword1);
                String userPassword1 = mPassword1TV.getText().toString();
                TextView mPassword2TV = (TextView) findViewById(R.id.userPassword2);
                String userPassword2 = mPassword2TV.getText().toString();
                TextView mFirstNameTV = (TextView) findViewById(R.id.userPrenom);
                String userFirstName = mFirstNameTV.getText().toString();
                TextView mLastNameTV = (TextView) findViewById(R.id.userNom);
                String userLastName = mLastNameTV.getText().toString();
                TextView mEmailTV = (TextView) findViewById(R.id.userEmail);
                String userEmail = mEmailTV.getText().toString();

                if (userLogin.isEmpty() || userPassword1.isEmpty() || userPassword2.isEmpty() ||
                        userFirstName.isEmpty() || userLastName.isEmpty() || userEmail.isEmpty()){

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Vous n'avez pas renseigné tous les champs",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    if (!userPassword1.equals(userPassword2)){
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Vous n'avez pas renseigné des mots de passes identiques",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }else{
                        if (userPassword1.length() < 4){
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Le mot de passe doit être  d'au minimum 4 caractères.",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }else{
                            newUser = new User(userLogin,userPassword1,userFirstName,userLastName,userEmail);
                            newUserBuilder = new BuildNewUserTasks();
                            newUserBuilder.execute();
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Votre compte a bien été créé",
                                    Toast.LENGTH_LONG);
                            toast.show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getApplication());
                            finish();
                        }
                    }
                }
            }
        });
    }

    class BuildNewUserTasks extends AsyncTask<Void,Void,Boolean> {


        @Override
        protected Boolean doInBackground(Void... voids) {
            WebService ws = WebService.getInstance();
            ws.setUser(newUser);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            /*après l'execution du poste on devrait récupérer la race qui a été crée et remplacé la
            * Globals Current Race par celle ci.*/
        }
    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
