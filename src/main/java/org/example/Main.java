package org.example;

import java.io.IOException;

import com.ringcentral.RestClient;
import com.ringcentral.RestException;
import com.ringcentral.definitions.CreateBridgeRequest;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) throws IOException, RestException {
        Dotenv.configure().systemProperties().load();

        var rc = new RestClient(System.getProperty("RINGCENTRAL_CLIENT_ID"),
                System.getProperty("RINGCENTRAL_CLIENT_SECRET"),
                System.getProperty("RINGCENTRAL_SERVER_URL"));
        rc.authorize(System.getProperty("RINGCENTRAL_JWT_TOKEN"));

        // create a bridge
        var body = new CreateBridgeRequest();
        body.name = "Test Meeting";
        var bridge = rc.rcvideo().v2().account("~").extension("~").bridges().post(body);
        // delete the bridge after testing
        rc.rcvideo().v2().bridges(bridge.id).delete();
        
        
        // personal meeting
        var personalMeeting = rc.rcvideo().v2().account("~").extension("~").bridges().default1().get();
        System.out.println(String.format("You personal meeting URL is %s", personalMeeting.discovery.web));

        // meeting history
        var r = rc.rcvideo().v1().history().meetings().list();
        for(var meeting : r.meetings)
        {
            System.out.println("Meeting:");
            System.out.println("  name: " + meeting.displayName);
            System.out.println("  start time: " + meeting.startTime);
            System.out.println("  duration: " + meeting.duration);
        }
    }
}