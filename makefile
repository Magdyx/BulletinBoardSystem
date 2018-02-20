JFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
        $(JC) $(JFLAGS) $*.java
        
CLASSES = \
        Foo.java \
        Blah.java \
        Library.java \
        Main.java 
       
default: classes

classes: $(CLASSES:.java=.class)

run: $(MAIN).class
	$(JVM) $(MAIN)

clean:
        $(RM) *.class
