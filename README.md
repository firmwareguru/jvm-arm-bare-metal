### So what is this?

Well, it's a Java bytecode execution engine, a.k.a. Java Virtual Machine, designed for bare metal execution on ARM Cortex-M3/M4 MCUs. 

### So what does it do? 

It executes Java bytecodes as defined in the Java Language Specification.  And that means you can write embedded firmware in "Java" and run such programs on the embedded target.  

"Java" is in quotes because there are a few restrictons and limitations to what you can do.  Since the VM has no garbage collector, like any good embedded developer you'll need to track the objects you create with explict 'new' calls.  Strings are handled in a special way.  Access to hardware resources is possible with Java class libraries.  Class files must be packaged into a special 'flash image' format with a tool (supplied). Object synchronization has not been implemented, and only basic Threading is possible (but it IS possible!)  Also, a limited core library is available, along with some basic hardware abstraction classes.  So yes, you can actually write to a Java 'Graphics' class and have the strings display on a hardware device. That is pretty cool, and that's why I did this.  While this was a personal passion project, it is fairly comprehensive with a complete cycle by cycle bytecode instruction simulator with memory visualizer, class content viewer, and packaging tools (written in regular Java for execution in a regular JVM).

Features of this JVM for bare-metal ARM Cortex-M:
- Executes directly in ARM registers R1-R15, with a couple of virtual registers at the start of RAM.  That's it!  No 'C' context, no context of any kind.  As bare-metal as it gets. 
- Entire VM written in pure assembly.  Basic java core library (e.g. java.lang) is available and custom implemented in, of course, Java.
- Most bytecodes implemented, including the hard ones like invokevirtual, invokestatic, tablelookup.
- Multi-threading! Thread class' start, sleep supported.
- Heap-based object and stack allocation.
- Compiled Java class files must be processed into a special flash image with the 'ClassPackager' tool before loading to the target.
- VM currently implemented in the 'old style' ARM assembly format.  

What's missing:
- Garbage collector.
- Object synchronization  (Thread.wait and synchronized blocks).
- Most of the java core library hasn't been implemented.

Just enough core library support has been implemented to support a demo of a multi-threaded program printing to a display.  More information on how to use this JVM will be forthcoming.
