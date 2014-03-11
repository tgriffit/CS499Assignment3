CLASSPATH=json-simple-1.1.1.jar:.
#asg.cliche-020310.jar:Jama-1.0.3.jar:
all: TrackerReader.class Part2PC.class
#
TrackerReader.class: TrackerReader.java
	javac -cp $(CLASSPATH) TrackerReader.java

Part2PC.class: Part2PC.java
	nxjpcc -cp $(CLASSPATH) Part2PC.java

part2:all
	nxjpc -cp $(CLASSPATH) Part2PC

clean: 
	$(RM) *.class
	$(RM) *~
