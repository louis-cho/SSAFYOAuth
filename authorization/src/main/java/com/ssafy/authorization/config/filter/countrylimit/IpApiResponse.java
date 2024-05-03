package com.ssafy.authorization.config.filter.countrylimit;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IpApiResponse {
	private String ip;
	private boolean error;
	private String reason;
	private String network;
	private String version;
	private String city;
	private String region;
	private String region_code;
	private String country;
	private String country_name;
	private String country_code;
	private String country_code_iso3;
	private String country_capital;
	private String country_tld;
	private String continent_code;
	private boolean in_eu;
	private String postal;
	private double latitude;
	private double longitude;
	private String timezone;
	private String utc_offset;
	private String country_calling_code;
	private String currency;
	private String currency_name;
	private String languages;
	private double country_area;
	private int country_population;
	private String asn;
	private String org;
}