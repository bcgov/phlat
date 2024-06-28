package com.moh.phlat.backend.service;

import com.moh.phlat.backend.model.ProcessData;
import com.moh.phlat.backend.service.dto.ReportSummary;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProcessDataService {
	
	public final List<String> PROCESS_DATA_COLUMNS = Arrays.asList("id", "controlTableId", "do_not_load", "stakeholder", "hdsIpcId", "hdsCpnId", "hdsProviderIdentifier1",
			"hdsProviderIdentifier2", "hdsProviderIdentifier3", "hdsProviderIdentifierType1", "hdsProviderIdentifierType2", "hdsProviderIdentifierType3", 
			"hdsMspFacilityNumber", "hdsType", "hdsName", "hds_name_alias", "hdsPreferredNameFlag", "hdsEmail", "hdsWebsite", "hdsBusTelAreaCode", "hdsBusTelNumber",
			"hdsTelExtension", "hdsCellAreaCode", "hdsCellNumber", "hdsFaxAreaCode", "hdsFaxNumber", "pcnServiceDeliveryType", "pcnClinicType", "pcnPciFlag",
			"hdsHoursOfOperation", "hdsContactName", "hdsIsForProfitFlag", "sourceStatus", "hdsParentIpcId", "busIpcId", "busCpnId", "busName", "busLegalName",
			"busPayeeNumber", "busOwnerName", "busOwnerType", "busOwnerTypeOther", "facBuildingName", "facilityHdsDetailsAdditionalInfo", "physicalAddr1",
			"physicalAddr2", "physicalAddr3", "physicalAddr", "physicalCity", "physicalProvince", "physicalPcode", "physicalCountry", "physAddrIsPrivate",
			"mailAddr1", "mailAddr2", "mailAddr3", "mailAddr4", "mailCity", "mailBc", "mailPcode", "mailCountry", "mailAddrIsPrivate");

    List<ReportSummary> getReportSummary(Long controlTableId);
    
    List<ProcessData> getProcessDataWithMessages(Long controlTableId, String rowStatus, Pageable pageable);
	
	List<ProcessData> getProcessDataWithMessages(Long controlId, String reqRowStatusCode, List<String> ids, List<String> actions, 
		List<String> rowStatusCode, List<String> messages, List<String> doNotLoad, List<String> stakeholder, List<String> hdsLpcId, 
		List<String> hdsCpnId, List<String> hdsProviderId1, List<String> hdsProviderId2, List<String> hdsProviderId3, 
		List<String> hdsProviderIdType1, List<String> hdsProviderIdType2, List<String> hdsProviderIdType3, List<String> hdsHibcFacId, 
		List<String> hdsType, List<String> hdsName, List<String> hdsNameAlias, List<String> hdsPrefNameFlag, List<String> hdsEmail, 
		List<String> hdsWebsite, List<String> hdsBusTelAreaCode, List<String> hdsBusTelNum, List<String> hdsTelExt, List<String> hdsCellAreaCode,
		List<String> hdsCellNum, List<String> hdsFaxAreaCode, List<String> hdsFaxNum, List<String> hdsServiceDelType, List<String> pcnCLinicType,
		List<String> pcnPciFlag, List<String> hdsHoursOfOp, List<String> hdsContactName, List<String> hdsIsForProfitFlag, List<String> sourceStatus,
		List<String> hdsParentIpcId, List<String> busIpcId, List<String> busCpnId, List<String> busName, List<String> busLegalName, 
		List<String> busPayeeNum, List<String> busOwnerName, List<String> busOwnerType, List<String> busOwnerTypeOther, 
		List<String> facBuildingName, List<String> facHdsDetailAddInfo, List<String> physAddr1, List<String> physAddr2, List<String> physAddr3,
		List<String> physAddr4, List<String> physCity, List<String> physProv, List<String> physPCode, List<String> physCountry, 
		List<String> physAddrIsPrivate, List<String> mailAddr1, List<String> mailAddr2, List<String> mailAddr3, List<String> mailAddr4, 
		List<String> mailCity, List<String> mailBc, List<String> mailPcode, List<String> mailCountry, List<String> mailAddrIsPriv, Pageable pageable);
	
	public List<String> getDistinctColumnValues(@PathVariable Long controlTableId, @PathVariable String columnKey);
}
