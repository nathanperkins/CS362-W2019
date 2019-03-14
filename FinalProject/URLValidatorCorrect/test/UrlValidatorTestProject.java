

import junit.framework.TestCase;

import java.util.Random;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

//You can use this as a skeleton for your 3 different test approach
//It is an optional to use this file, you can generate your own test file(s) to test the target function!
// Again, it is up to you to use this file or not!


public class UrlValidatorTestProject extends TestCase {
    static final String LOWER_CASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    static final String UPPER_CASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String NUMERIC_CHARS = "0123456789";

    public UrlValidatorTestProject(String testName) {
        super(testName);
    }



    public void testManualTest()
    {
//You can use this function to implement your manual testing	   

    }
    
	boolean softAssertEquals(String message, boolean expected, boolean actual) {
		if(expected == actual) {
			//System.out.println("PASS: " + message);
			return true;
		}
		//System.out.println("FAIL: " + message);
		return false;
	}
	
    @Test
    public void unitTestOne() {
		ResultPair[] testUrls = {
				new ResultPair("http://www.yahoo.com", true),
				new ResultPair("https://www.teamliquid.net/forum/sc2-tournaments/", true),
				new ResultPair("https://twitter.com/allinkid/status/1077417707526590464", true),
				new ResultPair("ftp://example.com", true),
				new ResultPair("http://www.example.com:8080/foo/bar", true),
				new ResultPair("http://www.example.com?foo=BaR", true),
				new ResultPair("http://www.example.com/foo bar", true),
				new ResultPair("http://www.yahoo/com", false),
				new ResultPair("http://yahoo/com", false),
				new ResultPair("http://www/yahoo/com", false),
				new ResultPair("htp://www.yahoo.com", false),
				new ResultPair("http://www.yahoo.com::", false),
				new ResultPair("http://www.yahoo.com:://", false),
				new ResultPair("http://sports.yahoo.com::/nba", false)
		};
    	
        long options =
                UrlValidator.ALLOW_2_SLASHES
                + UrlValidator.ALLOW_ALL_SCHEMES;
		UrlValidator urlVal = new UrlValidator(null, null, options);
		boolean result = true;
		
		for (ResultPair item : testUrls) {
			String message = item.item + " should be " + item.valid;
			try {
				if(!softAssertEquals(message, item.valid, urlVal.isValid(item.item))) {
					result = false;
				}
			}
			catch (Throwable e) {
				System.out.println("ERROR: " + message);
				result = false;
			}
		}
    	
    	System.out.println("Unit tests one passed: " + result);
    	assertTrue(result);
    }
    
    @Test
    public void unitTestTwo() {
    	ResultPair[] schemes = {
    			new ResultPair("http://", true),
    			new ResultPair("https://", true),
    			new ResultPair("ftp://", true),
    			new ResultPair("spotify://", false),
    			new ResultPair("http:/", false),
    			new ResultPair("://", false)
    	};
    	
    	ResultPair[] domains = {
    			new ResultPair("0.0.0.0", true),
    			new ResultPair("255.255.255.255", true),
    			new ResultPair("256.256.256.256", false),
    			new ResultPair("255.255.255.255.255", false),
    			new ResultPair("255.255", false),
    			new ResultPair("1.2.3..", false),
    			new ResultPair("sports.yahoo.com", true),
    			new ResultPair("sports.yahoo.com.", true),
    			new ResultPair("yahoo.com", true),
    			new ResultPair("com", false),
    			new ResultPair("www.verywrong", false),
    			new ResultPair(".", false),
    			new ResultPair("", false),
    	};
    	
    	ResultPair[] ports = {
    			new ResultPair("", true),
    			new ResultPair(":80", true),
    			new ResultPair(":0", true),
    			new ResultPair(":65535", true),
    			new ResultPair(":65536", false),
    			new ResultPair(":-100", false),
    			new ResultPair(":abc", false)
    	};
    	
    	ResultPair[] paths = {
    			new ResultPair("", true),
    			new ResultPair("/", true),
    			new ResultPair("/foo", true),
    			new ResultPair("/foo/bar", true),
    			new ResultPair("/..", false),
    	};
    	
    	ResultPair[] queryStrings = {
    			new ResultPair("", true),
    			new ResultPair("?name=Bob", true),
    			new ResultPair("?name=Bob&foo=bar", true),
    			new ResultPair("?name=Bob&&&", true),
    	};
    	
        long options =
                UrlValidator.ALLOW_2_SLASHES
                + UrlValidator.ALLOW_ALL_SCHEMES;
		
		UrlValidator urlVal = new UrlValidator(null, null, options);
    	
    	boolean urlValid;
    	boolean finalResult = true;
    	
    	for(ResultPair scheme: schemes) {
    		urlValid = true;
    		for(ResultPair domain: domains) {
        		for(ResultPair port: ports) {
            		for(ResultPair path: paths) {
                		for(ResultPair queryString: queryStrings) {
                			String url = scheme.item + domain.item + port.item + path.item + queryString.item;
                			urlValid = scheme.valid && domain.valid && port.valid && path.valid && queryString.valid;
                			String message = url + " should be " + urlValid;
                			try {
                				if(!softAssertEquals(message, urlValid, urlVal.isValid(url))) {
                					finalResult = false;
                				}
                			}
                			catch (Throwable e) {
                				System.out.println("ERROR: " + message);
                				finalResult = false;
                			}
                		}
            		}
        		}
    		}
    	}
    	
    	System.out.println("Unit tests two passed: " + finalResult);
    	assertTrue(finalResult);
    }
    
