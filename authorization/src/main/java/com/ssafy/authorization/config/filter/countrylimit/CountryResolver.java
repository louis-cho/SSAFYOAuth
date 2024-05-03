package com.ssafy.authorization.config.filter.countrylimit;

public interface CountryResolver {
	String resolveCountry(String ip);
}
