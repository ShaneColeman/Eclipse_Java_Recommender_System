package recommender.system.lib.rec.java.project;

import java.io.FileInputStream;
import java.util.Properties;

import net.librec.conf.Configuration;
import net.librec.job.RecommenderJob;
import net.librec.math.algorithm.Randoms;

public class UserKNN 
{
	public static String CONFIGURATION_FILE = "conf/user_knn.properties";
	
	public static void main(String[] args) throws Exception
	{
		/*
		 * To fix log4j:WARN error
		 * https://stackoverflow.com/questions/12532339/no-appenders-could-be-found-for-loggerlog4j
		 */
		
		
		System.out.print("UserKNN.java\n\n");
		
		Configuration configuration = new Configuration();
		String configurationFilePath = CONFIGURATION_FILE;
		Properties properties = new Properties();
		properties.load(new FileInputStream(configurationFilePath));
		for(String name: properties.stringPropertyNames())
		{
			configuration.set(name, properties.getProperty(name));
		}
		
		Randoms.seed(2017); 
		RecommenderJob job = new RecommenderJob(configuration);
		job.runJob();

		System.out.print("Finished");
		
	}
}