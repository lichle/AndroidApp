package mobile.lichle.com.trackme.repository.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import mobile.lichle.com.trackme.repository.entity.RouteEntity;

/**
 * Created by lichvl.dp on 9/27/2018.
 */
@Dao
public interface RouteDao {

    @Query("SELECT * FROM RouteEntity WHERE trackId=:trackId")
    List<RouteEntity> loadAllRoutesByTrackId(final long trackId);

    @Insert
    void insert(List<RouteEntity> entity);

}
