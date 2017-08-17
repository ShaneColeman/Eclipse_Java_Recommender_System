package recommender.system.lib.rec.java.project;

import net.librec.conf.Configuration;
import net.librec.data.model.TextDataModel;
import net.librec.recommender.RecommenderContext;

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
	}
}
