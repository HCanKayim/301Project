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
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  CircularProgress
} from '@mui/material';
import api from '../services/api';

function Announcements() {
  const [announcements, setAnnouncements] = useState([]);
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [openDialog, setOpenDialog] = useState(false);
  const [newAnnouncement, setNewAnnouncement] = useState({
    title: '',
    content: '',
    courseId: '',
    authorId: JSON.parse(localStorage.getItem('user'))?.id,
    authorName: JSON.parse(localStorage.getItem('user'))?.username
  });

  const user = JSON.parse(localStorage.getItem('user'));
  const isTeacher = user?.role === 'TEACHER';

  useEffect(() => {
    fetchAnnouncements();
    if (isTeacher) {
      fetchCourses();
    }
  }, [isTeacher]);

  const fetchAnnouncements = async () => {
    try {
      const response = await api.get('/api/announcements');
      setAnnouncements(response.data);
      setLoading(false);
    } catch (error) {
      setError('Error fetching announcements');
      setLoading(false);
    }
  };

  const fetchCourses = async () => {
    try {
      const response = await api.get('/api/courses');
      // If teacher, only show their courses
      const teacherCourses = response.data.filter(course => course.instructorId === user.id);
      setCourses(teacherCourses);
    } catch (error) {
      console.error('Error fetching courses:', error);
    }
  };

  const handleCreateAnnouncement = async () => {
    try {
      if (!newAnnouncement.title || !newAnnouncement.content) {
        setError('Please fill in all required fields');
        return;
      }
      await api.post('/api/announcements', newAnnouncement);
      setOpenDialog(false);
      setNewAnnouncement({
        title: '',
        content: '',
        courseId: '',
        authorId: user?.id,
        authorName: user?.username
      });
      fetchAnnouncements();
    } catch (error) {
      console.error('Error creating announcement:', error);
      setError('Error creating announcement');
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
          <Typography variant="h4">Announcements</Typography>
          {isTeacher && (
            <Button variant="contained" color="primary" onClick={() => setOpenDialog(true)}>
              Create Announcement
            </Button>
          )}
        </Grid>

        {error && (
          <Grid item xs={12}>
            <Typography color="error">{error}</Typography>
          </Grid>
        )}

        {announcements.map(announcement => (
          <Grid item xs={12} key={announcement.id}>
            <Paper sx={{ padding: 3 }}>
              <Typography variant="h6">{announcement.title}</Typography>
              <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                By: {announcement.authorName}
              </Typography>
              {announcement.courseId && courses.find(c => c.id === announcement.courseId) && (
                <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                  Course: {courses.find(c => c.id === announcement.courseId).title}
                </Typography>
              )}
              <Typography variant="subtitle2" color="text.secondary" sx={{ marginTop: 1 }}>
                {new Date(announcement.createdAt).toLocaleString()}
              </Typography>
              <Typography variant="body1" sx={{ marginTop: 2 }}>
                {announcement.content}
              </Typography>
            </Paper>
          </Grid>
        ))}
      </Grid>

      <Dialog open={openDialog} onClose={() => setOpenDialog(false)} maxWidth="md" fullWidth>
        <DialogTitle>Create New Announcement</DialogTitle>
        <DialogContent>
          <TextField
            fullWidth
            required
            margin="normal"
            label="Title"
            value={newAnnouncement.title}
            onChange={(e) => setNewAnnouncement({ ...newAnnouncement, title: e.target.value })}
          />
          <TextField
            fullWidth
            required
            margin="normal"
            label="Content"
            multiline
            rows={4}
            value={newAnnouncement.content}
            onChange={(e) => setNewAnnouncement({ ...newAnnouncement, content: e.target.value })}
          />
          <FormControl fullWidth margin="normal">
            <InputLabel>Course (Optional)</InputLabel>
            <Select
              value={newAnnouncement.courseId}
              onChange={(e) => setNewAnnouncement({ ...newAnnouncement, courseId: e.target.value })}
              label="Course (Optional)"
            >
              <MenuItem value="">None</MenuItem>
              {courses.map(course => (
                <MenuItem key={course.id} value={course.id}>
                  {course.title} ({course.code})
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)}>Cancel</Button>
          <Button 
            onClick={handleCreateAnnouncement} 
            variant="contained" 
            color="primary"
            disabled={!newAnnouncement.title || !newAnnouncement.content}
          >
            Create
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}

export default Announcements; 