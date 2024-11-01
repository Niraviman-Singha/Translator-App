package com.example.translatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

class MainActivity : AppCompatActivity() {
    private lateinit var translator:Translator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val translateButton = findViewById<Button>(R.id.translateButton)
        val outputText = findViewById<TextView>(R.id.outputText)

        // Create an English-German translator:
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.BENGALI)
            .build()
        translator = Translation.getClient(options)

        var conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                translateButton.setOnClickListener {
                    val textToTranslate = inputEditText.text.toString()
                    translateText(textToTranslate, outputText)
                }
            }
            .addOnFailureListener { exception ->
               outputText.text = "Model Download Failed!"
            }
    }

    private fun translateText(inputText:String, outputTextView: TextView){

        translator.translate(inputText)
            .addOnSuccessListener { translatedText ->
               outputTextView.text = translatedText
            }
            .addOnFailureListener { exception ->
                outputTextView.text = "Translation Failed!"
            }

    }

    override fun onDestroy() {
        super.onDestroy()
        translator.close()
    }
}