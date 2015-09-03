package webCrawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

public class CrawlerTask implements Runnable{
	private URLPool urlPool;
	public static final String URL_PREFIX = "http://";
	
	public CrawlerTask(URLPool up)
	{
		urlPool = up;
	}
	
	public String getHost(String url)
	{
		String host;
		host = url.substring(0);
		return host;
	}
	
	public void parseUrls(String inputString, int depth)
	{
		String[] splitText = inputString.split(URL_PREFIX);
		if(splitText.length>1)
		{
			//System.out.println(URL_PREFIX + splitText[1].split("\"")[0]);
			urlPool.putUrlPair(new URLDepthPair(URL_PREFIX + splitText[1].split("\"")[0], depth));
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		URLDepthPair cUrlD;
		int currDepth = 0;
		BufferedReader in = null;
		/*
		 * Using URLConnection
		 */
		while(true) 
		{
			cUrlD = urlPool.getUrlPair();
			try {
				URL yahoo = new URL(cUrlD.url);
			    URLConnection yc = yahoo.openConnection();
			    System.out.println("Getting "+ cUrlD.url);
			    in = new BufferedReader(
			                                new InputStreamReader(
			                                yc.getInputStream()));
			    String inputLine;
		
			    while ((inputLine = in.readLine()) != null) {
			        parseUrls(inputLine, cUrlD.depth+1);
			    }
			        in.close();
			} catch (Exception e) {
					System.out.println(cUrlD.url+" Not reachable");
			}
		}
	}

}
