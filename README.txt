============= README ====================
=========================================

This README file covers the full assignment
for the CS22510's 'Where Am I?' assignment, written by
James Euesden (jee22) and where the particular
files for it may be found.

============= DOCUMENTATION ==============

The documentation for this assignment can be found in the
DOCS folder, and should be consulted for the 'What I did'
portion of the assignment.

============= SCREEN CASTS ===============

The screen casts demonstrating the three versions of the
applications in use and their output can be found in
the SCREENCASTS folder. Within it are three videos of the
screen casts:

C: jee22_c.ogv
C++: jee22_cplusplus.ogv
Java: jee22_java_screencast.mp4

Each screen cast can be viewed using most video players.
It is recommended to use VLC media player however, as
they have all been tried and tested with it. VLC media
player is a free to use media player software that supports
the most common and used media formats.
http://www.videolan.org/vlc/index.html

============= PROGRAM CODE ================

All three application programs are contained in the SRC folder,
with titles to indicate which folder is for which language.
The files handed in will be the full project files for
each version of the application in the different languages,
along with any files required to run it within the IDE
that the application was built in.

In order to find the specific files needed for compilation
and running the application, I have listed below where
these might be found, the IDE used, any additional parameters
required and the files themselves. No additional libraries
outside of those provided by the base language itself were
used in the writing of these applications.

================== C ======================
The C source files (.c) and header files (.h) are
all contained in:
SRC > C > ProcessingGPSData_C:
main.c
file_handler.c

and
SRC > C > ProcessingGPSData_C>gps_data:
data_coordinator.c
data_coordinator.h
date_manipulation.c
date_manipulation.h
file_handler.h
offset_handler.c
offset_handler.h
sentence_handler.c
sentence_handler.h
stream_components.c
stream_components.h
xml_creator.c
xml_creator.h

The application was written, and should probably
be tested in, the NetBeans IDE in an ubuntu
environment. This was set to conform to C89 using
the gcc compiler, with any Warning as Errors, and
has the additional cmd line option:
'-D_BSD_SOURCE'
which allows the application the use of 'strsep'
to split the char * c style strings into parts
split by the delimiter ','.

================== C++ ====================

The C source (.cpp) and header (.h) files are
all contained in:
SRC > Cplusplus > ProcessingGPSData_Cplusplus:

DataCoordinator.cpp
DataCoordinator.h
FileHandler.cpp
FileHandler.h
main.cpp
SentenceHandler.cpp
SentenceHandler.h
Stream.cpp
Stream.h
XMLCreator.cpp
XMLCreator.h

The application was written, and should probably
be tested in, the NetBeans IDE. This was set to
C++98 mode, with Warnings set to be Errors, using
the g++ compiler in an ubuntu environment.

================== Java ===================

The Java source files (.java) are all contained in:
SRC > JAVA > ProcessingGPSData-Java > src:

DataCoordinator.java
FileOutputter.java
Main.java
SentenceHandler.java
Stream.java
StreamReader.java
XMLCreator.java

The application was built, and should probably be tested in,
the IntelliJ IDE. It takes no extra parameters, and assuming
that where the files are compiled is also a folder called
'gps_data' containing the two .dat files, the application should
run. This was built on a machine running Windows 8.1. 

================== GPX OUTPUT =============

The GPX output files can each be found either in the
top layer of the source file folders of the applications
upon running the application.
For the sake of finding them easily, I have put the most
recent output files into their own 'GPX_OUTPUT' folder.

While the content of each GPX file is moderately similar,
there are some differences, e.g. Latitute and Longitude
may be ever so slightly different in how precise they are,
based upon how a particular language handles input, output
and manipulation of the 'double' numerical type. It is
for this reason that all three output files have been included.

The format of the GPX files is:

GPX
~
Way point: Lat= Lng =
Time
Elevation
End way point
~
End GPX
