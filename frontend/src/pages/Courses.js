import React, { useState, useEffect } from 'react';
import { 
  Container, 
  Grid, 
  Paper, 
  Typography, 
  Button, 
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  CircularProgress,
  Select,
  MenuItem,
  FormControl,
  InputLabel
} from '@mui/material';
import api from '../services/api';

function Courses() {
  const user = JSON.parse(localStorage.getItem('user'));
  const isTeacher = user?.role === 'TEACHER';

  const [courses, setCourses] = useState([]);
  const [instructors, setInstructors] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [openDialog, setOpenDialog] = useState(false);
  const [newCourse, setNewCourse] = useState({
    title: '',
    code: '',
    description: '',
    capacity: 30,
    instructorId: user?.id || '',
    day: '',
    startTime: '',
    endTime: ''
  });

  const days = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY'];
  const timeSlots = Array.from({ length: 12 }, (_, i) => {
    const hour = i + 9;
    return `${hour.toString().padStart(2, '0')}:00`;
  });

  useEffect(() => {
    fetchCourses();
  }, []);

  const fetchInstructors = async (courses) => {
    try {
      const response = await api.get('/api/users');
      const users = response.data;
      const instructorMap = {};
      users.forEach(user => {
        instructorMap[user.id] = user;
      });
      setInstructors(instructorMap);
    } catch (error) {
      console.error('Error fetching instructors:', error);
    }
  };

  const fetchCourses = async () => {
    try {
      const response = await api.get('/api/courses');
      setCourses(response.data);
      await fetchInstructors(response.data);
      setLoading(false);
    } catch (error) {
      setError('Error fetching courses');
      setLoading(false);
    }
  };

  const handleCreateCourse = async () => {
    try {
      if (!newCourse.title || !newCourse.code || !newCourse.description || 
          !newCourse.day || !newCourse.startTime || !newCourse.endTime) {
        setError('Please fill in all required fields');
        return;
      }

      // Validate time range
      const startHour = parseInt(newCourse.startTime.split(':')[0]);
      const endHour = parseInt(newCourse.endTime.split(':')[0]);
      if (startHour >= endHour) {
        setError('End time must be after start time');
        return;
      }

      // Format the course data for the backend
      const courseData = {
        ...newCourse,
        startTime: newCourse.startTime + ':00.000',  // Convert to LocalTime format
        endTime: newCourse.endTime + ':00.000'       // Convert to LocalTime format
      };

      const response = await api.post('/api/courses', courseData);
      if (response.data) {
        setOpenDialog(false);
        setNewCourse({
          title: '',
          code: '',
          description: '',
          capacity: 30,
          instructorId: user?.id || '',
          day: '',
          startTime: '',
          endTime: ''
        });
        setError(''); // Clear any existing errors
        fetchCourses();
      }
    } catch (error) {
      console.error('Error creating course:', error);
      const errorMessage = error.response?.data || error.message;
      setError('Error creating course: ' + errorMessage);
    }
  };

  const handleEnroll = async (courseId) => {
    try {
      const response = await api.post(`/api/courses/${courseId}/enroll`, {
        studentId: user.id
      });
      if (response.status === 200) {
        setError(''); // Clear any existing errors
        fetchCourses();
      }
    } catch (error) {
      console.error('Enrollment error:', error);
      const errorMessage = error.response?.data || error.message;
      setError('Error enrolling in course: ' + errorMessage);
    }
  };

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
        <Grid item xs={12} sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Typography variant="h4">Courses</Typography>
          {isTeacher && (
            <Button variant="contained" color="primary" onClick={() => setOpenDialog(true)}>
              Create Course
            </Button>
          )}
        </Grid>

        {error && (
          <Grid item xs={12}>
            <Typography color="error">{error}</Typography>
          </Grid>
        )}

        {courses.map(course => (
          <Grid item xs={12} md={6} key={course.id}>
            <Paper sx={{ padding: 3 }}>
              <Typography variant="h6">{course.title}</Typography>
              <Typography variant="subtitle1">Code: {course.code}</Typography>
              <Typography variant="subtitle2" color="textSecondary" sx={{ mt: 1 }}>
                Instructor: {instructors[course.instructorId]?.username || 'Unknown'}
              </Typography>
              <Typography variant="body1" sx={{ marginTop: 1.5 }}>
                {course.description}
              </Typography>
              <Typography variant="body2" sx={{ marginTop: 1 }}>
                Schedule: {course.day}, {course.startTime} - {course.endTime}
              </Typography>
              <Typography variant="body2" sx={{ marginTop: 1 }}>
                Capacity: {course.enrolled}/{course.capacity}
              </Typography>
              {!isTeacher && (
                <Button
                  variant="contained"
                  color="primary"
                  sx={{ marginTop: 1.5 }}
                  onClick={() => handleEnroll(course.id)}
                  disabled={course.enrolled >= course.capacity || course.enrolledStudents?.includes(user.id)}
                >
                  {course.enrolledStudents?.includes(user.id) ? 'Enrolled' : 'Enroll'}
                </Button>
              )}
            </Paper>
          </Grid>
        ))}
      </Grid>

      <Dialog open={openDialog} onClose={() => setOpenDialog(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Create New Course</DialogTitle>
        <DialogContent>
          <TextField
            fullWidth
            margin="normal"
            label="Course Title"
            value={newCourse.title}
            onChange={(e) => setNewCourse({ ...newCourse, title: e.target.value })}
          />
          <TextField
            fullWidth
            margin="normal"
            label="Course Code"
            value={newCourse.code}
            onChange={(e) => setNewCourse({ ...newCourse, code: e.target.value })}
          />
          <TextField
            fullWidth
            margin="normal"
            label="Description"
            multiline
            rows={4}
            value={newCourse.description}
            onChange={(e) => setNewCourse({ ...newCourse, description: e.target.value })}
          />
          <TextField
            fullWidth
            margin="normal"
            label="Capacity"
            type="number"
            value={newCourse.capacity}
            onChange={(e) => setNewCourse({ ...newCourse, capacity: parseInt(e.target.value) })}
          />
          <FormControl fullWidth margin="normal">
            <InputLabel>Day</InputLabel>
            <Select
              value={newCourse.day}
              label="Day"
              onChange={(e) => setNewCourse({ ...newCourse, day: e.target.value })}
            >
              {days.map(day => (
                <MenuItem key={day} value={day}>{day}</MenuItem>
              ))}
            </Select>
          </FormControl>
          <FormControl fullWidth margin="normal">
            <InputLabel>Start Time</InputLabel>
            <Select
              value={newCourse.startTime}
              label="Start Time"
              onChange={(e) => setNewCourse({ ...newCourse, startTime: e.target.value })}
            >
              {timeSlots.map(time => (
                <MenuItem key={time} value={time}>{time}</MenuItem>
              ))}
            </Select>
          </FormControl>
          <FormControl fullWidth margin="normal">
            <InputLabel>End Time</InputLabel>
            <Select
              value={newCourse.endTime}
              label="End Time"
              onChange={(e) => setNewCourse({ ...newCourse, endTime: e.target.value })}
            >
              {timeSlots.map(time => (
                <MenuItem key={time} value={time}>{time}</MenuItem>
              ))}
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)}>Cancel</Button>
          <Button onClick={handleCreateCourse} variant="contained" color="primary">
            Create
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}

export default Courses; 