package assignment;

import twitter4j.*; //library for managing twitter api

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class TwitterScanner {
	
	//Implementing TSValue class
	public static class TSValue {
		private Instant timestamp ;
		private static double val ;
		public TSValue(Instant timestamp, double val) {
			this . timestamp = timestamp;
			this . val = val;
		}
		public Instant getTimestamp() {
			return timestamp ;
		}
		public double getVal() {
			return val ;
		}
	}

	static int counter = 0; //Counter to count number of tweets per hour
	static int arrayHour=0; //Hour check to trigger store of number of tweets
	static Map<Integer, Integer> result = new HashMap<Integer, Integer>(); //Hashmap to store dateid, count of hours results
	static long[] followArray= {}; //followArray, function of the Twitter API to choose userid's to search
	static String[] companyName = {"Facebook"}; //Company name that we are targeting
    public static void main(String[] args) throws TwitterException {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance().addListener(new StatusListener() {
            @Override
            public void onStatus(Status status) {
            	SimpleDateFormat dateFormatter = new SimpleDateFormat("mdh"); //Specifying format for date id 
            	Date Date = status.getCreatedAt(); //Taking the date the tweet was created
            	Calendar cal = Calendar.getInstance();
            	Integer dateid = Integer.parseInt(dateFormatter.format(Date));
            	result.put(dateid,0); //Initialising result with dateid and 0
            	cal.setTime(Date);
            	int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                System.out.println("Current Hour =" + currentHour);
                System.out.println("Current counter ="+counter);
                if (currentHour != arrayHour) {
                	arrayHour = currentHour;
                	result.put(dateid+1,counter);
                	if (result.size()==0) {
                		int change = 0;
                		storeValue(Instant.now(), change);
                	} else {
                		int change = (counter-result.get(dateid-1)/result.get(dateid))*100;
                		storeValue(Instant.now(), change);
                	}
                	counter=-1;
                	System.out.println(Arrays.asList(result));
                };
                counter++;
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
               // System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                //System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                //System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                //System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                //ex.printStackTrace();
            }
        });

        // filter() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
        twitterStream.filter(new FilterQuery(0, followArray, companyName));
    }
    
    protected static void storeValue(Instant ts, int count) {
    	TSValue a = new TSValue(ts, count);
    	System.out.println(a.timestamp.toString());
    	System.out.println(a.getVal());
    	
	}

}
