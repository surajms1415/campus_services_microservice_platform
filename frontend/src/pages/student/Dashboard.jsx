import React, { useContext } from 'react';
import { AuthContext } from '../../context/AuthContext';

const Dashboard = () => {
  const { user } = useContext(AuthContext);
  return (
    <div className="page-container">
      <h2>Welcome, {user?.name}!</h2>
      <p>Use the navigation menu to browse facilities or view your bookings.</p>
    </div>
  );
};
export default Dashboard;
