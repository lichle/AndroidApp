package mobile.lichle.com.trackme.repository.repository;

import mobile.lichle.com.trackme.repository.model.Track;

/**
 * Created by lich on 9/28/18.
 */

public interface MemTrackDataSource {

    Track getTrack();

    void clear();

}
