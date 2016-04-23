package com.tudor.rotarus.unibuc.metme.pojos.interfaces.network;

import com.tudor.rotarus.unibuc.metme.pojos.responses.get.CountryGetBody;

/**
 * Created by Tudor on 30.03.2016.
 */
public interface CountriesListener {
    void onCountryPopulateSuccess(CountryGetBody response);
    void onCountryPopulateFailure();
}
