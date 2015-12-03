package com.aciworldwide.modern.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aciworldwide.modern.domain.Warehouse;
import com.aciworldwide.modern.repository.WarehouseRepository;
import com.aciworldwide.modern.web.rest.util.HeaderUtil;
import com.aciworldwide.modern.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Warehouse.
 */
@RestController
@RequestMapping("/api")
public class WarehouseResource {

    private final Logger log = LoggerFactory.getLogger(WarehouseResource.class);

    @Inject
    private WarehouseRepository warehouseRepository;

    /**
     * POST  /warehouses -> Create a new warehouse.
     */
    @RequestMapping(value = "/warehouses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Warehouse> createWarehouse(@Valid @RequestBody Warehouse warehouse) throws URISyntaxException {
        log.debug("REST request to save Warehouse : {}", warehouse);
        if (warehouse.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new warehouse cannot already have an ID").body(null);
        }
        Warehouse result = warehouseRepository.save(warehouse);
        return ResponseEntity.created(new URI("/api/warehouses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("warehouse", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /warehouses -> Updates an existing warehouse.
     */
    @RequestMapping(value = "/warehouses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Warehouse> updateWarehouse(@Valid @RequestBody Warehouse warehouse) throws URISyntaxException {
        log.debug("REST request to update Warehouse : {}", warehouse);
        if (warehouse.getId() == null) {
            return createWarehouse(warehouse);
        }
        Warehouse result = warehouseRepository.save(warehouse);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("warehouse", warehouse.getId().toString()))
            .body(result);
    }

    /**
     * GET  /warehouses -> get all the warehouses.
     */
    @RequestMapping(value = "/warehouses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Warehouse>> getAllWarehouses(Pageable pageable)
        throws URISyntaxException {
        Page<Warehouse> page = warehouseRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/warehouses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /warehouses/:id -> get the "id" warehouse.
     */
    @RequestMapping(value = "/warehouses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Warehouse> getWarehouse(@PathVariable Long id) {
        log.debug("REST request to get Warehouse : {}", id);
        return Optional.ofNullable(warehouseRepository.findOneWithEagerRelationships(id))
            .map(warehouse -> new ResponseEntity<>(
                warehouse,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /warehouses/:id -> delete the "id" warehouse.
     */
    @RequestMapping(value = "/warehouses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        log.debug("REST request to delete Warehouse : {}", id);
        warehouseRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("warehouse", id.toString())).build();
    }
}
