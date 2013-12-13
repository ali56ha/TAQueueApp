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
5. On each student you can do the following actions:
      * ```Accept student``` to help a student. The student will be highlighted in the same color as yourself.
      * ```Remove student``` to remove a student from the list of students in the queue.
      * ```Put back student``` to put a student back into the queue.
6. The button on the bottom left is the ```Deactivate``` button. Tap this to deactivate the queue, remove all students currently in the queue, and prevent additional students from entering the queue. The background of the screen will turn red to indicate the queue is deactivated and the button text will toggle to ```Activate```. Tap again to activate the queue and the background turns white or yellow depending on number of students in the queue to TAs available.
7. The button on the bottom center is the ```Freeze``` button. Tap this to freeze the queue, keep the current students in the queue, and prevent additional students from entering the queue. The background of the screen will turn blue to indicate the queue is frozen and the button text will toggle to ```Unfreeze```. Tap again to unfreeze the queue and the background turns white or yellow depending on number of students in the queue to TAs available. NOTE: This mode only works if the queue is active.
8. The button the bottom right is the ```Sign Out``` button. Tap this to exit the queue completely. You will be returned to the ```Login Screen``` and can login again, or hit the Back button on your device to see the list of courses, and hit Back again to see the list of universities.


Color indicators for TAs
===

* ```White``` = queue is active and there are no students waiting for help.
* ```Red``` = queue is deactivated.
* ```Blue``` = queue is frozen.
* ```Yellow``` = queue is active and there are students waiting for help.

Take the app for a test drive
===

1. Tap the app widget on your device.
2. Select the ```University of Utah```.
3. Expand the ```Android Application``` instructor item.
4. Tap the class ```Android App Testing``` item.
5. First, we will test as a ```Student```. The login defaults as the student, so no need to tap the ```Student``` button.
6. Enter a username and location, then tap the ```Login``` button.
7. You should now see any information for this queue regarding students, TAs, and queue status. You are also able to ```Enter Queue``` if active and not frozen, ```Exit Queue```, and ```Sign Out```.
8. Now, sign out and tap the ```TA``` button to login as a ```TA```.
9. Enter username and password. For the ```Android App Testing``` course, the password is ```asdf```. Tap the ```Login``` button.
10. You should now see any information for this queue regarding students, TAs, and queue status. You are also able to update the queue message for everyone to see, ```Deactivate/Activate```, ```Freeze/Unfreeze```, and ```Sign Out```. You can also tap to ```Accept a student```, ```Remove student```, and ```Put back student```. 

What can also enhance the testing experience is to manipulate the same queue from the browser client. The URL to access the queue used in this test is:  http://nine.eng.utah.edu/schools/uofu/android/CS1234/login
