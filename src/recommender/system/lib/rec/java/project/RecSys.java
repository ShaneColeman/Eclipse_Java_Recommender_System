package recommender.system.lib.rec.java.project;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import net.librec.common.LibrecException;
import net.librec.conf.Configuration;
//import net.librec.data.convertor.ArffDataConvertor;
//import net.librec.data.model.ArffDataModel;
//import net.librec.data.model.ArffInstance;
import net.librec.job.RecommenderJob;
import net.librec.math.algorithm.Randoms;
//import net.librec.math.structure.SparseTensor;

public class RecSys 
{
	private String input = "";
	private String data = "";
	private String similarity = "";
	private String knnNeighboursNumber = "";
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
			
			//Configuration Instantiation Test Location 1 - (Can remove as not required at this code location)
			//configuration = new Configuration();
			
			recommenderSelector();
			
			//Configuration Setup Test Location 1 - (Can remove as not required at this code location)
			//configurationSetup();
			
			//Configuration Instantiation Test Location 2 - (Can remove as not required at this code location)
			//configuration = new Configuration();
			
			dataSelector();
			
			//dataTXT();
			
			configurationFile();
			
			scanner.close();
			
			//Configuration Setup Test Location 2 - (Can remove as not required at this code location)
			//configurationSetup();
			
			Randoms.seed(2017); 
			
			recommenderJob();
			
