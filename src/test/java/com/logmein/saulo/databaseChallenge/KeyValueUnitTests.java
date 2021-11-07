package com.logmein.saulo.databaseChallenge;

import com.logmein.saulo.databaseChallenge.server.entity.KeyValueEntity;
import com.logmein.saulo.databaseChallenge.server.repository.KeyValueRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {DatabaseChallengeApplication.class},
        loader = AnnotationConfigContextLoader.class
)
@Transactional
public class KeyValueUnitTests {

    @Resource
    private KeyValueRepository repository;

    @Test
    public void givenKeyValue_whenPut_ThenGetOK()
    {
        KeyValueEntity entity = new KeyValueEntity("1", "value of key 1");

        repository.save(entity);

        KeyValueEntity entityDb = (KeyValueEntity) repository.findById(entity.getKey()).get();
        Assert.assertEquals(entity.getKey(),entityDb.getKey());
    }
}
