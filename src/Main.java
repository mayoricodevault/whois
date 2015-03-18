import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.SocketException;
import org.apache.commons.net.whois.WhoisClient;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Main
{


    public static void main(String[] args)
    {
        String domain = "globalflock.com";
        String domainInfo = getWhois(domain);
        System.out.println(domainInfo);
        System.out.println(getOrganization(domainInfo));



    }

    public static String getWhois(String domainName) {

        StringBuilder result = new StringBuilder("");

        WhoisClient whois = new WhoisClient();
        try {

            whois.connect(WhoisClient.DEFAULT_HOST);
            String whoisData1 = whois.query("=" + domainName);
            // append first result
            //result.append(whoisData1);
            whois.disconnect();
            // get the google.com whois server - whois.markmonitor.com
            String whoisServerUrl = getWhoisServer(whoisData1);
            if (!whoisServerUrl.equals("")) {
                // whois -h whois.markmonitor.com google.com
                String whoisData2 =
                        queryWithWhoisServer(domainName, whoisServerUrl);
                result.append(whoisData2);
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();

    }

    private static String queryWithWhoisServer(String domainName, String whoisServer) {

        String result = "";
        WhoisClient whois = new WhoisClient();
        try {

            whois.connect(whoisServer);
            result = whois.query(domainName);
            whois.disconnect();

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }

    private static String getWhoisServer(String whois) {
// regex whois parser
        Pattern pattern = null;
        Matcher matcher;
        final String WHOIS_SERVER_PATTERN = "Whois Server:\\s(.*)";
        pattern = Pattern.compile(WHOIS_SERVER_PATTERN);

        String result = "";

        matcher = pattern.matcher(whois);

        // get last whois server
        while (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    private static String getOrganization(String whois) {
// regex whois parser
        Pattern pattern = null;
        Matcher matcher;
        final String WHOIS_SERVER_PATTERN = "Registrant Organization:\\s(.*)";
        pattern = Pattern.compile(WHOIS_SERVER_PATTERN);



        String result = "";

        matcher = pattern.matcher(whois);

        // get last whois server
        while (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

}