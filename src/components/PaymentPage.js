import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import axios from 'axios';

export default function Component() {
  const navigate = useNavigate();
  const location = useLocation();
  const { cartItems, finalPrice, userAddress, totalPrice, discountCode, cartId, customerId, restaurantId } = location.state || {};

  console.log(location.state)
  const [paymentMethod, setPaymentMethod] = useState('card');
  const [paymentDetails, setPaymentDetails] = useState({
    cardNumber: '',
    validThru: '',
    cvv: '',
    upiId: '',
  });
  const [error, setError] = useState('');

  const handlePaymentChange = (e) => {
    setPaymentDetails({ ...paymentDetails, [e.target.name]: e.target.value });
  };

  const handlePaymentMethodChange = (e) => {
    setPaymentMethod(e.target.value);
    // Reset payment details when switching methods
    setPaymentDetails({
      cardNumber: '',
      validThru: '',
      cvv: '',
      upiId: '',
    });
  };

  const handlePayment = async () => {
    try {
      // Validate payment details
      if (paymentMethod === 'card') {
        if (!paymentDetails.cardNumber || !paymentDetails.validThru || !paymentDetails.cvv) {
          setError('Please fill in all card details.');
          return;
        }
      } else if (paymentMethod === 'upi') {
        if (!paymentDetails.upiId) {
          setError('Please enter your UPI ID.');
          return;
        }
      }

      const discountAmount = totalPrice - finalPrice;
      const newOrder = {
        neworder: {
          totalAmount: finalPrice,
          orderStatus: 'Pending',
          paymentMethod: paymentMethod,
          orderDate: new Date().toISOString(),
          deliverAddress: userAddress,
          discountApplied: discountAmount > 0,
          discountAmount: discountAmount,
        },
        customer_id: customerId,
        restaurant_id: restaurantId,
        cart_id: cartId,
      };
      console.log(newOrder)

      const orderResponse = await axios.post('http://localhost:8888/order/add', newOrder);
      const createdOrderId = orderResponse.data.order_id;

      const paymentData = {
        orderId: createdOrderId,
        restaurantId: restaurantId,
        totalAmount: finalPrice,
        paymentMethod: paymentMethod,
        status: 'Completed'
      };

      await axios.post('http://localhost:8888/payments', paymentData);

      navigate('/order-confirmation', { 
        state: { 
          confirmed: true, 
          orderId: createdOrderId, 
          finalPrice,
          userAddress
        } 
      });
    } catch (error) {
      console.error('Error processing payment:', error);
      setError('There was an error processing your payment. Please try again.');
    }
  };

  return (
    <div className="container mt-5">
      <h1 className="text-center mb-4">Payment</h1>
      <div className="card">
        <div className="card-body">
          <h5 className="card-title">Order Summary</h5>
          <p><strong>Total Amount:</strong> Rs. {totalPrice.toFixed(2)}</p>
          {discountCode && <p><strong>Discount Applied:</strong> {discountCode}</p>}
          <p><strong>Final Amount:</strong> Rs. {finalPrice.toFixed(2)}</p>
          <p><strong>Delivery Address:</strong> {userAddress}</p>
          <h5 className="mt-4">Payment Method</h5>
          <select
            className="form-select mb-3"
            value={paymentMethod}
            onChange={handlePaymentMethodChange}
          >
            <option value="card">Credit/Debit Card</option>
            <option value="upi">UPI</option>
          </select>
          {paymentMethod === 'card' && (
            <div>
              <input
                type="text"
                className="form-control mb-2"
                name="cardNumber"
                placeholder="Card Number"
                value={paymentDetails.cardNumber}
                onChange={handlePaymentChange}
              />
              <input
                type="text"
                className="form-control mb-2"
                name="validThru"
                placeholder="Valid Thru (MM/YY)"
                value={paymentDetails.validThru}
                onChange={handlePaymentChange}
              />
              <input
                type="password"
                className="form-control mb-2"
                name="cvv"
                placeholder="CVV"
                value={paymentDetails.cvv}
                onChange={handlePaymentChange}
              />
            </div>
          )}
          {paymentMethod === 'upi' && (
            <input
              type="text"
              className="form-control mb-2"
              name="upiId"
              placeholder="Enter your UPI ID"
              value={paymentDetails.upiId}
              onChange={handlePaymentChange}
            />
          )}
          {error && <div className="alert alert-danger mt-3">{error}</div>}
          <button className="btn btn-primary mt-3 w-100" onClick={handlePayment}>
            Pay Now
          </button>
        </div>
      </div>
    </div>
  );
} 