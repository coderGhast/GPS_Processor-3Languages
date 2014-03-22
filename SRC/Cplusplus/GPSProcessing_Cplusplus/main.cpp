/* 
 * File:   main.cpp
 * Author: jee22
 *
 * Created on 21 March 2014, 02:03
 */

#include <cstdlib>
#include <stdio.h>
#include "DataCoordinator.h"

using namespace std;

/*
 * 
 */
int main(int argc, char** argv) {
    DataCoordinator * data_coordinator = new DataCoordinator();
    data_coordinator->runApp();
    return 0;
}

