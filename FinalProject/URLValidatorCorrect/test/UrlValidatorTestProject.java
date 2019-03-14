

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

    private static String generateString(int length, String validChars, String invalidChars)
    {
        StringBuilder s = new StringBuilder();
        double validRatio     = Math.pow(0.95, 1.0/length);

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


    private static ResultPair generateScheme(int length)
    /*
    https://tools.ietf.org/html/rfc3986#section-3.1
    scheme = ALPHA *( ALPHA / DIGIT / "+" / "-" / "." )
    */

    {
        String validChars     = LOWER_CASE_CHARS + UPPER_CASE_CHARS + NUMERIC_CHARS + "+-.";
        String invalidChars   = "~!@#$%^&*()_";

        String item = generateString(length, validChars, invalidChars) + "://";
        boolean valid = item == "" || item.matches("^[A-z]+[A-z0-9+\\-.]*://$");

        return new ResultPair(item, valid);
    }

    private static ResultPair generateAuthority(int length)
    /*
    https://tools.ietf.org/html/rfc3986#section-3.2

    authority   = [ userinfo "@" ] host [ ":" port ]

    */
    {
        String validChars = ALPHABET_CHARS + NUMERIC_CHARS;
        String invalidChars = "~!@#$%^&*()_+";

        String host = generateString(length, validChars, invalidChars);
        boolean hostValid = host.matches("^[A-z]+[A-z0-9.]*[A-z0-9]$");

        String domain = generateString(3, ALPHABET_CHARS, NUMERIC_CHARS + invalidChars);
        boolean domainValid = domain.matches("^[A-z]+$");

        String item = host + "." + domain;
        boolean valid = hostValid && domainValid;
        return new ResultPair(item, valid);
    }

    private static ResultPair generatePath(int length)
    {
        String validChars = ALPHABET_CHARS + NUMERIC_CHARS + "/";
    }

    public void testRandom()
    {
        ArrayList<ResultPair> schemes = new ArrayList<ResultPair>();
        for (int i = 0; i < 1000; i++)
        {
            ResultPair authority = generateAuthority(20);
            System.out.println(authority.item + authority.valid);
        }
    }
}
