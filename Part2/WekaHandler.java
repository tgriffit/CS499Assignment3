import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;
import weka.clusterers.SelfOrganizingMap;

public class WekaHandler {
	private SelfOrganizingMap som;
	private Instances dataSet;

	// This was loosely based on the following code (but has changed
	// substantially):
	// http://stackoverflow.com/questions/8212980/weka-example-simple-classification-of-lines-of-text
	public WekaHandler(String arfffile) {

		// Default weka options for each clusterer (with -N changed to 4)
		String[] somOptions = { "-L", "1.0", "-O", "2000", "-C", "1000", "-H",
				"2", "-W", "2" };

		ArffLoader loader = new ArffLoader();
		try {
			loader.setFile(new File(arfffile));
			dataSet = loader.getStructure();
		} catch (IOException e) {
			System.out.println("Error loading arff file)");
		}
		
		Instance i;
		try {
			while ((i = loader.getNextInstance(dataSet)) != null) {
				dataSet.add(i);
			}
		} catch (IOException e) {
			System.out.println("Error adding instances to dataSet");
		}
		
		try {
			som = (SelfOrganizingMap) SerializationHelper.read(new FileInputStream("SoMcluster.model"));
		} catch (FileNotFoundException e) {
			System.out.println("Could not find model file to load");
		} catch (Exception e) {
			System.out.println("Something else when wrong loading the model file");
		}
		
		/*
		som = new SelfOrganizingMap();
		try {
			som.setOptions(somOptions);
			som.buildClusterer(dataSet);
		} catch (Exception e) {
			System.out.println("Error creating SOM cluster");
		}
		*/
	}

	public int getSomCluster(double x, double y) {
		Instance instance = dataSet.firstInstance();
		instance.setValue(dataSet.attribute("x"), x);
		instance.setValue(dataSet.attribute("y"), y);

		// PREDICTION
		int cluster = -1;
		try {
			cluster = som.clusterInstance(instance);
		} catch (Exception e) {
			System.out.println("Error getting prediction in wekahandler");
		}
		return cluster;
	}

}
