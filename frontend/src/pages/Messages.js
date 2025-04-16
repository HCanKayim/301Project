import React, { useState, useEffect } from 'react';
import {
  Container,
  Grid,
  Paper,
  Typography,
  Button,
  TextField,
  List,
  ListItem,
  ListItemText,
  ListItemSecondaryAction,
  IconButton,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  CircularProgress,
  Divider,
  Select,
  MenuItem,
  FormControl,
  InputLabel
} from '@mui/material';
import { Delete as DeleteIcon, Send as SendIcon } from '@mui/icons-material';
import api from '../services/api';

function Messages() {
  const [messages, setMessages] = useState([]);
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedUser, setSelectedUser] = useState('');
  const [newMessage, setNewMessage] = useState({
    receiverId: '',
    content: '',
    senderId: '',
    senderName: '',
    receiverName: ''
  });

  const user = JSON.parse(localStorage.getItem('user'));

  useEffect(() => {
    fetchMessages();
    fetchUsers();
  }, []);

  const fetchMessages = async () => {
    try {
      const response = await api.get(`/api/messages/user/${user.id}`);
      setMessages(response.data);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching messages:', error);
      setError('Error fetching messages');
      setLoading(false);
    }
  };

  const fetchUsers = async () => {
    try {
      const response = await api.get('/api/users');
      const otherUsers = response.data.filter(u => u.id !== user.id);
      setUsers(otherUsers);
    } catch (error) {
      console.error('Error fetching users:', error);
    }
  };

  const handleSendMessage = async () => {
    if (!newMessage.content.trim() || !selectedUser) {
      return;
    }

    const receiver = users.find(u => u.id === selectedUser);
    if (!receiver) {
      return;
    }

    try {
      const messageData = {
        ...newMessage,
        senderId: user.id,
        senderName: user.username,
        receiverId: receiver.id,
        receiverName: receiver.username,
        read: false,
        createdAt: new Date().toISOString()
      };

      await api.post('/api/messages', messageData);
      setOpenDialog(false);
      setNewMessage({ receiverId: '', content: '', senderId: '', senderName: '', receiverName: '' });
      setSelectedUser('');
      fetchMessages();
    } catch (error) {
      console.error('Error sending message:', error);
      setError('Error sending message');
    }
  };

  const handleDeleteMessage = async (messageId) => {
    try {
      await api.delete(`/api/messages/${messageId}`);
      fetchMessages();
    } catch (error) {
      console.error('Error deleting message:', error);
      setError('Error deleting message');
    }
  };

  const formatTimestamp = (timestamp) => {
    if (!timestamp) return '';
    try {
      // Parse the ISO string or the LocalDateTime format
      const date = new Date(timestamp);
      if (isNaN(date.getTime())) {
        // If direct parsing fails, try parsing the LocalDateTime format
        const [datePart, timePart] = timestamp.split('T');
        const [year, month, day] = datePart.split('-');
        const [hour, minute, secondPart] = timePart.split(':');
        const second = secondPart.split('.')[0]; // Remove any milliseconds
        const formattedDate = new Date(year, month - 1, day, hour, minute, second);
        return formattedDate.toLocaleString();
      }
      return date.toLocaleString();
    } catch (error) {
      console.error('Error formatting date:', error);
      return 'Date unavailable';
    }
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
      <Grid container spacing={3}>
        <Grid item xs={12}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h5" gutterBottom>
              Messages
            </Typography>
            <Button
              variant="contained"
              color="primary"
              onClick={() => setOpenDialog(true)}
              sx={{ mb: 2 }}
            >
              New Message
            </Button>
            {error && (
              <Typography color="error" sx={{ mb: 2 }}>
                {error}
              </Typography>
            )}
            <List>
              {messages.length === 0 ? (
                <Typography variant="body1" sx={{ textAlign: 'center', mt: 2 }}>
                  No messages yet
                </Typography>
              ) : (
                messages.map((message) => (
                  <React.Fragment key={message.id}>
                    <ListItem>
                      <ListItemText
                        primary={
                          <Typography>
                            {message.senderId === user.id ? (
                              <>To: {message.receiverName}</>
                            ) : (
                              <>From: {message.senderName}</>
                            )}
                          </Typography>
                        }
                        secondary={
                          <>
                            <Typography variant="body2">{message.content}</Typography>
                            <Typography variant="caption" color="textSecondary">
                              {formatTimestamp(message.createdAt)}
                            </Typography>
                          </>
                        }
                      />
                      <ListItemSecondaryAction>
                        <IconButton
                          edge="end"
                          aria-label="delete"
                          onClick={() => handleDeleteMessage(message.id)}
                        >
                          <DeleteIcon />
                        </IconButton>
                      </ListItemSecondaryAction>
                    </ListItem>
                    <Divider />
                  </React.Fragment>
                ))
              )}
            </List>
          </Paper>
        </Grid>
      </Grid>

      <Dialog open={openDialog} onClose={() => setOpenDialog(false)} maxWidth="sm" fullWidth>
        <DialogTitle>New Message</DialogTitle>
        <DialogContent>
          <FormControl fullWidth sx={{ mt: 2, mb: 2 }}>
            <InputLabel>Recipient</InputLabel>
            <Select
              value={selectedUser}
              onChange={(e) => setSelectedUser(e.target.value)}
              label="Recipient"
            >
              {users.map((u) => (
                <MenuItem key={u.id} value={u.id}>
                  {u.username}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          <TextField
            autoFocus
            margin="dense"
            label="Message"
            type="text"
            fullWidth
            multiline
            rows={4}
            value={newMessage.content}
            onChange={(e) => setNewMessage({ ...newMessage, content: e.target.value })}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)}>Cancel</Button>
          <Button
            onClick={handleSendMessage}
            variant="contained"
            color="primary"
            endIcon={<SendIcon />}
            disabled={!newMessage.content.trim() || !selectedUser}
          >
            Send
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}

export default Messages; 