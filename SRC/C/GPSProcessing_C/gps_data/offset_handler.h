/* 
 * File:   offset_handler.h
 * Author: jee22
 *
 * Created on 20 March 2014, 04:02
 */

#ifndef OFFSET_HANDLER_H
#define	OFFSET_HANDLER_H

#ifdef	__cplusplus
extern "C" {
#endif




#ifdef	__cplusplus
}
#endif

#endif	/* OFFSET_HANDLER_H */

double update_latitude_offset(stream * stream_1, stream * stream_2);
double update_longitude_offset(stream * stream_1, stream * stream_2);
void applyOffsets(stream * stream_2, double lat_offset, double lng_offset);