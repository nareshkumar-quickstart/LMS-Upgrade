package com.softech.vu360.lms.repositories;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.data.jpa.domain.Specification;

public class RepositorySpecification<T> implements Specification<T> {

	@Inject
	private SearchCriteria criteria;
	
	private List<Order> ordering = new LinkedList<Order>();

	public RepositorySpecification(SearchCriteria searchCriteria) {
		this.criteria = searchCriteria;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
			CriteriaBuilder builder) {
		if (criteria.getOperation().equalsIgnoreCase(">")) {
			return builder.greaterThanOrEqualTo(root.<String> get(criteria
					.getKey()), criteria.getValue().toString());
		} else if (criteria.getOperation().equalsIgnoreCase("<")) {
			return builder.lessThanOrEqualTo(root.<String> get(criteria
					.getKey()), criteria.getValue().toString());
		} else if (criteria.getOperation().equalsIgnoreCase("=")) {
			if (root.get(criteria.getKey()).getJavaType() == String.class) {
				return builder.equal(builder.upper(root.<String> get(criteria.getKey())), criteria.getValue().toString().toUpperCase());
//						like(root.<String> get(criteria.getKey()), "%"+ criteria.getValue() + "%");
			} else {
				return builder.equal(root.get(criteria.getKey()),
						criteria.getValue());
			}
		} else if (criteria.getOperation().equalsIgnoreCase("!IN")) {
			return builder.not(root.get(criteria.getKey()).in(
					criteria.getArray()));
		} else if (criteria.getOperation().equalsIgnoreCase(":IC")) {
			return builder.like(
					builder.upper(root.<String> get(criteria.getKey())), "%"
							+ criteria.getValue().toString().toUpperCase()
							+ "%");
		} else if (criteria.getOperation().equalsIgnoreCase("IN")) {
			Expression<String> exp = root.get(criteria.getKey());
			return exp.in(criteria.getArray());
		} else if (criteria.getOperation().equalsIgnoreCase("!=")) {
			return builder.notEqual(root.get(criteria.getKey()),
					criteria.getValue());
		} else if (criteria.getOperation().equalsIgnoreCase("!Null")) {
			Expression<T> exp = root.get(criteria.getKey());
			return builder.isNotNull(exp);
		} else if (criteria.getOperation().equalsIgnoreCase("BTW")) {
			Expression exp = root.get(criteria.getKey());
			Expression minRange = builder.literal(criteria.getBetween_value1());
			Expression maxRange = builder.literal(criteria.getBetween_value2());
			return builder.between(exp, minRange, maxRange);
		} 
		else if (criteria.getOperation().equalsIgnoreCase("::")) {
			String Key = criteria.getKey();
			Expression<T> exp = createJoins(root, Key);
			Object value = criteria.getValue();
			return builder.equal(exp, criteria.getValue());
		} else if (criteria.getOperation().equalsIgnoreCase("::IC")) {
			String Key = criteria.getKey();
			Expression<T> exp = createJoins(root, Key);
			return builder.like(builder.upper((Expression<String>) exp), "%"
					+ criteria.getValue().toString().toUpperCase() + "%");
		} else if (criteria.getOperation().equalsIgnoreCase("::!=")) {
			String Key = criteria.getKey();
			Expression<T> exp = createJoins(root, Key);
			return builder.notEqual(exp, criteria.getValue());
		} else if (criteria.getOperation().equalsIgnoreCase("::IN")) {
			String Key = criteria.getKey();
			Expression<T> exp = createJoins(root, Key);
			return exp.in(criteria.getArray());
		} else if (criteria.getOperation().equalsIgnoreCase("::!IN")) {
			String Key = criteria.getKey();
			Expression<T> exp = createJoins(root, Key);
			return builder.not(exp.in(criteria.getArray()));
		} else if (criteria.getOperation().equalsIgnoreCase("::>")) {
			String Key = criteria.getKey();
			Expression exp = createJoins(root, Key);
			return builder.greaterThanOrEqualTo(exp, criteria.getValue()
					.toString());
		} else if (criteria.getOperation().equalsIgnoreCase("::<")) {
			String Key = criteria.getKey();
			Expression exp = createJoins(root, Key);
			return builder.lessThanOrEqualTo(exp, criteria.getValue()
					.toString());
		} else if (criteria.getOperation().equalsIgnoreCase("::BTW")) {
			String Key = criteria.getKey();
			Expression exp = createJoins(root, Key);
			Expression minRange = builder.literal(criteria.getBetween_value1());
			Expression maxRange = builder.literal(criteria.getBetween_value2());
			return builder.between(exp, minRange, maxRange);
		} else if (criteria.getOperation().equalsIgnoreCase("::!Null")) {
			String Key = criteria.getKey();
			Expression<T> exp = createJoins(root, Key);
			return builder.isNotNull(exp);
		} else if (criteria.getOperation().equalsIgnoreCase("DISTINCT")) {
			query.distinct(true);
		}
		return null;
	}

	// Added By Marium Saud
	// The method will create join as per number of roots.
	public Expression<T> createJoins(Root<T> root, String key) {

		Expression<T> expression = null;
		String[] Join_Roots = key.split("_");
		int No_of_Joins = Join_Roots.length;
		if (No_of_Joins == 1){
			Join<T, T> join_1 = root.join(Join_Roots[0].toString(),JoinType.LEFT);
			expression = join_1;
		}
		else if (No_of_Joins == 2) {
			Join<T, T> join_1 = root.join(Join_Roots[0].toString(),JoinType.LEFT);
			expression = join_1.get(Join_Roots[1].toString());
		} else if (No_of_Joins == 3) {
			Join<T, T> join_1 = root.join(Join_Roots[0].toString(),JoinType.LEFT);
			Join<T, T> join_2 = join_1.join(Join_Roots[1].toString(),JoinType.LEFT);
			expression = join_2.get(Join_Roots[2].toString());
		} else if (No_of_Joins == 4) {
			Join<T, T> join_1 = root.join(Join_Roots[0].toString(),JoinType.LEFT);
			Join<T, T> join_2 = join_1.join(Join_Roots[1].toString(),JoinType.LEFT);
			Join<T, T> join_3 = join_2.join(Join_Roots[2].toString(),JoinType.LEFT);
			expression = join_3.get(Join_Roots[3].toString());
		}

		return expression;
	}

}
