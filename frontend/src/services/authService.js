import api from './api';

export const login = async (username, password) => {
  const response = await api.post('/api/auth/login', { username, password });
  return response.data;
};

export const register = async (userData) => {
  const response = await api.post('/api/auth/signup', userData);
  return response.data;
}; 