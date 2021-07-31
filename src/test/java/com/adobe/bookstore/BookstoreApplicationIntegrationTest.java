package com.adobe.bookstore;

import com.adobe.bookstore.controller.OrderController;
import com.adobe.bookstore.dto.OrderProductDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {BookstoreApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookstoreApplicationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private OrderController orderController;

    @Test
    @Order(1)
    public void contextLoads() {
        Assertions
                .assertThat(orderController)
                .isNotNull();
    }

    @Test
    @Order(2)
    public void givenGetOrdersApiCall_whenNoOrders_thenSizeMatchAndListContainsNoProductNames() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:" + port + "/api/orders?type=json", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
        });

        String orders = responseEntity.getBody();
        assertEquals("[]", orders);
    }

    @Test
    @Order(3)
    public void givenGetOrdersApiCallCSV_whenExistingOrders_thenSizeMatchAndListContainsNoProductNames() {
        String ordersList = "Identifier,Status,Order Identifier -  Quantity1,PENDING,\"[d02b58ae-8731-451c-9acb-1941adf88501-1, 58716995-b335-4bb0-89c1-3503bc003118-1]\"";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<OrderProductDto> orderProducts = getCorrectOrderProducts();
        HttpEntity<List<OrderProductDto>> requestEntity = new HttpEntity<>(orderProducts, headers);
        restTemplate.postForEntity("http://localhost:" + port + "/api/orders", requestEntity, String.class);


        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:" + port + "/api/orders?type=csv", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
        });

        String orders = responseEntity.getBody();
        assertEquals(ordersList.replace("\n", "").replace("\r", ""), orders.replace("\n", "").replace("\r", ""));
    }

    @Test
    @Order(4)
    public void givenGetOrdersApiCallJSON_whenExistingOrders_thenSizeMatchAndListContainsNoProductNames() {
        String ordersList = "[{\"orderProducts\":[{\"quantity\":1,\"product\":{\"id\":\"d02b58ae-8731-451c-9acb-1941adf88501\",\"name\":\"ullamco do voluptate cillum amet\",\"quantity\":2}},{\"quantity\":1,\"product\":{\"id\":\"58716995-b335-4bb0-89c1-3503bc003118\",\"name\":\"nulla qui proident consectetur occaecat\",\"quantity\":3}}],\"id\":1,\"status\":\"DONE\"},{\"orderProducts\":[{\"quantity\":1,\"product\":{\"id\":\"d02b58ae-8731-451c-9acb-1941adf88501\",\"name\":\"ullamco do voluptate cillum amet\",\"quantity\":2}},{\"quantity\":1,\"product\":{\"id\":\"58716995-b335-4bb0-89c1-3503bc003118\",\"name\":\"nulla qui proident consectetur occaecat\",\"quantity\":3}}],\"id\":2,\"status\":\"DONE\"}]";
        String pendingOrdersList = "[{\"orderProducts\":[{\"quantity\":1,\"product\":{\"id\":\"d02b58ae-8731-451c-9acb-1941adf88501\",\"name\":\"ullamco do voluptate cillum amet\",\"quantity\":2}},{\"quantity\":1,\"product\":{\"id\":\"58716995-b335-4bb0-89c1-3503bc003118\",\"name\":\"nulla qui proident consectetur occaecat\",\"quantity\":3}}],\"id\":1,\"status\":\"DONE\"},{\"orderProducts\":[{\"quantity\":1,\"product\":{\"id\":\"d02b58ae-8731-451c-9acb-1941adf88501\",\"name\":\"ullamco do voluptate cillum amet\",\"quantity\":2}},{\"quantity\":1,\"product\":{\"id\":\"58716995-b335-4bb0-89c1-3503bc003118\",\"name\":\"nulla qui proident consectetur occaecat\",\"quantity\":3}}],\"id\":2,\"status\":\"PENDING\"}]";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<OrderProductDto> orderProducts = getCorrectOrderProducts();
        HttpEntity<List<OrderProductDto>> requestEntity = new HttpEntity<>(orderProducts, headers);
        restTemplate.postForEntity("http://localhost:" + port + "/api/orders", requestEntity, String.class);

        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:" + port + "/api/orders?type=json", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
        });

        String orders = responseEntity.getBody();
        assertTrue(orders.equals(ordersList) || orders.equals(pendingOrdersList));
    }

    @Test
    @Order(5)
    public void givenPostOrder_whenProductsDontExist_thenResponseDoesntExist() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<OrderProductDto> orderProducts = new ArrayList<>();
        orderProducts.add(new OrderProductDto("1", 1));
        orderProducts.add(new OrderProductDto("2", 1));

        HttpEntity<List<OrderProductDto>> requestEntity = new HttpEntity<>(orderProducts, headers);


        final ResponseEntity<String> postResponse = restTemplate.postForEntity("http://localhost:" + port + "/api/orders", requestEntity, String.class);
        String response = postResponse.getBody();
        Assertions
                .assertThat(postResponse.getStatusCode())
                .isEqualByComparingTo(HttpStatus.NOT_FOUND);

        assertEquals("{\"message\":\"Product not found\"}", response);
    }

    @Test
    @Order(6)
    public void givenPostOrder_whenProductsWithoutStock_thenResponseOutOfStock() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<OrderProductDto> orderProducts = new ArrayList<>();
        orderProducts.add(new OrderProductDto("d02b58ae-8731-451c-9acb-1941adf88501", 100));
        orderProducts.add(new OrderProductDto("d02b58ae-8731-451c-9acb-1941adf88501", 1));

        HttpEntity<List<OrderProductDto>> requestEntity = new HttpEntity<>(orderProducts, headers);

        final ResponseEntity<String> postResponse = restTemplate.postForEntity("http://localhost:" + port + "/api/orders", requestEntity, String.class);
        String response = postResponse.getBody();
        Assertions
                .assertThat(postResponse.getStatusCode())
                .isEqualByComparingTo(HttpStatus.NOT_FOUND);

        assertEquals("{\"message\":\"Some product is out of stock\"}", response);
    }

    @Test
    @Order(7)
    public void givenPostOrder_whenProductsExistsAndHaveStock_thenResponseOrderId() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<OrderProductDto> orderProducts = getCorrectOrderProducts();

        HttpEntity<List<OrderProductDto>> requestEntity = new HttpEntity<>(orderProducts, headers);

        final ResponseEntity<String> postResponse = restTemplate.postForEntity("http://localhost:" + port + "/api/orders", requestEntity, String.class);
        String response = postResponse.getBody();
        Assertions
                .assertThat(postResponse.getStatusCode())
                .isEqualByComparingTo(HttpStatus.CREATED);

        assertEquals("3", response);
    }

    List<OrderProductDto> getCorrectOrderProducts() {
        List<OrderProductDto> orderProducts = new ArrayList<>();
        orderProducts.add(new OrderProductDto("d02b58ae-8731-451c-9acb-1941adf88501", 1));
        orderProducts.add(new OrderProductDto("58716995-b335-4bb0-89c1-3503bc003118", 1));

        return orderProducts;
    }

}