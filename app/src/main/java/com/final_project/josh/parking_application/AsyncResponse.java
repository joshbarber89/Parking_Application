package com.final_project.josh.parking_application;

import com.google.android.gms.maps.model.Marker;

public interface AsyncResponse {
    void processFinish(Object output);
    Marker getMarker();
}
