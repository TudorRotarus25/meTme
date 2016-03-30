package com.tudor.rotarus.unibuc.metme.pojos.interfaces;

import com.tudor.rotarus.unibuc.metme.pojos.requests.get.CountryGetBody;

/**
 * Created by Tudor on 30.03.2016.
 */
public interface CountriesListener {
    void onCountryPopulateSuccess(CountryGetBody response);
    void onCountryPopulateFailure();
}
