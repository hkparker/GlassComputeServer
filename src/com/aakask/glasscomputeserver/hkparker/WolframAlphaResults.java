package com.aakask.glasscomputeserver.hkparker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAImage;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;


/**
 * Class that pulls results for a query from WA and makes a ResultObject
 * Copyright 2013 Aakash Patel
 * @author AakashPatel
 *
 */
public class WolframAlphaResults {
	private static String appid = "YOUR APP ID";
	private WAEngine engine;
	private WAQuery query;
	private WAQueryResult queryResult;
	private int podCount=0;
	private int resultCount=0;
	private boolean threwException = false;
	
	public WolframAlphaResults(String queryString){
		engine = getEngine();
		query = engine.createQuery();
		query.setInput(queryString);
		try{
			queryResult = engine.performQuery(query);
		} catch (WAException e) {
			threwException = true;
			e.printStackTrace();
		}
	}

	private WAEngine getEngine(){
		// Get WAEngine for search ready
		WAEngine tempEngine = new WAEngine();
		// Set WA search params
		tempEngine.setAppID(appid);
		tempEngine.addFormat("image");
		tempEngine.setWidth(320);
		tempEngine.setMaxWidth(320);
		tempEngine.setPlotWidth(320);
		tempEngine.setMagnification(2.0);
		return tempEngine;
	}

	/**
	 * Holds status of query
	 * @return error coe if there was one, elase 0
	 */
	public int queryStatus(){
		if (queryResult.isError() || threwException==true) {
			return ResultObject.QUERY_UNKNOWN_ERROR;
		}
		else if(!queryResult.isSuccess()){
			return ResultObject.QUERY_NO_RESULTS;
		}
		return  0;
	}

	/**
	 * Get all the pod (pages) data.
	 * Why did I do this? Because it works. Feel free to change.
	 * ArrayList<> contains the index (0-n) of pods (pages). This is to keep the order as it was received.
	 * HashMap<K,V> contains the pod (page) title as key and result image URLs in a LinkedList as a value. This will contain only 1 k,v pair
	 * LinkedList<String> explained above. Contains image URLs associated with the current page (pod).
	 * @return ArrayList<HashMap<String, LinkedList<String>>> of result data
	 */
	private ArrayList<HashMap<String, LinkedList<String>>> getResultsData(){

		ArrayList<HashMap<String, LinkedList<String>>> map = new ArrayList<HashMap<String,LinkedList<String>>>();
		int currentIndex = -1;
		for (WAPod pod : queryResult.getPods()) {
			if (!pod.isError()) {
				for (WASubpod subpod : pod.getSubpods()) {
					for (Object element : subpod.getContents()) {
						if (element instanceof WAImage) {
							resultCount++;
							if(currentIndex != -1 && map.get(currentIndex).containsKey(pod.getTitle())){
								map.get(currentIndex).get(pod.getTitle()).add(((WAImage) element).getURL());
							}
							else{
								currentIndex++;
								HashMap<String,LinkedList<String>> tempMap = new HashMap<String,LinkedList<String>>();
								LinkedList<String> tempList = new LinkedList<String>();
								tempList.add(((WAImage) element).getURL());
								tempMap.put(pod.getTitle(), tempList);
								map.add(currentIndex, tempMap);
							}
						}
					}
				}
			}
		}
		podCount = currentIndex+1;
		return map;
	}
	
	/**
	 * Return the ResultObject associated with a query
	 * @return ResultObject
	 */
	public ResultObject getResults(){
		ResultObject results = new ResultObject(getResultsData(), queryStatus());
		return results;
	}
}
