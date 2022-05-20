package web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PetDTO extends BaseDTO<Long> {
    private String name, breed;
    private Integer age;

}
