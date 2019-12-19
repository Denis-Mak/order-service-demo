package com.denismak.orderservicedemo.rest;

import com.denismak.orderservicedemo.bean.Order;
import com.denismak.orderservicedemo.repository.OrderRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.QueryTimeoutException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    public void getOrders_success() throws Exception {
        List<Order> orders = Collections.singletonList(new Order("a", "b"));
        given(orderRepository.findAll()).willReturn(orders);

        mvc.perform(get("/api/orders").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].product", is("a")))
                .andExpect(jsonPath("$[0].customer", is("b")));
    }

    @Test
    public void getOrders_emptyResult() throws Exception {
        given(orderRepository.findAll()).willReturn(Collections.emptyList());

        mvc.perform(get("/api/orders").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(is(0))));
    }

    @Test
    public void getOrders_error() throws Exception {
        given(orderRepository.findAll()).willThrow(new QueryTimeoutException());

        MvcResult result = mvc.perform(get("/api/orders").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();
        assertThat(result.getResolvedException()).isNotNull();
        assertThat(result.getResolvedException().getMessage()).isEqualTo("Something bad happened");
    }

    @Test
    public void findOrderById_success() throws Exception {
        Order order = new Order("a", "b");
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        mvc.perform(get("/api/orders/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.product", is("a")))
                .andExpect(jsonPath("$.customer", is("b")));
    }

    @Test
    public void findOrderById_notFound() throws Exception {
        given(orderRepository.findById(anyLong())).willReturn(Optional.empty());

        mvc.perform(get("/api/orders/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findOrderById_invalidId() throws Exception {
        mvc.perform(get("/api/orders/r").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_success() throws Exception {
        Order order = new Order("a", "b");
        given(orderRepository.save(order)).willReturn(order);

        mvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"product\": \"a\", \"customer\": \"b\"}"))

                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.product", is("a")))
                .andExpect(jsonPath("$.customer", is("b")));
    }

    @Test
    public void create_fails() throws Exception {
        Order order = new Order("a", "b");
        given(orderRepository.save(order)).willThrow(new QueryTimeoutException());

        mvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"product\": \"a\", \"customer\": \"b\"}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void create_invalidJson() throws Exception {
        mvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"customer\": \"b\"}"))
                .andExpect(status().isBadRequest());
    }
}
