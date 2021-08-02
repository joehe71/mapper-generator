package mapper;

import request.CreateBookAJAXRequest;
import request.CreateBookRequest;

/**
 * @author joe
 */
public interface BooServiceMapper {
    CreateBookRequest toCreateBookRequest(CreateBookAJAXRequest request);
}
