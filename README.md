CS499Assignment3
================

This project contains three folders: PIQLE, Testing, and Part2.


The PIQLE folder is just the source code for the PIQLE library. It's required by our code for parts 3 and 4.

The Testing folder contains our solution for parts 1, 3, and 4. It contains an eclipse project for running jockey. It requires the PIQLE library to compile properly, but Eclipse can't seem to handle relative links when linking external source code, so you will have to update the project's link to the PIQLE folder manually (Since I assume that you won't be testing it from /cshome/tgriffit/c499/CS499Assignment3). If you run the code it will provide a GUI providing all of the possible actions. If any of the options that involve clustering are selected you will be asked to provide an arff file. The arff file we used in the demo is located at Testing/Cluster\ Data.arff.

The Part2 folder contains our solution for part 2. To run this part just open Part2 as project already containing source code in eclipse. The .classpath and .project files should still work because they only reference .jar files and so relative paths within Eclipse work in this case. To run this simply press run in eclipse and a UI will appear with the available keyboard commands on it. It is important to note that once the UI is open it will wait until the tracker.py software is also running in another terminal window. After the tracker is running you can press any of the keyboard commands. Specifically, 'd' will start the DEMO, 'r' will begin recording x, y, lightvalue data, and 'p' will pause the data collection. 't' is a testing mode that just outputs a bunch of data to console for testing and debugging purposes.

A version of Weka that includes SelfOrganizingMaps is also included within the Part2/weka-3-7-10/ directory.


