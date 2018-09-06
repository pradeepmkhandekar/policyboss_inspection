package inspection.utility;

import android.location.Location;

/**
 * Created by Rohit on 19/02/16.
 */
public interface ILocationStateListener {

    void onLocationChanged(Location location);

    void onConnected();

    void onConnectionFailed();
}
