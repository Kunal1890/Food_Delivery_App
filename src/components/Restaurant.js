import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate, useLocation } from "react-router-dom";

function RestaurantsList() {
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const location = useLocation();
  const { restaurantId } = location.state || {};
  const navigate = useNavigate(); // Call useNavigate correctly

  const handleNavigateToMenu = (restaurantId) => {
    navigate(`/menu/${restaurantId}`); // Pass restaurant ID as URL parameter
  };

  // Fetch all restaurants when the component mounts
  useEffect(() => {
    const fetchRestaurants = async () => {
      try {
        setLoading(true);
        const response = await axios.get("http://localhost:8888/restaurant/all");
        setRestaurants(response.data);
        setLoading(false);
      } catch (err) {
        setError("Error fetching restaurants list.");
        setLoading(false);
      }
    };

    fetchRestaurants();
  }, []);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="container">
      <h1 className="my-4">Restaurants List</h1>
      <div className="row">
        {restaurants.map((restaurant) => (
          
          <div key={restaurant.id} className="col-md-6 mb-4">
             { console.log(restaurant.restaurantId)}
            <div className="card">
              <div className="card-header">
                <h2>{restaurant.name}</h2>
              </div>
              <div className="card-body">
                <p><strong>Address:</strong> {restaurant.address}</p>
                <p><strong>Phone:</strong> {restaurant.mobileNo}</p>
                <p><strong>Rating:</strong> {restaurant.ratings} / 5</p>
                {/* Pass restaurant ID to the menu page */}
                <button onClick={() => handleNavigateToMenu(restaurant.restaurantId)}>Go To Menu</button>
              
                            </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default RestaurantsList;
