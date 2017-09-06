package recommender.system.lib.rec.java.project;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import net.librec.common.LibrecException;
import net.librec.conf.Configuration;
import net.librec.job.RecommenderJob;
import net.librec.math.algorithm.Randoms;

public class RecSys 
{
	private String input = "";
	private String data = "";
	private String similarity = "";
	private String configurationFile = "";
	
	private Configuration configuration;
	private Properties properties;
	private RecommenderJob recommenderJob;
	private Scanner scanner;
	
	public RecSys()
	{
		try
		{
			//To fix log4j:WARN error
			//https://stackoverflow.com/questions/12532339/no-appenders-could-be-found-for-loggerlog4j
			
			scanner = new Scanner(System.in);
			
			configuration = new Configuration();
			
			recommender();
			
			//configuration = new Configuration();
			
			dataTXT();
			
			configurationFile();
			
			scanner.close();
			
			configurationSetup();
			
			Randoms.seed(2017); 
			
			recommenderJob();
			
			System.out.println("\nFinished Recommendation Process");
		}
		catch(Exception e)
		{
			System.err.println("Exception" + e.getMessage());
		}
	}
	
	private void recommender()
	{
		String recommenderText = "Please enter recommender system classification type (a, b, c or d):\n" + 
				 "a. 'User KNN Recommender'\nb. 'Item KNN Recommender'\n" + 
						"c. 'User Cluster Recommender'\nd. 'Item Cluster Recommender'"; 
				
		//scanner = new Scanner(System.in);
		System.out.println(recommenderText);
		input =  scanner.nextLine();
				
		while(input.equals("") || !input.equals("a") && !input.equals("b") && !input.equals("c") && !input.equals("d"))
		{
			//System.err.println("\nNo recommender system classification type selected!");
			System.out.println("\nNo recommender system classification type selected!\n" + recommenderText);
			input =  scanner.nextLine();
		}
				
		if(input.equals("a"))
			configurationFile = "conf/user_knn.properties";
		else if(input.equals("b"))
			configurationFile = "conf/item_knn.properties";
		else if(input.equals("c"))
			configurationFile = "conf/user_cluster.properties";
		else if(input.equals("d"))
			configurationFile = "conf/item_cluster.properties";
	}
	
	private void dataTXT()
	{
		String dataText = "Please select data source (a or b):\na. Filmtrust\nb. NVD";
		System.out.println("\n" + dataText);
		input =  scanner.nextLine();
		
		while(input.equals("") || !input.equals("a") && !input.equals("b"))
		{
			//System.err.println("\nNo data source selected!");
			System.out.println("\nNo data source selected!\n" + dataText);
			input =  scanner.nextLine();
		}
		
		if(input.equals("a"))
		{
			data = "filmtrust";
			configuration.set("data.input.path", data);
		}
		else if(input.equals("b"))
		{
			data = "nvd/txt";
			configuration.set("data.input.path", data);
		}
	}
	
	private void configurationFile()
	{
		if(configurationFile.equals("conf/user_knn.properties"))
		{
			similarity();
			
			System.out.println("\n#----------User KNN Recommender----------#");
		}
		else if(configurationFile.equals("conf/item_knn.properties"))
		{
			similarity();
			
			System.out.println("\n#----------Item KNN Recommender----------#");
		}
		else if(configurationFile.equals("conf/user_cluster.properties"))
		{
			System.out.println("\n#----------User Cluster Recommender----------#");
		}
		else if(configurationFile.equals("conf/item_cluster.properties"))
		{
			System.out.println("\n#----------Item Cluster Recommender----------#");
		}
	}
	
	private void configurationSetup() throws FileNotFoundException, IOException
	{
		String configurationFilePath = configurationFile;
		
		properties = new Properties();
		properties.load(new FileInputStream(configurationFilePath));
		
		for(String name: properties.stringPropertyNames())
		{
			configuration.set(name, properties.getProperty(name));
		}
	}
	
	private void similarity()
	{
		String similarityText = "Please select similarity class type (a or b):" + 
				"\na. Pearson Correlation Coefficient (PCC)" +
				"\nb. Jaccard";
		
		System.out.println("\n" + similarityText);
		input = scanner.nextLine();
		
		while(input.equals("") || !input.equals("a") && !input.equals("b"))
		{
			//System.err.println("\nNo similarity class selected!");
			System.out.println("\nNo similarity class selected!\n" + similarityText);
			input =  scanner.nextLine();
		}
		
		if(input.equals("a"))
		{
			similarity = "pcc";
			configuration.set("rec.similarity.class",similarity);
		}
		else if(input.equals("b"))
		{
			similarity = "jaccard";
			configuration.set("rec.similarity.class",similarity);
		}
	}
	
	private void recommenderJob() throws ClassNotFoundException, LibrecException, IOException
	{
		recommenderJob = new RecommenderJob(configuration);
		recommenderJob.runJob();
		
		System.out.println("\nData Model Class: " + recommenderJob.getDataModelClass() +
				"\nRecommender Class: " + recommenderJob.getRecommenderClass() +
				"\nSimilarity Class: " + recommenderJob.getSimilarityClass() +
				"\nFilter Class: " + recommenderJob.getFilterClass());
	}
}
