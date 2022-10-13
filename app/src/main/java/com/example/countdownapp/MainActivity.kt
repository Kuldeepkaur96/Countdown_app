package com.example.countdownapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Message as message

class MainActivity : AppCompatActivity() {

    //variables
    lateinit var countdown: Chronometer    //countdown specifies reverse count in stopwatch
    var running = false                 //is the stopwatch running?
    var offset: Long = 0                // the base offset for the stopwatch

    // key strings for the bundle
    val OFFSET_KEY = "offset"
    val RUNNING_KEY = "running"
    val BASE_KEY = "base"

    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        countdown = findViewById<Chronometer>(R.id.countdown)

        //check the status of running countdown is it going -ve
        countdown.setOnChronometerTickListener {


            if (isGoingNegative()) {
                countdown.stop()
                running = false
                offset = 0
            }else if (offset > 0 && running) {

                offset-=1000
            }
        }


        //Restore the previous state (Bundle)
        if (savedInstanceState != null) {
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if (running) {
                countdown.base = savedInstanceState.getLong(OFFSET_KEY)
                countdown.start()
            } else {
                setBasetime()
            }
        }
             

        val addOneSecButton = findViewById<Button>(R.id.addOneSecButton)
          // adding one sec button function
           addOneSecButton.setOnClickListener{
            if (!running){
                offset += 1000
                countdown.base = SystemClock.elapsedRealtime() + offset
            }else{
                showWarningAlert(
                    "you can't add value when timer is running",
                    "Warning!"
                )
            }
        }

        val addTenSecButton = findViewById<Button>(R.id.addTenSecButton)
        // adding ten sec button function
        addTenSecButton.setOnClickListener {
            if (!running){
             offset += 10000
             countdown.base = SystemClock.elapsedRealtime() + offset   
            }else{
                showWarningAlert(
                    "you can't add value when timer is running",
                    "Warning!"
                )
            }
            
        }
        
        //adding start button
        val startButton = findViewById<Button>(R.id.Start_button)
        startButton.setOnClickListener {
            if (!running) {
                if (isGoingNegative()){
                    
                    countdown.stop()
                    
                } else {
                    //set the base time
                    setBasetime()
                    //start the stopwatch
                    countdown.start()
                    //set running = true
                    running = true

                }
            }
            
            //the pause button pauses the stopwatch if its running
            val pauseButton = findViewById<Button>(R.id.Pause_button)
            pauseButton.setOnClickListener {
                if (running) {
                    //save offset <-- Reset back down to 0
                    saveOffset()
                    //stop the stopwatch
                    countdown.stop()
                    // set  running = false
                    running = false
                }
            }
            
            //the Reset button sets the offset and stopwatch to 0
            val resetButton = findViewById<Button>(R.id.Reset_button)
            resetButton.setOnClickListener {
                //Offset set to 0
                offset = 0
                //Reset stopwatch to 0
                setBasetime()
            }
        }



        fun onSavedInstanceState(savedInstanceState: Bundle) {
            savedInstanceState.putLong(OFFSET_KEY, offset)
            savedInstanceState.putBoolean(RUNNING_KEY, running)
            savedInstanceState.putLong(BASE_KEY, countdown.base)
            super.onSaveInstanceState(savedInstanceState)
        }
    }


    private fun isGoingNegative(): Boolean {
        var  time = countdown.text.toString()
        if (time == "00:00" || time == "00:00:00") {
            return true
        }
        if (time.contains(other = "-")){
            return true
        }
        return false
    }


    private fun showWarningAlert(message: String, title: String ){
        val dialog = AlertDialog.Builder(this)
        dialog.setMessage(message)
        dialog.setCancelable(true)
        val alert = dialog.create()
        alert.setTitle(title)
        alert.show()
    }

    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - countdown.base
    }

    // updates the stopwatch.base time, allowing for any object
    private fun setBasetime() {
        countdown.base = SystemClock.elapsedRealtime() - offset
    }
}




