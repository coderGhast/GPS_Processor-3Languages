/* 
 * File:   data_coordinator.h
 * Author: jee22
 *
 * Created on 19 March 2014, 15:08
 */

#ifndef DATA_COORDINATOR_H
#define	DATA_COORDINATOR_H

#ifdef	__cplusplus
extern "C" {
#endif




#ifdef	__cplusplus
}
#endif

#endif	/* DATA_COORDINATOR_H */

void get_initial_timestamps(FILE*primary_stream, stream * stream_1, 
        FILE*secondary_stream, stream * stream_2);
void sync_streams(FILE * primary_stream, FILE *secondary_stream);
void run_application();