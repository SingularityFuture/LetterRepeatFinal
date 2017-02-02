package com.example.michael.letterrepeat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 2/2/2017.
 *
 * Enter in strings and process to find letter repeats
 */

public class MainActivity extends AppCompatActivity{

    private Button runButton; // Button that runs the processing
    public int numberOfStrings; // Keeps track of number of strings
    public List<String> stringsList = new ArrayList<>(); // Create list for all stringsList
    private Integer lookbackNumber; // Lookback number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numberOfStrings = getResources().getInteger(R.integer.defaultStrings); // Set initial number of strings
        lookbackNumber = getResources().getInteger(R.integer.defaultLookback); // Set initial number of lookbacks

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Initialize the toolbar
        setSupportActionBar(toolbar);

        final LinearLayout layout = (LinearLayout)findViewById(R.id.activity_main); // Get the main activity
        for (int i = 1; i < layout.getChildCount(); i++) { // For each element in the layout, starting after the first TextView
            View v = layout.getChildAt(i); // Get each child
            if (v instanceof EditText && i<=numberOfStrings) { // If this is an EditText element and its within the string section
                stringsList.add(getResources().getStringArray(R.array.stringArray)[i-1]); // Set the initial string traits
                addStringTextListener((EditText) v, i-1); // Add a listener to the EditText view if the input changes
                // TODO: Make the touch listener work so you can delete items
/*                v.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) { // Create swipe listener to delete items
                    public void onSwipeRight() {
                        layout.removeView(v);
                        Toast.makeText(MainActivity.this, "Deleted String", Toast.LENGTH_SHORT).show();
                    }
                    public void onSwipeLeft() {
                        Toast.makeText(MainActivity.this, "Deleted String", Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
        }
        View lookbackInput = findViewById(R.id.lookback); // Get the lookback input
        addLookbackListener((EditText) lookbackInput); // Add a listener for lookback number input
        addRunButtonListener(); // Add a listener to the run button

        FloatingActionButton addString = (FloatingActionButton) layout.findViewById(R.id.addString); // Get the FAB
        addString.setOnClickListener(new View.OnClickListener() { // Set up what it does on clicking
            public void onClick(View v) {

                EditText newString = new EditText(getApplication());
                newString.setText(R.string.newValue); // Put in a value
                newString.setTextColor(Color.BLACK); // Set to black
                newString.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN); // Set underline to black to make it stand out
                newString.setMaxLines(1); // Make it a one liner.
                newString.setImeOptions(EditorInfo.IME_ACTION_DONE); // Set action done property
                newString.setInputType(EditorInfo.TYPE_CLASS_TEXT); // I think this works
                numberOfStrings += 1; // Increment the number of strings counter
                newString.setId(View.generateViewId()); // Generate a new ID
                newString.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.addView(newString,numberOfStrings);

                stringsList.add(newString.getText().toString()); // Set the string traits
                addStringTextListener(newString, numberOfStrings-1); // Add a listener to the EditText view if the input changes
            }
        });
    }

    public void addStringTextListener(final EditText stringText, final int index){

        stringText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView stringText, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) { // If user presses Enter
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // Get the keyboard
                    mgr.hideSoftInputFromWindow(stringText.getWindowToken(), 0); // Get the keyboard
                    // Parse and validate here
                    try{
                        stringsList.set(index,stringText.getText().toString()); // Replace the default with the new input data only after validated
                        Toast.makeText(MainActivity.this, "String entered \n"+ stringsList.get(index), Toast.LENGTH_SHORT).show(); // Test listener with toast.
                        handled = true;
                    }
                    catch(IllegalArgumentException e){
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show(); // Display error message
                        //<font color='#EE0000'>
                    }
                }
                return handled;
            }
        });
    }

    public void addLookbackListener(final EditText lookbackText){

        lookbackText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) { // If user presses Enter
                    Toast.makeText(MainActivity.this, "Lookback number entered", Toast.LENGTH_SHORT).show(); // Test listener with toast.
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // Get the keyboard
                    mgr.hideSoftInputFromWindow(lookbackText.getWindowToken(), 0); // Hide keyboard
                    lookbackNumber=Integer.valueOf(lookbackText.getText().toString()); // Replace the default with the new input data

                    handled = true;
                }
                return handled;
            }
        });
    }

    public void addRunButtonListener(){
        runButton = (Button) findViewById(R.id.runProcessing); // Find the run button

        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Processing", Toast.LENGTH_SHORT).show(); // Test listener with toast.
                // Process all the inputs and show the results!
                ArrayList<String> results = new Processor().runProcessor(stringsList,lookbackNumber);

                TextView outputText = (TextView) findViewById(R.id.output);

                StringBuilder builder = new StringBuilder(); // Create a string builder
                for (String s: results) { // For each string in results
                    builder.append(s); // Append it to the builder
                    builder.append("\n"); // And put a carriage return at the end of the line
                }
                outputText.setText(builder.toString()); // Set results to the final output TextView
            }
        });
    }
}
