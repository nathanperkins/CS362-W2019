

import junit.framework.TestCase;

//You can use this as a skeleton for your 3 different test approach
//It is an optional to use this file, you can generate your own test file(s) to test the target function!
// Again, it is up to you to use this file or not!


public class UrlValidatorTestUnit extends TestCase {
    static final String LOWER_CASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    static final String UPPER_CASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String NUMERIC_CHARS = "0123456789";

    public UrlValidatorTestUnit(String testName) {
        super(testName);
    }

	boolean softAssertEquals(String message, boolean expected, boolean actual) {
		if(expected == actual) {
			//System.out.println("PASS: " + message);
			return true;
		}
		//System.out.println("FAIL: " + message);
		return false;
	}
	
    public void testUnitOne() {
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
    
    public void testUnitTwo() {
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
}
