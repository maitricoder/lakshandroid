package com.ielts.lakshacademy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ielts.lakshacademy.Adapter.TestMarksAdapter;
import com.ielts.lakshacademy.ApiHelper.JsonField;
import com.ielts.lakshacademy.ApiHelper.WebUrl;
import com.ielts.lakshacademy.Model.TestMarksData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TestMarksActivity extends AppCompatActivity {

    RecyclerView rvTestMarks;
    TestMarksAdapter testMarksAdapter;
    ArrayList<TestMarksData> testMarksDataArrayList;
    DatePickerDialog datePickerDialog;
    TextView tvFromDate,tvToDate,tvSubmit;
    SessionManager sessionManager;
    HashMap<String,String> studentSessionDetail;
    String student_id;
    String s_date,e_date;
    Locale id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_marks);

        sessionManager = new SessionManager(TestMarksActivity.this,SessionManager.STUDENT_LOGIN_KEY);
        studentSessionDetail = sessionManager.getStudentDataFromSession();
         student_id = studentSessionDetail.get(SessionManager.ID);
        id = new Locale("us","ID");

        getTestScore();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy",id);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd",id);


        //DatePicker Hooks
        tvFromDate = findViewById(R.id.tvFromDate);
        tvToDate = findViewById(R.id.tvToDate);
        tvSubmit=findViewById(R.id.tvSubmit);
        tvFromDate.setText(simpleDateFormat.format(calendar.getTime()));
        tvToDate.setText(simpleDateFormat.format(calendar.getTime()));
        s_date = simpleDateFormat1.format(calendar.getTime());
        e_date = simpleDateFormat1.format(calendar.getTime());


        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(TestMarksActivity.this, s_date+"|"+e_date, Toast.LENGTH_SHORT).show();

                Log.d("start_date",s_date);
                Log.d("start_date",e_date);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.TEST_SCORE_DATE_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJson(response.toString());
                        Toast.makeText(TestMarksActivity.this, response.toString(), Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        Toast.makeText(TestMarksActivity.this,error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String,String> params = new HashMap<>();
                        params.put(JsonField.STUDENT_ID,student_id);
                        params.put(JsonField.TEST_START_DATE,s_date);
                        params.put(JsonField.TEST_END_DATE,e_date);

                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(TestMarksActivity.this);
                requestQueue.add(stringRequest);
            }
        });


        tvFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar  = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy",id);
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd",id);

                datePickerDialog = new DatePickerDialog(TestMarksActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                       calendar.set(i,i1,i2);
                        tvFromDate.setText(simpleDateFormat.format(calendar.getTime()));
                        s_date = simpleDateFormat1.format(calendar.getTime());

                    }
                },year,month,day);

                datePickerDialog.show();
            }
        });

        tvToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar  = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy",id);
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd",id);


                datePickerDialog = new DatePickerDialog(TestMarksActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i,i1,i2);
                        tvToDate.setText(simpleDateFormat.format(calendar.getTime()));
                        e_date = simpleDateFormat1.format(calendar.getTime());

                    }
                },year,month,day);

                datePickerDialog.show();
            }
        });


        testMarksDataArrayList = new ArrayList<>();

//        testMarksDataArrayList.add(new TestMarksData("Reading","20 Apr 2022","50","40"));
//        testMarksDataArrayList.add(new TestMarksData("Listing","22 Apr 2022","50","35"));
//        testMarksDataArrayList.add(new TestMarksData("Speaking","26 Apr 2022","50","25"));
//        testMarksDataArrayList.add(new TestMarksData("Writing","30 Apr 2022","50","26"));
//        testMarksDataArrayList.add(new TestMarksData("Reading","05 Mar 2022","50","46"));
//        testMarksDataArrayList.add(new TestMarksData("Listing","09 Mar 2022","50","50"));
//        testMarksDataArrayList.add(new TestMarksData("Reading","20 Apr 2022","50","40"));
//        testMarksDataArrayList.add(new TestMarksData("Reading","20 Apr 2022","50","40"));
//        testMarksDataArrayList.add(new TestMarksData("Reading","20 Apr 2022","50","40"));
//        testMarksDataArrayList.add(new TestMarksData("Reading","20 Apr 2022","50","40"));

//        setTestMarksDate(testMarksDataArrayList);

    }

    private void setTestMarksDate(ArrayList<TestMarksData> testMarksDataArrayList) {

        rvTestMarks = findViewById(R.id.rvTestMarks);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rvTestMarks.setLayoutManager(layoutManager);
        testMarksAdapter = new TestMarksAdapter(this,testMarksDataArrayList);
        rvTestMarks.setAdapter(testMarksAdapter);
    }

    private void getTestScore(){

//        SessionManager sessionManager = new SessionManager(TestMarksActivity.this,SessionManager.STUDENT_LOGIN_KEY);

//        String student_id = studentSessionDetail.get(SessionManager.ID);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, WebUrl.TEST_SCORE_URL+student_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJson(response.toString());
                Log.d("response",response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(TestMarksActivity.this);
        requestQueue.add(stringRequest);

    }

    private void parseJson(String toString) {
        try {
            JSONObject jsonObject = new JSONObject(toString);
            int flag = jsonObject.optInt(JsonField.FLAG);
            if(flag == 1){

                JSONArray jsonArray = jsonObject.optJSONArray(JsonField.TEST_SCORE_ARRAY);
                if(jsonArray.length()>0){
                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject object = jsonArray.getJSONObject(i);
                        String module_name = object.getString(JsonField.MODULE_NAME);
                        String exam_date = object.getString(JsonField.EXAM_DATE);

//                        Date date = new SimpleDateFormat("dd MMM yyyy").parse(exam_date);

                        String date_exam = object.getString(JsonField.EXAM_DATE);
                        String total_mark = object.getString(JsonField.TOTAL_MARK);
                        String mark = object.getString(JsonField.MARK);
                        String exam_detail_id = object.getString(JsonField.EXAM_DETAIL_ID);
                        String que_paper = object.getString(JsonField.QUE_PAPER);
                        String ans_paper = object.getString(JsonField.ANS_PAPER);

                        TestMarksData testMarksData = new TestMarksData();
                        testMarksData.setModule(module_name);
                        testMarksData.setExam_date(date_exam);
                        testMarksData.setTotal_marks(total_mark);
                        testMarksData.setObtain_marks(mark);
                        testMarksData.setExam_detail_id(exam_detail_id);
                        testMarksData.setQue_paper(que_paper);
                        testMarksData.setAns_key(ans_paper);

                        testMarksDataArrayList.add(testMarksData);
                    }
                    setTestMarksDate(testMarksDataArrayList);
                }
            }else{
                Log.d("no_data","0");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}