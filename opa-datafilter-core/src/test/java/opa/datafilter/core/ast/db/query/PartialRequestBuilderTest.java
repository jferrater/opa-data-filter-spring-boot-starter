package opa.datafilter.core.ast.db.query;

import opa.datafilter.core.ast.db.query.model.request.PartialRequest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author joffryferrater
 */
class PartialRequestBuilderTest {

    @Test
    void shouldBuildPartialRequest() {
        PartialRequest result = PartialRequest.builder()
                .query("data.petclinic.authz.allow = true")
                .httpMethod("GET")
                .httpPath(List.of("pets", "fluffy"))
                .input("subject", new UserInfo("alice", "SOMA"))
                .unknowns(Set.of("data.pets"))
                .build();

        assertThat(result, is(notNullValue()));
        assertThat(result.getQuery(), is("data.petclinic.authz.allow = true"));
        assertThat(result.getUnknowns(), hasItem("data.pets"));
        Map<String, Object> input = result.getInput();
        assertThat(input.get("method"), is("GET"));
        List<String> path = (List<String>) input.get("path");
        assertThat(path, hasItems("pets", "fluffy"));
        UserInfo userInfo = (UserInfo) input.get("subject");
        assertThat(userInfo.getUsername(), is("alice"));
        assertThat(userInfo.getLocation(), is("SOMA"));
    }

    private static class UserInfo {
        private String username;
        private String location;

        public UserInfo(String username, String location) {
            this.username = username;
            this.location = location;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}