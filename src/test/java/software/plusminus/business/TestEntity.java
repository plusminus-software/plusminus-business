package software.plusminus.business;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import software.plusminus.business.model.BusinessEntity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

@Data
@EqualsAndHashCode(of = "", callSuper = true)
@ToString(of = "", callSuper = true)
@Entity
public class TestEntity extends BusinessEntity {

    private String myField;

    @ElementCollection
    private List<String> list = new ArrayList<>();

}