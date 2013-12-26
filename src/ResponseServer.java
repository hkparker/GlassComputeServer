import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
 * Hands request from GlassCompute app
 * Copyright 2013 AakashPatel
 * @author AakashPatel
 *
 */
public class ResponseServer extends NanoHTTPD {
	/**
	 * Constructs an HTTP server on given port.
	 */
	public ResponseServer() {
		super(8080);
	}

	@Override
	public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) {
		StringBuilder sb = new StringBuilder();
		// Make sure we have the correct params in url
		if(parms.containsKey("parsed") && parms.containsKey("q")  && !parms.get("parsed").isEmpty() && !parms.get("q").isEmpty()){
			// If query was not parsed, parse it then get the results
			if(parms.get("parsed").equals("false")){
				sb.append(getJSONResult(parseQuery(parms.get("q"))));
			}
			// If query was not parsed, send directly to WA server to get the results
			else if(parms.get("parsed").equals("true")){
				{
					sb.append(getJSONResult(parms.get("q")));
				}
			}
			// Send empty results object if bad query
			else{
				Gson gson = new Gson();
				sb.append(gson.toJson(new ResultObject(new ArrayList<HashMap<String,LinkedList<String>>>(), ResultObject.URL_ARGS_ERROR)));
			}
		}
		// Send empty results object if bad query
		else{
			Gson gson = new Gson();
			sb.append(gson.toJson(new ResultObject(new ArrayList<HashMap<String,LinkedList<String>>>(), ResultObject.URL_ARGS_ERROR)));
		}
		// Return the results object
		return new Response(sb.toString());
	}
	
	/**
	 * Get the JSON serialization of the ResultObject associated with an input query
	 * @param input parsed input query string
	 * @return	JSON serialization of ResultObject
	 */
	public String getJSONResult(String input){
		
		WolframAlphaResults waResult = new WolframAlphaResults(input);
		ResultObject results = waResult.getResults();
		Gson gson = new Gson();
		return gson.toJson(results);
		
	}
	
	/**
	 * Parse the query input text
	 * @param query input
	 * @return parsed output
	 */
	public String parseQuery(String query){
		return query;
	}

	/**
	 * Run the server
	 * @param args
	 */
	public static void main(String[] args) {
		ServerRunner.run(ResponseServer.class);
	}
	
	
}

