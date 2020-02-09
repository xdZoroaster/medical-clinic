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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.core.style.ToStringCreator;
import medicalclinic.model.Person;


@Entity
@Table(name = "doctors")
public class Doctor extends Person {

    @Column(name = "address")
    @NotEmpty
    private String address;

    @Column(name = "city")
    @NotEmpty
    private String city;

    @Column(name = "telephone")
    @NotEmpty
    @Digits(fraction = 0, integer = 10)
    private String telephone;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor")
    private Set<Patient> patients;

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    protected Set<Patient> getPatientsInternal() {
        if (this.patients == null) {
            this.patients = new HashSet<>();
        }
        return this.patients;
    }

    protected void setPatientsInternal(Set<Patient> patients) {
        this.patients = patients;
    }

    public List<Patient> getPatients() {
        List<Patient> sortedPatients = new ArrayList<>(getPatientsInternal());
        PropertyComparator.sort(sortedPatients, new MutableSortDefinition("name", true, true));
        return Collections.unmodifiableList(sortedPatients);
    }

    public void addPatient(Patient patient) {
        if (patient.isNew()) {
            getPatientsInternal().add(patient);
        }
        patient.setDoctor(this);
    }

    /**
     * Return the Patient with the given name, or null if none found for this Doctor.
     *
     * @param name to test
     * @return true if pet name is already in use
     */
    public Patient getPatient(String name) {
        return getPatient(name, false);
    }

    /**
     * Return the Patient with the given name, or null if none found for this Doctor.
     *
     * @param name to test
     * @return true if pet name is already in use
     */
    public Patient getPatient(String name, boolean ignoreNew) {
        name = name.toLowerCase();
        for (Patient patient : getPatientsInternal()) {
            if (!ignoreNew || !patient.isNew()) {
                String compName = patient.getName();
                compName = compName.toLowerCase();
                if (compName.equals(name)) {
                    return patient;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)

            .append("id", this.getId()).append("new", this.isNew()).append("lastName", this.getLastName())
            .append("firstName", this.getFirstName()).append("address", this.address).append("city", this.city)
            .append("telephone", this.telephone).toString();
    }

}
