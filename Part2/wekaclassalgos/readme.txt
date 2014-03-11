WEKA Classification Algorithms (A WEKA Plug-in)
http://sourceforge.net/projects/wekaclassalgos
By Jason Brownlee
===============================================================================

This project provides implementation for a number of artificial neural network
(ANN) and artificial immune system (AIS) based classification algorithms for 
the WEKA (Waikato Environment for Knowledge Analysis) machine learning workbench. 
The WEKA platform was selected for the implementation of the selected algorithms 
because I think its an excellent piece of free software. 

The WEKA project is required to run the algorithms provided in this project, and 
a version of WEKA is included in this download. This is an open source project 
(released under the GPL) so the source code is available.


To install:
===============================================================================
1. Download from: http://sourceforge.net/projects/wekaclassalgos/
2. Unzip, for example /weka
3. To Run:
	a. Windows: run.bat
	b. Mac/Linux: run.sh (you might have to make it executable: chmod a+x ./run.sh)
	c. Command Line: java -classpath weka;wekaclassalgos.jar weka.gui.GUIChooser
4. The new algorithms will appear in the Explorer and Experimenter GUI's
   
For more information about WEKA, see: 
* http://sourceforge.net/projects/weka
* http://www.cs.waikato.ac.nz/~ml/


Software Versions:
===============================================================================
WEKA: 3-6-4
Java: 1.6


Release Notes:
===============================================================================
* Version 1.8, May 2011
	* Updated to WEKA 3.6.4
	* Updated AIS Models to support changes to the WEKA API

* Version 1.7, September 2007
	* Added a build script 
	* Included WEKA and example datasets in the release
	* Updated the website
	* Generate Javadoc in build and include on website

* Version 1.6, March 2006
      o Consolidated Immunological and Neural Network software libraries into a single package

* Version 1.5, August 2005
      o Changed algorithm definition from old GenericObjectEditor.props to new GenericPropertiesCreator.props method
      
* Version 1.4, January 2005
      o Added AIRS, CLONALG, Immunos, algorithms
      o Compiled with JDK 1.5 (5.0) (no 1.5 features used)
      
* Version 1.4, June 2004
      o Fixed issues when using algorithms from the command line
      o Added hooks for epoch event handling (useful for external graphing of algorithms progress)
      
* Version 1.3, May 2004
      o Added SOM algorithm
      o Added Multi-Pass SOM algorithm
      o Simplified Multi-Pass LVQ GUI interface
      o More useful algorithm help
      o Speed improvements
      o Fixed classification bug in HierarchalLVQ algorithm
      o Faster model initialisation
      o Added many additional model initialisation techniques (Random Values, Knn, SimpleKMeans Clustering, Farthest First Clustering)
      o More useful algorithm debug information
      o Quantization error reporting (debug)

* Version 1.2, May 2004
      o Changed all algorithms from batch to single instance mode (same as LVQ_PAK)
      o Order of magnitude performance increase
      o Performance improvement in distance measure, affects datasets with many attributes
      o Added HierarchalLVQ algorithm
      o Added OLVQ3 algorithm
      o Added Perceptron algorithm
      o Added Widrow Hoff algorithm
      o Added Back Propagation algorithm
      o Added Bold Driver Back Propagation algorithm
      o Added new filter for normalising data for Tanh transfer function called NormaliseMidpointZero
      o LVQ Bug fixes
      
* Version 1.1, August 2003
      o Added a main to MultiPassLVQ
      o Added sample code which shows how to use an LVQ model in standalone application

* Version 1.0, July 2003, initial version

#EOF