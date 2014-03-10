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
	
	//Количество вкладок
	final static int NUMBER_OF_TABS = 7;
	
	// имена атрибутов для Map
	final static String ATTRIBUTE_ID_TEXT = "ATTRIBUTE_ID_TEXT";
	final static String ATTRIBUTE_SURNAME_TEXT = "ATTRIBUTE_SURNAME_TEXT";
	final static String ATTRIBUTE_NAME_TEXT = "ATTRIBUTE_NAME_TEXT";
	final static String ATTRIBUTE_INPHONE_TEXT = "ATTRIBUTE_INPHONE_TEXT";
	final static String ATTRIBUTE_OUTPHONE_TEXT = "ATTRIBUTE_OUTPHONE_TEXT";
	
	// массив имен атрибутов, из которых будут читаться данные
    final static String[] FROM = {ATTRIBUTE_ID_TEXT, ATTRIBUTE_SURNAME_TEXT, ATTRIBUTE_NAME_TEXT, ATTRIBUTE_INPHONE_TEXT, ATTRIBUTE_OUTPHONE_TEXT};
    // массив ID View-компонентов, в которые будут вставлять данные
    final static int[] TO = {R.id.tvId, R.id.tvSurname, R.id.tvName, R.id.tvInPhone, R.id.tvOutPhone};
	
    private static Context context;
    
	static DataBaseHelper dbHelper;
	
	DialogEdit editDialog;
	
	//ListView lvSimple, lvAdministrative;
	
	//Массив списков
	static ListView[] lvArray = new ListView[NUMBER_OF_TABS];
	
	// Maccuв
	// упаковываем данные в понятную для адаптера структуру
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

		// массивы данных
