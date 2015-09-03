package webCrawler;

import java.util.LinkedList;

public class URLPool {
	private LinkedList<URLDepthPair> visited, notVisited;
	private int maxDepth;
	private int numOfWaitingThreads;
	
	public URLPool(int d)
	{
		visited = new LinkedList<URLDepthPair>();
		notVisited = new LinkedList<URLDepthPair>();
		maxDepth = d;
		numOfWaitingThreads = 0;
	}
	
	public synchronized URLDepthPair getUrlPair() 
	{
        while (notVisited.isEmpty()) {
            try {
            	++numOfWaitingThreads;
                wait();
                --numOfWaitingThreads;
            } catch (InterruptedException e) {}
        }
        notifyAll();
        URLDepthPair urlD = notVisited.removeFirst();
        visited.add(urlD);
		return urlD;
	}
	
	public synchronized void putUrlPair(URLDepthPair urlD)
	{
		if(urlD.url.endsWith("iso") || urlD.url.endsWith("xz") )
		{
			visited.add(urlD);
			return;
		}
		if(visited.contains(urlD))
			return;
		if(urlD.depth < maxDepth)
			notVisited.add(urlD);
	}
	
	public synchronized int getNumOfWaitingThreads()
	{
		return numOfWaitingThreads;
	}
	
	public int printVisitedUrls()
	{
		for(URLDepthPair u: visited)
			System.out.println(u.url);
		return visited.size();
	}
	

}
