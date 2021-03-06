package layout

import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.ImageButton

// function create highlight ui features for on click
class ButtonHighlighterOnTouchListener(val imageButton: Button) :
    OnTouchListener {
    override fun onTouch(view: View?, motionEvent: MotionEvent): Boolean {
        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
            //grey color filter, you can change the color as you like
            imageButton.setBackgroundColor(Color.argb(0, 240, 248, 255))
        } else if (motionEvent.action == MotionEvent.ACTION_UP) {
            imageButton.setBackgroundColor(Color.rgb(176, 196, 222))
        }
        return false
    }
}

// function create highlight ui features for on click
class ButtonHighlighterOnTouchListener2(val imageButton: ImageButton) :
    OnTouchListener {
    override fun onTouch(view: View?, motionEvent: MotionEvent): Boolean {
        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
            //grey color filter, you can change the color as you like
            imageButton.setBackgroundColor(Color.rgb(176, 196, 222))
        } else if (motionEvent.action == MotionEvent.ACTION_UP) {
            imageButton.setBackgroundColor(Color.argb(0, 240, 248, 255))
        }
        return false
    }
}