package ua.com.techinservice.tabs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	final String LOG_TAG = "myLogs";
	// Departments
	final String DEPARTMENT[] ={ "directors", "administrative", "automatic", "accounting",
								 "drivers", "logistics", "mechanics", "programmers",
								 "biotechnology", "heating", "technical", "security"};
	
	//���������� �������
	final int NUMBER_OF_TABS = 7;
	
	// ����� ��������� ��� Map
	final String ATTRIBUTE_SURNAME_TEXT = "ATTRIBUTE_SURNAME_TEXT";
	final String ATTRIBUTE_NAME_TEXT = "ATTRIBUTE_NAME_TEXT";
	final String ATTRIBUTE_INPHONE_TEXT = "ATTRIBUTE_INPHONE_TEXT";
	final String ATTRIBUTE_OUTPHONE_TEXT = "ATTRIBUTE_OUTPHONE_TEXT";
	
	// ������ ���� ���������, �� ������� ����� �������� ������
    final String[] from = {ATTRIBUTE_SURNAME_TEXT, ATTRIBUTE_NAME_TEXT, ATTRIBUTE_INPHONE_TEXT, ATTRIBUTE_OUTPHONE_TEXT};
    // ������ ID View-�����������, � ������� ����� ��������� ������
    final int[] to = {R.id.tvSurname, R.id.tvName, R.id.tvInPhone, R.id.tvOutPhone};
	
	DataBaseHelper dbHelper;
	
	ListView lvSimple, lvAdministrative;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
		
		 // ������� ������ ��� �������� � ���������� �������� ��
	    dbHelper = new DataBaseHelper(this);
		
	    try {
	    	dbHelper.createDataBase();
	    } catch (IOException ioe) {
	    	throw new Error("Unable to create database");
	    }
	    
		// ������� ������
//		String[] surname = {"����������", "���������", "����������", "�������"};
//		String[] name = {"�������� ����������", "����� ����������", "������ �������������", "������� �����������"};
//		String[] inPhone = {"134", "222", "143", "104"};
//		String[] outPhone = {"050-351-14-96", "050-312-92-12", "068-701-72-41", "063-145-97-68"};

	    
        for (int i = 0; i < NUMBER_OF_TABS; i++) {
	    	//������������ � ��
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			String selection = "department = '" + DEPARTMENT[i] + "'";			
			// ������ ������ ������ ������ �� ������� phones, �������� Cursor 
			Cursor c = db.query("phones", null, selection, null, null, null, null);
			
			// ����������� ������ � �������� ��� �������� ���������
		    ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		    Map<String, String> m;
			
			// ������ ������� ������� �� ������ ������ �������
		    // ���� � ������� ��� �����, �������� false
		    if (c.moveToFirst()) {
		    	
		    	// ���������� ������ �������� �� ����� � �������
		        int idColIndex = c.getColumnIndex("_id");
		        int surnameColIndex = c.getColumnIndex("surname");
		        int nameColIndex = c.getColumnIndex("name");
		        int inPhoneColIndex = c.getColumnIndex("inPhone");
		        int outPhoneColIndex = c.getColumnIndex("outPhone");
		        do {
		        	// �������� �������� �� ������� �������� � ����� ��� � ���
			        Log.d(LOG_TAG,
			        "ID = " + c.getInt(idColIndex) + 
			        ", surname = " + c.getString(surnameColIndex) + 
			        ", name = " + c.getString(nameColIndex));
			        
			        m = new HashMap<String, String>();
			        m.put(ATTRIBUTE_SURNAME_TEXT, c.getString(surnameColIndex));
			        m.put(ATTRIBUTE_NAME_TEXT, c.getString(nameColIndex));
			        m.put(ATTRIBUTE_INPHONE_TEXT, c.getString(inPhoneColIndex));
			        m.put(ATTRIBUTE_OUTPHONE_TEXT, c.getString(outPhoneColIndex));
			        data.add(m);
			        // ������� �� ��������� ������ 
			        // � ���� ��������� ��� (������� - ���������), �� false - ������� �� �����
			        } while (c.moveToNext());
			    } else
			    	Log.d(LOG_TAG, "0 rows");
			    c.close();
			    dbHelper.close();  
		    // ������� �������
	        SimpleAdapter sAdapter = new SimpleAdapter(this, data, R.layout.my_list_item, from, to);
	        // ���������� ������ � ����������� ��� �������
	        lvSimple = (ListView) findViewById(0x7f070000 + i);
	        lvSimple.setAdapter(sAdapter);
	    }
        // onClick administrative List (Tab1)
        lvAdministrative = (ListView) findViewById(R.id.lvTab1);
        lvAdministrative.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
            	String outPhone = ((TextView) view.findViewById(R.id.tvOutPhone)).getText().toString();
            	
            	//parent.getAdapter().getItem(position).toString()
              Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "
                  + id + ", outPhone = " + outPhone);
            }
          });
        
        // ������������� �������
        tabHost.setup();
        TabHost.TabSpec tabSpec;
        
        for (int i = 0; i < NUMBER_OF_TABS; i++ ) {
			// ������� ������� � ��������� ���
			tabSpec = tabHost.newTabSpec("tab" + i);
			// �������� �������
			tabSpec.setIndicator(DEPARTMENT[i]);
			// ��������� id ���������� �� FrameLayout, �� � ������ ����������
			tabSpec.setContent(0x7f070000 + i);
			// ��������� � �������� �������
			tabHost.addTab(tabSpec);
		}		
        
        // ������ ������� ����� ������� �� ���������
        tabHost.setCurrentTabByTag("tab2");

        // ���������� ������������ �������
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
        	public void onTabChanged(String tabId) {
        		
        		Toast.makeText(getBaseContext(), "tabId = " + tabId, Toast.LENGTH_SHORT).show();
        		
//        		//������������ � ��
//        		SQLiteDatabase db = dbHelper.getWritableDatabase();
        		
        		if (tabId.equals("tab0")) {
        			
        		}
        		
        		if (tabId.equals("tab1")) {
        			
        		}
        		
        		if (tabId.equals("tab2")) {
        			
        		}
        		
        		if (tabId.equals("tab3")) {
        			
        		}
        		
        		if (tabId.equals("tab4")) {
//        			Log.d(LOG_TAG, "--- Rows in mytable: ---");
//          	      // ������ ������ ���� ������ �� ������� mytable, �������� Cursor 
//          	      Cursor c = db.query("phones", null, null, null, null, null, null);
//          	      // ������ ������� ������� �� ������ ������ �������
//          	      // ���� � ������� ��� �����, �������� false
//          	      if (c.moveToFirst()) {
//
//          	        // ���������� ������ �������� �� ����� � �������
//          	        int idColIndex = c.getColumnIndex("_id");
//          	        int surnameColIndex = c.getColumnIndex("surname");
//          	        int nameColIndex = c.getColumnIndex("name");
//
//          	        do {
//          	          // �������� �������� �� ������� �������� � ����� ��� � ���
//          	          Log.d(LOG_TAG,
//          	              "ID = " + c.getInt(idColIndex) + 
//          	              ", surname = " + c.getString(surnameColIndex) + 
//          	              ", name = " + c.getString(nameColIndex));
//          	          // ������� �� ��������� ������ 
//          	          // � ���� ��������� ��� (������� - ���������), �� false - ������� �� �����
//          	        } while (c.moveToNext());
//          	      } else
//          	        Log.d(LOG_TAG, "0 rows");
//          	      c.close();
        		}
//        		// ��������� ����������� � ��
//        		dbHelper.close();
            }
        });
        
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
}

