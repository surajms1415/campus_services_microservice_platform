import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Layout from './components/Layout';
import ProtectedRoute from './components/ProtectedRoute';

import Login from './pages/public/Login';
import Register from './pages/public/Register';
import Dashboard from './pages/student/Dashboard';
import Facilities from './pages/student/Facilities';
import FacilityDetails from './pages/student/FacilityDetails';
import BookFacility from './pages/student/BookFacility';
import MyBookings from './pages/student/MyBookings';
import Notifications from './pages/student/Notifications';
import Profile from './pages/student/Profile';
import AdminDashboard from './pages/admin/AdminDashboard';
import ManageFacilities from './pages/admin/ManageFacilities';
import ManageBookings from './pages/admin/ManageBookings';
import ManageUsers from './pages/admin/ManageUsers';

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          
          <Route element={<Layout />}>
            {/* Student Routes */}
            <Route element={<ProtectedRoute allowedRole="STUDENT" />}>
              <Route path="/student" element={<Dashboard />} />
              <Route path="/student/facilities" element={<Facilities />} />
              <Route path="/student/facilities/:id" element={<FacilityDetails />} />
              <Route path="/student/facilities/:id/book" element={<BookFacility />} />
              <Route path="/student/bookings" element={<MyBookings />} />
              <Route path="/student/notifications" element={<Notifications />} />
              <Route path="/student/profile" element={<Profile />} />
            </Route>

            {/* Admin Routes */}
            <Route element={<ProtectedRoute allowedRole="ADMIN" />}>
              <Route path="/admin" element={<AdminDashboard />} />
              <Route path="/admin/facilities" element={<ManageFacilities />} />
              <Route path="/admin/bookings" element={<ManageBookings />} />
              <Route path="/admin/users" element={<ManageUsers />} />
            </Route>
          </Route>
          
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}
export default App;
