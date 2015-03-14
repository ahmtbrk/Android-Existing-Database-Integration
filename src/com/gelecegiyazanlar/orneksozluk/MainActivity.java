package com.gelecegiyazanlar.orneksozluk;

import java.io.IOException;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	DatabaseHelper dbHelper;
	EditText trEditText,enEditText;
	Button cevirButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		dbHelper = new DatabaseHelper(this);
		try {
			dbHelper.createDataBase();
			dbHelper.openDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		trEditText = (EditText)findViewById(R.id.trEditText);
		enEditText = (EditText)findViewById(R.id.enEditText);
		cevirButton = (Button)findViewById(R.id.cevir);
		
		cevirButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String turkceKelime = trEditText.getText().toString();
				Cursor crs = dbHelper.getDatabase().query("kelimeler", new String[]{ "kelimeEn" }, "kelimeTr = ?", new String[]{ turkceKelime }, null, null, null);
				if(crs.getCount()>0){
					crs.moveToFirst();
					String ingilizceKelime = crs.getString(crs.getColumnIndex("kelimeEn"));
					enEditText.setText(ingilizceKelime);
				}else{
					Toast.makeText(getApplicationContext(), "Kelimenin karþýlýðý bulunamadý", Toast.LENGTH_SHORT).show();
					trEditText.getText().clear();
				}
			}
		});
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		dbHelper.close();
	}
}