			System.out.println("\nFinished Recommendation Process");
		}
		catch(Exception e)
		{
			System.err.println("\nException: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void recommenderSelector() throws FileNotFoundException, IOException
	{
		String recommenderText = "Please select recommender system technique:\n" + 
				"\tBaseline Recommendation Algorithms:\n" +
					"\t\ta. User Cluster Recommender\n\t\tb. Item Cluster Recommender\n" + 
					"\tCollaborative Filtering Recommendation Algorithms:\n" +
						"\t\tc. User KNN Recommender\n\t\td. Item KNN Recommender"; 

		System.out.println(recommenderText);
		input =  scanner.nextLine();
			
		while(input.equals("") || !input.equals("a") && !input.equals("b") && !input.equals("c") && !input.equals("d"))
		{
			System.out.println("\nNo recommender system technique selected!\n" + recommenderText);
			input =  scanner.nextLine();
		}	
				
		if(input.equals("a"))
			configurationFile = "conf/user_cluster.properties";
		else if(input.equals("b"))
			configurationFile = "conf/item_cluster.properties";
		else if(input.equals("c"))
			configurationFile = "conf/user_knn.properties";
		else if(input.equals("d"))
			configurationFile = "conf/item_knn.properties";
		
		configurationSetup();
	}
	
	private void configurationSetup() throws FileNotFoundException, IOException
	{
		//https://www.youtube.com/watch?v=Zoaoc12wms8
		
		String configurationFilePath = configurationFile;
		configuration = new Configuration();
		properties = new Properties();
		properties.load(new FileInputStream(configurationFilePath));
		
		for(String name: properties.stringPropertyNames())
		{
			configuration.set(name, properties.getProperty(name));
		}
	}
	
	private void dataSelector() throws IOException, LibrecException
	{
		String dataSelectorText = "Please select data format:\na. TXT (Text File Format)" + 
									"\nb. ARFF (Attribute-Relation File Format)";
		System.out.println("\n" + dataSelectorText);
		input = scanner.nextLine();
		
		while(input.equals("") || !input.equals("a") && !input.equals("b"))
		{
			System.out.println("\nNo data format selected!\n" + dataSelectorText);
			input = scanner.nextLine();
		}
		
		if(input.equals("a"))
			dataTXT();
		else if(input.equals("b"))
			dataARFF();
	}
	
	private void dataTXT()
	{
		configuration.set("data.model.format", "text");
		
		String dataText = "Please select data source:\na. Filmtrust (Pre-Defined Test Dataset (LibRec))" + 
							"\nb. National Vulnerability Database (NVD)" + 
							"\nc. MovieLens";
		System.out.println("\n" + dataText);
		input =  scanner.nextLine();
		
		while(input.equals("") || !input.equals("a") && !input.equals("b") && !input.equals("c")) 
		{
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
		else if(input.equals("c"))
		{
			data = "movielens";
			configuration.set("data.input.path", data);
		}
	}
	
	private void dataARFF() throws IOException, LibrecException
	{
		
		configuration.set("data.model.format", "arff");
		
		configuration.set("data.input.path", "arfftest/data.arff");
		//ArffDataConvertor arffDataConvertor = new ArffDataConvertor(configuration.get("data.input.path"));
		//arffDataConvertor.readData();
		
		//ArffDataModel arffDataModel = new ArffDataModel(configuration);
		//arffDataModel.buildDataModel();
		
		//SparseTensor sparseTensor = arffDataConvertor.getSparseTensor();
		//ArrayList<ArffInstance> instances = arffDataConvertor.getInstances();
		//String s1 = arffDataConvertor.getRelationName();
		//String s2 = arffDataConvertor.getAttributes().get(0).getName();
		
		/*
		configuration.set("data.model.format", "arff");
		
		data = "arfftest/data.arff";
		configuration.set("data.input.path", data);
		*/
		
		//String dataARFF = "";
		
		/*
		System.out.println("\nARFF not initially set up... now terminating application!");
		System.exit(0);
		*/
	}
	
	private void configurationFile()
	{
		if(configurationFile.equals("conf/user_cluster.properties"))
		{
			similarity();
			
			System.out.println("\n#----------User Cluster Recommender----------#");
		}
		else if(configurationFile.equals("conf/item_cluster.properties"))
		{
			similarity();
			
			System.out.println("\n#----------Item Cluster Recommender----------#");
		}
		else if(configurationFile.equals("conf/user_knn.properties"))
		{
			similarity();
			
			setKNNNeighboursNumber();
			
			System.out.println("\n#----------User KNN Recommender----------#");
		}
		else if(configurationFile.equals("conf/item_knn.properties"))
		{
			similarity();

			setKNNNeighboursNumber();
			
			System.out.println("\n#----------Item KNN Recommender----------#");
		}
	}
	
	private void similarity()
	{
		String similarityText = "Please select similarity class type:" + 
									"\na. Pearson Correlation Coefficient (PCC)" +
										"\nb. Jaccard" +
											"\nc. ExJaccard" +
												"\nd. Cosine";
		
		System.out.println("\n" + similarityText);
		input = scanner.nextLine();
		
		while(input.equals("") || !input.equals("a") && !input.equals("b") && !input.equals("c") && !input.equals("d"))
		{
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
		else if(input.equals("c"))
		{
			similarity = "exjaccard";
			configuration.set("rec.similarity.class",similarity);
		}
		else if(input.equals("d"))
		{
			similarity = "cos";
			configuration.set("rec.similarity.class",similarity);
		}
	}
	
	private void setKNNNeighboursNumber()
	{
		String knnText = "Please enter the number of KNN neighbours: ";
		//String knnNeighboursNumberDefault = "50";
		int knn = 0;
		int inputInt;
		
		System.out.println("\n" + knnText);
		input = scanner.nextLine();
		
		while(input.equals(""))
		{
			System.out.println("\nNo number of KNN neighbours entered, please re-enter:");
			input = scanner.nextLine();
		}
		
		knnNeighboursNumber = input;
		
		//Test
		knn = Integer.parseInt(knnNeighboursNumber);
		
		while(knn <= 0)
		{
			System.out.println("\nNumber of KNN neighbours cannot be zero or less, please re-enter:");
			inputInt = scanner.nextInt();
			knn = inputInt;
			
			/*
			System.out.println("Setting default KNN neighbours from " + knn + " to " + knnNeighboursNumberDefault );
			knn = Integer.parseInt(knnNeighboursNumberDefault);
			*/
		}
		
		//knn = Integer.parseInt(knnNeighboursNumberDefault);
		
		knnNeighboursNumber = Integer.toString(knn);
		
		configuration.set("rec.neighbors.knn.number", knnNeighboursNumber);
	}
	
	private String getKNNNeighboursNumber()
	{
		return configuration.get("rec.neighbors.knn.number");
	}
	
	private void recommenderJob() throws ClassNotFoundException, LibrecException, IOException
	{
		//https://www.youtube.com/watch?v=Zoaoc12wms8
		
		recommenderJob = new RecommenderJob(configuration);
		recommenderJob.runJob();
		
		System.out.println("\n#----------Summary Information----------#");
		System.out.println("Data Model Class: " + recommenderJob.getDataModelClass() +
							"\nRecommender Class: " + recommenderJob.getRecommenderClass() +
								"\nSimilarity Class: " + recommenderJob.getSimilarityClass() +
									"\nFilter Class: " + recommenderJob.getFilterClass());
		
		if(configurationFile.equals("conf/user_knn.properties") || configurationFile.equals("conf/item_knn.properties"))
			System.out.println("KNN Neighbours Number: " + getKNNNeighboursNumber());
	}
	
	/*
	//Test Filtering Method
	private void filter()
	{
		List<String> userIDList = new ArrayList<String>();
		List<String> itemIDList = new ArrayList<String>();
		
		userIDList.add("");
		itemIDList.add("");
		
		List<RecommendedItem> recommendedItemList = recommenderJob;
	}
	*/
}
