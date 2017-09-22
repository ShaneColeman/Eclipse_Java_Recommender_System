package recommender.system.lib.rec.java.project;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//import java.util.ArrayList;
//import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.apache.log4j.lf5.util.Resource;

import net.librec.common.LibrecException;
import net.librec.conf.Configuration;
//import net.librec.data.convertor.ArffDataConvertor;
//import net.librec.data.model.ArffDataModel;
//import net.librec.data.model.ArffInstance;
import net.librec.job.RecommenderJob;
import net.librec.math.algorithm.Randoms;
import net.librec.recommender.item.RecommendedItem;
//import net.librec.math.structure.SparseTensor;

public class RecSys 
{
	private String input = "";
	private String data = "";
	//private String similarity = "";
	//private String knnNeighboursNumber = "";
	private String configurationFile = "";
	
	private Configuration configuration;
	//private Properties properties;
	//private RecommenderJob recommenderJob;
	private Scanner scanner;
	
	public RecSys(String[] args)
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
			
			//Randoms.seed(2017); 
			
			recommenderJob();
			
			//System.out.println("\nFinished Recommendation Process");
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
		
		configuration = new Configuration();
		
		String configurationFilePath = configurationFile;
		Properties properties = new Properties();
		properties.load(new FileInputStream(configurationFilePath));
		
		for(String name: properties.stringPropertyNames())
		{
			configuration.set(name, properties.getProperty(name));
		}
		
		System.out.println("\nConfiguration File Path: " + configurationFilePath);
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
		//configuration.set("dfs.data.dir", "data");
		System.out.println("\nData Directory: " + configuration.get("dfs.data.dir"));
		
