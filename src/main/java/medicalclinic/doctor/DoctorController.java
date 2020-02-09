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

import medicalclinic.visit.VisitRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

@Controller
class DoctorController {

    private static final String VIEWS_DOCTOR_CREATE_OR_UPDATE_FORM = "doctors/createOrUpdateDoctorForm";

    private final DoctorRepository doctors;

    private VisitRepository visits;

    public DoctorController(DoctorRepository clinicService, VisitRepository visits) {
        this.doctors = clinicService;
        this.visits = visits;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping("/doctors/new")
    public String initCreationForm(Map<String, Object> model) {
        Doctor doctor = new Doctor();
        model.put("doctor", doctor);
        return VIEWS_DOCTOR_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/doctors/new")
    public String processCreationForm(@Valid Doctor doctor, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_DOCTOR_CREATE_OR_UPDATE_FORM;
        } else {
            this.doctors.save(doctor);
            return "redirect:doctors/" + doctor.getId();
        }
    }

    @GetMapping("/doctors/find")
    public String initFindForm(Map<String, Object> model) {
        model.put("doctor", new Doctor());
        return "doctors/findDoctors";
    }

    @GetMapping("/doctors")
    public String processFindForm(Doctor doctor, BindingResult result, Map<String, Object> model) {

        // allow parameterless GET request for /doctors to return all records
        if (doctor.getLastName() == null) {
            doctor.setLastName(""); // empty string signifies broadest possible search
        }

        // find doctors by last name
        Collection<Doctor> results = this.doctors.findByLastName(doctor.getLastName());
        if (results.isEmpty()) {
            // no doctors found
            result.rejectValue("lastName", "notFound", "not found");
            return "doctors/findDoctors";
        } else if (results.size() == 1) {
            // 1 doctor found
            doctor = results.iterator().next();
            return "redirect:/doctors/" + doctor.getId();
        } else {
            // multiple doctors found
            model.put("selections", results);
            return "doctors/doctorList";
        }
    }

    @GetMapping("/doctors/{doctorId}/edit")
    public String initUpdateDoctorForm(@PathVariable("doctorId") int doctorId, Model model) {
        Doctor doctor = this.doctors.findById(doctorId);
        model.addAttribute(doctor);
        return VIEWS_DOCTOR_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/doctors/{doctorId}/edit")
    public String processUpdateDoctorForm(@Valid Doctor doctor, BindingResult result,
                                          @PathVariable("doctorId") int doctorId) {
        if (result.hasErrors()) {
            return VIEWS_DOCTOR_CREATE_OR_UPDATE_FORM;
        } else {
            doctor.setId(doctorId);
            this.doctors.save(doctor);
            return "redirect:/doctors/{doctorId}";
        }
    }

    /**
     * Custom handler for displaying a doctor.
     *
     * @param doctorId the ID of the doctor to display
     * @return a ModelMap with the model attributes for the view
     */
    @GetMapping("/doctors/{doctorId}")
    public ModelAndView showDoctor(@PathVariable("doctorId") int doctorId) {
        ModelAndView mav = new ModelAndView("doctors/doctorDetails");
        Doctor doctor = this.doctors.findById(doctorId);
        for (Patient patient : doctor.getPatients()) {
            patient.setVisitsInternal(visits.findByPatientId(patient.getId()));
        }
        mav.addObject(doctor);
        return mav;
    }

}
