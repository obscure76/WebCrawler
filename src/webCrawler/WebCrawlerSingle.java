package webCrawler;

import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;


class URLDepthPair {
	public String url;
	public int depth;
	URLDepthPair(){};
	
	URLDepthPair(String u, int d)
	{
		this.url = u;
		this.depth = d;
	}
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }
	    if (getClass() != obj.getClass()) {
	        return false;
	    }
	    final URLDepthPair other = (URLDepthPair) obj;
	    if ((this.url == null) ? (other.url != null) : !this.url.equals(other.url)) {
	        return false;
	    }
	    return true;
	}
}

public class WebCrawlerSingle {

	public static final String URL_PREFIX = "http://";
	public static final int HTTP_PORT = 80;
	
	public static String getHost(String url)
	{
		String host;
		host = url.substring(0);
		return host;
	}
	
	public static void parseUrls(LinkedList<URLDepthPair> notVisited, String inputString, int depth)
	{
		String[] splitText = inputString.split(URL_PREFIX);
		if(splitText.length>1)
		{
			//System.out.println(URL_PREFIX + splitText[1].split("\"")[0]);
			notVisited.add(new URLDepthPair(URL_PREFIX + splitText[1].split("\"")[0], depth));
		}
	}
	
	public static LinkedList<URLDepthPair> getSites(String url, int depth) throws IOException
	{
		System.out.println("WebPages crawl started with root URL "+ url);
		LinkedList<URLDepthPair> visited = new LinkedList<URLDepthPair>();
		LinkedList<URLDepthPair> notVisited = new LinkedList<URLDepthPair>();
		URLDepthPair cUrlD;
		int currDepth = 0;
		notVisited.add(new URLDepthPair(url, currDepth));
		while(!notVisited.isEmpty())
		{
			cUrlD = notVisited.removeFirst();
			if(cUrlD.depth >= depth)
				continue;
			if(visited.contains(cUrlD))
				continue;
			visited.add(cUrlD);
			if(cUrlD.url.endsWith("iso") || cUrlD.url.endsWith("xz") )
			{
				continue;
			}
			BufferedReader in = null;
			/*
			 * Using URLConnection
			 */
			try {
				URL yahoo = new URL(cUrlD.url);
		        URLConnection yc = yahoo.openConnection();
		        System.out.println("Getting "+ cUrlD.url);
		        in = new BufferedReader(
		                                new InputStreamReader(
		                                yc.getInputStream()));
		        String inputLine;
	
		        while ((inputLine = in.readLine()) != null) {
		        	parseUrls(notVisited, inputLine, cUrlD.depth+1);
		        }
		        in.close();
			} catch (Exception e) {
				System.out.println(cUrlD.url+" Not reachable");
				continue;
			}
		}
		return visited;	
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length <2)
		{
			System.out.println("Usage: WebCrawlerSingle <URL> <depth>");
		}
		long startTime = System.currentTimeMillis();
		LinkedList<URLDepthPair> visitedUrls = new LinkedList<URLDepthPair>();
		String url=args[0];
		int depth = Integer.parseInt(args[1]);
		try {
			visitedUrls = getSites(url, depth);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(URLDepthPair ud : visitedUrls)
		{
			System.out.printf("%s   %d\n", ud.url, ud.depth);
		}
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.printf("Total URL crawled %d  in  ", visitedUrls.size());
		System.out.print(totalTime/1000);
		System.out.println(" Seconds");
	}

}
