

import junit.framework.TestCase;

import java.util.Random;
import java.util.ArrayList;

//You can use this as a skeleton for your 3 different test approach
//It is an optional to use this file, you can generate your own test file(s) to test the target function!
// Again, it is up to you to use this file or not!


public class UrlValidatorTestRandom extends TestCase {
    static final String LOWER_CASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    static final String UPPER_CASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String ALPHABET_CHARS   = LOWER_CASE_CHARS + UPPER_CASE_CHARS;
    static final String NUMERIC_CHARS = "0123456789";

    public UrlValidatorTestRandom(String testName) {
        super(testName);
    }

    private static String generateString(int length, double validRatio, String validChars, String invalidChars)
    // length: desired length for the string
    // validRatio: approximate chance that this should be a valid string
    // validChars: chars to use for valid string
    // invalidChars: chars to use for invalid string
    {
        StringBuilder s = new StringBuilder();
        double validCharRatio     = Math.pow(validRatio, 1.0/length);

        for (int i = 0; i < length; i++)
        {
            float validOrNot = new Random().nextFloat();
            int choice;

            if (validOrNot > validCharRatio) {
                choice = new Random().nextInt(invalidChars.length());
                s.append(invalidChars.charAt(choice));
            } else {
                choice = new Random().nextInt(validChars.length());
                s.append(validChars.charAt(choice));
            }
        }

        return s.toString();
    }


    private static ResultPair generateScheme(int length, double validRatio)
    /*
    https://tools.ietf.org/html/rfc3986#section-3.1

    Scheme names consist of a sequence of characters beginning with a
    letter and followed by any combination of letters, digits, plus
    ("+"), period ("."), or hyphen ("-").  Although schemes are case-
    insensitive, the canonical form is lowercase and documents that
    specify schemes must do so with lowercase letters.  An implementation
    should accept uppercase letters as equivalent to lowercase in scheme
    names (e.g., allow "HTTP" as well as "http") for the sake of
    robustness but should only produce lowercase scheme names for
    consistency.

    scheme = ALPHA *( ALPHA / DIGIT / "+" / "-" / "." )
    */

    {
    	if(length == 0) {
    		return new ResultPair("://", false);
    	}
    	String validChars     = LOWER_CASE_CHARS + UPPER_CASE_CHARS + NUMERIC_CHARS;
        String invalidChars   = "~!@#$%^&*()_+-.";

        String item = generateString(length, validRatio, validChars, invalidChars) + "://";
        boolean valid = item.matches("^[A-Za-z]+[A-Za-z0-9+\\-.]*://$");

        return new ResultPair(item, valid);
    }

    private static ResultPair generateAuthority(int length, double validRatio)
    /*
    https://tools.ietf.org/html/rfc3986#section-3.2

    Many URI schemes include a hierarchical element for a naming
    authority so that governance of the name space defined by the
    remainder of the URI is delegated to that authority (which may, in
    turn, delegate it further).  The generic syntax provides a common
    means for distinguishing an authority based on a registered name or
    server address, along with optional port and user information.

    The authority component is preceded by a double slash ("//") and is
    terminated by the next slash ("/"), question mark ("?"), or number
    sign ("#") character, or by the end of the URI.

    authority   = [ userinfo "@" ] host [ ":" port ]
    */
    {
    	ResultPair[] tldList = {
    		new ResultPair("com", true),
    		new ResultPair("org", true),
    		new ResultPair("net", true),
    		new ResultPair("gov", true),
    		new ResultPair("dummy", false),
    		new ResultPair("invalid", false),
    		new ResultPair("asdf", false),
    	};
    	
    	// Authority allows abc@def.org
        String validChars = ALPHABET_CHARS + NUMERIC_CHARS;
        String invalidChars = "~!#$%^&*()_+";

        String host = generateString(length, validRatio, validChars, invalidChars);
        boolean hostValid = host.matches("^[A-Za-z0-9]+[A-Za-z0-9.]*(?<![.])$");

        int randomNum = new Random().nextInt(tldList.length);
        String domain = tldList[randomNum].item;
        boolean domainValid = tldList[randomNum].valid;

        String item = host + "." + domain;
        boolean valid = hostValid && domainValid;

        return new ResultPair(item, valid);
    }
    
    private static ResultPair generatePort(int length, double validRatio)
    /*
    A port is either "" or ":portNum"
    where portNum is between 0 and 65535.

    it is valid for the port to start with 0s.
     */
    {
    	if(length == 0) {
    		return new ResultPair("", true);
    	}

    	String validChars = NUMERIC_CHARS;
    	String invalidChars = ALPHABET_CHARS;
    	
    	String port = generateString(length, validRatio, validChars, invalidChars);
        int portNumber;

    	try {
            portNumber = Integer.parseInt(port);
        }
		catch (NumberFormatException e) { portNumber = -1; }

    	String item = ":" + port;
    	boolean valid = portNumber >= 0 && portNumber < 65536;

    	return new ResultPair(item , valid);

    }

