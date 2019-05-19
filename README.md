# Conference Scheduler

An application written as part of a successful ThoughtWorks interview process.

The Conference Scheduler takes as input a file containing a list of talks
to be scheduled at a conference, and schedules these talks based on the
following business rules and assumptions:-

* A conference may have multiple tracks
* Each track has a morning session from 9am-12pm, lunch from 12pm-1pm, and then
   a networking event that cannot start before 4pm but must not start after 5pm.
* It has been assumed that events may not finish exactly at the end of a session
   (for example morning events may end before lunch which starts at 12pm), but
   events MUST finish before the latest start time of the break)
* Based on the sample output provided, it has been assumed that if a talk
   runs past 4pm, the networking event will start at 5pm, even if that talk 
   finishes at 4.05pm

The application approaches the problem of scheduling events as a Bin-Packing
problem https://en.wikipedia.org/wiki/Bin_packing_problem.

The application has been structured in a way that should make it easy to change
the structure of a track at the conference (e.g. split the morning session into
2 with a tea break in between), or to implement a better performing bin packing
algorithm.

The application is structured in the following packages:-

* info.gregbiegel - this package contains a single Main class
* info.gregbiegel.model - this package contains a set of objects that model the domain
* info.gregbiegel.service - this package contains a service interface that provides
 read the input file and schedule the events contained therein.
* info.gregbiegel.service.exception - this package contains exception classes
 defined by the service interface
* info.gregbiegel.service.binpack - this package contains 2 different bin-packing
 algorithm implementations, and factories to create them.
* info.gregbiegel.service.binpack.exception - this package contains exception
 classes defined by the bin pack interface
 
## Build and run the application

The application is built with Maven (v.3.5.0 [https://maven.apache.org/docs/3.5.0/release-notes.html])
The application requires at least Java 1.8 to run.

To build the application, run the following cmd:-

```bash
$mvn clean package
```


This will create a runnable jar file named thoughtworks-conference-1.0.0-RELEASE.jar
in the target directory.

To run the application:-

```bash
$java -jar thoughtworks-conference-1.0.0-RELEASE.jar
```

You will be prompted to provide the filename of the input file:-

  Please enter the input filename:

When you have entered the filename, you will be able to choose which one of the 
two bin packing algorithms implemented to use:-

  Select scheduling method
   1. Next fit
   2. First fit
   Press <Enter> for default method
 
 The Next Fit algorithm is selected by default.

A future enhancement to the application would be to allow the user to specify 
whether to sort the input with respect to the duration of the talks. This
could result in a more optimal solution to the scheduling of the talks. 