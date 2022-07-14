package com.example.mywebbrowser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.mywebbrowser.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = object:WebViewClient(){
                override fun onPageFinished(view: WebView, url: String) {
                    binding.urlEditText.setText(url)
                }
            }
        }
        binding.webView.loadUrl("https://www.google.com")

        binding.urlEditText.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                binding.webView.loadUrl(binding.urlEditText.text.toString())
                true
            }else{
                false
            }
        }
        registerForContextMenu(binding.webView)
    }

    override fun onBackPressed(){
        if(binding.webView.canGoBack()){
            binding.webView.goBack()
        }else{
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_google, R.id.action_home -> {
                binding.webView.loadUrl("https://www.google.com")
                return true
            }
            R.id.action_naver -> {
                binding.webView.loadUrl("https://www.naver.com")
                return true
            }
            R.id.action_daum -> {
                binding.webView.loadUrl("https://www.daum.net")
                return true
            }
            R.id.action_call -> {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:031-123-4567")
                if(intent.resolveActivity(packageManager) != null){
                    startActivity(intent)
                }
                return true
            }
            R.id.action_send_text -> {
                binding.webView.url?.let { url ->
                    //문자보내기
                    sendSMS("031-123-4567", url)
                }
                return true
            }
            R.id.action_email -> {
                binding.webView.url?.let { url ->
                    //이메일보내기
                    email("test@example.com", "좋은 사이트", url)
                }
                return true
            }
            R.id.action_address -> {
                val intent = Intent(this, AddressListView::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_share -> {
                binding.webView.url?.let{url ->
                    //페이지 공유
                    share(url)
                }
                return true
            }

            R.id.action_browser -> {
                binding.webView.url?.let{url ->
                    //기본 웹 브라우저에서 열기
                    browse(url)
                }
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}
