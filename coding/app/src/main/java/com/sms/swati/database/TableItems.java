package com.sms.swati.database;

/**
 * Created by aggarwal.swati on 8/22/17.
 */

public class TableItems {

	public static final String TABLE_SMS = "sms_new";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_CATEGORY = "address";
	public static final String COLUMN_BODY = "body";;

	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_SMS
			+ " ( " + COLUMN_ID + " integer , "
			+ DataBaseHelper.TBL_COL_SENDER_ID + " text, "
			+ DataBaseHelper.TBL_COL_DATE + " text, "
			+ DataBaseHelper.TBL_COL_TYPE + " text, "
			+ DataBaseHelper.TBL_COL_BODY + " text " + ");";

	public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_SMS;
	public static String[] Columns = new String[]{COLUMN_ID, COLUMN_CATEGORY,
			COLUMN_BODY};
}
