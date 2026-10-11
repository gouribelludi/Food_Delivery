package com.quickcrave.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateFoodRequestJsonTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void readsCanonicalBooleanProperties() throws Exception {
        CreateFoodRequest request = objectMapper.readValue(
                """
                        {"vegetarian":true,"seasonal":false}
                        """,
                CreateFoodRequest.class
        );

        assertTrue(request.getVegetarian());
        assertFalse(request.getSeasonal());
    }

    @Test
    void acceptsLegacyMisspelledBooleanProperties() throws Exception {
        CreateFoodRequest request = objectMapper.readValue(
                """
                        {"vegetarin":false,"seasional":true}
                        """,
                CreateFoodRequest.class
        );

        assertFalse(request.getVegetarian());
        assertTrue(request.getSeasonal());
    }

    @Test
    void doesNotFailWhenBooleanPropertiesAreMissingOrNull() throws Exception {
        CreateFoodRequest missingProperties = objectMapper.readValue(
                "{}",
                CreateFoodRequest.class
        );
        CreateFoodRequest nullProperties = objectMapper.readValue(
                """
                        {"vegetarian":null,"seasonal":null}
                        """,
                CreateFoodRequest.class
        );

        assertFalse(missingProperties.getVegetarian());
        assertFalse(missingProperties.getSeasonal());
        assertNull(nullProperties.getVegetarian());
        assertNull(nullProperties.getSeasonal());
    }

    @Test
    void ignoresIngredientStockWhenDeserializingIngredientReferences() throws Exception {
        ObjectMapper strictObjectMapper = objectMapper.copy()
                .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);

        CreateFoodRequest request = strictObjectMapper.readValue(
                """
                        {
                          "name":"Test menu item",
                          "description":"Temporary form validation item",
                          "price":100,
                          "category":{"id":153},
                          "vegetarian":true,
                          "seasonal":false,
                          "ingredients":[{"id":53,"inStock":null}],
                          "images":["https://example.com/test-menu-item.jpg"]
                        }
                        """,
                CreateFoodRequest.class
        );

        assertEquals(53L, request.getIngredients().get(0).getId());
    }

    @Test
    void skipsNullIngredientsWhenStrictNullDeserializationIsEnabled() throws Exception {
        ObjectMapper strictObjectMapper = objectMapper.copy()
                .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);

        CreateFoodRequest request = strictObjectMapper.readValue(
                """
                        {"ingredients":null}
                        """,
                CreateFoodRequest.class
        );

        assertNull(request.getIngredients());
    }
}
