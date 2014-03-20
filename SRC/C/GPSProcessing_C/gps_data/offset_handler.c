/* 
 * File:   offset_handler.c
 * Author: jee22
 * 
 * Updates to the offsets between
 * the latitude and longitudes of
 * the primary and secondary streams.
 */

#include "stream_components.h"

/**
 * Find the latitude offset between each stream.
 * @param stream_1 Primary
 * @param stream_2 Secondary
 * @return The updated latitude offset.
 */
double update_latitude_offset(stream * stream_1, stream * stream_2) {
    double double_lat;
    double_lat = stream_1->latitude;
    double_lat = double_lat - stream_2->latitude;
    return double_lat;
}

/**
 * Find the longitude offset between the two streams.
 * @param stream_1 Primary
 * @param stream_2 Secondary
 * @return The updated longitude offset.
 */
double update_longitude_offset(stream * stream_1, stream * stream_2) {
    double double_lng;
    double_lng = stream_1->longitude;
    double_lng = double_lng - stream_2->longitude;
    return double_lng;
}

/**
 * Apply the offsets to the secondary stream for
 * outputting the corrected/aligned stream.
 * @param stream_2 Secondary stream
 * @param lat_offset Latitude offset
 * @param lng_offset Longitude offset
 */
void apply_offsets(stream * stream_2, double lat_offset, double lng_offset) {
    stream_2->latitude = stream_2->latitude + lat_offset;
    stream_2->longitude = stream_2->longitude + lng_offset;
}