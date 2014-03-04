package ua.com.techinservice.tabs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
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

public class MainActivity extends Activity {
	
	final String LOG_TAG = "myLogs";
	// Departments
	final String DEPARTMENT[] ={ "directors", "administrative", "automatic", "accounting",
								 "drivers", "logistics", "mechanics", "programmers",
								 "biotechnology", "heating", "technical", "security"};
	
	//Количество вкладок
	final int NUMBER_OF_TABS = 7;
	
	// имена атрибутов для Map
	final String ATTRIBUTE_SURNAME_TEXT = "ATTRIBUTE_SURNAME_TEXT";
	final String ATTRIBUTE_NAME_TEXT = "ATTRIBUTE_NAME_TEXT";
	final String ATTRIBUTE_INPHONE_TEXT = "ATTRIBUTE_INPHONE_TEXT";
	final String ATTRIBUTE_OUTPHONE_TEXT = "ATTRIBUTE_OUTPHONE_TEXT";
	
	// массив имен атрибутов, из которых будут читаться данные
    final String[] from = {ATTRIBUTE_SURNAME_TEXT, ATTRIBUTE_NAME_TEXT, ATTRIBUTE_INPHONE_TEXT, ATTRIBUTE_OUTPHONE_TEXT};
    // массив ID View-компонентов, в которые будут вставлять данные
    final int[] to = {R.id.tvSurname, R.id.tvName, R.id.tvInPhone, R.id.tvOutPhone};
	
	DataBaseHelper dbHelper;
	
	//ListView lvSimple, lvAdministrative;
	//Массив списков
	ListView[] lvArray = new ListView[NUMBER_OF_TABS];
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
		// инициализация вкладок
        tabHost.setup();
        TabHost.TabSpec tabSpec;
		
		// создаем объект для создания и управления версиями БД
	    dbHelper = new DataBaseHelper(this);
		
	    try {
	    	dbHelper.createDataBase();
	    } catch (IOException ioe) {
	    	throw new Error("Unable to create database");
	    }	    
		// массивы данных
//		String[] surname = {"Кабальский", "Ровинский", "Литовченко", "Каравай"};
//		String[] name = {"Геннадий Витальевич", "Артем Демьянович", "Марина Александровна", "Евгения Геннадиевна"};
//		String[] inPhone = {"134", "222", "143", "104"};
//		String[] outPhone = {"050-351-14-96", "050-312-92-12", "068-701-72-41", "063-145-97-68"};

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
	        SimpleAdapter sAdapter = new SimpleAdapter(this, data, R.layout.my_list_item, from, to);
	        // определяем список и присваиваем ему адаптер
	        lvArray[i] = (ListView) findViewById(0x7f070000 + i);
	        lvArray[i].setAdapter(sAdapter);
	        
	        // СОЗДАНИЕ ВКЛАДОК!!!
			// создаем вкладку и указываем тег
			tabSpec = tabHost.newTabSpec("tab" + i);
			// название вкладки
			tabSpec.setIndicator(DEPARTMENT[i]);
			// указываем id компонента из FrameLayout, он и станет содержимым
			tabSpec.setContent(0x7f070000 + i);
			// добавляем в корневой элемент
			tabHost.addTab(tabSpec);
	    }
  
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
        
     // обработчик нажатия на елемент в списке
        lvArray[0].setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	startCall(view);
        	}
        });
        
        lvArray[1].setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	//startCall(view);
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
        		startCall(view);
        	return true;
        		
        	}
        });
        
	}
	
	// Направить телефон в активити диалера
	public void startCall(View view) {
		
		Intent intent;
		String outPhone = ((TextView) view.findViewById(R.id.tvOutPhone)).getText().toString();
		intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + outPhone));
        startActivity(intent);
		
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
	
	
}

