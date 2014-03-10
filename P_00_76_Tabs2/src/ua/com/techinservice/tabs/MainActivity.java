package ua.com.techinservice.tabs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	
	final static String LOG_TAG = "myLogs";
	// Departments
	final static String DEPARTMENT[] ={ "directors", "administrative", "automatic", "accounting",
								 "drivers", "logistics", "mechanics", "programmers",
								 "biotechnology", "heating", "technical", "security"};
	
	//���������� �������
	final static int NUMBER_OF_TABS = 7;
	
	// ����� ��������� ��� Map
	final static String ATTRIBUTE_ID_TEXT = "ATTRIBUTE_ID_TEXT";
	final static String ATTRIBUTE_SURNAME_TEXT = "ATTRIBUTE_SURNAME_TEXT";
	final static String ATTRIBUTE_NAME_TEXT = "ATTRIBUTE_NAME_TEXT";
	final static String ATTRIBUTE_INPHONE_TEXT = "ATTRIBUTE_INPHONE_TEXT";
	final static String ATTRIBUTE_OUTPHONE_TEXT = "ATTRIBUTE_OUTPHONE_TEXT";
	
	// ������ ���� ���������, �� ������� ����� �������� ������
    final static String[] FROM = {ATTRIBUTE_ID_TEXT, ATTRIBUTE_SURNAME_TEXT, ATTRIBUTE_NAME_TEXT, ATTRIBUTE_INPHONE_TEXT, ATTRIBUTE_OUTPHONE_TEXT};
    // ������ ID View-�����������, � ������� ����� ��������� ������
    final static int[] TO = {R.id.tvId, R.id.tvSurname, R.id.tvName, R.id.tvInPhone, R.id.tvOutPhone};
	
    private static Context context;
    
	static DataBaseHelper dbHelper;
	
	DialogEdit editDialog;
	
	//ListView lvSimple, lvAdministrative;
	
	//������ �������
	static ListView[] lvArray = new ListView[NUMBER_OF_TABS];
	
	// Maccu�
	// ����������� ������ � �������� ��� �������� ���������
    //static ArrayList<Map<String, String>>[] data;
    //static Map<String, String>[] m;
	//static SimpleAdapter sAdapter[];
    
	static TabHost tabHost;
	//static TabHost.TabSpec tabSpec;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// context of MainActivity
		MainActivity.context = getApplicationContext();

		// ������� ������
