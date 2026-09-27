import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../../api/axiosConfig';

const FacilityDetails = () => {
  const { id } = useParams();
  const [facility, setFacility] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchFacility = async () => {
      try {
        const res = await api.get(`/facilities/${id}`);
        setFacility(res.data);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };
    fetchFacility();
  }, [id]);

  if (loading) return <div>Loading details...</div>;
  if (!facility) return <div>Facility not found.</div>;

  return (
    <div className="page-container">
      <h2>{facility.name}</h2>
      <p><strong>Description:</strong> {facility.description}</p>
      <p><strong>Location:</strong> {facility.location}</p>
      <p><strong>Capacity:</strong> {facility.capacity}</p>
      <p><strong>Status:</strong> {facility.available ? 'Available' : 'Unavailable'}</p>
      {facility.available && (
        <Link to={`/student/facilities/${id}/book`} className="btn-primary" style={{display: 'inline-block', marginTop: '1rem', textDecoration: 'none'}}>Book This Facility</Link>
      )}
    </div>
  );
};
export default FacilityDetails;
