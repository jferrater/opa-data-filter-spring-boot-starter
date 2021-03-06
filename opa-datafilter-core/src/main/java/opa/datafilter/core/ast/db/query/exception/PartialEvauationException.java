package opa.datafilter.core.ast.db.query.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author joffryferrater
 */
@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "OPA partial evaluation returns empty result")
public class PartialEvauationException extends RuntimeException {

    public PartialEvauationException(String message) {
        super(message);
    }
}
