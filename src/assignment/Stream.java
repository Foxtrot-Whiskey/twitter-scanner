package assignment;

import twitter4j.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class Stream {
    /**
     * Main entry of this application.
     *
     * @param args arguments doesn't take effect with this example
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public static void main(String[] args) throws TwitterException {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance().addListener(new RawStreamListener() {

            String[] outputArray = {"Initial"};
            public void onMessage(String rawJSON) {
                System.out.println(rawJSON);
                outputArray = add(outputArray, rawJSON);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        }).sample();
    }
    
    public static String[] add(String[] stringArray, String newValue)
    {
        String[] tempArray = new String[ stringArray.length + 1 ];
        for (int i=0; i<stringArray.length; i++)
        {
            tempArray[i] = stringArray[i];
        }
        tempArray[stringArray.length] = newValue;
        return tempArray;
    }
}