    private static ResultPair generatePath(int length, double validRatio)
    /*
    https://tools.ietf.org/html/rfc3986#section-3.3


    The path component contains data, usually organized in hierarchical
    form, that, along with data in the non-hierarchical query component
    (Section 3.4), serves to identify a resource within the scope of the
    URI's scheme and naming authority (if any).  The path is terminated
    by the first question mark ("?") or number sign ("#") character, or
    by the end of the URI.

    path          = path-abempty    ; begins with "/" or is empty
                    / path-absolute   ; begins with "/" but not "//"
                    / path-noscheme   ; begins with a non-colon segment
                    / path-rootless   ; begins with a segment
                    / path-empty      ; zero characters

    path-abempty  = *( "/" segment )
    path-absolute = "/" [ segment-nz *( "/" segment ) ]
    path-noscheme = segment-nz-nc *( "/" segment )
    path-rootless = segment-nz *( "/" segment )
    path-empty    = 0<pchar>
    */
    {
    	if(length == 0) {
    		return new ResultPair("", true);
    	}

        String validChars = ALPHABET_CHARS + NUMERIC_CHARS + "/";
        String invalidChars = ".";

        String item = "/" + generateString(length, validRatio, validChars, invalidChars);
        
        // If path is not "/.." and doesn't contain '//' or '/../', it passes
        boolean valid = !item.equals("/..") &&
                        !item.matches("^/[.]{2}/(.*)$") &&
                        !item.matches("(.*)[/]{2}(.*)");

        return new ResultPair(item, valid);
    }

    private static ResultPair generateQuery(int length, double validRatio)
    /*
    https://tools.ietf.org/html/rfc3986#section-3.4

    The query component contains non-hierarchical data that, along with
    data in the path component (Section 3.3), serves to identify a
    resource within the scope of the URI's scheme and naming authority
    (if any).  The query component is indicated by the first question
    mark ("?") character and terminated by a number sign ("#") character
    or by the end of the URI.

    query       = *( pchar / "/" / "?" )
    */
    {
    	if(length == 0) {
    		return new ResultPair("", true);
    	}

    	String validChars = ALPHABET_CHARS + NUMERIC_CHARS + "&=+";
    	String invalidChars = "#";

    	String item = "?" + generateString(length, validRatio, validChars, invalidChars);
    	boolean valid = true;

        return new ResultPair(item, valid);
    }

    public void testRandomBadScheme()
    /*
    Tests a random bad scheme
    */
    {
        Random rand = new Random();
        double validRatio = 1;
        long options = UrlValidator.ALLOW_ALL_SCHEMES;
        UrlValidator urlVal = new UrlValidator(null, null, options);

        for(int i = 0; i < 100_000; i++) {
            ResultPair scheme = generateScheme(rand.nextInt(6)+2, 0);
            ResultPair authority = generateAuthority(rand.nextInt(20), validRatio);
            ResultPair port = generatePort(rand.nextInt(5), validRatio);
            ResultPair path = generatePath(rand.nextInt(30), validRatio);
            ResultPair query = generateQuery(rand.nextInt(30), validRatio);

            String url = scheme.item + authority.item + port.item + path.item + query.item;
            boolean valid = scheme.valid && authority.valid && port.valid && path.valid && query.valid;
            String message = "testRandomBadScheme(): all URLs should be invalid - " + url;
            assertEquals(message, valid, false);

            boolean resultValid = urlVal.isValid(url);
            message = url + " isValid is " + resultValid + " we expected " + valid;
            assertEquals(message, resultValid, valid);
        }

        System.out.println("testRandomBadScheme() passed.");
    }

    public void testRandomBadAuthority()
    /*
    Tests a random bad scheme
    */
    {
        Random rand = new Random();
        double validRatio = 1;
        long options = UrlValidator.ALLOW_ALL_SCHEMES;
        UrlValidator urlVal = new UrlValidator(null, null, options);

        for(int i = 0; i < 100_000; i++) {
            ResultPair scheme = generateScheme(rand.nextInt(6), validRatio);
            ResultPair authority = generateAuthority(rand.nextInt(20), 0);
            ResultPair port = generatePort(rand.nextInt(5), validRatio);
            ResultPair path = generatePath(rand.nextInt(30), validRatio);
            ResultPair query = generateQuery(rand.nextInt(30), validRatio);

            String url = scheme.item + authority.item + port.item + path.item + query.item;
            boolean valid = scheme.valid && authority.valid && port.valid && path.valid && query.valid;
            String message = "testRandomBadAuthority(): all URLs should be invalid - " + url;
            assertEquals(message, valid, false);

            boolean resultValid = urlVal.isValid(url);
            message = url + " isValid is " + resultValid + " we expected " + valid;
            assertEquals(message, resultValid, valid);
        }

        System.out.println("testRandomBadAuthority() passed.");
    }

    public void testRandomAll()
    /*
    Tests for fully random generated URLs that may be fully valid, or may have one or more components invalid.
    */
    {
    	Random rand = new Random();
    	double validRatio = 0.90;
    	long options = UrlValidator.ALLOW_ALL_SCHEMES;
		UrlValidator urlVal = new UrlValidator(null, null, options);

		for(int i = 0; i < 1_000_000; i++) {
    		ResultPair scheme = generateScheme(rand.nextInt(6), validRatio);
    		ResultPair authority = generateAuthority(rand.nextInt(20), validRatio);
    		ResultPair port = generatePort(rand.nextInt(5), validRatio);
    		ResultPair path = generatePath(rand.nextInt(30), validRatio);
    		ResultPair query = generateQuery(rand.nextInt(30), validRatio);
    		
    		String url = scheme.item + authority.item + port.item + path.item + query.item;
    		boolean valid = scheme.valid && authority.valid && port.valid && path.valid && query.valid;
    		boolean resultValid = urlVal.isValid(url);
    		
    		String message = url + " isValid is " + resultValid + " we expected " + valid;
    		assertEquals(message, resultValid, valid);
		}

		System.out.println("UrlValidatorTestRandom.testRandomAll() passed.");
    }
}
