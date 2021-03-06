# TrackMeAndroidApp

Install a mock GPS application to mock up the phone location.<br/>
For example: https://play.google.com/store/apps/details?id=fr.dvilleneuve.lockito&hl=en <br/>

Coding convention: https://source.android.com/setup/contribute/code-style

Implement Test: JUnit (for Java core), Robolectric (for Android stuff)

![alt text](https://cdn-images-1.medium.com/max/772/1*B7LkQDyDqLN3rRSrNYkETA.jpeg)

The architecture of the project is MVP-Clean Architecture.
At the same time, the maximum quantity of running thread is 5 (1 main Thread and 4 background threads). Because I used thread pool in the Iteractor layer (with initialization to 4 threads running in concurrency). The reason is to balance the CPU processing. Although I make a new thread every request to execute data in the repository (as you said in the interview), but the repository is called by the Iterator, and I limited the thread in Iterator to 4 only. The reason is to for reusable, you can import the repository layer to other architectures you want and donÕt need to revise the repository codes because it makes sure to run the data task in background thread. In this architecture,  you can even omit making new thread in repository because the thread calls it (created from the Iterator layer: ThreadPoolExecutor.execute()) is already a background thread.

I chose the Clean Architecture because: 
- I wanna update velocity task and distance task latter to make it better: The GPS on the phone is not good, in some cases, you donÕt move but the location call back is triggered, and it give you an invalid location which may be up to 100 meters or more, depend on the quality of your phone and the environment of your location. The first idea comes to my mind when I start this case is to make a queue to keep about 10 location items, then I will use that items and apply an algorithm to make the velocity or distance to be accuracy as most as possible. But it may take a little bit of time to do it, so currently I use raw data (from location callback) to calculate velocity and distance. If I update the velocity for example, the only place I will update is the UpdateVelocityTask class, and it doesnÕt affect to other classes because I have all in this class (the input of this class is the queue which I can get form the repository field, the output is the same Ð velocity, the job is only to make an algorithm to have a more accurate velocity)
- I used to work with MVP, it is good enough for a small project but if the project is big, which has a lot of business cases, lot of networks, screen, the presenter classes become to be big and messed very fast. Because Presenter handle a lot of logic things network, view,É ItÕs hard for do Unit test and maintenance. The problem is eliminated in the Clean Architecture.

The problem with this Architecture is because in Android, we cannot manage the life cycle of the view ourselves, Android System manages that, some view may not be shown on screen, but its instance is not destroyed. Therefor we will implement codes to track the view status, especially. When it is in start state, stop state, viewed state, not-viewed state..., the Presenter have to track its status to determine to update or not. ItÕs also the problem with MVP. To eliminate it, we can use MVVM, which view and model are binded, may be 2 ways if you want, automatically. In Android, the only way to start a component (Activity, Service, Broadcast, ContentProvider) is using context, which is resident in the View layer. But one of the rules of the Clean Architecture is the inner layer must NOT know anything about the outer layers. So instead of starting a component directly from a view itself, it tells the Presenter to do that. Then the presenter tells the View again to start the component. (^-^). I really donÕt like it although I know it help to apply the Presenter to other platform (without changing Presenter logic), like IOS. I will find a better solution to solve this problem and update here.

In my point, Clean Architecture is helpful for the project that requires to change UI a lots. So we are free to revise it without caring the others (database, network, business case,...).  Because the view is the outest layer, so changing the view will not effect or update codes of the other inner layers. Beside some limitations above, I found that clean architecture is quite good, it's not only give you a robust skeleton but also easily to extends and maintenance. As its name, "clean", this architecture is really clean, all components are separated and the communication between them is clean too. But there is not an architecture that resolve all problems. Your job is to choose the architecture that is the most suitable for your project, accept its pros and cons, and may need to customize it if it's require to serve your case.


I will talk about the limitation case of this architecture....... as well as the project, some points to improve latter..........


Contact me:
lich.le.swe@gmail.com

