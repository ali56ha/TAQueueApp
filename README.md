TAQueueApp
==========

Android client for the TA Queue implemented here: https://github.com/urails/ta_queue/

How to Use this app
===

There are two ways you can use this app:

1. As a student
2. As a TA

App Startup
===

1. Tap the app widget on your home screen.
2. Select the university.
3. Select the course listed by instructor. You can expand or hide these items.
4. Select ```Student``` or ```TA``` option.

Login as Student
===

1. Enter username. This can be any arbitrary name, such as ```Optimus Prime```, but usually best if you use your own name.
2. Enter a location. Again, this can be arbitrary, but best to enter one where the TA can find you, such as ```lab1-4```.
3. Tap the ```Login``` button.

Login as a TA
===

1. Enter username. This can be arbitrary, such as ```Foobar```, but usually best to put your own name.
2. Enter password. This MUST be the password for that queue given to you by the instructor of the course.
3. Tap the ```Login``` button.

Queue view as a Student
===

1. The top banner shows the ```Course Number```.
2. Underneath, ```TA announcements``` will appear, but when there are none, it will be blank.
3. The left hand list shows the ```TAs in the queue``` with a specific color.
4. The right hand list shows the ```Students in the queue``` with the matching color of the TA helping them. If the student is not being helped, then the color is white.
5. The button on the bottom left is the ```Enter Queue``` button. Tap this to see your name in the ```Students in the queue``` list.
6. The button on the bottom center is the ```Exit Queue``` button. Tap this to see your name be removed from the ```Students in the queue``` list.
7. The button the bottom right is the ```Sign Out``` button. Tap this to exit the queue completely. You will be returned to the ```Login Screen``` and can login again, or hit the Back button on your device to see the list of courses, and hit Back again to see the list of universities.

Queue view as a TA
===

1. The top banner shows the ```Course Number```.
2. Underneath, ```TA announcements``` edit text field where you can enter a new message, but when there are none, it will contain a default meessage.
3. The left hand list shows the ```TAs in the queue``` with a specific color.
4. The right hand list shows the ```Students in the queue``` with the matching color of the TA helping them. If the student is not being helped, then the color is white.
5. 5. The button on the bottom left is the ```Deactivate``` button. Tap this to deactivate the queue, remove all students currently in the queue, and prevent additional students from entering the queue. The background of the screen will turn red to indicate the queue is deactivated and the button text will toggle to ```Activate```. Tap again to activate the queue and the background turns white or yellow depending on number of students in the queue to TAs available.
6. The button on the bottom center is the ```Freeze``` button. Tap this to freeze the queue, keep the current students in the queue, and prevent additional students from entering the queue. The background of the screen will turn blue to indicate the queue is frozen and the button text will toggle to ```Unfreeze```. Tap again to unfreeze the queue and the background turns white or yellow depending on number of students in the queue to TAs available. NOTE: This mode only works if the queue is active.
7. The button the bottom right is the ```Sign Out``` button. Tap this to exit the queue completely. You will be returned to the ```Login Screen``` and can login again, or hit the Back button on your device to see the list of courses, and hit Back again to see the list of universities.


Color indicators for TAs
===

* White = queue is active and there are no students waiting for help.
* Red = queue is deactivated.
* Blue = queue is frozen.
* Yellow = queue is active and there are students waiting for help.
