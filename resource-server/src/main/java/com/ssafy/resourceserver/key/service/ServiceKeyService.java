package com.ssafy.resourceserver.key.service;

import org.springframework.stereotype.Service;

public interface ServiceKeyService {

	boolean createServiceKey(Integer teamId, String userEmail);
	boolean reCreateServiceKey(Integer teamId, String userEmail);
}
