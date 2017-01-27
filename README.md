# Build Prerequisites

* SBT 0.13
* Scala 2.11.8

# Building and Running

We assuming that current working directory is the project dir.

To  build jar file submit : 
```
sbt assembly
```

Run the calculation by command:
```
java -Xmx500m -jar target/scala-2.11/reviews.jar <path to Reviews.csv>
```

# Responses to Questions

#### How do you make sure that there are no duplicates in the file?

I'm not sure about it because I have no idea what it definitely means in the context of the data set.

#### We will be running this on machine with 500MB of RAM. How do you make sure that we are not using more than that? How are you going to monitor the memory usage of your program?

JVM option -Xmx500m ensures that the program does not use more than 500MB.
  
There are number of tools to monitor jvm memory usage: jstat, jconsole, jmc, VisualVM. Linux commands such as top, vmstat are also helpful.

### Our goal is to support the files with up to 100M reviews on multiple machines with 500MB of RAM and 4 core CPUs. How are you going to make it happen?

I see a few ways at first sight:

* Hadoop Map Reduce 
* Spark
* Akka, Akka Streaming

Of course this is not complete list, I beleive there are many other ways to think about.

