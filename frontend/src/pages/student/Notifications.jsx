import React, { useEffect, useState } from 'react';
import api from '../../api/axiosConfig';

const Notifications = () => {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchNotifications();
  }, []);

  const fetchNotifications = async () => {
    try {
      const res = await api.get('/notifications/my');
      setNotifications(res.data);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleRead = async (id) => {
    try {
      await api.put(`/notifications/${id}/read`);
      setNotifications(notifications.filter(n => n.id !== id));
    } catch (error) {
      console.error(error);
    }
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div className="page-container">
      <h2>Notifications</h2>
      {notifications.length === 0 ? <p>No new notifications.</p> : (
        <ul className="notification-list">
          {notifications.map(n => (
            <li key={n.id} className="notification-item">
              <p><strong>{n.type}</strong></p>
              <p>{n.message}</p>
              <button onClick={() => handleRead(n.id)}>Mark as Read</button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};
export default Notifications;
