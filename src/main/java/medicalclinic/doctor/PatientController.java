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
package medicalclinic.doctor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
@RequestMapping("/doctors/{doctorId}")
class PatientController {

    private static final String VIEWS_PATIENTS_CREATE_OR_UPDATE_FORM = "patients/createOrUpdatePatientForm";

    private final PatientRepository patients;

    private final DoctorRepository doctors;

    public PatientController(PatientRepository patients, DoctorRepository doctors) {
        this.patients = patients;
        this.doctors = doctors;
    }

    @ModelAttribute("types")
    public Collection<Gender> populatePatientTypes() {
        return this.patients.findGenderTypes();
    }

    @ModelAttribute("doctor")
    public Doctor findDoctor(@PathVariable("doctorId") int doctorId) {
        return this.doctors.findById(doctorId);
    }

    @InitBinder("doctor")
    public void initDoctorBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @InitBinder("patient")
    public void initPatientBinder(WebDataBinder dataBinder) {
        dataBinder.setValidator(new PatientValidator());
    }

    @GetMapping("/patients/new")
    public String initCreationForm(Doctor doctor, ModelMap model) {
        Patient patient = new Patient();
        doctor.addPatient(patient);
        model.put("patient", patient);
        return VIEWS_PATIENTS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/patients/new")
    public String processCreationForm(Doctor doctor, @Valid Patient patient, BindingResult result, ModelMap model) {
        if (StringUtils.hasLength(patient.getName()) && patient.isNew() && doctor.getPatient(patient.getName(), true) != null) {
            result.rejectValue("name", "duplicate", "already exists");
        }
        doctor.addPatient(patient);
        if (result.hasErrors()) {
            model.put("patient", patient);
            return VIEWS_PATIENTS_CREATE_OR_UPDATE_FORM;
        }
        else {
            this.patients.save(patient);
            return "redirect:/doctors/{doctorId}";
        }
    }

    @GetMapping("/patients/{patientId}/edit")
    public String initUpdateForm(@PathVariable("patientId") int patientId, ModelMap model) {
        Patient patient = this.patients.findById(patientId);
        model.put("patient", patient);
        return VIEWS_PATIENTS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/patients/{patientId}/edit")
    public String processUpdateForm(@Valid Patient patient, BindingResult result, Doctor doctor, ModelMap model) {
        if (result.hasErrors()) {
            patient.setDoctor(doctor);
            model.put("patient", patient);
            return VIEWS_PATIENTS_CREATE_OR_UPDATE_FORM;
        }
        else {
            doctor.addPatient(patient);
            this.patients.save(patient);
            return "redirect:/doctors/{doctorId}";
        }
    }

}
