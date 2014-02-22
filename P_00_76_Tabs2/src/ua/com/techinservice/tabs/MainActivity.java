package ua.com.techinservice.tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	//Количество вкладок
	final int NUMBER_OF_TABS = 5;
	// имена атрибутов для Map
	final String ATTRIBUTE_SURNAME_TEXT = "ATTRIBUTE_SURNAME_TEXT";
	final String ATTRIBUTE_NAME_TEXT = "ATTRIBUTE_NAME_TEXT";
	final String ATTRIBUTE_INPHONE_TEXT = "ATTRIBUTE_INPHONE_TEXT";
	final String ATTRIBUTE_OUTPHONE_TEXT = "ATTRIBUTE_OUTPHONE_TEXT";
	
	ListView lvSimple;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
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
			tabSpec = tabHost.newTabSpec("tag"+i);
			// название вкладки
			tabSpec.setIndicator("Вкладка "+i);
			// указываем id компонента из FrameLayout, он и станет содержимым
			tabSpec.setContent(0x7f070000 + i - 1);
			// добавляем в корневой элемент
			tabHost.addTab(tabSpec);
		}		
        
        // вторая вкладка будет выбрана по умолчанию
        tabHost.setCurrentTabByTag("tag2");
        
        // обработчик переключения вкладок
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
        public void onTabChanged(String tabId) {
        Toast.makeText(getBaseContext(), "tabId = " + tabId, Toast.LENGTH_SHORT).show();
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
