package org.bahar.helios;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import java.util.Locale;



public class ChangeCityActivity extends AppCompatActivity {
    private TextView mCityName,mCityId,mres;
    private GridView mCityList;
     private String mSharePref_cityid,mSharePref_cityname;

    String[] mArrayCityname = new String[] {"Tehran","Mashhad","Hamedan","Shiraz","Rasht","Sanandaj","Yazd","Isfahan","tabriz","Zanjan","Semnan","Ahvaz"};
    String[] mArrayCityid = new String[] {"112931","124665","132144","115019","118743","117574","111822","418863","113646","111453","116402","144448"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLocale();
        setContentView(R.layout.activity_change_city);
        getSupportActionBar().setTitle("Setting");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViews();
        init_sharepreference();
        loadData();

    }
    private void setLocale(){
        Locale myLocale = new Locale("en");
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
    private void init_sharepreference(){

        SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("APIPARAM",MODE_PRIVATE);
        mSharePref_cityid= sharedpreferences.getString("cityID", "112931");
        mSharePref_cityname= sharedpreferences.getString("cityname", "Tehran");


    }
    private void findViews() {
        mCityId=findViewById(R.id.rowsetiing_Textview_cityid);
        mCityName=findViewById(R.id.rowsetiing_Textview_cityname);
        mCityList=findViewById(R.id.activitySetting_ListView_CityList);
        mres=findViewById(R.id.activitysetting_TextView_selectedCity);

    }
    private void loadData() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.rowsetting_city_list, R.id.rowsetiing_Textview_cityname, mArrayCityname);
        mCityList.setAdapter(adapter);

        SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("APIPARAM",MODE_PRIVATE);
        mSharePref_cityname= sharedpreferences.getString("cityName", "Tehran");
        mres.setText("City: "+mSharePref_cityname);




        mCityList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityListViewClickedValue = mArrayCityname[position].toString();
                String IDListViewClickedValue = mArrayCityid[position].toString();

                SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("APIPARAM",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("cityID", IDListViewClickedValue);
                editor.putString("cityName", cityListViewClickedValue);
                editor.commit();
                mSharePref_cityname= sharedpreferences.getString("cityName", "Tehran");
                mres.setText("City: "+mSharePref_cityname);

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);}
}
