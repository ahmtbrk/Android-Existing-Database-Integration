package com.gelecegiyazanlar.orneksozluk;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1;
	private static final String DB_PATH = "/data/data/com.gelecegiyazanlar.orneksozluk/databases/";
	private static final String DB_NAME = "sozluk";

	private SQLiteDatabase sqLiteDatabase;
	private Context mContext;

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	/*
	 * Eðer veritabaný yoksa kopyalayýp oluþturacak varsa hiç bir þey yapmayacak
	 * methodumuz.
	 */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			/*
			 * Veritabaný daha önce mevcut o yüzden herhangi bir iþlem yapmaya
			 * gerek yok.
			 */
		} else {
			/*
			 * Veritabaný daha önce hiç oluþturulmamýþ o yüzden veritabanýný
			 * kopyala
			 */
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {

				throw new Error("Veritabaný kopyalanamadý");

			}
		}

	}

	/*
	 * Veritabaný daha önce oluþturulmuþ mu oluþturulmamýþ mý bunu öðrenmek için
	 * yazýlan method.
	 */
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {

		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/*
	 * Veritabanýný assets'ten alýp
	 * "/data/data/com.gelecegiyazanlar.orneksozluk/databases/" altýna
	 * kopyalayacak method
	 */
	private void copyDataBase() throws IOException {
		InputStream myInput = mContext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	/*
	 * Veritabanýný uygulamada kullanacaðýmýz zaman açmak için kullanacaðýmýz
	 * method
	 */
	public void openDataBase() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		sqLiteDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	}

	/*
	 * Veritabanýný iþimiz bittiðinde kapatmak için kullanacaðýmýz method
	 */
	@Override
	public synchronized void close() {
		if (sqLiteDatabase != null)
			sqLiteDatabase.close();
		super.close();
	}
	
	/*
	 * Uygulama içinde kullanacaðýmýz db örneði
	 * */
	public SQLiteDatabase getDatabase(){
		return sqLiteDatabase;
	}

}
