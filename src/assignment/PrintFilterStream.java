package assignment;

import twitter4j.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class PrintFilterStream {
    /**
     * Main entry of this application.
     *
     * @param args follow(comma separated user ids) track(comma separated filter terms)
     * @throws TwitterException when Twitter service or network is unavailable
     */
	static int counter = 0;
	static int arrayHour=0;
	static Map<String, String> result = new HashMap<String, String>();
	static long[] followArray= {};
	static String[] companyName = {"Facebook"};
    public static void main(String[] args) throws TwitterException {
        if (args.length < 1) {
            System.out.println("Usage: java twitter4j.examples.PrintFilterStream [follow(comma separated numerical user ids)] [track(comma separated filter terms)]");
            System.exit(-1);
        }

        TwitterStream twitterStream = new TwitterStreamFactory().getInstance().addListener(new StatusListener() {
            @Override
            public void onStatus(Status status) {
                //System.out.println(status.getText());
            	SimpleDateFormat dateFormatter = new SimpleDateFormat("y-M-d 'at' h 'OClock '");
            	Date Date = status.getCreatedAt();
            	Calendar cal = Calendar.getInstance();
            	cal.setTime(Date);
            	int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                System.out.println("Current Hour =" + currentHour);
                System.out.println("Current counter ="+counter);
                if (currentHour != arrayHour) {
                	arrayHour = currentHour;
                	result.put("Count of "+companyName[0]+" mentions on "+ dateFormatter.format(Date),String.valueOf(counter));
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

}
