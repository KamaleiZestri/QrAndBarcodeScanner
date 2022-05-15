package com.example.barcodescanner.feature.tabs.settings.connection

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.barcodescanner.feature.BaseActivity
import com.example.barcodescanner.R
import com.example.barcodescanner.di.settings
import com.example.barcodescanner.extension.applySystemWindowInsets
import com.example.barcodescanner.extension.isNotBlank
import kotlinx.android.synthetic.main.activity_ip_address.*

class IpAddressActivity: BaseActivity() {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, IpAddressActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ip_address);
        supportEdgeToEdge()
        handleToolbarBackClicked()
        initTextArea()
        handleTextEdited()
    }

    private fun supportEdgeToEdge() {
        root_view.applySystemWindowInsets(applyTop = true, applyBottom = true)
    }

    private fun handleToolbarBackClicked() {
        toolbar.setNavigationOnClickListener { finish() }
    }

   private fun initTextArea() {
        val defaultText = settings.setIP
        text_area.apply {
            setText(defaultText)
            setSelection(defaultText.length)
            requestFocus()
        }
   }

    private fun handleTextEdited() {
        text_area.addTextChangedListener {
            settings.setIP = text_area.text.toString()
        }
    }
}