/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.doctor;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
class VisitController {

	private final VisitRepository visits;

	private final PatientRepository patients;

	public VisitController(VisitRepository visits, PatientRepository patients) {
		this.visits = visits;
		this.patients = patients;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	/**
	 * Called before each and every @RequestMapping annotated method. 2 goals: - Make sure
	 * we always have fresh data - Since we do not use the session scope, make sure that
	 * Patient object always has an id (Even though id is not part of the form fields)
	 * @param patientId
	 * @return Patient
	 */
	@ModelAttribute("visit")
	public Visit loadPatientWithVisit(@PathVariable("patientId") int patientId, Map<String, Object> model) {
		Patient patient = this.patients.findById(patientId);
		patient.setVisitsInternal(this.visits.findByPatientId(patientId));
		model.put("patient", patient);
		Visit visit = new Visit();
		patient.addVisit(visit);
		return visit;
	}

	// Spring MVC calls method loadPatientWithVisit(...) before initNewVisitForm is called
	@GetMapping("/doctors/*/patients/{patientId}/visits/new")
	public String initNewVisitForm(@PathVariable("patientId") int patientId, Map<String, Object> model) {
		return "patients/createOrUpdateVisitForm";
	}

	// Spring MVC calls method loadPatientWithVisit(...) before processNewVisitForm is called
	@PostMapping("/doctors/{doctorId}/patients/{patientId}/visits/new")
	public String processNewVisitForm(@Valid Visit visit, BindingResult result) {
		if (result.hasErrors()) {
			return "patients/createOrUpdateVisitForm";
		}
		else {
			this.visits.save(visit);
			return "redirect:/doctors/{doctorId}";
		}
	}

}
