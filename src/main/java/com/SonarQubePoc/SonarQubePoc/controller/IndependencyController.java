package com.SonarQubePoc.SonarQubePoc.controller;


import com.SonarQubePoc.SonarQubePoc.models.Country;
import com.SonarQubePoc.SonarQubePoc.models.CountryResponse;
import com.SonarQubePoc.SonarQubePoc.repositories.CountryRepository;
import com.SonarQubePoc.SonarQubePoc.util.DifferenceBetweenDates;
import java.time.Period;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController()
public class IndependencyController {

    CountryResponse countryResponse;
    Optional<Country> country;
    CountryRepository countryRepository;
    DifferenceBetweenDates differenceBetweenDates;

    public IndependencyController(CountryRepository countryRepository, DifferenceBetweenDates differenceBetweenDates) {
        this.countryRepository = countryRepository;
        this.differenceBetweenDates = differenceBetweenDates;
    }

    @GetMapping(path = "/country/{countryId}")
    public ResponseEntity<CountryResponse> getCountryDetails(@PathVariable("countryId") String countryId) {
        country = Optional.of(new Country());
        countryResponse = new CountryResponse();

        country = Optional.ofNullable(countryRepository.findCountryByIsoCode(countryId.toUpperCase()));

        if (country.isPresent()) {
            Period period = differenceBetweenDates.calculateYearsOfIndependency(country.get().getCountryIdependenceDate());
            countryResponse.setCountryName(country.get().getCountryName());
            countryResponse.setCapitalName(country.get().getCountryCapital());
            countryResponse.setIndependenceDate(country.get().getCountryIdependenceDate());
            countryResponse.setDayssOfIndependency(period.getDays());
            countryResponse.setMonthsOfIndependency(period.getMonths());
            countryResponse.setYearsOfIndependency(period.getYears());
        }
        return ResponseEntity.ok(countryResponse);
    }
}