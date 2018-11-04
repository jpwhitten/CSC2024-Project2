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
package uk.ac.ncl.cs.csc2024.operator;

import uk.ac.ncl.cs.csc2024.route.Route;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

/**
 * Hibernate Operator Entity
 *
 * Task: Create fields, methods and annotations which implicitly define an appropriate database table schema for
 * Operator records.
 *
 * @author hugofirth
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Operator.SELECT_ALL, query = "select o from Operator o order by o.name asc"),
        @NamedQuery(name = Operator.SELECT_ALL_ROUTES_BY_DIAMOND_BUSES, query = "select o.routes from Operator o where o.name = 'Diamond Buses'"),
        @NamedQuery(name = Operator.SELECT_ALL_FOR_PARK_GATES, query = "select o from Operator o, Route r, BusStop b inner join o.routes routes where routes.number = r.number and (r.start = b.id or r.destination = b.id) and b.description = 'Park Gates'")
})
@Table(name = "operator")
public class Operator {

	@Id @Column(name = "BUS_OPERATOR_NAME", nullable = false)
	private String name;
	
	@Column(name = "EMAIL", nullable = false)
	private String email;
	
	@ManyToMany(targetEntity = Route.class, mappedBy = "operators", cascade =  {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Route> routes = new HashSet<Route>();
	
    public static final String SELECT_ALL =  "Operator.selectAll";
    public static final String SELECT_ALL_ROUTES_BY_DIAMOND_BUSES =  "Operator.selectAllRoutesByDiamondBuses";
    public static final String SELECT_ALL_FOR_PARK_GATES =  "Operator.selectAllForParkGates";

 
    
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(Set<Route> routes) {
		this.routes = routes;
	}
    
	
	
}
