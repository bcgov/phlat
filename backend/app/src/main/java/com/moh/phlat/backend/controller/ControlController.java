package com.moh.phlat.backend.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.moh.phlat.backend.model.Control;
import com.moh.phlat.backend.repository.ControlRepository;
import com.moh.phlat.backend.response.ResponseMessage;
import com.moh.phlat.backend.service.ControlService;
import com.moh.phlat.backend.service.ProcessDataService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/controltable")
@CrossOrigin(origins = "*")
public class ControlController {
	private static final Logger logger = LoggerFactory.getLogger(ControlController.class);

	@Autowired
	private ControlRepository controlRepository;
	
	@Autowired
	private ControlService controlService;
	
	@PreAuthorize("hasAnyRole(@roleService.getAllRoles())")
	@GetMapping("/view/all")
	public @ResponseBody ResponseEntity<ResponseMessage> getAllControls() {
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseMessage("success", 200, "", controlRepository.findAll()));
	}

	// view specific file control
	@PreAuthorize("hasAnyRole(@roleService.getAllRoles())")
	@GetMapping("/view/{id}")
	public ResponseEntity<ResponseMessage> getControlById(@PathVariable Long id) {
		List<Control> controlTable = controlService.findById(id);
		if (controlTable.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseMessage("success", 404, "Control table not found with id: " + id, "[]"));
		}

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseMessage("success", 200, "", controlService.findById(id)));
	}

	@PreAuthorize("hasAnyRole(@roleService.getAllRoles())")
	@GetMapping("/view/filename/{fileName}")
	public ResponseEntity<ResponseMessage> getControlByFileName(@PathVariable String fileName) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseMessage("success", 200, "", controlService.findByFileName(fileName)));

	}

	@PreAuthorize("hasAnyRole(@roleService.getAllRoles())")
	@PutMapping("/update/{id}")
	public ResponseEntity<ResponseMessage> updateControl(@PathVariable("id") long id,
			@RequestBody Control requestControl) {
		List<Control> controlTableData = controlService.findById(id);

		if (controlTableData.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseMessage("error", 404, "Control table not found with id: " + id, "[]"));
		}

		try {

			Control _controlTable = controlTableData.get(0);

			_controlTable.setFileName(requestControl.getFileName());
			_controlTable.setUserId(AuthenticationUtils.getAuthenticatedUserId());
			_controlTable.setFileExtractedDate(requestControl.getFileExtractedDate());
			_controlTable.setBatchLabelName(requestControl.getBatchLabelName());

			_controlTable.setLoadTypeFacility(requestControl.getLoadTypeFacility());
			_controlTable.setLoadTypeHds(requestControl.getLoadTypeHds());
			_controlTable.setLoadTypeOrg(requestControl.getLoadTypeOrg());
			_controlTable.setLoadTypeOFRelationship(requestControl.getLoadTypeOFRelationship());
			_controlTable.setLoadTypeOORelationship(requestControl.getLoadTypeOORelationship());
			_controlTable.setLoadTypeIORelationship(requestControl.getLoadTypeIORelationship());
			_controlTable.setLoadTypeWOXref(requestControl.getLoadTypeWOXref());
			_controlTable.setLoadTypeWPIXref(requestControl.getLoadTypeWPIXref());
			_controlTable.setStatusCode(requestControl.getStatusCode());

			_controlTable.setUpdatedBy(AuthenticationUtils.getAuthenticatedUserId());
			_controlTable.setUpdatedAt(new Date());

			controlRepository.save(_controlTable);

			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("success", 200, "", _controlTable));

		} catch (Exception e) {
			logger.error("Error occured: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("error", 500,
					"Internal error encountered while updating Control table with id: " + id, "[]"));
		}
	}

	@PreAuthorize("hasRole(@roleService.getRegAdminRole())")
	@PutMapping("/approve/{id}")
	public ResponseEntity<ResponseMessage> approveToLoadToPLR(@PathVariable("id") long id) {
		Optional<Control> controlTableData = controlRepository.findById(id);

		if (controlTableData.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseMessage("error", 404, "Control table not found with id: " + id, "[]"));
		}

		try {

			Control _controlTable = controlTableData.get();

			_controlTable.setStatusCode("APPROVED");

			_controlTable.setUpdatedBy(AuthenticationUtils.getAuthenticatedUserId());
			_controlTable.setUpdatedAt(new Date());

			controlRepository.save(_controlTable);

			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("success", 200, "Record approved successfully.", _controlTable));

		} catch (Exception e) {
			logger.error("Error occured: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("error", 500,
					"Internal error encountered while approving constrol table with id: " + id, "[]"));
		}
	}
}
