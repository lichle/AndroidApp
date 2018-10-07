# TrackMeAndroidApp

Install a mock GPS application to mock up the phone location.<br/>
For example: https://play.google.com/store/apps/details?id=fr.dvilleneuve.lockito&hl=en <br/>

Requirements: <b>This project is for interview purpose only. The idea is not mine, it's owned by my interviewer, so I wanna keep it confidential.</b> </br>

Coding convention: https://source.android.com/setup/contribute/code-style

Implement Test: JUnit (for Java core), Robolectric (for Android stuff)

Document is in update...........

The architecture of the project is MVP-Clean Architecture.
At the same time, the maximum quantity of running thread is 5 (1 main Thread and 4 background threads). Because I used thread pool in the Iteractor layer (with initialization to 4 threads running in concurrency). The reason is to balance the CPU processing. Although I make a new thread every request to execute data in the repository (as you said in the interview), but the repository is called by the Iterator, and I limited the thread in Iterator to 4 only. The reason is to for reusable, you can import the repository layer to other architectures you want and don’t need to revise the repository codes because it makes sure to run the data task in background thread. In this architecture,  you can even omit making new thread in repository because the thread calls it (created from the Iterator layer: ThreadPoolExecutor.execute()) is already a background thread.

I chose the Clean Architecture because: 
- I wanna update velocity task and distance task latter to make it better: The GPS on the phone is not good, in some cases, you don’t move but the location call back is triggered, and it give you an invalid location which may be up to 100 meters or more, depend on the quality of your phone and the environment of your location. The first idea comes to my mind when I start this case is to make a queue to keep about 10 location items, then I will use that items and apply an algorithm to make the velocity or distance to be accuracy as most as possible. But it may take a little bit of time to do it, so currently I use raw data (from location callback) to calculate velocity and distance. If I update the velocity for example, the only place I will update is the UpdateVelocityTask class, and it doesn’t affect to other classes because I have all in this class (the input of this class is the queue which I can get form the repository field, the output is the same – velocity, the job is only to make an algorithm to have a more accurate velocity)
- I used to work with MVP, it is good enough for a small project but if the project is big, which has a lot of business cases, lot of networks, screen, the presenter classes become to be big and messed very fast. Because Presenter handle a lot of logic things network, view,… It’s hard for do Unit test and maintenance. The problem is eliminated in the Clean Architecture.

The problem with this Architecture is because in Android, we cannot manage the life cycle of the view ourselves, Android System manages that, some view may not be shown on screen, but its instance is not destroyed. Therefor we will implement codes to track the view status, especially. When it is in start state, stop state, viewed state, not-viewed state,…, the Presenter have to track its status to determine to update or not. It’s also the problem with MVP. To eliminate it, we can use MVVM, which view and model are binded, may be 2 ways if you want, automatically. In Android, the only way to start a component (Acitivy, Service, Broadcast, ContentProvider,…) is using context, which is resident in the View layer. But one of the rules of the Clean Architecture is the inner layer must NOT know anything about the outer layers. So instead of starting a component directly from a view itself, it tells the Presenter to do that. Then the presenter tells the View again to start the component. (^-^). I really don’t like it although I know it help to apply the Presenter to other platform (without changing Presenter logic), like IOS. I will find another solution to solve this problem and update it here.




Contact me:
lich.le.swe@gmail.com

