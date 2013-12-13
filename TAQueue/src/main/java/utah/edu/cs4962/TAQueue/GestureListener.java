package utah.edu.cs4962.TAQueue;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Notes:
 *
 * Punting swipe feature for now. It is proving to be more complex than what it is worth.
 *
 * Will return to this another time.
 *
 * Created by shong on 12/11/13.
 */
public class GestureListener extends GestureDetector.SimpleOnGestureListener
{

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private QueueClient _client;
    private String _id;
    private String _token;

    public GestureListener(String id, String token)
    {
        _client = QueueClientFactory.getInstance();
        _id = id;
        _token = token;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        //detect right and left swipe
        if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
        {
            Log.d("swipe action", "right to left");
            return true; // Right to left
        }
        else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
        {
            Log.d("swipe action", "left to right");
            return false; // Left to right
        }
        else
        {
            return false;
        }
//
//        if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY)
//        {
//            return false; // Bottom to top
//        }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY)
//        {
//            return false; // Top to bottom
//        }

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e)
    {
        Log.d("single tap occured", "");
        return true;
    }


}
