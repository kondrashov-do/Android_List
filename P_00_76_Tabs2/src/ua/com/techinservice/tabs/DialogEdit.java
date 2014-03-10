package ua.com.techinservice.tabs;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DialogEdit extends DialogFragment implements OnClickListener {
	final String LOG_TAG = "myLogs";
	
	String id, surname, name, inPhone, outPhone;
	View etSurname, etName, etInPhone, etOutPhone;
	DataBaseHelper dbHelper;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		getDialog().setTitle("Title!");
	    View v = inflater.inflate(R.layout.edit_with_new_lines, null);

	    etSurname = v.findViewById(R.id.etSurname1);
	    ((TextView) etSurname).setText(surname);
	    
	    etName = v.findViewById(R.id.etName);
	    ((TextView) etName).setText(name);
	    
	    etInPhone = v.findViewById(R.id.etInPhone);
	    ((TextView) etInPhone).setText(inPhone);
	    
	    etOutPhone = v.findViewById(R.id.etOutPhone);
	    ((TextView) etOutPhone).setText(outPhone);

	    v.findViewById(R.id.btnOK).setOnClickListener(this);
	    v.findViewById(R.id.btnCancel).setOnClickListener(this);
	    
	    return v;
	}
	
	public void setFields(String id, String surname, String name, String inPhone, String outPhone, DataBaseHelper dbHelper) {
		this.id = id;
		this.surname = surname;
		this.name = name;
		this.inPhone = inPhone;
		this.outPhone = outPhone;
		this.dbHelper = dbHelper;
	}
	
	public void onClick(View v) {
	    Log.d(LOG_TAG, "Dialog 1: " + ((Button) v).getText());
	    
	    // создаем объект для данных
        ContentValues cv = new ContentValues();
	    
        Log.d(LOG_TAG, "Id: " + id);
        
        // подготовим данные для вставки в виде пар: наименование столбца -
        // значение
        cv.put("surname", ((TextView) etSurname).getText().toString());
        cv.put("name", ((TextView) etName).getText().toString());
        cv.put("inPhone", ((TextView) etInPhone).getText().toString());
        cv.put("outPhone", ((TextView) etOutPhone).getText().toString());
        
        //подключаемся к БД
		SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        // обновляем по id
		int updCount = db.update("phones", cv, "_id = " + id, null);
		
		Log.d(LOG_TAG, "updated rows count = " + updCount);
        
        // закрываем подключение к БД
        dbHelper.close();
        MainActivity.createListTab();
        dismiss();
        MainActivity.setCurrentTag();
	}
	
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		Log.d(LOG_TAG, "Dialog 1: onDismiss");
	}

	public void onCancel(DialogInterface dialog) {
	    super.onCancel(dialog);
	    Log.d(LOG_TAG, "Dialog 1: onCancel");
	}
}
