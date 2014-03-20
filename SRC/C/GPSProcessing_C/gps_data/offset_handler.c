
#include "stream_components.h"

double update_latitude_offset(stream * stream_1, stream * stream_2){
    double double_lat;
    double_lat = stream_1->latitude;
    double_lat = double_lat - stream_2->latitude;
    return double_lat;
}

double update_longitude_offset(stream * stream_1, stream * stream_2){
    double double_lng;
    double_lng = stream_1->longitude;
    double_lng = double_lng - stream_2->longitude;
    return double_lng;
}

void applyOffsets(stream * stream_2, double lat_offset, double lng_offset){
    stream_2->latitude = stream_2->latitude + lat_offset;
    stream_2->longitude = stream_2->longitude + lng_offset;
}