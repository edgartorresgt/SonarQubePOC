package com.SonarQubePoc.SonarQubePoc.repositories;

import com.SonarQubePoc.SonarQubePoc.models.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {
    Country findCountryByIsoCode(String isoCode);
}
