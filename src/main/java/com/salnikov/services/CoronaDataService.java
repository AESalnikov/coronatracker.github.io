package com.salnikov.services;

import com.salnikov.models.Location;
import lombok.Getter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaDataService {

    private static final String CORONA_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    @Getter
    private List<Location> allStats = new ArrayList<>();

    @PostConstruct
//   <minute> <hour> <day-of-month> <month> <day-of-week> <command>
    @Scheduled(cron = "* 9 * * * *") // запуск каждый день в 9:00
    public void fetchCoronaData() throws IOException {
//        RestTemplate restTemplate = new RestTemplate();
//        final String stringPosts = restTemplate.getForObject(CORONA_DATA_URL, String.class);
//        System.out.println(stringPosts);

        List<Location> newStats = new ArrayList<>();

        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");
        HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
        ResponseEntity<String> responseEntity = rest.exchange(CORONA_DATA_URL, HttpMethod.GET, requestEntity, String.class);

        StringReader csvReader = new StringReader(responseEntity.getBody());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);

        for (CSVRecord record : records) {
            Location location = new Location();
            location.setState(record.get("Province/State"));
            location.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size()-1));
            int prevDayCases = Integer.parseInt(record.get(record.size()-2));
            location.setLatestTotalCases(latestCases);
            location.setDelta(latestCases - prevDayCases);
            newStats.add(location);
        }
        this.allStats = newStats;
    }
}
