import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../../api/axiosConfig';

const BookFacility = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [date, setDate] = useState('');
  const [startTime, setStartTime] = useState('');
  const [endTime, setEndTime] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/bookings', {
        facilityId: parseInt(id),
        bookingDate: date,
        startTime: startTime + ":00",
        endTime: endTime + ":00"
      });
      navigate('/student/bookings');
    } catch (err) {
      setError(err.response?.data || 'Booking failed');
    }
  };

  return (
    <div className="form-container">
      <h2>Book Facility</h2>
      {error && <p className="error">{error}</p>}
      <form onSubmit={handleSubmit}>
        <label>Date:</label>
        <input type="date" value={date} onChange={e => setDate(e.target.value)} required />
        <label>Start Time (HH:MM):</label>
        <input type="time" value={startTime} onChange={e => setStartTime(e.target.value)} required />
        <label>End Time (HH:MM):</label>
        <input type="time" value={endTime} onChange={e => setEndTime(e.target.value)} required />
        <button type="submit">Submit Booking</button>
      </form>
    </div>
  );
};
export default BookFacility;
