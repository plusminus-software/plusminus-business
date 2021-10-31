package software.plusminus.business;

import org.springframework.context.annotation.Profile;
import software.plusminus.business.repository.BusinessRepository;

@Profile("test")
public interface TestRepository extends BusinessRepository<TestEntity> {
}