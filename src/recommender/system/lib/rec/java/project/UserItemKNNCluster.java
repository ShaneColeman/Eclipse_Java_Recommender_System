package recommender.system.lib.rec.java.project;

import java.io.FileInputStream;
import java.util.Properties;

import net.librec.conf.Configuration;
import net.librec.job.RecommenderJob;
import net.librec.math.algorithm.Randoms;

public class UserItemCluster 
{
	public static String CONFIGURATION_FILE = "conf/item_cluster.properties";
	//public static String CONFIGURATION_FILE = "conf/user_cluster.properties";
	
	public static void main(String[] args) throws Exception
	{
		/*
		 * To fix log4j:WARN error
		 * https://stackoverflow.com/questions/12532339/no-appenders-could-be-found-for-loggerlog4j
		 */
		
		if(CONFIGURATION_FILE == "conf/item_cluster.properties")
		{
			System.out.print("Item Cluster Recommender\n\n");
		}
		else if(CONFIGURATION_FILE == "conf/user_cluster.properties")
		{
			System.out.print("User Cluster Recommender\n\n");
		}
		
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

		System.out.println("Data Model Class: " + job.getDataModelClass());
		System.out.println("Recommender Class: " + job.getRecommenderClass());
		System.out.println("Similarity Class: " + job.getSimilarityClass());
		System.out.println("Filter Class: " + job.getFilterClass());
		
		System.out.print("Finished Recommendation Process");
	}
}