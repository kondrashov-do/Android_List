package ua.com.techinservice.tabs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	final String LOG_TAG = "myLogs";
	
	//���������� �������
	final int NUMBER_OF_TABS = 5;
	// ����� ��������� ��� Map
	final String ATTRIBUTE_SURNAME_TEXT = "ATTRIBUTE_SURNAME_TEXT";
	final String ATTRIBUTE_NAME_TEXT = "ATTRIBUTE_NAME_TEXT";
	final String ATTRIBUTE_INPHONE_TEXT = "ATTRIBUTE_INPHONE_TEXT";
	final String ATTRIBUTE_OUTPHONE_TEXT = "ATTRIBUTE_OUTPHONE_TEXT";
	
	DataBaseHelper dbHelper;
	
	ListView lvSimple;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		 // ������� ������ ��� �������� � ���������� �������� ��
	    dbHelper = new DataBaseHelper(this);
		
	    try {
	    	dbHelper.createDataBase();
	    } catch (IOException ioe) {
	    	throw new Error("Unable to create database");
	    }
	    
		// ������� ������
		String[] surname = {"����������", "���������", "����������", "�������"};
		String[] name = {"�������� ����������", "����� ����������", "������ �������������", "������� �����������"};
		String[] inPhone = {"134", "222", "143", "104"};
		String[] outPhone = {"050-351-14-96", "050-312-92-12", "068-701-72-41", "063-145-97-68"};
		
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        
               
        // ����������� ������ � �������� ��� �������� ���������
        ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>(surname.length);
        Map<String, String> m;
        for (int i = 0; i < surname.length; i++) {
        	m = new HashMap<String, String>();
            m.put(ATTRIBUTE_SURNAME_TEXT, surname[i]);
            m.put(ATTRIBUTE_NAME_TEXT, name[i]);
            m.put(ATTRIBUTE_INPHONE_TEXT, inPhone[i]);
            m.put(ATTRIBUTE_OUTPHONE_TEXT, outPhone[i]);
            data.add(m);
          }
        
        // ������ ���� ���������, �� ������� ����� �������� ������
        String[] from = {ATTRIBUTE_SURNAME_TEXT, ATTRIBUTE_NAME_TEXT, ATTRIBUTE_INPHONE_TEXT, ATTRIBUTE_OUTPHONE_TEXT};
        // ������ ID View-�����������, � ������� ����� ��������� ������
        int[] to = {R.id.tvSurname, R.id.tvName, R.id.tvInPhone, R.id.tvOutPhone};
        
        // ������� �������
        SimpleAdapter sAdapter = new SimpleAdapter(this, data, R.layout.my_list_item, from, to);
        
        // ���������� ������ � ����������� ��� �������
        lvSimple = (ListView) findViewById(R.id.lvTab5);
        lvSimple.setAdapter(sAdapter);
        
        // ������������� �������
        tabHost.setup();
        TabHost.TabSpec tabSpec;
        
        for (int i = 1; i <= NUMBER_OF_TABS; i++ ) {
			// ������� ������� � ��������� ���
			tabSpec = tabHost.newTabSpec("tab"+i);
			// �������� �������
			tabSpec.setIndicator("������� "+i);
			// ��������� id ���������� �� FrameLayout, �� � ������ ����������
			tabSpec.setContent(0x7f070000 + i - 1);
			// ��������� � �������� �������
			tabHost.addTab(tabSpec);
		}		
        
        // ������ ������� ����� ������� �� ���������
        tabHost.setCurrentTabByTag("tab2");
        
        // ���������� ������������ �������
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
        	public void onTabChanged(String tabId) {
        		Toast.makeText(getBaseContext(), "tabId = " + tabId, Toast.LENGTH_SHORT).show();
        		//������������ � ��
        		SQLiteDatabase db = dbHelper.getWritableDatabase();
        		Log.d(LOG_TAG, "--- Rows in mytable: ---");
        	      // ������ ������ ���� ������ �� ������� mytable, �������� Cursor 
        	      Cursor c = db.query("phones", null, null, null, null, null, null);
        	   // ������ ������� ������� �� ������ ������ �������
        	      // ���� � ������� ��� �����, �������� false
        	      if (c.moveToFirst()) {

        	        // ���������� ������ �������� �� ����� � �������
        	        int idColIndex = c.getColumnIndex("_id");
        	        int surnameColIndex = c.getColumnIndex("surname");
        	        int nameColIndex = c.getColumnIndex("name");

        	        do {
        	          // �������� �������� �� ������� �������� � ����� ��� � ���
        	          Log.d(LOG_TAG,
        	              "ID = " + c.getInt(idColIndex) + 
        	              ", surname = " + c.getString(surnameColIndex) + 
        	              ", name = " + c.getString(nameColIndex));
        	          // ������� �� ��������� ������ 
        	          // � ���� ��������� ��� (������� - ���������), �� false - ������� �� �����
        	        } while (c.moveToNext());
        	      } else
        	        Log.d(LOG_TAG, "0 rows");
        	      c.close();
        	      // ��������� ����������� � ��
        	      dbHelper.close();
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

class DataBaseHelper extends SQLiteOpenHelper{
	// ���� � ���� ������ ������ ����������
    private static String DB_PATH = "/data/data/ua.com.techinservice.tabs/databases/";
    private static String DB_NAME = "tech";
    private SQLiteDatabase myDataBase;
    private final Context mContext;
    
    /**
     * �����������
     * ��������� � ��������� ������ �� ���������� �������� ��� ������� � �������� ����������
     * @param context
     */
    public DataBaseHelper(Context context) {
    	super(context, DB_NAME, null, 1);
        this.mContext = context;
    }
    
    /**
     * ������� ������ ���� ������ � �������������� �� ����� ����������� �����
     * */
    public void createDataBase() throws IOException{
    	boolean dbExist = checkDataBase();

    	if(dbExist){
    		//������ �� ������ - ���� ��� ����
    	}else{
    		//������� ���� ����� ������� ������ ����, ����� ��� ����� ������������
        	this.getReadableDatabase();

        	try {
    			copyDataBase();
    		} catch (IOException e) {
        		throw new Error("Error copying database");
        	}
    	}
    }
    
    /**
     * ���������, ���������� �� ��� ��� ����, ����� �� ���������� ������ ��� ��� ������� ����������
     * @return true ���� ����������, false ���� �� ����������
     */
    private boolean checkDataBase(){
    	SQLiteDatabase checkDB = null;

    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    	}catch(SQLiteException e){
    		//���� ��� �� ����������
    	}
    	if(checkDB != null){
    		checkDB.close();
    	}
    	return checkDB != null ? true : false;
    }
    
    /**
     * �������� ���� �� ����� assets ������� ��������� ��������� ��
     * ����������� ����� ����������� ������ ������.
     * */
    private void copyDataBase() throws IOException{
    	//��������� ��������� �� ��� �������� �����
    	InputStream myInput = mContext.getAssets().open(DB_NAME);

    	//���� �� ����� ��������� ��
    	String outFileName = DB_PATH + DB_NAME;

    	//��������� ������ ���� ������ ��� ��������� �����
    	OutputStream myOutput = new FileOutputStream(outFileName);

    	//���������� ����� �� ��������� ����� � ���������
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}

    	//��������� ������
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
    }
    
    public void openDataBase() throws SQLException{
    	//��������� ��
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }
    
    @Override
	public synchronized void close() {
    	    if(myDataBase != null)
    		    myDataBase.close();
    	    super.close();
	}
    
    @Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

    // ����� ����� �������� ��������������� ������ ��� ������� � ��������� ������ �� ��
    // �� ������ ���������� ������� ����� "return myDataBase.query(....)", ��� �������� �� �������������
    // � �������� ��������� ��� ����� view
    
}
