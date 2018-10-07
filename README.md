# TrackMeAndroidApp

Install a mock GPS application to mock up the phone location.<br/>
For example: https://play.google.com/store/apps/details?id=fr.dvilleneuve.lockito&hl=en <br/>

Requirements: <b>This project is for interview purpose only. The idea is not mine, it's owned by my interviewer, so I wanna keep it confidential.<b> </br>

Coding convention: https://source.android.com/setup/contribute/code-style

Implement Test: JUnit (for Java core), Robolectric (for Android stuff)

Document is in update...........

The architecture of the project is MVP-Clean Architecture.
At the same time, the maximum quantity of running thread is 5 (1 main Thread and 4 background threads). Because I used thread pool in the Iteractor package (with initialization to 4 threads running in concurrency). The reason is to balance the CPU processing. Although I make a new thread every request to execute data in the repository, but the repository is called by the Iterator, and I limited the thread in Iterator to 4 only. The reason is to for reusable, you can import the repository package to other architecture you want which does not revise it because it’s sure to run in background thread. In this architecture,  you can even omit making new thread when execute data task in repository because the thread call it (create from the Iterator: ThreadPoolExecutor.execute()) is already in background thread.

I chose the Architecture because: 
- I want to update velocity task and distance task latter to make it better: The GPS on the phone is not good, in some cases, you don’t move but the location call back is triggered, and it give you an invalid location which may be up to 100 meters or more, depend on the quality of your phone and the environment of your location. The first idea comes to my mind when I start this case is making a queue to keep about 10 location items, then I will use that items and apply an algorithm to make the velocity or distance to be accuracy as most as possible. But I quite take a little bit of time to do it, so currently I use raw data (from location callback) to calculate velocity and distance





Contact me:
lich.le.swe@gmail.com

