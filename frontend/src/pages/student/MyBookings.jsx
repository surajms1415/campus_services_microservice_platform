import React, { useEffect, useState } from 'react';
import api from '../../api/axiosConfig';

const MyBookings = () => {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchBookings();
  }, []);

  const fetchBookings = async () => {
    try {
      const res = await api.get('/bookings/my');
      setBookings(res.data);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = async (id) => {
    if (window.confirm('Are you sure you want to cancel this booking?')) {
      try {
        await api.put(`/bookings/${id}/cancel`);
        fetchBookings();
      } catch (error) {
        alert('Failed to cancel');
      }
    }
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div className="page-container">
      <h2>My Bookings</h2>
      <table className="data-table">
        <thead>
          <tr>
            <th>Facility ID</th>
            <th>Date</th>
            <th>Time</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {bookings.map(b => (
            <tr key={b.id}>
              <td>{b.facilityId}</td>
              <td>{b.bookingDate}</td>
              <td>{b.startTime} - {b.endTime}</td>
              <td>{b.status}</td>
              <td>
                {(b.status === 'PENDING' || b.status === 'APPROVED') && (
                  <button onClick={() => handleCancel(b.id)} className="btn-danger">Cancel</button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
export default MyBookings;
