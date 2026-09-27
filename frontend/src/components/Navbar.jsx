import React, { useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import './Navbar.css';

const Navbar = () => {
  const { user, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar-brand">Campus Services Platform</div>
      <div className="navbar-links">
        {user ? (
          <>
            {user.role === 'ADMIN' ? (
              <>
                <Link to="/admin">Dashboard</Link>
                <Link to="/admin/facilities">Facilities</Link>
                <Link to="/admin/bookings">Bookings</Link>
                <Link to="/admin/users">Users</Link>
              </>
            ) : (
              <>
                <Link to="/student">Dashboard</Link>
                <Link to="/student/facilities">Facilities</Link>
                <Link to="/student/bookings">My Bookings</Link>
                <Link to="/student/notifications">Notifications</Link>
                <Link to="/student/profile">Profile</Link>
              </>
            )}
            <button onClick={handleLogout} className="btn-logout">Logout</button>
          </>
        ) : (
          <>
            <Link to="/login">Login</Link>
            <Link to="/register">Register</Link>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
