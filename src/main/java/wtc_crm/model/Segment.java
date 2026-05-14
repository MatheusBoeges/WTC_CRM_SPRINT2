package wtc_crm.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "segments")
public class Segment {
    @Id
    private String id;
    private String name;
    private List<String> customerIds;
}