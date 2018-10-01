package mobile.lichle.com.trackme.repository.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import mobile.lichle.com.trackme.repository.entity.TrackEntity;

/**
 * Created by lichvl.dp on 9/27/2018.
 */
@Dao
public interface TrackDao {

    @Query("SELECT * FROM TrackEntity ORDER BY createdTime DESC")
    List<TrackEntity> loadAllTracks();

    @Insert
    long insert(TrackEntity entity);

    @Query("SELECT * FROM TrackEntity WHERE id = :trackId")
    TrackEntity getTrack(int trackId);

}
