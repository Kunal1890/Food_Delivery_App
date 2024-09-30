import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import logo from '../assets/MealKings.png'; // Optional: Add a logo image
import Header from './Header';

import Restaurant from '../components/Restaurant';

function Dashboard() {
  const navigate = useNavigate();

  useEffect(() => {
    const user = localStorage.getItem('registeredUser');
    // Uncomment this to enforce login requirement
    // if (!user) {
    //   alert('You must log in to view the dashboard');
    //   navigate('/login');
    // }
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem('registeredUser');
    navigate('/');
  };

  return (
    <>
    
      {/* Header Section */}
      <Header />

      {/* Main Dashboard Container */}
      <div className="container mt-4">
        <div className="text-center">
          {/* Logo and Heading */}
          <img src={logo} alt="Logo" className="mb-2" style={{ height: '50px' }} />
          <h2 className="mb-3">Dashboard</h2>
          <p>Welcome to your dashboard! You are logged in.</p>
        </div>

        {/* Restaurant Component */}
        <Restaurant />

        {/* Logout Button */}
        <div className="d-flex justify-content-center mt-3">
          <button className="btn btn-danger" onClick={handleLogout}>
            Logout
          </button>
        </div>
      </div>

    
    </>
  );
}

export default Dashboard;
