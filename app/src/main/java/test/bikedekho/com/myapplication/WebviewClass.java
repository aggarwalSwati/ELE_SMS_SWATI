package test.bikedekho.com.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aggarwal.swati on 7/26/16.
 */
public class WebviewClass extends Activity {

	private WebView webView;
	private ValueCallback<Uri[]> mFilePathCallback;
	private String mCameraPhotoPath;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		webView = (WebView) findViewById(R.id.webView1);
		webView.setWebViewClient(new MyBrowser());
		webView.setWebChromeClient(new PQChromeClient());
		webView.getSettings().setJavaScriptEnabled(true);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			WebView.setWebContentsDebuggingEnabled(true);
		}

		WebSettings settings = webView.getSettings();
		settings.setAppCacheEnabled(true);
		settings.setJavaScriptEnabled(true);
		settings.setSaveFormData(false);
		settings.setAllowFileAccess(true);
		settings.setPluginState(WebSettings.PluginState.ON);
		//webView.loadUrl("http://www.snapdeal.com");

	}
	private class MyBrowser extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			//view.loadUrl(url);
			return false;
		}
	}

	public class PQChromeClient extends WebChromeClient {
		// For Android 5.0
		public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient.FileChooserParams fileChooserParams) {
			// Double check that we don't have any existing callbacks
			if (mFilePathCallback != null) {
				mFilePathCallback.onReceiveValue(null);
			}
			mFilePathCallback = filePath;
			String TAG = MainActivity.class.getSimpleName();

			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
				// Create the File where the photo should go
				File photoFile = null;
				try {
					photoFile = createImageFile();
					takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
				} catch (IOException ex) {
					// Error occurred while creating the File
					Log.e(TAG, "Unable to create Image File", ex);
				}

				// Continue only if the File was successfully created
				if (photoFile != null) {
					mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(photoFile));
				} else {
					takePictureIntent = null;
				}
			}

			Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
			contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
			contentSelectionIntent.setType("image/*");

			Intent[] intentArray;
			if (takePictureIntent != null) {
				intentArray = new Intent[]{takePictureIntent};
			} else {
				intentArray = new Intent[0];
			}

			Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
			chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
			chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
			chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

			startActivityForResult(chooserIntent, 1);

			return true;

		}
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File imageFile = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				storageDir      /* directory */
		);
		return imageFile;
	}



	@Override
	protected void onResume() {
		super.onResume();
		webView.loadUrl("http://postimage.org/");
	}
}
