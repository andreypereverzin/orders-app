package com.roche.orders.controller;

import com.roche.orders.data.request.OrderRequestData;
import com.roche.orders.data.response.OrderResponseData;
import com.roche.orders.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;

@RestController
@Api(value = "Orders API")
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation(value = "Create an order")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created order"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping(path = "/orders", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> createOrder(@RequestBody OrderRequestData order) {
        log.debug("Started creating order");

        OrderResponseData orderCreated = orderService.createOrder(order);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{orderId}")
                .buildAndExpand(orderCreated.getOrderId())
                .toUri();

        log.debug("Created order {}", orderCreated);

        return ResponseEntity.created(location).build();
    }

    @ApiOperation(value = "Update an order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated order"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PutMapping(path = "/orders", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> updateOrder(@RequestBody OrderRequestData order) {
        log.debug("Started updating order {}", order);

        OrderResponseData orderUpdated = orderService.updateOrder(order);

        log.debug("Updated order {}", orderUpdated);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Get list of orders", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved orders")
    })
    @GetMapping(path = "/orders", produces = "application/json")
    public ResponseEntity<List<OrderResponseData>> getOrders(
            @RequestParam(name = "from", defaultValue = "01-01-1970")
            @DateTimeFormat(pattern = "dd-MM-yyyy") Date from,
            @RequestParam(name = "to", defaultValue = "01-01-2100")
            @DateTimeFormat(pattern = "dd-MM-yyyy") Date to
    ) {
        log.debug("Started getting orders");

        List<OrderResponseData> orders = orderService.getOrders(from, to);

        log.debug("Got orders {}", orders.size());

        return ResponseEntity.ok(orders);
    }

    @ApiOperation(value = "Get an order", response = OrderResponseData.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved order"),
            @ApiResponse(code = 404, message = "Order not found")
    })
    @GetMapping(path = "/orders/{orderId}", produces = "application/json")
    public ResponseEntity<OrderResponseData> getOrder(@PathVariable String orderId) {
        log.debug("Started getting order {}", orderId);

        OrderResponseData order = orderService.getOrder(orderId);

        log.debug("Got order {}", order);

        return ResponseEntity.ok(order);
    }
}
