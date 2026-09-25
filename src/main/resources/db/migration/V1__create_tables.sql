CREATE TABLE customers (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    city        VARCHAR(80),
    created_at  DATE NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE products (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(100) NOT NULL,
    category  VARCHAR(50) NOT NULL,
    price     DECIMAL(10, 2) NOT NULL,
    stock     INT NOT NULL DEFAULT 0
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE orders (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    customer_id  INT NOT NULL,
    order_date   DATE NOT NULL,
    status       VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES customers (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE order_items (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    order_id    INT NOT NULL,
    product_id  INT NOT NULL,
    quantity    INT NOT NULL,
    unit_price  DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
