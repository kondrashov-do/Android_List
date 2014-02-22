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
	
	//���������� �������
	final int NUMBER_OF_TABS = 5;
	// ����� ��������� ��� Map
	final String ATTRIBUTE_SURNAME_TEXT = "ATTRIBUTE_SURNAME_TEXT";
	final String ATTRIBUTE_NAME_TEXT = "ATTRIBUTE_NAME_TEXT";
	final String ATTRIBUTE_INPHONE_TEXT = "ATTRIBUTE_INPHONE_TEXT";
	final String ATTRIBUTE_OUTPHONE_TEXT = "ATTRIBUTE_OUTPHONE_TEXT";
	
	ListView lvSimple;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
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
			tabSpec = tabHost.newTabSpec("tag"+i);
			// �������� �������
			tabSpec.setIndicator("������� "+i);
			// ��������� id ���������� �� FrameLayout, �� � ������ ����������
			tabSpec.setContent(0x7f070000 + i - 1);
			// ��������� � �������� �������
			tabHost.addTab(tabSpec);
		}		
        
        // ������ ������� ����� ������� �� ���������
        tabHost.setCurrentTabByTag("tag2");
        
        // ���������� ������������ �������
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
