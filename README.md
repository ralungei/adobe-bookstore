## Adobe Bookstore SpringBoot API
![Screenshot](adobelogo.png)

Online book store capable to receive book orders from customers and process them.


## Features

**Create a new Order:**

- The application receives orders in a JSON format through an HTTP API endpoint (POST).
- Orders contain a list of books and the quantity.
- Before registering the order, the system should check if there's enough stock to fulfill the order.
- This JSON file contains stock availability (stock.json)
- If one of the books in the order does not have enough stock we will reject the entire order.
- After stock validation, the order is marked as a success, and it would return a Unique Order Identifier to the caller of the HTTP API endpoint.
- If the order was processed we need to update available stock, taking into consideration:
- Updating stock should not be a blocker for replying to the customer.
- If the process of updating stock fails, should not cause an error in order processing.

**Retrieve Orders:**

- The application has an endpoint to extract a list of existing orders.
- The endpoint has a request parameter to indicate the format of requested data. It can be JSON or CSV.

## API Endpoints
**POST Order /api/orders**
```sh
> Body example (Order products ids and quantities)
[
    {"id": "d02b58ae-8731-451c-9acb-1941adf88501", "quantity": 1},
    {"id": "8d80c009-b3be-4d9d-95ba-cec1e7a2d52b", "quantity": 1}
]

> Return response example (Created order identifier)
1
```

**GET Order /api/orders?type=json**
```sh
POST Order /api/orders/
> Params
    type : "json" / "csv"
```
```sh
# Type CSV Response Example
    Identifier,Status,Order Identifier -  Quantity
    1,PENDING,[d02b58ae-8731-451c-9acb-1941adf88501-1]
```
```sh
#TYPE JSON Response Example
    [
        {
            "orderProducts": [
                {
                    "quantity": 1,
                    "product": {
                        "id": "d02b58ae-8731-451c-9acb-1941adf88501",
                        "name": "ullamco do voluptate cillum amet",
                        "quantity": 3
                    }
                }
            ],
            "id": 1,
            "status": "PENDING"
        }
    ]
```
