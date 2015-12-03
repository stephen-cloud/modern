package com.aciworldwide.modern.web.rest;

import com.aciworldwide.modern.Application;
import com.aciworldwide.modern.domain.Warehouse;
import com.aciworldwide.modern.repository.WarehouseRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the WarehouseResource REST controller.
 *
 * @see WarehouseResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class WarehouseResourceIntTest {

    private static final String DEFAULT_LOCATION = "AAAAA";
    private static final String UPDATED_LOCATION = "BBBBB";

    @Inject
    private WarehouseRepository warehouseRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWarehouseMockMvc;

    private Warehouse warehouse;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WarehouseResource warehouseResource = new WarehouseResource();
        ReflectionTestUtils.setField(warehouseResource, "warehouseRepository", warehouseRepository);
        this.restWarehouseMockMvc = MockMvcBuilders.standaloneSetup(warehouseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        warehouse = new Warehouse();
        warehouse.setLocation(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    public void createWarehouse() throws Exception {
        int databaseSizeBeforeCreate = warehouseRepository.findAll().size();

        // Create the Warehouse

        restWarehouseMockMvc.perform(post("/api/warehouses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(warehouse)))
                .andExpect(status().isCreated());

        // Validate the Warehouse in the database
        List<Warehouse> warehouses = warehouseRepository.findAll();
        assertThat(warehouses).hasSize(databaseSizeBeforeCreate + 1);
        Warehouse testWarehouse = warehouses.get(warehouses.size() - 1);
        assertThat(testWarehouse.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = warehouseRepository.findAll().size();
        // set the field null
        warehouse.setLocation(null);

        // Create the Warehouse, which fails.

        restWarehouseMockMvc.perform(post("/api/warehouses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(warehouse)))
                .andExpect(status().isBadRequest());

        List<Warehouse> warehouses = warehouseRepository.findAll();
        assertThat(warehouses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWarehouses() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouses
        restWarehouseMockMvc.perform(get("/api/warehouses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(warehouse.getId().intValue())))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())));
    }

    @Test
    @Transactional
    public void getWarehouse() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get the warehouse
        restWarehouseMockMvc.perform(get("/api/warehouses/{id}", warehouse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(warehouse.getId().intValue()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWarehouse() throws Exception {
        // Get the warehouse
        restWarehouseMockMvc.perform(get("/api/warehouses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWarehouse() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

		int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();

        // Update the warehouse
        warehouse.setLocation(UPDATED_LOCATION);

        restWarehouseMockMvc.perform(put("/api/warehouses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(warehouse)))
                .andExpect(status().isOk());

        // Validate the Warehouse in the database
        List<Warehouse> warehouses = warehouseRepository.findAll();
        assertThat(warehouses).hasSize(databaseSizeBeforeUpdate);
        Warehouse testWarehouse = warehouses.get(warehouses.size() - 1);
        assertThat(testWarehouse.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void deleteWarehouse() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

		int databaseSizeBeforeDelete = warehouseRepository.findAll().size();

        // Get the warehouse
        restWarehouseMockMvc.perform(delete("/api/warehouses/{id}", warehouse.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Warehouse> warehouses = warehouseRepository.findAll();
        assertThat(warehouses).hasSize(databaseSizeBeforeDelete - 1);
    }
}
