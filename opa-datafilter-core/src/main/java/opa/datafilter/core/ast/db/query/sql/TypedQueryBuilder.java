package opa.datafilter.core.ast.db.query.sql;

import opa.datafilter.core.ast.db.query.PredicateConverter;
import opa.datafilter.core.ast.db.query.elements.SqlPredicate;
import opa.datafilter.core.ast.db.query.exception.PartialEvauationException;
import opa.datafilter.core.ast.db.query.model.response.OpaCompilerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

    public <S extends T>TypedQuery<S> getTypedQuery(Class<S> domainClass, Sort sort){
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
                .collect(toList()).toArray(Predicate[]::new);
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
        String rightExpression = sqlPredicate.getRightExpression();
        Predicate predicate = null;
        switch (operator) {
            case "=":
                predicate = criteriaBuilder.equal(root.get(attributeName), rightExpression);
                break;
            case "<":
                predicate = criteriaBuilder.lessThan(root.get(attributeName), Integer.valueOf(rightExpression));
                break;
            case ">":
                predicate = criteriaBuilder.greaterThan(root.get(attributeName), Integer.valueOf(rightExpression));
                break;
            default:
                LOGGER.warn("Criteria equivalent of SQL operator '{}' is not yet supported", operator);
        }
        return predicate;
    }
}
