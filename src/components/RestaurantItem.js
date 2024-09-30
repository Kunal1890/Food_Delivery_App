import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Card, Button, Container, Row, Col } from 'react-bootstrap';
import { useNavigate,useLocation } from 'react-router-dom';
import '../styling/Menu.css';

const RestaurantItem = () => {
  const [menu, setMenu] = useState([]);
  const [cart, setCart] = useState([]);
  const [error, setError] = useState('');
  const [userId] = useState(1); // Hardcoding userId for simplicity
  const navigate = useNavigate();
  const location = useLocation();
  const { confirmed, orderId, finalPrice, userAddress } = location.state || {};

  useEffect(() => {
    const fetchMenuDetails = async () => {
      try {
        const menuResponse = await axios.get('http://localhost:8888/restaurant/2/items');
        console.log(menuResponse.data)
        setMenu(menuResponse.data);
      } catch (err) {
        setError('Failed to load restaurant menu.');
      }
    };

    fetchMenuDetails();
  }, []);

  // Add item to cart (calls backend)
  const handleAddToCart = async (item) => {
    try {
      const cartItem = {
        itemId: 1, // Use item's actual ID
        quantity: 1, // Default to quantity 1
      };

      const response = await axios.post(`http://localhost:8888/cart/${userId}/item`, cartItem);

      if (response.status === 200) {
        // Update cart state
        setCart([...cart, item]);
      } else {
        setError('Failed to add item to the cart.');
      }
    } catch (err) {
      setError('Error adding item to the cart.');
    }
  };

  // Remove item from cart
  const handleRemoveFromCart = async (itemId) => {
    try {
      await axios.delete(`http://localhost:8888/cart/${userId}/items/${itemId}`);

      // Update cart state after successful removal
      setCart(cart.filter((item) => item.id !== itemId));
    } catch (err) {
      setError('Error removing item from the cart.');
    }
  };

  const handleCheckout = () => {
    navigate('/checkout');
  };

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  return (
    <Container className="restaurant-page mt-5">
      <Row>
        <Col md={8}>
          <Card className="mb-4 ">
            <Card.Header>
              <h2>Menu</h2>
            </Card.Header>
            <Card.Body>
              <ul className="menu-list">
                {menu.length > 0 ? (
                  menu.map((item) => (
                    <li key={item.id} className="menu-item mb-3">
                      <div className="d-flex justify-content-between align-items-center">
                        <div>
                          <p><strong>{item.itemName}</strong></p>
                          <p>{item.description}</p>
                          <p><strong>Price:</strong> ${item.itemCost}</p>
                        </div>
                        <Button variant="primary" onClick={() => handleAddToCart(item)}>
                          Add to Cart
                        </Button>
                      </div>
                    </li>
                  ))
                ) : (
                  <p>No menu items available.</p>
                )}
              </ul>
            </Card.Body>
          </Card>
        </Col>

        <Col md={4}>
          <Card className="shadow-sm">
            <Card.Header>
              <h2>Your Cart</h2>
            </Card.Header>
            <Card.Body>
              <div className="box">
                {cart.length > 0 ? (
                  <ul className="cart-list">
                    {cart.map((item) => (
                      <li key={item.id} className="cart-item mb-3">
                        <div className="d-flex justify-content-between align-items-center">
                          <div>
                            <p><strong>{item.itemName}</strong></p>
                            <p><strong>Price:</strong> ${item.itemCost}</p>
                          </div>
                          <Button variant="danger" onClick={() => handleRemoveFromCart(item.id)}>
                            Remove
                          </Button>
                        </div>
                      </li>
                    ))}
                  </ul>
                ) : (
                  <p>Your cart is empty.</p>
                )}
              </div>
            </Card.Body>
          </Card>

          {cart.length > 0 && (
            <div className="mt-3">
              <Button variant="success" className="w-100" onClick={handleCheckout}>
                Go To Checkout
              </Button>
            </div>
          )}
        </Col>
      </Row>
    </Container>
  );
};

export default RestaurantItem;
