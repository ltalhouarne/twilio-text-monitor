package com.loic.perso;

import com.loic.perso.health.TimeConsumedIndicator;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@SpringBootApplication
public class TextmeApplication {

    @Autowired
    private TwilioRestClient twilioRestClient;

    @Autowired
    private TimeConsumedIndicator timeConsumedIndicator;

    private int count = 0;

    private boolean setup = false;

    public static void main(String[] args) {
		SpringApplication.run(TextmeApplication.class, args);
	}

    @Scheduled(fixedDelay = 10000)
    public void textMeIfSomethingChanges() throws IOException, TwilioRestException {
        if(!setup){
            setup = true;
            twilioRestClient.getAccount().getMessageFactory().create(getParams("OMITTED", "Monitor setup. " +
                    "Interval of 10 seconds. You will get notified when a new update is posted on OMITTED's website."));
        }

        String response = Jsoup.connect("OMITTED").userAgent("Mozilla").get().html();

        timeConsumedIndicator.setLocalDateTime(LocalDateTime.now());

        int index = response.indexOf("?releaseid");

        if(!response.substring(index, index + 40).contains("?releaseid=OMITTED") && count++ <=2) {
            twilioRestClient.getAccount().getMessageFactory().create(getParams("OMITTED", response.substring(index, index + 400)));
        }
    }

    private List<NameValuePair> getParams(String to, String message) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Body", message));
        params.add(new BasicNameValuePair("To", to));
        params.add(new BasicNameValuePair("From", "OMITTED"));
        return params;
    }
}
