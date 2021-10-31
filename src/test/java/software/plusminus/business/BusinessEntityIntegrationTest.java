package software.plusminus.business;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import software.plusminus.authentication.AuthenticationParameters;
import software.plusminus.check.util.JsonUtils;
import software.plusminus.jwt.service.JwtGenerator;
import software.plusminus.tenant.service.TenantService;

import java.util.Collections;
import java.util.stream.Stream;
import javax.persistence.EntityManager;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BusinessEntityIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private EntityManager entityManager;

    @SpyBean
    private TenantService tenantService;

    private TestEntity entity1;
    private TestEntity entity2;
    private TestEntity entity3;
    private TestEntity entityWithUnknownTenant;
    private TestEntity entityWithNullTenant;
    private TestEntity deletedEntity;

    @Before
    public void before() {
        entity1 = readTestEntity();
        entity1.setId(null);
        entity1.setTenant("localhost");

        entity2 = readTestEntity();
        entity2.setId(null);
        entity2.setTenant("localhost");

        entityWithUnknownTenant = readTestEntity();
        entityWithUnknownTenant.setId(null);
        entityWithUnknownTenant.setTenant("Unknown tenant");

        entityWithNullTenant = readTestEntity();
        entityWithNullTenant.setId(null);
        entityWithNullTenant.setTenant(null);

        deletedEntity = readTestEntity();
        deletedEntity.setId(null);
        deletedEntity.setDeleted(true);

        entity3 = readTestEntity();
        entity3.setId(null);
        entity3.setTenant("localhost");

        doReturn("localhost", "localhost", "Unknown tenant", null, null, "Some tenant", "localhost")
                .when(tenantService).currentTenant();
        Stream.of(entity1, entity2, entityWithUnknownTenant, entityWithNullTenant, deletedEntity, entity3)
                .forEach(entityManager::persist);
        doCallRealMethod().when(tenantService).currentTenant();
    }

    @Test
    public void filteredByTest() throws Exception {
        mvc.perform(get("/test?page=0&size=100")
                .header("Authorization", "Bearer " + generateToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", Is.is(3)))
                .andExpect(jsonPath("$.content[0].id", Is.is(1)))
                .andExpect(jsonPath("$.content[1].id", Is.is(2)))
                .andExpect(jsonPath("$.content[2].id", Is.is(6)));
    }

    private TestEntity readTestEntity() {
        return JsonUtils.fromJson("/json/test-entity.json", TestEntity.class);
    }

    private String generateToken() {
        AuthenticationParameters parameters = AuthenticationParameters.builder()
                .otherParameters(Collections.singletonMap("tenant", "localhost"))
                .build();
        return jwtGenerator.generateAccessToken(parameters);
    }

}
