package com.tudor.rotarus.unibuc.metme.pojos.requests.get;

/**
 * Created by Tudor on 07.03.2016.
 */
public class CountryGetBody {
    private String name;
    private String phoneCode;

    public CountryGetBody(String name, String phoneCode) {
        this.name = name;
        this.phoneCode = phoneCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }
}
