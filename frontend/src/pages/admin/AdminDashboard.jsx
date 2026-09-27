import React, { useContext } from 'react';
import { AuthContext } from '../../context/AuthContext';

const AdminDashboard = () => {
  const { user } = useContext(AuthContext);
  return (
    <div className="page-container">
      <h2>Admin Dashboard</h2>
      <p>Welcome, {user?.name}. Use the navigation menu to manage campus resources.</p>
    </div>
  );
};
export default AdminDashboard;
