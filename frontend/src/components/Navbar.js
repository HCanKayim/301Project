import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { AppBar, Toolbar, Typography, Button, Box, Chip, Avatar } from '@mui/material';
import { Person as PersonIcon } from '@mui/icons-material';

function Navbar() {
  const navigate = useNavigate();
  const isAuthenticated = localStorage.getItem('token');
  const user = JSON.parse(localStorage.getItem('user'));

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    navigate('/login');
  };

  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" sx={{ flexGrow: 1 }}>
          Student Hub
        </Typography>
        {isAuthenticated ? (
          <>
            <Button color="inherit" component={Link} to="/">Dashboard</Button>
            <Button color="inherit" component={Link} to="/courses">Courses</Button>
            <Button color="inherit" component={Link} to="/schedule">Schedule</Button>
            <Button color="inherit" component={Link} to="/announcements">Announcements</Button>
            <Button color="inherit" component={Link} to="/messages">Messages</Button>
            <Box sx={{ mx: 2, display: 'flex', alignItems: 'center' }}>
              <Chip
                avatar={<Avatar><PersonIcon /></Avatar>}
                label={`${user?.username} (${user?.role})`}
                variant="outlined"
                sx={{ 
                  color: 'white',
                  borderColor: 'white',
                  '& .MuiChip-avatar': {
                    color: 'white',
                    backgroundColor: 'transparent'
                  }
                }}
              />
            </Box>
            <Button color="inherit" onClick={handleLogout}>Logout</Button>
          </>
        ) : (
          <>
            <Button color="inherit" component={Link} to="/login">Login</Button>
            <Button color="inherit" component={Link} to="/register">Register</Button>
          </>
        )}
      </Toolbar>
    </AppBar>
  );
}

export default Navbar; 