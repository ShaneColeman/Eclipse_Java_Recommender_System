package recommender.system.lib.rec.java.project;

import java.io.FileInputStream;
import java.util.Properties;

import net.librec.conf.Configuration;
import net.librec.job.RecommenderJob;
import net.librec.math.algorithm.Randoms;

public class RecSysConfigDriver 
{
	//public static String CONFIGURATION_FILE = "conf/UserKNN-CF.properties";
	public static String CONFIGURATION_FILE = "conf/ItemKNN-CF.properties";
	
	public static void main(String[] args) throws Exception
	{
		/*
		 * To fix log4j:WARN error
		 * https://stackoverflow.com/questions/12532339/no-appenders-could-be-found-for-loggerlog4j
		 */

		Configuration configuration = new Configuration();
		String configurationFilePath = CONFIGURATION_FILE;
		Properties properties = new Properties();
		properties.load(new FileInputStream(configurationFilePath));
		for(String name: properties.stringPropertyNames())
		{
			configuration.set(name, properties.getProperty(name));
		}
		
		if(configurationFilePath.equals("conf/UserKNN-CF.properties"))
		{
			System.out.println("User KNN Recommender\n");
		}
		else if(configurationFilePath.equals("conf/ItemKNN-CF.properties"))
		{
			System.out.println("Item KNN Recommender\n");
		}
		
		Randoms.seed(20171025); 
		RecommenderJob job = new RecommenderJob(configuration);
		job.runJob();
		
		System.out.println("\nFinished Recommendation Process\n");

		System.out.println("Data Model Class: " + job.getDataModelClass());
		System.out.println("Recommender Class: " + job.getRecommenderClass());
		System.out.println("Similarity Class: " + job.getSimilarityClass());
		System.out.println("Filter Class: " + job.getFilterClass());
		
		
		if(configurationFilePath.equals("conf/UserKNN-CF.properties") || configurationFilePath.equals("conf/ItemKNN-CF.properties"))
		{
			System.out.println("Number of KNN Neighbours: " + configuration.get("rec.neighbors.knn.number"));
			
			if(configuration.get("rec.recommender.isranking").equals("true"))
				System.out.println("Number of Top-Ns (Ranking): " + configuration.get("rec.recommender.ranking.topn"));
		}
		
	}
}