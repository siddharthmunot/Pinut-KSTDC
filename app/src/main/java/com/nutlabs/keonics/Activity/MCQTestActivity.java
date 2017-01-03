package com.nutlabs.keonics.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nutlabs.keonics.Communicator;
import com.nutlabs.keonics.R;
import com.nutlabs.keonics.SharedConstants;
import com.nutlabs.keonics.Temporary_sub_chapter_Activities.Activity1GridView;
import com.nutlabs.keonics.kore.ui.BaseActivity;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubham on 10/7/2016.
 */

public class MCQTestActivity extends BaseActivity {
    private TextView quizQuestion;

    private RadioGroup radioGroup;
    private RadioButton optionOne;
    private RadioButton optionTwo;
    private RadioButton optionThree;
    private RadioButton optionFour;
    private Button submitButton;
    public int count = 0;

    private int currentQuizQuestion;
    private int quizCount;
    MCQTask mMCQtask;
    Communicator mCommunicator;

    private QuizWrapper firstQuestion;
    private List<QuizWrapper> parsedObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.default_toolbar);
        setSupportActionBar(toolbar);

       /* ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);


        actionBar.setTitle(R.string.app_name);*/

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        quizQuestion = (TextView) findViewById(R.id.quiz_question);

        mCommunicator = Communicator.getInstance(this);


        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        optionOne = (RadioButton) findViewById(R.id.radio0);
        optionTwo = (RadioButton) findViewById(R.id.radio1);
        optionThree = (RadioButton) findViewById(R.id.radio2);
        optionFour = (RadioButton) findViewById(R.id.radio3);
        submitButton = (Button) findViewById(R.id.submit);

        final Button previousButton = (Button) findViewById(R.id.previousquiz);
        final Button nextButton = (Button) findViewById(R.id.nextquiz);

        AsyncJsonObject asyncObject = new AsyncJsonObject();
        asyncObject.execute("");

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int radioSelected = radioGroup.getCheckedRadioButtonId();
                int userSelection = getSelectedAnswer(radioSelected);

                int correctAnswerForQuestion = firstQuestion.getCorrectAnswer();

                if (userSelection == correctAnswerForQuestion) {
                    // correct answer

                    Toast.makeText(MCQTestActivity.this, "You got the answer correct", Toast.LENGTH_LONG).show();
                    currentQuizQuestion++;
                    previousButton.setVisibility(View.VISIBLE);
                    if (currentQuizQuestion >= quizCount) {
                        nextButton.setVisibility(View.GONE);
                        submitButton.setVisibility(View.VISIBLE);
                        Toast.makeText(MCQTestActivity.this, "End of the Quiz Questions", Toast.LENGTH_LONG).show();

                        return;
                    } else {
                        firstQuestion = parsedObject.get(currentQuizQuestion);
                        quizQuestion.setText(firstQuestion.getQuestion());
                        String[] possibleAnswers = firstQuestion.getAnswers().split(",");
                        uncheckedRadioButton();
                        optionOne.setText(possibleAnswers[0]);
                        optionTwo.setText(possibleAnswers[1]);
                        optionThree.setText(possibleAnswers[2]);
                        optionFour.setText(possibleAnswers[3]);
                    }
                } else {
                    // failed question
                    count++;
                    Toast.makeText(MCQTestActivity.this, "You chose the wrong answer" + count, Toast.LENGTH_LONG).show();

                    return;
                }
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuizQuestion--;
                if (currentQuizQuestion < 0) {
                    return;
                }
                uncheckedRadioButton();
                firstQuestion = parsedObject.get(currentQuizQuestion);
                quizQuestion.setText(firstQuestion.getQuestion());
                String[] possibleAnswers = firstQuestion.getAnswers().split(",");
                optionOne.setText(possibleAnswers[0]);
                optionTwo.setText(possibleAnswers[1]);
                optionThree.setText(possibleAnswers[2]);
                optionFour.setText(possibleAnswers[3]);
            }
        });
        addListenerOnButtonClick();

    }

    public void addListenerOnButtonClick() {

        submitButton = (Button) findViewById(R.id.submit);
        //Performing action on Button Click
        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                mMCQtask = new MCQTask();
                mMCQtask.execute((Void) null);
                finish();


            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!navigationDrawerFragment.isDrawerOpen()) {
//            getMenuInflater().inflate(R.menu.media_info, menu);
//        }

        // getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem menu1 = menu.add("HOME");
        menu1.setIcon(R.drawable.home_icon);

        menu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MCQTestActivity.this, Activity1GridView.class);
                startActivity(intent);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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
    }


    private class AsyncJsonObject extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            HttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
            HttpPost httpPost = new HttpPost("http://192.168.0.1/jsonmcq.json");
            String jsonResult = "";

            try {
                HttpResponse response = httpClient.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
                System.out.println("Returned Json object " + jsonResult.toString());

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return jsonResult;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MCQTestActivity.this, "Downloading Quiz", "Wait....", true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            System.out.println("Resulted Value: " + result);
            parsedObject = returnParsedJsonObject(result);
            if (parsedObject == null) {
                return;
            }
            quizCount = parsedObject.size();
            firstQuestion = parsedObject.get(0);

            quizQuestion.setText(firstQuestion.getQuestion());
            String[] possibleAnswers = firstQuestion.getAnswers().split(",");
            optionOne.setText(possibleAnswers[0]);
            optionTwo.setText(possibleAnswers[1]);
            optionThree.setText(possibleAnswers[2]);
            optionFour.setText(possibleAnswers[3]);
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = br.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return answer;
        }
    }

    private List<QuizWrapper> returnParsedJsonObject(String result) {

        List<QuizWrapper> jsonObject = new ArrayList<QuizWrapper>();
        JSONObject resultObject = null;
        JSONArray jsonArray = null;
        QuizWrapper newItemObject = null;

        try {
            resultObject = new JSONObject(result);
            System.out.println("Testing the water " + resultObject.toString());
            jsonArray = resultObject.optJSONArray("quiz_questions");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonChildNode = null;
            try {
                jsonChildNode = jsonArray.getJSONObject(i);
                int id = jsonChildNode.getInt("id");
                String question = jsonChildNode.getString("question");
                String answerOptions = jsonChildNode.getString("possible_answers");
                int correctAnswer = jsonChildNode.getInt("correct_answer");
                newItemObject = new QuizWrapper(id, question, answerOptions, correctAnswer);
                jsonObject.add(newItemObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    private int getSelectedAnswer(int radioSelected) {

        int answerSelected = 0;
        if (radioSelected == R.id.radio0) {
            answerSelected = 1;
        }
        if (radioSelected == R.id.radio1) {
            answerSelected = 2;
        }
        if (radioSelected == R.id.radio2) {
            answerSelected = 3;
        }
        if (radioSelected == R.id.radio3) {
            answerSelected = 4;
        }
        return answerSelected;
    }

    private void uncheckedRadioButton() {
        optionOne.setChecked(false);
        optionTwo.setChecked(false);
        optionThree.setChecked(false);
        optionFour.setChecked(false);
    }


    public class MCQTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
               /* String comment="";
                if (etComment.getText().toString()!=null || etComment.getText().toString().length()>0)
                    comment=etComment.getText().toString();
                Log.d(SharedConstants.TAG, "doinbackground is called for feedback api");
                int retVal = mCommunicator.feeadback((int)rbRideExprnc.getRating(),(int)rbPinutExprnc.getRating(),comment);
                if (retVal == HttpStatus.SC_OK) {
                    Log.d(SharedConstants.TAG, "Feedback API successfull");*/
             //  int mcount = Integer.parseInt("");

                String mcount="";
                if (count != 0 || count>0)
                    //mcount = count;
               // mcount = count;
                mcount= String.valueOf(count);
                Log.d(SharedConstants.TAG, "doinbackground is called for feedback api");
                int ncount = mCommunicator.mcqresult( mcount, count);
                if (ncount == HttpStatus.SC_OK) {
                    Log.d(SharedConstants.TAG, "Feedback API succesfull");
                } else {
                    Log.d(SharedConstants.TAG, "Feedback API call failed");

                }

                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