//		String[] surname = {"����������", "���������", "����������", "�������"};
//		String[] name = {"�������� ����������", "����� ����������", "������ �������������", "������� �����������"};
//		String[] inPhone = {"134", "222", "143", "104"};
//		String[] outPhone = {"050-351-14-96", "050-312-92-12", "068-701-72-41", "063-145-97-68"};
		
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		
		// ������������� �������
        tabHost.setup();
        TabHost.TabSpec tabSpec;
		
        editDialog = new DialogEdit();
        
		// ������� ������ ��� �������� � ���������� �������� ��
	    dbHelper = new DataBaseHelper(this);
		
	    for (int i = 0; i < NUMBER_OF_TABS; i++) {
	    	// ���������� ������
	        lvArray[i] = (ListView) findViewById(R.id.lvTab0 + i);
	        
	        // �������� �������!!!
	        // ������� ������� � ��������� ���
	     	tabSpec = tabHost.newTabSpec("tab" + i);
	     	// �������� �������
	     	tabSpec.setIndicator(DEPARTMENT[i]);
	     	// ��������� id ���������� �� FrameLayout, �� � ������ ����������
	     	tabSpec.setContent(R.id.lvTab0 + i);
	     	// ��������� � �������� �������
	     	tabHost.addTab(tabSpec);
	    }
	    
	    createListTab();
  
        // ������ ������� ����� ������� �� ���������
        tabHost.setCurrentTabByTag("tab2");

        // ���������� ������������ �������
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
        	public void onTabChanged(String tabId) {
        		
        		Toast.makeText(getBaseContext(), "tabId = " + tabId, Toast.LENGTH_SHORT).show();
        		
        		if (tabId.equals("tab0")) {
        			
        		}
        		
        		if (tabId.equals("tab1")) {
        			
        		}
        		
        		if (tabId.equals("tab2")) {
        		
        		}
        		
        		if (tabId.equals("tab3")) {
        		
        		}
        		
        		if (tabId.equals("tab4")) {
        		
        		}
            }
        });
        
     // ���������� ������� �� ������� � ������ �������� ��� ������� ����
        lvArray[0].setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	startCall(view);
        	}
        });
        
        lvArray[1].setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	startCall(view);
        	}
        });
        
        lvArray[2].setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	startCall(view);
        	}
        });
        
        lvArray[3].setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	startCall(view);
        	}
        });
        
        //���������� �������� �������
        lvArray[1].setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				
				editDialog.setFields(((TextView) view.findViewById(R.id.tvId)).getText().toString(),
						((TextView) view.findViewById(R.id.tvSurname)).getText().toString(),
						((TextView) view.findViewById(R.id.tvName)).getText().toString(),
						((TextView) view.findViewById(R.id.tvInPhone)).getText().toString(),
						((TextView) view.findViewById(R.id.tvOutPhone)).getText().toString(),
						dbHelper);
				
				editDialog.show( getSupportFragmentManager(), "editDialog");
				Log.d(LOG_TAG, "Long click is over");
			return true;
        		
        	}
        });
        // ����� ���� ������� �������
        Log.d(LOG_TAG,"TabTag =" + tabHost.getCurrentTabTag());
	}
	
	// ��������� ������� � �������� �������
	private void startCall(View view) {
		Intent intent;
		String outPhone = ((TextView) view.findViewById(R.id.tvOutPhone)).getText().toString();
		if (!outPhone.isEmpty()) {
			intent = new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:" + outPhone));
			startActivity(intent);
		}
	}
	
	// ���������� ������ ��� ����� ���������� ��
	public static void setCurrentTag() {
		tabHost.setCurrentTabByTag(tabHost.getCurrentTabTag());
	}
	
	// �������� ������� ��� ���������� �����, ��������� ������ �� ��
	public static void createListTab() { 
		
		try {
			dbHelper.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
		
		// ������������� �������
        tabHost.setup();
        TabHost.TabSpec tabSpec;
        
		//��������� ������ �� �� � ����� � ������� �������
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
			        m.put(ATTRIBUTE_ID_TEXT, c.getString(idColIndex));
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
			SimpleAdapter sAdapter = new SimpleAdapter(MainActivity.context, data, R.layout.my_list_item, FROM, TO);
	        // ���������� ������ � ����������� ��� �������
	        
	        //������� ����� �������� �� � ������ ������
	        //lvArray[i] = (ListView) findViewById(R.id.lvTab0 + i);
	        lvArray[i].setAdapter(sAdapter);
	        
	        // �������� �������!!!
			// ������� ������� � ��������� ���
			tabSpec = tabHost.newTabSpec("tab" + i);
			// �������� �������
			//tabSpec.setIndicator(DEPARTMENT[i]);
			// ��������� id ���������� �� FrameLayout, �� � ������ ����������
			tabSpec.setContent(R.id.lvTab0 + i);
			// ��������� � �������� �������
			//tabHost.addTab(tabSpec);
			
			
	    }
		}
	
	// ���������� �������� ������
	public static void updateListItem() {
		
		//������������ � ��
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// id �������� ����
		int idTab = Integer.parseInt(tabHost.getCurrentTabTag().substring(3));
		
		String selection = "department = '" + DEPARTMENT[idTab] + "'";			
		// ������ ������ ������ ������ �� ������� phones, �������� Cursor 
		Cursor c = db.query("phones", null, selection, null, null, null, null);
		
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> m;
		
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
		        m.put(ATTRIBUTE_ID_TEXT, c.getString(idColIndex));
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
	    SimpleAdapter sAdapter = new SimpleAdapter(MainActivity.context, data, R.layout.my_list_item, FROM, TO);
        
        lvArray[idTab].setAdapter(sAdapter);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
	
	
}