//		String[] surname = {"Кабальский", "Ровинский", "Литовченко", "Каравай"};
//		String[] name = {"Геннадий Витальевич", "Артем Демьянович", "Марина Александровна", "Евгения Геннадиевна"};
//		String[] inPhone = {"134", "222", "143", "104"};
//		String[] outPhone = {"050-351-14-96", "050-312-92-12", "068-701-72-41", "063-145-97-68"};
		
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		
		// инициализация вкладок
        tabHost.setup();
        TabHost.TabSpec tabSpec;
		
        editDialog = new DialogEdit();
        
		// создаем объект для создания и управления версиями БД
	    dbHelper = new DataBaseHelper(this);
		
	    for (int i = 0; i < NUMBER_OF_TABS; i++) {
	    	// определяем список
	        lvArray[i] = (ListView) findViewById(R.id.lvTab0 + i);
	        
	        // СОЗДАНИЕ ВКЛАДОК!!!
	        // создаем вкладку и указываем тег
	     	tabSpec = tabHost.newTabSpec("tab" + i);
	     	// название вкладки
	     	tabSpec.setIndicator(DEPARTMENT[i]);
	     	// указываем id компонента из FrameLayout, он и станет содержимым
	     	tabSpec.setContent(R.id.lvTab0 + i);
	     	// добавляем в корневой элемент
	     	tabHost.addTab(tabSpec);
	    }
	    
	    createListTab();
  
        // вторая вкладка будет выбрана по умолчанию
        tabHost.setCurrentTabByTag("tab2");

        // обработчик переключения вкладок
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
        
     // обработчик нажатия на елемент в списке добавить для каждого таба
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
        
        //Обработчик длинного нажатия
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
        // Вывод тега текущей вкладки
        Log.d(LOG_TAG,"TabTag =" + tabHost.getCurrentTabTag());
	}
	
	// Направить телефон в активити диалера
	private void startCall(View view) {
		Intent intent;
		String outPhone = ((TextView) view.findViewById(R.id.tvOutPhone)).getText().toString();
		if (!outPhone.isEmpty()) {
			intent = new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:" + outPhone));
			startActivity(intent);
		}
	}
	
	// установить нужный таб после обновления БД
	public static void setCurrentTag() {
		tabHost.setCurrentTabByTag(tabHost.getCurrentTabTag());
	}
	
	// создание списков для заполнения табов, вычитывая данные из БД
	public static void createListTab() { 
		
		try {
			dbHelper.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
		
		// инициализация вкладок
        tabHost.setup();
        TabHost.TabSpec tabSpec;
        
		//Заполняем данные из БД в листы и создаем вкладки
        for (int i = 0; i < NUMBER_OF_TABS; i++) {
	    	//подключаемся к БД
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			String selection = "department = '" + DEPARTMENT[i] + "'";			
			// делаем запрос нужных данных из таблицы phones, получаем Cursor 
			Cursor c = db.query("phones", null, selection, null, null, null, null);
			
			// упаковываем данные в понятную для адаптера структуру
			ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		    Map<String, String> m;
			
			// ставим позицию курсора на первую строку выборки
		    // если в выборке нет строк, вернется false
		    if (c.moveToFirst()) {
		    	
		    	// определяем номера столбцов по имени в выборке
		        int idColIndex = c.getColumnIndex("_id");
		        int surnameColIndex = c.getColumnIndex("surname");
		        int nameColIndex = c.getColumnIndex("name");
		        int inPhoneColIndex = c.getColumnIndex("inPhone");
		        int outPhoneColIndex = c.getColumnIndex("outPhone");
		        do {
		        	// получаем значения по номерам столбцов и пишем все в лог
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
			        // переход на следующую строку 
			        // а если следующей нет (текущая - последняя), то false - выходим из цикла
			        } while (c.moveToNext());
			    } else
			    	Log.d(LOG_TAG, "0 rows");
			    c.close();
			    dbHelper.close();  
			    
			// создаем адаптер
			SimpleAdapter sAdapter = new SimpleAdapter(MainActivity.context, data, R.layout.my_list_item, FROM, TO);
	        // определяем список и присваиваем ему адаптер
	        
	        //ВЫРЕЗАЛ НУЖНО ВСТАВИТЬ НЕ В СТАТИК МЕТОДЕ
	        //lvArray[i] = (ListView) findViewById(R.id.lvTab0 + i);
	        lvArray[i].setAdapter(sAdapter);
	        
	        // СОЗДАНИЕ ВКЛАДОК!!!
			// создаем вкладку и указываем тег
			tabSpec = tabHost.newTabSpec("tab" + i);
			// название вкладки
			//tabSpec.setIndicator(DEPARTMENT[i]);
			// указываем id компонента из FrameLayout, он и станет содержимым
			tabSpec.setContent(R.id.lvTab0 + i);
			// добавляем в корневой элемент
			//tabHost.addTab(tabSpec);
			
			
	    }
		}
	
	// Обновление элемента списка
	public static void updateListItem() {
		
		//подключаемся к БД
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// id текущего таба
		int idTab = Integer.parseInt(tabHost.getCurrentTabTag().substring(3));
		
		String selection = "department = '" + DEPARTMENT[idTab] + "'";			
		// делаем запрос нужных данных из таблицы phones, получаем Cursor 
		Cursor c = db.query("phones", null, selection, null, null, null, null);
		
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> m;
		
		if (c.moveToFirst()) {
			// определяем номера столбцов по имени в выборке
			int idColIndex = c.getColumnIndex("_id");
			int surnameColIndex = c.getColumnIndex("surname");
	        int nameColIndex = c.getColumnIndex("name");
	        int inPhoneColIndex = c.getColumnIndex("inPhone");
	        int outPhoneColIndex = c.getColumnIndex("outPhone");
	        
	        do {
	        	// получаем значения по номерам столбцов и пишем все в лог
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
		        // переход на следующую строку 
		        // а если следующей нет (текущая - последняя), то false - выходим из цикла
		        } while (c.moveToNext());
		} else
	    	Log.d(LOG_TAG, "0 rows");
		
		c.close();
	    dbHelper.close();
	    
	    // создаем адаптер
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

