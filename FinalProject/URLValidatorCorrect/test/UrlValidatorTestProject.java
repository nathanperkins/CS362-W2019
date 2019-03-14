

import junit.framework.TestCase;

import java.util.Random;
import java.util.ArrayList;

//You can use this as a skeleton for your 3 different test approach
//It is an optional to use this file, you can generate your own test file(s) to test the target function!
// Again, it is up to you to use this file or not!


public class UrlValidatorTestProject extends TestCase {
    static final String LOWER_CASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    static final String UPPER_CASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String ALPHABET_CHARS   = LOWER_CASE_CHARS + UPPER_CASE_CHARS;
    static final String NUMERIC_CHARS = "0123456789";

    public UrlValidatorTestProject(String testName) {
        super(testName);
    }



    public void testManualTest()
    {
//You can use this function to implement your manual testing	   

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
        String validChars     = LOWER_CASE_CHARS + UPPER_CASE_CHARS + NUMERIC_CHARS + "+-.";
        String invalidChars   = "~!@#$%^&*()_";

        String item = generateString(length, validRatio, validChars, invalidChars) + "://";
        boolean valid = item == "" || item.matches("^[A-z]+[A-z0-9+\\-.]*://$");

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
        String validChars = ALPHABET_CHARS + NUMERIC_CHARS;
        String invalidChars = "~!@#$%^&*()_+";

        String host = generateString(length, validRatio, validChars, invalidChars);
        boolean hostValid = host.matches("^[A-z]+[A-z0-9.]*[A-z0-9]$");

        String domain = generateString(3, validRatio, ALPHABET_CHARS, NUMERIC_CHARS + invalidChars);
        boolean domainValid = domain.matches("^[A-z]+$");

        String item = host + "." + domain;
        boolean valid = hostValid && domainValid;
        return new ResultPair(item, valid);
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
        String validChars = ALPHABET_CHARS + NUMERIC_CHARS + "/";

        String item = "";
        boolean valid = true;

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

    }

    public void testRandomValid()
    /*
    Tests for random generated URLs that are all valid.
    */
    {

    }

    public void testRandomInvalidScheme()
    /*
    Tests for random generated URLs that are valid except for the scheme.
    */
    {

    }

    public void testRandomInvalidAuthority()
    /*
    Tests for random generated URLs that are valid except for the authority.
    */
    {
        ArrayList<ResultPair> schemes = new ArrayList<ResultPair>();
        for (int i = 0; i < 1000; i++)
        {
            ResultPair authority = generateAuthority(20, 0.95);
            System.out.println(authority.item + authority.valid);
        }
    }

    public void testRandomInvalidPort()
    /*
    Tests for random generated URLs that are valid except for the port.
    */
    {

    }

    public void testRandomInvalidPath()
    /*
    Tests for random generated URLs that are valid except for the path.
    */
    {

    }

    public void testRandomInvalidQuery()
    /*
    Tests for random generated URLs that are valid except for the query.
    */
    {

    }

    public void testRandomAll()
    /*
    Tests for fully random generated URLs that may be fully valid, or may have one or more components invalid.
    */
    {

    }
}