    public void testYourFirstPartition()
    {
        //You can use this function to implement your First Partition testing

    }

    public void testYourSecondPartition(){
        //You can use this function to implement your Second Partition testing

    }
    //You need to create more test cases for your Partitions if you need to

    public void testIsValid()
    {
        //You can use this function for programming based testing

    }

    private static String generateString(int length, String validChars, String invalidChars)
    {
        StringBuilder s = new StringBuilder();
        double validRatio     = Math.pow(0.9, 1.0/length);

        for (int i = 0; i < length; i++)
        {
            float validOrNot = new Random().nextFloat();
            int choice;

            if (validOrNot > validRatio) {
                choice = new Random().nextInt(invalidChars.length());
                s.append(invalidChars.charAt(choice));
            } else {
                choice = new Random().nextInt(validChars.length());
                s.append(validChars.charAt(choice));
            }
        }

        return s.toString();
    }
    
    private static String randomString (String characterPool) {
    	StringBuilder result = new StringBuilder();
    	int length = 
    	for(int i=0; i<characterPool.length(); i++) {
    		
    	}
    }


    private static ResultPair generateScheme(int length)
    /*
    https://tools.ietf.org/html/rfc3986#section-3.1
    scheme = ALPHA *( ALPHA / DIGIT / "+" / "-" / "." )
    */

    {
        String validChars     = LOWER_CASE_CHARS + UPPER_CASE_CHARS + NUMERIC_CHARS + "+-.";
        String invalidChars   = "~!@#$%^&*()_";

        String item = generateString(length, validChars, invalidChars);
        boolean valid = !item.equals("") || item.matches("^[A-z]+[A-z0-9+\\-.]*$");

        return new ResultPair(item, valid);
    }
    
    // Valid format ?foo=bar&second=was+it+clear+%28already%29%3F
    private static ResultPair generateQuery(int length) {
    	String item;
    	boolean valid;
    	return new ResultPair(item, valid);
    }
    
    private static ResultPair generatePath(int length) {
    	
    	String validChars = LOWER_CASE_CHARS + UPPER_CASE_CHARS + NUMERIC_CHARS;
    	String invalidChars = ;
    	String item = generateString(length, validChars, invalidChars);
    	boolean valid;
    	
    	return new ResultPair(item, valid);
    }

    public void testRandom()
    {
        ArrayList<ResultPair> schemes = new ArrayList<ResultPair>();
        for (int i = 0; i < 20; i++)
        {
            int length = new Random().nextInt(10);
            schemes.add(generateScheme(length));
        }
        //System.out.println(schemes.size());
    }
}
