package ua.com.techinservice.tabs;

import android.content.DialogInterface;
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
	
	String surname, name, inPhone, outPhone;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		getDialog().setTitle("Title!");
	    View v = inflater.inflate(R.layout.edit_with_new_lines, null);

	    View etSurname = v.findViewById(R.id.etSurname);
	    ((TextView) etSurname).setText(surname);
	    
	    View etName = v.findViewById(R.id.etName);
	    ((TextView) etName).setText(name);
	    
	    View etInPhone = v.findViewById(R.id.etInPhone);
	    ((TextView) etInPhone).setText(inPhone);
	    
	    View etOutPhone = v.findViewById(R.id.etOutPhone);
	    ((TextView) etOutPhone).setText(outPhone);

	    v.findViewById(R.id.btnOK).setOnClickListener(this);
	    v.findViewById(R.id.btnCancel).setOnClickListener(this);
	    
	    return v;
	}
	
	public void setFields(String surname, String name, String inPhone, String outPhone) {
		this.surname = surname;
		this.name = name;
		this.inPhone = inPhone;
		this.outPhone = outPhone;
	}
	
	public void onClick(View v) {
	    Log.d(LOG_TAG, "Dialog 1: " + ((Button) v).getText());
	    dismiss();
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
