package webCrawler;



public class WebCrawlerMultithread {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		String url;
		int maxDepth;
		int maxThreads;
		if(args.length >=3)
		{
			try {
				url = args[0];
				maxDepth = Integer.parseInt(args[1]);
				maxThreads = Integer.parseInt(args[2]);
			} catch (Exception e){
				System.out.println("Usage: WebCrawlerMultiThread <URL> <depth> <max-threadds>");
				return;
			}
		} else {
			System.out.println("Usage-hmmm: WebCrawlerMultiThread <URL> <depth> <max-threads>");
			return;
		}
		Thread threads[] = new Thread[maxThreads];
		URLPool urlPool = new URLPool(maxDepth);
		urlPool.putUrlPair(new URLDepthPair(url, 0));
		
		for(int i=0; i<maxThreads; ++i)
		{
			threads[i] = new Thread(new CrawlerTask(urlPool));
			threads[i].start();
		}
		while(true)
		{
			if(urlPool.getNumOfWaitingThreads() == maxThreads)
			{
				long totalTime = System.currentTimeMillis() - startTime;
				System.out.printf("Total URL crawled %d  in  ", urlPool.printVisitedUrls());
				System.out.print(totalTime/1000);
				System.out.println(" Seconds");
				System.exit(0);
			}
			Thread.sleep(1000);
		}
	}

}
