import mapper.BooServiceMapper;
import request.CreateBookAJAXRequest;
import request.CreateBookRequest;

import java.util.Collections;

/**
 * @author joe
 */
public class Main {
    public static void main(String[] args) throws Exception {
        BooServiceMapper mapper = MapperGenerator.generate(BooServiceMapper.class);
        CreateBookAJAXRequest createBookAJAXRequest = new CreateBookAJAXRequest();
        createBookAJAXRequest.bookName = "AA";
        createBookAJAXRequest.description = "AA";
        createBookAJAXRequest.categoryId = "AA";
        createBookAJAXRequest.tagIds = Collections.emptyList();
        createBookAJAXRequest.author = "AA";
        createBookAJAXRequest.imgUrl = "AA";
        createBookAJAXRequest.isbn = "AA";
        createBookAJAXRequest.number = 10;
        CreateBookRequest createBookRequest = mapper.toCreateBookRequest(createBookAJAXRequest);
        System.out.println(createBookRequest);
        Class<? extends BooServiceMapper> mapperClass = mapper.getClass();
        System.out.println(mapperClass);
    }
}
