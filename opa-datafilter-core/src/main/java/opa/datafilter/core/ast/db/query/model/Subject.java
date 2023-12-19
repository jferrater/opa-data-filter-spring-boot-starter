package opa.datafilter.core.ast.db.query.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * @author joffryferrater
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Subject {

    private String user;
    private String jwt;
    private Map<String, String> attributes;

    public Subject() {
    }

    public Subject(String user, String jwt, Map<String, String> attributes) {
        this();
        this.user = user;
        this.jwt = jwt;
        this.attributes = attributes;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
