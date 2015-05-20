package se.chalmers.snake.highscoreDatabase;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class ScoreHandler {
	
	/**
	 * This is the key used when doing the SHA-1 hash signature that is used later on the
	 * server to verify that the post call was really sent from this application and not from another source. Key must remain secret
	 */
	private static final String KEY = "1DA65G65G43421MO8WE6322Y3ERFE26T24TY64";
	
	/**
	 * This defines the hash algorithm used to generate the signature
	 */
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	
	/**
	 * Method used to post a call to the webserver and request a database score list. The scores are sent back
	 * in the post response and the method then parses it and creates a list of user objects 
	 * @return An ArrayList of DataBaseUser objects of the top 10 global scores
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 * @throws NoSuchAlgorithmException
	 * @throws ProtocolException
	 */
	public static ArrayList<DataBaseUser> GetGlobalDataBase() throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, ProtocolException{
		
		ArrayList<DataBaseUser> list = new ArrayList<DataBaseUser>(); 
		
		try{
			URL url = new URL("http://www.martinsonesson.se/pi/snakescore/model/DataBaseFetcher.php");
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			
		    // Get the response
		    BufferedReader rd = new BufferedReader(
		            new InputStreamReader(in));

		    String responseData = rd.readLine();
		    rd.close();
		    
		    String[] users = responseData.split("\\?");
		    
		    for (int i = 0; i < users.length; i++){
		    	if (users[i] != null || users[i] != ""){
		    		String[] userData = users[i].split("&");
		    		DataBaseUser user = new DataBaseUser();
		    		
		    		for (int j = 0; j < userData.length; j++)
		    		{
		    			String [] userDataParts = userData[j].split("=");
		    			String key = userDataParts[0];
		    			String value = userDataParts[1];
		    			
		    			if (key.equals("name"))
		    			{
		    				user.setName(value);
		    			} else if (key.equals("score"))
		    			{
		    				user.setScore(Integer.parseInt(value));
		    			} else if (key.equals("country"))
		    			{
		    				user.setCountry(value);
		    			} else if (key.equals("timestamp"))
		    			{
		    				user.setTimestamp(Integer.parseInt(value));
		    			}
		    		}
		    		
		    		list.add(user);
		    	}
		    }   
		    
		    return list;
		    
		}catch(Exception e){
			System.out.println("The system failed with the operation of making peace with the absurdity of human existance, thus experiencing ennui");
			return null;
		}
	}
	
	/**
	 * 
	 * @param score The highscore recorded
	 * @param name Name of the player
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 * @throws NoSuchAlgorithmException
	 * @throws ProtocolException
	 */
	public static void AttemptToSendScore(int score, String name, String country) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, ProtocolException{
		
		// Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://www.martinsonesson.se/pi/snakescore/model/ScoreDataBaseHandler.php");

	    long timeStamp = System.currentTimeMillis() / 1000L;
		String str = "name=" + name + "&score=" + score + "&timeStamp=" + timeStamp + "&country=" + country;
		String hmac = calculateRFC2104HMAC(str, KEY);
	    
	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
	        nameValuePairs.add(new BasicNameValuePair("name", name));
	        nameValuePairs.add(new BasicNameValuePair("score", ""+score));
	        nameValuePairs.add(new BasicNameValuePair("timestamp", ""+timeStamp));
	        nameValuePairs.add(new BasicNameValuePair("country", country));
	        nameValuePairs.add(new BasicNameValuePair("hash", hmac));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        
	    } catch (Exception e) {
	    	System.out.println("ERRRRRROORRR");
	    }
	}
	
	/**
	 * 
	 * @param bytes 
	 * @return
	 */
	private static String toHexString(byte[] bytes) {
		Formatter formatter = new Formatter();
		for (byte b : bytes) {
			formatter.format("%02x", b);
		}
		return formatter.toString();
	}
	
	/**
	 * 
	 * @param data The data to be hashed
	 * @param key Key used for hashing
	 * @return
	 * @throws SignatureException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static String calculateRFC2104HMAC(String data, String key)
		throws SignatureException, NoSuchAlgorithmException, InvalidKeyException
	{
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
		Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);
		return toHexString(mac.doFinal(data.getBytes()));
	}
}
