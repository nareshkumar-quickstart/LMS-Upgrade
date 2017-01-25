package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.criteria.Order;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

public class RepositorySpecificationsBuilder<T> {

	private final List<SearchCriteria> params;
	public String GREATER_THAN_OR_EQUAL_TO = ">";
	public String LESS_THAN_OR_EQUAL_TO = "<";
	public String EQUALS_TO = "=";
	public String NOT_EQUALS_TO = "!=";
	public String NOT_IN = "!IN";
	public String IN = "IN";
	public String LIKE_IGNORE_CASE = ":IC";
	public String NOT_NULL = "!Null";
	public String BETWEEN = "BTW";
	public String ANY_OF_EQUALS = "MM=";
	public String ANY_OF_NOT_EQUALS = "MM!=";
	public String JOIN_EQUALS = "::";
	public String JOIN_NOT_EQUALS = "::!=";
	public String JOIN_LIKE_IGNORE_CASE = "::IC";
	public String JOIN_IN = "::IN";
	public String JOIN_NOT_IN = "::!IN";
	public String JOIN_GREATER_THAN_EQUALS_TO = "::>";
	public String JOIN_LESS_THAN_EQUALS_TO = "::<";
	public String JOIN_BETWEEN = "::BTW";
	public String JOIN_NOT_NULL = "::!Null";
	public String DISTINCT = "DISTINCT";
	public String SORT_BY = "SORT_BY";
	public String STRING_ARRAY_IN="SIN";

	public RepositorySpecificationsBuilder() {
		params = new ArrayList<SearchCriteria>();
	}

	public RepositorySpecificationsBuilder<T> with(String keyAttribute,
			String operation, Object value, String joinType) {
		if (value != null) {
			if (value.toString().trim().length() > 0) {
				params.add(new SearchCriteria(keyAttribute, operation, value,
						joinType));
			}
		}
		return this;

	}

	public RepositorySpecificationsBuilder<T> with(String keyAttribute,
			String operation, Object[] Array, String joinType) {

		if (Array != null) {
			if (Array.length > 0) {
				params.add(new SearchCriteria(keyAttribute, operation,
						Array, joinType));
			}
		}
		return this;
	}

	public RepositorySpecificationsBuilder<T> with(String keyAttribute,
			String operation, Object between_value1, Object between_value2,
			String joinType) {

		if (between_value1 != null && between_value2 != null) {
			params.add(new SearchCriteria(keyAttribute, operation,
					between_value1, between_value2, joinType));
		}
		return this;

	}
	
	public Specification<T> build() {
		if (params.size() == 0) {
			return null;
		}

		List<Specification<T>> specs = new ArrayList<Specification<T>>();
		for (SearchCriteria param : params) {
			specs.add(new RepositorySpecification<T>(param));
		}

		Specification<T> result = specs.get(0);
		for (int i = 1; i < specs.size(); i++) {

			if (params.get(i).getJoinType().equalsIgnoreCase("and"))
				result = Specifications.where(result).and(specs.get(i));
			else if (params.get(i).getJoinType().equalsIgnoreCase("or"))
				result = Specifications.where(result).or(specs.get(i));
		}
		return result;
	}

}
