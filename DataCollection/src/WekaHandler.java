import java.io.FileInputStream;
import java.io.FileNotFoundException;
import weka.core.Instance;
import weka.core.Attribute;
import weka.core.SerializationHelper;
import weka.classifiers.functions.SMOreg;

public class WekaHandler {
	private SMOreg classifier;

	// credit:
	// http://stackoverflow.com/questions/8212980/weka-example-simple-classification-of-lines-of-text
	public WekaHandler(String modelfile) throws FileNotFoundException,
			Exception {

		classifier = (SMOreg) SerializationHelper.read(new FileInputStream(
				modelfile));

	}

	public double getClassification(int lightVal) {

		Instance instance = new Instance(1);
		instance.setValue(new Attribute("light"), lightVal);

		// PREDICTION
		try {
			return classifier.classifyInstance(instance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0.0;
	}

}
