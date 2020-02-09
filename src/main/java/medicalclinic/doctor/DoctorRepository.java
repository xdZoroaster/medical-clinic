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

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository class for <code>Doctor</code> domain objects All method names are compliant
 * with Spring Data naming conventions so this interface can easily be extended for Spring
 * Data. See:
 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
 */
public interface DoctorRepository extends Repository<Doctor, Integer> {

    /**
     * Retrieve {@link Doctor}s from the data store by last name, returning all doctors
     * whose last name <i>starts</i> with the given name.
     *
     * @param lastName Value to search for
     * @return a Collection of matching {@link Doctor}s (or an empty Collection if none
     * found)
     */
    @Query("SELECT DISTINCT doctor FROM Doctor doctor left join fetch doctor.patients WHERE doctor.lastName LIKE :lastName%")
    @Transactional(readOnly = true)
    Collection<Doctor> findByLastName(@Param("lastName") String lastName);

    /**
     * Retrieve an {@link Doctor} from the data store by id.
     *
     * @param id the id to search for
     * @return the {@link Doctor} if found
     */
    @Query("SELECT doctor FROM Doctor doctor left join fetch doctor.patients WHERE doctor.id =:id")
    @Transactional(readOnly = true)
    Doctor findById(@Param("id") Integer id);

    /**
     * Save an {@link Doctor} to the data store, either inserting or updating it.
     *
     * @param doctor the {@link Doctor} to save
     */
    void save(Doctor doctor);

}
