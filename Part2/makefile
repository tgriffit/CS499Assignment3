CLASSPATH=json-simple-1.1.1.jar:.
#asg.cliche-020310.jar:Jama-1.0.3.jar:
all: TrackerReader.class Demo.class
#
TrackerReader.class: TrackerReader.java
	javac -cp $(CLASSPATH) TrackerReader.java

Demo.class: Demo.java
	nxjpcc -cp $(CLASSPATH) Demo.java

part2:all
	nxjpc -cp $(CLASSPATH) Demo

clean: 
	$(RM) *.class
	$(RM) *~
