package com.nenu.dao;

import java.util.List;
import com.nenu.domain.Quality;

public interface QualityDao {
	int insertQuality(Quality quality);

	int updateQuality(Quality quality);

	int deleteQuality(int quality_ID);

	List<Quality> findAllQuality();

	Quality findQualityById(int quality_ID);
}
