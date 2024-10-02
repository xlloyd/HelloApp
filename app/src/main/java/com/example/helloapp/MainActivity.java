package com.example.helloapp;

// Import necessary Android and Java classes
import android.os.Bundle; // For handling activity lifecycle events and passing data
import android.os.Handler; // For scheduling tasks to be executed later
import android.util.Log; // For logging debug messages
import android.widget.TextView; // For displaying text in the UI

import androidx.activity.EdgeToEdge; // For enabling edge-to-edge display mode
import androidx.appcompat.app.AppCompatActivity; // For using AppCompatActivity, which provides backward-compatible features
import androidx.core.graphics.Insets; // For handling insets (e.g., system bars)
import androidx.core.view.ViewCompat; // For working with view properties
import androidx.core.view.WindowInsetsCompat; // For managing window insets

import java.text.SimpleDateFormat; // For formatting dates and times
import java.util.Date; // For working with dates and times
import java.util.Locale; // For locale-specific settings
import java.util.TimeZone; // For handling time zones

public class MainActivity extends AppCompatActivity {

    // Define member variables
    private TextView clockTextView; // Reference to the TextView that displays the time
    private TextView greetingTextView; // Reference to the TextView that displays the greeting
    private TextView messageTextView; // Reference to the TextView that displays the short message
    private final Handler handler = new Handler(); // Handler to post delayed tasks
    private Runnable runnable; // Runnable to execute periodic tasks
    private static final String TAG = "MainActivity"; // Tag for logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enable edge-to-edge display mode
        setContentView(R.layout.activity_main); // Set the content view to the activity_main layout

        // Find the TextViews by their IDs
        clockTextView = findViewById(R.id.clockTextView);
        greetingTextView = findViewById(R.id.greetingTextView);
        messageTextView = findViewById(R.id.messageTextView);

        // Apply window insets to handle system bars (e.g., status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Set padding based on system bars insets to avoid overlap
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets; // Return the insets after applying them
        });

        // Initialize and start a runnable that updates the time every second
        runnable = new Runnable() {
            @Override
            public void run() {
                updateTime(); // Update the time on each run
                handler.postDelayed(this, 1000); // Schedule the runnable to run again after 1 second
            }
        };
        handler.post(runnable); // Start the runnable immediately
    }

    // Method to update the clock TextView with the current time and set greeting and message
    private void updateTime() {
        // Set the time zone to Asia/Manila
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Manila");

        // Log the current time zone to verify it's correctly set
        Log.d(TAG, "TimeZone: " + timeZone.getID());

        // Create a SimpleDateFormat for 12-hour format with AM/PM
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        sdf.setTimeZone(timeZone); // Set the time zone for the formatter

        // Get the current time formatted according to the SimpleDateFormat
        String currentTime = sdf.format(new Date());

        // Update the TextView with the current time
        clockTextView.setText(currentTime);

        // Determine the greeting and message based on the current hour
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH", Locale.getDefault());
        hourFormat.setTimeZone(timeZone);
        int hour = Integer.parseInt(hourFormat.format(new Date()));

        String greeting;
        String message;



        if (hour >= 0 && hour < 12) {
            greeting = "Good Morning!";
            message = "Start your day with a smile!";
        } else if (hour >= 12 && hour < 18) {
            greeting = "Good Afternoon!";
            message = "Keep up the great work!";
        } else {
            greeting = "Good Evening!";
            message = "Relax and unwind!";
        }

        // Update the TextViews with the appropriate greeting and message
        greetingTextView.setText(greeting);
        messageTextView.setText(message);

        // Log the current time, greeting, and message to verify they're being updated correctly
        Log.d(TAG, "Current Time: " + currentTime + " | Greeting: " + greeting + " | Message: " + message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable); // Stop the runnable when the activity is destroyed
    }
}
