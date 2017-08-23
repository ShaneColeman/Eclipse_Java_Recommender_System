package recommender.system.lib.rec.java.project;

import java.util.ArrayList;
import java.util.List;

import net.librec.conf.Configuration;
import net.librec.data.model.TextDataModel;
import net.librec.eval.RecommenderEvaluator;
import net.librec.eval.rating.RMSEEvaluator;
import net.librec.filter.GenericRecommendedFilter;
import net.librec.recommender.Recommender;
import net.librec.recommender.RecommenderContext;
import net.librec.recommender.baseline.ItemClusterRecommender;
import net.librec.recommender.cf.ItemKNNRecommender;
import net.librec.recommender.cf.UserKNNRecommender;
import net.librec.recommender.cf.ranking.EALSRecommender;
import net.librec.recommender.item.RecommendedItem;
import net.librec.similarity.BinaryCosineSimilarity;
import net.librec.similarity.CosineSimilarity;
import net.librec.similarity.JaccardSimilarity;
import net.librec.similarity.PCCSimilarity;
import net.librec.similarity.RecommenderSimilarity;

public class RecommenderSystemDriverTest 
{
	public static void main(String[] args) throws Exception
	{
		//Building the Data Model (Text File Format)
		Configuration configuration = new Configuration();
		configuration.set("dfs.data.dir","data");
		TextDataModel textDataModel = new TextDataModel(configuration);
		textDataModel.buildDataModel();
		
		 /*
		 * To fix log4j:WARN error
		 * https://stackoverflow.com/questions/12532339/no-appenders-could-be-found-for-loggerlog4j
		 */
		
		//Building the Recommender System Context (Framework)
		RecommenderContext recommenderContext = new RecommenderContext(configuration, textDataModel);
		
		
		//Building the Recommender System Similarity 
		//configuration.set("rec.recommender.similarity.key", "item");
		RecommenderSimilarity recommenderSimilarity = new PCCSimilarity();
		recommenderSimilarity.buildSimilarityMatrix(textDataModel);
		recommenderContext.setSimilarity(recommenderSimilarity);
		
		//Building the Recommender System Model
		//configuration.set("rec.neighbours.knn.number", "5");
		Recommender recommender = new ItemClusterRecommender();
		recommender.setContext(recommenderContext);
		
		//Executing (running) the Recommender System Model (Algorithm)
		recommender.recommend(recommenderContext);
		
		//Evaluating the Recommender System Model (Algorithm) Results
		RecommenderEvaluator recommenderEvaluator = new RMSEEvaluator();
		System.out.println("Root Mean Square Error: " + recommender.evaluate(recommenderEvaluator));
		
		
		//Set ID List (User and Item) of Filter
		List<String> userIDList = new ArrayList<>();
		List<String> itemIDList = new ArrayList<>();
		userIDList.add("CVE-2017-0001");
		itemIDList.add("CWE-20");
		
		
		//Filtering the Recommender System Recommended Results
		List<RecommendedItem> recommendedItemList = recommender.getRecommendedList();
		GenericRecommendedFilter genericRecommendedFilter = new GenericRecommendedFilter();
		genericRecommendedFilter.setUserIdList(userIDList);
		genericRecommendedFilter.setItemIdList(itemIDList);
		recommendedItemList = genericRecommendedFilter.filter(recommendedItemList);
		
		//Printing the Recommender System Recommended Results (Filtered)
		for(RecommendedItem recommendedItem : recommendedItemList)
		{
			System.out.println("User: " + recommendedItem.getUserId() + " " +
								"Item: " + recommendedItem.getItemId() + " " +
								"Value: " + recommendedItem.getValue());
		}
		
		
	}
}
