package assignment;

import twitter4j.*; //library for managing twitter api

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

	
	static Calendar cal = Calendar.getInstance();
	static int counter = 0; //Counter to count number of tweets per hour
	static int lastDateId = 10311; //Store for last date id to use to calculate percentage change in tweets
	static int arrayHour=11; //Hour check to trigger store of number of tweets, initialise with current hour
	static Map<Integer, Integer> result = new HashMap<>(Map.of(10308,1,10309,3,10310,7,10311,1)); //Hashmap to store dateid, count of hours results, initialised with test values
	static long[] followArray= {}; //followArray, function of the Twitter API to choose userid's to search, empty for this example
	static String[] companyName = {"Tesla"}; //Company name that we are targeting
    public static void main(String[] args) throws TwitterException {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance().addListener(new StatusListener() {
        	@Override
            public void onStatus(Status status) {
            	SimpleDateFormat dateFormatter = new SimpleDateFormat("MMdH"); //Specifying format for date id monthdayhour
            	Date Date = status.getCreatedAt(); //Taking the date the tweet was created
            	Integer dateId = Integer.parseInt(dateFormatter.format(Date)); //
            	cal.setTime(Date);
            	int currentHour = cal.get(Calendar.HOUR_OF_DAY);
            	counter++;
                if (currentHour != arrayHour) {
                	arrayHour = currentHour;
                	result.put(dateId,counter);
                	if (result.size()==0) {
                		int change = 0;
                		storeValue(Instant.now(), change);
                    	System.out.println(Arrays.asList(result));
                    	lastDateId = dateId;
                	} else {
	            		int change = (counter-result.get(lastDateId)/result.get(dateId))*100; //calculate percentage change in tweets since last hour
	            		storeValue(Instant.now(), change); //store value in TSValue
	                	lastDateId = dateId;

                	}
                	counter=-1; // Reset counter for next hour
                };
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
    	System.out.println("For timestamp = "+a.timestamp.toString());
    	System.out.println("Percentage change = "+a.getVal());
    	
	}

}
