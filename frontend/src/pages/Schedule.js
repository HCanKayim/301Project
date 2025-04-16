import React, { useState, useEffect } from 'react';
import {
  Container,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
  CircularProgress
} from '@mui/material';
import api from '../services/api';

function Schedule() {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const user = JSON.parse(localStorage.getItem('user'));

  const timeSlots = Array.from({ length: 12 }, (_, i) => i + 9); // 9 AM to 8 PM
  const days = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY'];

  useEffect(() => {
    fetchCourses();
  }, []);

  const fetchCourses = async () => {
    try {
      const response = await api.get('/api/courses');
      // Filter courses based on user role
      const relevantCourses = user.role === 'STUDENT'
        ? response.data.filter(course => course.enrolledStudents?.includes(user.id))
        : response.data.filter(course => course.instructorId === user.id);
      setCourses(relevantCourses);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching courses:', error);
      setError('Error fetching course schedule');
      setLoading(false);
    }
  };

  const formatTime = (hour) => {
    return `${hour % 12 || 12}:00 ${hour < 12 ? 'AM' : 'PM'}`;
  };

  const getCourseForTimeSlot = (day, hour) => {
    return courses.find(course => {
      if (course.day !== day) return false;
      const courseStartHour = parseInt(course.startTime.split(':')[0]);
      const courseEndHour = parseInt(course.endTime.split(':')[0]);
      return hour >= courseStartHour && hour < courseEndHour;
    });
  };

  if (loading) {
    return (
      <Container sx={{ mt: 4, display: 'flex', justifyContent: 'center' }}>
        <CircularProgress />
      </Container>
    );
  }

  return (
    <Container sx={{ mt: 4 }}>
      <Typography variant="h4" gutterBottom>
        Course Schedule
      </Typography>
      {error && (
        <Typography color="error" sx={{ mb: 2 }}>
          {error}
        </Typography>
      )}
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Time</TableCell>
              {days.map(day => (
                <TableCell key={day} align="center">{day}</TableCell>
              ))}
            </TableRow>
          </TableHead>
          <TableBody>
            {timeSlots.map(hour => (
              <TableRow key={hour}>
                <TableCell>{formatTime(hour)}</TableCell>
                {days.map(day => {
                  const course = getCourseForTimeSlot(day, hour);
                  return (
                    <TableCell 
                      key={day} 
                      align="center"
                      sx={{
                        backgroundColor: course ? '#e3f2fd' : 'inherit',
                        borderLeft: '1px solid rgba(224, 224, 224, 1)'
                      }}
                    >
                      {course && (
                        <>
                          <Typography variant="subtitle2">{course.title}</Typography>
                          <Typography variant="caption">{course.code}</Typography>
                        </>
                      )}
                    </TableCell>
                  );
                })}
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Container>
  );
}

export default Schedule; 