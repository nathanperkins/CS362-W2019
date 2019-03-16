import junit.framework.TestCase;

public class UrlValidatorManualTest extends TestCase {

	public UrlValidatorManualTest(String testName) {
		super(testName);
	}

	boolean softAssertEquals(String message, boolean expected, boolean actual) {
		if(expected == actual) {
			System.out.println("PASS: " + message);
			return true;
		}
		System.out.println("FAIL: " + message);
		return false;
	}

	public void testIsValid() {

		//long options = UrlValidator.ALLOW_ALL_SCHEMES;
		
        long options =
                UrlValidator.ALLOW_2_SLASHES
                    + UrlValidator.ALLOW_ALL_SCHEMES;
		
		UrlValidator urlVal = new UrlValidator(null, null, options);
		
		ResultPair[] testUrls = {
				new ResultPair("http://www.yahoo.com", true),
				new ResultPair("https://www.teamliquid.net/forum/sc2-tournaments/", true),
				new ResultPair("https://twitter.com/allinkid/status/1077417707526590464", true),
				new ResultPair("ftp://example.com", true),
				new ResultPair("http://www.example.com:8080/foo/bar", true),
				new ResultPair("http://www.example.com?foo=BaR", true),
				new ResultPair("http://www.example.com/foo%20bar", true),
				new ResultPair("htp://www.askjeeves.com", true),
                new ResultPair("https://www.askjeeves/", false),
				new ResultPair("http://www.askjeeves/com", false),
				new ResultPair("http://askjeeves/com", false),
				new ResultPair("http://www/askjeeves/com", false),
				new ResultPair("http://www.askjeeves.com::", false),
				new ResultPair("http://www.askjeeves.com:://", false),
				new ResultPair("http://sports.askjeeves.com::/nba", false)
		};
		
		// 1. ftp, https, htp fails
		// 2. authority never validated, just checks for does not contain ":"
		// 3. authority should allow port, checks for ":" and returns false
		// 4. ResultPair.valid is equal to !valid
		
		for (ResultPair item : testUrls) {
			boolean expected = item.valid;
			boolean actual = urlVal.isValid(item.item);
			String message = "isValid() returned " + actual + " for " + item.item + " when we expected " + item.valid + ".";

			assertEquals(message, expected, actual);
		}
		
		System.out.println("UrlValidatorManualTest.testIsValid() passed.");
	}

}
