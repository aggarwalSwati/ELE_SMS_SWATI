package com.sms.swati;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	private TransactionsSmSListFragment listFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final int REQUEST_CODE_ASK_PERMISSIONS = 123;

		addSMSListFragment();

//		final int REQUEST_CODE_ASK_PERMISSIONS = 123;
		// ActivityCompat.requestPermissions(this, new
		// String[]{"android.permission.READ_SMS"},
		// REQUEST_CODE_ASK_PERMISSIONS);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void addSMSListFragment() {
		listFragment = new TransactionsSmSListFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.activity_main, listFragment).commit();
	}
}
