package mobile.lichle.com.trackme.repository.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by lichvl.dp on 9/27/2018.
 */
//@Entity(foreignKeys = @ForeignKey(entity = TrackEntity.class, parentColumns = "id", childColumns = "trackId"))
@Entity
public class RouteEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "duration")
    private int mDuration;

    @ColumnInfo(name = "route")
    private String mRoute;

    @ColumnInfo(name = "velocity")
    private float mVelocity;

    @ColumnInfo(name = "distance")
    private float mDistance;

    @ForeignKey(entity = TrackEntity.class, parentColumns = "id", childColumns = "trackId", onDelete = CASCADE, onUpdate = CASCADE)
    private long trackId;

    @ColumnInfo(name = "createdTime")
    private long mCreatedTime;


    public float getDistance() {
        return mDistance;
    }

    public void setDistance(float mDistance) {
        this.mDistance = mDistance;
    }

    public float getVelocity() {
        return mVelocity;
    }

    public void setVelocity(float mVelocity) {
        this.mVelocity = mVelocity;
    }

    public long getCreatedTime() {
        return mCreatedTime;
    }

    public void setCreatedTime(long createdTime) {
        this.mCreatedTime = createdTime;
    }

    public String getRoute() {
        return mRoute;
    }

    public void setRoute(String route) {
        this.mRoute = route;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public long getTrackId() {
        return trackId;
    }

    public void setTrackId(long trackId) {
        this.trackId = trackId;
    }
}
