package mobile.lichle.com.trackme.repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import mobile.lichle.com.trackme.Constants;
import mobile.lichle.com.trackme.repository.converter.DateConverter;
import mobile.lichle.com.trackme.repository.dao.RouteDao;
import mobile.lichle.com.trackme.repository.dao.TrackDao;
import mobile.lichle.com.trackme.repository.entity.RouteEntity;
import mobile.lichle.com.trackme.repository.entity.TrackEntity;

/**
 * Created by lichvl.dp on 9/27/2018.
 */
@Database(entities = {RouteEntity.class, TrackEntity.class}, version = 8)
@TypeConverters({DateConverter.class})
public abstract class TrackMeDatabase extends RoomDatabase {

    private static final Object sLock = new Object();
    public static TrackMeDatabase sInstance;

    public static TrackMeDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (sLock) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context);
                }
            }
        }
        return sInstance;
    }

    private static TrackMeDatabase buildDatabase(final Context appContext) {
        return Room.databaseBuilder(appContext, TrackMeDatabase.class, Constants.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public abstract RouteDao routeDao();

    public abstract TrackDao trackDao();

}
