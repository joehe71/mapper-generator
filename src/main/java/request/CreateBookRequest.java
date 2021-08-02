package request;

import java.util.List;

/**
 * @author joe
 */
public class CreateBookRequest {
    public String bookName;

    public String description;

    public String categoryId;

    public List<String> tagIds;

    public String author;

    public String imgUrl;

    public String isbn;

    public Integer number;

    @Override
    public String toString() {
        return "CreateBookRequest{" +
                "bookName='" + bookName + '\'' +
                ", description='" + description + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", tagIds=" + tagIds +
                ", author='" + author + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", isbn='" + isbn + '\'' +
                ", number=" + number +
                '}';
    }
}
