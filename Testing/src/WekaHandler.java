import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.classifiers.Classifier;

public class WekaHandler {
	private Classifier classifier;
	private Instances dataSet;

	// This was loosely based on the following code (but has changed substantially):
	// http://stackoverflow.com/questions/8212980/weka-example-simple-classification-of-lines-of-text
	public WekaHandler(String modelfile, String arffile, int part) throws FileNotFoundException,
			Exception {
		
		classifier = (Classifier) SerializationHelper.read(new FileInputStream(
					modelfile));
		
		BufferedReader reader = new BufferedReader(new FileReader(arffile));
		dataSet = new Instances(reader);
		dataSet.setClassIndex(dataSet.numAttributes() - 1);
	}

	public int getPart1Classification(int dist) {
		Instance instance = dataSet.firstInstance();
		instance.setValue(dataSet.attribute("pedDist"), dist);
		
		// PREDICTION
		double classLabelRet = 0.0;
		try {
			classLabelRet = classifier.classifyInstance(instance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (int)classLabelRet;
	}
	
	public MovementType getPart2Classification(int lightVal, int ultrasound) {
		Instance instance = dataSet.firstInstance();
		instance.setValue(dataSet.attribute("light"), lightVal);
		instance.setValue(dataSet.attribute("ultrasound"), ultrasound);
		
		double classLabelRet = 0.0;
		try {
			classLabelRet = classifier.classifyInstance(instance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return MovementType.intToMovementType((int)classLabelRet);
	}

	public MovementType getPart3Classification(int lightVal, int ultrasound, int rightIR, int leftIR) {
		Instance instance = dataSet.firstInstance();
		instance.setValue(dataSet.attribute("light"), lightVal);
		instance.setValue(dataSet.attribute("ultrasound"), ultrasound);
		instance.setValue(dataSet.attribute("rightIR"), rightIR);
		instance.setValue(dataSet.attribute("leftIR"), leftIR);
		
		double classLabelRet = 0.0;
		try {
			classLabelRet = classifier.classifyInstance(instance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return MovementType.intToMovementType((int)classLabelRet);
	}
	
	// This is an attempt to make Jockey react faster by having Weka handle fewer values. As far as
	// I can tell, it didn't work.
	public MovementType getPart3ClassificationNoUltrasound(int lightVal, int rightIR, int leftIR) {
		Instance instance = dataSet.firstInstance();
		instance.setValue(dataSet.attribute("light"), lightVal);
		instance.setValue(dataSet.attribute("rightIR"), rightIR);
		instance.setValue(dataSet.attribute("leftIR"), leftIR);
		
		double classLabelRet = 0.0;
		try {
			classLabelRet = classifier.classifyInstance(instance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return MovementType.intToMovementType((int)classLabelRet);
	}
}
