package com.Brett.mongoDBTutorial;

import java.io.IOException;
import java.io.StringWriter;
import java.net.UnknownHostException;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class MongoSparkFreemarker {
	public static void main(String[] args) throws UnknownHostException {
		final Configuration conf = new Configuration();
		conf.setClassForTemplateLoading(SparkAndFreemarker.class, "/");
		
		MongoClient client =  new MongoClient(new ServerAddress("localhost",27017));

		DB database = client.getDB("test");
		final DBCollection collection = database.getCollection("mycollection");
		
		Spark.get(new Route("/") {

			@Override
			public Object handle(final Request request, final Response response) {
				StringWriter writer = new StringWriter();
				try {
					Template helloTemplate = conf.getTemplate("hello.ftl");
					
					DBObject document = collection.findOne();
					helloTemplate.process(document, writer);

				} catch (IOException e) {
					e.printStackTrace();
				} catch (TemplateException e) {
					e.printStackTrace();
				}
				return writer;
			}
		});

	}
}
