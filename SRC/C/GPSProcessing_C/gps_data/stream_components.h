/* 
 * File:   stream_components.h
 * Author: jee22
 *
 * Created on 20 March 2014, 01:29
 */

#ifndef STREAM_COMPONENTS_H
#define	STREAM_COMPONENTS_H

#ifdef	__cplusplus
extern "C" {
#endif




#ifdef	__cplusplus
}
#endif

#endif	/* STREAM_COMPONENTS_H */

#include <time.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <stddef.h>
#include "date_manipulation.h"

typedef struct stream_struct {
    time_t date_and_time;
    double latitude;
    double longitude;
} stream;