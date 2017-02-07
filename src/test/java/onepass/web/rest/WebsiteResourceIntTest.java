package onepass.web.rest;

import onepass.OnepassApp;

import onepass.domain.Website;
import onepass.repository.WebsiteRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WebsiteResource REST controller.
 *
 * @see WebsiteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OnepassApp.class)
public class WebsiteResourceIntTest {

    private static final String DEFAULT_DOMAIN = "AAAAAAAAAA";
    private static final String UPDATED_DOMAIN = "BBBBBBBBBB";

    @Inject
    private WebsiteRepository websiteRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restWebsiteMockMvc;

    private Website website;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WebsiteResource websiteResource = new WebsiteResource();
        ReflectionTestUtils.setField(websiteResource, "websiteRepository", websiteRepository);
        this.restWebsiteMockMvc = MockMvcBuilders.standaloneSetup(websiteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Website createEntity(EntityManager em) {
        Website website = new Website()
                .domain(DEFAULT_DOMAIN);
        return website;
    }

    @Before
    public void initTest() {
        website = createEntity(em);
    }

    @Test
    @Transactional
    public void createWebsite() throws Exception {
        int databaseSizeBeforeCreate = websiteRepository.findAll().size();

        // Create the Website

        restWebsiteMockMvc.perform(post("/api/websites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(website)))
            .andExpect(status().isCreated());

        // Validate the Website in the database
        List<Website> websiteList = websiteRepository.findAll();
        assertThat(websiteList).hasSize(databaseSizeBeforeCreate + 1);
        Website testWebsite = websiteList.get(websiteList.size() - 1);
        assertThat(testWebsite.getDomain()).isEqualTo(DEFAULT_DOMAIN);
    }

    @Test
    @Transactional
    public void createWebsiteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = websiteRepository.findAll().size();

        // Create the Website with an existing ID
        Website existingWebsite = new Website();
        existingWebsite.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWebsiteMockMvc.perform(post("/api/websites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingWebsite)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Website> websiteList = websiteRepository.findAll();
        assertThat(websiteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWebsites() throws Exception {
        // Initialize the database
        websiteRepository.saveAndFlush(website);

        // Get all the websiteList
        restWebsiteMockMvc.perform(get("/api/websites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(website.getId().intValue())))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())));
    }

    @Test
    @Transactional
    public void getWebsite() throws Exception {
        // Initialize the database
        websiteRepository.saveAndFlush(website);

        // Get the website
        restWebsiteMockMvc.perform(get("/api/websites/{id}", website.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(website.getId().intValue()))
            .andExpect(jsonPath("$.domain").value(DEFAULT_DOMAIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWebsite() throws Exception {
        // Get the website
        restWebsiteMockMvc.perform(get("/api/websites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWebsite() throws Exception {
        // Initialize the database
        websiteRepository.saveAndFlush(website);
        int databaseSizeBeforeUpdate = websiteRepository.findAll().size();

        // Update the website
        Website updatedWebsite = websiteRepository.findOne(website.getId());
        updatedWebsite
                .domain(UPDATED_DOMAIN);

        restWebsiteMockMvc.perform(put("/api/websites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWebsite)))
            .andExpect(status().isOk());

        // Validate the Website in the database
        List<Website> websiteList = websiteRepository.findAll();
        assertThat(websiteList).hasSize(databaseSizeBeforeUpdate);
        Website testWebsite = websiteList.get(websiteList.size() - 1);
        assertThat(testWebsite.getDomain()).isEqualTo(UPDATED_DOMAIN);
    }

    @Test
    @Transactional
    public void updateNonExistingWebsite() throws Exception {
        int databaseSizeBeforeUpdate = websiteRepository.findAll().size();

        // Create the Website

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWebsiteMockMvc.perform(put("/api/websites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(website)))
            .andExpect(status().isCreated());

        // Validate the Website in the database
        List<Website> websiteList = websiteRepository.findAll();
        assertThat(websiteList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWebsite() throws Exception {
        // Initialize the database
        websiteRepository.saveAndFlush(website);
        int databaseSizeBeforeDelete = websiteRepository.findAll().size();

        // Get the website
        restWebsiteMockMvc.perform(delete("/api/websites/{id}", website.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Website> websiteList = websiteRepository.findAll();
        assertThat(websiteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
