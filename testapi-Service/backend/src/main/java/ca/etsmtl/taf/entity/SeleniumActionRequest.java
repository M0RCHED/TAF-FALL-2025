package ca.etsmtl.taf.entity;

import lombok.Getter;
import lombok.Setter;

// SeleniumActionRequest

@Getter
@Setter
public class SeleniumActionRequest {
    int action_id;
    int action_type_id;
    String action_type_name;
    String object;
    String input;
    String target;
}
