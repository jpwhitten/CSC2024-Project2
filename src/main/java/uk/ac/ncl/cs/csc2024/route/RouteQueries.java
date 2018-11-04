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

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.Type;

import uk.ac.ncl.cs.csc2024.busstop.BusStop;
import uk.ac.ncl.cs.csc2024.operator.Operator;
import uk.ac.ncl.cs.csc2024.query.ExampleQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Collection of Queries relating to Route entities
 *
 * Task: fill out method bodies to create "Queries" which satisfy the requirements laid out in the coursework spec.
 * Other than the `insert(...)` method, where you should return the session to which an Entity object has been persisted,
 * you should return an ExampleQuery object.
 *
 * The methods of the ExampleQuery objects should return a Query, a Named Query's identifier and a Criteria Query for
 * each task laid out in the coursework spec. You should return every query type for each task.
 *
 * An example of how this should look is provided in the `selectAll(...)` query.
 *
 * @author hugofirth
 */
public class RouteQueries {

    public static Session insert(final Map<String, String> row, final Session session) {
    	
    	Route route = new Route();
    	route.setNumber(row.get("number"));
    	route.setFrequency(Integer.parseInt(row.get("frequency")));
    	int startStop = Integer.parseInt(row.get("start"));
    	route.setStart( (BusStop) session.createQuery("select b from BusStop b where b.id = (:startStop)").setParameter("startStop", startStop).uniqueResult());
    	int destinationStop = Integer.parseInt(row.get("destination"));
    	route.setDestination( (BusStop) session.createQuery("select b from BusStop b where b.id = (:destinationStop)").setParameter("destinationStop", destinationStop).uniqueResult());
    	
    	List<String> operatorNames = Arrays.asList(row.get("operators").split("\\|"));
    	List<Operator> operators = session.createQuery("select o from Operator o where o.name IN (:operatorNames)").setParameterList("operatorNames", operatorNames).list();
    	route.setOperators(new HashSet<Operator>(operators));
    	
    	session.save(route);
    	
        return session;
    }

    public static ExampleQuery selectAll() {
        return new ExampleQuery() {
            @Override
            public Query getQuery(Session session) {
                return session.createQuery("select r from Route r order by r.number asc");
            }

            @Override
            public String getNamedQueryName() {
                return Route.SELECT_ALL;
            }

            @Override
            public Criteria getCriteria(Session session) {
                Criteria criteria = session.createCriteria(Route.class, "r");
                criteria.addOrder(Order.asc("r.number"));
                return criteria;
            }
        };
    }


    public static ExampleQuery selectAllForRailwayStation() {
        return new ExampleQuery() {
            @Override
            public Query getQuery(Session session) {
                return session.createQuery("select r from Route r, BusStop b where (r.start = b.id or r.destination = b.id) and b.description = 'Railway Station'");
            }

            @Override
            public String getNamedQueryName() {
                return Route.SELECT_ALL_FOR_RAILWAY_STATION;
            }

            @Override
            public Criteria getCriteria(Session session) {
            	Criteria criteria = session.createCriteria(Route.class, "r")
            	.createAlias("start", "s")
            	.createAlias("destination", "d")
            	.add(Restrictions.or(Restrictions.eq("s.description", "Railway Station"), Restrictions.eq("d.description", "Railway Station")));
                return criteria;
            }
        };
    }

    public static ExampleQuery cumulativeFrequencyByOkTravel() {
        return new ExampleQuery() {
            @Override
            public Query getQuery(Session session) {
                return session.createQuery("select sum(r.frequency)/r.operators.size from Route r, Operator o inner join r.operators operators inner join o.routes routes where operators.name = 'OK Travel' and routes.number = r.number");
            }

            @Override
            public String getNamedQueryName() {
                return Route.CUMULATIVE_FREQUENCY_BY_OK_TRAVEL;
            }

            @Override
            public Criteria getCriteria(Session session) {
            	Criteria criteria = session.createCriteria(Route.class, "r")
            	.createAlias("operators", "op")
            	.add(Restrictions.eq("op.name", "OK Travel"))
            	.setProjection(Projections.sum("frequency"));
            	
            	DetachedCriteria getOperatorSize = DetachedCriteria.forClass(Route.class)
            			.createAlias("operators", "op")
            			.setProjection(Projections.count("op.name"));
            	
            	//Divide to get actual frequency for routes with operators.size > 1
            	
            	
            	return criteria;
            }
        };
    }


}
