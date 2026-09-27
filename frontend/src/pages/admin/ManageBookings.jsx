import React, { useEffect, useState } from 'react';
import api from '../../api/axiosConfig';

const ManageBookings = () => {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchBookings();
  }, []);

  const fetchBookings = async () => {
    try {
      const res = await api.get('/bookings');
      setBookings(res.data);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleStatusUpdate = async (id, action) => {
    try {
      await api.put(`/bookings/${id}/${action}`);
      fetchBookings();
    } catch (error) {
      alert('Action failed');
    }
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div className="page-container">
      <h2>Manage Bookings</h2>
      <table className="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Facility ID</th>
            <th>Date</th>
            <th>Time</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {bookings.map(b => (
            <tr key={b.id}>
              <td>{b.id}</td>
              <td>{b.facilityId}</td>
              <td>{b.bookingDate}</td>
              <td>{b.startTime} - {b.endTime}</td>
              <td>{b.status}</td>
              <td>
                {b.status === 'PENDING' && (
                  <>
                    <button onClick={() => handleStatusUpdate(b.id, 'approve')} className="btn-primary" style={{marginRight: '0.5rem'}}>Approve</button>
                    <button onClick={() => handleStatusUpdate(b.id, 'reject')} className="btn-danger">Reject</button>
                  </>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
export default ManageBookings;
