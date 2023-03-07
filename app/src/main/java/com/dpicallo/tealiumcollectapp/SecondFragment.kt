package com.dpicallo.tealiumcollectapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.dpicallo.tealiumcollectapp.databinding.FragmentSecondBinding


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val globalContext = view.getContext();
        val sharedPreference =  globalContext.getSharedPreferences("MODE_PRIVATE", 0)
        val ecid = sharedPreference.getString("adobeECID", null); // getting String

        val myWebView: WebView = view.findViewById(R.id.webview)
        myWebView.getSettings().setLoadWithOverviewMode(true)
        myWebView.getSettings().setJavaScriptEnabled(true)
        myWebView.getSettings().setUseWideViewPort(true)
        myWebView.loadUrl("https://url/index.html?adobeEcidAPP=" + ecid + "&platform=mobile")

        myWebView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                return super.shouldInterceptRequest(view, request)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}