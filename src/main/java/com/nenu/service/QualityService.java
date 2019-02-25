package com.nenu.service;

import java.util.List;

import com.nenu.domain.Quality;

public interface QualityService {
	List<Quality> findAllQuality();

	int insertQuality(Quality quality);

	int updateQuality(Quality quality);

	int deleteQuality(int quality_ID);

	Quality findQualityById(int quality_ID);

}
