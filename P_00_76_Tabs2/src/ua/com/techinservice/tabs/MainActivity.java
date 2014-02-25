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
	
	//Количество вкладок
	final int NUMBER_OF_TABS = 5;
	// имена атрибутов для Map
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
		
		 // создаем объект для создания и управления версиями БД
	    dbHelper = new DataBaseHelper(this);
		
	    try {
	    	dbHelper.createDataBase();
	    } catch (IOException ioe) {
	    	throw new Error("Unable to create database");
	    }
	    
		// массивы данных
		String[] surname = {"Кабальский", "Ровинский", "Литовченко", "Каравай"};
		String[] name = {"Геннадий Витальевич", "Артем Демьянович", "Марина Александровна", "Евгения Геннадиевна"};
		String[] inPhone = {"134", "222", "143", "104"};
		String[] outPhone = {"050-351-14-96", "050-312-92-12", "068-701-72-41", "063-145-97-68"};
		
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        
               
        // упаковываем данные в понятную для адаптера структуру
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
        
        // массив имен атрибутов, из которых будут читаться данные
        String[] from = {ATTRIBUTE_SURNAME_TEXT, ATTRIBUTE_NAME_TEXT, ATTRIBUTE_INPHONE_TEXT, ATTRIBUTE_OUTPHONE_TEXT};
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = {R.id.tvSurname, R.id.tvName, R.id.tvInPhone, R.id.tvOutPhone};
        
        // создаем адаптер
        SimpleAdapter sAdapter = new SimpleAdapter(this, data, R.layout.my_list_item, from, to);
        
        // определяем список и присваиваем ему адаптер
        lvSimple = (ListView) findViewById(R.id.lvTab5);
        lvSimple.setAdapter(sAdapter);
        
        // инициализация вкладок
        tabHost.setup();
        TabHost.TabSpec tabSpec;
        
        for (int i = 1; i <= NUMBER_OF_TABS; i++ ) {
			// создаем вкладку и указываем тег
			tabSpec = tabHost.newTabSpec("tab"+i);
			// название вкладки
			tabSpec.setIndicator("Вкладка "+i);
			// указываем id компонента из FrameLayout, он и станет содержимым
			tabSpec.setContent(0x7f070000 + i - 1);
			// добавляем в корневой элемент
			tabHost.addTab(tabSpec);
		}		
        
        // вторая вкладка будет выбрана по умолчанию
        tabHost.setCurrentTabByTag("tab2");
        
        // обработчик переключения вкладок
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
        	public void onTabChanged(String tabId) {
        		Toast.makeText(getBaseContext(), "tabId = " + tabId, Toast.LENGTH_SHORT).show();
        		//подключаемся к БД
        		SQLiteDatabase db = dbHelper.getWritableDatabase();
        		Log.d(LOG_TAG, "--- Rows in mytable: ---");
        	      // делаем запрос всех данных из таблицы mytable, получаем Cursor 
        	      Cursor c = db.query("phones", null, null, null, null, null, null);
        	   // ставим позицию курсора на первую строку выборки
        	      // если в выборке нет строк, вернется false
        	      if (c.moveToFirst()) {

        	        // определяем номера столбцов по имени в выборке
        	        int idColIndex = c.getColumnIndex("_id");
        	        int surnameColIndex = c.getColumnIndex("surname");
        	        int nameColIndex = c.getColumnIndex("name");

        	        do {
        	          // получаем значения по номерам столбцов и пишем все в лог
        	          Log.d(LOG_TAG,
        	              "ID = " + c.getInt(idColIndex) + 
        	              ", surname = " + c.getString(surnameColIndex) + 
        	              ", name = " + c.getString(nameColIndex));
        	          // переход на следующую строку 
        	          // а если следующей нет (текущая - последняя), то false - выходим из цикла
        	        } while (c.moveToNext());
        	      } else
        	        Log.d(LOG_TAG, "0 rows");
        	      c.close();
        	      // закрываем подключение к БД
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
	// путь к базе данных вашего приложения
    private static String DB_PATH = "/data/data/ua.com.techinservice.tabs/databases/";
    private static String DB_NAME = "tech";
    private SQLiteDatabase myDataBase;
    private final Context mContext;
    
    /**
     * Конструктор
     * Принимает и сохраняет ссылку на переданный контекст для доступа к ресурсам приложения
     * @param context
     */
    public DataBaseHelper(Context context) {
    	super(context, DB_NAME, null, 1);
        this.mContext = context;
    }
    
    /**
     * Создает пустую базу данных и перезаписывает ее нашей собственной базой
     * */
    public void createDataBase() throws IOException{
    	boolean dbExist = checkDataBase();

    	if(dbExist){
    		//ничего не делать - база уже есть
    	}else{
    		//вызывая этот метод создаем пустую базу, позже она будет перезаписана
        	this.getReadableDatabase();

        	try {
    			copyDataBase();
    		} catch (IOException e) {
        		throw new Error("Error copying database");
        	}
    	}
    }
    
    /**
     * Проверяет, существует ли уже эта база, чтобы не копировать каждый раз при запуске приложения
     * @return true если существует, false если не существует
     */
    private boolean checkDataBase(){
    	SQLiteDatabase checkDB = null;

    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    	}catch(SQLiteException e){
    		//база еще не существует
    	}
    	if(checkDB != null){
    		checkDB.close();
    	}
    	return checkDB != null ? true : false;
    }
    
    /**
     * Копирует базу из папки assets заместо созданной локальной БД
     * Выполняется путем копирования потока байтов.
     * */
    private void copyDataBase() throws IOException{
    	//Открываем локальную БД как входящий поток
    	InputStream myInput = mContext.getAssets().open(DB_NAME);

    	//Путь ко вновь созданной БД
    	String outFileName = DB_PATH + DB_NAME;

    	//Открываем пустую базу данных как исходящий поток
    	OutputStream myOutput = new FileOutputStream(outFileName);

    	//перемещаем байты из входящего файла в исходящий
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}

    	//закрываем потоки
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
    }
    
    public void openDataBase() throws SQLException{
    	//открываем БД
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

    // Здесь можно добавить вспомогательные методы для доступа и получения данных из БД
    // вы можете возвращать курсоры через "return myDataBase.query(....)", это облегчит их использование
    // в создании адаптеров для ваших view
    
}