		configuration.set("data.model.format", "text");
		System.out.println("Data Model Format: " + configuration.get("data.model.format"));
		
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
			data = "filmtrust/rating";
			configuration.set("data.input.path", data);
			System.out.println("\nData Input Path: " + configuration.get("data.input.path"));
		}
		else if(input.equals("b"))
		{
			data = "nvd/txt";
			configuration.set("data.input.path", data);
			System.out.println("\nData Input Path: " + configuration.get("data.input.path"));
		}
		else if(input.equals("c"))
		{
			data = "movielens";
			configuration.set("data.input.path", data);
			System.out.println("\nData Input Path: " + configuration.get("data.input.path"));
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
			similaritySelector();
			
			//evaluator();
			
			System.out.println("\n#----------User Cluster Recommender----------#");
		}
		else if(configurationFile.equals("conf/item_cluster.properties"))
		{
			similaritySelector();
			
			//evaluator();
			
			System.out.println("\n#----------Item Cluster Recommender----------#");
		}
		else if(configurationFile.equals("conf/user_knn.properties"))
		{
			similaritySelector();
			
			//setKNNNeighboursNumber();
			
			evaluator();
			
			System.out.println("\n#----------User KNN Recommender----------#");
		}
		else if(configurationFile.equals("conf/item_knn.properties"))
		{
			similaritySelector();

			//setKNNNeighboursNumber();
			
			evaluator();
			
			System.out.println("\n#----------Item KNN Recommender----------#");
		}
	}
	
	private void similaritySelector()
	{
		String similarity = "";
		String similarityText = "Please select similarity class type:" + 
									"\na. Binary Cosine (bcos)" +
										"\nb. Cosine (cos)" +
											"\nc. Constrained Pearson Correlation (cpc)" +
												"\nd. Mean Square Error (msesim)" +
													"\ne. Mean Square Difference (msd)" +
														"\nf. Pearson Correlation Coefficient (pcc)" +
															"\ng. Kendall Rank Correlation Coefficient (krcc)" +
																"\nh. Dice Coefficient (dice)" +
																	"\ni. Jaccard (jaccard)" +
																		"\nj. ExJaccard (exjaccard)";
		
		System.out.println("\n" + similarityText);
		input = scanner.nextLine();
		
		while(input.equals("") || !input.equals("a") && !input.equals("b") && !input.equals("c") && !input.equals("d") &&
				!input.equals("e") && !input.equals("f") && !input.equals("g") && !input.equals("h") && !input.equals("i") &&
				!input.equals("j"))
		{
			System.out.println("\nNo similarity class selected!\n" + similarityText);
			input =  scanner.nextLine();
		}
		
		if(input.equals("a"))
		{
			similarity = "bcos";
			configuration.set("rec.similarity.class",similarity);
			System.out.println("\nRecommender Similarity Class: " + configuration.get("rec.similarity.class"));
		}
		else if(input.equals("b"))
		{
			similarity = "cos";
			configuration.set("rec.similarity.class",similarity);
			System.out.println("\nRecommender Similarity Class: " + configuration.get("rec.similarity.class"));
		}
		else if(input.equals("c"))
		{
			similarity = "cpc";
			configuration.set("rec.similarity.class",similarity);
			System.out.println("\nRecommender Similarity Class: " + configuration.get("rec.similarity.class"));
		}
		else if(input.equals("d"))
		{
			similarity = "msesim";
			configuration.set("rec.similarity.class",similarity);
			System.out.println("\nRecommender Similarity Class: " + configuration.get("rec.similarity.class"));
		}
		else if(input.equals("e"))
		{
			similarity = "msd";
			configuration.set("rec.similarity.class",similarity);
			System.out.println("\nRecommender Similarity Class: " + configuration.get("rec.similarity.class"));
		}
		else if(input.equals("f"))
		{
			similarity = "pcc";
			configuration.set("rec.similarity.class",similarity);
			System.out.println("\nRecommender Similarity Class: " + configuration.get("rec.similarity.class"));
		}
		else if(input.equals("g"))
		{
			similarity = "krcc";
			configuration.set("rec.similarity.class",similarity);
			System.out.println("\nRecommender Similarity Class: " + configuration.get("rec.similarity.class"));
		}
		else if(input.equals("h"))
		{
			similarity = "dice";
			configuration.set("rec.similarity.class",similarity);
			System.out.println("\nRecommender Similarity Class: " + configuration.get("rec.similarity.class"));
		}
		else if(input.equals("i"))
		{
			similarity = "jaccard";
			configuration.set("rec.similarity.class",similarity);
			System.out.println("\nRecommender Similarity Class: " + configuration.get("rec.similarity.class"));
		}
		else if(input.equals("j"))
		{
			similarity = "exjaccard";
			configuration.set("rec.similarity.class",similarity);
			System.out.println("\nRecommender Similarity Class: " + configuration.get("rec.similarity.class"));
		}
	}
	
	private void setKNNNeighboursNumber()
	{
		String knnNeighboursNumber = "";
		int knn = 0;
		int inputInt = 0;
		
		String knnText = "Please enter the number of KNN neighbours: ";
		System.out.println("\n" + knnText);
		input = scanner.nextLine();
		
		while(input.equals(""))
		{
			System.out.println("\nNo number of KNN neighbours entered, please re-enter:");
			input = scanner.nextLine();
		}
		
		if(!input.equals(""))
		{
		knnNeighboursNumber = input;
		
		//Test
		knn = Integer.parseInt(knnNeighboursNumber);
		
		while(knn <= 0)
		{
			System.out.println("\nNumber of KNN neighbours cannot be zero or less, please re-enter:");
			inputInt = scanner.nextInt();
			knn = inputInt;
		}
		
		knnNeighboursNumber = Integer.toString(knn);
		
		configuration.set("rec.neighbors.knn.number", knnNeighboursNumber);
		
		System.out.println("\nNumber of KNN Neighbours: " + getKNNNeighboursNumber());
		}
	}
	
	private String getKNNNeighboursNumber()
	{
		return configuration.get("rec.neighbors.knn.number");
	}
	
	private void evaluator()
	{
		String ratingRankingSelectorText = "Please select recommender evaluator type: " +
												"\na. Rating" +
													"\nb. Ranking";
		System.out.println("\n" + ratingRankingSelectorText);
		input = scanner.nextLine();
		
		while(input.equals("") || !input.equals("a") && !input.equals("b"))
		{
			System.out.println("\nNo recommender evaluator type selected!\n" + ratingRankingSelectorText);
			input = scanner.nextLine();
		}
		
		if(input.equals("a"))
		{
			configuration.set("rec.eval.enable", "true");
			configuration.set("rec.recommender.isranking","false");
			
			System.out.println("\nRecommender Evaluation Enabled: " + configuration.get("rec.eval.enable"));
			System.out.println("Recommender (Is Ranking?): " + configuration.get("rec.recommender.isranking"));
			
			String ratingEvaluatorSelectorText = "Please select rating evaluator: " +
													"\na. All Rating Evaluators" +
														"\nb. MAE" +
															"\nc. MPE" +
																"\nd. MSE" + 
																	"\ne. RSME";
			System.out.println("\n" + ratingEvaluatorSelectorText);
			input = scanner.nextLine();
			
			while(input.equals("") || !input.equals("a") && !input.equals("b") && !input.equals("c") && !input.equals("d")
					&& !input.equals("e"))
			{
				System.out.println("\nNo rating evaluator selected!\n" + ratingEvaluatorSelectorText);
				input = scanner.nextLine();
			}
			
			if(input.equals("a"))
			{
				configuration.set("rec.eval.classes", "");
				System.out.println("\nRecommender Evaluation Class: " + configuration.get("rec.eval.classes"));
			}	
			else if(input.equals("b"))
			{
				configuration.set("rec.eval.classes", "mae");
				System.out.println("\nRecommender Evaluation Class: " + configuration.get("rec.eval.classes"));
			}	
			else if(input.equals("c"))
			{
				configuration.set("rec.eval.classes", "mpe");
				System.out.println("\nRecommender Evaluation Class: " + configuration.get("rec.eval.classes"));
			}	
			else if(input.equals("d"))
			{
				configuration.set("rec.eval.classes", "mse");
				System.out.println("\nRecommender Evaluation Class: " + configuration.get("rec.eval.classes"));
			}	
			else if(input.equals("e"))
			{
				configuration.set("rec.eval.classes", "rmse");
				System.out.println("\nRecommender Evaluation Class: " + configuration.get("rec.eval.classes"));
			}	
		}
		else if(input.equals("b"))
		{
			String topN = "3";
			
			configuration.set("rec.eval.enable", "true");
			configuration.set("rec.recommender.isranking","true");
			
			System.out.println("\nRecommender Evaluation Enabled: " + configuration.get("rec.eval.enable"));
			System.out.println("Recommender (Is Ranking?): " + configuration.get("rec.recommender.isranking"));
			
			String rankingEvaluatorSelectorText = "Please select rating evaluator: " +
													"\na. All Ranking Evaluators" + 
														"\nb. AUC" +
															"\nc. AP" +
																"\nd. NDCG" +
																	"\ne. PRECISION" +
																		"\nf. RECALL" +
																			"\ng. RR";
			System.out.println("\n" + rankingEvaluatorSelectorText);
			input = scanner.nextLine();
			
			while(input.equals("") || !input.equals("a") && !input.equals("b") && !input.equals("c") && !input.equals("d") 
					&& !input.equals("e") && !input.equals("f") && !input.equals("g"))
			{
				System.out.println("\nNo rating evaluator selected!\n" + rankingEvaluatorSelectorText);
				input = scanner.nextLine();
			}
			
			if(input.equals("a"))
			{
				configuration.set("rec.eval.classes", "");
				configuration.set("rec.recommender.ranking.topn", topN);
				
				System.out.println("\nRecommender Evaluation Class: " + configuration.get("rec.eval.classes"));
				System.out.println("Recommender Ranking Top N: " + configuration.get("rec.recommender.ranking.topn"));
			}
			else if(input.equals("b"))
			{
				configuration.set("rec.eval.classes", "auc");
				configuration.set("rec.recommender.ranking.topn", topN);
				
				System.out.println("\nRecommender Evaluation Class: " + configuration.get("rec.eval.classes"));
				System.out.println("Recommender Ranking Top N: " + configuration.get("rec.recommender.ranking.topn"));
			}
			else if(input.equals("c"))
			{
				configuration.set("rec.eval.classes", "ap");
				configuration.set("rec.recommender.ranking.topn", topN);
				
				System.out.println("\nRecommender Evaluation Class: " + configuration.get("rec.eval.classes"));
				System.out.println("Recommender Ranking Top N: " + configuration.get("rec.recommender.ranking.topn"));
			}
			else if(input.equals("d"))
			{
				configuration.set("rec.eval.classes", "ndcg");
				configuration.set("rec.recommender.ranking.topn", topN);
				
				System.out.println("\nRecommender Evaluation Class: " + configuration.get("rec.eval.classes"));
				System.out.println("Recommender Ranking Top N: " + configuration.get("rec.recommender.ranking.topn"));
			}
			else if(input.equals("e"))
			{
				configuration.set("rec.eval.classes", "precision");
				configuration.set("rec.recommender.ranking.topn", topN);
				
				System.out.println("\nRecommender Evaluation Class: " + configuration.get("rec.eval.classes"));
				System.out.println("Recommender Ranking Top N: " + configuration.get("rec.recommender.ranking.topn"));
			}
			else if(input.equals("f"))
			{
				configuration.set("rec.eval.classes", "recall");
				configuration.set("rec.recommender.ranking.topn", topN);
				
				System.out.println("\nRecommender Evaluation Class: " + configuration.get("rec.eval.classes"));
				System.out.println("Recommender Ranking Top N: " + configuration.get("rec.recommender.ranking.topn"));
			}
			else if(input.equals("g"))
			{
				configuration.set("rec.eval.classes", "rr");
				configuration.set("rec.recommender.ranking.topn", topN);
				
				System.out.println("\nRecommender Evaluation Class: " + configuration.get("rec.eval.classes"));
				System.out.println("Recommender Ranking Top N: " + configuration.get("rec.recommender.ranking.topn"));
			}
		}
	}
	
	private void recommenderJob() throws ClassNotFoundException, LibrecException, IOException
	{
		//https://www.youtube.com/watch?v=Zoaoc12wms8
		
		//Test Code - Can remove if it does not work
		//Configuration.Resource resource = new Configuration.Resource("rec/cf/itemknn-test.properties");
		//configuration.addResource(resource);
		//configuration.set("rec.recommender.verbose", "true");
		
		Randoms.seed(201709); 
		RecommenderJob recommenderJob = new RecommenderJob(configuration);
		recommenderJob.runJob();
		System.out.println("\nFinished Recommendation Process");
		
		System.out.println("\n#----------Summary Information----------#");
		System.out.println("Data Model Class: " + recommenderJob.getDataModelClass() +
							"\nRecommender Class: " + recommenderJob.getRecommenderClass() +
								"\nSimilarity Class: " + recommenderJob.getSimilarityClass() +
									"\nFilter Class: " + recommenderJob.getFilterClass());
		
		if(configurationFile.equals("conf/user_knn.properties") || configurationFile.equals("conf/item_knn.properties"))
		{
			//System.out.println("KNN Neighbours Number: " + getKNNNeighboursNumber());
			System.out.println("KNN Neighbours Number: " + configuration.get("rec.neighbors.knn.number"));
		}
		
		//saveRecommenderResults();
	}
	
	/*
	//Test saveRecommenderResults() Method
	public void saveRecommenderResults() throws ClassNotFoundException, LibrecException, IOException
	{
		List<RecommendedItem> recommendedItemList = new ArrayList<RecommendedItem>();
		recommenderJob.saveResult(recommendedItemList);
		System.out.println("Size: " + recommendedItemList.size());
		
		Iterator<RecommendedItem> iterator = recommendedItemList.iterator();
		while(iterator.hasNext())
		{
			System.out.println("\nRecommended List: " + iterator.next());
		}
	}
	*/
	
	/*
	//Test filter() Method
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
