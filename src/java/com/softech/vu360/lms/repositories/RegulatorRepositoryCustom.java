package com.softech.vu360.lms.repositories;

import java.util.List;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Regulator;
import com.softech.vu360.lms.model.RegulatoryAnalyst;

public interface RegulatorRepositoryCustom {
	public List<Regulator> findRegulator(String name, String alias, String emailAddress, List<ContentOwner> cos);
	public List<Regulator> searchRegulator(String name, List<ContentOwner> cos);
}
