/**
 * csc2024-hibernate-assignment
 *
 * Copyright (c) 2015 Newcastle University
 * Email: <h.firth@ncl.ac.uk/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package uk.ac.ncl.cs.csc2024.busstop;

import uk.ac.ncl.cs.csc2024.route.Route;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Hibernate BusStop Entity
 *
 * Task: Create fields, methods and annotations which implicitly define an appropriate database table schema for
 * BusStop records.
 *
 * @author hugofirth
 */
@Entity
@NamedQueries({
        @NamedQuery(name = BusStop.SELECT_ALL, query = "select b from BusStop b order by b.id asc"),
        @NamedQuery(name = BusStop.SELECT_MAX_ID, query = "select b from BusStop b where b.id = (select max(id) from BusStop)")
})
@Table(name = "bus_stop")
public class BusStop {

	@Id @Column(name="BUS_STOP_ID", nullable=false)
	private int id;
	
	@Column(name="DESCRIPTION", nullable=false)
	private String description;
	
	
	@OneToMany(targetEntity = Route.class, mappedBy = "start", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Route> starts = new HashSet<Route>();
	
	@OneToMany(targetEntity = Route.class, mappedBy = "destination", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Route> destinations = new HashSet<Route>();
	
    public static final String SELECT_ALL = "BusStop.selectAll";
    public static final String SELECT_MAX_ID = "BusStop.selectMaxId";
    
    

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
    
}
