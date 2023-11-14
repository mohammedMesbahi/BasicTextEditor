package com.example.basictexteditor

import android.graphics.Typeface
import android.os.Bundle
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import yuku.ambilwarna.AmbilWarnaDialog

class MainActivity : AppCompatActivity() {

    private var isBold = false
    private var isUnderline = false
    private var isItalic = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner_fonts = findViewById<Spinner>(R.id.spinner_fonts)
        val spinner_size = findViewById<Spinner>(R.id.spinner_size)

        val txt_area = findViewById<EditText>(R.id.txt_area)

        val buttonColor = findViewById<Button>(R.id.buttonColer)
        val buttonBold = findViewById<TextView>(R.id.buttonBold)
        val buttonUnderline = findViewById<TextView>(R.id.buttonUnderline)
        val buttonItalic = findViewById<TextView>(R.id.buttonItalic)

        // Set a single listener for all three buttons
        val styleToggleListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.buttonBold -> {
                    isBold = !isBold
                    toggleStyle(StyleSpan(Typeface.BOLD), isBold)
                }
                R.id.buttonUnderline -> {
                    isUnderline = !isUnderline
                    toggleStyle(UnderlineSpan(), isUnderline)
                }
                R.id.buttonItalic -> {
                    isItalic = !isItalic
                    toggleStyle(StyleSpan(Typeface.ITALIC), isItalic)
                }
            }

        }

        // Assign the listener to the buttons
        buttonBold.setOnClickListener(styleToggleListener)
        buttonUnderline.setOnClickListener(styleToggleListener)
        buttonItalic.setOnClickListener(styleToggleListener)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.fonts,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val index = adapter.getPosition("Cagliostro-Regular")
        spinner_fonts.adapter = adapter
        spinner_fonts.setSelection(index)

        // Listener

        /*object : AdapterView.OnItemSelectedListener { ... }: This is an anonymous object expression
        in Kotlin. It creates an instance of an anonymous class
        that implements the `AdapterView.OnItemSelectedListener` interface.*/
        spinner_fonts.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                p1: View?,
                position: Int,
                p3: Long
            ) {
                val fontName = parent?.getItemAtPosition(position).toString().plus(".ttf")
                val style = Typeface.createFromAsset(assets, "font/$fontName")
                txt_area.typeface = style
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        // button color listener
        buttonColor.setOnClickListener {
            val dialog = AmbilWarnaDialog(
                this, txt_area.currentTextColor,
                object : AmbilWarnaDialog.OnAmbilWarnaListener {
                    override fun onCancel(dialog: AmbilWarnaDialog) {}
                    override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                        txt_area.setTextColor(color)
                    }
                }, "OK", "Cancel"
            )
            dialog.show()
        }

        // spinner config
        val textSizeArray = resources.getStringArray(R.array.text_sizes)

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, textSizeArray)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        spinner_size.adapter = adapter2
        spinner_size.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Get the selected text size from the array
                val selectedTextSize = textSizeArray[position]

                // Convert the selected text size to sp and apply it to your TextView or EditText
                val textSizeInSp =
                    selectedTextSize.substring(0, selectedTextSize.length - 2).toFloat()
                txt_area.textSize = textSizeInSp
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected (optional)
            }
        }

    }

    private fun toggleStyle(styleSpan: Any, isEnabled: Boolean) {
        val txt_area = findViewById<EditText>(R.id.txt_area)
        val selectionStart = txt_area.selectionStart
        val selectionEnd = txt_area.selectionEnd
        val text = txt_area.text

        if (isEnabled) {
            text.setSpan(styleSpan, selectionStart, selectionEnd, 0)
        } else {
            // Remove the style span if it exists
            val spans = text.getSpans(selectionStart, selectionEnd, styleSpan::class.java)
            for (span in spans) {
                text.removeSpan(span)
            }
        }

        txt_area.text = text
    }
}