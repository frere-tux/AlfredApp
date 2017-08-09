package com.WaynesIndustries.Alfred;

import android.app.*;
import android.os.*;
import android.webkit.*;
import android.view.*;
import android.widget.*;
import android.animation.*;

public class MainActivity extends Activity 
{
	private boolean m_error;
	private WebView m_webView;
	private View m_loadingView;
	private TextView m_loadingText;
	private Button m_loadingButton;
	private long m_fadingTime = 500;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		
		m_error = false;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
		
		m_webView = (WebView) findViewById(R.id.webview);
		m_loadingView = findViewById(R.id.loading);
		m_loadingText = (TextView) findViewById(R.id.loadingText);
		m_loadingButton = (Button) findViewById(R.id.loadingButton);
		
		m_webView.setWebChromeClient(new WebChromeClient()
		{
			@Override
			public void onProgressChanged (WebView view, int newProgress)
			{
				super.onProgressChanged(view,newProgress);
				
				if (!m_error)
				{
					m_loadingText.setText(Integer.toString(newProgress) + " %");
				
					if (newProgress == 100)
					{
						crossfade();
					}
				}
			}
		});
		
		m_webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
			{
				super.onReceivedError(view, errorCode, description, failingUrl);
					
				m_error = true;
				m_loadingText.setText(description);
				m_loadingButton.setVisibility(View.VISIBLE);
			}
		});
		
		m_webView.loadUrl("http://alfred");
		
		WebSettings webSettings = m_webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
		m_loadingButton.setVisibility(View.GONE);
		m_loadingButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					m_error = false;
					m_webView.loadUrl("http://alfred");
				}
			});
    }
	
	private void crossfade() {

		m_webView.setAlpha(0f);
		m_webView.setVisibility(View.VISIBLE);

		m_webView.animate()
            .alpha(1f)
            .setDuration(m_fadingTime)
            .setListener(null);

		m_loadingView.animate()
            .alpha(0f)
            .setDuration(m_fadingTime*2)
            .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    m_loadingView.setVisibility(View.GONE);
                }
            });
	}
}
