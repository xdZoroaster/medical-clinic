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

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

public interface PatientRepository extends Repository<Patient, Integer> {

    /**
     * Retrieve all {@link Gender}s from the data store.
     *
     * @return a Collection of {@link Gender}s.
     */
    @Query("SELECT gender FROM Gender gender ORDER BY gender.name")
    @Transactional(readOnly = true)
    List<Gender> findGenderTypes();

    /**
     * Retrieve a {@link Patient} from the data store by id.
     *
     * @param id the id to search for
     * @return the {@link Patient} if found
     */
    @Transactional(readOnly = true)
    Patient findById(Integer id);

    /**
     * Save a {@link Patient} to the data store, either inserting or updating it.
     *
     * @param patient the {@link Patient} to save
     */
    void save(Patient patient);

}
