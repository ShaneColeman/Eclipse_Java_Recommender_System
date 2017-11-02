package recommender.system.lib.rec.java.project;

import java.io.FileInputStream;
import java.util.Properties;

import net.librec.conf.Configuration;
import net.librec.job.RecommenderJob;
import net.librec.math.algorithm.Randoms;

public class RecSysConfigDriver 
{
	/*
	 * AbstractRecommender
	 * Baseline
	 * UserClusterRecommender
	 * ItemClusterRecommender
	 */
	//public static String CONFIGURATION_FILE = "conf/UserCluster-Baseline.properties";
	//public static String CONFIGURATION_FILE = "conf/ItemCluster-Baseline.properties";
	
	/*
	 * AbstractRecommender
	 * Collaborative Filtering 
	 * UserKNNRecommender
	 * ItemKNNRecommender 
	 */
	//public static String CONFIGURATION_FILE = "conf/UserKNN-CF.properties";
	//public static String CONFIGURATION_FILE = "conf/ItemKNN-CF.properties";
	
	/*
	 * AbstractRecommender
	 * Collaborative Filtering - Ranking
	 * SLIMRecommender
	 */
	//public static String CONFIGURATION_FILE = "conf/SLIM-CF-Ranking.properties";
	
	/*
	 * AbstractRecommender
	 * EXT
	 * AssociationRuleRecommender
	 */
	//public static String CONFIGURATION_FILE = "conf/AssociationRule-EXT.properties";
	
	/*
	 * AbstractRecommender
	 * Hybrid
	 * HybridRecommender
	 */
	//public static String CONFIGURATION_FILE = "conf/Hybrid.properties";
	
	/*
	 * MatrixFactorizationRecommender 
	 * Collaborative Filtering - Ranking
	 * AoBPRRecommender
	 * RankALSRecommender
	 */
	public static String CONFIGURATION_FILE = "conf/AoBPR-CF-Ranking.properties";
	//public static String CONFIGURATION_FILE = "conf/RankALS-CF-Ranking.properties";
	
	/*
	 * BiasedMFRecommender -> MatrixFactorizationRecommender
	 * Collaborative Filtering - Rating
	 * SVDPlusPlusRecommender
	 */
	//public static String CONFIGURATION_FILE = "conf/SVDPlusPlus-CF-Rating.properties";
	
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
		
		if(configurationFilePath.equals("conf/UserCluster-Baseline.properties"))
		{
			System.out.println("User Cluster Recommender\n");
		}
		else if(configurationFilePath.equals("conf/ItemCluster-Baseline.properties"))
		{
			System.out.println("Item Cluster Recommender\n");
		}
		else if(configurationFilePath.equals("conf/UserKNN-CF.properties"))
		{
			System.out.println("User KNN Recommender\n");
		}
		else if(configurationFilePath.equals("conf/ItemKNN-CF.properties"))
		{
			System.out.println("Item KNN Recommender\n");
		}
		else if(configurationFilePath.equals("conf/SLIM-CF-Ranking.properties"))
		{
			System.out.println("SLIM Recommender\n");
		}
		else if(configurationFilePath.equals("conf/AssociationRule-EXT.properties"))
		{
			System.out.println("Association Rule Recommender\n");
		}
		else if(configurationFilePath.equals("conf/Hybrid.properties"))
		{
			System.out.println("Hybrid Recommender\n");
		}
		else if(configurationFilePath.equals("conf/AoBPR-CF-Ranking.properties"))
		{
			System.out.println("AoBPR Recommender\n");
		}
		else if(configurationFilePath.equals("conf/RankALS-CF-Ranking.properties"))
		{
			System.out.println("Rank ALS Recommender\n");
		}
		else if(configurationFilePath.equals("conf/SVDPlusPlus-CF-Rating.properties"))
		{
			System.out.println("SVD Plus Plus Recommender\n");
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