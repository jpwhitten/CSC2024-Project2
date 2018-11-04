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
package uk.ac.ncl.cs.csc2024.route;

import uk.ac.ncl.cs.csc2024.busstop.BusStop;
import uk.ac.ncl.cs.csc2024.operator.Operator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
        @NamedQuery(name = Route.SELECT_ALL, query = "select r from Route r order by r.number asc"),
        @NamedQuery(name = Route.SELECT_ALL_FOR_RAILWAY_STATION, query = "select r from Route r, BusStop b where (r.start = b.id or r.destination = b.id) and b.description = 'Railway Station'"),
        @NamedQuery(name = Route.CUMULATIVE_FREQUENCY_BY_OK_TRAVEL, query = "select sum(r.frequency)/r.operators.size from Route r, Operator o inner join r.operators operators inner join o.routes routes where operators.name = 'OK Travel' and routes.number = r.number")
})
@Table(name = "route")
public class Route {
	
	@Id @Column(name = "ROUTE_NUMBER", nullable = false)
	private String number;
	
	@ManyToOne(targetEntity = BusStop.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "START",  nullable = false)
	private BusStop start;
	
	@ManyToOne(targetEntity = BusStop.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "DESTINATION", nullable = false)
	private BusStop destination;
	
	@Column(name = "FREQUENCY", nullable = false)
	private int frequency;
	
	@ManyToMany(targetEntity = Operator.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "route_operators",
			 joinColumns= @JoinColumn(name="route"),  
			 inverseJoinColumns=@JoinColumn(name="operators"))
	private Set<Operator> operators = new HashSet<Operator>();
		
    public static final String SELECT_ALL = "Route.selectAll";
    public static final String SELECT_ALL_FOR_RAILWAY_STATION = "Route.selectAllForRailwayStation";
    public static final String CUMULATIVE_FREQUENCY_BY_OK_TRAVEL = "Route.cumulativeFrequencyOkTravel";
  
    
    
	public String getNumber() {
		return number;
	}

	public void setNumber(String routeNumber) {
		this.number = routeNumber;
	}


	public BusStop getStart() {
		return start;
	}

	public void setStart(BusStop start) {
		this.start = start;
	}
	
	
	public BusStop getDestination() {
		return destination;
	}

	public void setDestination(BusStop destination) {
		this.destination = destination;
	}


	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public Set<Operator> getOperators() {
		return operators;
	}

	public void setOperators(Set<Operator> operators) {
		this.operators = operators;
	}
	
	
	
}
