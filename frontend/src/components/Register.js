const handleSubmit = async (e) => {
    e.preventDefault();
    try {
        const response = await register(formData);
        // Handle successful registration
        navigate('/login');
    } catch (error) {
        setError(error.response?.data?.message || 'Registration failed. Please try again.');
    }
}; 