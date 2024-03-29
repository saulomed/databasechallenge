package com.logmein.saulo.databaseChallenge.server.resource;

import com.logmein.saulo.databaseChallenge.server.entity.KeyValueEntity;
import com.logmein.saulo.databaseChallenge.server.repository.KeyValueRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/v1/keyvalue")
@CrossOrigin(origins = "*")
@Api(tags = "KeyValue", value = "API Key Value")
public class KeyValueResource {

    @Autowired
    private KeyValueRepository _repository;

    @ApiOperation(value = "Put key value")
    @RequestMapping(value = "/{key}", method = RequestMethod.PUT)
    public void put(@PathVariable(value = "key") String key, @RequestBody String value)
    {
        KeyValueEntity entity = new KeyValueEntity(key, value);
        _repository.save(entity);
    }

    @ApiOperation(value = "Get value from key")
    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public String get(@PathVariable(value = "key") String key)
    {
        return ((KeyValueEntity)_repository.findById(key).get()).getValue();

    }

    @ApiOperation(value = "Delete key value")
    @RequestMapping(value = "/{key}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value = "key") String key)
    {
        _repository.delete(key);
    }
}
