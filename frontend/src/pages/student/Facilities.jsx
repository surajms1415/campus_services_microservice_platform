import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../../api/axiosConfig';

const Facilities = () => {
  const [facilities, setFacilities] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');

  useEffect(() => {
    fetchFacilities();
  }, []);

  const fetchFacilities = async (q = '') => {
    try {
      const res = await api.get(`/facilities?search=${q}&page=0&size=50`);
      setFacilities(res.data.content || []);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    fetchFacilities(search);
  };

  if (loading) return <div>Loading facilities...</div>;

  return (
    <div className="page-container">
      <h2>Campus Facilities</h2>
      <form onSubmit={handleSearch} style={{marginBottom: '1rem'}}>
        <input type="text" placeholder="Search facilities..." value={search} onChange={e => setSearch(e.target.value)} />
        <button type="submit">Search</button>
      </form>
      <div className="grid-container">
        {facilities.map(f => (
          <div key={f.id} className="card">
            <h3>{f.name}</h3>
            <p>{f.location}</p>
            <p>Capacity: {f.capacity}</p>
            <p>Status: {f.available ? 'Available' : 'Unavailable'}</p>
            <Link to={`/student/facilities/${f.id}`} className="btn-link">View Details</Link>
          </div>
        ))}
      </div>
    </div>
  );
};
export default Facilities;
