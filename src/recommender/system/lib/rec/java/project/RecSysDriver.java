package recommender.system.lib.rec.java.project;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.Scanner;

import net.librec.conf.Configuration;
import net.librec.job.RecommenderJob;
import net.librec.math.algorithm.Randoms;

public class RecSysDriver 
{
	public static String input = "";
	public static String data = "";
	public static String CONFIGURATION_FILE = "";
	
	public static void main(String[] args) throws Exception
	{
		try
		{
			/*
			 * To fix log4j:WARN error
			 * https://stackoverflow.com/questions/12532339/no-appenders-could-be-found-for-loggerlog4j
			 */
			
			String recommenderText = "Please enter recommender system classification type (a, b, c or d):\n" + 
			 "a. 'User KNN Recommender'\nb. 'Item KNN Recommender'\n" + 
					"c. 'User Cluster Recommender'\nd. 'Item Cluster Recommender'"; 
			
			Scanner scanner = new Scanner(System.in);
			System.out.println(recommenderText);
			input =  scanner.nextLine();
			
			while(input.equals("") || !input.equals("a") && !input.equals("b") && !input.equals("c") && !input.equals("d"))
			{
				System.out.println("No recommender system classification type selected!\n" + recommenderText);
				input =  scanner.nextLine();
			}
			
			if(input.equals("a"))
				CONFIGURATION_FILE = "conf/user_knn.properties";
			else if(input.equals("b"))
				CONFIGURATION_FILE = "conf/item_knn.properties";
			else if(input.equals("c"))
				CONFIGURATION_FILE = "conf/user_cluster.properties";
			else if(input.equals("d"))
				CONFIGURATION_FILE = "conf/item_cluster.properties";
			//scanner.close();
			
			/*
			if(CONFIGURATION_FILE.equals("conf/user_knn.properties"))
			{
				System.out.println("\n#----------User KNN Recommender----------#");
			}
			else if(CONFIGURATION_FILE.equals("conf/item_knn.properties"))
			{
				System.out.println("\n#----------Item KNN Recommender----------#");
			}
			else if(CONFIGURATION_FILE.equals("conf/user_cluster.properties"))
			{
				System.out.println("\n#----------User Cluster Recommender----------#");
			}
			else if(CONFIGURATION_FILE.equals("conf/item_cluster.properties"))
			{
				System.out.println("\n#----------Item Cluster Recommender----------#");
			}
			*/
			
			Configuration configuration = new Configuration();
			
			String dataText = "\nPlease select data source (a or b):\na. Filmtrust\nb. NVD";
			System.out.println(dataText);
			input =  scanner.nextLine();
			
			while(input.equals("") || !input.equals("a") && !input.equals("b"))
			{
				System.out.println("No data source selected!" + dataText);
				input =  scanner.nextLine();
			}
			
			if(input.equals("a"))
			{
				data = "filmtrust/rating";
				configuration.set("data.input.path", data);
			}
			else if(input.equals("b"))
			{
				data = "nvd/txt";
				configuration.set("data.input.path", data);
			}
			scanner.close();
			
			String configurationFilePath = CONFIGURATION_FILE;
			
			if(CONFIGURATION_FILE.equals("conf/user_knn.properties"))
			{
				System.out.println("\n#----------User KNN Recommender----------#");
			}
			else if(CONFIGURATION_FILE.equals("conf/item_knn.properties"))
			{
				System.out.println("\n#----------Item KNN Recommender----------#");
			}
			else if(CONFIGURATION_FILE.equals("conf/user_cluster.properties"))
			{
				System.out.println("\n#----------User Cluster Recommender----------#");
			}
			else if(CONFIGURATION_FILE.equals("conf/item_cluster.properties"))
			{
				System.out.println("\n#----------Item Cluster Recommender----------#");
			}
			
			Properties properties = new Properties();
			properties.load(new FileInputStream(configurationFilePath));
			for(String name: properties.stringPropertyNames())
			{
				configuration.set(name, properties.getProperty(name));
			}
			
			Randoms.seed(2017); 
			RecommenderJob recommenderJob = new RecommenderJob(configuration);
			recommenderJob.runJob();
	
			System.out.println("\nData Model Class: " + recommenderJob.getDataModelClass() +
					"\nRecommender Class: " + recommenderJob.getRecommenderClass() +
					"\nSimilarity Class: " + recommenderJob.getSimilarityClass() +
					"\nFilter Class: " + recommenderJob.getFilterClass());
			
			System.out.println("\nFinished Recommendation Process");
		}
		catch(Exception e)
		{
			System.err.println("Exception" + e.getMessage());
		}
	}
}
