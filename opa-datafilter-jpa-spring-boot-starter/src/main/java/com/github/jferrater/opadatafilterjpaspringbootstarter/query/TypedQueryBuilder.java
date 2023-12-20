package com.github.jferrater.opadatafilterjpaspringbootstarter.query;

import jakarta.persistence.criteria.*;
import opa.datafilter.core.ast.db.query.PredicateConverter;
import opa.datafilter.core.ast.db.query.elements.SqlPredicate;
import opa.datafilter.core.ast.db.query.exception.PartialEvauationException;
import opa.datafilter.core.ast.db.query.model.response.OpaCompilerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

/**
 * @author joffryferrater
 * @param <T> The class type
 */
public class TypedQueryBuilder<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TypedQueryBuilder.class);

    private final OpaCompilerResponse opaCompilerResponse;
    private final EntityManager entityManager;

    public TypedQueryBuilder(OpaCompilerResponse opaCompilerResponse, EntityManager entityManager) {
        this.opaCompilerResponse = opaCompilerResponse;
        this.entityManager = entityManager;
    }

    public <S extends T>TypedQuery<S> getTypedQuery(Class<S> domainClass, Sort sort) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<S> criteriaQuery = criteriaBuilder.createQuery(domainClass);

        Root<S> root = applyPredicates(domainClass, criteriaQuery);
        criteriaQuery.select(root);
        if(sort.isSorted()) {
            criteriaQuery.orderBy(toOrders(sort, root, criteriaBuilder));
        }
        return entityManager.createQuery(criteriaQuery);
    }

    private <U extends T> Root<U> applyPredicates(Class<U> domainClass, CriteriaQuery<U> criteriaQuery) {
        Assert.notNull(domainClass, "Domain class must not be null!");
        Assert.notNull(criteriaQuery, "CriteriaQuery must not be null!");

        Root<U> root = criteriaQuery.from(domainClass);
        Predicate predicate = orPredicates(root);
        if(predicate == null) {
            throw new PartialEvauationException("The OPA partial evaluation returns empty result. User may not have permissions!");
        }
        criteriaQuery.where(predicate);
        return root;
    }

    <S extends T>Predicate orPredicates(Root<S> root) {
        List<List<opa.datafilter.core.ast.db.query.model.response.Predicate>> opaPredicates = opaCompilerResponse.getResult().getQueries();
        Predicate[] predicates = opaPredicates.stream()
                .map(opaPredicateObjList -> andPredicates(opaPredicateObjList, root))
                .toList().toArray(Predicate[]::new);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        return criteriaBuilder.or(predicates);
    }

    <S extends T>Predicate andPredicates(List<opa.datafilter.core.ast.db.query.model.response.Predicate> opaPredicateObjList, Root<S> root){
        List<Predicate> predicates = getPredicatesFromOpaResponse(opaPredicateObjList, root);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }

    <U extends T> List<Predicate> getPredicatesFromOpaResponse(
            List<opa.datafilter.core.ast.db.query.model.response.Predicate> opaPredicateObjList,
            Root<U> root) {
        return opaPredicateObjList.stream()
                .map(opaPredicate -> new PredicateConverter(opaPredicate).astToSqlPredicate())
                .map(sqlPredicate -> createPredicate(sqlPredicate, root))
                .collect(toList());
    }

    <U extends T> Predicate createPredicate(SqlPredicate sqlPredicate, Root<U> root) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        String operator = sqlPredicate.getOperator();
        String leftExpression = sqlPredicate.getLeftExpression();
        String attributeName = leftExpression.split("\\.")[1];
        Object rightExpression = sqlPredicate.getRightExpression();
        Predicate predicate = null;
        switch (operator) {
            case "=":
                predicate = equalPredicateFromExpression(attributeName, rightExpression, root);
                break;
            case "<":
                predicate = getLTPredicateFromExpression(attributeName, rightExpression, root);
                break;
            case "<=":
                predicate = getLTEPredicateFromExpression(attributeName, rightExpression, root);
                break;
            case ">":
                predicate = getGTPredicateFromExpression(attributeName, rightExpression, root);
                break;
            case ">=":
                predicate = getGTEPredicateFromExpression(attributeName, rightExpression, root);
                break;
            default:
                LOGGER.warn("Criteria equivalent of SQL operator '{}' is not yet supported", operator);
        }
        return predicate;
    }

    <U extends T> Predicate equalPredicateFromExpression(String attributeName, Object rightExpression, Root<U> root) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Predicate predicate = null;
        if(rightExpression.getClass().isAssignableFrom(Long.class)) {
            predicate = criteriaBuilder.lessThan(root.get(attributeName).as(Long.class), (Long) rightExpression);
        } else if(rightExpression.getClass().isAssignableFrom(Integer.class)) {
            predicate = criteriaBuilder.lessThan(root.get(attributeName).as(Integer.class), (Integer) rightExpression);
        } else if(rightExpression.getClass().isAssignableFrom(Double.class)) {
            predicate = criteriaBuilder.lessThan(root.get(attributeName).as(Double.class), (Double) rightExpression);
        } else if(rightExpression.getClass().isAssignableFrom(Boolean.class)) {
            predicate = criteriaBuilder.equal(root.get(attributeName).as(Boolean.class), (Boolean) rightExpression);
        } else if (rightExpression.getClass().isAssignableFrom(String.class)) {
            predicate = criteriaBuilder.equal(root.get(attributeName).as(String.class), (String) rightExpression);
        } else {
            LOGGER.warn("Unknown data type: {}", rightExpression);
        }
        return predicate;
    }

    <U extends T> Predicate getLTPredicateFromExpression(String attributeName, Object rightExpression, Root<U> root) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Predicate predicate = null;
        if(rightExpression.getClass().isAssignableFrom(Long.class)) {
            predicate = criteriaBuilder.lessThan(root.get(attributeName).as(Long.class), (Long) rightExpression);
        } else if(rightExpression.getClass().isAssignableFrom(Integer.class)) {
            predicate = criteriaBuilder.lessThan(root.get(attributeName).as(Integer.class), (Integer) rightExpression);
        } else if(rightExpression.getClass().isAssignableFrom(Double.class)) {
            predicate = criteriaBuilder.lessThan(root.get(attributeName).as(Double.class), (Double) rightExpression);
        } else {
            LOGGER.warn("Unknown data type: {}", rightExpression);
        }
        return predicate;
    }

    <U extends T> Predicate getLTEPredicateFromExpression(String attributeName, Object rightExpression, Root<U> root) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Predicate predicate = null;
        if(rightExpression.getClass().isAssignableFrom(Long.class)) {
            predicate = criteriaBuilder.lessThanOrEqualTo(root.get(attributeName).as(Long.class), (Long) rightExpression);
        } else if(rightExpression.getClass().isAssignableFrom(Integer.class)) {
            predicate = criteriaBuilder.lessThanOrEqualTo(root.get(attributeName).as(Integer.class), (Integer) rightExpression);
        } else if(rightExpression.getClass().isAssignableFrom(Double.class)) {
            predicate = criteriaBuilder.lessThanOrEqualTo(root.get(attributeName).as(Double.class), (Double) rightExpression);
        } else {
            LOGGER.warn("Unknown data type: {}", rightExpression);
        }
        return predicate;
    }

    <U extends T> Predicate getGTPredicateFromExpression(String attributeName, Object rightExpression, Root<U> root) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Predicate predicate = null;
        if(rightExpression.getClass().isAssignableFrom(Long.class)) {
            predicate = criteriaBuilder.greaterThan(root.get(attributeName).as(Long.class), (Long) rightExpression);
        } else if(rightExpression.getClass().isAssignableFrom(Integer.class)) {
            predicate = criteriaBuilder.greaterThan(root.get(attributeName).as(Integer.class), (Integer) rightExpression);
        } else if(rightExpression.getClass().isAssignableFrom(Double.class)) {
            predicate = criteriaBuilder.greaterThan(root.get(attributeName).as(Double.class), (Double) rightExpression);
        } else {
            LOGGER.warn("Unknown data type: {}", rightExpression);
        }
        return predicate;
    }

    <U extends T> Predicate getGTEPredicateFromExpression(String attributeName, Object rightExpression, Root<U> root) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Predicate predicate = null;
        if(rightExpression.getClass().isAssignableFrom(Long.class)) {
            predicate = criteriaBuilder.greaterThanOrEqualTo(root.get(attributeName).as(Long.class), (Long) rightExpression);
        } else if(rightExpression.getClass().isAssignableFrom(Integer.class)) {
            predicate = criteriaBuilder.greaterThanOrEqualTo(root.get(attributeName).as(Integer.class), (Integer) rightExpression);
        } else if(rightExpression.getClass().isAssignableFrom(Double.class)) {
            predicate = criteriaBuilder.greaterThanOrEqualTo(root.get(attributeName).as(Double.class), (Double) rightExpression);
        } else {
            LOGGER.warn("Unknown data type: {}", rightExpression);
        }
        return predicate;
    }
}
