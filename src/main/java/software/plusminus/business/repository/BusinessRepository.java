package software.plusminus.business.repository;

import org.springframework.data.repository.NoRepositoryBean;
import software.plusminus.business.model.BusinessEntity;
import software.plusminus.crud.repository.CrudRepository;

@NoRepositoryBean
public interface BusinessRepository<T extends BusinessEntity> extends CrudRepository<T, Long> {
}
