package com.moh.phlat.backend.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.moh.phlat.backend.model.SourceDataFilterParams;
import com.moh.phlat.backend.model.SourceData;

public interface SourceDataService {
	
	List<String> SOURCE_DATA_COLUMNS = Arrays.asList("control_id", "doNotLoadFlag", "stakeholder", "stakeholderId", "hdsIpcId", "hdsCpnId", "hdsProviderIdentifier1",
			"hdsProviderIdentifier2", "hdsProviderIdentifier3", "hdsProviderIdentifierType1", "hdsProviderIdentifierType2", "hdsProviderIdentifierType3", "hdsHibcFacilityId",
			"hdsType", "hdsName", "hdsNameAlias", "hdsPreferredNameFlag", "hdsEmail", "hdsWebsite", "hdsBusTelAreaCode", "hdsBusTelNumber", "hdsTelExtension", 
			"hdsCellAreaCode", "hdsCellNumber", "hdsFaxAreaCode", "hdsFaxNumber", "hdsServiceDeliveryType", "pcnClinicType", "pcnPciFlag", "hdsHoursOfOperation",
			"hdsContactName", "hdsIsForProfitFlag", "sourceStatus", "hdaParentIpcId", "busIpcId", "busCpnId", "busName",  "busLegalName", "busPayeeNumber", "busOwnerName",
			"busOwnerType", "busOwnerTypeOther", "facBuildingName", "facilityHdsDetailsAdditionalInfo", "physicalAddr1", "physicalAddr2", "physicalAddr3", "physicalAddr4",
			"physicalCity", "physicalProvince", "physicalPcode", "physicalCountry", "physAddrIsPrivate", "mailAddr1", "mailAddr2", "mailAddr3", "mailAddr4", "mailCity",
			"mailBc", "mailPcode", "mailCountry", "mailAddrIsPrivate","hdsSubType", "hdsMspFacilityNumber", "pcnServiceDeliveryType", "pcnClinicStatus",
			"hdsEffectiveStartDate", "facAddressUnit", "physicalAddrPrpsTypeCd");

	List<SourceData> getSourceData(Long controlId,  SourceDataFilterParams filterSource, Pageable pageable);
	
	Long countSourceData(Long controlId,  SourceDataFilterParams filterSource);
	
	List<String> getUniqueColumnValues(Long controlTableId, String columnKey);
}
