import React, { useState, useEffect } from 'react';
import { Container, Grid, Paper, Typography, CircularProgress } from '@mui/material';
import api from '../services/api';

function Dashboard() {
  const [userData, setUserData] = useState({
    courses: [],
    announcements: [],
    messages: []
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const user = JSON.parse(localStorage.getItem('user'));
        
        // Fetch courses and announcements in parallel
        const [coursesResponse, announcementsResponse] = await Promise.all([
          api.get('/api/courses'),
          api.get('/api/announcements')
        ]);

        setUserData({
          courses: coursesResponse.data.slice(0, 5),
          announcements: announcementsResponse.data.slice(0, 5),
          messages: [] // Messages feature will be implemented later
        });
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
        setError('Error loading dashboard data');
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  if (loading) {
    return (
      <Container sx={{ display: 'flex', justifyContent: 'center', marginTop: 6 }}>
        <CircularProgress />
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ marginTop: 3 }}>
      <Grid container spacing={3}>
        <Grid item xs={12}>
          <Typography variant="h4">Dashboard</Typography>
        </Grid>

        {error && (
          <Grid item xs={12}>
            <Typography color="error">{error}</Typography>
          </Grid>
        )}
        
        {/* Available Courses */}
        <Grid item xs={12} md={6}>
          <Paper sx={{ padding: 3 }}>
            <Typography variant="h6" gutterBottom>Available Courses</Typography>
            {userData.courses.length > 0 ? (
              userData.courses.map(course => (
                <Paper key={course.id} sx={{ padding: 2, margin: '10px 0', backgroundColor: '#f5f5f5' }}>
                  <Typography variant="subtitle1" sx={{ fontWeight: 'bold' }}>{course.title}</Typography>
                  <Typography variant="body2" color="textSecondary">Code: {course.code}</Typography>
                  <Typography variant="body2">
                    Enrolled: {course.enrolled}/{course.capacity}
                  </Typography>
                </Paper>
              ))
            ) : (
              <Typography variant="body1" color="textSecondary">No courses available</Typography>
            )}
          </Paper>
        </Grid>

        {/* Recent Announcements */}
        <Grid item xs={12} md={6}>
          <Paper sx={{ padding: 3 }}>
            <Typography variant="h6" gutterBottom>Recent Announcements</Typography>
            {userData.announcements.length > 0 ? (
              userData.announcements.map(announcement => (
                <Paper key={announcement.id} sx={{ padding: 2, margin: '10px 0', backgroundColor: '#f5f5f5' }}>
                  <Typography variant="subtitle1" sx={{ fontWeight: 'bold' }}>{announcement.title}</Typography>
                  <Typography variant="body2" color="textSecondary">
                    By: {announcement.authorName}
                  </Typography>
                  <Typography variant="body2">
                    {announcement.content?.substring(0, 100)}
                    {announcement.content?.length > 100 ? '...' : ''}
                  </Typography>
                </Paper>
              ))
            ) : (
              <Typography variant="body1" color="textSecondary">No announcements available</Typography>
            )}
          </Paper>
        </Grid>
      </Grid>
    </Container>
  );
}

export default Dashboard; 